package cn.dazhou.im.core;

import android.content.Context;

import cn.dazhou.im.core.smack.SmackImApiImpl;

/**
 * Created by hooyee on 2017/5/5.
 */

public class ConnectManager {
    public static final byte CONNECT_PROTOCOL_XMPP = 0;
    public static final byte CONNECT_PROTOCOL_TCP = 1;

    public static IMApi getConnection(Context context, int type, String ip, int port, int timeout) throws Exception {
        IMApi imApi = null;
        switch (type) {
            case CONNECT_PROTOCOL_XMPP :
                imApi = SmackImApiImpl.getInstance(context);
                imApi.connect(ip, port, timeout);
                break;
            case CONNECT_PROTOCOL_TCP :
                break;
        }
        return imApi;
    }
}
