package cn.dazhou.im.core;

import cn.dazhou.im.core.smack.SmackConnectApi;

/**
 * Created by hooyee on 2017/5/5.
 */

public class ConnectManager {
    public static final byte CONNECT_PROTOCOL_XMPP = 0;
    public static final byte CONNECT_PROTOCOL_TCP = 1;

    public static IConnection getConnection(int type) throws Exception {
        IConnection IConnection = null;
        switch (type) {
            case CONNECT_PROTOCOL_XMPP :
                IConnection = SmackConnectApi.getInstance();
                IConnection.connect();
                break;
            case CONNECT_PROTOCOL_TCP :
                break;
        }
        return IConnection;
    }
}
