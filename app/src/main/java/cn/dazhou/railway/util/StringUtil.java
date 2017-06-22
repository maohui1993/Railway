package cn.dazhou.railway.util;

import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;

/**
 * Created by hooyee on 2017/6/16.
 */

public class StringUtil {
    public static String getRealJid(String wrapJid) {
        return wrapJid.split(Constants.JID_SEPARATOR)[0] + Constants.JID_SEPARATOR + MyApp.gServerIp;
    }

    public static String getWrapJid(String rawJid) {
        if (rawJid.contains(Constants.JID_SEPARATOR)) {
            rawJid = rawJid.split(Constants.JID_SEPARATOR)[0];
        }
        return rawJid + Constants.JID_SEPARATOR + MyApp.gCurrentUsername;
    }

    public static String getWrapJid(String rawJid, String wrap) {
        if (rawJid.contains(Constants.JID_SEPARATOR)) {
            rawJid = rawJid.split(Constants.JID_SEPARATOR)[0];
        }
        return rawJid + Constants.JID_SEPARATOR + wrap;
    }

    public static String getUsername(String jid) {
        return jid.split(Constants.JID_SEPARATOR)[0];
    }

}
