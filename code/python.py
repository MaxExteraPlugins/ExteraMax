import base64
import zlib
import json
import re
import os
import urllib.parse
import time as _time
import threading
import sqlite3

from java import cast, dynamic_proxy, jint, jclass, jfloat, jarray, jlong, jbyte
from java.lang import Boolean, Integer, Long, Math, Runnable, Float, Object, String, CharSequence
from java.lang import String as JString
from java.util import ArrayList
from android.text import SpannableStringBuilder, Spanned
from android.text.style import ForegroundColorSpan, StyleSpan
from android.graphics import Typeface
from android.view import View, ViewGroup
from android.view import WindowManager
from android.view.inputmethod import InputMethodManager
from android.content import Context
from android.util import SparseArray, Base64
from android.widget import ScrollView, LinearLayout, TextView, EditText
from android.text import InputType
from androidx.core.content import ContextCompat

from org.telegram.messenger import (ApplicationLoader, MessageObject,
                                     MessagesController, MessagesStorage,
                                     UserConfig, Utilities, NotificationCenter,
                                     LocaleController, ChatObject, AndroidUtilities,
                                     ContactsController, SendMessagesHelper,
                                     AccountInstance, FileLoader, ImageLocation,
                                     VideoEditedInfo)
from org.telegram.tgnet import TLRPC, TLObject, ConnectionsManager, NativeByteBuffer
from org.telegram.tgnet.tl import TL_account
from org.telegram.ui.ActionBar import Theme, AlertDialog
from org.telegram.ui.Cells import ChatMessageCell
from org.telegram.ui import ChatActivity, LaunchActivity
from org.telegram.ui.Components import BulletinFactory, ColoredImageSpan

from android_utils import log, run_on_ui_thread

from base_plugin import (BasePlugin, HookResult, HookStrategy, MenuItemData,
                         MenuItemType, MethodHook, MethodReplacement, XposedHook)
from client_utils import (get_messages_controller, get_messages_storage,
                          run_on_queue, send_request, get_last_fragment, RequestCallback,
                          send_document)
from file_utils import write_file, read_file, ensure_dir_exists
from hook_utils import find_class, get_private_field, set_private_field
from ui.bulletin import BulletinHelper
from ui.settings import Header, Switch, Selector, Text, Divider, Input, Custom, SimpleSettingFactory
from ui.alert import AlertDialogBuilder
from org.telegram.messenger import MediaDataController
from org.telegram.ui.Components import BackupImageView, LayoutHelper, EditTextBoldCursor
from android.view import Gravity
from android.widget import LinearLayout as JLinearLayout, FrameLayout as JFrameLayout

__id__ = "ExteraMax"
__name__ = "ExteraMax"
__description__ = "[EN]: Updated version of the «re:extera» plugin («ghost» mode, spy features, message filters, and much more).\n\n[RU]: Обновленная версия плагина «re:extera» (режим «призрака», функции шпиона, фильтры сообщений и многое другое)."
__author__ = "@itNotMax - @MaxExteraPlugins"
__version__ = "1.5.46"
__icon__ = "MaxExteraPluginsStickers/30"
__min_version__ = "12.10.6"
__app_version__ = ">=12.8.1"
__sdk_version__ = ">=1.4.3.3"

ENTRY_CLASS = "com.maxplugins.sample.Main"
DEX_BEGIN = "__DEX_BEGIN__"
DEX_END = "__DEX_END__"
PREFS_NAME = "exteramax_prefs"
PRESENCE_KEY = "presence_data"
PREMIUM_CACHE_KEY = "premium_cache"
FLAG_DELETED = 1 << 16

READ_REQS = frozenset(["TL_messages_readHistory", "TL_messages_readEncryptedHistory",
             "TL_messages_readDiscussion", "TL_messages_readMessageContents",
             "TL_channels_readHistory", "TL_channels_readMessageContents",
             "TL_messages_markDialogUnread"])
TYPING_REQS = frozenset(["TL_messages_setTyping", "TL_messages_setEncryptedTyping"])
STORIES_REQS = frozenset(["TL_stories_readStories", "TL_stories_incrementStoryViews"])
SEND_REQS = frozenset(["TL_messages_sendMessage", "TL_messages_sendMedia",
             "TL_messages_sendMultiMedia", "TL_messages_forwardMessages"])
ONLINE_REQS = READ_REQS | TYPING_REQS | STORIES_REQS | SEND_REQS | frozenset([
              "TL_messages_editMessage", "TL_stories_sendStory",
              "TL_stories_sendReaction", "updateStatus", "TL_account_updateStatus"])
BLOCKED_AD_REQS = frozenset()
DELETE_REQS = frozenset(["TL_messages_deleteMessages", "TL_channels_deleteMessages"])
PRESENCE_UPD = frozenset(["TL_updateUserStatus", "TL_updateUserTyping", "TL_updateChatUserTyping",
                "TL_updateChannelUserTyping", "TL_updateReadHistoryOutbox", "TL_updateReadChannelOutbox",
                "TL_updateNewMessage", "TL_updateNewChannelMessage",
                "TL_updateEditMessage", "TL_updateEditChannelMessage",
                "TL_updateBotMessageReaction", "TL_updateMessageReactions"])

ONE_YEAR_SECONDS = 365 * 24 * 60 * 60

_OPT_REVISIONS = 7171
_OPT_MARK_READ = 7272
_OPT_VIEW_DELETED = 7373

# Canonical implementations of the pure-logic helpers below live in the Java
# side (com.maxplugins.sample.Main). Once the DEX is loaded the plugin publishes
# it here so module-level helpers can delegate to Java; the Python bodies are
# kept only as a fallback for the short window before the DEX is ready.
_ACTIVE_DEX = None


def _dex():
    d = _ACTIVE_DEX
    return d if (d is not None and d.loaded) else None

class _ExclusionsStoreWrapper:
    @staticmethod
    def getMode(did, isReading):
        d = _dex()
        if d:
            res = d.call("pyExclusionGetMode", jlong(int(did)), bool(isReading))
            return int(res) if res is not None else 0
        return 0

    @staticmethod
    def set(did, reading, typing):
        d = _dex()
        if d:
            d.call("pyExclusionSet", jlong(int(did)), jint(int(reading)), jint(int(typing)))

    @staticmethod
    def remove(did):
        d = _dex()
        if d:
            d.call("pyExclusionRemove", jlong(int(did)))

    @staticmethod
    def all():
        d = _dex()
        if d:
            res = d.call("pyExclusionAll")
            if res is not None:
                return res
        return ArrayList()


class _ShadowbanStoreWrapper:
    class _MapWrapper:
        @staticmethod
        def get(uid):
            d = _dex()
            if d:
                return d.call("pyShadowbanGet", jlong(int(uid)))
            return None
    map = _MapWrapper()

    @staticmethod
    def all():
        d = _dex()
        if d:
            res = d.call("pyShadowbanAll")
            if res is not None:
                return res
        return ArrayList()

    @staticmethod
    def add(uid, hideDialog, hideInGroups):
        d = _dex()
        if d:
            d.call("pyShadowbanAdd", jlong(int(uid)), bool(hideDialog), bool(hideInGroups))

    @staticmethod
    def remove(uid):
        d = _dex()
        if d:
            d.call("pyShadowbanRemove", jlong(int(uid)))


class JMain:
    ExclusionsStore = _ExclusionsStoreWrapper
    ShadowbanStore = _ShadowbanStoreWrapper



def read_file_from_uri(ctx, uri):
    try:
        cr = ctx.getContentResolver()
        is_stream = cr.openInputStream(uri)
        if is_stream is None:
            return None
        from java.io import BufferedReader, InputStreamReader
        reader = BufferedReader(InputStreamReader(is_stream, "UTF-8"))
        sb = []
        line = reader.readLine()
        while line is not None:
            sb.append(str(line))
            line = reader.readLine()
        reader.close()
        return "\n".join(sb)
    except Exception as e:
        log(f"[MAX] read_file_from_uri failed: {e}")
        return None


class L:
    _ru = {
        "ghost": "Призрак", "spy": "Шпион", "filters": "Фильтры", "hacker": "Хакер",
        "tab_ghost": "Включить призрак", "tab_ghost_sub": "Выключить все функции вкладки",
        "tab_spy": "Включить шпион-режим", "tab_spy_sub": "Выключить все функции вкладки",
        "tab_hacker": "Включить хакер-режим", "tab_hacker_sub": "Выключить все функции вкладки",
        "enable_features": "Включить функции", "enable_features_sub": "Выкл — настройки и функции вкладки отключены",
        "channels_only": "Только для каналов", "channels_only_sub": "Применять фильтр только в каналах",
        "settings_misc": "Настройки и прочее",
        "ghost_header": "Режим призрака", "ghost_toggle": "Режим призрака",
        "ghost_toggle_sub": "Скрыть онлайн-статус, набор текста и прочтение",
        "ghost_settings": "Настройки призрака",
        "no_online": "Не отправлять онлайн", "instant_offline": "Немедленный оффлайн",
        "no_typing": "Не отправлять печатает",
        "no_read": "Не читать сообщения", "no_read_sub": "Собеседник не увидит галочки прочтения",
        "no_read_stories": "Не читать истории",
        "read_on_action": "Читать при действиях", "read_on_action_sub": "Прочтение отправится при ответе или реакции",
        "use_scheduled": "Использовать отложку",
        "silent_send": "Отправлять без звука", "silent_never": "Никогда", "silent_always": "Всегда", "silent_ghost": "Только с призраком",
        "outside_ghost": "Просмотр историй",
        "story_when_ghost_on": "При включённом призраке",
        "story_when_ghost_off": "При выключенном призраке",
        "story_view_public": "Смотреть публично",
        "story_view_private": "Смотреть приватно",
        "story_view_ask": "Спрашивать",
        "read_whitelist": "Белый список прочтения", "read_whitelist_sub": "Эти люди будут видеть ваше прочтение даже в призраке",
        "read_wl_users": "Пользователи", "read_wl_empty": "Список пуст",
        "read_wl_clear_all": "Очистить всё", "read_wl_clear_confirm": "Удалить всех пользователей из белого списка прочтения?",
        "drawer_btn": "Кнопка Призрак в шторке",
        "ghost_on": "Призрак включен", "ghost_off": "Призрак выключен",
        "ghost_btn_on": "Выключить призрака", "ghost_btn_off": "Включить призрака",
        "story_dlg_title": "Режим призрака", "story_dlg_msg": "Включить режим призрака для просмотра историй?",
        "yes": "Да", "no": "Нет",
        "sel_always_off": "Всегда выкл.", "sel_with_ghost": "Только с призраком", "sel_always_on": "Всегда вкл.",
        "sel_ask_on_open": "Спрашивать при открытии", "ghost_status_offline": "оффлайн",
        "max_vigilance": "Максимальная бдительность", "keep_alive": "Оставлять клиент активным", "keep_alive_sub": "Работа в фоне даже после закрытия (тратит батарею)",
        "local_read_header": "Локальное чтение",
        "local_read_msgs": "В личных чатах",
        "local_read_private": "В личных (приватных)",
        "local_read_groups": "В группах",
        "local_read_channels": "В каналах",
        "local_read_stories": "В историях",
        "local_read_msgs_sub": "Читать сообщения в личных чатах без отправки прочитанного",
        "local_read_private_sub": "Читать приватные сообщения без отправки прочитанного",
        "local_read_groups_sub": "Читать сообщения в группах без отправки прочитанного",
        "local_read_channels_sub": "Читать сообщения в каналах без отправки прочитанного",
        "local_read_stories_sub": "Читать истории без отправки просмотра",
        "local_read_sub": "Настройки локального чтения (без отправки прочитанного)",
        "spy_mode": "Режим шпиона", "settings_categories": "Настройки",
        "save_deleted": "Сохранять удалённые", "save_deleted_sub": "Удалённые сообщения останутся в чате",
        "save_edits": "Сохранять историю правок", "save_edits_sub": "Все версии отредактированных сообщений",
        "save_bots": "Сохранять в чатах с ботами",
        "save_online": "Сохранять последний онлайн", "save_online_sub": "Запоминать время онлайна всех пользователей",
        "save_own_del": "Не сохранять свои удаления",
        "appearance": "Оформление", "show_mark": "Показывать метку",
        "use_trash_icon": "Использовать значок корзины", "use_trash_icon_sub": "Иконка вместо текстовой метки",
        "del_text": "Текст удалённого", "del_color": "Цвет подписи HEX (без #)",
        "del_customize": "Настроить вид удалёнок",
        "show_mark_sub": "Отключите, чтобы удалённые сообщения выглядели как обычные (кроме прозрачности)",
        "transparent_del": "Прозрачные удалёнки", "transparent_del_sub": "Удалённые сообщения становятся полупрозрачными",
        "opacity_level": "Уровень прозрачности",
        "storage": "Хранилище", "save_attachments": "Сохранять вложения",
        "save_attach_private": "В личных чатах", "save_attach_groups": "В группах", "save_attach_channels": "В каналах",
        "max_folder": "Макс. размер папки вложений", "auto_cleanup": "Автоочистка медиа", "auto_cleanup_sub": "Автоматически удалять старые файлы",
        "attach_folder": "Папка вложений", "attach_folder_sub": "Выбрать папку для сохранения",
        "attach_folder_default": "По умолчанию (Downloads)", "attach_folder_reset": "Сбросить",
        "folder_set": "Папка установлена", "folder_reset": "Папка сброшена",
        "wipe_storage": "Очистить хранилище",
        "import_from_file": "Импорт из файла", "import_from_file_sub": "Выбрать .mre_backup файл вручную",
        "wipe_confirm": "Удалить все сохранённые сообщения и историю правок?",
        "delete": "Удалить", "cancel": "Отмена", "cleared": "Очищено",
        "tracker": "Трекер", "only_hidden": "Только скрытый",
        "save_last_seen": "Сохранять последний онлайн", "save_last_seen_sub": "Показывать точное время в скрытых статусах для всех пользователей («~ был(а) в HH:MM»)",
        "save_once_msgs": "Сохранять одноразовые", "save_once_msgs_sub": "Не удалять одноразовые сообщения после просмотра",
        "decay": "Затухание", "tracked_users": "Отслеживаемые",
        "journal": "Журнал", "wipe_tracker": "Очистить трекер",
        "hide_ads": "Скрыть рекламу", "hide_feed_ads": "Скрыть рекламу в ленте", "hide_feed_ads_sub": "Убирает рекламные сообщения из ленты",
        "no_fwd_limit": "Снять лимит пересылок", "no_fwd_limit_sub": "Пересылать более 100 сообщений за раз",
        "local_premium": "Локальный Premium", "local_premium_sub": "Премиум-функции видны только вам",
        "once_media": "Одноразовые медиа", "once_media_sub": "Одноразовые фото/видео не исчезают",
        "bypass": "Обход", "screenshots": "Скриншоты", "screenshots_sub": "Разрешить скриншоты в защищённых чатах",
        "noforwards": "Noforwards", "noforwards_sub": "Копировать и пересылать защищённый контент",
        "fwd_mode": "Режим пересылки", "fwd_copy": "Копия (от своего лица)", "fwd_try": "Пересылка (может быть ошибка)",
                "stories_bypass": "Сторис", "stories_bypass_sub": "Сохранять сторис с запретом на скачивание",
        "info": "Информация", "author_channel": "Канал автора", "credits": "Благодарности",
        "credits_empty": "Пока пусто", "params": "Параметры",
        "show_in_settings": "Показать в настройках", "show_in_settings_sub": "Показывать кнопку ExteraMax в главных настройках Telegram",
        "show_in_settings_restart": "Изменение применится после перезахода в приложение",
        "language": "Язык плагина", "lang_auto": "Авто", "lang_ru": "Русский", "lang_en": "English",
        "revisions": "История правок", "no_revisions": "Нет истории правок",
        "original": "Оригинал", "version": "Версия", "view": "Просмотр",
        "close": "Закрыть", "back": "Назад", "saved": "Сохранено",
        "view_deleted": "Посмотреть удалёнки", "no_deleted": "Нет удалённых сообщений",
        "purge_deleted": "Очистить удалёнки", "purge_deleted_confirm": "Удалить все сохранённые удалёнки этого чата?", "purged": "Удалёнки очищены",
        "clear_all_deleted": "Очистить ВСЕ удалёнки", "clear_all_deleted_sub": "Полный сброс базы сохранённых удалённых сообщений",
        "clear_all_deleted_confirm": "Вы уверены, что хотите полностью очистить данные обо ВСЕХ сохранённых удалённых сообщениях во всех чатах? Все удалёнки будут сброшены.", "all_deleted_cleared": "Все сохранённые удалёнки очищены",
        "sync_all_chats": "Синхронизировать все чаты", "sync_all_chats_sub": "Перезагрузить последние сообщения из облака во всех чатах",
        "menu_reextera": "MAX re:extera",
        "deleted_list": "Удалённые сообщения", "msg_deleted": "удалено",
        "add": "Добавить", "remove": "Удалить?", "removed": "Удалён",
        "added": "Добавлен", "not_found": "Не найден", "error": "Ошибка",
        "invalid_id": "Неверный ID", "list_empty": "Список пуст",
        "online_journal": "Журнал онлайна", "no_records": "Нет записей",
        "patterns": "Шаблоны", "add_pattern": "Добавить шаблон",
        "invalid_regex": "Невалидный regex",
        "enable_filters": "Включить фильтры", "enable_filters_sub": "Скрывать сообщения по шаблонам",
        "regex_patterns": "Regex-шаблоны (SQLite)", "add_filter": "Добавить фильтр",
        "new_filter": "Новый фильтр", "editor": "Редактор",
        "regex_expr": "Regex выражение", "enabled": "Включён",
        "case_insensitive": "Без учёта регистра", "inverted": "Обратный", "inverted_sub": "НЕ содержащие",
        "save": "Сохранить", "enter_expr": "Введите выражение!",
        "mark_read": "Прочитать", "edit_history": "История изменений",
        "forward": "Переслать", "save_media": "Сохранить",
        "media_changed": "[медиа изменено]", "no_text": "[без текста]",
        "go_to_msg": "Перейти к сообщению", "download_file": "Скачать файл",
        "has_photo": "[фото]", "has_video": "[видео]", "has_doc": "[файл]",
        "has_sticker": "[стикер]", "has_voice": "[голосовое]", "has_round": "[кружочек]",
        "saved_to": "Сохранено",
        "browse_attachments": "Просмотр вложений", "browse_attachments_sub": "Сохранённые файлы по чатам",
        "no_attachments": "Нет сохранённых вложений", "files_count": "файлов",
        "updates": "Обновления", "check_updates": "Проверять обновления при старте",
        "check_updates_sub": "Автопроверка новых версий плагина",
        "update_available": "Доступно обновление", "whats_new": "Что нового",
        "download_verified": "Скачать с проверенного канала",
        "view_author_channel": "Посмотреть в канале автора",
        "remind_later": "Напомнить позже",
        "author_warning": "Вы хотите перейти на канал автора этого плагина. Хоть я и ручаюсь за свои плагины, но все же у меня нет метки проверенного источника.",
        "dont_show_again": "Больше не показывать",
        "author_warn_toggle": "Предупреждение о канале автора",
        "author_warn_toggle_sub": "Показывать предупреждение при переходе",
        "no_updates": "Обновлений нет",
        "backup": "Резервная копия", "export_settings": "Экспорт настроек",
        "export_to_saved": "Экспорт в Избранное", "export_to_saved_sub": "Отправить .mre_backup файл в Избранное",
        "export_done": "Настройки экспортированы в Избранное",
        "export_error": "Ошибка экспорта",
        "import_title": "Импорт настроек", "import_confirm": "Восстановить все настройки MAX re:extera из этого бэкапа?",
        "import_done": "Настройки импортированы! Перезагрузите плагин",
        "import_error": "Ошибка импорта",
        "username_or_id": "@username или id",
        "shadowban": "Теневой бан",
        "shadowban_empty": "Список теневого бана пуст. Добавьте пользователя из меню профиля или по ID.",
        "shadowban_added": "Пользователь добавлен в теневой бан",
        "shadowban_removed": "Пользователь убран из теневого бана",
        "shadowban_hide_dialog": "Скрывать диалог",
        "shadowban_hide_groups": "Скрывать в группах",
        "shadowban_remove": "Убрать из теневого бана",
        "sb_dialog_hidden": "диалог скрыт", "sb_dialog_visible": "диалог виден",
        "sb_groups_hidden": "группы скрыты", "sb_groups_visible": "группы видны",
        "exclusions": "Исключения чатов",
        "exclusions_empty": "Список исключений пуст. Настройте чтение и тайпинг для отдельного чата.",
        "exclusions_saved": "Исключение сохранено",
        "exclusions_removed": "Исключение удалено",
        "exclusions_reading": "Чтение", "exclusions_typing": "Тайпинг",
        "mode_default": "По умолчанию", "mode_always": "Всегда", "mode_never": "Никогда",
        "reading_default": "По умолчанию (как в Ghost)", "reading_always": "Всегда отправлять чтение", "reading_never": "Никогда не читать",
        "typing_default": "По умолчанию (как в Ghost)", "typing_always": "Всегда отправлять тайпинг", "typing_never": "Никогда не показывать тайпинг",
        "reading_for": "Чтение для чата", "typing_for": "Тайпинг для чата",
        "enter_user_id": "Введите User ID", "enter_dialog_id": "Введите Dialog/Chat ID",
        "by_id": "По ID", "pick_chat": "Выбрать чат", "clear_list_confirm": "Очистить весь список?", "edit": "Изменить",
        "exclusions_row_sub": "Настройка чтения и тайпинга по отдельным чатам",
        "shadowban_row_sub": "Управление списком скрытых пользователей",
        "regex_quick": "Regex-фильтры (быстрый список)", "regex_quick_sub": "Фильтрация сообщений по регулярным выражениям",
        "noforward_sub": "Обход запрета копирования и пересылки",
        "gif_reuploader": "Перезаливка GIF", "gif_reuploader_sub": "Сохранение защищённых GIF в Избранное без ограничений",
        "cr_extera": "Без экстеры ничего бы не было",
        "cr_bleizix": "Фундамент re:extera",
        "cr_shiawasez": "Обновление re:extera (v2)",
        "cr_thisisgame": "Мотивация и поддержка",
        "cr_summering": "Тестирование и развитие",
        "cr_testing": "Тестирование и развитие",
        "cr_kvuco": "AdBlock, Mirror, RFS",
        "cr_alexeicrystal": "Скрытие рекламы в ленте, улучшения кода",
                "cr_ai_title": "ИИ", "cr_ai": "Реализация :)))",
        "downloading_retry": "Загрузка... попробуйте снова",
        "dex_not_loaded": "DEX не загружен",


    }
    _en = {
        "ghost": "Ghost", "spy": "Spy", "filters": "Filters", "hacker": "Hacker",
        "tab_ghost": "Enable ghost", "tab_ghost_sub": "Turn off all tab features",
        "tab_spy": "Enable spy mode", "tab_spy_sub": "Turn off all tab features",
        "tab_hacker": "Enable hacker", "tab_hacker_sub": "Turn off all tab features",
        "enable_features": "Enable features", "enable_features_sub": "Off — tab settings and features are disabled",
        "channels_only": "Channels only", "channels_only_sub": "Apply the filter only in channels",
        "settings_misc": "Settings & more",
        "ghost_header": "Ghost mode", "ghost_toggle": "Ghost mode",
        "ghost_toggle_sub": "Hide online status, typing and read receipts",
        "ghost_settings": "Ghost settings",
        "no_online": "Don't send online", "instant_offline": "Instant offline",
        "no_typing": "Don't send typing",
        "no_read": "Don't read messages", "no_read_sub": "Others won't see read checkmarks",
        "no_read_stories": "Don't read stories",
        "read_on_action": "Read on action", "read_on_action_sub": "Send read receipt on reply or reaction",
        "use_scheduled": "Use scheduled", "silent_send": "Silent send",
        "silent_never": "Never", "silent_always": "Always", "silent_ghost": "Ghost only",
        "outside_ghost": "Story viewing",
        "story_when_ghost_on": "When ghost is on",
        "story_when_ghost_off": "When ghost is off",
        "story_view_public": "View publicly",
        "story_view_private": "View privately",
        "story_view_ask": "Ask",
        "read_whitelist": "Read whitelist", "read_whitelist_sub": "These people will see your read receipts even in ghost",
        "read_wl_users": "Users", "read_wl_empty": "List is empty",
        "read_wl_clear_all": "Clear all", "read_wl_clear_confirm": "Remove all users from the read whitelist?",
        "drawer_btn": "Ghost button in drawer",
        "ghost_on": "Ghost ON", "ghost_off": "Ghost OFF",
        "ghost_btn_on": "Disable ghost", "ghost_btn_off": "Enable ghost",
        "story_dlg_title": "Ghost mode", "story_dlg_msg": "Enable ghost mode for viewing stories?",
        "yes": "Yes", "no": "No",
        "sel_always_off": "Always off", "sel_with_ghost": "Only with ghost", "sel_always_on": "Always on",
        "sel_ask_on_open": "Ask on open", "ghost_status_offline": "offline",
        "max_vigilance": "Maximum vigilance", "keep_alive": "Keep client active", "keep_alive_sub": "Stay active in background (uses more battery)",
        "spy_mode": "Spy mode", "settings_categories": "Settings",
        "save_deleted": "Save deleted", "save_deleted_sub": "Deleted messages stay visible in chat",
        "save_edits": "Save edit history", "save_edits_sub": "Keep all versions of edited messages",
        "save_bots": "Save in bot chats",
        "save_online": "Save last online", "save_online_sub": "Remember online time for all users",
        "save_own_del": "Don't save own deletions",
        "local_read_header": "Local Read",
        "local_read_msgs": "In private chats",
        "local_read_private": "In private (private)",
        "local_read_groups": "In groups",
        "local_read_channels": "In channels",
        "local_read_stories": "In stories",
        "local_read_msgs_sub": "Read messages in private chats without sending read receipt",
        "local_read_private_sub": "Read private messages without sending read receipt",
        "local_read_groups_sub": "Read messages in groups without sending read receipt",
        "local_read_channels_sub": "Read messages in channels without sending read receipt",
        "local_read_stories_sub": "Read stories without sending view receipt",
        "local_read_sub": "Local read settings (without sending read receipts)",
        "appearance": "Appearance", "show_mark": "Show mark",
        "use_trash_icon": "Use trash icon", "use_trash_icon_sub": "Icon instead of text label",
        "del_text": "Deleted text", "del_color": "Label color HEX (no #)",
        "del_customize": "Customize deleted look",
        "show_mark_sub": "Turn off to make deleted messages look like normal ones (opacity still applies)",
        "transparent_del": "Transparent deleted", "transparent_del_sub": "Deleted messages become semi-transparent",
        "opacity_level": "Opacity level",
        "storage": "Storage", "save_attachments": "Save attachments",
        "save_attach_private": "In private chats", "save_attach_groups": "In groups", "save_attach_channels": "In channels",
        "max_folder": "Max attachment folder size", "auto_cleanup": "Auto-cleanup media", "auto_cleanup_sub": "Automatically delete old files",
        "attach_folder": "Attachments folder", "attach_folder_sub": "Choose folder for saving",
        "attach_folder_default": "Default (Downloads)", "attach_folder_reset": "Reset",
        "folder_set": "Folder set", "folder_reset": "Folder reset",
        "wipe_storage": "Wipe storage",
        "import_from_file": "Import from file", "import_from_file_sub": "Select .mre_backup file manually",
        "wipe_confirm": "Delete all saved messages and edit history?",
        "delete": "Delete", "cancel": "Cancel", "cleared": "Cleared",
        "tracker": "Tracker", "only_hidden": "Hidden only",
        "save_last_seen": "Save last online", "save_last_seen_sub": "Show exact time in hidden statuses for all users («~ last seen at HH:MM»)",
        "save_once_msgs": "Keep one-time messages", "save_once_msgs_sub": "Don't delete one-time messages after viewing",
        "decay": "Decay", "tracked_users": "Tracked users",
        "journal": "Journal", "wipe_tracker": "Wipe tracker",
        "hide_ads": "Hide ads", "hide_feed_ads": "Hide ads in feed", "hide_feed_ads_sub": "Removes ad messages from the feed",
        "no_fwd_limit": "Remove forward limit", "no_fwd_limit_sub": "Forward more than 100 messages at once",
        "local_premium": "Local Premium", "local_premium_sub": "Premium features visible only to you",
        "once_media": "Once media", "once_media_sub": "View-once photos/videos won't disappear",
        "bypass": "Bypass", "screenshots": "Screenshots", "screenshots_sub": "Allow screenshots in protected chats",
        "noforwards": "Noforwards", "noforwards_sub": "Copy and forward protected content",
        "fwd_mode": "Forward mode", "fwd_copy": "Copy (as your own)", "fwd_try": "Forward (may error)",
                "stories_bypass": "Stories", "stories_bypass_sub": "Save stories with download restrictions",
        "info": "Information", "author_channel": "Author's channel", "credits": "Credits",
        "credits_empty": "Nothing here yet", "params": "Parameters",
        "language": "Plugin language", "lang_auto": "Auto", "lang_ru": "Русский", "lang_en": "English",
        "revisions": "Edit history", "no_revisions": "No edit history",
        "original": "Original", "version": "Version", "view": "Preview",
        "close": "Close", "back": "Back", "saved": "Saved",
        "view_deleted": "View deleted", "no_deleted": "No deleted messages",
        "purge_deleted": "Clear deleted", "purge_deleted_confirm": "Delete all saved deleted messages of this chat?", "purged": "Deleted cleared",
        "clear_all_deleted": "Clear ALL deleted", "clear_all_deleted_sub": "Completely reset saved deleted messages database",
        "clear_all_deleted_confirm": "Are you sure you want to completely clear all saved deleted messages across all chats? All deleted records will be reset.", "all_deleted_cleared": "All saved deleted messages cleared",
        "sync_all_chats": "Sync all chats", "sync_all_chats_sub": "Reload recent messages from cloud across all chats",
        "menu_reextera": "MAX re:extera",
        "deleted_list": "Deleted messages", "msg_deleted": "deleted",
        "add": "Add", "remove": "Remove?", "removed": "Removed",
        "added": "Added", "not_found": "Not found", "error": "Error",
        "invalid_id": "Invalid ID", "list_empty": "List is empty",
        "online_journal": "Online journal", "no_records": "No records",
        "patterns": "Patterns", "add_pattern": "Add pattern",
        "invalid_regex": "Invalid regex",
        "enable_filters": "Enable filters", "enable_filters_sub": "Hide messages by patterns",
        "regex_patterns": "Regex patterns (SQLite)", "add_filter": "Add filter",
        "new_filter": "New filter", "editor": "Editor",
        "regex_expr": "Regex expression", "enabled": "Enabled",
        "case_insensitive": "Case insensitive", "inverted": "Inverted", "inverted_sub": "NOT matching",
        "save": "Save", "enter_expr": "Enter expression!",
        "mark_read": "Mark read", "edit_history": "Edit history",
        "forward": "Forward", "save_media": "Save",
        "media_changed": "[media changed]", "no_text": "[no text]",
        "go_to_msg": "Go to message", "download_file": "Download file",
        "has_photo": "[photo]", "has_video": "[video]", "has_doc": "[file]",
        "has_sticker": "[sticker]", "has_voice": "[voice]", "has_round": "[round]",
        "saved_to": "Saved",
        "browse_attachments": "Browse attachments", "browse_attachments_sub": "Saved files by chat",
        "no_attachments": "No saved attachments", "files_count": "files",
        "updates": "Updates", "check_updates": "Check updates on start",
        "check_updates_sub": "Auto-check for new plugin versions",
        "update_available": "Update available", "whats_new": "What's new",
        "download_verified": "Download from verified channel",
        "view_author_channel": "View in author's channel",
        "remind_later": "Remind later",
        "author_warning": "You are about to visit the author's channel. Although I vouch for my plugins, I don't have a verified source badge.",
        "dont_show_again": "Don't show again",
        "author_warn_toggle": "Author channel warning",
        "author_warn_toggle_sub": "Show warning when navigating",
        "no_updates": "No updates available",
        "backup": "Backup", "export_settings": "Export settings",
        "export_to_saved": "Export to Saved Messages", "export_to_saved_sub": "Send .mre_backup file to Saved Messages",
        "export_done": "Settings exported to Saved Messages",
        "export_error": "Export error",
        "import_title": "Import settings", "import_confirm": "Restore all MAX re:extera settings from this backup?",
        "import_done": "Settings imported! Reload plugin",
        "import_error": "Import error",
        "username_or_id": "@username or ID",
        "shadowban": "Shadowban",
        "shadowban_empty": "Shadowban list is empty. Add a user from the profile menu or by ID.",
        "shadowban_added": "User added to shadowban",
        "shadowban_removed": "User removed from shadowban",
        "shadowban_hide_dialog": "Hide dialog",
        "shadowban_hide_groups": "Hide in groups",
        "shadowban_remove": "Remove from shadowban",
        "sb_dialog_hidden": "dialog hidden", "sb_dialog_visible": "dialog visible",
        "sb_groups_hidden": "groups hidden", "sb_groups_visible": "groups visible",
        "exclusions": "Chat exclusions",
        "exclusions_empty": "Exclusions list is empty. Configure reading and typing for a specific chat.",
        "exclusions_saved": "Exclusion saved",
        "exclusions_removed": "Exclusion removed",
        "exclusions_reading": "Reading", "exclusions_typing": "Typing",
        "mode_default": "Default", "mode_always": "Always", "mode_never": "Never",
        "reading_default": "Default (as in Ghost)", "reading_always": "Always send read receipts", "reading_never": "Never read",
        "typing_default": "Default (as in Ghost)", "typing_always": "Always send typing", "typing_never": "Never show typing",
        "reading_for": "Reading for chat", "typing_for": "Typing for chat",
        "enter_user_id": "Enter User ID", "enter_dialog_id": "Enter Dialog/Chat ID",
        "by_id": "By ID", "pick_chat": "Pick chat", "clear_list_confirm": "Clear the whole list?", "edit": "Edit",
        "exclusions_row_sub": "Configure reading and typing per chat",
        "shadowban_row_sub": "Manage hidden users list",
        "regex_quick": "Regex filters (quick list)", "regex_quick_sub": "Filter messages by regular expressions",
        "noforward_sub": "Bypass copy and forward restrictions",
        "gif_reuploader": "Gif reuploader", "gif_reuploader_sub": "Save protected GIFs to Saved Messages without restrictions",
        "show_in_settings": "Show in Telegram Settings", "show_in_settings_sub": "Show ExteraMax button in Telegram main settings",
        "show_in_settings_restart": "Change takes effect after app restart",
        "cr_extera": "Nothing would exist without extera",
        "cr_bleizix": "Foundation of re:extera",
        "cr_shiawasez": "re:extera update (v2)",
        "cr_thisisgame": "Motivation and support",
        "cr_summering": "Testing and development",
        "cr_testing": "Testing and development",
        "cr_kvuco": "AdBlock, Mirror, RFS",
        "cr_alexeicrystal": "Hiding ads in the feed, code improvements",
                "cr_ai_title": "AI", "cr_ai": "Implementation :)))",
        "downloading_retry": "Downloading... try again",
        "dex_not_loaded": "DEX not loaded",


    }
    # Дополнительные языки (ar de es fr hi ja pt tr uk zh) хранятся сжато
    # (zlib+base64 JSON), распаковываются лениво при первом выборе языка.
    # Экономит ~90KB исходника: файл компилируется движком при каждом старте.
    _EXTRA_BLOB = (
        "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"
        "b19c+eJjqb/2w09Pz4yjuUhm8ZOb+5/98Pmz8T+XZBLj/cqJOSnyZOnCyvKXaGft7kGsliyVnwcsfT+9cChM8F25cfvp+FUz"
        "115Ob5e3ipZn33y2dnwa5Dxdvvv08LXVsfHHY+PPvry4sv80jfX6pbX5myvffrd6KPr5qLfogVDnpdg+Wfpk7cy4tLsy/fmz"
        "sfFoOu3KJ5dXj197euYminlZs2vz11eOqLqrh/68cuH4k7unVsfnnSRZ6ebZV/tB5uq1Kee1WcprT24ee7K8jLJPbn3z9MSR"
        "lakFNbCZT7C+YJ21iwvRpFan+aP7nywfiaWuyvu1U2CNubIEVMwEphEdrk4dW5n+BGuOccvgvEn280af3L5LXHv90srFQ6uf"
        "fvH0m/BzTKvHv1/5ccFMs5fe+eTmp6snzj25sQAWiyR1rsweeXpzKszlJC7++jPhoEju5urJ05hKMynxRE3mj5VbY9h5GG55"
        "FqaUk+1slXOzLFf2n0dT0m9Z1uTT2fEnS0diZSQpcnX6s2fHL8ZSIWURn359e+WzT1Y+OVqW7giGxYJgNZl87N6/3fp47e7X"
        "T5a/JQ67cdJw2OrEHJbKW1s/YxELuzp1PZKZKOJp9av5p2eXZcWdLMTVw7dXFg+hd+pC9vjHh559cXFd3yniyabP8YVJhCLS"
        "tBbwMwVdHRGmA3rSxs8AxGhWPjtjNSIln9w8bfPS324VnxxCDZ25h5qRhL3VE2MrM7NrF86sTh71k/Ke3P3L6sfjUB2Y4ac/"
        "za9eubl2fTKWbUdb87NPV6YXpIYU/NutKdl4q99eWZn55umXV1cXv//brUNu9pun6byEN7S3cuEr2rlT3z/7eiae1WZeYylF"
        "goGetfHDKzNnsb5qENPza7PjflKaNP/01rG1O5+vfHpgZfoSxNjK7aOxZDOUfXr0a/QlxZ9NXHxy8xr1yMrz6aGp1RPnvcQw"
        "kLF6YhkKCYxt04M+g/yvJ0uHV8+fEmkfK6KoAH1Pbk+rPpn1QQLqoha2PSYzzM0SCSxtrh08t3J90czWyoH9Kxdv+HlXKzPH"
        "1mYnvCyr1a/uPJ1ZNoI+yKdSMva7yZUTd1ZPXVv59g7KluVGydu1sY9BGzh09dhBTJ7YH27Ok1Aq7/9z7MTjsQkvk0kZLT+c"
        "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"
        "AchUH/c="
    )
    _prefs_key = "max_lang"
    _lang_cache = [None, 0]
    # "ru"/"en" are the recommended/primary languages (kept first in the UI selector);
    # the rest are additional languages, listed alphabetically by autonym in the selector.
    _RECOMMENDED = ("ru", "en")
    _EXTRA = ("ar", "de", "es", "fr", "hi", "ja", "pt", "tr", "uk", "zh")
    _ALL_CODES = _RECOMMENDED + _EXTRA
    _AUTONYMS = {
        "ru": "Русский", "en": "English",
        "ar": "العربية", "de": "Deutsch", "es": "Español", "fr": "Français",
        "hi": "हिन्दी", "ja": "日本語", "pt": "Português", "tr": "Türkçe",
        "uk": "Українська", "zh": "中文",
    }
    _EXTRA_DICTS = None

    @classmethod
    def _dict_for(cls, code):
        d = getattr(cls, "_" + code, None)
        if d is None and code in cls._EXTRA:
            try:
                if cls._EXTRA_DICTS is None:
                    cls._EXTRA_DICTS = json.loads(
                        zlib.decompress(base64.b64decode(cls._EXTRA_BLOB)).decode("utf-8"))
                d = cls._EXTRA_DICTS.get(code)
                if d is not None:
                    setattr(cls, "_" + code, d)
            except Exception:
                d = None
        return d if d is not None else cls._en
    @classmethod
    def _lang(cls):
        now = _time.time()
        if cls._lang_cache[0] is not None and now - cls._lang_cache[1] < 10.0:
            return cls._lang_cache[0]
        try:
            v = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0).getString(cls._prefs_key, "auto")
        except Exception:
            v = "auto"
        if v not in cls._ALL_CODES:
            v = cls._detect_tg_lang()
        cls._lang_cache = [v, now]
        return v
    @classmethod
    def _detect_tg_lang(cls):
        # Bug fix (#10): getCurrentLocale().getLanguage() reflects the OS/Java locale,
        # not necessarily the language the user actually picked inside Telegram's own
        # UI language settings - those two can differ (e.g. Russian in-app language on
        # an English-OS device), so "auto" silently fell back to English for such users.
        # Prefer Telegram's own currently-applied locale info (shortName), which is
        # what the app itself uses for its interface strings, and only fall back to the
        # Java locale if that isn't available. Also normalize things like "pt-BR"/"zh_CN"
        # down to the base code before matching against our supported list.
        raw = None
        try:
            info = LocaleController.getInstance().getCurrentLocaleInfo()
            if info is not None:
                raw = str(info.pkg or info.shortName or info.baseLangCode or "")
        except Exception:
            raw = None
        if not raw:
            try:
                raw = str(LocaleController.getInstance().getCurrentLocale().getLanguage())
            except Exception:
                raw = "en"
        raw = (raw or "en").strip().lower()
        for sep in ("-", "_"):
            if sep in raw:
                raw = raw.split(sep)[0]
                break
        return raw if raw in cls._ALL_CODES else "en"
    @classmethod
    def g(cls, key):
        code = cls._lang()
        val = cls._dict_for(code).get(key)
        if val is None:
            val = cls._en.get(key, key)
        return val

REPLY_QUOTE_FROM = None
try:
    REPLY_QUOTE_FROM = getattr(ChatActivity.ReplyQuote, "from")
except Exception:
    pass


def _sl(val):
    try:
        return str(val).strip() or "0"
    except Exception:
        return "0"

def _int(v, d=0):
    try:
        return int(v)
    except Exception:
        return d

def _fm(cls, name, pc=None):
    try:
        for m in cls.getDeclaredMethods():
            if str(m.getName()) == name:
                if pc is None or len(m.getParameterTypes()) == pc:
                    m.setAccessible(True)
                    return m
    except Exception:
        pass
    return None

def _ff(cls, name):
    try:
        f = cls.getDeclaredField(name)
        f.setAccessible(True)
        return f
    except Exception:
        pass
    return None

def _al_to_list(arr):
    return [] if arr is None else [arr.get(i) for i in range(arr.size())]

def _to_al(it):
    r = ArrayList()
    for x in it:
        r.add(x)
    return r

def _jlist(jl):
    out = []
    if jl is None:
        return out
    try:
        it = jl.iterator()
        while it.hasNext():
            out.append(it.next())
    except Exception:
        try:
            for i in range(jl.size()):
                out.append(jl.get(i))
        except Exception:
            pass
    return out

def _peer_did(msg):
    d = _dex()
    if d is not None:
        try:
            return int(d.call("peerDid", msg))
        except Exception:
            pass
    try:
        p = msg.peer_id
        if isinstance(p, TLRPC.TL_peerUser):
            return int(p.user_id)
        if isinstance(p, TLRPC.TL_peerChat):
            return -int(p.chat_id)
        if isinstance(p, TLRPC.TL_peerChannel):
            return -int(p.channel_id)
    except Exception:
        pass
    return 0

def _encode_msg(msg):
    d = _dex()
    if d is not None:
        try:
            r = d.call("encodeMessageB64", msg)
            return str(r) if r is not None else None
        except Exception:
            pass
    buf = None
    try:
        buf = NativeByteBuffer(msg.getObjectSize())
        msg.serializeToStream(buf)
        data = buf.toByteArray()
        buf.reuse()
        buf = None
        return Base64.encodeToString(data, Base64.NO_WRAP)
    except Exception:
        if buf:
            try:
                buf.reuse()
            except Exception:
                pass
        return None

def _decode_msg(raw):
    d = _dex()
    if d is not None and raw:
        try:
            return d.call("decodeMessageB64", JString(str(raw)))
        except Exception:
            pass
    buf = None
    try:
        data = Base64.decode(raw, Base64.NO_WRAP)
        buf = NativeByteBuffer(len(data))
        buf.writeBytes(data)
        buf.position(0)
        msg = TLRPC.Message.TLdeserialize(buf, buf.readInt32(False), False)
        buf.reuse()
        buf = None
        return msg
    except Exception:
        if buf:
            try:
                buf.reuse()
            except Exception:
                pass
        return None

def _format_date(ts):
    d = _dex()
    if d is not None:
        try:
            return str(d.call("formatDate", jlong(int(ts))))
        except Exception:
            pass
    try:
        import datetime
        dt = datetime.datetime.fromtimestamp(int(ts))
        return dt.strftime("%d.%m.%y %H:%M")
    except Exception:
        try:
            return str(LocaleController.formatDateOnline(int(ts), None))
        except Exception:
            return ""

def _get_media_hash(msg):
    d = _dex()
    if d is not None:
        try:
            r = d.call("mediaHash", msg)
            return str(r) if r is not None else None
        except Exception:
            pass
    try:
        if not msg:
            return None
        media = getattr(msg, "media", None)
        if not media:
            return None
        if hasattr(media, "photo") and media.photo:
            return f"photo_{media.photo.id}"
        if hasattr(media, "document") and media.document:
            return f"doc_{media.document.id}"
        if hasattr(media, "webpage") and media.webpage:
            return f"web_{media.webpage.id}"
        return f"media_{type(media).__name__}"
    except Exception:
        return None

_CA_MSGS_FIELD = None
_CA_MSGS_FIELD_READY = False


def _get_message_from_chat(did, mid, account=0):
    global _CA_MSGS_FIELD, _CA_MSGS_FIELD_READY
    try:
        try:
            mc = jclass("org.telegram.messenger.MessagesController").getInstance(int(account))
            d_msgs = mc.dialogMessage.get(int(did))
            if d_msgs:
                for i in range(d_msgs.size()):
                    mo = d_msgs.get(i)
                    if mo and int(mo.getId()) == int(mid):
                        return mo
        except Exception:
            pass
        try:
            ms = jclass("org.telegram.messenger.MessagesStorage").getInstance(int(account))
            msg = ms.getMessage(int(did), int(mid))
            if msg:
                mo_cls = jclass("org.telegram.messenger.MessageObject")
                return mo_cls(int(account), msg, False, False)
        except Exception:
            pass
        frag = LaunchActivity.getLastFragment()
        if isinstance(frag, ChatActivity):
            if not _CA_MSGS_FIELD_READY:
                _CA_MSGS_FIELD = _ff(jclass("org.telegram.ui.ChatActivity").getClass(), "messages")
                _CA_MSGS_FIELD_READY = True
            if _CA_MSGS_FIELD:
                msgs = _CA_MSGS_FIELD.get(frag)
                if msgs:
                    target = int(mid)
                    for i in range(msgs.size()):
                        mo = msgs.get(i)
                        if mo and int(mo.getId()) == target:
                            return mo
    except Exception:
        pass
    return None


def _describe_media(msg):
    try:
        if not msg:
            return ""
        media = getattr(msg, "media", None)
        if not media:
            return ""
        mn = str(media.getClass().getSimpleName())
        if "Photo" in mn:
            return L.g("has_photo")
        if "Document" in mn:
            doc = getattr(media, "document", None)
            if doc:
                try:
                    if MessageObject.isVideoDocument(doc):
                        return L.g("has_video")
                    if MessageObject.isStickerDocument(doc) or MessageObject.isAnimatedStickerDocument(doc):
                        return L.g("has_sticker")
                    if MessageObject.isVoiceDocument(doc):
                        return L.g("has_voice")
                    if MessageObject.isRoundVideoDocument(doc):
                        return L.g("has_round")
                except Exception:
                    pass
            return L.g("has_doc")
        if "Geo" in mn or "Venue" in mn:
            return "[geo]"
        if "Contact" in mn:
            return "[contact]"
        if "Poll" in mn:
            return "[poll]"
    except Exception:
        pass
    return ""

def _save_media_to_downloads(msg, context):
    try:
        if not msg:
            return False
        media = getattr(msg, "media", None)
        if not media:
            return False
        doc = getattr(media, "document", None)
        photo = getattr(media, "photo", None)
        acc = int(UserConfig.selectedAccount)
        fl = FileLoader.getInstance(acc)
        if doc:
            f = fl.getPathToAttach(doc, False)
            if not f or not f.exists():
                f = fl.getPathToAttach(doc, True)
            if f and f.exists():
                try:
                    MediaController = jclass("org.telegram.messenger.MediaController")
                    MediaController.saveFile(str(f.getAbsolutePath()), context, 2, None, None, None)
                    return True
                except Exception:
                    pass
        elif photo:
            try:
                sizes = photo.sizes
                ps = FileLoader.getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize())
                if ps:
                    f = fl.getPathToAttach(ps, False)
                    if f and f.exists():
                        MediaController = jclass("org.telegram.messenger.MediaController")
                        MediaController.saveFile(str(f.getAbsolutePath()), context, 0, None, None, None)
                        return True
            except Exception:
                pass
    except Exception:
        pass
    return False


def _ensure_nomedia(dirpath):
    if not dirpath:
        return
    try:
        nm = os.path.join(dirpath, ".nomedia")
        if os.path.exists(nm):
            return
    except Exception:
        pass
    try:
        JFile = jclass("java.io.File")
        f = JFile(os.path.join(dirpath, ".nomedia"))
        f.createNewFile()
        return
    except Exception:
        pass
    try:
        nm = os.path.join(dirpath, ".nomedia")
        with open(nm, "wb") as f:
            pass
    except Exception:
        pass


def _get_custom_attach_dir():
    try:
        prefs = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0)
        custom = str(prefs.getString("custom_attach_dir", "") or "")
        if custom and os.path.isdir(custom):
            _ensure_nomedia(custom)
            return custom
    except Exception:
        pass
    return None


def _get_attach_dir():
    custom = _get_custom_attach_dir()
    if custom:
        return custom
    try:
        from android.os import Environment
        dl = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        base = os.path.join(str(dl.getAbsolutePath()), "ExteraMax")
        os.makedirs(base, exist_ok=True)
        _ensure_nomedia(base)
        return base
    except Exception:
        pass
    try:
        base = os.path.join(ApplicationLoader.applicationContext.getFilesDir().getAbsolutePath(), "exteramax", "attachments")
        os.makedirs(base, exist_ok=True)
        _ensure_nomedia(base)
        return base
    except Exception:
        return None


_FOLDER_SIZES = (300 * 1024 * 1024, 1024 * 1024 * 1024, 2 * 1024 * 1024 * 1024,
                  4 * 1024 * 1024 * 1024, 8 * 1024 * 1024 * 1024, 16 * 1024 * 1024 * 1024, 0)
_CLEANUP_SECONDS = (0, 3 * 86400, 7 * 86400, 14 * 86400, 30 * 86400, 90 * 86400)


def _enforce_folder_limit():
    try:
        attach_dir = _get_attach_dir()
        if not attach_dir or not os.path.exists(attach_dir):
            return
        try:
            prefs = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0)
            idx = _int(prefs.getString("max_folder_size", "2"), 2)
        except Exception:
            idx = 2
        max_bytes = _FOLDER_SIZES[min(max(idx, 0), len(_FOLDER_SIZES) - 1)]
        if max_bytes <= 0:
            return
        files = []
        total_size = 0
        for fname in os.listdir(attach_dir):
            fpath = os.path.join(attach_dir, fname)
            if os.path.isfile(fpath):
                try:
                    st = os.stat(fpath)
                    files.append((st.st_mtime, st.st_size, fpath))
                    total_size += st.st_size
                except Exception:
                    pass
        if total_size <= max_bytes:
            return
        files.sort(key=lambda x: x[0])
        for mtime, fsize, fpath in files:
            if total_size <= max_bytes:
                break
            try:
                os.remove(fpath)
                total_size -= fsize
            except Exception:
                pass
    except Exception:
        pass


def _enforce_time_limit():
    try:
        attach_dir = _get_attach_dir()
        if not attach_dir or not os.path.exists(attach_dir):
            return
        try:
            prefs = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0)
            idx = _int(prefs.getString("auto_cleanup_age", "0"), 0)
        except Exception:
            idx = 0
        max_age = _CLEANUP_SECONDS[min(max(idx, 0), len(_CLEANUP_SECONDS) - 1)]
        if max_age <= 0:
            return
        now = _time.time()
        for fname in os.listdir(attach_dir):
            fpath = os.path.join(attach_dir, fname)
            if os.path.isfile(fpath):
                try:
                    if now - os.path.getmtime(fpath) > max_age:
                        os.remove(fpath)
                except Exception:
                    pass
    except Exception:
        pass


def _get_file_from_msg(msg):
    if not msg:
        return None
    try:
        acc = int(UserConfig.selectedAccount)
        fl = FileLoader.getInstance(acc)
        media = getattr(msg, "media", None)
        if not media:
            return None
        doc = getattr(media, "document", None)
        photo = getattr(media, "photo", None)
        if doc:
            for b in [False, True]:
                f = fl.getPathToAttach(doc, b)
                if f and f.exists():
                    return f
        elif photo:
            try:
                if hasattr(photo, "sizes") and photo.sizes:
                    for ps in _jlist(photo.sizes):
                        for b in [False, True]:
                            f = fl.getPathToAttach(ps, b)
                            if f and f.exists():
                                return f
            except Exception:
                pass
    except Exception:
        pass
    return None


def _get_custom_attach_doc_tree():
    # Bug fix (part of #14): when the folder picked via SAF (Storage Access
    # Framework) can't be resolved to a real filesystem path (e.g. SD card, cloud
    # storage provider), the plugin used to store only a display-only URI that
    # nothing ever wrote to - _get_attach_dir() would silently fall back to the
    # app's default private folder while settings kept showing the chosen folder
    # as "set", so attachments ended up saved somewhere the user never checked.
    # This resolves that URI to a real DocumentFile tree so we can actually write
    # into it via the ContentResolver instead of silently using another folder.
    try:
        prefs = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0)
        uri_str = str(prefs.getString("custom_attach_uri", "") or "")
        if not uri_str:
            return None
        from android.net import Uri
        from androidx.documentfile.provider import DocumentFile
        uri = Uri.parse(uri_str)
        tree = DocumentFile.fromTreeUri(ApplicationLoader.applicationContext, uri)
        if tree and tree.exists() and tree.canWrite():
            return tree
    except Exception:
        pass
    return None


def _copy_path_to_vault_saf(src_path, did, mid, media_hash=None):
    try:
        tree = _get_custom_attach_doc_tree()
        if not tree:
            return None
        ext = ""
        dot = src_path.rfind(".")
        if dot >= 0:
            ext = src_path[dot:]
        suffix = f"_{media_hash}" if media_hash else ""
        name = f"{did}_{mid}{suffix}{ext}"
        existing = tree.findFile(name)
        if existing is not None and existing.exists():
            return str(existing.getUri().toString())
        mime = "application/octet-stream"
        doc = tree.createFile(mime, name)
        if doc is None:
            return None
        resolver = ApplicationLoader.applicationContext.getContentResolver()
        out = resolver.openOutputStream(doc.getUri())
        try:
            with open(src_path, "rb") as fsrc:
                while True:
                    chunk = fsrc.read(1024 * 256)
                    if not chunk:
                        break
                    from java.lang import String as JString
                    out.write(bytearray(chunk))
        finally:
            try:
                out.close()
            except Exception:
                pass
        return str(doc.getUri().toString())
    except Exception:
        return None


def _copy_path_to_vault(src_path, did, mid, media_hash=None):
    try:
        if not src_path or not os.path.exists(src_path):
            return None
        attach_dir = _get_attach_dir()
        # Bug fix (#14): if the configured folder is a SAF tree that has no
        # resolvable real path, write into it via ContentResolver instead of
        # silently falling back to the default directory.
        if not _get_custom_attach_dir() and _get_custom_attach_doc_tree() is not None:
            saf_result = _copy_path_to_vault_saf(src_path, did, mid, media_hash)
            if saf_result:
                _enforce_folder_limit()
                return saf_result
        if not attach_dir:
            return None
        ext = ""
        dot = src_path.rfind(".")
        if dot >= 0:
            ext = src_path[dot:]
        suffix = f"_{media_hash}" if media_hash else ""
        dst_path = os.path.join(attach_dir, f"{did}_{mid}{suffix}{ext}")
        if os.path.exists(dst_path):
            return dst_path
        import shutil
        shutil.copy2(src_path, dst_path)
        _enforce_folder_limit()
        return dst_path
    except Exception:
        return None


def _copy_attach_to_vault(msg, did, mid, media_hash=None):
    try:
        src = _get_file_from_msg(msg)
        if not src or not src.exists():
            return None
        return _copy_path_to_vault(str(src.getAbsolutePath()), did, mid, media_hash)
    except Exception:
        return None


AUTO_DOWNLOAD_MAX_SIZE = 10 * 1024 * 1024


def _ensure_attach_saved(msg, did, mid, media_hash=None, force_download=False):
    try:
        p = _copy_attach_to_vault(msg, did, mid, media_hash)
        if p:
            return p
        media = getattr(msg, "media", None) if msg else None
        if not media:
            return None
        doc = getattr(media, "document", None)
        photo = getattr(media, "photo", None)
        doc_size = int(getattr(doc, "size", 0)) if doc else 0
        if not force_download and doc and doc_size > AUTO_DOWNLOAD_MAX_SIZE:
            return None
        acc = int(UserConfig.selectedAccount)
        fl = FileLoader.getInstance(acc)
        mo_tmp = MessageObject(acc, msg, False, False)
        if doc:
            try:
                fl.loadFile(doc, mo_tmp, 1, 0)
            except Exception:
                pass
        elif photo:
            try:
                sizes = photo.sizes
                ps = FileLoader.getClosestPhotoSizeWithSize(sizes, AndroidUtilities.getPhotoSize())
                if ps:
                    fl.loadFile(ImageLocation.getForPhoto(ps, photo), mo_tmp, None, 1, 0)
            except Exception:
                pass
        else:
            return None
        def _retry():
            for delay in (2.0, 5.0, 10.0, 20.0):
                _time.sleep(delay)
                try:
                    saved = _copy_attach_to_vault(msg, did, mid, media_hash)
                    if saved:
                        return
                except Exception:
                    pass
        threading.Thread(target=_retry, daemon=True).start()
    except Exception:
        pass
    return None


def _get_vault_attach(did, mid):
    try:
        attach_dir = _get_attach_dir()
        if not attach_dir:
            return None
        prefix = f"{did}_{mid}"
        best = None
        best_time = 0
        for fname in os.listdir(attach_dir):
            if fname.startswith(prefix):
                full = os.path.join(attach_dir, fname)
                if os.path.isfile(full) and os.path.getsize(full) > 0:
                    t = os.path.getmtime(full)
                    if t > best_time:
                        best_time = t
                        best = full
        return best
    except Exception:
        pass
    return None
def _get_vault_attachs(did, mid):
    try:
        attach_dir = _get_attach_dir()
        if not attach_dir:
            return []
        prefix = f"{did}_{mid}"
        result = []
        for fname in sorted(os.listdir(attach_dir)):
            if fname.startswith(prefix):
                full = os.path.join(attach_dir, fname)
                if os.path.isfile(full) and os.path.getsize(full) > 0:
                    result.append(full)
        return result
    except Exception:
        return []


def _open_file(path):
    try:
        if not path:
            return
        JFile = jclass("java.io.File")
        f = JFile(path)
        if not f.exists():
            return
        ctx = ApplicationLoader.applicationContext
        uri = jclass("androidx.core.content.FileProvider").getUriForFile(ctx, ctx.getPackageName() + ".provider", f)
        mime = "*/*"
        name = str(f.getName()).lower()
        if name.endswith((".jpg", ".jpeg", ".png", ".webp")):
            mime = "image/*"
        elif name.endswith((".mp4", ".mkv", ".avi")):
            mime = "video/*"
        elif name.endswith((".mp3", ".ogg", ".wav", ".m4a")):
            mime = "audio/*"
        from android.content import Intent
        intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(intent)
    except Exception:
        try:
            from android.content import Intent
            ctx = ApplicationLoader.applicationContext
            intent = Intent(Intent.ACTION_VIEW)
            from android.net import Uri
            intent.setData(Uri.parse("file://" + path))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        except Exception:
            pass


def _is_out_message(did, mid):
    try:
        frag = LaunchActivity.getLastFragment()
        if isinstance(frag, ChatActivity):
            msgs_field = _ff(CA_CLS, "messages")
            if msgs_field:
                msgs = msgs_field.get(frag)
                if msgs:
                    for i in range(msgs.size()):
                        mo = msgs.get(i)
                        if mo and int(mo.getId()) == int(mid) and int(mo.getDialogId()) == int(did):
                            return bool(mo.isOut())
    except Exception:
        pass
    try:
        mc = MessagesController.getInstance(UserConfig.selectedAccount)
        dm = getattr(mc, "dialogMessagesByIds", None)
        if dm:
            obj = dm.get(Integer(int(mid)))
            if obj and isinstance(obj, MessageObject):
                return bool(obj.isOut())
    except Exception:
        pass
    return False


def _save_attachments_for_mids(did, mids):
    try:
        frag = LaunchActivity.getLastFragment()
        if not isinstance(frag, ChatActivity):
            return
        msgs_field = _ff(CA_CLS, "messages")
        if not msgs_field:
            return
        msgs = msgs_field.get(frag)
        if not msgs:
            return
        mid_set = set(int(m) for m in mids)
        for i in range(msgs.size()):
            mo = msgs.get(i)
            if mo and int(mo.getId()) in mid_set:
                msg = mo.messageOwner
                if msg:
                    try:
                        _ensure_attach_saved(msg, did, int(mo.getId()), _get_media_hash(msg))
                    except Exception:
                        pass
    except Exception:
        pass


def _collect_deleted_text(did, mids, account=None):
    data_map = {}
    if account is None:
        account = UserConfig.selectedAccount
    try:
        frag = LaunchActivity.getLastFragment()
        if isinstance(frag, ChatActivity):
            msgs_field = _ff(CA_CLS, "messages")
            if msgs_field:
                msgs = msgs_field.get(frag)
                if msgs:
                    mid_set = set(int(m) for m in mids)
                    for i in range(msgs.size()):
                        mo = msgs.get(i)
                        if mo and int(mo.getId()) in mid_set:
                            owner = mo.messageOwner
                            text = str(owner.message) if owner and owner.message else ""
                            if text:
                                data_map[int(mo.getId())] = text
    except Exception:
        pass
    try:
        mc = MessagesController.getInstance(account)
        dm = getattr(mc, "dialogMessagesByIds", None)
        if dm:
            for mid in mids:
                if int(mid) in data_map:
                    continue
                try:
                    obj = dm.get(Integer(int(mid)))
                    if obj and obj.messageOwner and obj.messageOwner.message:
                        data_map[int(mid)] = str(obj.messageOwner.message)
                except Exception:
                    pass
    except Exception:
        pass
    return data_map


def _find_old_text(did, mid, account):
    try:
        frag = LaunchActivity.getLastFragment()
        if isinstance(frag, ChatActivity):
            msgs_field = _ff(CA_CLS, "messages")
            if msgs_field:
                msgs = msgs_field.get(frag)
                if msgs:
                    for i in range(msgs.size()):
                        mo = msgs.get(i)
                        if mo and int(mo.getId()) == int(mid):
                            owner = mo.messageOwner
                            if owner and owner.message:
                                return str(owner.message)
                            break
    except Exception:
        pass
    try:
        mc = MessagesController.getInstance(account)
        obj = mc.dialogMessagesByIds.get(Integer(int(mid)))
        if obj and obj.messageOwner and obj.messageOwner.message:
            return str(obj.messageOwner.message)
    except Exception:
        pass
    return None


def _fp_base_dir():
    try:
        d = os.path.join(ApplicationLoader.applicationContext.getFilesDir().getAbsolutePath(), "exteramax")
        os.makedirs(d, exist_ok=True)
        return d
    except Exception:
        return "/tmp"

def _self_uid(acc=None):
    try:
        if acc is None:
            acc = UserConfig.selectedAccount
        uc = UserConfig.getInstance(acc)
        if uc and uc.isClientActivated():
            return int(uc.getClientUserId())
    except Exception:
        pass
    return 0

def _emoji_cache_path(acc=None, uid=None):
    try:
        d = _fp_base_dir()
        u = int(uid) if uid is not None else _self_uid(acc)
        if not u:
            return None
        return os.path.join(d, f"emoji_status_{u}.json")
    except Exception:
        return None

def _premium_cache_path(acc=None, uid=None):
    try:
        d = _fp_base_dir()
        u = int(uid) if uid is not None else _self_uid(acc)
        if not u:
            return None
        return os.path.join(d, f"premium_state_{u}.json")
    except Exception:
        return None

def _atomic_write_json(path, data):
    tmp = path + ".tmp"
    try:
        with open(tmp, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False)
        os.replace(tmp, path)
    except Exception:
        try:
            os.remove(tmp)
        except Exception:
            pass

def _safe_read_json(path):
    try:
        if not path or not os.path.exists(path):
            return None
        with open(path, "r", encoding="utf-8") as f:
            d = json.load(f)
        return d if isinstance(d, dict) else None
    except Exception:
        return None

def _is_empty_es(es):
    if es is None:
        return True
    try:
        if isinstance(es, TLRPC.TL_emojiStatusEmpty):
            return True
    except Exception:
        pass
    return False

def _is_empty_color(c):
    if c is None:
        return True
    try:
        if int(getattr(c, "flags", 0)) == 0:
            return True
    except Exception:
        pass
    return False

def _emoji_to_dict(es):
    try:
        if es is None:
            return None
        if isinstance(es, TLRPC.TL_emojiStatusEmpty):
            return None
        try:
            if isinstance(es, TLRPC.TL_emojiStatusCollectible):
                return {
                    "type": "collectible",
                    "flags": int(es.flags),
                    "collectible_id": int(es.collectible_id),
                    "document_id": int(es.document_id),
                    "title": str(es.title or ""),
                    "slug": str(es.slug or ""),
                    "pattern_document_id": int(es.pattern_document_id),
                    "center_color": int(es.center_color),
                    "edge_color": int(es.edge_color),
                    "pattern_color": int(es.pattern_color),
                    "text_color": int(es.text_color),
                    "until": int(es.until)
                }
        except Exception:
            pass
        try:
            if isinstance(es, TLRPC.TL_emojiStatus):
                return {
                    "type": "status",
                    "flags": int(es.flags),
                    "document_id": int(es.document_id),
                    "until": int(es.until)
                }
        except Exception:
            pass
    except Exception:
        pass
    return None

def _dict_to_emoji(d):
    try:
        if not d:
            return None
        t = d.get("type")
        if t == "status":
            es = TLRPC.TL_emojiStatus()
            es.flags = int(d.get("flags", 0))
            es.document_id = int(d.get("document_id", 0))
            es.until = int(d.get("until", 0))
            return es
        if t == "collectible":
            es = TLRPC.TL_emojiStatusCollectible()
            es.flags = int(d.get("flags", 0))
            es.collectible_id = int(d.get("collectible_id", 0))
            es.document_id = int(d.get("document_id", 0))
            es.title = d.get("title", "") or ""
            es.slug = d.get("slug", "") or ""
            es.pattern_document_id = int(d.get("pattern_document_id", 0))
            es.center_color = int(d.get("center_color", 0))
            es.edge_color = int(d.get("edge_color", 0))
            es.pattern_color = int(d.get("pattern_color", 0))
            es.text_color = int(d.get("text_color", 0))
            es.until = int(d.get("until", 0))
            return es
    except Exception:
        pass
    return None

def _peer_color_to_dict(c):
    try:
        if c is None:
            return None
        try:
            if isinstance(c, TLRPC.TL_peerColorCollectible):
                d = {
                    "type": "collectible",
                    "flags": int(c.flags),
                    "collectible_id": int(c.collectible_id),
                    "gift_emoji_id": int(c.gift_emoji_id),
                    "background_emoji_id": int(c.background_emoji_id),
                    "accent_color": int(c.accent_color),
                    "dark_accent_color": int(c.dark_accent_color)
                }
                try:
                    d["colors"] = [int(x) for x in c.colors] if c.colors else []
                except Exception:
                    d["colors"] = []
                try:
                    d["dark_colors"] = [int(x) for x in c.dark_colors] if c.dark_colors else []
                except Exception:
                    d["dark_colors"] = []
                return d
        except Exception:
            pass
        try:
            if isinstance(c, TLRPC.TL_peerColor):
                return {
                    "type": "plain",
                    "flags": int(c.flags),
                    "color": int(c.color),
                    "background_emoji_id": int(c.background_emoji_id)
                }
        except Exception:
            pass
    except Exception:
        pass
    return None

def _dict_to_peer_color(d):
    try:
        if not d:
            return None
        t = d.get("type")
        if t == "plain":
            c = TLRPC.TL_peerColor()
            c.flags = int(d.get("flags", 0))
            c.color = int(d.get("color", 0))
            c.background_emoji_id = int(d.get("background_emoji_id", 0))
            return c
        if t == "collectible":
            c = TLRPC.TL_peerColorCollectible()
            c.flags = int(d.get("flags", 0))
            c.collectible_id = int(d.get("collectible_id", 0))
            c.gift_emoji_id = int(d.get("gift_emoji_id", 0))
            c.background_emoji_id = int(d.get("background_emoji_id", 0))
            c.accent_color = int(d.get("accent_color", 0))
            c.dark_accent_color = int(d.get("dark_accent_color", 0))
            try:
                cs = ArrayList()
                for x in (d.get("colors") or []):
                    cs.add(Integer(int(x)))
                c.colors = cs
                dcs = ArrayList()
                for x in (d.get("dark_colors") or []):
                    dcs.add(Integer(int(x)))
                c.dark_colors = dcs
            except Exception:
                pass
            return c
    except Exception:
        pass
    return None


class _DbWorker(threading.Thread):
    def __init__(self, db_path):
        super().__init__(daemon=True, name="MAX-DB")
        self._path = db_path
        self._queue = []
        self._lock = threading.Lock()
        self._event = threading.Event()
        self._conn = None
        self._stop = False

    def _get_conn(self):
        if self._conn is None:
            self._conn = sqlite3.connect(self._path, check_same_thread=False)
            self._conn.execute("PRAGMA journal_mode=WAL")
            self._conn.execute("PRAGMA synchronous=FULL")
        return self._conn

    def push(self, ops):
        with self._lock:
            self._queue.append(ops)
        self._event.set()

    def stop(self):
        self._stop = True
        self._event.set()
        try:
            self.join(timeout=2.0)
        except Exception:
            pass
        self._drain_final()

    def run(self):
        while not self._stop:
            self._event.wait()
            self._event.clear()
            self._drain()
        self._drain()

    def _drain_final(self):
        try:
            conn = sqlite3.connect(self._path, check_same_thread=False)
            conn.execute("PRAGMA journal_mode=WAL")
            conn.execute("PRAGMA synchronous=FULL")
            with self._lock:
                remaining = self._queue[:]
                self._queue.clear()
            for ops in remaining:
                for sql, params in ops:
                    try:
                        conn.execute(sql, params)
                    except Exception:
                        pass
            conn.commit()
            conn.close()
        except Exception:
            pass

    def _drain(self):
        while True:
            with self._lock:
                if not self._queue:
                    return
                batch = self._queue[:]
                self._queue.clear()
            try:
                conn = self._get_conn()
                for ops in batch:
                    for sql, params in ops:
                        try:
                            conn.execute(sql, params)
                        except Exception as e:
                            log(f"[MAX] db op: {e}")
                conn.commit()
            except Exception as e:
                log(f"[MAX] db drain: {e}")


class Vault:
    def __init__(self):
        base = os.path.join(ApplicationLoader.applicationContext.getFilesDir().getAbsolutePath(), "exteramax")
        os.makedirs(base, exist_ok=True)
        self._db_path = os.path.join(base, "vault.db")
        self._removed = set()
        self._removed_ts = {}
        self._revisions = {}
        self._originals = {}
        self._rx = []
        self._rx_cache = []
        self._read_conn = None
        self._read_lock = threading.Lock()
        self._worker = _DbWorker(self._db_path)
        self._init_schema()
        self._load()
        self._worker.start()

    def _sync_conn(self):
        conn = sqlite3.connect(self._db_path, check_same_thread=False)
        conn.execute("PRAGMA journal_mode=WAL")
        conn.execute("PRAGMA synchronous=FULL")
        return conn

    def _get_read_conn(self):
        if self._read_conn is None:
            self._read_conn = sqlite3.connect(self._db_path, check_same_thread=False)
            self._read_conn.execute("PRAGMA journal_mode=WAL")
        return self._read_conn

    def _init_schema(self):
        conn = self._sync_conn()
        try:
            conn.execute("CREATE TABLE IF NOT EXISTS removed (dialog_id INTEGER NOT NULL, message_id INTEGER NOT NULL, timestamp INTEGER DEFAULT 0, data TEXT, PRIMARY KEY (dialog_id, message_id))")
            removed_cols = [str(r[1]) for r in conn.execute("PRAGMA table_info(removed)").fetchall()]
            if "timestamp" not in removed_cols:
                conn.execute("ALTER TABLE removed ADD COLUMN timestamp INTEGER DEFAULT 0")
            if "data" not in removed_cols:
                conn.execute("ALTER TABLE removed ADD COLUMN data TEXT")
            conn.execute("CREATE TABLE IF NOT EXISTS revisions (dialog_id INTEGER NOT NULL, message_id INTEGER NOT NULL, ver INTEGER NOT NULL, timestamp INTEGER DEFAULT 0, data TEXT, text TEXT DEFAULT '', media_hash TEXT DEFAULT '', PRIMARY KEY (dialog_id, message_id, ver))")
            revision_cols = [str(r[1]) for r in conn.execute("PRAGMA table_info(revisions)").fetchall()]
            required = {"dialog_id", "message_id", "ver", "timestamp", "data", "text", "media_hash"}
            if not required.issubset(set(revision_cols)):
                rows = conn.execute("SELECT * FROM revisions").fetchall()
                conn.execute("DROP TABLE IF EXISTS revisions_migrated")
                conn.execute("CREATE TABLE revisions_migrated (dialog_id INTEGER NOT NULL, message_id INTEGER NOT NULL, ver INTEGER NOT NULL, timestamp INTEGER DEFAULT 0, data TEXT, text TEXT DEFAULT '', media_hash TEXT DEFAULT '', PRIMARY KEY (dialog_id, message_id, ver))")
                for row in rows:
                    old = dict(zip(revision_cols, row))
                    did = _int(old.get("dialog_id", 0))
                    mid = _int(old.get("message_id", 0))
                    ver = _int(old.get("ver", old.get("idx", 0)))
                    ts = _int(old.get("timestamp", 0))
                    data = old.get("data")
                    text = old.get("text", "") or ""
                    media_hash = old.get("media_hash", "") or ""
                    if did and mid:
                        conn.execute("INSERT OR IGNORE INTO revisions_migrated(dialog_id,message_id,ver,timestamp,data,text,media_hash) VALUES(?,?,?,?,?,?,?)", (did, mid, ver, ts, data, text, media_hash))
                conn.execute("DROP TABLE revisions")
                conn.execute("ALTER TABLE revisions_migrated RENAME TO revisions")
            originals_cols = [str(r[1]) for r in conn.execute("PRAGMA table_info(originals)").fetchall()]
            if {"dialog_id", "message_id", "text"}.issubset(set(originals_cols)):
                conn.execute("INSERT OR IGNORE INTO revisions(dialog_id,message_id,ver,timestamp,data,text,media_hash) SELECT dialog_id,message_id,0,0,NULL,text,'' FROM originals")
            conn.execute("CREATE TABLE IF NOT EXISTS rx_patterns (id INTEGER PRIMARY KEY AUTOINCREMENT, pattern TEXT NOT NULL)")
            conn.execute("CREATE INDEX IF NOT EXISTS idx_rem_did ON removed(dialog_id)")
            conn.execute("CREATE INDEX IF NOT EXISTS idx_rev_did_mid ON revisions(dialog_id, message_id)")
            conn.commit()
        finally:
            conn.close()

    def _load(self):
        conn = self._sync_conn()
        try:
            cur = conn.cursor()
            cur.execute("SELECT dialog_id, message_id, timestamp FROM removed")
            for did, mid, ts in cur.fetchall():
                self._removed.add((int(did), int(mid)))
                self._removed_ts[(int(did), int(mid))] = int(ts or 0)
            cur.execute("SELECT dialog_id, message_id, ver, timestamp, text, media_hash FROM revisions ORDER BY dialog_id, message_id, ver LIMIT 20000")
            for row in cur.fetchall():
                did, mid, ver, ts, text, mh = row
                k = (int(did), int(mid))
                if k not in self._revisions:
                    self._revisions[k] = []
                self._revisions[k].append({
                    "ver": int(ver),
                    "timestamp": int(ts or 0),
                    "text": text or "",
                    "media_hash": mh or ""
                })
            cur.execute("SELECT pattern FROM rx_patterns ORDER BY id")
            self._rx = [r[0] for r in cur.fetchall()]
            self._rebuild_rx()
        except Exception as e:
            log(f"[MAX] vault load: {e}")
        finally:
            conn.close()

    def _db(self, ops):
        if ops:
            self._worker.push(ops)

    def _rebuild_rx(self):
        cache = []
        for p in self._rx:
            try:
                cache.append(re.compile(p, re.IGNORECASE | re.MULTILINE))
            except Exception:
                pass
        self._rx_cache = cache

    def mark_removed(self, did, mids, data_map=None):
        if int(did) == 489000:
            return
        ts = int(_time.time())
        ops = []
        for mid in mids:
            k = (int(did), int(mid))
            if k not in self._removed:
                self._removed.add(k)
                self._removed_ts[k] = ts
                data = data_map.get(int(mid)) if data_map else None
                ops.append(("INSERT OR IGNORE INTO removed(dialog_id,message_id,timestamp,data) VALUES(?,?,?,?)", (int(did), int(mid), ts, data)))
        self._db(ops)

    def is_removed(self, did, mid):
        return (int(did), int(mid)) in self._removed

    def is_removed_obj(self, mo):
        try:
            return self.is_removed(mo.getDialogId(), mo.getId())
        except Exception:
            return False

    def get_removed_list(self, did):
        result = []
        try:
            conn = self._sync_conn()
            try:
                cur = conn.cursor()
                cur.execute("SELECT message_id, data, timestamp FROM removed WHERE dialog_id=? ORDER BY timestamp DESC LIMIT 200", (int(did),))
                for row in cur.fetchall():
                    mid = int(row[0])
                    text = str(row[1]) if row[1] else ""
                    ts = int(row[2] or 0)
                    result.append({"mid": mid, "text": text, "ts": ts})
            finally:
                conn.close()
        except Exception:
            pass
        return result

    def get_removed_mids(self, did):
        try:
            did = int(did)
            return [m for (d, m) in self._removed if d == did]
        except Exception:
            return []

    def purge_removed(self, did, mids):
        ops = []
        for mid in mids:
            self._removed.discard((int(did), int(mid)))
            self._removed_ts.pop((int(did), int(mid)), None)
            ops.append(("DELETE FROM removed WHERE dialog_id=? AND message_id=?", (int(did), int(mid))))
        self._db(ops)

    def clear_all_removed(self):
        try:
            self._removed.clear()
            self._removed_ts.clear()
            self._db([("DELETE FROM removed", ())])
        except Exception as e:
            log(f"[MAX] clear_all_removed: {e}")

    def has_revisions(self, did, mid):
        return any(int(r.get("ver", 0)) > 0 for r in self._revisions.get((int(did), int(mid)), []))

    def save_message_version(self, did, mid, msg, is_original=False):
        k = (int(did), int(mid))
        ts = int(_time.time())
        text = ""
        data = None
        media_hash = ""
        try:
            if msg:
                text = str(msg.message) if msg.message else ""
                media_hash = _get_media_hash(msg) or ""
                try:
                    encoded = _encode_msg(msg)
                    data = str(encoded) if encoded else None
                except Exception as enc_e:
                    log(f"[MAX] encode_msg: {enc_e}")
                if is_original:
                    source_ts = _int(getattr(msg, "date", 0))
                else:
                    source_ts = _int(getattr(msg, "edit_date", 0))
                    if source_ts <= 0:
                        source_ts = _int(getattr(msg, "date", 0))
                if source_ts > 0:
                    ts = source_ts
        except Exception:
            pass
        if k not in self._revisions:
            self._revisions[k] = []
        lst = self._revisions[k]
        if lst:
            last = lst[-1]
            if last["text"] == text and last["media_hash"] == media_hash:
                return
        ver = 0 if is_original else (lst[-1]["ver"] + 1 if lst else 1)
        if is_original and lst:
            return
        mem_entry = {"ver": ver, "timestamp": ts, "text": text, "media_hash": media_hash}
        if is_original:
            lst.insert(0, mem_entry)
            self._db([("DELETE FROM revisions WHERE dialog_id=? AND message_id=?", (int(did), int(mid)))])
            ops = []
            for i, e in enumerate(lst):
                ops.append(("INSERT INTO revisions(dialog_id,message_id,ver,timestamp,data,text,media_hash) VALUES(?,?,?,?,?,?,?)",
                           (int(did), int(mid), i, e["timestamp"], data if i == 0 else None, e["text"], e["media_hash"])))
            self._db(ops)
        else:
            lst.append(mem_entry)
            self._db([("INSERT OR REPLACE INTO revisions(dialog_id,message_id,ver,timestamp,data,text,media_hash) VALUES(?,?,?,?,?,?,?)",
                      (int(did), int(mid), ver, ts, data, text, media_hash))])

    def get_revisions(self, did, mid):
        revs = list(self._revisions.get((int(did), int(mid)), []))
        if revs:
            try:
                conn = self._sync_conn()
                try:
                    cur = conn.cursor()
                    cur.execute("SELECT ver, data FROM revisions WHERE dialog_id=? AND message_id=? AND data IS NOT NULL", (int(did), int(mid)))
                    data_map = {int(r[0]): r[1] for r in cur.fetchall()}
                    for rev in revs:
                        v = int(rev.get("ver", 0))
                        if v in data_map:
                            rev["data"] = data_map[v]
                finally:
                    conn.close()
            except Exception:
                pass
        return revs

    def get_rx(self):
        return self._rx[:]

    def add_rx(self, pattern):
        self._rx.append(pattern)
        self._db([("INSERT INTO rx_patterns(pattern) VALUES(?)", (pattern,))])
        self._rebuild_rx()

    def remove_rx(self, index):
        if 0 <= index < len(self._rx):
            p = self._rx.pop(index)
            self._db([("DELETE FROM rx_patterns WHERE pattern=?", (p,))])
            self._rebuild_rx()

    def rx_match(self, mo):
        if not mo or not self._rx_cache:
            return False
        try:
            txt = mo.messageOwner.message
            return bool(txt) and any(r.search(txt) for r in self._rx_cache)
        except Exception:
            return False

    def wipe(self):
        self._removed.clear()
        self._removed_ts.clear()
        self._revisions.clear()
        self._db([("DELETE FROM removed", ()), ("DELETE FROM revisions", ())])

    def shutdown(self):
        try:
            self._worker.stop()
        except Exception:
            pass
        try:
            if self._read_conn:
                self._read_conn.close()
                self._read_conn = None
        except Exception:
            pass


class JavaRunnable(dynamic_proxy(Runnable)):
    def __init__(self, fn):
        super().__init__()
        self._fn = fn
    def run(self):
        try:
            self._fn()
        except Exception:
            pass


def _focus_and_show_keyboard(dialog, edit_text):
    # Единая логика автофокуса + принудительного показа клавиатуры для диалогов
    # с полем ввода на python-стороне плагина (см. Main.forceShowKeyboardForDialog
    # в Java-части — та же проблема и тот же фикс). AndroidUtilities.showKeyboard()
    # сам по себе часто не срабатывает для AlertDialog поверх активности: у окна
    # диалога может стоять флаг FLAG_ALT_FOCUSABLE_IM, блокирующий клавиатурный
    # фокус, поэтому его снимаем явно. requestFocus() оборачиваем в edit_text.post(),
    # чтобы не пытаться выставить фокус раньше, чем поле полностью attach-нуто к
    # окну (в момент вызова сразу после d.show() layout ещё может быть не завершён).
    # InputMethodManager.showSoftInput(..., SHOW_FORCED) — гарантированный фолбэк,
    # не зависящий от внутренней реализации AndroidUtilities.showKeyboard().
    try:
        window = dialog.getWindow()
        if window is not None:
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    except Exception as e:
        log(f"[MAX] kbdFlags: {e}")

    def _do():
        try:
            edit_text.setFocusable(True)
            edit_text.setFocusableInTouchMode(True)
            edit_text.requestFocus()
            AndroidUtilities.showKeyboard(edit_text)
            imm = cast(InputMethodManager, edit_text.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
            if imm is not None:
                imm.showSoftInput(edit_text, InputMethodManager.SHOW_FORCED)
        except Exception as e:
            log(f"[MAX] kbdShow: {e}")
    try:
        edit_text.post(JavaRunnable(_do))
    except Exception as e:
        log(f"[MAX] kbdPost: {e}")


class Dex:
    def __init__(self, ec):
        self.ec = ec
        self.clazz = None
        self.loaded = False
        self._m = {}
    def load(self):
        if self.loaded:
            return
        if self.clazz is None:
            ctx = ApplicationLoader.applicationContext
            if not ctx:
                raise RuntimeError("no ctx")
            self.clazz = jclass("dalvik.system.InMemoryDexClassLoader")(jclass("java.nio.ByteBuffer").wrap(self._rp()), ctx.getClassLoader()).loadClass(self.ec)
            for m in self.clazz.getDeclaredMethods():
                m.setAccessible(True)
                self._m[str(m.getName())] = m
        self._m["load"].invoke(None)
        self.loaded = True
    def unload(self):
        try:
            if self.loaded and "unload" in self._m:
                self._m["unload"].invoke(None)
        except Exception:
            pass
        self.loaded = False
    def call(self, name, *args):
        try:
            if self.loaded and name in self._m:
                return self._m[name].invoke(None, *args)
        except Exception as e:
            log(f"[MAX] dex.{name}: {e}")
        return None
    def _rp(self):
        _dex_b64 = globals().get("_DEX_PAYLOAD")
        if _dex_b64:
            import base64, zlib
            return zlib.decompress(base64.b64decode(_dex_b64))
        with open(__file__, "r", encoding="utf-8") as f:
            t = f.read()
        b = t.split(f"# {DEX_BEGIN}", 1)[1].split(f"# {DEX_END}", 1)[0]
        p = "".join(l.strip()[1:].strip() for l in b.splitlines() if l.strip().startswith("#"))
        if not p:
            raise RuntimeError("dex empty")
        return zlib.decompress(base64.b64decode(p))


CA_CLS = jclass("org.telegram.ui.ChatActivity").getClass()
IS_NEW = False
try:
    CA_CLS.getDeclaredField("actionsButtonsLayout")
    IS_NEW = True
except Exception:
    pass
HIDE_ACTION = _fm(CA_CLS, "hideActionMode", 0)
UPD_PINNED = _fm(CA_CLS, "updatePinnedMessageView", 1)
UNSEL_ADD = _fm(CA_CLS, "addToSelectedMessages", 3)
UPD_VISIBLE = _fm(CA_CLS, "updateVisibleRows", 0)
UPD_AM_TITLE = _fm(CA_CLS, "updateActionModeTitle", 0)

_TAB_GATE = {
    "tab_ghost": frozenset(["ghost_mode", "ghost_no_online", "ghost_immediate_offline", "ghost_no_typing", "ghost_no_read", "ghost_no_read_stories", "read_on_action", "use_scheduled", "silent_send", "story_when_ghost_on", "story_when_ghost_off", "drawer_ghost_btn", "local_read_msgs", "local_read_private", "local_read_groups", "local_read_channels", "local_read_stories"]),
    "tab_spy": frozenset(["keep_app_alive", "save_deleted", "save_edits", "save_bot_chats", "presence_track", "track_own_deletions", "keep_once", "save_last_online", "del_show_mark", "del_use_icon", "del_custom_label", "del_color", "del_mark_color", "del_mark_emoji_id", "del_show_opacity", "del_opacity_level", "save_attachments", "save_attach_private", "save_attach_groups", "save_attach_channels", "max_folder_size", "auto_cleanup_age", "presence_only_hidden", "presence_decay"]),
    "tab_hacker": frozenset(["hide_ads", "hide_feed_ads", "no_forward_limit", "fake_premium", "allow_screenshots", "bypass_noforward", "fwd_mode", "bypass_story_save"]),
}


class NFLSelectorHook(MethodHook):
    def __init__(self, plugin):
        super().__init__()
        self.plugin = plugin
        self.msgs = {}
        self.grp = False
        self.grp_last = False
        self.grp_end = False
    def _idx(self, param):
        return int(param.args[0].getDialogId() != get_private_field(param.thisObject, "dialog_id"))
    def before_hooked_method(self, param):
        if not self.plugin._gs("no_forward_limit", False):
            return
        ca = param.thisObject
        smo = param.args[0]
        if self.grp:
            self.grp_last = bool(param.args[2])
            return
        if bool(param.args[1]) and bool(param.args[2]) and smo.getGroupId() != 0:
            self.grp = True
        idx = self._idx(param)
        jms = get_private_field(ca, "selectedMessagesIds")
        try:
            jms[idx] = ca.SparseArrayWithTouch()
        except Exception:
            jms[idx] = SparseArray()
        set_private_field(ca, "selectedMessagesIds", jms)
    def after_hooked_method(self, param):
        if not self.plugin._gs("no_forward_limit", False):
            return
        if self.grp_end:
            self.grp_end = False
            return
        if self.grp:
            if not self.grp_last:
                return
            self.grp = False
            self.grp_last = False
            self.grp_end = True
        ca = param.thisObject
        idx = self._idx(param)
        jms = get_private_field(ca, "selectedMessagesIds")
        sma = jms[idx]
        cm = get_private_field(ca, "chatMode")
        cc = get_private_field(ca, "currentChat")
        ism = cm == 1
        for ri in reversed(range(sma.size())):
            mid = sma.keyAt(ri)
            mo = sma.get(mid)
            if mid in self.msgs:
                try:
                    if not mo.canForwardMessage():
                        v = get_private_field(ca, "cantForwardMessagesCount")
                        set_private_field(ca, "cantForwardMessagesCount", Integer(max(0, int(str(v)) - 2)))
                except Exception:
                    pass
                try:
                    if not mo.canDeleteMessage(ism, cc):
                        v = get_private_field(ca, "cantDeleteMessagesCount")
                        set_private_field(ca, "cantDeleteMessagesCount", Integer(max(0, int(str(v)) - 2)))
                except Exception:
                    pass
                self.msgs.pop(mid)
            else:
                self.msgs[mid] = mo
        sma.clear()
        for k, v in self.msgs.items():
            sma.put(k, v)
        if not self.msgs:
            try:
                if HIDE_ACTION:
                    HIDE_ACTION.invoke(ca)
                if UPD_PINNED:
                    UPD_PINNED.invoke(ca, [True])
            except Exception:
                pass
            return
        try:
            if IS_NEW:
                abl = get_private_field(ca, "actionsButtonsLayout")
                if abl:
                    abl.showReplyButton(len(self.msgs) == 1, True)
            else:
                rb = get_private_field(ca, "replyButton")
                if rb:
                    rb.setVisibility(View.VISIBLE if len(self.msgs) == 1 else View.GONE)
        except Exception:
            pass
    def clear(self):
        self.msgs.clear()

class NFLClearHook(MethodHook):
    def __init__(self, sel):
        super().__init__()
        self.sel = sel
    def before_hooked_method(self, param):
        self.sel.clear()

class NFLMultiHook(MethodHook):
    def __init__(self, plugin):
        super().__init__()
        self.plugin = plugin
    def after_hooked_method(self, param):
        if not self.plugin._gs("no_forward_limit", False):
            return
        try:
            clv = get_private_field(param.thisObject, "chatListView")
            if clv:
                sl = get_private_field(clv, "multiSelectionListener")
                if sl:
                    self.plugin.hook_method(sl.getClass().getMethod("limitReached"), NFLBypassHook())
        except Exception:
            pass

class NFLBypassHook(MethodReplacement):
    def replace_hooked_method(self, param):
        return False

class NFLDeleteHook(XposedHook):
    def before_hooked_method(self, param):
        fa, sa = param.args[0], param.args[1]
        msgs, ai = (fa, 0) if fa.size() > 0 else (sa, 1)
        ml = _al_to_list(msgs)
        if len(ml) <= 100:
            return
        chunks = [ml[i:i+100] for i in range(0, len(ml), 100)]
        for i, chunk in enumerate(chunks):
            param.args[ai] = _to_al(chunk)
            if i < len(chunks) - 1:
                param.method.invoke(param.thisObject, param.args)


MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT


def _create_sticker_view(context, list_view, current_account, class_guid, resources_provider):
    layout = JFrameLayout(context)
    layout.setLayoutParams(ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    layout.setPadding(0, AndroidUtilities.dp(20), 0, AndroidUtilities.dp(20))
    iv = BackupImageView(context)
    iv.setRoundRadius(AndroidUtilities.dp(45))
    layout.addView(iv, LayoutHelper.createFrame(130, 130, Gravity.CENTER))
    return layout

def _bind_sticker_view(view, item, divider, adapter, list_view):
    MediaDataController.getInstance(UserConfig.selectedAccount).setPlaceholderImageByIndex(
        view.getChildAt(0), "MaxExteraPluginsStickers", 30, "130_130")

_StickerFactory = SimpleSettingFactory(_create_sticker_view, _bind_sticker_view, is_clickable=False, is_shadow=False)


def _find_field_in_hierarchy(obj, name):
    if obj is None:
        return None
    try:
        current = obj.getClass()
    except Exception:
        return None
    while current is not None:
        try:
            f = current.getDeclaredField(name)
            f.setAccessible(True)
            return f
        except Exception:
            pass
        try:
            current = current.getSuperclass()
        except Exception:
            current = None
    return None


def _set_field_value(obj, name, value):
    f = _find_field_in_hierarchy(obj, name)
    if f is None:
        return False
    try:
        type_name = str(f.getType().getName())
        if type_name == "int":
            f.setInt(obj, int(value))
        elif type_name == "long":
            f.setLong(obj, int(value))
        elif type_name == "boolean":
            f.setBoolean(obj, bool(value))
        else:
            f.set(obj, value)
        return True
    except Exception:
        return False


def _set_field_with_flag(obj, field_name, value, flag_bit):
    """Set a field and also set the corresponding bit in the flags field (for TL conditional fields)"""
    _set_field_value(obj, field_name, value)
    try:
        flags_field = _find_field_in_hierarchy(obj, "flags")
        if flags_field:
            flags = flags_field.getInt(obj)
            if isinstance(value, bool):
                flags = (flags | flag_bit) if value else (flags & ~flag_bit)
            elif isinstance(value, (int, long)):
                flags = (flags | flag_bit) if value != 0 else (flags & ~flag_bit)
            else:
                flags = (flags | flag_bit) if value is not None else (flags & ~flag_bit)
            flags_field.setInt(obj, flags)
    except Exception:
        pass
    return True


def _get_field_value(obj, name, default=None):
    f = _find_field_in_hierarchy(obj, name)
    if f is None:
        return default
    try:
        return f.get(obj)
    except Exception:
        return default


def _invoke_no_arg(obj, method_name):
    if obj is None:
        return None
    try:
        current = obj.getClass()
    except Exception:
        return None
    while current is not None:
        try:
            methods = current.getDeclaredMethods()
        except Exception:
            methods = []
        for method in methods:
            try:
                if str(method.getName()) != method_name:
                    continue
                try:
                    plen = len(method.getParameterTypes())
                except Exception:
                    try:
                        plen = int(method.getParameterTypes().length)
                    except Exception:
                        continue
                if plen != 0:
                    continue
                method.setAccessible(True)
                return method.invoke(obj)
            except Exception:
                continue
        try:
            current = current.getSuperclass()
        except Exception:
            current = None
    return None



# ============================================================
# MaxExteraPlugins Shared Author & Channel Subscription Module
# ============================================================

# ============================================================
# MaxExteraPlugins Shared Author & Channel Subscription Module
# ============================================================

# ============================================================
# MaxExteraPlugins Shared Author & Channel Subscription Module
# ============================================================
class AuthorChannelHelper:
    CONFIG_URL = "https://raw.githubusercontent.com/MaxExteraPlugins/config/main/links.json"

    DEFAULT_LINKS = {
        "channel_username": "MaxExteraPlugins",
        "channel_url": "https://t.me/MaxExteraPlugins",
        "header": {"ru": "MaxExteraPlugins", "en": "MaxExteraPlugins"},
        "buttons": [
            {"title": {"ru": "Канал автора", "en": "Author's Channel"}, "icon": "msg_channel", "url": "https://t.me/MaxExteraPlugins"},
            {"title": {"ru": "Поддержать автора", "en": "Support author"}, "icon": "msg_share", "url": "https://t.me/itNotMax"},
            {"title": {"ru": "Сообщить о баге", "en": "Report a bug"}, "icon": "msg_help", "url": "https://t.me/itNotMax"}
        ]
    }

    _TEXTS = {
        "install_card_title": {"ru": "Подпишитесь на канал", "en": "Subscribe to channel"},
        "install_card_msg": {
            "ru": (
                "Мы делаем плагины бесплатными и доступными для всех: пожалуйста, "
                "поддержите нас подпиской на официальный канал @MaxExteraPlugins.\n\n"
                "Там публикуются обновления, важные новости и анонсы новых плагинов."
            ),
            "en": (
                "We keep our plugins free and accessible for everyone: please "
                "support us by subscribing to our official channel @MaxExteraPlugins.\n\n"
                "Updates, news, and new plugin announcements are published there."
            )
        },
        "open_channel_btn": {"ru": "Перейти на канал", "en": "Open channel"},
        "remind_later_btn": {"ru": "Напомнить позже", "en": "Remind later"},
        "never_show_btn": {"ru": "Больше не показывать", "en": "Don't show again"},
        "sub_thank_you": {"ru": "Спасибо за подписку на наш канал!", "en": "Thank you for subscribing to our channel!"},
        "settings_channel": {"ru": "Канал автора", "en": "Author's Channel"},
        "settings_donate": {"ru": "Поддержать автора", "en": "Support author"},
        "settings_bug_report": {"ru": "Сообщить о баге", "en": "Report a bug"}
    }

    @classmethod
    def is_ru(cls) -> bool:
        try:
            from org.telegram.messenger import LocaleController
            lang = str(LocaleController.getInstance().getCurrentLocale().getLanguage() or "").lower()
            return lang in ("ru", "be", "uk", "kk")
        except Exception:
            return True

    @classmethod
    def tr(cls, key: str) -> str:
        entry = cls._TEXTS.get(key, {})
        return entry.get("ru" if cls.is_ru() else "en", entry.get("en", key))

    def __init__(self, plugin, icon_sticker=None):
        self.plugin = plugin
        self.icon_sticker = icon_sticker or getattr(plugin, "__icon__", "MaxExteraPluginsStickers/1")
        self._stopped = False
        self._active_dialog = None

    def on_load(self):
        self._stopped = False
        self._active_dialog = None
        self._load_remote_links_async()
        self._check_initial_subscription_flow()

    def on_event(self, event_type):
        try:
            name = str(event_type).upper()
            if name.endswith("START") or name.endswith("RESUME"):
                if self.is_subscribed() and not self.plugin.get_setting("was_subscribed", False):
                    self.plugin.set_setting("was_subscribed", True)
                    self._dismiss_active_dialog()
                    self._notify_subscribed()
        except Exception:
            pass

    def on_unload(self):
        self._stopped = True
        self._dismiss_active_dialog()

    def is_allowed(self) -> bool:
        return True

    def _get_links(self) -> dict:
        raw = self.plugin.get_setting("remote_links_cache", None)
        if raw:
            try:
                import json
                data = json.loads(raw)
                if isinstance(data, dict): return data
            except Exception:
                pass
        return self.DEFAULT_LINKS

    def _load_remote_links_async(self):
        def worker():
            try:
                import urllib.request, json
                req = urllib.request.Request(self.CONFIG_URL, headers={"User-Agent": "MaxExteraPlugins"})
                with urllib.request.urlopen(req, timeout=4) as resp:
                    if resp.status == 200:
                        data = json.loads(resp.read().decode("utf-8"))
                        if isinstance(data, dict):
                            self.plugin.set_setting("remote_links_cache", json.dumps(data))
                            self._resolve_channel_in_telegram(data.get("channel_username", "MaxExteraPlugins"))
            except Exception:
                pass
        import threading
        t = threading.Thread(target=worker, daemon=True)
        t.start()

    def _open_url(self, url: str, view=None):
        try:
            from org.telegram.messenger.browser import Browser
            from org.telegram.messenger import ApplicationLoader
            from client_utils import get_last_fragment
            ctx = None
            if view is not None:
                ctx = view.getContext()
            if ctx is None:
                frag = get_last_fragment()
                if frag is not None:
                    ctx = frag.getParentActivity()
            if ctx is None:
                ctx = ApplicationLoader.applicationContext
            Browser.openUrl(ctx, url)
        except Exception:
            pass

    def _resolve_channel_in_telegram(self, username: str):
        try:
            from org.telegram.messenger import UserConfig, MessagesController
            from org.telegram.tgnet import TLRPC, ConnectionsManager, RequestDelegate
            clean_user = username.lstrip("@")
            acc = int(UserConfig.selectedAccount)
            mc = MessagesController.getInstance(acc)
            if not mc: return
            req = TLRPC.TL_contacts_resolveUsername()
            req.username = clean_user
            helper_ref = self
            class Delegate(RequestDelegate):
                def run(self, response, error):
                    if error is None and response is not None:
                        chats = getattr(response, "chats", None)
                        if chats is not None and chats.size() > 0:
                            for a in range(UserConfig.MAX_ACCOUNT_COUNT):
                                try:
                                    if UserConfig.getInstance(a).isClientActivated():
                                        MessagesController.getInstance(a).putChats(chats, False)
                                except Exception:
                                    pass
                            c = chats.get(0)
                            cid = getattr(c, "id", 0)
                            if cid > 0:
                                helper_ref.plugin.set_setting("resolved_channel_id", int(cid))
            ConnectionsManager.getInstance(acc).sendRequest(req, Delegate())
        except Exception:
            pass

    def is_subscribed(self) -> bool:
        try:
            from org.telegram.messenger import UserConfig, MessagesController, MessagesStorage
            from java.lang import Long as JLong
            links = self._get_links()
            target = str(links.get("channel_username", "MaxExteraPlugins") or "MaxExteraPlugins").lower().lstrip("@")
            saved_cid = int(self.plugin.get_setting("resolved_channel_id", 0) or 0)

            for acc in range(UserConfig.MAX_ACCOUNT_COUNT):
                try:
                    uconf = UserConfig.getInstance(acc)
                    if not uconf.isClientActivated():
                        continue

                    mc = MessagesController.getInstance(acc)
                    storage = MessagesStorage.getInstance(acc)

                    # 1. In-memory check by resolved_channel_id
                    if saved_cid and mc is not None:
                        try:
                            c = mc.getChat(JLong(saved_cid))
                            if c is not None:
                                left = bool(getattr(c, "left", True))
                                kicked = bool(getattr(c, "kicked", False))
                                if not left and not kicked:
                                    return True
                        except Exception:
                            pass

                    # 2. In-memory check by username
                    if mc is not None:
                        try:
                            chat = mc.getUserOrChat(target)
                            if chat is not None:
                                cid = int(getattr(chat, "id", 0) or 0)
                                if cid and not saved_cid:
                                    saved_cid = cid
                                    self.plugin.set_setting("resolved_channel_id", cid)
                                left = bool(getattr(chat, "left", True))
                                kicked = bool(getattr(chat, "kicked", False))
                                if not left and not kicked:
                                    return True
                        except Exception:
                            pass

                    # 3. In-memory dialogs check
                    if mc is not None:
                        try:
                            dialog_lists = []
                            d_chan = getattr(mc, "dialogsChannelsOnly", None)
                            if d_chan is not None and d_chan.size() > 0:
                                dialog_lists.append(d_chan)
                            d_all = getattr(mc, "allDialogs", None)
                            if d_all is not None and d_all.size() > 0:
                                dialog_lists.append(d_all)

                            for d_list in dialog_lists:
                                limit = min(d_list.size(), 300)
                                for i in range(limit):
                                    d = d_list.get(i)
                                    did = getattr(d, "id", 0)
                                    if did < 0:
                                        cid = -did
                                        if saved_cid and cid == saved_cid:
                                            c = mc.getChat(JLong(cid))
                                            if c is not None:
                                                left = bool(getattr(c, "left", True))
                                                kicked = bool(getattr(c, "kicked", False))
                                                if not left and not kicked:
                                                    return True
                                        else:
                                            c = mc.getChat(JLong(cid))
                                            if c is not None:
                                                uname = str(getattr(c, "username", "") or "").lower()
                                                if uname == target:
                                                    if not saved_cid:
                                                        saved_cid = cid
                                                        self.plugin.set_setting("resolved_channel_id", cid)
                                                    left = bool(getattr(c, "left", True))
                                                    kicked = bool(getattr(c, "kicked", False))
                                                    if not left and not kicked:
                                                        return True
                        except Exception:
                            pass

                    # 4. Storage check (SQLite on disk)
                    if storage is not None:
                        if saved_cid:
                            try:
                                if storage.containsLocalDialog(JLong(-saved_cid)):
                                    c_sync = storage.getChatSync(JLong(saved_cid))
                                    if c_sync is not None:
                                        left = bool(getattr(c_sync, "left", True))
                                        kicked = bool(getattr(c_sync, "kicked", False))
                                        if not left and not kicked:
                                            return True
                                    else:
                                        return True
                            except Exception:
                                pass

                        try:
                            db = storage.getDatabase()
                            if db is not None:
                                if saved_cid:
                                    try:
                                        cur = db.queryFinalized("SELECT did FROM dialogs WHERE did = ?", [JLong(-saved_cid)])
                                        if cur is not None:
                                            has_row = bool(cur.next())
                                            cur.dispose()
                                            if has_row:
                                                return True
                                    except Exception:
                                        pass

                                try:
                                    cur = db.queryFinalized("SELECT uid, name FROM chats WHERE LOWER(name) LIKE ?", ["%" + target + "%"])
                                    if cur is not None:
                                        while cur.next():
                                            c_id = cur.longValue(0)
                                            if c_id > 0:
                                                if not saved_cid:
                                                    saved_cid = int(c_id)
                                                    self.plugin.set_setting("resolved_channel_id", saved_cid)
                                                cur_d = db.queryFinalized("SELECT did FROM dialogs WHERE did = ?", [JLong(-c_id)])
                                                if cur_d is not None:
                                                    in_dlg = bool(cur_d.next())
                                                    cur_d.dispose()
                                                    if in_dlg:
                                                        cur.dispose()
                                                        return True
                                        cur.dispose()
                                except Exception:
                                    pass
                        except Exception:
                            pass
                except Exception:
                    pass
        except Exception:
            pass
        return False

    def prepend_settings(self, original_items=None) -> list:
        from ui.settings import Header, Text, Divider

        if original_items and any(getattr(x, "text", "") == "MaxExteraPlugins" for x in original_items if type(x).__name__ == "Header"):
            return original_items

        links = self._get_links()
        buttons_config = links.get("buttons")

        author_items = []
        if isinstance(buttons_config, list):
            for btn in buttons_config:
                if not isinstance(btn, dict): continue
                raw_url = btn.get("url")
                if isinstance(raw_url, dict):
                    keys = [
                        getattr(self.plugin, "id", None),
                        globals().get("__id__", None),
                        globals().get("__name__", None),
                        self.plugin.__class__.__name__
                    ]
                    keys += [k.lower() for k in keys if isinstance(k, str)]
                    resolved_url = ""
                    for k in keys:
                        if k and k in raw_url:
                            resolved_url = str(raw_url[k] or "").strip()
                            if resolved_url:
                                break
                    url = resolved_url
                elif isinstance(raw_url, str):
                    url = raw_url.strip()
                else:
                    url = ""

                if not url: continue

                title_data = btn.get("title")
                if isinstance(title_data, dict):
                    title = title_data.get("ru" if self.is_ru() else "en") or title_data.get("en") or title_data.get("ru") or ""
                elif isinstance(title_data, str) and title_data:
                    title = title_data
                else:
                    title = btn.get("title_ru" if self.is_ru() else "title_en") or btn.get("title_ru") or btn.get("title_en") or ""

                if not title: continue
                icon = str(btn.get("icon") or "msg_channel").strip()

                def make_click(u):
                    return lambda v=None: self._open_url(u, v)

                author_items.append(Text(text=title, icon=icon, on_click=make_click(url)))
        else:
            for k, text_key, icon_name in [
                ("channel_url", "settings_channel", "msg_channel"),
                ("donate_url", "settings_donate", "msg_share"),
                ("bug_report_url", "settings_bug_report", "msg_help")
            ]:
                u = str(links.get(k) or "").strip()
                if u:
                    author_items.append(Text(text=self.tr(text_key), icon=icon_name, on_click=lambda v=None, target_u=u: self._open_url(target_u, v)))

        res = []
        if author_items:
            hdr = links.get("header") or "MaxExteraPlugins"
            if isinstance(hdr, dict):
                hdr_text = hdr.get("ru" if self.is_ru() else "en") or hdr.get("ru") or hdr.get("en") or "MaxExteraPlugins"
            else:
                hdr_text = str(hdr)
            res.append(Header(text=hdr_text))
            res.extend(author_items)
            res.append(Divider())

        cleaned_items = []
        if original_items:
            skipping_info = False
            for item in original_items:
                item_cls = type(item).__name__
                if item_cls == "Header":
                    txt = str(getattr(item, "text", "") or getattr(item, "title", "") or "").lower()
                    if "информ" in txt or "inform" in txt:
                        skipping_info = True
                        continue
                    else:
                        skipping_info = False
                elif skipping_info and item_cls == "Divider":
                    skipping_info = False
                    continue

                if not skipping_info:
                    cleaned_items.append(item)

        res.extend(cleaned_items)
        return res

    def _dismiss_active_dialog(self):
        try:
            if self._active_dialog is not None:
                self._active_dialog.dismiss()
        except Exception:
            pass
        self._active_dialog = None

    def _is_dialog_active(self) -> bool:
        try:
            if self._active_dialog is not None:
                return bool(self._active_dialog.isShowing())
        except Exception:
            pass
        return False

    def _notify_subscribed(self):
        try:
            from ui.bulletin import BulletinHelper
            BulletinHelper.show_success(self.tr("sub_thank_you"))
        except Exception:
            pass

    def _check_initial_subscription_flow(self):
        def flow():
            try:
                if self.plugin.get_setting("never_show_sub_prompt", False):
                    return

                current_ver = str(getattr(self.plugin, "__version__", globals().get("__version__", "1.0.0")) or "1.0.0")
                last_prompted_ver = self.plugin.get_setting("last_prompted_version", None)

                # Only show on install (last_prompted_ver is None) or update (last_prompted_ver != current_ver)
                if last_prompted_ver is not None and str(last_prompted_ver) == current_ver:
                    return

                if self.is_subscribed():
                    self.plugin.set_setting("last_prompted_version", current_ver)
                    self.plugin.set_setting("was_subscribed", True)
                    return

                self._show_subscription_prompt(current_ver)
            except Exception:
                pass
        from android_utils import run_on_ui_thread
        run_on_ui_thread(flow, 1500)

    def _create_card_content(self, act, title_text, message_text):
        try:
            from android.widget import LinearLayout, TextView, ImageView
            from android.view import Gravity
            from org.telegram.messenger import AndroidUtilities
            from org.telegram.ui.ActionBar import Theme
            from client_utils import create_sticker_drawable

            dp = AndroidUtilities.dp

            layout = LinearLayout(act)
            layout.setOrientation(LinearLayout.VERTICAL)
            layout.setGravity(Gravity.CENTER_HORIZONTAL)
            layout.setPadding(dp(24), dp(20), dp(24), dp(10))

            sticker_drawable = None
            if self.icon_sticker:
                try:
                    sticker_drawable = create_sticker_drawable(self.icon_sticker, dp(120), dp(120))
                except Exception:
                    pass

            if sticker_drawable is not None:
                iv = ImageView(act)
                iv.setImageDrawable(sticker_drawable)
                lp_iv = LinearLayout.LayoutParams(dp(120), dp(120))
                lp_iv.gravity = Gravity.CENTER_HORIZONTAL
                lp_iv.bottomMargin = dp(14)
                layout.addView(iv, lp_iv)

            if title_text:
                tt = TextView(act)
                tt.setText(title_text)
                tt.setTextSize(19.0)
                tt.setTypeface(AndroidUtilities.bold())
                tt.setTextColor(Theme.getColor(Theme.key_dialogTextBlack))
                tt.setGravity(Gravity.CENTER_HORIZONTAL)
                lp_t = LinearLayout.LayoutParams(-1, -2)
                lp_t.bottomMargin = dp(8)
                layout.addView(tt, lp_t)

            if message_text:
                mt = TextView(act)
                mt.setText(message_text)
                mt.setTextSize(15.0)
                mt.setTextColor(Theme.getColor(Theme.key_dialogTextBlack))
                mt.setLineSpacing(float(dp(3)), 1.0)
                mt.setGravity(Gravity.CENTER_HORIZONTAL)
                lp_m = LinearLayout.LayoutParams(-1, -2)
                mt.setLayoutParams(lp_m)
                layout.addView(mt)

            return layout
        except Exception:
            return None

    def _show_subscription_prompt(self, current_ver: str):
        def show():
            try:
                if self._is_dialog_active(): return
                from client_utils import get_last_fragment
                frag = get_last_fragment()
                if not frag: return
                act = frag.getParentActivity()
                if not act: return

                links = self._get_links()
                url = links.get("channel_url", "https://t.me/MaxExteraPlugins")

                from ui.alert import AlertDialogBuilder
                b = AlertDialogBuilder(act)
                content = self._create_card_content(act, self.tr("install_card_title"), self.tr("install_card_msg"))
                if content is not None:
                    b.set_view(content)
                else:
                    b.set_title(self.tr("install_card_title"))
                    b.set_message(self.tr("install_card_msg"))

                def on_open_channel(dialog, which):
                    self._dismiss_active_dialog()
                    self.plugin.set_setting("last_prompted_version", current_ver)
                    self._open_url(url)

                def on_remind_later(dialog, which):
                    self._dismiss_active_dialog()
                    self.plugin.set_setting("last_prompted_version", current_ver)

                def on_never_show(dialog, which):
                    self._dismiss_active_dialog()
                    self.plugin.set_setting("never_show_sub_prompt", True)
                    self.plugin.set_setting("last_prompted_version", current_ver)

                b.set_positive_button(self.tr("open_channel_btn"), on_open_channel)
                b.set_neutral_button(self.tr("remind_later_btn"), on_remind_later)
                b.set_negative_button(self.tr("never_show_btn"), on_never_show)

                def on_dismiss(builder):
                    self.plugin.set_setting("last_prompted_version", current_ver)
                try:
                    b.set_on_dismiss_listener(on_dismiss)
                except Exception:
                    pass

                self._active_dialog = b.show()
                self.plugin.set_setting("last_prompted_version", current_ver)
            except Exception:
                pass
        from android_utils import run_on_ui_thread
        run_on_ui_thread(show)

class MaxReExteraPlugin(BasePlugin):
    def __init__(self):
        super().__init__()
        self.dex = Dex(ENTRY_CLASS)
        self.vault = None
        self._filters = []
        self._cpat = []
        self._unhooks = []
        self.nfl_sel = None
        self._presence_tracked = set()
        self._presence_lamp = {}
        self._presence_records = {}
        self._presence_last_seen = {}
        self._presence_open = {}
        self._presence_dirty = False
        self._presence_lock = threading.Lock()
        self._fp_cache = {}
        self._fp_dirty = False
        self._stop_event = None
        self._pending_read_dialogs = {}  # did -> max_id
        self._pending_read_content_mids = set()  # mids deliberately marked via "Mark read"
        self._ctx_msg = None
        self._my_deleting = set()
        self._my_del_lock = threading.Lock()
        self._vault_purging = set()
        self._vault_purging_mids = set()
        self._vault_icon = None
        self._f_ts = None
        self._f_tw = None
        self._f_w = None
        self._cmc_fields_cached = False
        self._cc_cls = None
        # Ghost-режим для сторис (перенесено из elyx): флаг чисто в памяти и влияет
        # только на story-read запросы; реальная настройка ghost_mode не трогается.
        self._story_ghost_override = False
        self._story_viewer_open = False
        self._story_bypass = False
        # Подмена собственного статуса (profile): блок нативного setText + кэш
        self._own_status_block = False
        self._own_status_views = set()
        self._own_status_last_req = 0.0
        self._own_status_last_text = None
        self._last_offline_push = 0
        self._forward_copying = False
        self._ad_field_cache = {}
        self._pending_send_cancel = set()
        self._pending_send_lock = threading.Lock()
        self._decorated_cells = {}
        self._read_state_cache = {}
        self._sc = {}
        self._sc_ts = 0
        self._my_uids = {}
        self._my_uids_ts = 0
        self._keep_alive_active = False
        self._wakelock = None
        self._stamp_icon_extra = 0
        self._stamp_icon_extra_ready = False
        self._removed_dids = set()
        self._read_whitelist = set()

    _prefs_instance = None
    def _prefs(self):
        if MaxReExteraPlugin._prefs_instance is None:
            MaxReExteraPlugin._prefs_instance = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0)
        return MaxReExteraPlugin._prefs_instance

    def _is_ghost(self):
        return self._gs("ghost_mode", False)

    def _fmode(self, key, default=1):
        # Режим функции призрака: 0 = всегда выкл, 1 = только с призраком, 2 = всегда вкл
        # (для ghost_no_read_stories дополнительно 3 = спрашивать при открытии).
        try:
            return int(self._gs(key, default))
        except Exception:
            return default

    def _factive(self, key):
        mode = self._fmode(key)
        return mode == 2 or (mode == 1 and self._is_ghost())

    def _silent_active(self):
        mode = self._fmode("silent_send", 0)
        return mode == 1 or (mode == 2 and self._is_ghost())

    def _chk(self, key):
        return self._factive(key)

    def _gs(self, key, default=None):
        sc = self._sc
        v = sc.get(key)
        if v is not None:
            return v
        if len(sc) > 50:
            sc.clear()
        for tab, keys in _TAB_GATE.items():
            if key in keys:
                if not self.get_setting(tab, True):
                    sc[key] = False
                    return False
                break
        v = self.get_setting(key, default)
        sc[key] = v
        return v

    def _gs_flush(self):
        self._sc.clear()

    def _get_my_uid(self, acc=None):
        if acc is None:
            acc = int(UserConfig.selectedAccount)
        now = _time.monotonic() if hasattr(_time, 'monotonic') else _time.time()
        if now - self._my_uids_ts > 10.0:
            self._my_uids.clear()
            self._my_uids_ts = now
        uid = self._my_uids.get(acc)
        if uid is not None:
            return uid
        try:
            uid = int(UserConfig.getInstance(acc).getClientUserId())
        except Exception:
            uid = 0
        self._my_uids[acc] = uid
        return uid

    def _is_removed_fast(self, did, mid):
        if not self.vault:
            return False
        return (int(did), int(mid)) in self.vault._removed

    def _invalidate_removed_cache(self):
        self._decorated_cells.clear()

    def _save_read_state(self, did, mid, mo):
        try:
            owner = mo.messageOwner if hasattr(mo, "messageOwner") else mo
            if not owner:
                return
            unread = bool(getattr(owner, "unread", False))
            k = (int(did), int(mid))
            self._read_state_cache[k] = unread
        except Exception:
            pass

    def _save_read_states_bulk(self, did, mids):
        try:
            frag = LaunchActivity.getLastFragment()
            if not isinstance(frag, ChatActivity):
                return
            msgs_field = _ff(CA_CLS, "messages")
            if not msgs_field:
                return
            msgs = msgs_field.get(frag)
            if not msgs:
                return
            mid_set = set(int(m) for m in mids)
            for i in range(msgs.size()):
                mo = msgs.get(i)
                if mo and int(mo.getId()) in mid_set:
                    self._save_read_state(did, mo.getId(), mo)
        except Exception:
            pass

    def _restore_read_state(self, mo, did, mid):
        try:
            k = (int(did), int(mid))
            if k not in self._read_state_cache:
                return
            saved_unread = self._read_state_cache[k]
            owner = mo.messageOwner
            if owner and bool(getattr(owner, "out", False)):
                owner.unread = saved_unread
        except Exception:
            pass

    def _mark_my_deletion(self, mids):
        if not mids or not self._gs("track_own_deletions", False):
            return
        with self._my_del_lock:
            for m in mids:
                self._my_deleting.add(int(m))
        def cleanup():
            _time.sleep(3)
            with self._my_del_lock:
                for m in mids:
                    self._my_deleting.discard(int(m))
        threading.Thread(target=cleanup, daemon=True).start()

    def _is_my_deletion(self, mid):
        if not self._gs("track_own_deletions", False):
            return False
        with self._my_del_lock:
            return int(mid) in self._my_deleting

    def _resolve_did(self, mids, account=None):
        if account is None:
            account = UserConfig.selectedAccount
        try:
            frag = LaunchActivity.getLastFragment()
            if isinstance(frag, ChatActivity):
                f = _ff(CA_CLS, "dialog_id")
                if f:
                    v = int(f.get(frag))
                    if v:
                        return v
        except Exception:
            pass
        try:
            ctrl = MessagesController.getInstance(account)
            for mid in mids:
                obj = ctrl.dialogMessagesByIds.get(Integer(mid))
                if obj:
                    v = int(obj.getDialogId())
                    if v:
                        return v
        except Exception:
            pass
        return 0

    def _refresh_cells(self):
        if self.dex and self.dex.loaded:
            try:
                self.dex.call("refreshCells")
                return
            except Exception:
                pass

    def _stamp_cell(self, cell, mo):
        pass

    def _stamp_cell_legacy(self, cell, mo):
        if not self.vault or not self.vault._removed or not mo:
            return
        try:
            did = mo.getDialogId()
            mid = mo.getId()
            if (int(did), int(mid)) not in self.vault._removed:
                return
            if not self._ensure_cell_fields():
                return
            cur = self._f_ts.get(cell)
            if not cur:
                return
            show_opacity = self._gs("del_show_opacity", True)
            try:
                c0 = cur.charAt(0)
                if c0 == "\u2009" or c0 == "\u2022":
                    if show_opacity:
                        cell.setAlpha(jfloat(self._get_opacity_val()))
                    return
            except Exception:
                pass
            paint = Theme.chat_timePaint
            if not paint:
                return
            use_icon = self._gs("del_use_icon", True)
            if use_icon and self._vault_icon:
                sb = SpannableStringBuilder("\u2009")
                span = ColoredImageSpan(self._vault_icon)
                try:
                    span.setOverrideColor(cell.getThemedColor(Theme.key_color_red))
                except Exception:
                    span.setOverrideColor(-44462)
                span.setRelativeSize(paint.getFontMetricsInt())
                sb.setSpan(span, 0, 1, 0)
                sb.append(" ")
                if not self._stamp_icon_extra_ready:
                    fm = paint.getFontMetricsInt()
                    self._stamp_icon_extra = abs(int(fm.descent) - int(fm.ascent)) + int(Math.ceil(paint.measureText(" ")))
                    self._stamp_icon_extra_ready = True
                extra = self._stamp_icon_extra
            else:
                custom = self._gs("del_custom_label", "") or ""
                label = custom if custom else L.g("msg_deleted")
                try:
                    cs = str(cur)
                    if cs.startswith(label):
                        if show_opacity:
                            cell.setAlpha(jfloat(self._get_opacity_val()))
                        return
                except Exception:
                    pass
                sb = SpannableStringBuilder(label)
                try:
                    sb.setSpan(ForegroundColorSpan(cell.getThemedColor(Theme.key_color_red)), 0, sb.length(), 33)
                except Exception:
                    sb.setSpan(ForegroundColorSpan(-44462), 0, sb.length(), 33)
                sb.append(" ")
                extra = int(Math.ceil(paint.measureText(sb, 0, sb.length())))
            result = SpannableStringBuilder(sb)
            result.append(cur)
            self._f_ts.set(cell, result)
            self._f_tw.set(cell, Integer(int(str(self._f_tw.get(cell))) + extra))
            self._f_w.set(cell, Integer(int(str(self._f_w.get(cell))) + extra))
            if show_opacity:
                cell.setAlpha(jfloat(self._get_opacity_val()))
        except Exception:
            pass

    def _ensure_cell_fields(self):
        if self._cmc_fields_cached:
            return self._f_ts is not None
        self._cmc_fields_cached = True
        try:
            CLC = find_class("org.telegram.ui.Cells.ChatMessageCell")
            self._f_ts = _ff(CLC.getClass(), "currentTimeString")
            self._f_tw = _ff(CLC.getClass(), "timeTextWidth")
            self._f_w = _ff(CLC.getClass(), "timeWidth")
        except Exception:
            pass
        return self._f_ts is not None



    def _send_read_content_for_message(self, did, mid):
        # Bug fix (#9): "Mark read" on one specific message used to call
        # _send_read_for_dialog(), which sends messages.readHistory/channels.readHistory
        # with max_id=mid. Per Telegram's protocol that's a *cumulative* read - it marks
        # every message up to and including mid as read, i.e. it silently read-marked
        # the whole visible chat instead of just the one message the user tapped.
        # messages.readMessageContents / channels.readMessageContents take an explicit
        # list of message ids and mark only those as read, without touching history.
        try:
            did = int(did)
            mid = int(mid)
            acc = int(UserConfig.selectedAccount)
            mc = MessagesController.getInstance(acc)
            # Deliberate, user-initiated "Mark read" must go through even while ghost
            # mode is suppressing reads in general (same idea as the bypass the old
            # _send_read_for_dialog()/_pending_read_dialogs mechanism gave readHistory
            # calls) - otherwise this request would just get cancelled by the
            # READ_REQS/ghost_no_read block further down in pre_request_hook.
            self._pending_read_content_mids.add(mid)
            is_channel = False
            if did < 0:
                try:
                    chat = mc.getChat(jlong(-did))
                    if chat and ChatObject.isChannel(chat):
                        is_channel = True
                except Exception:
                    pass
            def _cb(resp, err):
                if err:
                    log(f"[MAX] sendReadContent err: {err}")
            ids = ArrayList()
            ids.add(jint(mid))
            if is_channel:
                rq = TLRPC.TL_channels_readMessageContents()
                rq.channel = MessagesController.getInputChannel(mc.getChat(jlong(-did)))
                rq.id = ids
            else:
                rq = TLRPC.TL_messages_readMessageContents()
                rq.id = ids
            send_request(rq, RequestCallback(_cb))
        except Exception as e:
            log(f"[MAX] sendReadContent: {e}")

    def _send_read_for_dialog(self, did, max_id=0):
        try:
            did = int(did)
            mid = int(max_id) if max_id else 0
            acc = int(UserConfig.selectedAccount)
            mc = MessagesController.getInstance(acc)
            if mid <= 0:
                try:
                    f = get_last_fragment()
                    if f and str(f.getClass().getSimpleName()) == "ChatActivity":
                        msgs = f.messages
                        if msgs and msgs.size() > 0:
                            mid = int(msgs.get(0).getId())
                except Exception:
                    pass
            if mid <= 0:
                mid = 999999999
            is_channel = False
            if did < 0:
                try:
                    chat = mc.getChat(jlong(-did))
                    if chat and ChatObject.isChannel(chat):
                        is_channel = True
                except Exception:
                    pass
            self._pending_read_dialogs[did] = mid
            def _cb(resp, err):
                if err:
                    log(f"[MAX] sendRead err: {err}")
            if is_channel:
                rq = TLRPC.TL_channels_readHistory()
                rq.channel = MessagesController.getInputChannel(mc.getChat(jlong(-did)))
                rq.max_id = mid
                send_request(rq, RequestCallback(_cb))
            else:
                rq = TLRPC.TL_messages_readHistory()
                rq.peer = mc.getInputPeer(jlong(did))
                rq.max_id = mid
                send_request(rq, RequestCallback(_cb))
        except Exception as e:
            log(f"[MAX] sendRead: {e}")

    def _mark_local_read(self, dialog_id, max_id):
        try:
            acc = int(UserConfig.selectedAccount)
            mc = MessagesController.getInstance(acc)
            dialogs = mc.dialogs_dict
            if dialogs:
                try:
                    d = dialogs.get(jlong(int(dialog_id)))
                except Exception:
                    d = dialogs.get(int(dialog_id))
                if d:
                    d.unread_count = 0
            try:
                nc = NotificationCenter.getInstance(acc)
                nc.postNotificationName(NotificationCenter.updateInterfaces, Integer(256))
            except Exception:
                pass
        except Exception as e:
            log(f"[MAX] markLocalRead: {e}")

    def sendOffline(self):
        try:
            req = TL_account.updateStatus()
            req.offline = True
            send_request(req, lambda r, e: None)
            self._last_offline_push = _time.time()
        except Exception:
            pass

    def _ensure_offline(self):
        if not self._is_ghost() or not self.get_setting("ghost_no_online", True):
            return
        self.sendOffline()

    def _offline_burst(self):
        plugin = self
        def worker():
            previous = 0.0
            for target in [0.0, 0.3, 1.0, 2.5, 5.0, 10.0]:
                delay = target - previous
                previous = target
                if delay > 0 and plugin._stop_event and plugin._stop_event.wait(delay):
                    return
                if not plugin._is_ghost():
                    return
                plugin.sendOffline()
        threading.Thread(target=worker, daemon=True).start()

    def _toggle_ghost(self, ctx=None):
        cur = self.get_setting("ghost_mode", False)
        nv = not cur
        self.set_setting("ghost_mode", nv)
        self._sc.clear()
        if nv:
            self.sendOffline()
        BulletinHelper.show_info(L.g("ghost_on") if nv else L.g("ghost_off"))
        self._update_ghost_drawer_btn()

    def _update_ghost_drawer_btn(self):
        try:
            self.remove_menu_item("ghost_drawer")
        except Exception:
            pass
        if self._gs("drawer_ghost_btn", True):
            self._register_ghost_drawer_btn()

    def _register_ghost_drawer_btn(self):
        is_on = self.get_setting("ghost_mode", False)
        label = L.g("ghost_btn_on") if is_on else L.g("ghost_btn_off")
        self.add_menu_item(MenuItemData(menu_type=MenuItemType.DRAWER_MENU, text=label, icon="msg_secret", item_id="ghost_drawer", priority=100, on_click=self._toggle_ghost))

    def _enable_ghost_for_stories(self):
        # Ghost только для сторис (из elyx): флаг в памяти, реальная настройка
        # ghost_mode не меняется; на story-read запросы действует через
        # _story_ghost_override в pre_request_hook.
        self._story_ghost_override = True
        if not self._is_ghost():
            self.sendOffline()

    def _disable_ghost_for_stories(self):
        self._story_ghost_override = False

    def _disable_ghost_after_stories(self):
        self._story_viewer_open = False
        self._story_ghost_override = False

    def _do_open_story(self, method, obj, args, with_ghost):
        plugin = self
        def _run():
            plugin._story_viewer_open = True
            plugin._story_bypass = True
            if with_ghost:
                plugin._story_ghost_override = True
                if not plugin._is_ghost():
                    plugin.sendOffline()
            else:
                plugin._story_ghost_override = False
            try:
                ptypes = method.getParameterTypes()
                fixed = []
                for i, a in enumerate(args):
                    if i < len(ptypes):
                        pname = str(ptypes[i].getName())
                        if pname == "int":
                            fixed.append(jint(int(a)))
                        elif pname == "long":
                            fixed.append(jlong(int(a)))
                        elif pname == "boolean":
                            fixed.append(bool(a))
                        elif pname == "float":
                            fixed.append(jfloat(float(a)))
                        else:
                            fixed.append(a)
                    else:
                        fixed.append(a)
                method.invoke(obj, *fixed)
            except Exception as e:
                log(f"[MAX] openStory: {e}")
            plugin._story_bypass = False
        run_on_ui_thread(_run)

    def set_setting(self, key, value, reload_settings=None):
        try:
            super().set_setting(key, value, reload_settings=bool(reload_settings))
        except Exception as exc:
            if "reload_settings" in str(exc):
                try:
                    super().set_setting(key, value)
                except Exception:
                    pass
            try:
                if isinstance(value, bool):
                    self._prefs().edit().putBoolean(key, value).apply()
                else:
                    self._prefs().edit().putString(key, str(value)).apply()
            except Exception:
                pass
        try:
            self._sc.pop(key, None)
            if key in self._JAVA_SYNCED_KEYS:
                self._sync_settings_to_java()
        except Exception:
            pass

    def _register_attach_hooks(self):
        for u in ("TL_updateEditMessage", "TL_updateEditChannelMessage", "TL_updateNewMessage", "TL_updateNewChannelMessage"):
            try:
                self.add_hook(u)
            except Exception as e:
                log(f"[MAX] attach hook {u} failed: {e}")

    def _on_save_attachments_change(self, v):
        val = bool(v)
        self.set_setting("save_attachments", val)
        self._sc.clear()
        if val:
            self._register_attach_hooks()
        if self.dex and self.dex.loaded:
            self.dex.call("pySetBool", JString("save_attachments"), val)
            self.dex.call("pyFlushConfig")

    def _register_presence_hooks(self):
        # presence-апдейты — самая частая категория (печатает/статусы во всех
        # чатах), поэтому вешаем их только когда трекер реально включён.
        for u in ("TL_updateUserStatus", "TL_updateUserTyping", "TL_updateChatUserTyping",
                  "TL_updateChannelUserTyping", "TL_updateReadHistoryOutbox"):
            try:
                self.add_hook(u)
            except Exception as e:
                log(f"[MAX] presence hook {u} failed: {e}")

    def _on_save_last_online_change(self, v):
        val = bool(v)
        self.set_setting("save_last_online", val)
        self.set_setting("presence_track", val)
        self._sc.clear()
        if val:
            self._register_presence_hooks()
            self._start_presence_loop()
        else:
            self._stop_presence_loop()
        if self.dex and self.dex.loaded:
            self.dex.call("pySetBool", JString("save_last_online"), val)
            self.dex.call("pySetBool", JString("presence_track"), val)
            self.dex.call("pyFlushConfig")

    def _on_presence_track_change(self, v):
        self._on_save_last_online_change(v)

    def on_plugin_load(self):
        self._author_helper = AuthorChannelHelper(self, icon_sticker=globals().get("__icon__", "MaxExteraPluginsStickers/1"))
        self._author_helper.on_load()
        # --- Bug #5 fix: sync guard against double initialization ---
        if getattr(self, "_load_started", False):
            log("[MAX] on_plugin_load: already started, skipping")
            return
        self._load_started = True
        # -----------------------------------------------------------
        if self._unhooks:
            return
        self._stop_event = threading.Event()
        # Стартовые издержки: каждый зарегистрированный запрос/апдейт = JNI-вызов
        # в python на каждое событие. Регистрируем только то, что реально нужно:
        # DELETE_REQS и update-delete апдейты больше не обрабатываются python-ом
        # (удаления захватывает java-сторона), presence-апдейты регистрируются
        # только при включённом трекере (иначе они самая частая категория).
        for h in TYPING_REQS | STORIES_REQS | ONLINE_REQS | BLOCKED_AD_REQS | {"updateStatus", "TL_account_updateStatus", "TL_messages_sendReaction", "TL_messages_forwardMessages"}:
            try:
                self.add_hook(h)
            except Exception as e:
                log(f"[MAX] on_plugin_load: hook {h} failed: {e}")
        if self._gs("save_attachments", False):
            self._register_attach_hooks()
        # presence-апдейты регистрируются в _heavy_init только если есть
        # отслеживаемые пользователи в трекере (иначе хватает Java-слоя).
        self.add_on_send_message_hook()
        self.add_menu_item(MenuItemData(menu_type=MenuItemType.MESSAGE_CONTEXT_MENU, text=L.g("edit_history"), icon="msg_edit", on_click=self._on_eh_click))
        if self._gs("drawer_ghost_btn", True):
            self._register_ghost_drawer_btn()
        threading.Thread(target=self._heavy_init, daemon=True, name="MAX-init").start()

    _JAVA_SYNCED_KEYS = [
        "track_own_deletions", "no_forward_limit", "save_deleted", "save_edits",
        "save_attachments", "save_attach_private", "save_attach_groups", "save_attach_channels", "hide_ads", "fake_premium", "keep_once",
        "allow_screenshots", "bypass_noforward", "bypass_story_save",
        "ghost_mode", "ghost_no_online", "ghost_no_typing", "ghost_no_read",
        "ghost_no_read_stories", "ghost_immediate_offline", "read_on_action", "use_scheduled",
        "story_when_ghost_on", "story_when_ghost_off", "keep_app_alive", "del_show_mark", "del_use_icon",
        "del_show_opacity", "filters_enabled", "presence_track", "save_last_online",
        "presence_only_hidden", "fwd_mode",
        "silent_send", "del_opacity_level", "presence_decay",
        # Bug fix (#14): these two were missing here, so _enforce_folder_limit() and
        # _enforce_time_limit() (which read them via raw SharedPreferences on the
        # Java/native side) always saw the default fallback values, never whatever
        # the user actually picked in the settings UI.
        "max_folder_size", "auto_cleanup_age",
    ]

    def _sync_settings_to_java(self):
        try:
            editor = self._prefs().edit()
            for key in self._JAVA_SYNCED_KEYS:
                v = self.get_setting(key, None)
                if v is None:
                    continue
                if isinstance(v, bool):
                    editor.putBoolean(key, v)
                elif isinstance(v, int):
                    editor.putInt(key, v)
            editor.apply()
            if self.dex and self.dex.loaded:
                self.dex.call("pyFlushConfig")
            elif self.dex:
                # Schedule retry after dex loads
                def _retry_flush():
                    for _ in range(50):  # up to 5 seconds
                        _time.sleep(0.1)
                        if self.dex and self.dex.loaded:
                            try:
                                self.dex.call("pyFlushConfig")
                            except Exception:
                                pass
                            break
                threading.Thread(target=_retry_flush, daemon=True).start()
        except Exception:
            pass

    def _sync_settings_entry(self):
        try:
            v = self.get_setting("show_in_settings", True)
            self._prefs().edit().putBoolean("show_in_settings", bool(v)).apply()
        except Exception:
            pass

    # Bug #7: Show restart warning when "Show in settings" is toggled
    def _on_show_in_settings_change(self, value):
        try:
            self._sync_settings_entry()
            if value:
                BulletinHelper.show_info(L.g("show_in_settings_restart"))
        except Exception:
            pass

    def _heavy_init(self):
        try:
            self.dex.load()
        except Exception as e:
            log(f"[MAX] dex: {e}")
        global _ACTIVE_DEX
        _ACTIVE_DEX = self.dex
        self._sync_settings_to_java()
        self._sync_settings_entry()
        self.vault = Vault()
        self._load_filters()
        self._load_presence()
        if (self._gs("save_last_online", False) or self._gs("presence_track", False)) and self._presence_tracked:
            self._register_presence_hooks()
        self._load_read_whitelist()
        self._load_fp()
        try:
            from org.telegram.messenger import R as TGR
            self._vault_icon = ContextCompat.getDrawable(ApplicationLoader.applicationContext, TGR.drawable.msg_delete_filled)
        except Exception:
            pass
        try:
            _get_attach_dir()
        except Exception:
            pass
        def _ui_hooks():
            self._setup_hooks()
            if self._gs("hide_ads", True):
                self._hook_ad()
            if self._is_ghost():
                self._offline_burst()
        threading.Thread(target=_ui_hooks, daemon=True, name="MAX-hooks").start()
        threading.Thread(target=self._loop, daemon=True).start()

    def on_app_event(self, event_type):
        if hasattr(self, "_author_helper"):
            self._author_helper.on_event(event_type)
        name = str(event_type).upper()
        if name.endswith("START") or name.endswith("RESUME"):
            self._gs_flush()
            if self._is_ghost():
                self.sendOffline()
                self._offline_burst()
            if (self._gs("save_last_online", False) or self._gs("presence_track", False)) and self._presence_last_seen:
                run_on_ui_thread(self._restore_presence_statuses, 1500)

    def on_plugin_unload(self):
        if hasattr(self, "_author_helper"):
            self._author_helper.on_unload()
        if self._stop_event:
            self._stop_event.set()
        if self._gs("fake_premium", False):
            self._revert_fake_premium()
        self.dex.unload()
        global _ACTIVE_DEX
        _ACTIVE_DEX = None
        for u in self._unhooks:
            try:
                self.unhook_method(u)
            except Exception:
                pass
        self._unhooks.clear()
        self._stop_keep_alive()
        if self.vault:
            self.vault.shutdown()
        self._save_presence()
        self._save_fp()

    def _revert_fake_premium(self):
        try:
            for acc in range(4):
                try:
                    uc = UserConfig.getInstance(acc)
                    if not (uc and uc.isClientActivated()):
                        continue
                    my = int(uc.getClientUserId())
                    cu = uc.getCurrentUser()
                    if cu and int(cu.id) == my and bool(getattr(cu, "premium", False)):
                        cu.premium = False
                    try:
                        mc = MessagesController.getInstance(acc)
                        mu = mc.getUser(Long(my))
                        if mu and bool(getattr(mu, "premium", False)):
                            mu.premium = False
                    except Exception:
                        pass
                except Exception:
                    pass
        except Exception:
            pass

    def _start_keep_alive(self):
        if self._keep_alive_active:
            return
        self._keep_alive_active = True
        try:
            ctx = ApplicationLoader.applicationContext
            pm = ctx.getSystemService("power")
            if pm:
                WakeLock = jclass("android.os.PowerManager$WakeLock")
                wl = pm.newWakeLock(1, "MAX:KeepAlive")
                wl.setReferenceCounted(False)
                wl.acquire()
                self._wakelock = wl
        except Exception:
            pass
        try:
            ctx = ApplicationLoader.applicationContext
            app_cls = ctx.getClass()
            try:
                m = app_cls.getMethod("startPushService")
                m.invoke(ctx)
            except Exception:
                pass
        except Exception:
            pass

    def _stop_keep_alive(self):
        if not self._keep_alive_active:
            return
        self._keep_alive_active = False
        try:
            if self._wakelock:
                self._wakelock.release()
                self._wakelock = None
        except Exception:
            pass

    def _gc_caches(self):
        try:
            if len(self._read_state_cache) > 200:
                self._read_state_cache.clear()
        except Exception:
            pass
        try:
            if len(self._decorated_cells) > 100:
                self._decorated_cells.clear()
        except Exception:
            pass
        try:
            if len(self._ad_field_cache) > 20:
                self._ad_field_cache.clear()
        except Exception:
            pass
        try:
            if len(self._sc) > 30:
                self._sc.clear()
        except Exception:
            pass
        try:
            v = self.vault
            if v and v._revisions:
                if len(v._revisions) > 5000:
                    keys = sorted(v._revisions.keys(), key=lambda k: max((r.get("timestamp", 0) for r in v._revisions[k]), default=0))
                    for k in keys[:len(keys) - 2000]:
                        del v._revisions[k]
                for k, lst in list(v._revisions.items()):
                    for rev in lst:
                        if rev.get("data"):
                            rev["data"] = None
        except Exception:
            pass
        try:
            v = self.vault
            if v and len(v._removed) > 50000:
                v._removed.clear()
                v._removed_ts.clear()
                v._load()
        except Exception:
            pass
        try:
            if len(self._presence_records) > 0:
                for k, rows in list(self._presence_records.items()):
                    if len(rows) > 500:
                        self._presence_records[k] = rows[-500:]
        except Exception:
            pass
        try:
            if len(self._my_uids) > 10:
                self._my_uids.clear()
        except Exception:
            pass

    def _loop(self):
        cnt = 0
        _last_cleanup = 0
        _last_gc = 0
        while self._stop_event and not self._stop_event.is_set():
            try:
                cnt += 1
                self._presence_tick()
                if cnt % 3 == 0:
                    self._save_presence()
                    self._save_fp()
                now = _time.time()
                if now - _last_gc > 60:
                    _last_gc = now
                    self._gc_caches()
                if now - _last_cleanup > 1800:
                    _last_cleanup = now
                    if self._gs("save_attachments", False):
                        try:
                            _enforce_folder_limit()
                            _enforce_time_limit()
                        except Exception:
                            pass
                if self._gs("keep_app_alive", False):
                    if not self._keep_alive_active:
                        run_on_ui_thread(self._start_keep_alive)
                    if _time.time() - self._last_offline_push >= 20:
                        if self._is_ghost():
                            self.sendOffline()
                        self._last_offline_push = _time.time()
                elif self._keep_alive_active:
                    run_on_ui_thread(self._stop_keep_alive)
            except Exception:
                pass
            try:
                self._stop_event.wait(5.0)
            except Exception:
                break
        if self._keep_alive_active:
            try:
                self._stop_keep_alive()
            except Exception:
                pass

    def _on_eh_click(self, ctx):
        try:
            msg = ctx.get("message")
            if msg:
                did = int(msg.getDialogId())
                mid = int(msg.getId())
                self._show_revisions(did, mid)
        except Exception as e:
            log(f"[MAX] ehClick: {e}")

    def _setup_hooks(self):
        for fn in [self._hk_restore_loaded,
                   self._hk_local_read,
                   self._hk_self_online_label,
                   self._hk_own_profile_status,
                   self._hk_sched_no_redirect,
                   self._hk_story_save,
                   self._hk_media_seen,
                   self._hk_nfl,
                   self._hk_outgoing_params,
                    self._hk_story_viewer_prompt, self._hk_send_cancel,
                    self._hk_file_download, self._hk_open_document,
                    self._hk_forward_deleted, self._hk_fake_premium,
                    self._hk_feed_ads, self._hk_noforward,
                    self._hk_ctx_menu, self._hk_ctx_action]:
            try:
                fn()
            except Exception as e:
                log(f"[MAX] hook {getattr(fn, '__name__', '?')}: {e}")

    def _remove_messages_from_ui(self, mids):
        try:
            frag = LaunchActivity.getLastFragment()
            if not isinstance(frag, ChatActivity):
                return
            for mid in mids:
                try:
                    msgs_field = _ff(CA_CLS, "messages")
                    if not msgs_field:
                        break
                    msgs = msgs_field.get(frag)
                    if not msgs:
                        break
                    for i in range(msgs.size() - 1, -1, -1):
                        mo = msgs.get(i)
                        if mo and int(mo.getId()) == int(mid):
                            try:
                                frag.removeMessageWithThanos(mo)
                            except Exception:
                                msgs.remove(i)
                            break
                except Exception:
                    pass
        except Exception:
            pass

    def _hk_send_cancel(self):
        plugin = self
        class CancelHook(MethodHook):
            def before_hooked_method(self, param):
                try:
                    mo = param.args[0]
                    if mo is None:
                        return
                    mid = int(mo.getId())
                    did = int(mo.getDialogId())
                    if plugin.vault and plugin.vault.is_removed(did, mid):
                        plugin.vault.purge_removed(did, [mid])
                        plugin._invalidate_removed_cache()
                        run_on_ui_thread(lambda: plugin._reset_cell_appearance(did, mid))
                except Exception:
                    pass
        try:
            smh_cls = jclass("org.telegram.messenger.SendMessagesHelper").getClass()
            for mm in smh_cls.getDeclaredMethods():
                mn = str(mm.getName())
                if mn == "cancelSendingMessage" and len(mm.getParameterTypes()) == 1:
                    ptype = str(mm.getParameterTypes()[0].getName())
                    if "MessageObject" in ptype:
                        mm.setAccessible(True)
                        u = self.hook_method(mm, CancelHook())
                        if u:
                            self._unhooks.append(u)
        except Exception:
            pass

    def _hk_file_download(self):
        plugin = self
        try:
            FLC = find_class("org.telegram.messenger.FileLoader")
            class FileLoadHook(MethodHook):
                def after_hooked_method(self, param):
                    if not plugin._gs("save_attachments", False):
                        return
                    try:
                        f = param.args[0] if param.args else None
                        if f is None:
                            return
                        fpath = None
                        if hasattr(f, "getAbsolutePath"):
                            fpath = str(f.getAbsolutePath())
                        elif isinstance(f, str):
                            fpath = str(f)
                        if fpath:
                            plugin._on_file_downloaded_in_tg(fpath)
                    except Exception:
                        pass
            for mm in FLC.getClass().getDeclaredMethods():
                mn = str(mm.getName())
                if mn in ("onFinishLoadingFile", "fileDidLoaded") and len(mm.getParameterTypes()) >= 1:
                    mm.setAccessible(True)
                    u = self.hook_method(mm, FileLoadHook())
                    if u:
                        self._unhooks.append(u)
                    break
        except Exception:
            pass

    def _hk_open_document(self):
        plugin = self
        class OpenForViewHook(MethodHook):
            def before_hooked_method(self, param):
                try:
                    args = param.args
                    if args is None or len(args) == 0:
                        return
                    message = args[0]
                    if message is None:
                        return
                    if "MessageObject" not in str(message.getClass().getName()):
                        return
                    file_name = str(message.getFileName())
                    if not file_name.endswith(".mre_backup"):
                        return
                    param.setResult(True)
                    run_on_ui_thread(lambda: plugin._handle_backup_file(message))
                except Exception:
                    pass
        try:
            AU = jclass("org.telegram.messenger.AndroidUtilities").getClass()
            for mm in AU.getDeclaredMethods():
                if str(mm.getName()) == "openForView":
                    mm.setAccessible(True)
                    u = self.hook_method(mm, OpenForViewHook())
                    if u:
                        self._unhooks.append(u)
        except Exception:
            pass

    def _handle_backup_file(self, message):
        try:
            doc = message.getDocument()
            if doc is None:
                return
            acc = int(UserConfig.selectedAccount)
            fl = FileLoader.getInstance(acc)
            f = fl.getPathToAttach(doc)
            if f is None or not f.exists():
                try:
                    f = fl.getPathToMessage(message.messageOwner)
                except Exception:
                    f = None
            if f is None or not f.exists():
                fl.loadFile(doc, message, 1, 0)
                BulletinHelper.show_info(L.g("downloading_retry"))
                return
            content = read_file(str(f.getAbsolutePath()))
            if content is None:
                BulletinHelper.show_info(L.g("import_error"))
                return
            try:
                data = json.loads(content)
            except Exception:
                BulletinHelper.show_info(L.g("import_error"))
                return
            if not isinstance(data, dict):
                BulletinHelper.show_info(L.g("import_error"))
                return
            self._show_import_confirm_data(data)
        except Exception as e:
            log(f"[MAX] handle backup: {e}")
            BulletinHelper.show_info(L.g("import_error"))

    def _pick_attach_folder(self, _v=None):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                activity = frag.getParentActivity()
                from android.content import Intent
                intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                REQUEST_CODE = 48271
                def _setup_result_hook():
                    try:
                        Activity = jclass("android.app.Activity")
                        class ResultHook(MethodHook):
                            def before_hooked_method(self_hook, param):
                                try:
                                    rc = int(param.args[0])
                                    result_code = int(param.args[1])
                                    data = param.args[2]
                                    if rc != REQUEST_CODE:
                                        return
                                    if result_code != -1 or data is None:
                                        return
                                    uri = data.getData()
                                    if uri is None:
                                        return
                                    try:
                                        ctx = ApplicationLoader.applicationContext
                                        ctx.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                    except Exception:
                                        pass
                                    real_path = plugin._resolve_uri_to_path(uri)
                                    if real_path and os.path.isdir(real_path):
                                        plugin._prefs().edit().putString("custom_attach_dir", real_path).apply()
                                        _ensure_nomedia(real_path)
                                        run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("folder_set")))
                                        plugin._refresh_attach_folder_label()
                                    else:
                                        uri_str = str(uri.toString())
                                        plugin._prefs().edit().putString("custom_attach_uri", uri_str).apply()
                                        run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("folder_set")))
                                        plugin._refresh_attach_folder_label()
                                    try:
                                        if hasattr(self_hook, '_unhook_ref') and self_hook._unhook_ref:
                                            plugin.unhook_method(self_hook._unhook_ref)
                                    except Exception:
                                        pass
                                except Exception:
                                    pass
                        hook_inst = ResultHook()
                        for mm in Activity.getClass().getDeclaredMethods():
                            if str(mm.getName()) == "onActivityResult" and len(mm.getParameterTypes()) == 3:
                                mm.setAccessible(True)
                                u = plugin.hook_method(mm, hook_inst)
                                if u:
                                    hook_inst._unhook_ref = u
                                    plugin._unhooks.append(u)
                                break
                    except Exception:
                        pass
                _setup_result_hook()
                activity.startActivityForResult(intent, REQUEST_CODE)
            except Exception as e:
                log(f"[MAX] pickFolder: {e}")
        run_on_ui_thread(_ui)

    def _resolve_uri_to_path(self, uri):
        try:
            uri_str = str(uri.toString())
            if "primary" in uri_str:
                from android.os import Environment
                root = str(Environment.getExternalStorageDirectory().getAbsolutePath())
                parts = uri_str.split("primary%3A")
                if len(parts) > 1:
                    rel = parts[1].replace("%2F", "/").replace("%20", " ")
                    path = os.path.join(root, rel)
                    if os.path.isdir(path):
                        return path
            try:
                from android.provider import DocumentsContract
                doc_id = str(DocumentsContract.getTreeDocumentId(uri))
                if ":" in doc_id:
                    split = doc_id.split(":")
                    storage_type = split[0]
                    rel_path = split[1] if len(split) > 1 else ""
                    if storage_type == "primary":
                        from android.os import Environment
                        root = str(Environment.getExternalStorageDirectory().getAbsolutePath())
                        path = os.path.join(root, rel_path)
                        if os.path.isdir(path):
                            return path
                    else:
                        path = os.path.join("/storage", storage_type, rel_path)
                        if os.path.isdir(path):
                            return path
            except Exception:
                pass
        except Exception:
            pass
        return None

    def _reset_attach_folder(self, _v=None):
        try:
            self._prefs().edit().remove("custom_attach_dir").remove("custom_attach_uri").apply()
            BulletinHelper.show_info(L.g("folder_reset"))
            self._refresh_attach_folder_label()
        except Exception:
            pass

    # Bug #6: Manual import via file picker (fallback when auto-detect fails)
    def _import_from_file_picker(self, _v=None):
        try:
            frag = get_last_fragment()
            if not frag or not frag.getParentActivity():
                return
            activity = frag.getParentActivity()
            from android.content import Intent
            intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.setType("application/json")
            REQUEST_CODE = 48272
            def _setup_result_hook():
                try:
                    Activity = jclass("android.app.Activity")
                    class ResultHook(MethodHook):
                        def before_hooked_method(self_hook, param):
                            try:
                                rc = int(param.args[0])
                                result_code = int(param.args[1])
                                data = param.args[2]
                                if rc != REQUEST_CODE:
                                    return
                                if result_code != -1 or data is None:
                                    return
                                uri = data.getData()
                                if uri is None:
                                    return
                                try:
                                    ctx = ApplicationLoader.applicationContext
                                    content = read_file_from_uri(ctx, uri)
                                    if content is None:
                                        BulletinHelper.show_info(L.g("import_error"))
                                        return
                                    data = json.loads(content)
                                    if not isinstance(data, dict):
                                        BulletinHelper.show_info(L.g("import_error"))
                                        return
                                    self._show_import_confirm_data(data)
                                except Exception as e:
                                    log(f"[MAX] manual import failed: {e}")
                                    BulletinHelper.show_info(L.g("import_error"))
                                try:
                                    if hasattr(self_hook, '_unhook_ref') and self_hook._unhook_ref:
                                        self.unhook_method(self_hook._unhook_ref)
                                except Exception:
                                    pass
                            except Exception:
                                pass
                    hook_inst = ResultHook()
                    for mm in Activity.getClass().getDeclaredMethods():
                        if str(mm.getName()) == "onActivityResult" and len(mm.getParameterTypes()) == 3:
                            mm.setAccessible(True)
                            u = self.hook_method(mm, hook_inst)
                            if u:
                                hook_inst._unhook_ref = u
                                self._unhooks.append(u)
                            break
                except Exception:
                    pass
            _setup_result_hook()
            activity.startActivityForResult(intent, REQUEST_CODE)
        except Exception as e:
            log(f"[MAX] manual import picker: {e}")

    def _refresh_attach_folder_label(self):
        # subtext у Text(...) вычисляется один раз при построении create_settings()
        # и дальше не привязан к _get_current_folder_display(), поэтому после смены
        # или сброса папки строка с названием сама не обновится. set_setting(...,
        # reload_settings=True) — штатный способ фреймворка форсировать пересборку
        # экрана настроек (см. _tab_toggle выше); используем существующий ключ и
        # тот же самый текущий value, чтобы вызвать reload без побочных эффектов.
        try:
            run_on_ui_thread(lambda: self.set_setting("save_attachments", self.get_setting("save_attachments", False), reload_settings=True))
        except Exception as e:
            log(f"[MAX] refreshAttachFolderLabel: {e}")

    def _get_current_folder_display(self):
        try:
            custom = str(self._prefs().getString("custom_attach_dir", "") or "")
            if custom and os.path.isdir(custom):
                parts = custom.replace("\\", "/").split("/")
                return parts[-1] if parts else custom
            custom_uri = str(self._prefs().getString("custom_attach_uri", "") or "")
            if custom_uri:
                # Путь не резолвится в реальную ФС-директорию (SAF-дерево на съёмном
                # хранилище и т.п.) — достаём читаемое имя папки из хвоста URI дерева
                # документов, а не показываем "По умолчанию" для уже выбранной папки.
                try:
                    decoded = urllib.parse.unquote(custom_uri)
                except Exception:
                    decoded = custom_uri
                tail = decoded.rstrip("/").split("/")[-1]
                if ":" in tail:
                    tail = tail.split(":")[-1]
                return tail if tail else custom_uri
        except Exception:
            pass
        return L.g("attach_folder_default")

    def _show_browse_attachments(self, _v=None):
        plugin = self
        def _bg():
            try:
                attach_dir = _get_attach_dir()
                if not attach_dir or not os.path.isdir(attach_dir):
                    run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("no_attachments")))
                    return
                files = []
                for fname in os.listdir(attach_dir):
                    fpath = os.path.join(attach_dir, fname)
                    if not os.path.isfile(fpath) or os.path.getsize(fpath) == 0:
                        continue
                    parts = fname.split("_", 2)
                    if len(parts) < 2:
                        continue
                    try:
                        did = int(parts[0])
                        mid = int(parts[1].split("_")[0].split(".")[0])
                    except Exception:
                        continue
                    mtime = os.path.getmtime(fpath)
                    size = os.path.getsize(fpath)
                    files.append({"did": did, "mid": mid, "path": fpath, "name": fname, "mtime": mtime, "size": size})
                if not files:
                    run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("no_attachments")))
                    return
                chats = {}
                for f in files:
                    did = f["did"]
                    if did not in chats:
                        chats[did] = []
                    chats[did].append(f)
                for did in chats:
                    chats[did].sort(key=lambda x: -x["mtime"])
                chat_list = sorted(chats.items(), key=lambda x: -x[1][0]["mtime"])
                run_on_ui_thread(lambda: plugin._show_chat_list(chat_list))
            except Exception as e:
                log(f"[MAX] browse attach: {e}")
                run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("error")))
        threading.Thread(target=_bg, daemon=True).start()

    def _chat_display_name(self, did):
        try:
            acc = int(UserConfig.selectedAccount)
            mc = MessagesController.getInstance(acc)
            did = int(did)
            if did > 0:
                user = mc.getUser(Long(did))
                if user:
                    nm = (str(getattr(user, "first_name", "") or "") + " " + str(getattr(user, "last_name", "") or "")).strip()
                    if nm:
                        return nm
                    un = str(getattr(user, "username", "") or "")
                    if un:
                        return "@" + un
            else:
                chat = mc.getChat(Long(-did))
                if chat:
                    t = str(getattr(chat, "title", "") or "")
                    if t:
                        return t
        except Exception:
            pass
        return f"id{did}"

    def _fmt_size(self, size):
        if self.dex and self.dex.loaded:
            try:
                return str(self.dex.call("formatSize", jlong(int(size))))
            except Exception:
                pass
        if size < 1024:
            return f"{size} B"
        if size < 1024 * 1024:
            return f"{size // 1024} KB"
        return f"{size / (1024 * 1024):.1f} MB"

    def _show_chat_list(self, chat_list):
        try:
            frag = get_last_fragment()
            if not frag or not frag.getParentActivity():
                return
            plugin = self
            items = []
            chat_data = []
            for did, files in chat_list:
                name = plugin._chat_display_name(did)
                total_size = sum(f["size"] for f in files)
                last_date = _format_date(int(files[0]["mtime"]))
                items.append(f"{name}\n{len(files)} {L.g('files_count')} \u00B7 {plugin._fmt_size(total_size)} \u00B7 {last_date}")
                chat_data.append((did, files))
            def on_pick(dlg, which):
                dlg.dismiss()
                did, files = chat_data[which]
                plugin._show_file_list(did, files)
            d = AlertDialogBuilder(frag.getParentActivity())
            d.set_title(f"{L.g('browse_attachments')} ({len(chat_list)})")
            d.set_items(items, on_pick)
            d.set_negative_button(L.g("close"), lambda dd, w: dd.dismiss())
            d.show()
        except Exception:
            pass

    def _show_file_list(self, did, files):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                name = plugin._chat_display_name(did)
                items = []
                for f in files:
                    dt = _format_date(int(f["mtime"]))
                    sz = plugin._fmt_size(f["size"])
                    ext = os.path.splitext(f["name"])[1].lower()
                    if ext in (".jpg", ".jpeg", ".png", ".webp"):
                        icon = L.g("has_photo")
                    elif ext in (".mp4", ".mkv", ".avi"):
                        icon = L.g("has_video")
                    elif ext in (".mp3", ".ogg", ".wav", ".m4a", ".oga"):
                        icon = L.g("has_voice")
                    else:
                        icon = L.g("has_doc")
                    items.append(f"{icon} #{f['mid']}  {sz}  {dt}")
                def on_pick(dlg, which):
                    dlg.dismiss()
                    f = files[which]
                    plugin._show_file_actions(did, f)
                d = AlertDialogBuilder(frag.getParentActivity())
                d.set_title(f"{name} ({len(files)})")
                d.set_items(items, on_pick)
                d.set_negative_button(L.g("back"), lambda dd, w: (dd.dismiss(), plugin._show_browse_attachments()))
                d.show()
            except Exception:
                pass
        run_on_ui_thread(_ui)

    def _show_file_actions(self, did, f):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                dt = _format_date(int(f["mtime"]))
                sz = plugin._fmt_size(f["size"])
                title = f"#{f['mid']} \u00B7 {sz} \u00B7 {dt}"
                btn_items = [L.g("view"), L.g("go_to_msg"), L.g("delete")]
                def on_action(dlg, w):
                    dlg.dismiss()
                    if w == 0:
                        _open_file(f["path"])
                    elif w == 1:
                        plugin._scroll_to_message(did, f["mid"])
                    elif w == 2:
                        try:
                            os.remove(f["path"])
                            BulletinHelper.show_info(L.g("cleared"))
                        except Exception:
                            pass
                d = AlertDialogBuilder(frag.getParentActivity())
                d.set_title(title)
                d.set_items(btn_items, on_action)
                d.set_negative_button(L.g("back"), lambda dd, w: (dd.dismiss(), plugin._show_file_list(did, [ff for ff in plugin._get_chat_files(did)])))
                d.show()
            except Exception:
                pass
        run_on_ui_thread(_ui)

    def _get_chat_files(self, did):
        try:
            attach_dir = _get_attach_dir()
            if not attach_dir:
                return []
            prefix = f"{did}_"
            result = []
            for fname in os.listdir(attach_dir):
                if not fname.startswith(prefix):
                    continue
                fpath = os.path.join(attach_dir, fname)
                if not os.path.isfile(fpath) or os.path.getsize(fpath) == 0:
                    continue
                parts = fname.split("_", 2)
                try:
                    mid = int(parts[1].split("_")[0].split(".")[0])
                except Exception:
                    continue
                result.append({"did": did, "mid": mid, "path": fpath, "name": fname, "mtime": os.path.getmtime(fpath), "size": os.path.getsize(fpath)})
            result.sort(key=lambda x: -x["mtime"])
            return result
        except Exception:
            return []

    def _show_import_confirm_data(self, data):
        plugin = self
        ts = data.get("_ts", 0)
        ver = data.get("_version", "?")
        import datetime
        date_str = datetime.datetime.fromtimestamp(int(ts)).strftime("%d.%m.%Y %H:%M") if ts else "?"
        msg = f"{L.g('import_confirm')}\n\nv{ver} — {date_str}"
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                d = AlertDialogBuilder(frag.getParentActivity())
                d.set_title(L.g("import_title"))
                d.set_message(msg)
                d.set_positive_button(L.g("yes"), lambda dlg, w: (dlg.dismiss(), plugin._do_import(data)))
                d.set_negative_button(L.g("no"), lambda dlg, w: dlg.dismiss())
                d.show()
            except Exception:
                pass
        run_on_ui_thread(_ui)

    def _on_file_downloaded_in_tg(self, fpath):
        if not fpath:
            return
        def _work():
            try:
                if not os.path.exists(fpath):
                    return
                fname = os.path.basename(fpath)
                nums = re.findall(r'\d{6,}', fname)
                if not nums:
                    return
                v = self.vault
                if not v:
                    return
                for (did, mid), rev_list in list(v._revisions.items()):
                    for rev in rev_list:
                        mh = rev.get("media_hash", "")
                        if mh and any(n in mh for n in nums):
                            _copy_path_to_vault(fpath, did, mid, mh)
                            return
            except Exception:
                pass
        threading.Thread(target=_work, daemon=True).start()

    def _reset_cell_appearance(self, did, mid):
        try:
            frag = LaunchActivity.getLastFragment()
            if not isinstance(frag, ChatActivity):
                return
            lv = frag.getChatListView()
            if not lv:
                return
            for i in range(lv.getChildCount()):
                ch = lv.getChildAt(i)
                if ch is None:
                    continue
                cn = ch.getClass().getSimpleName()
                if cn is None or str(cn) != "ChatMessageCell":
                    continue
                mo = ch.getMessageObject()
                if mo and int(mo.getId()) == int(mid) and int(mo.getDialogId()) == int(did):
                    cell_id = id(ch)
                    self._decorated_cells.pop(cell_id, None)
                    ch.setAlpha(jfloat(1.0))
                    try:
                        ch.forceResetMessageObject()
                    except Exception:
                        pass
                    adapter = lv.getAdapter()
                    if adapter:
                        pos = lv.getChildAdapterPosition(ch)
                        if pos >= 0:
                            adapter.notifyItemChanged(pos)
                    break
        except Exception:
            pass

    def _hk_track_user_deletions(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                try:
                    ml = param.args[0]
                    if not ml or ml.isEmpty():
                        return
                    mids = [int(x) for x in _jlist(ml)]
                    did = plugin._resolve_did(mids)
                    if did == 489000:
                        return
                    if not did:
                        try:
                            did = int(param.args[3])
                        except Exception:
                            did = 0
                    already_deleted = []
                    if did and plugin.vault:
                        for mid in mids:
                            if plugin.vault.is_removed(did, mid):
                                already_deleted.append(mid)
                    if already_deleted and did:
                        plugin.vault.purge_removed(did, already_deleted)
                        plugin._invalidate_removed_cache()
                        for m in already_deleted:
                            plugin._vault_purging.add((int(did), int(m)))
                            plugin._vault_purging_mids.add(int(m))
                        def _cleanup_purging(keys=[(int(did), int(m)) for m in already_deleted]):
                            _time.sleep(10)
                            for k in keys:
                                plugin._vault_purging.discard(k)
                                plugin._vault_purging_mids.discard(k[1])
                        threading.Thread(target=_cleanup_purging, daemon=True).start()
                    own_mids = []
                    try:
                        for mid in mids:
                            mo = _get_message_from_chat(did, mid)
                            if mo and mo.messageOwner and bool(getattr(mo.messageOwner, "out", False)):
                                own_mids.append(mid)
                    except Exception:
                        if did:
                            own_mids = mids
                    plugin._mark_my_deletion(own_mids if own_mids else [])
                except Exception:
                    pass
        mc = get_messages_controller()
        if not mc:
            return
        for mm in mc.getClass().getDeclaredMethods():
            if str(mm.getName()) == "deleteMessages" and len(mm.getParameterTypes()) >= 7:
                mm.setAccessible(True)
                u = self.hook_method(mm, H())
                if u:
                    self._unhooks.append(u)
                break

    def _hk_intercept_delete(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                if not plugin._gs("save_deleted", True):
                    return
                try:
                    did = int(param.args[0])
                    if did == 489000:
                        return
                    mids = [int(x) for x in _jlist(param.args[1])]
                    if not mids:
                        return
                    purged = []
                    other = []
                    for m in mids:
                        if (did, m) in plugin._vault_purging:
                            purged.append(m)
                        elif not plugin._is_my_deletion(m):
                            skip_own = plugin._gs("track_own_deletions", False)
                            if skip_own and _is_out_message(did, m):
                                pass
                            else:
                                other.append(m)
                    my = [m for m in mids if plugin._is_my_deletion(m)]
                    pass_through = purged + my
                    if not other and not pass_through:
                        return
                    if other:
                        run_on_ui_thread(lambda: plugin._save_read_states_bulk(did, other))
                        data_map = _collect_deleted_text(did, other)
                        plugin.vault.mark_removed(did, other, data_map)
                        if plugin._gs("save_attachments", False):
                            threading.Thread(target=lambda: _save_attachments_for_mids(did, other), daemon=True).start()
                        plugin._invalidate_removed_cache()
                        run_on_ui_thread(plugin._refresh_cells)
                    if pass_through:
                        fl = ArrayList()
                        for m in pass_through:
                            fl.add(Integer(m))
                        param.args[1] = fl
                    else:
                        param.setResult(None)
                except Exception as e:
                    log(f"[MAX] intercept: {e}")
        MSC = find_class("org.telegram.messenger.MessagesStorage")
        for mm in MSC.getClass().getDeclaredMethods():
            if str(mm.getName()) == "markMessagesAsDeletedInternal" and len(mm.getParameterTypes()) == 5:
                mm.setAccessible(True)
                u = self.hook_method(mm, H())
                if u:
                    self._unhooks.append(u)
                break

    def _hk_suppress_delete_ui(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                if not plugin._gs("save_deleted", True):
                    return
                try:
                    ml = param.args[0]
                    if not ml or ml.isEmpty():
                        return
                    mids = [int(x) for x in _jlist(ml)]
                    try:
                        f = _ff(CA_CLS, "dialog_id")
                        did = int(f.get(param.thisObject)) if f else 0
                    except Exception:
                        did = 0
                    if did == 489000:
                        return
                    purged = []
                    other = []
                    for m in mids:
                        if did and (did, m) in plugin._vault_purging:
                            purged.append(m)
                        elif not plugin._is_my_deletion(m):
                            skip_own = plugin._gs("track_own_deletions", False)
                            if skip_own and _is_out_message(did, m):
                                pass
                            else:
                                other.append(m)
                    my = [m for m in mids if plugin._is_my_deletion(m)]
                    pass_through = purged + my
                    if not other and not pass_through:
                        return
                    if not other and pass_through:
                        return
                    if pass_through:
                        fl = ArrayList()
                        for m in pass_through:
                            fl.add(Integer(m))
                        param.args[0] = fl
                    else:
                        param.args[0] = ArrayList()
                    try:
                        if other and UNSEL_ADD:
                            jms = get_private_field(param.thisObject, "selectedMessagesIds")
                            if jms:
                                cnt = 0
                                for m in other:
                                    for i in range(len(jms)):
                                        sa = jms[i]
                                        if sa is not None and int(sa.indexOfKey(m)) >= 0:
                                            try:
                                                UNSEL_ADD.invoke(param.thisObject, sa.get(m), False, False)
                                                cnt += 1
                                            except Exception:
                                                pass
                                            break
                                if cnt:
                                    UNSEL_ADD.invoke(param.thisObject, None, False, True)
                                    if UPD_AM_TITLE:
                                        UPD_AM_TITLE.invoke(param.thisObject)
                        if UPD_VISIBLE:
                            UPD_VISIBLE.invoke(param.thisObject)
                    except Exception:
                        pass
                    try:
                        f = _ff(CA_CLS, "dialog_id")
                        did = int(f.get(param.thisObject)) if f else 0
                        if did and other:
                            plugin._save_read_states_bulk(did, other)
                            data_map = _collect_deleted_text(did, other)
                            plugin.vault.mark_removed(did, other, data_map)
                            plugin._invalidate_removed_cache()
                            if plugin._gs("save_attachments", False):
                                att_data = []
                                ca = param.thisObject
                                msgs_field = _ff(CA_CLS, "messages")
                                chat_msgs = msgs_field.get(ca) if msgs_field else None
                                if chat_msgs:
                                    other_set = set(other)
                                    for i in range(chat_msgs.size()):
                                        mo = chat_msgs.get(i)
                                        if mo and int(mo.getId()) in other_set:
                                            att_data.append((mo.messageOwner, did, int(mo.getId())))
                                if att_data:
                                    def _bg_copy(data=att_data):
                                        for msg, d, m in data:
                                            try:
                                                _ensure_attach_saved(msg, d, m, _get_media_hash(msg))
                                            except Exception:
                                                pass
                                    threading.Thread(target=_bg_copy, daemon=True).start()
                            plugin._refresh_cells()
                    except Exception:
                        pass
                except Exception:
                    pass
        m = _fm(CA_CLS, "processDeletedMessages", 4)
        if m:
            u = self.hook_method(m, H())
            if u:
                self._unhooks.append(u)

    def _hk_allow_delete(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                v = plugin.vault
                if not v or not v._removed:
                    return
                try:
                    mo = param.thisObject
                    did = int(mo.getDialogId())
                    if did not in plugin._removed_dids:
                        return
                    if (did, int(mo.getId())) in v._removed:
                        param.setResult(Boolean(True))
                except Exception:
                    pass
        MOC = find_class("org.telegram.messenger.MessageObject")
        CC = find_class("org.telegram.tgnet.TLRPC$Chat")
        m = MOC.getClass().getDeclaredMethod("canDeleteMessage", Boolean.TYPE, CC)
        m.setAccessible(True)
        u = self.hook_method(m, H())
        if u:
            self._unhooks.append(u)

    def _get_opacity_val(self):
        idx = _int(self._gs("del_opacity_level", 2), 2)
        return (1.0, 0.8, 0.6, 0.4, 0.2)[max(0, min(idx, 4))]

    def _hk_measure_time(self):
        plugin = self
        _opacity_cells = set()
        _jhash = jclass("java.lang.System").identityHashCode
        _removed_cache = {}
        _cache_ts = 0
        class H(MethodHook):
            def after_hooked_method(self, param):
                v = plugin.vault
                cell = param.thisObject
                mo = param.args[0]
                if not v or not mo:
                    return
                cell_id = int(_jhash(cell))
                # Fast path: if no removed messages, skip immediately
                if not v._removed:
                    _opacity_cells.discard(cell_id)
                    return
                # Cache removed check to avoid repeated set lookups
                nonlocal _removed_cache, _cache_ts
                now = _time.time()
                if now - _cache_ts > 2.0:  # Refresh cache every 2 seconds
                    _removed_cache = set(v._removed)
                    _cache_ts = now
                try:
                    key = (int(mo.getDialogId()), int(mo.getId()))
                    if key in _removed_cache:
                        _opacity_cells.add(cell_id)
                        plugin._stamp_cell(cell, mo)
                    else:
                        _opacity_cells.discard(cell_id)
                except Exception:
                    _opacity_cells.discard(cell_id)
                if len(_opacity_cells) > 500:
                    _opacity_cells.clear()
        class SetAlphaGuard(MethodHook):
            _cached_target = [0.6]
            _n = [0]
            def before_hooked_method(self, param):
                a = float(param.args[0])
                if a < 0.95:
                    return
                cell_id = int(_jhash(param.thisObject))
                if cell_id not in _opacity_cells:
                    return
                self._n[0] += 1
                if self._n[0] & 63 == 0:
                    if not plugin._gs("del_show_opacity", True):
                        self._cached_target[0] = 1.0
                    else:
                        self._cached_target[0] = plugin._get_opacity_val()
                target = self._cached_target[0]
                if target < 1.0:
                    param.args[0] = jfloat(target)
        CLC = find_class("org.telegram.ui.Cells.ChatMessageCell")
        MOC = find_class("org.telegram.messenger.MessageObject")
        m = CLC.getClass().getDeclaredMethod("measureTime", MOC)
        m.setAccessible(True)
        u = self.hook_method(m, H())
        if u:
            self._unhooks.append(u)
        cmc = CLC.getClass()
        try:
            sa_method = cmc.getMethod("setAlpha", Float.TYPE)
            sa_method.setAccessible(True)
            u = self.hook_method(sa_method, SetAlphaGuard())
            if u:
                self._unhooks.append(u)
        except Exception:
            pass

    def _hk_restore_loaded(self):
        pass

    def _hk_edit_capture(self):
        plugin = self
        _EDIT_NAMES = frozenset(["TL_updateEditMessage", "TL_updateEditChannelMessage"])
        _DELETE_NAMES = frozenset(["TL_updateDeleteMessages", "TL_updateDeleteChannelMessages"])
        class H(MethodHook):
            def after_hooked_method(self, param):
                v = plugin.vault
                if not v:
                    return
                try:
                    upds_obj = param.args[0]
                    if not upds_obj:
                        return
                    upd_list = getattr(upds_obj, "updates", None)
                    if not upd_list:
                        return
                    it = upd_list.iterator()
                    while it.hasNext():
                        upd = it.next()
                        cn = str(upd.getClass().getSimpleName())
                        if cn in _EDIT_NAMES:
                            try:
                                msg = upd.message
                                if not msg or bool(getattr(msg, "out", False)):
                                    continue
                                did = _peer_did(msg)
                                mid = int(msg.id)
                                if not did or not mid:
                                    continue
                                save_att = plugin._gs("save_attachments", False)
                                if not v.has_revisions(did, mid):
                                    mo = _get_message_from_chat(did, mid)
                                    if mo and mo.messageOwner:
                                        old_msg = mo.messageOwner
                                        v.save_message_version(did, mid, old_msg, is_original=True)
                                        if save_att:
                                            _ensure_attach_saved(old_msg, did, mid, _get_media_hash(old_msg))
                                v.save_message_version(did, mid, msg, is_original=False)
                                if save_att:
                                    _ensure_attach_saved(msg, did, mid, _get_media_hash(msg))
                            except Exception:
                                pass
                        elif cn in _DELETE_NAMES:
                            if not plugin._gs("save_deleted", True):
                                continue
                            try:
                                msg = upd.message
                                if not msg or bool(getattr(msg, "out", False)):
                                    continue
                                did = _peer_did(msg)
                                mid = int(msg.id)
                                if not did or not mid:
                                    continue
                                # Save deleted message to vault
                                data_map = _collect_deleted_text(did, [mid])
                                v.mark_removed(did, [mid], data_map)
                                if plugin._gs("save_attachments", False):
                                    threading.Thread(target=lambda: _ensure_attach_saved(msg, did, mid, _get_media_hash(msg), force_download=False), daemon=True).start()
                                # Prevent message from being removed from chat by cancelling the update
                                param.setResult(None)
                            except Exception:
                                pass
                except Exception:
                    pass
        MCC = find_class("org.telegram.messenger.MessagesController")
        for mm in MCC.getClass().getDeclaredMethods():
            if str(mm.getName()) == "processUpdates" and len(mm.getParameterTypes()) >= 2:
                mm.setAccessible(True)
                u = self.hook_method(mm, H())
                if u:
                    self._unhooks.append(u)
                break

    def _hk_story_viewer_prompt(self):
        # Из elyx: перехват открытия StoryViewer работает только когда для
        # ghost_no_read_stories выбран режим 3 ("спрашивать при открытии").
        # До подтверждения диалогом сторис не открывается и не читается.
        plugin = self
        try:
            sv_cls = jclass("org.telegram.ui.Stories.StoryViewer").getClass()
            class OpenHook(MethodHook):
                def before_hooked_method(self, param):
                    try:
                        if plugin._story_bypass:
                            return
                        if plugin._fmode("ghost_no_read_stories", 1) != 3:
                            return
                        the_method = param.method
                        the_obj = param.thisObject
                        the_args = list(param.args)
                        param.setResult(None)

                        def show_dialog():
                            try:
                                frag = get_last_fragment()
                                ctx = frag.getParentActivity() if frag else None
                                if not ctx:
                                    plugin._do_open_story(the_method, the_obj, the_args, False)
                                    return

                                def on_yes(dlg, w):
                                    dlg.dismiss()
                                    plugin._do_open_story(the_method, the_obj, the_args, True)

                                def on_no(dlg, w):
                                    dlg.dismiss()
                                    plugin._do_open_story(the_method, the_obj, the_args, False)

                                d = AlertDialogBuilder(ctx)
                                d.set_title(L.g("story_dlg_title"))
                                d.set_message(L.g("story_dlg_msg"))
                                d.set_positive_button(L.g("yes"), on_yes)
                                d.set_negative_button(L.g("no"), on_no)
                                d.show()
                            except Exception:
                                plugin._do_open_story(the_method, the_obj, the_args, False)

                        run_on_ui_thread(show_dialog)
                    except Exception:
                        pass
            class CloseHook(MethodHook):
                def after_hooked_method(self, param):
                    try:
                        if plugin._story_viewer_open:
                            plugin._story_viewer_open = False

                            def delayed_disable():
                                _time.sleep(1.0)
                                if not plugin._story_viewer_open:
                                    run_on_ui_thread(plugin._disable_ghost_after_stories)

                            threading.Thread(target=delayed_disable, daemon=True).start()
                    except Exception:
                        pass
            for mm in sv_cls.getDeclaredMethods():
                nm = str(mm.getName())
                if nm == "open" or nm == "openFor":
                    mm.setAccessible(True)
                    u = self.hook_method(mm, OpenHook())
                    if u:
                        self._unhooks.append(u)
            for close_name in ["close", "dismiss"]:
                for pc in [1, 0]:
                    m = _fm(sv_cls, close_name, pc)
                    if m:
                        u = self.hook_method(m, CloseHook())
                        if u:
                            self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] storyPrompt: {e}")

    def _hk_ctx_menu(self):
        plugin = self
        class H(MethodHook):
            def after_hooked_method(self, param):
                try:
                    mo = param.args[0]
                    plugin._ctx_msg = mo
                    did, mid = mo.getDialogId(), mo.getId()
                    icons, items, options = param.args[1], param.args[2], param.args[3]
                    if plugin._gs("save_edits", True) and plugin.vault.has_revisions(did, mid):
                        items.add(0, L.g("edit_history"))
                        options.add(0, Integer(_OPT_REVISIONS))
                        try:
                            from org.telegram.messenger import R as TGR
                            icons.add(0, Integer(TGR.drawable.menu_premium_clock))
                        except Exception:
                            icons.add(0, Integer(0))
                    is_once = False
                    try:
                        is_once = mo.isSecret() or mo.isSecretMedia() or mo.isVoiceOnce() or mo.isRoundOnce()
                    except Exception:
                        pass
                    if (plugin._chk("ghost_no_read") or is_once) and not mo.isOut():
                        items.add(0, L.g("mark_read"))
                        options.add(0, Integer(_OPT_MARK_READ))
                        try:
                            from org.telegram.messenger import R as TGR
                            icons.add(0, Integer(TGR.drawable.msg_markread))
                        except Exception:
                            icons.add(0, Integer(0))
                    if plugin._gs("keep_once", False) and is_once:
                        try:
                            from org.telegram.messenger import R as TGR
                            items.add(L.g("forward"))
                            options.add(Integer(2))
                            icons.add(Integer(TGR.drawable.msg_forward))
                            items.add(L.g("save_media"))
                            options.add(Integer(4))
                            icons.add(Integer(TGR.drawable.msg_gallery))
                        except Exception:
                            pass
                except Exception:
                    pass
        m = _fm(CA_CLS, "fillMessageMenu", 4)
        if m:
            u = self.hook_method(m, H())
            if u:
                self._unhooks.append(u)

    def _hk_ctx_action(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                try:
                    opt = int(param.args[0])
                    mo = plugin._ctx_msg
                    if not mo:
                        try:
                            fd = _ff(CA_CLS, "selectedObject")
                            if fd:
                                mo = fd.get(param.thisObject)
                        except Exception:
                            pass
                    if not mo:
                        return
                    if opt == _OPT_REVISIONS:
                        plugin._show_revisions(mo.getDialogId(), mo.getId())
                        param.setResult(None)
                    elif opt == _OPT_MARK_READ:
                        plugin._send_read_content_for_message(mo.getDialogId(), mo.getId())
                        param.setResult(None)
                except Exception:
                    pass
        m = _fm(CA_CLS, "processSelectedOption", 1)
        if m:
            u = self.hook_method(m, H())
            if u:
                self._unhooks.append(u)

    def _hk_outgoing_params(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                try:
                    p = param.args[0]
                    if p is None:
                        return
                    reply = getattr(p, "replyToMsg", None)
                    quote = getattr(p, "replyQuote", None)
                    top = getattr(p, "replyToTopMsg", None)
                    if reply or quote:
                        try:
                            did = reply.getDialogId() if reply else quote.message.getDialogId()
                            mid = reply.getId() if reply else quote.message.getId()
                        except Exception:
                            did = mid = 0
                        if did and mid and plugin.vault and plugin.vault.is_removed(did, mid):
                            try:
                                acc = UserConfig.selectedAccount
                                uid = quote.peerId if quote else reply.getSenderId()
                                sender = MessagesController.getInstance(acc).getUser(Long.valueOf(uid))
                                if sender:
                                    body = str(quote.getText()) if quote else str(reply.messageText or "")
                                    hdr = str(sender.first_name or "")
                                    header = f"{hdr}\n{body}\n"
                                    is_media = bool(getattr(p, "photo", None) or getattr(p, "document", None) or getattr(p, "path", None))
                                    existing = (p.caption if is_media else p.message) or ""
                                    combined = header + existing
                                    shift = len(header)
                                    new_ents = ArrayList()
                                    bq = TLRPC.TL_messageEntityBlockquote()
                                    bq.offset = 0
                                    bq.length = len(header) - 1
                                    try:
                                        bq.collapsed = True
                                    except Exception:
                                        pass
                                    new_ents.add(bq)
                                    mn = TLRPC.TL_inputMessageEntityMentionName()
                                    iu = TLRPC.TL_inputUser()
                                    iu.user_id = sender.id
                                    iu.access_hash = sender.access_hash
                                    mn.user_id = iu
                                    mn.offset = 0
                                    mn.length = len(hdr)
                                    new_ents.add(mn)
                                    if p.entities:
                                        for i in range(p.entities.size()):
                                            ent = p.entities.get(i)
                                            try:
                                                ent.offset = int(ent.offset) + shift
                                            except Exception:
                                                continue
                                            new_ents.add(ent)
                                    p.entities = new_ents
                                    if is_media:
                                        p.caption = combined
                                    else:
                                        p.message = combined
                                    p.replyToMsg = top if top else None
                                    p.replyToTopMsg = top if top else None
                                    p.replyQuote = None
                            except Exception:
                                pass
                    if plugin._silent_active():
                        p.notify = False
                    if plugin._gs("fake_premium", False) and p.entities:
                        try:
                            acc = UserConfig.selectedAccount
                            gids = set()
                            peer_val = getattr(p, "peer", 0)
                            if peer_val and int(peer_val) < 0:
                                try:
                                    cf = MessagesController.getInstance(acc).getChatFull(jlong(-int(peer_val)))
                                    if cf and cf.emojiset:
                                        ss = MediaDataController.getInstance(acc).getGroupStickerSetById(cf.emojiset)
                                        if ss and ss.documents:
                                            for doc in _jlist(ss.documents):
                                                gids.add(int(doc.id))
                                except Exception:
                                    pass
                            ents = p.entities
                            for i in range(ents.size()):
                                ent = ents.get(i)
                                if isinstance(ent, TLRPC.TL_messageEntityCustomEmoji):
                                    if int(ent.document_id) not in gids:
                                        ue = TLRPC.TL_messageEntityTextUrl()
                                        ue.offset = ent.offset
                                        ue.length = ent.length
                                        ue.url = f"tg://emoji?id={ent.document_id}"
                                        ents.set(i, ue)
                        except Exception:
                            pass
                except Exception:
                    pass
        SMH = find_class("org.telegram.messenger.SendMessagesHelper")
        SMP = find_class("org.telegram.messenger.SendMessagesHelper$SendMessageParams")
        m = SMH.getClass().getDeclaredMethod("sendMessage", SMP)
        m.setAccessible(True)
        u = self.hook_method(m, H())
        if u:
            self._unhooks.append(u)

    def _hk_notif_guard(self):
        plugin = self
        class H(MethodHook):
            def before_hooked_method(self, param):
                if plugin._gs("save_deleted", True):
                    try:
                        if not bool(param.args[1]):
                            param.setResult(None)
                    except Exception:
                        pass
        try:
            NCC = find_class("org.telegram.messenger.NotificationsController")
            m = _fm(NCC.getClass(), "removeDeletedMessagesFromNotifications", 2)
            if m:
                u = self.hook_method(m, H())
                if u:
                    self._unhooks.append(u)
        except Exception:
            pass

    def _dialog_scope(self, did):
        # 'private' | 'group' | 'channel' по dialog_id (из elyx)
        try:
            if did > 0:
                return "private"
            acc = int(UserConfig.selectedAccount)
            chat = MessagesController.getInstance(acc).getChat(jlong(-int(did)))
            if chat is None:
                return "group"
            if bool(ChatObject.isChannel(chat)) and not bool(chat.megagroup):
                return "channel"
            return "group"
        except Exception:
            return "group"

    def _local_read_scope_ok(self, did):
        scope = self._dialog_scope(did)
        return self._gs("local_read_" + scope, True)

    def _hk_local_read(self):
        # Из elyx: управляет ЛОКАЛЬНОЙ отметкой прочтения. Пока призрак включён,
        # можно запретить помечать чаты/сторис прочитанными даже локально
        # (настройки local_read_*), сервер при этом ничего не получает.
        plugin = self

        class MarkDialogHook(MethodHook):
            def before_hooked_method(self, param):
                try:
                    if not plugin._is_ghost():
                        return
                    if not plugin._gs("local_read_msgs", True):
                        param.setResult(None)
                        return
                    try:
                        did = int(param.args[0] or 0)
                    except Exception:
                        did = 0
                    if did and not plugin._local_read_scope_ok(did):
                        param.setResult(None)
                except Exception:
                    pass

        class StoryReadHook(MethodHook):
            def before_hooked_method(self, param):
                try:
                    if plugin._is_ghost() and not plugin._gs("local_read_stories", True):
                        param.setResult(Boolean(False))
                except Exception:
                    pass

        try:
            mc_cls = MessagesController.getInstance(int(UserConfig.selectedAccount)).getClass()
            for mname in ("markDialogAsRead",):
                for mm in mc_cls.getDeclaredMethods():
                    if str(mm.getName()) == mname:
                        mm.setAccessible(True)
                        u = self.hook_method(mm, MarkDialogHook())
                        if u:
                            self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] localRead MC: {e}")
        try:
            sc_cls = None
            for name in ["org.telegram.messenger.StoriesController", "org.telegram.ui.Stories.StoriesController"]:
                try:
                    sc_cls = jclass(name).getClass()
                    break
                except Exception:
                    pass
            if sc_cls is not None:
                for mm in sc_cls.getDeclaredMethods():
                    if str(mm.getName()) == "markStoryAsRead":
                        mm.setAccessible(True)
                        u = self.hook_method(mm, StoryReadHook())
                        if u:
                            self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] localRead SC: {e}")

    def _hk_self_online_label(self):
        # Из elyx: при включённом призраке собственный статус везде
        # ("онлайн" в шапке чата, в списке и т.д.) показывается как "оффлайн".
        plugin = self

        class SelfStatusHook(MethodHook):
            def after_hooked_method(self, param):
                try:
                    if not plugin._is_ghost():
                        return
                    user = param.args[1] if param.args and len(param.args) > 1 else None
                    if user is None:
                        return
                    try:
                        me = UserConfig.getCurrentUser()
                        if me is None or user.id != me.id:
                            return
                    except Exception:
                        return
                    res = param.getResult()
                    if res is None:
                        return
                    s = str(res)
                    online_str = None
                    try:
                        from org.telegram.messenger import R as TGR
                        online_str = str(LocaleController.getString("Online", TGR.string.Online))
                    except Exception:
                        online_str = "Online"
                    if s == online_str:
                        param.setResult(L.g("ghost_status_offline"))
                except Exception:
                    pass

        try:
            lc_cls = LocaleController.getInstance().getClass()
            for mm in lc_cls.getDeclaredMethods():
                if str(mm.getName()) == "formatUserStatus":
                    mm.setAccessible(True)
                    u = self.hook_method(mm, SelfStatusHook())
                    if u:
                        self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] selfStatus: {e}")

    def _own_status_apply_text(self, views, text):
        try:
            self._own_status_block = False
            for v in views:
                try:
                    if v is not None:
                        v.setText(text, False)
                except Exception:
                    pass
            self._own_status_block = True
            self._own_status_last_text = text
        except Exception:
            pass

    def _own_status_text_server(self, response, error, views):
        try:
            if response is not None and hasattr(response, "objects"):
                objs = list(response.objects)
                if objs:
                    me = objs[0]
                    st = getattr(me, "status", None)
                    if st is not None:
                        cls_name = str(st.toString()) if hasattr(st, "toString") else ""
                        try:
                            expires = int(getattr(st, "expires", 0) or 0)
                        except Exception:
                            expires = 0
                        if "Online" in cls_name:
                            txt = str(LocaleController.formatUserStatus(int(UserConfig.selectedAccount), me))
                        else:
                            try:
                                txt = str(LocaleController.formatDateTime(expires, True))
                            except Exception:
                                txt = L.g("ghost_status_offline")
                        self._own_status_apply_text(views, txt)
                        return
            self._own_status_apply_text(views, L.g("ghost_status_offline"))
        except Exception:
            pass

    def _own_status_refresh(self, views):
        try:
            now = _time.time()
            if now - self._own_status_last_req > 5.0:
                try:
                    self._own_status_last_req = now
                    me = UserConfig.getCurrentUser()
                    if me is None:
                        return False
                    mc = MessagesController.getInstance(int(UserConfig.selectedAccount))
                    iu = mc.getInputUser(me)
                    if iu is None:
                        return False
                    rtw = TLRPC.TL_users_getUsers()
                    rtw.id.add(iu)
                    send_request(rtw, RequestCallback(
                        lambda r, e, v=views: self._own_status_text_server(r, e, v)))
                    return True
                except Exception:
                    return False
            if self._own_status_last_text is not None:
                self._own_status_apply_text(views, self._own_status_last_text)
            return True
        except Exception:
            return False

    def _hk_own_profile_status(self):
        # Из elyx: в собственном профиле при призраке показывается реальный
        # статус (запрашивается с сервера), а не "онлайн". Нативную перерисовку
        # статуса блокируем через setText-хук отслеживаемых вью.
        plugin = self
        try:
            pa_cls = jclass("org.telegram.ui.ProfileActivity").getClass()

            class TextSetHook(MethodHook):
                def before_hooked_method(self, param):
                    try:
                        if getattr(plugin, "_own_status_block", False):
                            tracked = getattr(plugin, "_own_status_views", None)
                            if tracked and param.thisObject in tracked:
                                param.setResult(Boolean(False))
                    except Exception:
                        pass

            class PDHook(MethodHook):
                def after_hooked_method(self, param):
                    try:
                        act = param.thisObject
                        if not plugin._is_ghost():
                            plugin._own_status_block = False
                            return
                        acc = int(act.getCurrentAccount())
                        uid = int(get_private_field(act, "userId") or 0)
                        if uid != int(UserConfig.getInstance(acc).getClientUserId()):
                            return
                        views = get_private_field(act, "onlineTextView")
                        if views is None or len(views) == 0:
                            return
                        tracked = set()
                        for v in views:
                            if v is not None:
                                tracked.add(v)
                        plugin._own_status_views = tracked
                        plugin._own_status_block = True
                        if not plugin._own_status_refresh(views):
                            plugin._own_status_apply_text(views, L.g("ghost_status_offline"))
                    except Exception:
                        pass

            for mm in pa_cls.getDeclaredMethods():
                if str(mm.getName()) == "updateProfileData":
                    mm.setAccessible(True)
                    u = self.hook_method(mm, PDHook())
                    if u:
                        self._unhooks.append(u)
            stv_cls = jclass("org.telegram.ui.ActionBar.SimpleTextView").getClass()
            for mm in stv_cls.getDeclaredMethods():
                if str(mm.getName()) == "setText":
                    mm.setAccessible(True)
                    u = self.hook_method(mm, TextSetHook())
                    if u:
                        self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] ownProfile: {e}")

    def _hk_sched_no_redirect(self):
        # Из elyx: при ghost + "использовать отложку" Telegram после отправки
        # пытается открыть вкладку отложенных сообщений — блокируем только её,
        # а не все переходы между фрагментами.
        plugin = self

        class NoRedirectHook(MethodHook):
            def before_hooked_method(self, param):
                try:
                    args = param.args
                    if args is not None and len(args) == 2:
                        try:
                            cnt = int(args[0] or 0)
                        except Exception:
                            cnt = 0
                        if cnt != 0 and plugin._is_ghost() and plugin._gs("use_scheduled", False):
                            param.setResult(None)
                except Exception:
                    pass

        try:
            for mm in CA_CLS.getDeclaredMethods():
                if str(mm.getName()) == "openScheduledMessages":
                    mm.setAccessible(True)
                    u = self.hook_method(mm, NoRedirectHook())
                    if u:
                        self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] schedNoRedirect: {e}")

    def _hk_keep_once(self):
        plugin = self
        class MDH(MethodHook):
            def before_hooked_method(self, param):
                if plugin._gs("keep_once", False):
                    try:
                        mo = param.args[0]
                        if mo:
                            mo.forceExpired = False
                    except Exception:
                        pass
                    param.setResult(None)
        class MRH(MethodHook):
            def before_hooked_method(self, param):
                if plugin._gs("keep_once", False):
                    param.setResult(None)
        m = _fm(CA_CLS, "sendSecretMediaDelete", 1)
        if m:
            u = self.hook_method(m, MDH())
            if u:
                self._unhooks.append(u)
        m = _fm(CA_CLS, "sendSecretMessageRead", 2)
        if m:
            u = self.hook_method(m, MRH())
            if u:
                self._unhooks.append(u)
        try:
            svp = jclass("org.telegram.ui.SecretVoicePlayer").getClass()
            class DH(MethodHook):
                def before_hooked_method(self, param):
                    if plugin._gs("keep_once", False):
                        try:
                            f = _ff(svp, "closeAction")
                            if f:
                                f.set(param.thisObject, None)
                        except Exception:
                            pass
            m = _fm(svp, "dismiss", 0)
            if m:
                u = self.hook_method(m, DH())
                if u:
                    self._unhooks.append(u)
        except Exception:
            pass
        mc = get_messages_controller()
        if mc:
            class CT(MethodReplacement):
                def replace_hooked_method(self, param):
                    if plugin._gs("keep_once", False):
                        return Boolean(False)
                    try:
                        return param.invokeOriginalMethod()
                    except Exception:
                        return Boolean(False)
            m = _fm(mc.getClass(), "checkDeletingTask", 1)
            if m:
                u = self.hook_method(m, CT())
                if u:
                    self._unhooks.append(u)

    def _hk_screenshots(self):
        plugin = self
        try:
            fsr = jclass("org.telegram.messenger.FlagSecureReason").getClass()
            class AH(MethodHook):
                def before_hooked_method(self, param):
                    if plugin._gs("allow_screenshots", False):
                        param.setResult(None)
            m = _fm(fsr, "attach")
            if m:
                u = self.hook_method(m, AH())
                if u:
                    self._unhooks.append(u)
        except Exception:
            pass

    def _hk_noforward(self):
        plugin = self
        class RF(MethodHook):
            def before_hooked_method(self, param):
                if plugin._gs("bypass_noforward", False):
                    param.setResult(Boolean(False))
        class RT(MethodHook):
            def before_hooked_method(self, param):
                if plugin._gs("bypass_noforward", False):
                    param.setResult(Boolean(True))
        MCC = find_class("org.telegram.messenger.MessagesController")
        CC = find_class("org.telegram.tgnet.TLRPC$Chat")
        for name in ["isChatNoForwards", "isUserNoForwards", "isPeerNoForwards"]:
            for sig in [[CC], [Long.TYPE], []]:
                try:
                    m = MCC.getClass().getDeclaredMethod(name, *sig)
                    m.setAccessible(True)
                    u = self.hook_method(m, RF())
                    if u:
                        self._unhooks.append(u)
                except Exception:
                    pass
        try:
            MOC = jclass("org.telegram.messenger.MessageObject").getClass()
            m = _fm(MOC, "canForwardMessage")
            if m:
                u = self.hook_method(m, RT())
                if u:
                    self._unhooks.append(u)
        except Exception:
            pass
        m = _fm(CA_CLS, "hasSelectedNoforwardsMessage")
        if m:
            u = self.hook_method(m, RF())
            if u:
                self._unhooks.append(u)
        class DropNoFwd(MethodHook):
            def before_hooked_method(self, param):
                if not plugin._gs("bypass_noforward", False):
                    return
                try:
                    rq = param.args[0]
                    if rq and hasattr(rq, "drop_author"):
                        rq.drop_author = True
                    if rq and hasattr(rq, "noforwards"):
                        rq.noforwards = False
                except Exception:
                    pass
        try:
            SMH = find_class("org.telegram.messenger.SendMessagesHelper")
            for mm in SMH.getClass().getDeclaredMethods():
                if str(mm.getName()) == "sendMessage" and len(mm.getParameterTypes()) == 1:
                    ptype = str(mm.getParameterTypes()[0].getName())
                    if "SendMessageParams" in ptype:
                        mm.setAccessible(True)
                        class DropAuthor(MethodHook):
                            def before_hooked_method(self, param):
                                if not plugin._gs("bypass_noforward", False):
                                    return
                                try:
                                    p = param.args[0]
                                    if p and hasattr(p, "noforwards"):
                                        p.noforwards = False
                                except Exception:
                                    pass
                        u = self.hook_method(mm, DropAuthor())
                        if u:
                            self._unhooks.append(u)
                        break
        except Exception:
            pass
        try:
            class FwdHook(MethodHook):
                def before_hooked_method(self, param):
                    if not plugin._gs("bypass_noforward", False):
                        return
                    try:
                        rq = param.args[0]
                        if hasattr(rq, "drop_author"):
                            rq.drop_author = True
                    except Exception:
                        pass
            mc = get_messages_controller()
            if mc:
                for mm in mc.getClass().getDeclaredMethods():
                    if str(mm.getName()) == "sendMessagesForward":
                        mm.setAccessible(True)
                        u = self.hook_method(mm, FwdHook())
                        if u:
                            self._unhooks.append(u)
        except Exception:
            pass
        self._hook_reforward()

    def _is_noforward_msg(self, mo):
        try:
            owner = mo.messageOwner
            if not owner:
                return False
            if bool(getattr(owner, "noforwards", False)):
                return True
            try:
                if bool(mo.hasRevealedExtendedMedia()):
                    return True
            except Exception:
                pass
            if _int(getattr(owner, "ttl", 0)) != 0:
                return True
            acc = int(UserConfig.selectedAccount)
            mc = MessagesController.getInstance(acc)
            did = int(mo.getDialogId())
            if did == 489000:
                return True
            chat = mc.getChat(Long(did))
            if chat is None:
                chat = mc.getChat(Long(-did))
            if chat is not None:
                if bool(getattr(chat, "noforwards", False)):
                    return True
                migrated = getattr(chat, "migrated_to", None)
                if migrated:
                    cid = _int(getattr(migrated, "channel_id", 0))
                    if cid:
                        chat2 = mc.getChat(Long(cid))
                        if chat2 and bool(getattr(chat2, "noforwards", False)):
                            return True
            elif did > 0:
                try:
                    uf = mc.getUserFull(jlong(did))
                    if uf and (bool(getattr(uf, "noforwards_my_enabled", False)) or bool(getattr(uf, "noforwards_peer_enabled", False))):
                        return True
                except Exception:
                    pass
            try:
                if mo.isSecret() or mo.isSecretMedia() or mo.isVoiceOnce() or mo.isRoundOnce():
                    return True
            except Exception:
                pass
            if self.vault and self.vault.is_removed_obj(mo):
                return True
            return False
        except Exception:
            return False

    def _get_file_path(self, mo):
        try:
            acc = int(UserConfig.selectedAccount)
            fl = FileLoader.getInstance(acc)
            owner = mo.messageOwner
            attach_path = getattr(owner, "attachPath", None)
            if attach_path:
                ap = str(attach_path)
                if ap:
                    JFile = jclass("java.io.File")
                    af = JFile(ap)
                    if af.exists() and not af.isDirectory():
                        return ap
            f = fl.getPathToMessage(owner)
            if f and f.exists() and not f.isDirectory():
                return str(f.getAbsolutePath())
            doc = mo.getDocument()
            if doc:
                for b in [False, True]:
                    f = fl.getPathToAttach(doc, b)
                    if f and f.exists() and not f.isDirectory():
                        return str(f.getAbsolutePath())
            try:
                photo = MessageObject.getPhoto(owner)
                if photo:
                    ps = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize())
                    if ps:
                        for b in [False, True]:
                            f = fl.getPathToAttach(ps, b)
                            if f and f.exists() and not f.isDirectory():
                                return str(f.getAbsolutePath())
            except Exception:
                pass
        except Exception:
            pass
        return ""

    def _map_document(self, acc, mo, doc, file_path):
        try:
            new_doc = TLRPC.TL_document()
            new_doc.flags = doc.flags
            new_doc.file_reference = jarray(jbyte)(0)
            new_doc.dc_id = 0
            new_doc.user_id = doc.user_id
            new_doc.version = doc.version
            new_doc.mime_type = str(doc.mime_type or "")
            new_doc.file_name = str(getattr(doc, "file_name", "") or "")
            new_doc.file_name_fixed = str(getattr(doc, "file_name_fixed", "") or "")
            new_doc.date = int(ConnectionsManager.getInstance(acc).getCurrentTime())
            JFile = jclass("java.io.File")
            jf = JFile(file_path)
            new_doc.size = jf.length() if jf.exists() else doc.size
            new_doc.thumbs = doc.thumbs
            new_doc.video_thumbs = doc.video_thumbs
            new_doc.localThumbPath = getattr(doc, "localThumbPath", None)
            new_doc.attributes = doc.attributes
            new_doc.localPath = file_path
            try:
                if mo.isGif() or mo.isNewGif():
                    new_doc.mime_type = "video/mp4"
            except Exception:
                pass
            return new_doc
        except Exception as e:
            log(f"[MAX] mapDoc: {e}")
            return doc

    def _map_photo(self, acc, photo, file_path):
        try:
            smh = SendMessagesHelper.getInstance(acc)
            new_photo = smh.generatePhotoSizes(file_path, None)
            if new_photo:
                new_photo.flags = photo.flags
                new_photo.has_stickers = photo.has_stickers
                new_photo.date = int(ConnectionsManager.getInstance(acc).getCurrentTime())
                new_photo.geo = getattr(photo, "geo", None)
                new_photo.caption = getattr(photo, "caption", None)
                return new_photo
        except Exception as e:
            log(f"[MAX] mapPhoto: {e}")
        return photo

    def _send_non_media(self, ai, mo, peer, notify, schedule_date, reply_top):
        try:
            acc = int(UserConfig.selectedAccount)
            owner = mo.messageOwner
            media = getattr(owner, "media", None)
            mn = str(media.getClass().getSimpleName()) if media else ""
            smh = ai.getSendMessagesHelper()
            SMP = jclass("org.telegram.messenger.SendMessagesHelper$SendMessageParams")
            if any(x in mn for x in ["Geo", "Venue"]):
                smh.sendMessage(SMP.of(media, jlong(peer), reply_top, reply_top, None, None, bool(notify), int(schedule_date), 0))
            elif "Contact" in mn:
                u = TLRPC.TL_user()
                u.id = int(getattr(media, "user_id", 0))
                u.first_name = str(getattr(media, "first_name", "") or "")
                u.last_name = str(getattr(media, "last_name", "") or "")
                u.phone = str(getattr(media, "phone_number", "") or "")
                smh.sendMessage(SMP.of(u, jlong(peer), reply_top, reply_top, None, None, bool(notify), int(schedule_date), 0))
            elif "Poll" in mn:
                smh.sendMessage(SMP.of(media, jlong(peer), reply_top, reply_top, None, None, bool(notify), int(schedule_date), 0))
            elif "Document" in mn and getattr(media, "document", None):
                doc = media.document
                is_stk = bool(MessageObject.isStickerMessage(owner)) or bool(MessageObject.isAnimatedStickerMessage(owner))
                try:
                    is_stk = is_stk or bool(MessageObject.isVideoSticker(doc))
                except Exception:
                    pass
                if is_stk:
                    try:
                        smh.sendSticker(doc, None, jlong(peer), reply_top, reply_top, None, None, None, bool(notify), int(schedule_date), 0, False, mo, None, 0, jlong(0), jlong(0), None)
                    except Exception:
                        try:
                            smh.sendSticker(doc, None, jlong(peer), reply_top, reply_top, None, None, None, bool(notify), int(schedule_date), 0, False, mo, None, 0, jlong(0), jlong(0))
                        except Exception:
                            pass
                else:
                    path = self._get_file_path(mo)
                    if path:
                        mapped = self._map_document(acc, mo, doc, path)
                        caption = str(owner.message or "")
                        ents = getattr(owner, "entities", None)
                        if ents is not None and not isinstance(ents, ArrayList):
                            ents = _to_al(_jlist(ents))
                        has_spoiler = False
                        try:
                            has_spoiler = bool(mo.hasMediaSpoilers()) and not bool(mo.isHiddenSensitive())
                        except Exception:
                            pass
                        invert = bool(getattr(owner, "invert_media", False))
                        smh.sendMessage(SMP.of(mapped, None, path, jlong(peer), reply_top, reply_top, caption, ents, None, None, bool(notify), int(schedule_date), 0, 0, mo, None, False, has_spoiler))
                    else:
                        text = str(owner.message or "")
                        if text:
                            ents = getattr(owner, "entities", None)
                            if ents is not None and not isinstance(ents, ArrayList):
                                ents = _to_al(_jlist(ents))
                            smh.sendMessage(SMP.of(text, jlong(peer), reply_top, reply_top, None, True, ents, None, None, bool(notify), int(schedule_date), 0, None, False))
            else:
                text = str(owner.message or "")
                if text:
                    ents = getattr(owner, "entities", None)
                    if ents is not None and not isinstance(ents, ArrayList):
                        ents = _to_al(_jlist(ents))
                    smh.sendMessage(SMP.of(text, jlong(peer), reply_top, reply_top, None, True, ents, None, None, bool(notify), int(schedule_date), 0, None, False))
        except Exception as e:
            log(f"[MAX] non-media fwd: {e}")

    def _send_message_copies(self, ai, messages, peer, notify, schedule_date, reply_top):
        # Bug fixes (#2 "use_scheduled" and #3 "silent mute doesn't work"): these
        # no-forward-bypass / restore-deleted copy sends used to just forward the
        # notify/schedule_date values from the original intercepted request as-is,
        # completely ignoring the user's silent_send and use_scheduled settings.
        # Apply the same effective values the normal send pipeline (pre_request_hook)
        # would use, so muting/scheduling actually behaves consistently here too.
        try:
            silent_mode = _int(self._gs("silent_send", 0), 0)
            if silent_mode == 1 or (silent_mode == 2 and self._is_ghost()):
                notify = False
        except Exception:
            pass
        try:
            if not schedule_date and self._gs("use_scheduled", False):
                schedule_date = int(_time.time()) + 15
                log(f"[MAX] _send_message_copies: use_scheduled=True, setting schedule_date={schedule_date}")
        except Exception:
            pass
        grouped = {}
        singles = []
        for mo in messages:
            gid = int(mo.getGroupId())
            if gid != 0:
                grouped.setdefault(gid, []).append(mo)
            else:
                singles.append(mo)
        for group in grouped.values():
            try:
                self._send_group(ai, group, peer, notify, schedule_date, reply_top)
            except Exception as e:
                log(f"[MAX] group send: {e}")
        if singles:
            try:
                self._send_group(ai, singles, peer, notify, schedule_date, reply_top)
            except Exception as e:
                log(f"[MAX] singles send: {e}")

    def _send_group(self, ai, messages, peer, notify, schedule_date, reply_top):
        if not messages:
            return
        acc = int(UserConfig.selectedAccount)
        is_grouped = len(messages) > 1
        media_infos = []
        try:
            SMI = jclass("org.telegram.messenger.SendMessagesHelper$SendingMediaInfo")
        except Exception:
            for mo in messages:
                self._send_non_media(ai, mo, peer, notify, schedule_date, reply_top)
            return
        for i, mo in enumerate(messages):
            owner = mo.messageOwner
            media = getattr(owner, "media", None)
            mn = str(media.getClass().getSimpleName()) if media else ""
            is_photo = "Photo" in mn
            doc = getattr(media, "document", None) if media else None
            is_video = False
            is_doc_file = False
            if "Document" in mn and doc:
                is_stk = False
                try:
                    is_stk = bool(MessageObject.isStickerMessage(owner)) or bool(MessageObject.isAnimatedStickerMessage(owner))
                    try:
                        is_stk = is_stk or bool(MessageObject.isVideoSticker(doc))
                    except Exception:
                        pass
                except Exception:
                    pass
                is_voice = bool(MessageObject.isVoiceMessage(owner))
                is_round = bool(MessageObject.isRoundVideoMessage(owner))
                is_vid = bool(MessageObject.isVideoDocument(doc))
                try:
                    is_vid = is_vid and not bool(MessageObject.isVideoSticker(doc))
                except Exception:
                    pass
                if is_vid:
                    is_video = True
                elif not is_stk and not is_voice and not is_round:
                    is_doc_file = True
            if is_photo or is_video or is_doc_file:
                path = self._get_file_path(mo)
                if not path:
                    self._download_then_send(ai, mo, peer, notify, schedule_date, reply_top)
                    continue
                if is_doc_file and not is_grouped:
                    caption = str(owner.message or "")
                    try:
                        SendMessagesHelper.prepareSendingDocument(ai, path, path, None, caption, None, jlong(peer), reply_top, reply_top, None, None, None, bool(notify), int(schedule_date), None, None, 0, False)
                    except Exception as e:
                        log(f"[MAX] doc copy: {e}")
                    continue
                if is_doc_file:
                    info = SMI()
                    info.path = path
                    if i == 0 and owner.message:
                        info.caption = str(owner.message)
                        ents = getattr(owner, "entities", None)
                        info.entities = _to_al(_jlist(ents)) if ents else ArrayList()
                    info.isVideo = False
                    try:
                        info.forceDocument = True
                    except Exception:
                        pass
                    media_infos.append(info)
                else:
                    info = SMI()
                    info.path = path
                    if i == 0 and owner.message:
                        info.caption = str(owner.message)
                        ents = getattr(owner, "entities", None)
                        info.entities = _to_al(_jlist(ents)) if ents else ArrayList()
                    info.isVideo = bool(is_video)
                    media_infos.append(info)
            else:
                self._send_non_media(ai, mo, peer, notify, schedule_date, reply_top)
        if media_infos:
            al = _to_al(media_infos)
            try:
                SendMessagesHelper.prepareSendingMedia(ai, al, jlong(peer), reply_top, reply_top, None, None, False, bool(is_grouped), None, bool(notify), int(schedule_date), 0, 0, False, None, None, 0, jlong(0), False, jlong(0), jlong(0), None)
            except Exception:
                try:
                    SendMessagesHelper.prepareSendingMedia(ai, al, jlong(peer), reply_top, reply_top, None, None, False, bool(is_grouped), None, bool(notify), int(schedule_date), 0, 0, False, None, None, 0, jlong(0), False, jlong(0), jlong(0))
                except Exception:
                    for info in media_infos:
                        try:
                            p = info.path
                            SendMessagesHelper.prepareSendingDocument(ai, p, p, None, str(getattr(info, "caption", "") or ""), None, jlong(peer), reply_top, reply_top, None, None, None, bool(notify), int(schedule_date), None, None, 0, False)
                        except Exception as e:
                            log(f"[MAX] fallback doc: {e}")

    def _download_then_send(self, ai, mo, peer, notify, schedule_date, reply_top):
        plugin = self
        acc = int(UserConfig.selectedAccount)
        fl = FileLoader.getInstance(acc)
        owner = mo.messageOwner
        media = getattr(owner, "media", None)
        if not media:
            self._send_non_media(ai, mo, peer, notify, schedule_date, reply_top)
            return
        doc = getattr(media, "document", None)
        photo = getattr(media, "photo", None)
        if doc:
            fl.loadFile(doc, mo, 3, 0)
        elif photo:
            try:
                ps = FileLoader.getClosestPhotoSizeWithSize(photo.sizes, AndroidUtilities.getPhotoSize())
                if ps:
                    fl.loadFile(ImageLocation.getForPhoto(ps, photo), mo, None, 3, 0)
            except Exception:
                pass
        def _retry():
            _time.sleep(3)
            path = plugin._get_file_path(mo)
            if path:
                run_on_ui_thread(lambda: plugin._send_group(ai, [mo], peer, notify, schedule_date, reply_top))
            else:
                _time.sleep(5)
                path2 = plugin._get_file_path(mo)
                if path2:
                    run_on_ui_thread(lambda: plugin._send_group(ai, [mo], peer, notify, schedule_date, reply_top))
                else:
                    run_on_ui_thread(lambda: plugin._send_non_media(ai, mo, peer, notify, schedule_date, reply_top))
        threading.Thread(target=_retry, daemon=True).start()


    def _hook_reforward(self):
        plugin = self
        class ForwardHook(MethodHook):
            def before_hooked_method(self, param):
                if plugin._forward_copying:
                    return
                if not plugin._gs("bypass_noforward", False):
                    return
                if _int(plugin._gs("fwd_mode", 0)) == 1:
                    return
                try:
                    objects = _jlist(param.args[0])
                    if not objects:
                        return
                    no_fwd = []
                    regular = []
                    for mo in objects:
                        if plugin._is_noforward_msg(mo):
                            no_fwd.append(mo)
                        else:
                            regular.append(mo)
                    if not no_fwd:
                        return
                    peer = int(param.args[1])
                    notify = bool(param.args[4])
                    schedule_date = int(param.args[5])
                    reply_top = None
                    ptypes = param.method.getParameterTypes()
                    for i in range(6, len(param.args)):
                        try:
                            if "MessageObject" in str(ptypes[i].getName()) and param.args[i] is not None:
                                reply_top = param.args[i]
                                break
                        except Exception:
                            pass
                    if regular:
                        param.args[0] = _to_al(regular)
                    else:
                        param.setResult(Integer(0))
                    ai = AccountInstance.getInstance(int(UserConfig.selectedAccount))
                    plugin._forward_copying = True
                    try:
                        plugin._send_message_copies(ai, no_fwd, peer, notify, schedule_date, reply_top)
                    except Exception as e2:
                        log(f"[MAX] copy send: {e2}")
                    finally:
                        plugin._forward_copying = False
                except Exception as e:
                    log(f"[MAX] reforward hook: {e}")
        try:
            SMH_CLS = jclass("org.telegram.messenger.SendMessagesHelper").getClass()
            for method in SMH_CLS.getDeclaredMethods():
                try:
                    types = method.getParameterTypes()
                    if str(method.getName()) == "sendMessage" and len(types) >= 7 and str(types[0].getName()) == "java.util.ArrayList" and str(types[1].getName()) == "long":
                        method.setAccessible(True)
                        u = self.hook_method(method, ForwardHook())
                        if u:
                            self._unhooks.append(u)
                except Exception:
                    pass
        except Exception as e:
            log(f"[MAX] reforward setup: {e}")

    def _is_removed_any(self, mo):
        try:
            did = int(mo.getDialogId())
            mid = int(mo.getId())
            if self.vault and (did, mid) in self.vault._removed:
                return True
            if self.dex and self.dex.loaded:
                try:
                    r = self.dex.call("pyIsRemoved", jlong(did), jint(mid))
                    if r is not None and bool(r):
                        return True
                except Exception:
                    pass
        except Exception:
            pass
        return False

    def _hk_forward_deleted(self):
        plugin = self
        class ForwardDeletedHook(MethodHook):
            def before_hooked_method(self, param):
                if plugin._forward_copying:
                    return
                try:
                    objects = _jlist(param.args[0])
                    if not objects:
                        return
                    deleted = []
                    regular = []
                    for mo in objects:
                        try:
                            if plugin._is_removed_any(mo):
                                deleted.append(mo)
                            else:
                                regular.append(mo)
                        except Exception:
                            regular.append(mo)
                    if not deleted:
                        return
                    peer = int(param.args[1])
                    notify = bool(param.args[4])
                    schedule_date = int(param.args[5])
                    reply_top = None
                    ptypes = param.method.getParameterTypes()
                    for i in range(6, len(param.args)):
                        try:
                            if "MessageObject" in str(ptypes[i].getName()) and param.args[i] is not None:
                                reply_top = param.args[i]
                                break
                        except Exception:
                            pass
                    if regular:
                        param.args[0] = _to_al(regular)
                    else:
                        param.setResult(Integer(0))
                    ai = AccountInstance.getInstance(int(UserConfig.selectedAccount))
                    plugin._forward_copying = True
                    try:
                        plugin._send_message_copies(ai, deleted, peer, notify, schedule_date, reply_top)
                    except Exception as e2:
                        log(f"[MAX] fwd deleted: {e2}")
                    finally:
                        plugin._forward_copying = False
                except Exception as e:
                    log(f"[MAX] fwd deleted hook: {e}")
        try:
            SMH_CLS = jclass("org.telegram.messenger.SendMessagesHelper").getClass()
            for method in SMH_CLS.getDeclaredMethods():
                try:
                    types = method.getParameterTypes()
                    if str(method.getName()) == "sendMessage" and len(types) >= 7 and str(types[0].getName()) == "java.util.ArrayList" and str(types[1].getName()) == "long":
                        method.setAccessible(True)
                        u = self.hook_method(method, ForwardDeletedHook())
                        if u:
                            self._unhooks.append(u)
                except Exception:
                    pass
        except Exception as e:
            log(f"[MAX] fwd deleted setup: {e}")

    def _hk_story_save(self):
        plugin = self
        try:
            hc = jclass("org.telegram.ui.Stories.PeerStoriesView$StoryItemHolder").getClass()
            class AS(MethodHook):
                def before_hooked_method(self, param):
                    if plugin._gs("bypass_story_save", False):
                        param.setResult(Boolean(True))
            m = _fm(hc, "allowScreenshots", 0)
            if m:
                u = self.hook_method(m, AS())
                if u:
                    self._unhooks.append(u)
        except Exception:
            pass
        try:
            sv = jclass("org.telegram.ui.Stories.StoryViewer").getClass()
            class VS(MethodHook):
                def before_hooked_method(self, param):
                    if plugin._gs("bypass_story_save", False):
                        try:
                            if len(param.args) >= 1:
                                param.args[0] = Boolean(True)
                        except Exception:
                            pass
            m = _fm(sv, "allowScreenshots", 1)
            if m:
                u = self.hook_method(m, VS())
                if u:
                    self._unhooks.append(u)
        except Exception:
            pass

    def _hk_media_seen(self):
        plugin = self
        mc = get_messages_controller()
        if not mc:
            return
        class BS(MethodHook):
            def before_hooked_method(self, param):
                if plugin._is_ghost() and plugin._gs("ghost_no_read", True):
                    param.setResult(None)
        for mm in mc.getClass().getDeclaredMethods():
            if str(mm.getName()) == "markMessageContentAsRead":
                mm.setAccessible(True)
                u = self.hook_method(mm, BS())
                if u:
                    self._unhooks.append(u)

    def _mc_acc(self, mc):
        try:
            return int(mc.currentAccount)
        except Exception:
            return UserConfig.selectedAccount

    def _hk_fake_premium(self):
        plugin = self

        def _load_cached_es(acc=None, uid=None):
            try:
                return _dict_to_emoji(_safe_read_json(_emoji_cache_path(acc=acc, uid=uid)))
            except Exception:
                return None

        def _load_cached_premium(acc=None, uid=None):
            try:
                d = _safe_read_json(_premium_cache_path(acc=acc, uid=uid))
                if not isinstance(d, dict):
                    return (None, None)
                return (_dict_to_peer_color(d.get("color")), _dict_to_peer_color(d.get("profile_color")))
            except Exception:
                return (None, None)

        def _resolve_uc_acc(uc, acc):
            if acc is not None:
                return acc
            try:
                return int(uc.currentAccount)
            except Exception:
                return None

        def _resolve_uc_uid(uc):
            try:
                if uc.isClientActivated():
                    return int(uc.getClientUserId())
            except Exception:
                pass
            try:
                cu = uc.getCurrentUser()
                if cu is not None:
                    return int(cu.id)
            except Exception:
                pass
            return 0

        def _save_emoji_state_direct(acc, uid, es):
            try:
                p = _emoji_cache_path(acc=acc, uid=uid)
                if p is None:
                    return
                d = _emoji_to_dict(es)
                if d is not None:
                    _atomic_write_json(p, d)
            except Exception:
                pass

        def _save_premium_state_direct(acc, uid, color, profile_color):
            try:
                p = _premium_cache_path(acc=acc, uid=uid)
                if p is None:
                    return
                d = {}
                cd = _peer_color_to_dict(color)
                if cd is not None:
                    d["color"] = cd
                pd = _peer_color_to_dict(profile_color)
                if pd is not None:
                    d["profile_color"] = pd
                if d:
                    _atomic_write_json(p, d)
            except Exception:
                pass

        def _save_emoji_state(uc, acc=None):
            try:
                if uc is None:
                    return
                cu = uc.getCurrentUser()
                if cu is None:
                    return
                acc = _resolve_uc_acc(uc, acc)
                uid = _resolve_uc_uid(uc)
                if not uid:
                    return
                p = _emoji_cache_path(acc=acc, uid=uid)
                if p is None:
                    return
                d = _emoji_to_dict(getattr(cu, "emoji_status", None))
                if d is not None:
                    _atomic_write_json(p, d)
            except Exception:
                pass

        def _save_premium_state(uc, acc=None):
            try:
                if uc is None:
                    return
                cu = uc.getCurrentUser()
                if cu is None:
                    return
                acc = _resolve_uc_acc(uc, acc)
                uid = _resolve_uc_uid(uc)
                if not uid:
                    return
                p = _premium_cache_path(acc=acc, uid=uid)
                if p is None:
                    return
                d = {}
                try:
                    cd = _peer_color_to_dict(getattr(cu, "color", None))
                    if cd is not None:
                        d["color"] = cd
                except Exception:
                    pass
                try:
                    pd = _peer_color_to_dict(getattr(cu, "profile_color", None))
                    if pd is not None:
                        d["profile_color"] = pd
                except Exception:
                    pass
                if d:
                    _atomic_write_json(p, d)
            except Exception:
                pass

        def _pick_kept_es(acc, mc, uid):
            try:
                uc = UserConfig.getInstance(acc)
                cu = uc.getCurrentUser() if uc else None
                es = getattr(cu, "emoji_status", None) if cu is not None else None
                if not _is_empty_es(es):
                    return es
            except Exception:
                pass
            try:
                old = mc.getUser(Long(uid)) if mc is not None else None
                es = getattr(old, "emoji_status", None) if old is not None else None
                if not _is_empty_es(es):
                    return es
            except Exception:
                pass
            try:
                es = _load_cached_es(acc=acc, uid=uid)
                if not _is_empty_es(es):
                    return es
            except Exception:
                pass
            return None

        def _pick_kept_color(acc, mc, uid, field):
            try:
                uc = UserConfig.getInstance(acc)
                cu = uc.getCurrentUser() if uc else None
                c = getattr(cu, field, None) if cu is not None else None
                if not _is_empty_color(c):
                    return c
            except Exception:
                pass
            try:
                old = mc.getUser(Long(uid)) if mc is not None else None
                c = getattr(old, field, None) if old is not None else None
                if not _is_empty_color(c):
                    return c
            except Exception:
                pass
            try:
                ca, pa = _load_cached_premium(acc=acc, uid=uid)
                c = ca if field == "color" else pa
                if not _is_empty_color(c):
                    return c
            except Exception:
                pass
            return None

        def _patch_user(user, mc=None, acc=None):
            try:
                if user is None:
                    return
                try:
                    uid = int(user.id)
                except Exception:
                    return
                if acc is None:
                    try:
                        acc = plugin._mc_acc(mc) if mc is not None else UserConfig.selectedAccount
                    except Exception:
                        acc = UserConfig.selectedAccount
                try:
                    my = int(UserConfig.getInstance(acc).getClientUserId())
                except Exception:
                    my = 0
                if uid != my:
                    return
                user.premium = True
                try:
                    if _is_empty_es(getattr(user, "emoji_status", None)):
                        kept = _pick_kept_es(acc, mc, uid)
                        if kept is not None:
                            user.emoji_status = kept
                except Exception:
                    pass
                for field in ("color", "profile_color"):
                    try:
                        if _is_empty_color(getattr(user, field, None)):
                            kept = _pick_kept_color(acc, mc, uid, field)
                            if kept is not None:
                                setattr(user, field, kept)
                    except Exception:
                        pass
            except Exception:
                pass

        def _apply_to_user(u, es, color_c, profile_c):
            if u is None:
                return
            try:
                u.premium = True
            except Exception:
                pass
            try:
                if es is not None and _is_empty_es(getattr(u, "emoji_status", None)):
                    u.emoji_status = es
            except Exception:
                pass
            try:
                if color_c is not None and _is_empty_color(getattr(u, "color", None)):
                    u.color = color_c
            except Exception:
                pass
            try:
                if profile_c is not None and _is_empty_color(getattr(u, "profile_color", None)):
                    u.profile_color = profile_c
            except Exception:
                pass

        def apply_all():
            if not plugin._gs("fake_premium", False):
                return
            try:
                for acc in range(4):
                    try:
                        uc = UserConfig.getInstance(acc)
                        if not (uc and uc.isClientActivated()):
                            continue
                        try:
                            my = int(uc.getClientUserId())
                        except Exception:
                            continue
                        cached_es = _load_cached_es(acc=acc, uid=my)
                        cached_color, cached_profile = _load_cached_premium(acc=acc, uid=my)
                        user = uc.getCurrentUser()
                        _apply_to_user(user, cached_es, cached_color, cached_profile)
                        mu = None
                        try:
                            mc = MessagesController.getInstance(acc)
                            mu = mc.getUser(Long(my))
                            _apply_to_user(mu, cached_es, cached_color, cached_profile)
                        except Exception:
                            pass
                        try:
                            nc = NotificationCenter.getInstance(acc)
                            target = mu if mu is not None else user
                            def _fire(t=target, n=nc):
                                try:
                                    if t is not None:
                                        n.postNotificationName(NotificationCenter.userEmojiStatusUpdated, t)
                                except Exception:
                                    pass
                                try:
                                    n.postNotificationName(NotificationCenter.updateInterfaces, Integer(524288 | 1 | 2))
                                except Exception:
                                    pass
                            run_on_ui_thread(_fire)
                        except Exception:
                            pass
                    except Exception:
                        pass
            except Exception:
                pass

        run_on_ui_thread(apply_all)

        try:
            UCC = find_class("org.telegram.messenger.UserConfig")
            class SaveHook(MethodHook):
                def after_hooked_method(self, param):
                    if not plugin._gs("fake_premium", False):
                        return
                    try:
                        uc = param.thisObject
                        _save_premium_state(uc)
                        _save_emoji_state(uc)
                    except Exception:
                        pass
            for sig in [[Boolean.TYPE], []]:
                try:
                    m = UCC.getClass().getDeclaredMethod("saveConfig", *sig)
                    m.setAccessible(True)
                    u = self.hook_method(m, SaveHook())
                    if u:
                        self._unhooks.append(u)
                except Exception:
                    pass

            UC_cls0 = find_class("org.telegram.tgnet.TLRPC$User")
            class SetCurrentUserHook(MethodHook):
                def before_hooked_method(self, param):
                    if not plugin._gs("fake_premium", False):
                        return
                    try:
                        user = param.args[0]
                        if user is None:
                            return
                        try:
                            acc = param.thisObject.getCurrentAccount() if hasattr(param.thisObject, "getCurrentAccount") else UserConfig.selectedAccount
                        except Exception:
                            acc = UserConfig.selectedAccount
                        try:
                            uid = int(user.id)
                            my = int(UserConfig.getInstance(acc).getClientUserId())
                            if uid == my:
                                es = getattr(user, "emoji_status", None)
                                if not _is_empty_es(es):
                                    _save_emoji_state_direct(acc, uid, es)
                                color = getattr(user, "color", None)
                                profile_color = getattr(user, "profile_color", None)
                                if not _is_empty_color(color) or not _is_empty_color(profile_color):
                                    _save_premium_state_direct(acc, uid, color, profile_color)
                        except Exception:
                            pass
                        _patch_user(user, None, acc)
                    except Exception:
                        pass
            try:
                m = UCC.getClass().getDeclaredMethod("setCurrentUser", UC_cls0)
                m.setAccessible(True)
                u = self.hook_method(m, SetCurrentUserHook())
                if u:
                    self._unhooks.append(u)
            except Exception:
                pass

            class IPH(MethodHook):
                def before_hooked_method(self, param):
                    if not plugin._gs("fake_premium", False):
                        return
                    try:
                        obj = param.thisObject
                        if obj is None:
                            return
                        try:
                            acc = int(obj.currentAccount) if hasattr(obj, "currentAccount") else int(UserConfig.selectedAccount)
                        except Exception:
                            acc = int(UserConfig.selectedAccount)
                        my = plugin._get_my_uid(acc)
                        if my <= 0:
                            return
                        cu = obj.getCurrentUser()
                        if cu and int(cu.id) == my:
                            param.setResult(Boolean(True))
                    except Exception:
                        pass
            for mname in ["isPremium", "hasPremium"]:
                m = _fm(UCC.getClass(), mname)
                if m:
                    u = self.hook_method(m, IPH())
                    if u:
                        self._unhooks.append(u)
        except Exception as e:
            log(f"[MAX] fpHook uc: {e}")

        try:
            MC = find_class("org.telegram.messenger.MessagesController")
            class PutUserHook(MethodHook):
                def before_hooked_method(self, param):
                    if not plugin._gs("fake_premium", False):
                        return
                    try:
                        user = param.args[0]
                        if not user:
                            return
                        uid = int(user.id)
                        acc = int(param.thisObject.currentAccount) if hasattr(param.thisObject, "currentAccount") else int(UserConfig.selectedAccount)
                        if uid != plugin._get_my_uid(acc):
                            return
                        try:
                            es = getattr(user, "emoji_status", None)
                            if not _is_empty_es(es):
                                _save_emoji_state_direct(acc, uid, es)
                            color = getattr(user, "color", None)
                            profile_color = getattr(user, "profile_color", None)
                            if not _is_empty_color(color) or not _is_empty_color(profile_color):
                                _save_premium_state_direct(acc, uid, color, profile_color)
                        except Exception:
                            pass
                        _patch_user(user, param.thisObject, acc)
                    except Exception:
                        pass
            for mm in MC.getClass().getDeclaredMethods():
                if str(mm.getName()) == "putUser" and len(mm.getParameterTypes()) == 2:
                    mm.setAccessible(True)
                    u = self.hook_method(mm, PutUserHook())
                    if u:
                        self._unhooks.append(u)
                    break

            EMS_cls = None
            try:
                EMS_cls = find_class("org.telegram.tgnet.TLRPC$EmojiStatus")
            except Exception:
                pass
            SG_cls = None
            try:
                SG_cls = find_class("org.telegram.tgnet.tl.TL_stars$StarGift")
            except Exception:
                pass

            class UpdateEmojiHook(MethodHook):
                def after_hooked_method(self, param):
                    try:
                        mc = param.thisObject
                        try:
                            acc = plugin._mc_acc(mc)
                        except Exception:
                            acc = UserConfig.selectedAccount
                        uc = UserConfig.getInstance(acc)
                        if uc is None:
                            return
                        try:
                            if len(param.args) >= 3:
                                did = int(param.args[0])
                                if did != 0:
                                    try:
                                        my = int(uc.getClientUserId())
                                    except Exception:
                                        my = -1
                                    if did != my:
                                        return
                        except Exception:
                            pass
                        try:
                            _save_emoji_state(uc, acc=acc)
                        except Exception:
                            pass
                        try:
                            uc.saveConfig(True)
                        except Exception:
                            pass
                    except Exception:
                        pass

            sigs_em = []
            if EMS_cls and SG_cls:
                sigs_em.append([Long.TYPE, EMS_cls, SG_cls])
                sigs_em.append([EMS_cls, SG_cls])
            if EMS_cls:
                sigs_em.append([EMS_cls])
            for sig in sigs_em:
                try:
                    m = MC.getClass().getDeclaredMethod("updateEmojiStatus", *sig)
                    m.setAccessible(True)
                    u = self.hook_method(m, UpdateEmojiHook())
                    if u:
                        self._unhooks.append(u)
                    break
                except Exception:
                    pass
        except Exception:
            pass

        try:
            LA = find_class("org.telegram.ui.LaunchActivity")
            if LA is not None:
                class AccountSwitchHook(MethodHook):
                    def after_hooked_method(self, param):
                        try:
                            run_on_ui_thread(apply_all)
                        except Exception:
                            pass
                try:
                    m = LA.getClass().getDeclaredMethod("switchToAccount", Integer.TYPE, Boolean.TYPE)
                    m.setAccessible(True)
                    u = self.hook_method(m, AccountSwitchHook())
                    if u:
                        self._unhooks.append(u)
                except Exception:
                    pass
        except Exception:
            pass

    def _hk_nfl(self):
        self.nfl_sel = NFLSelectorHook(self)
        try:
            sel_m = _fm(CA_CLS, "addToSelectedMessages", 3)
            clr_m = _fm(CA_CLS, "hideActionMode", 0)
            mul_m = _fm(CA_CLS, "startMultiselect", 1)
            if sel_m:
                u = self.hook_method(sel_m, self.nfl_sel)
                if u:
                    self._unhooks.append(u)
            if clr_m:
                u = self.hook_method(clr_m, NFLClearHook(self.nfl_sel))
                if u:
                    self._unhooks.append(u)
            if mul_m:
                u = self.hook_method(mul_m, NFLMultiHook(self))
                if u:
                    self._unhooks.append(u)
            mc = get_messages_controller()
            if mc:
                for m in mc.getClass().getDeclaredMethods():
                    if str(m.getName()) == "deleteMessages" and len(m.getParameterTypes()) >= 7:
                        m.setAccessible(True)
                        u = self.hook_method(m, NFLDeleteHook())
                        if u:
                            self._unhooks.append(u)
                        break
        except Exception as e:
            log(f"[MAX] NFL: {e}")

    def _hk_feed_ads(self):
        plugin = self
        class RetFalse(MethodHook):
            def before_hooked_method(self, param):
                if not plugin._gs("hide_feed_ads", True):
                    return
                try:
                    param.setResult(Boolean(False))
                except Exception:
                    pass
        class RetNull(MethodHook):
            def before_hooked_method(self, param):
                if not plugin._gs("hide_feed_ads", True):
                    return
                try:
                    param.setResult(None)
                except Exception:
                    pass
        class RetEmptyList(MethodHook):
            def before_hooked_method(self, param):
                if not plugin._gs("hide_feed_ads", True):
                    return
                try:
                    param.setResult(ArrayList())
                except Exception:
                    pass
        class ClearInjectorHook(MethodHook):
            def after_hooked_method(self, param):
                if not plugin._gs("hide_feed_ads", True):
                    return
                try:
                    if param.thisObject is not None and hasattr(param.thisObject, "clear"):
                        param.thisObject.clear()
                except Exception:
                    pass
        try:
            controller_cls = find_class("com.exteragram.messenger.feed.ads.FeedAdController")
            injector_cls = find_class("com.exteragram.messenger.feed.ads.FeedAdInjector")
            parser_cls = find_class("com.exteragram.messenger.feed.ads.FeedAdParser")
        except Exception:
            return
        if controller_cls is None and injector_cls is None and parser_cls is None:
            return
        if controller_cls is not None:
            try:
                max_accounts = int(UserConfig.MAX_ACCOUNT_COUNT)
            except Exception:
                max_accounts = 4
            for acc in range(max_accounts):
                try:
                    instance = controller_cls.getInstance(acc)
                    if instance is not None:
                        _set_field_value(instance, "eligibleAds", ArrayList())
                except Exception:
                    pass
            for mn in ("isEnabled", "nextAd"):
                for mm in controller_cls.getClass().getDeclaredMethods():
                    if str(mm.getName()) != mn:
                        continue
                    mm.setAccessible(True)
                    u = self.hook_method(mm, RetFalse() if mn == "isEnabled" else RetNull())
                    if u:
                        self._unhooks.append(u)
                    break
        if injector_cls is not None:
            for mm in injector_cls.getClass().getDeclaredMethods():
                if str(mm.getName()) != "refresh":
                    continue
                mm.setAccessible(True)
                u = self.hook_method(mm, ClearInjectorHook())
                if u:
                    self._unhooks.append(u)
                break
        if parser_cls is not None:
            for mm in parser_cls.getClass().getDeclaredMethods():
                if str(mm.getName()) != "parse":
                    continue
                mm.setAccessible(True)
                u = self.hook_method(mm, RetEmptyList())
                if u:
                    self._unhooks.append(u)
                break

    def _hook_ad(self):
        plugin = self
        class PromoNoOpHook(MethodHook):
            def before_hooked_method(self, param):
                try:
                    param.setResult(None)
                except Exception:
                    return
                try:
                    controller = param.thisObject
                    plugin._run_on_stage_queue(
                        lambda: plugin._cleanup_messages_controller(controller)
                    )
                except Exception:
                    pass
        class RetF(MethodHook):
            def before_hooked_method(self, param):
                param.setResult(Boolean(False))
        class VideoAdsLoadHook(MethodHook):
            def before_hooked_method(self, param):
                video_ads = param.thisObject
                try:
                    _set_field_value(video_ads, "loading", False)
                    _set_field_value(video_ads, "loaded", True)
                    ads = _get_field_value(video_ads, "ads")
                    if ads is not None:
                        try:
                            ads.clear()
                        except Exception:
                            pass
                except Exception:
                    pass
                try:
                    param.setResult(None)
                except Exception:
                    pass
        class VideoNoOp(MethodHook):
            def before_hooked_method(self, param):
                param.setResult(None)
        mc = get_messages_controller()
        if mc:
            c = mc.getClass()
            m = _fm(c, "isPromoDialog", 2)
            if m:
                u = self.hook_method(m, RetF())
                if u:
                    self._unhooks.append(u)
        video_noop = VideoNoOp()
        for vmethod, vpc, vhook in [("load", 0, VideoAdsLoadHook()), ("schedule", 0, video_noop), ("show", 0, video_noop), ("isPopupShown", 0, RetF())]:
            try:
                va_cls = jclass("org.telegram.messenger.video.VideoAds").getClass()
                m = _fm(va_cls, vmethod, vpc)
                if m:
                    u = self.hook_method(m, vhook)
                    if u:
                        self._unhooks.append(u)
            except Exception:
                pass
        self._cleanup_all_accounts_soon()

    def _run_on_stage_queue(self, fn):
        try:
            Utilities.stageQueue.postRunnable(lambda: fn())
        except Exception:
            try:
                fn()
            except Exception:
                pass

    def _cleanup_all_accounts_soon(self):
        for delay in (0, 500, 2000):
            try:
                run_on_ui_thread(
                    lambda: self._run_on_stage_queue(self._cleanup_all_accounts),
                    delay,
                )
            except Exception:
                pass

    def _cleanup_all_accounts(self):
        try:
            max_accounts = int(UserConfig.MAX_ACCOUNT_COUNT)
        except Exception:
            max_accounts = 4
        for account in range(max_accounts):
            try:
                controller = MessagesController.getInstance(account)
                if controller is not None:
                    self._cleanup_messages_controller(controller)
            except Exception:
                continue

    def _cleanup_messages_controller(self, controller):
        if controller is None:
            return
        should_remove_dialog = False
        try:
            promo_dialog = _get_field_value(controller, "promoDialog")
            promo_dialog_id = _int(_get_field_value(controller, "promoDialogId", 0), 0)
            should_remove_dialog = promo_dialog is not None or bool(promo_dialog_id)
        except Exception:
            pass
        if should_remove_dialog:
            try:
                run_on_ui_thread(lambda: self._cleanup_promo_dialog_on_ui(controller))
            except Exception:
                pass

    def _cleanup_promo_dialog_on_ui(self, controller):
        if controller is None:
            return
        try:
            _invoke_no_arg(controller, "removePromoDialog")
        except Exception:
            pass
        try:
            _set_field_value(controller, "promoDialog", None)
            _set_field_value(controller, "promoDialogId", 0)
            _set_field_value(controller, "promoDialogType", 0)
        except Exception:
            pass

    def _clear_promo_preferences(self, controller, next_check_time):
        pass

    def _clear_promo_preferences_in(self, preferences, next_check_time, seen):
        pass

    def on_send_message_hook(self, account, params):
        try:
            if self._is_ghost() and self._factive("ghost_no_read") and self._gs("read_on_action", True):
                did = 0
                try:
                    did = int(getattr(params, "peer", 0) or 0)
                except Exception:
                    did = 0
                if not did:
                    f = get_last_fragment()
                    if f and str(f.getClass().getSimpleName()) == "ChatActivity":
                        try:
                            did = int(get_private_field(f, "dialog_id") or 0)
                        except Exception:
                            did = 0
                if did:
                    self._send_read_for_dialog(did)
        except Exception:
            pass
        try:
            if self._is_ghost() and self._gs("use_scheduled", False):
                if not int(getattr(params, "scheduleDate", 0) or 0):
                    params.scheduleDate = int(_time.time()) + 10
        except Exception:
            pass
        return HookResult(strategy=HookStrategy.MODIFY, params=params)

    def on_update_hook(self, update_name, account, update):
        if update_name in PRESENCE_UPD and (self._gs("save_last_online", False) or self._gs("presence_track", False)):
            try:
                self._presence_on_update(update_name, account, update)
            except Exception:
                pass
        # Handle deleted messages from server updates (other devices)
        # — захват удалённых сообщений полностью перенесён на java-сторону
        # (re-extera логика: MessagesStorage.markMessagesAsDeleted* и
        # updateDialogsWithDeletedMessages перехватываются в HookManager).
        if self._gs("save_edits", True) or self._gs("save_attachments", False):
            if update_name in ("TL_updateNewMessage", "TL_updateNewChannelMessage"):
                try:
                    msg = update.message
                    if not msg.out:
                        did = _peer_did(msg)
                        mid = int(msg.id)
                        if did and mid and self.vault and self._gs("save_attachments", False):
                            media_hash = _get_media_hash(msg)
                            threading.Thread(target=lambda: _ensure_attach_saved(msg, did, mid, media_hash, force_download=False), daemon=True).start()
                except Exception:
                    pass
            elif update_name in ("TL_updateEditMessage", "TL_updateEditChannelMessage"):
                try:
                    msg = update.message
                    if msg.out:
                        return HookResult()
                    did, mid = _peer_did(msg), int(msg.id)
                    if did and mid and self.vault:
                        save_att = self._gs("save_attachments", False)
                        if not self.vault.has_revisions(did, mid):
                            mo = _get_message_from_chat(did, mid)
                            if mo and mo.messageOwner:
                                old_msg = mo.messageOwner
                                if self._gs("save_edits", True):
                                    self.vault.save_message_version(did, mid, old_msg, is_original=True)
                                if save_att:
                                    _ensure_attach_saved(old_msg, did, mid, _get_media_hash(old_msg), force_download=False)
                        if self._gs("save_edits", True):
                            self.vault.save_message_version(did, mid, msg, is_original=False)
                        if save_att:
                            _ensure_attach_saved(msg, did, mid, _get_media_hash(msg), force_download=False)
                except Exception:
                    pass
        return HookResult()

    def pre_request_hook(self, request_name, account, request):
        if request_name in ("TL_messages_readMessageContents", "TL_channels_readMessageContents"):
            try:
                ids = list(request.id) if hasattr(request, "id") and request.id else []
                if any(int(i) in self._pending_read_content_mids for i in ids):
                    for i in ids:
                        self._pending_read_content_mids.discard(int(i))
                    return HookResult()
            except Exception:
                pass
        if request_name in READ_REQS:
            try:
                did = 0
                if hasattr(request, "peer"):
                    peer = request.peer
                    if hasattr(peer, "user_id") and peer.user_id:
                        did = _int(peer.user_id)
                    elif hasattr(peer, "chat_id") and peer.chat_id:
                        did = -_int(peer.chat_id)
                    elif hasattr(peer, "channel_id") and peer.channel_id:
                        did = -_int(peer.channel_id)
                if did and did in self._pending_read_dialogs:
                    expected_max_id = self._pending_read_dialogs[did]
                    # Bug fix (#1): this used to pop and bypass on ANY read request for
                    # this dialog, not just the deliberate one that set the entry. If the
                    # app also naturally issued its own readHistory around the same time
                    # (e.g. just from having the chat open), that unrelated request could
                    # consume the bypass first and slip through unblocked - making the
                    # "don't read" toggle behave inconsistently/unpredictably. Only treat
                    # it as the deliberate read when the request's own max_id actually
                    # matches what we expected (requests without max_id, like
                    # readMessageContents/readEncryptedHistory/readDiscussion, fall back
                    # to the old any-match behavior since there's nothing to compare).
                    req_max_id = _int(getattr(request, "max_id", None), None) if hasattr(request, "max_id") else None
                    if req_max_id is None or req_max_id == expected_max_id:
                        self._pending_read_dialogs.pop(did, None)
                        return HookResult()
            except Exception:
                pass
        if request_name in BLOCKED_AD_REQS and self._gs("hide_ads", True):
            return HookResult(strategy=HookStrategy.CANCEL)
        # Проверка исключений чатов (ExclusionsStore)
        if request_name in READ_REQS or request_name in TYPING_REQS:
            try:
                req_did = 0
                if hasattr(request, "peer") and request.peer:
                    p = request.peer
                    if hasattr(p, "user_id") and p.user_id: req_did = _int(p.user_id)
                    elif hasattr(p, "chat_id") and p.chat_id: req_did = -_int(p.chat_id)
                    elif hasattr(p, "channel_id") and p.channel_id: req_did = -_int(p.channel_id)
                if req_did:
                    # 0=DEFAULT, 1=ALWAYS, 2=NEVER
                    if request_name in READ_REQS:
                        r_mode = JMain.ExclusionsStore.getMode(jlong(req_did), True)
                        if r_mode == 1:
                            return HookResult()
                        elif r_mode == 2:
                            return HookResult(strategy=HookStrategy.CANCEL)
                    elif request_name in TYPING_REQS:
                        t_mode = JMain.ExclusionsStore.getMode(jlong(req_did), False)
                        if t_mode == 1:
                            return HookResult()
                        elif t_mode == 2:
                            return HookResult(strategy=HookStrategy.CANCEL)
            except Exception:
                pass

        if self._factive("ghost_no_read") and request_name in READ_REQS:
            if self._is_whitelisted_peer(request):
                return HookResult()
            return HookResult(strategy=HookStrategy.CANCEL)
        if request_name in TYPING_REQS and self._factive("ghost_no_typing"):
            return HookResult(strategy=HookStrategy.CANCEL)
        stories_mode = self._fmode("ghost_no_read_stories", 1)
        if request_name in STORIES_REQS and (
                self._factive("ghost_no_read_stories") or
                (stories_mode == 3 and self._story_ghost_override)):
            return HookResult(strategy=HookStrategy.CANCEL)
        if request_name in ("updateStatus", "TL_account_updateStatus") and self._factive("ghost_no_online"):
            try:
                if not getattr(request, "offline", False):
                    request.offline = True
                    return HookResult(strategy=HookStrategy.MODIFY, request=request)
            except Exception:
                pass
        if request_name == "TL_messages_sendReaction" and self._is_ghost() and self._factive(
                "ghost_no_read") and self._gs("read_on_action", True):
            did = 0
            try:
                peer = getattr(request, "peer", None)
                if peer is not None:
                    if getattr(peer, "user_id", 0):
                        did = _int(peer.user_id)
                    elif getattr(peer, "chat_id", 0):
                        did = -_int(peer.chat_id)
                    elif getattr(peer, "channel_id", 0):
                        did = -_int(peer.channel_id)
            except Exception:
                did = 0
            try:
                mid = _int(getattr(request, "msg_id", 0) or 0)
            except Exception:
                mid = 0
            if did:
                self._send_read_for_dialog(did, mid)
        if request_name in SEND_REQS and self._silent_active():
            try:
                if not getattr(request, "silent", False):
                    _set_field_with_flag(request, "silent", True, 32)
                    return HookResult(strategy=HookStrategy.MODIFY, request=request)
            except Exception:
                pass
        return HookResult()

    def post_request_hook(self, request_name, account, response, error):
        try:
            # ВАЖНО: updateStatus исключён из триггеров немедленного оффлайна,
            # иначе самозацикливание: sendOffline -> ответ на updateStatus ->
            # post_request_hook -> снова sendOffline каждые ~1с (шторм запросов).
            if (request_name in ONLINE_REQS
                    and request_name not in ("updateStatus", "TL_account_updateStatus")
                    and self._is_ghost() and self._gs("ghost_immediate_offline", True)):
                run_on_queue(lambda: self.sendOffline(), "externalNetworkQueue", 1000)
        except Exception:
            pass

    def _on_ghost_change(self, v):
        self.set_setting("ghost_mode", bool(v))
        self._sc.clear()
        if v:
            self.sendOffline()
        self._update_ghost_drawer_btn()

    def _on_view_deleted_click(self, ctx):
        try:
            if self.dex and self.dex.loaded:
                self.dex.call("pyShowDeletedList")
            else:
                BulletinHelper.show_info(L.g("no_deleted"))
        except Exception as e:
            log(f"[MAX] viewDel: {e}")

    def _on_purge_deleted_click(self, ctx):
        try:
            if not (self.dex and self.dex.loaded):
                BulletinHelper.show_info(L.g("no_deleted"))
                return
            def _do():
                try:
                    frag = get_last_fragment()
                    if not frag or not isinstance(frag, ChatActivity):
                        return
                    did = 0
                    f = _ff(CA_CLS, "dialog_id")
                    if f:
                        did = int(f.get(frag))
                    if not did:
                        return
                    mids = []
                    try:
                        if self.vault:
                            mids = self.vault.get_removed_mids(did)
                    except Exception:
                        mids = []
                    def on_yes(dlg, w):
                        dlg.dismiss()
                        try:
                            if self.vault and mids:
                                self.vault.purge_removed(did, mids)
                                self._invalidate_removed_cache()
                        except Exception:
                            pass
                        try:
                            self.dex.call("pyPurgeChatDeleted", jlong(did))
                            BulletinHelper.show_info(L.g("purged"))
                        except Exception as e:
                            log(f"[MAX] purgeDel: {e}")
                    try:
                        act = frag.getParentActivity()
                    except Exception:
                        act = None
                    d = AlertDialogBuilder(act)
                    d.set_title(L.g("purge_deleted"))
                    d.set_message(L.g("purge_deleted_confirm"))
                    d.set_positive_button(L.g("yes"), on_yes)
                    d.set_negative_button(L.g("no"), lambda dlg, w: dlg.dismiss())
                    d.show()
                except Exception as e:
                    log(f"[MAX] purgeDel: {e}")
            run_on_ui_thread(_do)
        except Exception as e:
            log(f"[MAX] purgeDel: {e}")

    def _on_clear_all_deleted_click(self, ctx=None):
        try:
            plugin = self
            def _do():
                try:
                    frag = get_last_fragment()
                    act = frag.getParentActivity() if frag else None
                    def on_yes(dlg, w):
                        dlg.dismiss()
                        try:
                            if plugin.vault:
                                plugin.vault.clear_all_removed()
                                plugin._invalidate_removed_cache()
                        except Exception as e:
                            log(f"[MAX] vault.clear_all_removed: {e}")
                        try:
                            if plugin.dex and plugin.dex.loaded:
                                plugin.dex.call("pyClearAllDeleted")
                            BulletinHelper.show_info(L.g("all_deleted_cleared"))
                        except Exception as e:
                            log(f"[MAX] pyClearAllDeleted: {e}")
                    d = AlertDialogBuilder(act)
                    d.set_title(L.g("clear_all_deleted"))
                    d.set_message(L.g("clear_all_deleted_confirm"))
                    d.set_positive_button(L.g("yes"), on_yes)
                    d.set_negative_button(L.g("no"), lambda dlg, w: dlg.dismiss())
                    d.show()
                except Exception as e:
                    log(f"[MAX] clearAllDel: {e}")
            run_on_ui_thread(_do)
        except Exception as e:
            log(f"[MAX] clearAllDel: {e}")

    def _on_sync_all_chats_click(self, ctx=None):
        try:
            if self.dex and self.dex.loaded:
                self.dex.call("pySyncAllChats")
            else:
                BulletinHelper.show_info(L.g("dex_not_loaded"))
        except Exception as e:
            log(f"[MAX] syncAllChats: {e}")

    def _navigate_to_message(self, did, mid):
        did = int(did)
        mid = int(mid)
        plugin = self
        def _do():
            try:
                frag = get_last_fragment()
                if not frag:
                    return
                frag_did = 0
                if isinstance(frag, ChatActivity):
                    f = _ff(CA_CLS, "dialog_id")
                    if f:
                        frag_did = int(f.get(frag))
                if frag_did == did:
                    try:
                        frag.scrollToMessageId(mid, 0, True, 0, True, 0)
                    except Exception:
                        pass
                    return
                try:
                    acc = int(UserConfig.selectedAccount)
                    args = jclass("android.os.Bundle")()
                    if did > 0:
                        args.putLong("user_id", jlong(did))
                    elif did < 0:
                        chat_id = -did
                        try:
                            mc = MessagesController.getInstance(acc)
                            chat = mc.getChat(Long(chat_id))
                            if chat and ChatObject.isChannel(chat):
                                args.putLong("chat_id", jlong(chat_id))
                            else:
                                args.putLong("chat_id", jlong(chat_id))
                        except Exception:
                            args.putLong("chat_id", jlong(chat_id))
                    args.putInt("message_id", jint(mid))
                    ca = ChatActivity(args)
                    frag.presentFragment(ca)
                except Exception as e:
                    log(f"[MAX] navigate: {e}")
            except Exception:
                pass
        run_on_ui_thread(_do)

    def _scroll_to_message(self, did, mid):
        self._navigate_to_message(did, mid)

    def _try_save_media(self, mo):
        try:
            frag = get_last_fragment()
            ctx = frag.getParentActivity() if frag else None
            if not ctx:
                return
            msg = mo.messageOwner if mo else None
            if msg and _save_media_to_downloads(msg, ctx):
                BulletinHelper.show_info(L.g("saved_to"))
            else:
                BulletinHelper.show_info(L.g("no_text"))
        except Exception:
            pass

    def _show_deleted_list(self, did):
        if self.dex and self.dex.loaded:
            try:
                self.dex.call("pyShowDeletedList")
                return
            except Exception:
                pass
        BulletinHelper.show_info(L.g("no_deleted"))

    def _download_revision_media(self, dmsg):
        try:
            if not dmsg:
                BulletinHelper.show_info(L.g("error"))
                return
            did = _peer_did(dmsg)
            mid = int(getattr(dmsg, "id", 0))
            mh = _get_media_hash(dmsg)
            p = _ensure_attach_saved(dmsg, did, mid, mh, force_download=True)
            if p:
                BulletinHelper.show_info(L.g("saved_to"))
            else:
                _save_media_to_downloads(dmsg, ApplicationLoader.applicationContext)
                BulletinHelper.show_info(L.g("saved_to"))
        except Exception:
            BulletinHelper.show_info(L.g("error"))

    def _show_revisions(self, did, mid):
        revs = self.vault.get_revisions(did, mid) if self.vault else []
        revs_json = ""
        if revs:
            try:
                items = []
                for rev in revs:
                    items.append({
                        "ver": int(rev.get("ver", 0)),
                        "timestamp": int(rev.get("timestamp", 0)),
                        "text": str(rev.get("text", "")),
                        "data": str(rev.get("data") or "")
                    })
                revs_json = json.dumps(items, ensure_ascii=False)
            except Exception:
                pass
        if self.dex and self.dex.loaded:
            try:
                from java.lang import Long as JLong, Integer as JInt, String as JString
                self.dex.call("pyShowMessageHistoryWithRevisions", JLong(int(did)), JInt(int(mid)), JString(revs_json))
                return
            except Exception as e:
                log(f"[MAX] pyShowMessageHistoryWithRevisions: {e}")
        BulletinHelper.show_info(L.g("no_revisions"))

    def _export_settings(self, _v=None):
        plugin = self
        def _bg():
            try:
                sdk_settings = {}
                try:
                    sdk_settings = plugin.export_settings()
                    if not isinstance(sdk_settings, dict):
                        sdk_settings = {}
                except Exception:
                    pass
                data = {"_version": __version__, "_ts": int(_time.time()), "_sdk": sdk_settings}
                prefs = plugin._prefs()
                try:
                    raw = prefs.getString("filters_list", "[]")
                    data["_filters"] = json.loads(str(raw))
                except Exception:
                    pass
                try:
                    raw = prefs.getString("read_whitelist", "[]")
                    data["_read_whitelist"] = json.loads(str(raw))
                except Exception:
                    pass
                try:
                    raw = prefs.getString(PRESENCE_KEY, "{}")
                    pd = json.loads(str(raw))
                    if pd.get("tracked"):
                        data["_presence_tracked"] = pd["tracked"]
                except Exception:
                    pass
                try:
                    lang = prefs.getString(L._prefs_key, "auto")
                    data["_lang"] = str(lang)
                except Exception:
                    pass
                payload = json.dumps(data, ensure_ascii=False, indent=2)
                dir_path = os.path.join(
                    str(ApplicationLoader.applicationContext.getCacheDir().getAbsolutePath()),
                    "mre_backup"
                )
                ensure_dir_exists(dir_path)
                fpath = os.path.join(dir_path, "exteramax.mre_backup")
                write_file(fpath, payload)
                my_id = int(UserConfig.getInstance(int(UserConfig.selectedAccount)).getClientUserId())
                send_document(my_id, fpath)
                run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("export_done")))
            except Exception as e:
                log(f"[MAX] export: {e}")
                run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("export_error")))
        threading.Thread(target=_bg, daemon=True).start()

    def _do_import(self, data):
        plugin = self
        def _bg():
            try:
                log("[MAX] _do_import: starting import")
                sdk_settings = data.get("_sdk")
                if isinstance(sdk_settings, dict) and sdk_settings:
                    try:
                        # Bug #6 fix: use reload_settings=True to rebuild settings UI after import
                        plugin.import_settings(sdk_settings, reload_settings=True)
                        log("[MAX] _do_import: SDK settings imported successfully")
                    except Exception as e:
                        log(f"[MAX] _do_import: SDK settings import failed: {e}")
                prefs = plugin._prefs()
                editor = prefs.edit()
                if "_filters" in data:
                    try:
                        editor.putString("filters_list", json.dumps(data["_filters"]))
                        plugin._filters = data["_filters"]
                        log("[MAX] _do_import: filters imported")
                    except Exception as e:
                        log(f"[MAX] _do_import: filters import failed: {e}")
                if "_read_whitelist" in data:
                    try:
                        wl = data["_read_whitelist"]
                        editor.putString("read_whitelist", json.dumps(wl))
                        plugin._read_whitelist = set(wl)
                        log("[MAX] _do_import: read whitelist imported")
                    except Exception as e:
                        log(f"[MAX] _do_import: whitelist import failed: {e}")
                if "_presence_tracked" in data:
                    try:
                        tracked = data["_presence_tracked"]
                        pd_raw = prefs.getString(PRESENCE_KEY, "{}")
                        pd = json.loads(str(pd_raw))
                        pd["tracked"] = tracked
                        editor.putString(PRESENCE_KEY, json.dumps(pd))
                        plugin._presence_tracked = set(tracked)
                        log("[MAX] _do_import: presence tracked imported")
                    except Exception as e:
                        log(f"[MAX] _do_import: presence tracked import failed: {e}")
                if "_lang" in data:
                    try:
                        editor.putString(L._prefs_key, str(data["_lang"]))
                        L._lang_cache = [None, 0]
                        log("[MAX] _do_import: language imported")
                    except Exception as e:
                        log(f"[MAX] _do_import: language import failed: {e}")
                editor.apply()
                plugin._sc.clear()
                plugin._load_filters()
                log("[MAX] _do_import: import completed successfully")
                run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("import_done")))
            except Exception as e:
                log(f"[MAX] _do_import: unexpected error: {e}")
                run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("import_error")))
        threading.Thread(target=_bg, daemon=True).start()

    def _push_filters(self):
        try:
            arr = []
            for f in self._filters:
                arr.append({
                    "enabled": bool(f.get("enabled", True)),
                    "expression": str(f.get("expression", "")),
                    "inverted": bool(f.get("inverted", False)),
                    "channels_only": bool(f.get("channels_only", False)),
                })
            self.dex.call("pySyncFilters", JString(json.dumps(arr)))
        except Exception as e:
            log(f"[MAX] push_filters: {e}")

    def _load_filters(self):
        try:
            self._filters = json.loads(str(self._prefs().getString("filters_list", "[]")))
        except Exception:
            self._filters = []
        self._cpat = []
        for f in self._filters:
            if f.get("enabled", True) and f.get("expression", ""):
                try:
                    flags = re.IGNORECASE if f.get("ignore_case", True) else 0
                    self._cpat.append((re.compile(f["expression"], flags), f.get("inverted", False), f.get("channels_only", False)))
                except Exception:
                    pass

    def _has_channel_only(self):
        try:
            for _, _, cc in self._cpat:
                if cc:
                    return True
        except Exception:
            pass
        return False

    def _save_filters(self):
        self._prefs().edit().putString("filters_list", json.dumps(self._filters)).apply()
        self._load_filters()
        self._push_filters()

    def _should_filter(self, text, chan=False):
        if not text or not self._cpat:
            return False
        if not self.get_setting("filters_enabled", True):
            return False
        ts = str(text)
        for pat, inv, cc in self._cpat:
            try:
                if cc and not chan:
                    continue
                found = pat.search(ts) is not None
                if (inv and not found) or (not inv and found):
                    return True
            except Exception:
                pass
        return False

    def _compare_versions(self, v1, v2):
        if self.dex and self.dex.loaded:
            try:
                return int(self.dex.call("compareVersions", JString(str(v1)), JString(str(v2))))
            except Exception:
                pass
        try:
            def parse(v):
                return [int(x) for x in str(v).strip().split(".")]
            p1, p2 = parse(v1), parse(v2)
            while len(p1) < len(p2):
                p1.append(0)
            while len(p2) < len(p1):
                p2.append(0)
            for a, b in zip(p1, p2):
                if a < b:
                    return -1
                if a > b:
                    return 1
            return 0
        except Exception:
            return 0

    def _fetch_json_url(self, url):
        try:
            import requests
            return requests.get(url, timeout=7).json()
        except Exception:
            pass
        try:
            from java.net import URL as JURL
            from java.io import BufferedReader, InputStreamReader
            conn = JURL(url).openConnection()
            conn.setConnectTimeout(7000)
            conn.setReadTimeout(7000)
            conn.setRequestProperty("User-Agent", "ExteraMax")
            reader = BufferedReader(InputStreamReader(conn.getInputStream(), "UTF-8"))
            sb = []
            line = reader.readLine()
            while line is not None:
                sb.append(str(line))
                line = reader.readLine()
            reader.close()
            conn.disconnect()
            return json.loads("\n".join(sb))
        except Exception:
            pass
        return None

    def _check_for_updates(self):
        pass

    def _open_tg_link(self, url):
        try:
            from android.content import Intent
            from android.net import Uri
            ctx = ApplicationLoader.applicationContext
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setPackage(ctx.getPackageName())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        except Exception:
            pass

    def _open_author_channel(self, url):
        self._open_tg_link(url)

    def _load_read_whitelist(self):
        try:
            raw = self._prefs().getString("read_whitelist", "[]")
            self._read_whitelist = set(json.loads(str(raw)))
        except Exception:
            self._read_whitelist = set()

    def _save_read_whitelist(self):
        try:
            self._prefs().edit().putString("read_whitelist", json.dumps(list(self._read_whitelist))).apply()
        except Exception:
            pass

    def _is_whitelisted_peer(self, request):
        if not self._read_whitelist:
            return False
        try:
            peer = getattr(request, "peer", None)
            if not peer:
                return False
            uid = 0
            if hasattr(peer, "user_id") and peer.user_id:
                uid = _int(peer.user_id)
            if uid and uid in self._read_whitelist:
                return True
        except Exception:
            pass
        return False

    def _show_read_whitelist(self, _v=None):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                ctx = frag.getParentActivity()
                uids = sorted(plugin._read_whitelist)
                items = [f"{plugin._presence_user_name(u)} (id{u})" for u in uids]
                def on_pick(dlg, which):
                    dlg.dismiss()
                    uid = uids[which]
                    def confirm():
                        f2 = get_last_fragment()
                        if not f2 or not f2.getParentActivity():
                            return
                        d = AlertDialogBuilder(f2.getParentActivity())
                        d.set_title(L.g("remove"))
                        d.set_message(plugin._presence_user_name(uid))
                        d.set_positive_button(L.g("yes"), lambda dd, w: (dd.dismiss(), do_rm(uid)))
                        d.set_negative_button(L.g("no"), lambda dd, w: (dd.dismiss(), run_on_ui_thread(_ui)))
                        d.show()
                    def do_rm(u):
                        plugin._read_whitelist.discard(int(u))
                        plugin._save_read_whitelist()
                        BulletinHelper.show_info(L.g("removed"))
                        run_on_ui_thread(_ui)
                    run_on_ui_thread(confirm)
                def on_add(dlg, w):
                    dlg.dismiss()
                    # Открывать новый AlertDialog сразу после dismiss() того же UI-колбэка
                    # ненадёжно — Android может ещё не успеть физически закрыть текущий
                    # диалог, и второй show() молча игнорируется без ошибок. Откладываем
                    # открытие на следующий тик UI-потока, как и при повторном показе
                    # списка в on_pick/do_rm выше.
                    run_on_ui_thread(plugin._show_read_wl_add)
                def on_clear_all(dlg, w):
                    dlg.dismiss()
                    def confirm_clear():
                        f2 = get_last_fragment()
                        if not f2 or not f2.getParentActivity():
                            return
                        d2 = AlertDialogBuilder(f2.getParentActivity())
                        d2.set_title(L.g("read_wl_users"))
                        d2.set_message(L.g("read_wl_clear_confirm"))
                        def do_clear(dd, w2):
                            dd.dismiss()
                            plugin._read_whitelist.clear()
                            plugin._save_read_whitelist()
                            BulletinHelper.show_info(L.g("removed"))
                        d2.set_positive_button(L.g("yes"), do_clear)
                        d2.set_negative_button(L.g("no"), lambda dd, w2: (dd.dismiss(), run_on_ui_thread(_ui)))
                        d2.show()
                    run_on_ui_thread(confirm_clear)
                d = AlertDialogBuilder(ctx)
                if items:
                    d.set_title(f"{L.g('read_wl_users')} ({len(items)})")
                    d.set_items(items, on_pick)
                    # Bug fix (#15): the whitelist is stored in raw SharedPreferences,
                    # separate from the plugin framework's own settings storage, so the
                    # host app's generic "reset plugin settings" action (which only
                    # resets the declared Switch/Selector settings) can never know about
                    # it and never clears it. Give an explicit, reliable way to clear it.
                    d.set_neutral_button(L.g("read_wl_clear_all"), on_clear_all)
                else:
                    d.set_title(L.g("read_wl_users"))
                    d.set_message(L.g("read_wl_empty"))
                d.set_positive_button(L.g("add"), on_add)
                d.set_negative_button(L.g("close"), lambda dd, w: dd.dismiss())
                d.show()
            except Exception as e:
                log(f"[MAX] readWL: {e}")
        run_on_ui_thread(_ui)

    def _pick_chats(self, on_picked):
        # Выбор пользователей нативным пикером чатов (UsersSelectActivity, только
        # личные чаты — как "поделиться контактом" в ботах) вместо ручного ввода id.
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag:
                    return
                USA = jclass("org.telegram.ui.UsersSelectActivity")
                DelCls = jclass("org.telegram.ui.UsersSelectActivity$FilterUsersActivityDelegate")
                act = None
                try:
                    act = USA(True, ArrayList(), 0)
                except Exception:
                    try:
                        act = USA(0)
                    except Exception as e0:
                        log(f"[MAX] pickChats ctor: {e0}")
                        return
                try:
                    act.asPrivateChats()
                except Exception:
                    pass

                class PickerDelegate(dynamic_proxy(DelCls)):
                    def didSelectChats(s_self, dids, flags):
                        try:
                            ids = []
                            if dids:
                                for d in _jlist(dids):
                                    try:
                                        v = int(d)
                                    except Exception:
                                        continue
                                    if v > 0:
                                        ids.append(v)
                            if ids:
                                run_on_ui_thread(lambda: on_picked(ids))
                        except Exception as e:
                            log(f"[MAX] pickDel: {e}")

                act.setDelegate(PickerDelegate())
                frag.presentFragment(act)
            except Exception as e:
                log(f"[MAX] pickChats: {e}")
        run_on_ui_thread(_ui)

    def _show_read_wl_add(self):
        plugin = self
        def on_picked(ids):
            added = 0
            for uid in ids:
                if uid not in plugin._read_whitelist:
                    plugin._read_whitelist.add(uid)
                    added += 1
            if added:
                plugin._save_read_whitelist()
                BulletinHelper.show_info(f"{L.g('added')}: {added}")
        self._pick_chats(on_picked)

    def _resolve_and_add_wl(self, raw):
        plugin = self
        try:
            v = raw.lstrip("@").strip()
            if v.lstrip("-").isdigit():
                uid = int(v)
                if uid <= 0:
                    BulletinHelper.show_error(L.g("invalid_id"))
                    return
                plugin._read_whitelist.add(uid)
                plugin._save_read_whitelist()
                BulletinHelper.show_info(L.g("added"))
                return
            req = TLRPC.TL_contacts_resolveUsername()
            req.username = v
            def done(resp, err):
                try:
                    if err or not resp:
                        run_on_ui_thread(lambda: BulletinHelper.show_error(L.g("not_found")))
                        return
                    users = _jlist(getattr(resp, "users", None))
                    if users:
                        uid = int(str(users[0].id))
                        plugin._read_whitelist.add(uid)
                        plugin._save_read_whitelist()
                        run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("added")))
                    else:
                        run_on_ui_thread(lambda: BulletinHelper.show_error(L.g("not_found")))
                except Exception:
                    run_on_ui_thread(lambda: BulletinHelper.show_error(L.g("error")))
            send_request(req, RequestCallback(done))
        except Exception:
            pass

    def _load_presence(self):
        try:
            data = json.loads(str(self._prefs().getString(PRESENCE_KEY, "{}")))
            self._presence_tracked = set(data.get("tracked", []))
            self._presence_records = data.get("records", {})
            self._presence_last_seen = {str(k): _int(v) for k, v in data.get("last_seen", {}).items() if _int(v) > 0}
        except Exception:
            self._presence_tracked = set()
            self._presence_records = {}
            self._presence_last_seen = {}

    def _save_presence(self):
        if not self._presence_dirty:
            return
        with self._presence_lock:
            try:
                for k, rows in list(self._presence_records.items()):
                    if len(rows) > 2000:
                        self._presence_records[k] = rows[-2000:]
                # last_seen пишут и python-, и java-сторона (java пишет время из
                # апдейтов при "Сохранять последний онлайн"); перед записью
                # сливаем текущее содержимое хранилища со своим, беря максимум,
                # чтобы не затереть более свежие чужие записи.
                try:
                    cur = json.loads(str(self._prefs().getString(PRESENCE_KEY, "{}")) or "{}")
                    merged = {str(k): _int(v) for k, v in (cur.get("last_seen") or {}).items() if _int(v) > 0}
                except Exception:
                    merged = {}
                for k, v in self._presence_last_seen.items():
                    if _int(v) > _int(merged.get(str(k), 0)):
                        merged[str(k)] = _int(v)
                self._presence_last_seen = merged
                data = {"tracked": list(self._presence_tracked), "records": self._presence_records, "last_seen": merged}
                self._prefs().edit().putString(PRESENCE_KEY, json.dumps(data)).apply()
                self._presence_dirty = False
            except Exception:
                pass

    def _presence_now(self):
        try:
            return int(ConnectionsManager.getInstance(int(UserConfig.selectedAccount)).getCurrentTime())
        except Exception:
            return int(_time.time())

    def _presence_decay(self):
        i = _int(self.get_setting("presence_decay", 1), 1)
        if self.dex and self.dex.loaded:
            try:
                return int(self.dex.call("presenceDecaySeconds", jint(i)))
            except Exception:
                pass
        return [30, 45, 60, 120][i] if 0 <= i < 4 else 45

    def _presence_is_hidden(self, uid, account=None):
        try:
            acc = int(UserConfig.selectedAccount if account is None else account)
            u = MessagesController.getInstance(acc).getUser(Long(int(uid)))
            if not u:
                return False
            st = getattr(u, "status", None)
            if not st:
                return True
            name = str(st.getClass().getSimpleName())
            return name in ("TL_userStatusEmpty", "TL_userStatusRecently", "TL_userStatusLastWeek", "TL_userStatusLastMonth")
        except Exception:
            return False

    def _presence_apply_last_seen(self, uid, ts, account=None):
        try:
            acc = int(UserConfig.selectedAccount if account is None else account)
            user = MessagesController.getInstance(acc).getUser(Long(int(uid)))
            if not user or not self._presence_is_hidden(uid, acc):
                return
            current = _int(getattr(getattr(user, "status", None), "was_online", 0))
            if current >= int(ts):
                return
            status = TLRPC.TL_userStatusOffline()
            status.was_online = int(ts)
            status.expires = int(ts)
            user.status = status
            NotificationCenter.getInstance(acc).postNotificationName(NotificationCenter.updateInterfaces, Integer(4))
        except Exception:
            pass

    def _restore_presence_statuses(self):
        return

    def _presence_remember(self, uid, ts, via, account):
        uid = int(uid)
        ts = int(ts)
        if uid <= 0 or ts <= 0:
            return
        key = str(uid)
        if ts > _int(self._presence_last_seen.get(key, 0)):
            self._presence_last_seen[key] = ts
            self._presence_dirty = True
        hidden = self._presence_is_hidden(uid, account)
        if uid in self._presence_tracked and (not self.get_setting("presence_only_hidden", True) or hidden):
            self._presence_lamp[uid] = {"last_active": ts, "via": str(via)}

    def _presence_lamp_on(self, uid):
        e = self._presence_lamp.get(int(uid))
        if not e:
            return False
        return (self._presence_now() - int(e["last_active"])) < self._presence_decay()

    def _presence_tick(self):
        try:
            decay = self._presence_decay()
            now = self._presence_now()
            for uid, e in list(self._presence_lamp.items()):
                la = int(e["last_active"])
                via = e.get("via", "?")
                on = (now - la) < decay
                cur = self._presence_open.get(uid)
                if on:
                    if cur is None:
                        self._presence_open[uid] = {"start": la, "end": la, "via": via}
                    else:
                        cur["end"] = la
                        cur["via"] = via
                else:
                    if cur is not None:
                        k = str(int(uid))
                        rows = self._presence_records.get(k)
                        if rows is None:
                            rows = []
                            self._presence_records[k] = rows
                        row = [int(cur["start"]), int(cur["end"]), str(cur.get("via", "?"))]
                        if not rows or rows[-1] != row:
                            rows.append(row)
                            self._presence_dirty = True
                        self._presence_open.pop(uid, None)
        except Exception:
            pass

    def _presence_on_update(self, update_name, account, update):
        try:
            uid = 0
            via = "?"
            ev_date = 0
            if update_name == "TL_updateUserStatus":
                uid = _int(getattr(update, "user_id", 0))
                status = getattr(update, "status", None)
                st_name = str(status.getClass().getSimpleName()) if status is not None else ""
                expires = _int(getattr(status, "expires", 0))
                was_online = _int(getattr(status, "was_online", 0))
                now = self._presence_now()
                if st_name == "TL_userStatusOnline" or (expires > now and was_online <= 0):
                    ev_date = now
                    via = "online"
                elif was_online > 0:
                    ev_date = min(was_online, now)
                    via = "offline"
                else:
                    return
            elif update_name == "TL_updateUserTyping":
                uid = _int(getattr(update, "user_id", 0))
                via = "typing"
            elif update_name in ("TL_updateChatUserTyping", "TL_updateChannelUserTyping"):
                fid = getattr(update, "from_id", None)
                if fid and hasattr(fid, "user_id"):
                    uid = _int(fid.user_id)
                via = "typing"
            elif update_name == "TL_updateReadHistoryOutbox":
                peer = getattr(update, "peer", None)
                if peer and hasattr(peer, "user_id"):
                    uid = _int(peer.user_id)
                via = "read"
            elif update_name in ("TL_updateNewMessage", "TL_updateNewChannelMessage"):
                msg = getattr(update, "message", None)
                if msg and not bool(getattr(msg, "out", False)):
                    ev_date = _int(getattr(msg, "date", 0))
                    fid = getattr(msg, "from_id", None)
                    if fid and hasattr(fid, "user_id"):
                        uid = _int(fid.user_id)
                    if not uid:
                        peer = getattr(msg, "peer_id", None)
                        if peer and hasattr(peer, "user_id"):
                            uid = _int(peer.user_id)
                    via = "message"
            elif update_name in ("TL_updateEditMessage", "TL_updateEditChannelMessage"):
                msg = getattr(update, "message", None)
                if msg and not bool(getattr(msg, "out", False)):
                    ev_date = _int(getattr(msg, "edit_date", 0) or getattr(msg, "date", 0))
                    fid = getattr(msg, "from_id", None)
                    if fid and hasattr(fid, "user_id"):
                        uid = _int(fid.user_id)
                    if not uid:
                        peer = getattr(msg, "peer_id", None)
                        if peer and hasattr(peer, "user_id"):
                            uid = _int(peer.user_id)
                    via = "edit"
            elif update_name in ("TL_updateBotMessageReaction", "TL_updateMessageReactions"):
                actor = getattr(update, "actor", None)
                if actor and hasattr(actor, "user_id"):
                    uid = _int(actor.user_id)
                if not uid:
                    peer = getattr(update, "peer", None)
                    if peer and hasattr(peer, "user_id"):
                        uid = _int(peer.user_id)
                ev_date = _int(getattr(update, "date", 0))
                via = "reaction"
            if uid <= 0:
                return
            try:
                my = int(UserConfig.getInstance(account).getClientUserId())
                if uid == my:
                    return
            except Exception:
                pass
            now = self._presence_now()
            ts = ev_date if (ev_date > 0 and ev_date <= now + 300) else now
            self._presence_remember(uid, ts, via, account)
        except Exception:
            pass

    def _presence_user_name(self, uid):
        try:
            u = MessagesController.getInstance(int(UserConfig.selectedAccount)).getUser(Long(int(uid)))
            if u:
                nm = (str(getattr(u, "first_name", "") or "") + " " + str(getattr(u, "last_name", "") or "")).strip()
                if nm:
                    return nm
                un = str(getattr(u, "username", "") or "")
                if un:
                    return "@" + un
        except Exception:
            pass
        return f"id{uid}"

    def _presence_fmt_dur(self, sec):
        sec = int(sec)
        if self.dex and self.dex.loaded:
            try:
                return str(self.dex.call("formatDuration", jlong(sec)))
            except Exception:
                pass
        if sec < 60:
            return f"{sec}s"
        if sec < 3600:
            return f"{sec // 60}m"
        return f"{sec // 3600}h{(sec % 3600) // 60}m"

    def _load_fp(self):
        try:
            self._fp_cache = json.loads(str(self._prefs().getString(PREMIUM_CACHE_KEY, "{}")))
        except Exception:
            self._fp_cache = {}

    def _save_fp(self):
        if not self._fp_dirty:
            return
        try:
            self._prefs().edit().putString(PREMIUM_CACHE_KEY, json.dumps(self._fp_cache)).apply()
            self._fp_dirty = False
        except Exception:
            pass

    def _show_presence_tracked(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                ctx = frag.getParentActivity()
                uids = sorted(plugin._presence_tracked)
                items = [f"{plugin._presence_user_name(u)} (id{u})" for u in uids]
                def on_pick(dlg, which):
                    dlg.dismiss()
                    uid = uids[which]
                    def confirm():
                        f2 = get_last_fragment()
                        if not f2 or not f2.getParentActivity():
                            return
                        d = AlertDialogBuilder(f2.getParentActivity())
                        d.set_title(L.g("remove"))
                        d.set_message(plugin._presence_user_name(uid))
                        d.set_positive_button(L.g("yes"), lambda dd, w: (dd.dismiss(), do_rm(uid)))
                        d.set_negative_button(L.g("no"), lambda dd, w: (dd.dismiss(), run_on_ui_thread(_ui)))
                        d.show()
                    def do_rm(u):
                        plugin._presence_tracked.discard(int(u))
                        plugin._presence_lamp.pop(int(u), None)
                        plugin._presence_open.pop(int(u), None)
                        plugin._presence_dirty = True
                        BulletinHelper.show_info(L.g("removed"))
                        run_on_ui_thread(_ui)
                    run_on_ui_thread(confirm)
                def on_add(dlg, w):
                    dlg.dismiss()
                    run_on_ui_thread(plugin._show_presence_add)
                d = AlertDialogBuilder(ctx)
                if items:
                    d.set_title(f"{L.g('tracked_users')} ({len(items)})")
                    d.set_items(items, on_pick)
                else:
                    d.set_title(L.g("tracked_users"))
                    d.set_message(L.g("list_empty"))
                d.set_positive_button(L.g("add"), on_add)
                d.set_negative_button(L.g("close"), lambda dd, w: dd.dismiss())
                d.show()
            except Exception as e:
                log(f"[MAX] presTracked: {e}")
        run_on_ui_thread(_ui)

    def _show_presence_add(self):
        plugin = self
        def on_picked(ids):
            added = 0
            for uid in ids:
                if uid not in plugin._presence_tracked:
                    plugin._presence_tracked.add(uid)
                    added += 1
            if added:
                plugin._presence_dirty = True
                plugin._save_presence()
                if plugin._gs("save_last_online", False) or plugin._gs("presence_track", False):
                    plugin._register_presence_hooks()
                BulletinHelper.show_info(f"{L.g('added')}: {added}")
        self._pick_chats(on_picked)

    def _resolve_and_add(self, raw):
        plugin = self
        try:
            v = raw.lstrip("@").strip()
            if v.lstrip("-").isdigit():
                uid = int(v)
                if uid <= 0:
                    BulletinHelper.show_error(L.g("invalid_id"))
                    return
                plugin._presence_tracked.add(uid)
                plugin._presence_dirty = True
                BulletinHelper.show_info(L.g("added"))
                return
            req = TLRPC.TL_contacts_resolveUsername()
            req.username = v
            def done(resp, err):
                try:
                    if err or not resp:
                        run_on_ui_thread(lambda: BulletinHelper.show_error(L.g("not_found")))
                        return
                    users = _jlist(getattr(resp, "users", None))
                    if users:
                        uid = int(str(users[0].id))
                        plugin._presence_tracked.add(uid)
                        plugin._presence_dirty = True
                        run_on_ui_thread(lambda: BulletinHelper.show_info(L.g("added")))
                    else:
                        run_on_ui_thread(lambda: BulletinHelper.show_error(L.g("not_found")))
                except Exception:
                    run_on_ui_thread(lambda: BulletinHelper.show_error(L.g("error")))
            send_request(req, RequestCallback(done))
        except Exception as e:
            log(f"[MAX] resolve: {e}")

    def _show_presence_log(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                uids = sorted(plugin._presence_tracked)
                if not uids:
                    BulletinHelper.show_info(L.g("list_empty"))
                    return
                items = []
                for uid in uids:
                    cnt = len(plugin._presence_records.get(str(uid), []))
                    lamp = "[ON] " if plugin._presence_lamp_on(uid) else ""
                    items.append(f"{lamp}{plugin._presence_user_name(uid)} — {cnt}")
                def on_pick(dlg, which):
                    dlg.dismiss()
                    uid = uids[which]
                    def _detail():
                        f2 = get_last_fragment()
                        if not f2 or not f2.getParentActivity():
                            return
                        rows = list(plugin._presence_records.get(str(int(uid)), []))
                        lines = []
                        for r in reversed(rows[-200:]):
                            try:
                                start, end, via = int(r[0]), int(r[1]), str(r[2])
                                a = _format_date(start)
                                dur = max(0, end - start)
                                lines.append(f"- {a} ({plugin._presence_fmt_dur(dur)}, {via})")
                            except Exception:
                                pass
                        body = "\n".join(lines) if lines else L.g("no_records")
                        d = AlertDialogBuilder(f2.getParentActivity())
                        d.set_title(f"{plugin._presence_user_name(uid)} ({len(rows)})")
                        d.set_message(body)
                        d.set_negative_button(L.g("close"), lambda dd, w: dd.dismiss())
                        d.show()
                    run_on_ui_thread(_detail)
                d = AlertDialogBuilder(frag.getParentActivity())
                d.set_title(L.g("online_journal"))
                d.set_items(items, on_pick)
                d.set_negative_button(L.g("close"), lambda dd, w: dd.dismiss())
                d.show()
            except Exception as e:
                log(f"[MAX] presLog: {e}")
        run_on_ui_thread(_ui)

    def _wipe_vault(self, _v=None):
        plugin = self
        def _ui():
            frag = get_last_fragment()
            if not frag or not frag.getParentActivity():
                return
            d = AlertDialogBuilder(frag.getParentActivity())
            d.set_title(L.g("wipe_storage"))
            d.set_message(L.g("wipe_confirm"))
            def do_wipe(dlg, w):
                dlg.dismiss()
                if plugin.dex and plugin.dex.loaded:
                    try:
                        plugin.dex.call("pyWipeAll")
                    except Exception:
                        pass
                if plugin.vault:
                    plugin.vault.wipe()
                plugin._invalidate_removed_cache()
                try:
                    attach_dir = _get_attach_dir()
                    if attach_dir and os.path.isdir(attach_dir):
                        import shutil
                        shutil.rmtree(attach_dir, ignore_errors=True)
                        os.makedirs(attach_dir, exist_ok=True)
                except Exception:
                    pass
                BulletinHelper.show_info(L.g("cleared"))
            d.set_positive_button(L.g("delete"), do_wipe)
            d.set_negative_button(L.g("cancel"), lambda dd, w: dd.dismiss())
            d.show()
        run_on_ui_thread(_ui)

    def _show_rx_manager(self, _v=None):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                act = frag.getParentActivity()
                patterns = plugin.vault.get_rx() if plugin.vault else []
                if not patterns:
                    d = AlertDialogBuilder(act)
                    d.set_title(L.g("patterns"))
                    d.set_message(L.g("list_empty"))
                    d.set_positive_button(L.g("add"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(lambda: plugin._show_rx_add())))
                    d.set_negative_button(L.g("close"), lambda dlg, w: dlg.dismiss())
                    d.show()
                    return
                items = [f"#{i+1}: {p}" for i, p in enumerate(patterns)]
                def on_pick(dlg, which):
                    dlg.dismiss()
                    pats = plugin.vault.get_rx() if plugin.vault else []
                    rem_p = pats[which] if 0 <= which < len(pats) else None
                    plugin.vault.remove_rx(which)
                    if rem_p:
                        try:
                            plugin.dex.call("pyRemoveRegex", JString(rem_p))
                        except Exception:
                            pass
                    BulletinHelper.show_info(L.g("removed"))
                    run_on_ui_thread(_ui)
                dlg = AlertDialogBuilder(act)
                dlg.set_title(f"{L.g('patterns')} ({len(patterns)})")
                dlg.set_items(items, on_pick)
                dlg.set_positive_button(L.g("add"), lambda d, w: (d.dismiss(), run_on_ui_thread(lambda: plugin._show_rx_add())))
                dlg.set_negative_button(L.g("close"), lambda d, w: d.dismiss())
                dlg.show()
            except Exception as e:
                log(f"[MAX] rxMgr: {e}")
        run_on_ui_thread(_ui)

    def _show_rx_add(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                if not frag or not frag.getParentActivity():
                    return
                act = frag.getParentActivity()
                d = AlertDialogBuilder(act)
                d.set_title(L.g("add_pattern"))
                inp = EditTextBoldCursor(act)
                inp.setHint("regex")
                inp.setInputType(InputType.TYPE_CLASS_TEXT)
                inp.setSingleLine(True)
                inp.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText))
                inp.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText))
                inp.setBackgroundDrawable(None)
                inp.setLineColors(
                    Theme.getColor(Theme.key_windowBackgroundWhiteInputField),
                    Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated),
                    Theme.getColor(Theme.key_text_RedRegular),
                )
                _wrap = JLinearLayout(act)
                _wrap.setOrientation(JLinearLayout.VERTICAL)
                _side = AndroidUtilities.dp(24)
                _bottom = AndroidUtilities.dp(8)
                _wrap.setPadding(_side, _side, _side, _bottom)
                _wrap.addView(inp, JLinearLayout.LayoutParams(-1, -2))
                d.set_view(_wrap)
                def _on_add(dlg, w):
                    p = str(inp.getText()).strip()
                    if not p:
                        return
                    try:
                        re.compile(p)
                    except Exception:
                        BulletinHelper.show_error(L.g("invalid_regex"))
                        return
                    plugin.vault.add_rx(p)
                    try:
                        plugin.dex.call("pyAddRegex", JString(p))
                    except Exception:
                        pass
                    inp.setText("")
                    BulletinHelper.show_info(L.g("added"))
                    dlg.dismiss()
                d.set_positive_button(L.g("add"), _on_add)
                d.set_negative_button(L.g("close"), lambda dlg, w: dlg.dismiss())
                dlg_obj = d.show()
                _focus_and_show_keyboard(dlg_obj, inp)
            except Exception as e:
                log(f"[MAX] rxAdd: {e}")
        run_on_ui_thread(_ui)

    def create_settings(self):
        try:
            items = self._inner_create_settings()
        except Exception:
            items = []
        if hasattr(self, '_author_helper'):
            return self._author_helper.prepend_settings(items)
        return items

    def _inner_create_settings(self):
        # Bug fix: previously the whole list was built inside a single try/except,
        # so if ANY single entry threw (e.g. _StickerFactory.instance not ready yet
        # on first open), the entire tab list vanished and got replaced by one
        # "Error loading settings" row. Build each entry independently instead, so
        # a single failure only drops that one row instead of every tab.
        log("[MAX] create_settings: building settings UI")
        entries = []
        try:
            entries.append(Header(text=L.g("settings_categories")))
        except Exception as e:
            log(f"[MAX] create_settings: header failed: {e}")
        for text_key, icon, sub in (
            ("ghost", "ghost_solar", self._pg),
            ("spy", "msg_secret", self._ps),
            ("filters", "msg_search", self._pf),
            ("hacker", "msg_fave", self._ph),
        ):
            try:
                entries.append(Text(text=L.g(text_key), icon=icon, create_sub_fragment=sub, link_alias="page_" + text_key))
            except Exception as e:
                log(f"[MAX] create_settings: '{text_key}' tab failed: {e}")
        try:
            entries.append(Divider())
            entries.append(Text(text=L.g("settings_misc"), icon="msg_settings", create_sub_fragment=self._pmisc, link_alias="page_misc"))
        except Exception as e:
            log(f"[MAX] create_settings: misc tab failed: {e}")
        if not entries:
            return [Text(text="Error loading settings", icon="msg_error")]
        return entries

    def _tab_master(self, tab, header, icon):
        return [
            Header(text=L.g(header)),
            Switch(key=tab, text=L.g("enable_features"), default=True, subtext=L.g("enable_features_sub"), icon=icon, on_change=lambda v: self._on_tab_toggle(tab, v), link_alias=tab),
        ]

    def _on_tab_toggle(self, tab, v):
        try:
            self._sc.clear()
            self.set_setting(tab, v, reload_settings=True)
        except Exception as e:
            log(f"[MAX] tabToggle: {e}")

    def _tab_on(self, tab):
        try:
            return bool(self.get_setting(tab, True))
        except Exception:
            return True

    def _mode_items(self):
        return [L.g("sel_always_off"), L.g("sel_with_ghost"), L.g("sel_always_on")]

    def _stories_mode_items(self):
        return [L.g("sel_always_off"), L.g("sel_with_ghost"), L.g("sel_always_on"), L.g("sel_ask_on_open")]

    def _silent_mode_items(self):
        return [L.g("silent_never"), L.g("silent_always"), L.g("silent_ghost")]

    def _pg(self):
        # Вкладка призрака перенесена из elyx: функции-селекторы с тремя режимами
        # (всегда выкл / только с призраком / всегда вкл), для сторис — четвёртый
        # режим "спрашивать при открытии", беззвучная отправка тремя режимами.
        items = self._tab_master("tab_ghost", "tab_ghost", "ghost_solar")
        if not self._tab_on("tab_ghost"):
            if hasattr(self, "_author_helper"):

                return self._author_helper.prepend_settings(items)

            return items
        items += [
            Text(text=L.g("exclusions"), icon="msg_permissions", subtext=L.g("exclusions_row_sub"), on_click=lambda v: self._show_exclusions_manager(), link_alias="ghost_exclusions"),
            Divider(),
            Switch(key="ghost_mode", text=L.g("ghost_toggle"), default=False,
                   subtext=L.g("ghost_toggle_sub"), icon="ghost_solar",
                   on_change=self._on_ghost_change, link_alias="ghost_mode"),
            Switch(key="drawer_ghost_btn", text=L.g("drawer_btn"), default=True, link_alias="ghost_drawer_btn"),
            Divider(),
            Header(text=L.g("ghost_settings")),
            Selector(key="ghost_no_online", text=L.g("no_online"),
                     default=1, items=self._mode_items(), link_alias="ghost_no_online"),
            Selector(key="ghost_immediate_offline", text=L.g("instant_offline"),
                     default=1, items=self._mode_items(), link_alias="ghost_instant_offline"),
            Selector(key="ghost_no_typing", text=L.g("no_typing"),
                     default=1, items=self._mode_items(), link_alias="ghost_no_typing"),
            Selector(key="ghost_no_read", text=L.g("no_read"),
                     default=1, items=self._mode_items(), link_alias="ghost_no_read"),
            Selector(key="ghost_no_read_stories", text=L.g("no_read_stories"),
                     default=1, items=self._stories_mode_items(), link_alias="ghost_no_read_stories"),
            Selector(key="silent_send", text=L.g("silent_send"),
                     default=0, items=self._silent_mode_items(), link_alias="ghost_silent_send"),
            Divider(),
            Switch(key="read_on_action", text=L.g("read_on_action"), default=True,
                   subtext=L.g("read_on_action_sub"), link_alias="ghost_read_on_action"),
            Switch(key="use_scheduled", text=L.g("use_scheduled"), default=False, link_alias="ghost_use_scheduled"),
            Divider(),
            Header(text=L.g("local_read_header")),
            Switch(key="local_read_msgs", text=L.g("local_read_msgs"),
                   default=True, subtext=L.g("local_read_msgs_sub"), link_alias="local_read_msgs"),
            Switch(key="local_read_private", text=L.g("local_read_private"),
                   default=True, link_alias="local_read_private"),
            Switch(key="local_read_groups", text=L.g("local_read_groups"),
                   default=True, link_alias="local_read_groups"),
            Switch(key="local_read_channels", text=L.g("local_read_channels"),
                   default=True, link_alias="local_read_channels"),
            Switch(key="local_read_stories", text=L.g("local_read_stories"),
                   default=True, subtext=L.g("local_read_stories_sub"), link_alias="local_read_stories"),
            Divider(),
            Header(text=L.g("read_whitelist")),
            Text(text=L.g("read_wl_users"), icon="msg_contacts", subtext=L.g("read_whitelist_sub"), on_click=lambda v: self._show_read_whitelist(), link_alias="read_whitelist"),
        ]
        return items

    def _open_del_customize(self, _v=None):
        try:
            ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0).edit().putBoolean("open_del_cust", True).apply()
            self.dex.call("pyFlushConfig")
        except Exception as e:
            BulletinHelper.show_info("err: " + str(e)[:55])

    def _ps(self):
        # Вкладка Шпиона по структуре re:extera: мастер-выключатель сохранения
        # удалёнок, дополнительные настройки (история/одноразовые/боты/свои),
        # кнопка кастомизации вида, хранилище вложений, трекер присутствия.
        plugin = self
        items = self._tab_master("tab_spy", "tab_spy", "msg_secret")
        if not self._tab_on("tab_spy"):
            return items
        items += [
            Header(text=L.g("max_vigilance")),
            Switch(key="keep_app_alive", text=L.g("keep_alive"), default=False, subtext=L.g("keep_alive_sub"), link_alias="spy_keep_alive"),
            Divider(),
            Header(text=L.g("spy_mode")),
            Switch(key="save_deleted", text=L.g("save_deleted"), default=True, subtext=L.g("save_deleted_sub"), link_alias="spy_save_deleted"),
            Switch(key="save_edits", text=L.g("save_edits"), default=True, subtext=L.g("save_edits_sub"), link_alias="spy_save_edits"),
            Switch(key="keep_once", text=L.g("save_once_msgs"), default=False, subtext=L.g("save_once_msgs_sub"), link_alias="spy_keep_once"),
            Switch(key="save_bot_chats", text=L.g("save_bots"), default=False, link_alias="spy_save_bots"),
            Switch(key="track_own_deletions", text=L.g("save_own_del"), default=False, link_alias="spy_own_deletions"),
            Text(text=L.g("del_customize"), icon="msg_customize", on_click=lambda v: self._open_del_customize(), link_alias="spy_del_customize"),
            Text(text=L.g("clear_all_deleted"), icon="msg_delete", subtext=L.g("clear_all_deleted_sub"), on_click=lambda v: plugin._on_clear_all_deleted_click(), link_alias="spy_clear_deleted"),
            Text(text=L.g("sync_all_chats"), icon="msg_retry", subtext=L.g("sync_all_chats_sub"), on_click=lambda v: plugin._on_sync_all_chats_click(), link_alias="spy_sync_chats"),
            Divider(),
            Header(text=L.g("storage")),
            Switch(key="save_attachments", text=L.g("save_attachments"), default=False, on_change=self._on_save_attachments_change, link_alias="spy_save_attachments"),
            Switch(key="save_attach_private", text=L.g("save_attach_private"), default=True, link_alias="spy_attach_private"),
            Switch(key="save_attach_groups", text=L.g("save_attach_groups"), default=True, link_alias="spy_attach_groups"),
            Switch(key="save_attach_channels", text=L.g("save_attach_channels"), default=True, link_alias="spy_attach_channels"),
            Text(text=L.g("attach_folder"), icon="msg_folder", subtext=plugin._get_current_folder_display(), on_click=plugin._pick_attach_folder, link_alias="spy_attach_folder"),
            Text(text=L.g("attach_folder_reset"), icon="msg_reset", on_click=plugin._reset_attach_folder, link_alias="spy_attach_folder_reset"),
            Selector(key="max_folder_size", text=L.g("max_folder"), default=2, items=["300 MB", "1 GB", "2 GB", "4 GB", "8 GB", "16 GB", "No limit"], link_alias="spy_max_folder"),
            Selector(key="auto_cleanup_age", text=L.g("auto_cleanup"), default=0, items=["Off", "3 days", "7 days", "14 days", "30 days", "90 days"], link_alias="spy_auto_cleanup"),
            Text(text=L.g("browse_attachments"), icon="msg_gallery", subtext=L.g("browse_attachments_sub"), on_click=plugin._show_browse_attachments, link_alias="spy_browse_attachments"),
            Text(text=L.g("import_from_file"), icon="msg_import", subtext=L.g("import_from_file_sub"), on_click=plugin._import_from_file_picker, link_alias="spy_import_file"),
            Text(text=L.g("wipe_storage"), icon="msg_clear", on_click=plugin._wipe_vault, link_alias="spy_wipe_storage"),
            Divider(),
            Header(text=L.g("tracker")),
            Switch(key="save_last_online", text=L.g("save_last_seen"), default=False, subtext=L.g("save_last_seen_sub"), on_change=self._on_save_last_online_change, link_alias="spy_last_online"),
        ]
        return items

    def _wipe_presence(self):
        self._presence_records = {}
        self._presence_lamp = {}
        self._presence_open = {}
        self._presence_dirty = True
        self._save_presence()
        BulletinHelper.show_info(L.g("cleared"))

    def _pf(self):
        self._load_filters()
        items = [
            Header(text=L.g("enable_filters")),
            Switch(key="filters_enabled", text=L.g("enable_features"), default=True, subtext=L.g("enable_features_sub"), icon="msg_search", on_change=lambda v: self._on_tab_toggle("filters_enabled", v), link_alias="filters_enabled"),
            Text(text=L.g("shadowban"), icon="msg_block", subtext=L.g("shadowban_row_sub"), on_click=lambda v: self._show_shadowban_manager(), link_alias="filters_shadowban"),
            Text(text=L.g("regex_quick"), icon="msg_search", subtext=L.g("regex_quick_sub"), on_click=lambda v: self._show_rx_manager(), link_alias="filters_regex_quick"),
            Divider(),
        ]
        if not self._tab_on("filters_enabled"):
            return items
        items += [
            Text(text=L.g("regex_patterns"), icon="msg_search", on_click=self._show_rx_manager, link_alias="filters_patterns"),
            Divider(),
            Text(text=L.g("add_filter"), icon="msg_add", create_sub_fragment=self._fe_fragment(-1), link_alias="filters_add"),
        ]
        for i, f in enumerate(self._filters):
            items.append(Text(text=f"{'[+]' if f.get('enabled',True) else '[-]'} {f.get('expression','')}", subtext=L.g("inverted") if f.get("inverted") else None, create_sub_fragment=self._fe_fragment(i), link_alias="filter_%d" % i))
        return items

    def _fe_fragment(self, idx):
        seq = getattr(self, "_fe_seq", 0) + 1
        self._fe_seq = seq
        key = "fe%d" % seq
        def open_editor():
            try:
                return self._fe_ui(idx, key)
            except Exception:
                return []
        return open_editor

    def _fe_ui(self, index, key):
        try:
            is_new = index == -1
            f = {"expression": "", "enabled": True, "ignore_case": True, "inverted": False, "channels_only": False}
            if not is_new and 0 <= index < len(self._filters):
                f = self._filters[index]
            pl = self
            k_expr = key + "_e"
            k_on = key + "_on"
            k_ic = key + "_ic"
            k_inv = key + "_inv"
            k_cc = key + "_cc"
            def back_to_filters():
                try:
                    frag = get_last_fragment()
                    if frag is not None:
                        frag.finishFragment()
                except Exception:
                    pass
            def clean():
                try:
                    pl.set_setting(k_expr, "")
                    pl.set_setting(k_on, True)
                    pl.set_setting(k_ic, True)
                    pl.set_setting(k_inv, False)
                    pl.set_setting(k_cc, False)
                except Exception:
                    pass
            def save(v):
                try:
                    e = pl.get_setting(k_expr, "")
                    if not e:
                        BulletinHelper.show_info(L.g("enter_expr"))
                        return
                    entry = {"expression": e, "enabled": bool(pl.get_setting(k_on, True)), "ignore_case": bool(pl.get_setting(k_ic, True)), "inverted": bool(pl.get_setting(k_inv, False)), "channels_only": bool(pl.get_setting(k_cc, False))}
                    if is_new:
                        pl._filters.append(entry)
                    else:
                        pl._filters[index] = entry
                    pl._save_filters()
                    BulletinHelper.show_info(L.g("saved"))
                    clean()
                    try:
                        pl.set_setting(k_expr, e, reload_settings=True)
                    except Exception:
                        pass
                    run_on_ui_thread(back_to_filters)
                except Exception:
                    pass
            def delete(v):
                try:
                    if not is_new and 0 <= index < len(pl._filters):
                        del pl._filters[index]
                        pl._save_filters()
                        BulletinHelper.show_info(L.g("removed"))
                        clean()
                        try:
                            pl.set_setting(k_expr, "", reload_settings=True)
                        except Exception:
                            pass
                        run_on_ui_thread(back_to_filters)
                except Exception:
                    pass
            items = [
                Header(text=L.g("new_filter") if is_new else L.g("editor")),
                Input(key=k_expr, text=L.g("regex_expr"), default=f.get("expression", ""), link_alias=k_expr),
                Switch(key=k_on, text=L.g("enabled"), default=f.get("enabled", True), link_alias=k_on),
                Switch(key=k_ic, text=L.g("case_insensitive"), default=f.get("ignore_case", True), link_alias=k_ic),
                Switch(key=k_inv, text=L.g("inverted"), default=f.get("inverted", False), subtext=L.g("inverted_sub"), link_alias=k_inv),
                Switch(key=k_cc, text=L.g("channels_only"), default=f.get("channels_only", False), subtext=L.g("channels_only_sub"), link_alias=k_cc),
                Divider(),
                Text(text=L.g("save"), icon="msg_check", on_click=save, link_alias=key + "_save"),
            ]
            if not is_new:
                items.append(Text(text=L.g("delete"), icon="msg_delete", on_click=delete, link_alias=key + "_delete"))
            return items
        except Exception:
            return []

    def _ph(self):
        items = self._tab_master("tab_hacker", "tab_hacker", "msg_fave")
        if not self._tab_on("tab_hacker"):
            return items
        return items + [
            Header(text=L.g("hacker")),
            Switch(key="hide_ads", text=L.g("hide_ads"), default=True, link_alias="hacker_hide_ads"),
            Switch(key="hide_feed_ads", text=L.g("hide_feed_ads"), default=True, subtext=L.g("hide_feed_ads_sub"), link_alias="hacker_hide_feed_ads"),
            Switch(key="no_forward_limit", text=L.g("no_fwd_limit"), default=False, subtext=L.g("no_fwd_limit_sub"), link_alias="hacker_no_fwd_limit"),
            Switch(key="fake_premium", text=L.g("local_premium"), default=False, subtext=L.g("local_premium_sub"), link_alias="hacker_fake_premium"),
            Switch(key="keep_once", text=L.g("once_media"), default=False, subtext=L.g("once_media_sub"), link_alias="hacker_keep_once"),
            Divider(),
            Header(text=L.g("bypass")),
            Switch(key="allow_screenshots", text=L.g("screenshots"), default=False, subtext=L.g("screenshots_sub"), link_alias="hacker_screenshots"),
            Switch(key="bypass_noforward", text=L.g("noforwards"), default=False, subtext=L.g("noforward_sub"), link_alias="hacker_noforward"),
            Switch(key="gif_reuploader", text=L.g("gif_reuploader"), default=True, subtext=L.g("gif_reuploader_sub"), link_alias="hacker_gif_reuploader"),
            Selector(key="fwd_mode", text=L.g("fwd_mode"), default=0, items=[L.g("fwd_copy"), L.g("fwd_try")], link_alias="hacker_fwd_mode"),
            Switch(key="bypass_story_save", text=L.g("stories_bypass"), default=False, subtext=L.g("stories_bypass_sub"), link_alias="hacker_story_save"),
        ]

    def _show_shadowban_manager(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                entries = JMain.ShadowbanStore.all()
                b = AlertDialogBuilder(act)
                b.set_title(L.g("shadowban"))
                uids = []
                if entries and not entries.isEmpty():
                    items = []
                    for i in range(entries.size()):
                        e = entries.get(i)
                        uids.append(int(e.userId))
                        d_st = L.g("sb_dialog_hidden") if e.hideDialog else L.g("sb_dialog_visible")
                        g_st = L.g("sb_groups_hidden") if e.hideInGroups else L.g("sb_groups_visible")
                        items.append(f"{plugin._presence_user_name(int(e.userId))}\n{d_st} · {g_st}")
                    def on_pick(dlg, which):
                        dlg.dismiss()
                        if 0 <= which < len(uids):
                            run_on_ui_thread(lambda: plugin._shadowban_item_actions(uids[which]))
                    b.set_items(items, on_pick)
                    def on_clear(dlg, w):
                        dlg.dismiss()
                        def confirm():
                            f2 = get_last_fragment()
                            a2 = f2.getParentActivity() if f2 else None
                            if not a2:
                                return
                            d2 = AlertDialogBuilder(a2)
                            d2.set_title(L.g("shadowban"))
                            d2.set_message(L.g("clear_list_confirm"))
                            def do_clear(dd, w2):
                                dd.dismiss()
                                for u in uids:
                                    JMain.ShadowbanStore.remove(jlong(u))
                                BulletinHelper.show_info(L.g("cleared"))
                                run_on_ui_thread(plugin._show_shadowban_manager)
                            d2.set_positive_button(L.g("yes"), do_clear)
                            d2.set_negative_button(L.g("no"), lambda dd, w2: (dd.dismiss(), run_on_ui_thread(plugin._show_shadowban_manager)))
                            d2.show()
                        run_on_ui_thread(confirm)
                    b.set_negative_button(L.g("read_wl_clear_all"), on_clear)
                else:
                    b.set_message(L.g("shadowban_empty"))
                b.set_positive_button(L.g("add"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._shadowban_add_menu)))
                b.set_neutral_button(L.g("close"), lambda dlg, w: dlg.dismiss())
                b.show()
            except Exception as e:
                log(f"[MAX] shadowban manager error: {e}")
        run_on_ui_thread(_ui)

    def _shadowban_item_actions(self, uid):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                e = JMain.ShadowbanStore.map.get(jlong(uid))
                hd = bool(e.hideDialog) if e is not None else True
                hg = bool(e.hideInGroups) if e is not None else True
                items = [
                    f"{L.g('shadowban_hide_dialog')}: {'✓' if hd else '✗'}",
                    f"{L.g('shadowban_hide_groups')}: {'✓' if hg else '✗'}",
                    L.g("shadowban_remove"),
                ]
                def on_pick(dlg, which):
                    dlg.dismiss()
                    if which == 0:
                        JMain.ShadowbanStore.add(jlong(uid), not hd, hg)
                    elif which == 1:
                        JMain.ShadowbanStore.add(jlong(uid), hd, not hg)
                    elif which == 2:
                        JMain.ShadowbanStore.remove(jlong(uid))
                        BulletinHelper.show_info(L.g("shadowban_removed"))
                    run_on_ui_thread(plugin._show_shadowban_manager)
                b = AlertDialogBuilder(act)
                b.set_title(plugin._presence_user_name(uid))
                b.set_items(items, on_pick)
                b.set_negative_button(L.g("back"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_shadowban_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] shadowban item error: {e}")
        run_on_ui_thread(_ui)

    def _shadowban_add_menu(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                def on_picked(ids):
                    added = 0
                    for uid in ids:
                        if uid > 0:
                            JMain.ShadowbanStore.add(jlong(uid), True, True)
                            added += 1
                    if added:
                        BulletinHelper.show_info(f"{L.g('shadowban_added')}: {added}")
                    run_on_ui_thread(plugin._show_shadowban_manager)
                b = AlertDialogBuilder(act)
                b.set_title(L.g("shadowban"))
                b.set_items([L.g("pick_chat"), L.g("by_id")], lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(lambda: plugin._pick_chats(on_picked) if w == 0 else plugin._prompt_add_shadowban())))
                b.set_negative_button(L.g("back"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_shadowban_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] shadowban add menu error: {e}")
        run_on_ui_thread(_ui)

    def _prompt_add_shadowban(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                b = AlertDialogBuilder(act)
                b.set_title(L.g("shadowban"))
                b.set_message(L.g("enter_user_id"))
                b.set_input_text("", "User ID")
                def _on_save(val):
                    try:
                        uid = int(str(val).strip())
                        if uid > 0:
                            JMain.ShadowbanStore.add(jlong(uid), True, True)
                            BulletinHelper.show_success(L.g("shadowban_added"))
                            run_on_ui_thread(plugin._show_shadowban_manager)
                        else:
                            BulletinHelper.show_error(L.g("invalid_id"))
                    except Exception:
                        BulletinHelper.show_error(L.g("invalid_id"))
                b.set_positive_button(L.g("save"), lambda dlg, w: (dlg.dismiss(), _on_save(b.get_input_text())))
                b.set_negative_button(L.g("cancel"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_shadowban_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] prompt shadowban error: {e}")
        run_on_ui_thread(_ui)

    def _show_exclusions_manager(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                entries = JMain.ExclusionsStore.all()
                mode_names = {0: L.g("mode_default"), 1: L.g("mode_always"), 2: L.g("mode_never")}
                b = AlertDialogBuilder(act)
                b.set_title(L.g("exclusions"))
                dids = []
                if entries and not entries.isEmpty():
                    items = []
                    for i in range(entries.size()):
                        e = entries.get(i)
                        dids.append(int(e.dialogId))
                        r = mode_names.get(int(e.reading), "?")
                        t = mode_names.get(int(e.typing), "?")
                        items.append(f"{plugin._chat_display_name(int(e.dialogId))}\n{L.g('exclusions_reading')}: {r} · {L.g('exclusions_typing')}: {t}")
                    def on_pick(dlg, which):
                        dlg.dismiss()
                        if 0 <= which < len(dids):
                            run_on_ui_thread(lambda: plugin._exclusion_item_actions(dids[which]))
                    b.set_items(items, on_pick)
                    def on_clear(dlg, w):
                        dlg.dismiss()
                        def confirm():
                            f2 = get_last_fragment()
                            a2 = f2.getParentActivity() if f2 else None
                            if not a2:
                                return
                            d2 = AlertDialogBuilder(a2)
                            d2.set_title(L.g("exclusions"))
                            d2.set_message(L.g("clear_list_confirm"))
                            def do_clear(dd, w2):
                                dd.dismiss()
                                for d in dids:
                                    JMain.ExclusionsStore.remove(jlong(d))
                                BulletinHelper.show_info(L.g("cleared"))
                                run_on_ui_thread(plugin._show_exclusions_manager)
                            d2.set_positive_button(L.g("yes"), do_clear)
                            d2.set_negative_button(L.g("no"), lambda dd, w2: (dd.dismiss(), run_on_ui_thread(plugin._show_exclusions_manager)))
                            d2.show()
                        run_on_ui_thread(confirm)
                    b.set_negative_button(L.g("read_wl_clear_all"), on_clear)
                else:
                    b.set_message(L.g("exclusions_empty"))
                b.set_positive_button(L.g("add"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._exclusion_add_menu)))
                b.set_neutral_button(L.g("close"), lambda dlg, w: dlg.dismiss())
                b.show()
            except Exception as e:
                log(f"[MAX] exclusions manager error: {e}")
        run_on_ui_thread(_ui)

    def _exclusion_item_actions(self, did):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                items = [L.g("edit"), L.g("delete")]
                def on_pick(dlg, which):
                    dlg.dismiss()
                    if which == 0:
                        run_on_ui_thread(lambda: plugin._prompt_configure_exclusion_modes(did))
                    elif which == 1:
                        JMain.ExclusionsStore.remove(jlong(did))
                        BulletinHelper.show_info(L.g("exclusions_removed"))
                        run_on_ui_thread(plugin._show_exclusions_manager)
                b = AlertDialogBuilder(act)
                b.set_title(plugin._chat_display_name(did))
                b.set_items(items, on_pick)
                b.set_negative_button(L.g("back"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_exclusions_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] exclusion item error: {e}")
        run_on_ui_thread(_ui)

    def _exclusion_add_menu(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                def on_picked(ids):
                    if ids:
                        run_on_ui_thread(lambda: plugin._prompt_configure_exclusion_modes(ids[0]))
                b = AlertDialogBuilder(act)
                b.set_title(L.g("exclusions"))
                b.set_items([L.g("pick_chat"), L.g("by_id")], lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(lambda: plugin._pick_chats(on_picked) if w == 0 else plugin._prompt_add_exclusion())))
                b.set_negative_button(L.g("back"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_exclusions_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] exclusion add menu error: {e}")
        run_on_ui_thread(_ui)

    def _prompt_add_exclusion(self):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                b = AlertDialogBuilder(act)
                b.set_title(L.g("exclusions"))
                b.set_message(L.g("enter_dialog_id"))
                b.set_input_text("", "Dialog ID")
                def _on_save(val):
                    try:
                        did = int(str(val).strip())
                        if did != 0:
                            run_on_ui_thread(lambda: plugin._prompt_configure_exclusion_modes(did))
                        else:
                            BulletinHelper.show_error(L.g("invalid_id"))
                    except Exception:
                        BulletinHelper.show_error(L.g("invalid_id"))
                b.set_positive_button(L.g("add"), lambda dlg, w: (dlg.dismiss(), _on_save(b.get_input_text())))
                b.set_negative_button(L.g("cancel"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_exclusions_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] prompt exclusion error: {e}")
        run_on_ui_thread(_ui)

    def _prompt_configure_exclusion_modes(self, did):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                items = [L.g("reading_default"), L.g("reading_always"), L.g("reading_never")]
                b = AlertDialogBuilder(act)
                b.set_title(f"{L.g('reading_for')} {did}")
                curr_r = JMain.ExclusionsStore.getMode(jlong(did), True)
                b.set_single_choice_items(items, curr_r, lambda dlg, which: (dlg.dismiss(), run_on_ui_thread(lambda: plugin._prompt_configure_typing_mode(did, which))))
                b.set_negative_button(L.g("cancel"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_exclusions_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] exclusion reading dlg: {e}")
        run_on_ui_thread(_ui)

    def _prompt_configure_typing_mode(self, did, reading_mode):
        plugin = self
        def _ui():
            try:
                frag = get_last_fragment()
                act = frag.getParentActivity() if frag else None
                if not act:
                    return
                items = [L.g("typing_default"), L.g("typing_always"), L.g("typing_never")]
                b = AlertDialogBuilder(act)
                b.set_title(f"{L.g('typing_for')} {did}")
                curr_t = JMain.ExclusionsStore.getMode(jlong(did), False)
                def _on_select(dlg, which):
                    dlg.dismiss()
                    JMain.ExclusionsStore.set(jlong(did), reading_mode, which)
                    BulletinHelper.show_success(L.g("exclusions_saved"))
                    run_on_ui_thread(plugin._show_exclusions_manager)
                b.set_single_choice_items(items, curr_t, _on_select)
                b.set_negative_button(L.g("cancel"), lambda dlg, w: (dlg.dismiss(), run_on_ui_thread(plugin._show_exclusions_manager)))
                b.show()
            except Exception as e:
                log(f"[MAX] exclusion typing dlg: {e}")
        run_on_ui_thread(_ui)

    def _pmisc(self):
        plugin = self
        def _open_channel(v):
            try:
                from android.content import Intent
                from android.net import Uri
                ctx = ApplicationLoader.applicationContext
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/MaxExteraPlugins"))
                intent.setPackage(ctx.getPackageName())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ctx.startActivity(intent)
            except Exception:
                pass
        # "auto" + recommended (ru, en) come first, then the remaining languages
        # sorted alphabetically by their own autonym.
        _lang_codes = ("auto",) + L._RECOMMENDED + tuple(
            sorted(L._EXTRA, key=lambda c: L._AUTONYMS.get(c, c))
        )
        _lang_items = [L.g("lang_auto")] + [L._AUTONYMS.get(c, c) for c in _lang_codes[1:]]
        def _on_lang_change(new_index):
            try:
                new_lang = _lang_codes[int(new_index)]
                ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0).edit().putString(L._prefs_key, new_lang).apply()
                L._lang_cache = [None, 0]
                plugin.set_setting("plugin_lang", int(new_index), reload_settings=True)
            except Exception:
                pass
        def _cur_lang_idx():
            try:
                raw = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, 0).getString(L._prefs_key, "auto")
            except Exception:
                raw = "auto"
            try:
                return _lang_codes.index(raw)
            except ValueError:
                return 0
        return [
            Header(text=L.g("info")),
            Text(text=L.g("author_channel"), icon="msg_channel", on_click=_open_channel, link_alias="misc_channel"),
            Text(text=L.g("credits"), icon="msg_fave", link_alias="misc_credits", create_sub_fragment=lambda: [
                Header(text=L.g("credits")),
                Text(text="@exteraOwner", subtext=L.g("cr_extera"), link_alias="cr_extera"),
                Text(text="@bleizix", subtext=L.g("cr_bleizix"), link_alias="cr_bleizix"),
                Text(text="@shiawasez", subtext=L.g("cr_shiawasez"), link_alias="cr_shiawasez"),
                Text(text="@ThisisGame_tig", subtext=L.g("cr_thisisgame"), link_alias="cr_thisisgame"),
                Text(text="@summering", subtext=L.g("cr_summering"), link_alias="cr_summering"),
                Text(text="@homewatcha", subtext=L.g("cr_testing"), link_alias="cr_homewatcha"),
                Text(text="@Jebebr", subtext=L.g("cr_testing"), link_alias="cr_jebebr"),
                Text(text="@AdzusaNakano", subtext=L.g("cr_testing"), link_alias="cr_adzusa"),
                Text(text="@Farewalls", subtext=L.g("cr_testing"), link_alias="cr_farewalls"),
                Text(text="@dyr0v_ukraine", subtext=L.g("cr_testing"), link_alias="cr_dyr0v"),
                Text(text="@ablipo", subtext=L.g("cr_testing"), link_alias="cr_ablipo"),
                Text(text="@AlexeiCrystal", subtext=L.g("cr_alexeicrystal"), link_alias="cr_alexeicrystal"),
                Text(text="@kvuco", subtext=L.g("cr_kvuco"), link_alias="cr_kvuco"),
                Text(text=L.g("cr_ai_title"), subtext=L.g("cr_ai"), link_alias="cr_ai"),
            ]),
            Divider(),
            Header(text=L.g("params")),
            Switch(key="show_in_settings", text=L.g("show_in_settings"), default=True, subtext=L.g("show_in_settings_sub"), icon="msg_settings", on_change=lambda v: plugin._on_show_in_settings_change(v), link_alias="misc_show_in_settings"),
            Selector(key="plugin_lang", text=L.g("language"), default=_cur_lang_idx(), items=_lang_items, icon="msg_language", on_change=_on_lang_change, link_alias="misc_language"),
            Divider(),
            Header(text=L.g("backup")),
            Text(text=L.g("export_to_saved"), icon="msg_saved", subtext=L.g("export_to_saved_sub"), on_click=plugin._export_settings, link_alias="misc_export"),
        ]


__plugin__ = MaxReExteraPlugin


# __DEX_BEGIN__
# __DEX_END__

