import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class FixInner {
    static int mask(int acc) {
        return acc & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED |
                Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_INTERFACE |
                Opcodes.ACC_ABSTRACT | Opcodes.ACC_SYNTHETIC | Opcodes.ACC_ANNOTATION | Opcodes.ACC_ENUM);
    }
    static class AccV extends ClassVisitor {
        int acc; boolean hasThis0 = false;
        AccV() { super(Opcodes.ASM9); }
        public void visit(int v, int a, String n, String s, String su, String[] i) { acc = a; }
        public FieldVisitor visitField(int a, String n, String d, String s, Object v) { if (n.equals("this$0")) hasThis0 = true; return null; }
        public MethodVisitor visitMethod(int a, String n, String d, String s, String[] e) { return null; }
        public AnnotationVisitor visitAnnotation(String d, boolean v) { return null; }
    }
    static InnerClassNode find(ClassNode cn, String bin) {
        for (InnerClassNode ic : cn.innerClasses) if (ic.name.equals(bin)) return ic;
        return null;
    }
    public static void main(String[] a) throws Exception {
        String path = a[0];
        Map<String, byte[]> classes = new LinkedHashMap<>();
        try (ZipInputStream z = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry e;
            while ((e = z.getNextEntry()) != null) {
                if (e.getName().endsWith(".class")) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    byte[] buf = new byte[65536]; int n;
                    while ((n = z.read(buf)) > 0) b.write(buf, 0, n);
                    classes.put(e.getName(), b.toByteArray());
                }
            }
        }
        Map<String, Integer> access = new HashMap<>();
        Map<String, List<String>> members = new HashMap<>();
        for (String name : classes.keySet()) {
            String cn = name.substring(0, name.length() - 6).replace('/', '.');
            int acc = Opcodes.ACC_PUBLIC;
            try {
                AccV av = new AccV();
                new ClassReader(classes.get(name)).accept(av, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                acc = av.acc;
                if (!av.hasThis0) acc |= Opcodes.ACC_STATIC;
            } catch (Throwable t) {}
            access.put(cn, mask(acc));
            int idx = cn.lastIndexOf('$');
            if (idx > 0) members.computeIfAbsent(cn.substring(0, idx), k -> new ArrayList<>()).add(cn);
        }
        int fixed = 0;
        Set<String> targets = new HashSet<>(members.keySet());
        for (String cn : access.keySet()) if (cn.contains("$")) targets.add(cn);
        for (String outer : targets) {
            String key = outer.replace('.', '/') + ".class";
            byte[] orig = classes.get(key);
            if (orig == null) continue;
            ClassNode cn = new ClassNode();
            new ClassReader(orig).accept(cn, 0);
            boolean changed = false;
            String outerBin = outer.replace('.', '/');
            List<String> mems = members.get(outer);
            if (mems != null) for (String mem : mems) {
                String memBin = mem.replace('.', '/');
                String innerName = mem.substring(mem.lastIndexOf('$') + 1);
                int acc = access.getOrDefault(mem, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC);
                InnerClassNode ic = find(cn, memBin);
                if (ic == null) { cn.innerClasses.add(new InnerClassNode(memBin, outerBin, innerName, acc)); changed = true; }
                else {
                    if (ic.innerName == null || !ic.innerName.equals(innerName)) { ic.innerName = innerName; changed = true; }
                    if (ic.outerName == null || !ic.outerName.equals(outerBin)) { ic.outerName = outerBin; changed = true; }
                    if (ic.access != acc) { ic.access = acc; changed = true; }
                }
            }
            int oi = outer.lastIndexOf('$');
            if (oi > 0) {
                String oo = outer.substring(0, oi).replace('.', '/');
                String in = outer.substring(oi + 1);
                int sacc = access.getOrDefault(outer, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC);
                InnerClassNode ic = find(cn, outerBin);
                if (ic == null) { cn.innerClasses.add(new InnerClassNode(outerBin, oo, in, sacc)); changed = true; }
                else {
                    if (ic.innerName == null || !ic.innerName.equals(in)) { ic.innerName = in; changed = true; }
                    if (ic.outerName == null || !ic.outerName.equals(oo)) { ic.outerName = oo; changed = true; }
                    if (ic.access != sacc) { ic.access = sacc; changed = true; }
                }
            }
            if (changed) {
                ClassWriter cw = new ClassWriter(0);
                cn.accept(cw);
                classes.put(key, cw.toByteArray());
                fixed++;
            }
        }
        try (ZipOutputStream z = new ZipOutputStream(new FileOutputStream(path))) {
            z.setLevel(Deflater.NO_COMPRESSION);
            for (Map.Entry<String, byte[]> en : classes.entrySet()) {
                z.putNextEntry(new ZipEntry(en.getKey()));
                z.write(en.getValue());
                z.closeEntry();
            }
        }
        System.out.println("fixed: " + fixed + " / " + classes.size());
    }
}
