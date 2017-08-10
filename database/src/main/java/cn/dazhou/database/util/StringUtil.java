package cn.dazhou.database.util;

import cn.dazhou.im.acpect.db.FriendDbApi;
import cn.dazhou.im.util.Config;

/**
 * Created by hooyee on 2017/6/16.
 */

public class StringUtil {
    static String JID_SEPARATOR = "@";

    public static String getRealJid(String wrapJid) {
        return wrapJid.split(FriendDbApi.JID_SEPARATOR)[0] + FriendDbApi.JID_SEPARATOR + Config.gServerIp;
    }

    public static String getRealJid(String wrapJid, String serverIp) {
        return wrapJid.split(FriendDbApi.JID_SEPARATOR)[0] + FriendDbApi.JID_SEPARATOR + serverIp;
    }

    public static String getWrapJid(String rawJid) {
        return getWrapJid(rawJid, Config.gCurrentUsername);
    }

    public static String getWrapJid(String rawJid, String wrap) {
        if (rawJid.contains(FriendDbApi.JID_SEPARATOR)) {
            rawJid = rawJid.split(FriendDbApi.JID_SEPARATOR)[0];
        }
        return rawJid + FriendDbApi.JID_SEPARATOR + wrap;
    }

    public static String getUsername(String jid) {
        return jid.split(FriendDbApi.JID_SEPARATOR)[0];
    }

}
