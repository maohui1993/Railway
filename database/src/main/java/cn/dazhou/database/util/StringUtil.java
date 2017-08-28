package cn.dazhou.database.util;

import cn.dazhou.im.util.Config;

/**
 * Created by hooyee on 2017/6/16.
 */

public class StringUtil {
    static String JID_SEPARATOR = "@";

    public static String getRealJid(String wrapJid) {
        return wrapJid.split(JID_SEPARATOR)[0] + JID_SEPARATOR + Config.gServerIp;
    }

    public static String getRealJid(String wrapJid, String serverIp) {
        return wrapJid.split(JID_SEPARATOR)[0] + JID_SEPARATOR + serverIp;
    }

    public static String getWrapJid(String rawJid) {
        return getWrapJid(rawJid, Config.gCurrentUsername);
    }

    public static String getWrapJid(String rawJid, String wrap) {
        if (rawJid.contains(JID_SEPARATOR)) {
            rawJid = rawJid.split(JID_SEPARATOR)[0];
        }
        return rawJid + JID_SEPARATOR + wrap;
    }

    public static String getUsername(String jid) {
        return jid.split(JID_SEPARATOR)[0];
    }

}
