package cn.dazhou.im.acpect.db;

import cn.dazhou.im.util.Constants;

/**
 * Created by hooyee on 2017/7/20.
 */

public interface FriendDbApi {
    String JID_SEPARATOR = Constants.JID_SEPARATOR;
    void jid(String jid);

    String name();
}
