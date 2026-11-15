package ni.shikatu.re_extera.data;

import kotlin.Metadata;

/* JADX INFO: compiled from: ShadowbanStore.kt */
/* JADX INFO: loaded from: C:\Users\max\Desktop\ETG\PluginSystem\projects\ExteraMax\other-source\re_extera.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J1\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0015\u001a\u00020\u00052\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b¨\u0006\u001b"}, d2 = {"Lni/shikatu/re_extera/data/ShadowbanEntry;", "", "userId", "", "hideDialog", "", "hideInGroups", "addedAt", "<init>", "(JZZJ)V", "getUserId", "()J", "getHideDialog", "()Z", "getHideInGroups", "getAddedAt", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "", "re_extera"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class ShadowbanEntry {
    private final long addedAt;
    private final boolean hideDialog;
    private final boolean hideInGroups;
    private final long userId;

    public static /* synthetic */ ShadowbanEntry copy$default(ShadowbanEntry shadowbanEntry, long j, boolean z, boolean z2, long j2, int i, Object obj) {
        if ((i & 1) != 0) {
            j = shadowbanEntry.userId;
        }
        long j3 = j;
        if ((i & 2) != 0) {
            z = shadowbanEntry.hideDialog;
        }
        boolean z3 = z;
        if ((i & 4) != 0) {
            z2 = shadowbanEntry.hideInGroups;
        }
        boolean z4 = z2;
        if ((i & 8) != 0) {
            j2 = shadowbanEntry.addedAt;
        }
        return shadowbanEntry.copy(j3, z3, z4, j2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getUserId() {
        return this.userId;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getHideDialog() {
        return this.hideDialog;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getHideInGroups() {
        return this.hideInGroups;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final long getAddedAt() {
        return this.addedAt;
    }

    public final ShadowbanEntry copy(long userId, boolean hideDialog, boolean hideInGroups, long addedAt) {
        return new ShadowbanEntry(userId, hideDialog, hideInGroups, addedAt);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ShadowbanEntry)) {
            return false;
        }
        ShadowbanEntry shadowbanEntry = (ShadowbanEntry) other;
        return this.userId == shadowbanEntry.userId && this.hideDialog == shadowbanEntry.hideDialog && this.hideInGroups == shadowbanEntry.hideInGroups && this.addedAt == shadowbanEntry.addedAt;
    }

    public int hashCode() {
        return (((((Long.hashCode(this.userId) * 31) + Boolean.hashCode(this.hideDialog)) * 31) + Boolean.hashCode(this.hideInGroups)) * 31) + Long.hashCode(this.addedAt);
    }

    public String toString() {
        return "ShadowbanEntry(userId=" + this.userId + ", hideDialog=" + this.hideDialog + ", hideInGroups=" + this.hideInGroups + ", addedAt=" + this.addedAt + ")";
    }

    public ShadowbanEntry(long j, boolean z, boolean z2, long j2) {
        this.userId = j;
        this.hideDialog = z;
        this.hideInGroups = z2;
        this.addedAt = j2;
    }

    public final long getUserId() {
        return this.userId;
    }

    public final boolean getHideDialog() {
        return this.hideDialog;
    }

    public final boolean getHideInGroups() {
        return this.hideInGroups;
    }

    public final long getAddedAt() {
        return this.addedAt;
    }
}
