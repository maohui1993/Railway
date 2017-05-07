package cn.dazhou.im.core;

<<<<<<< HEAD
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.core.smack.SmackImApiImpl;
=======
import cn.dazhou.im.core.smack.SmackConnectApi;
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb

/**
 * Created by hooyee on 2017/5/5.
 */

public class ConnectManager {
    public static final byte CONNECT_PROTOCOL_XMPP = 0;
    public static final byte CONNECT_PROTOCOL_TCP = 1;

<<<<<<< HEAD
    public static IMApi getConnection(int type, String ip) throws Exception {
        IMApi imApi = null;
        switch (type) {
            case CONNECT_PROTOCOL_XMPP :
                imApi = SmackImApiImpl.getInstance();
                imApi.connect(ip);
=======
    public static IConnection getConnection(int type, String ip) throws Exception {
        IConnection IConnection = null;
        switch (type) {
            case CONNECT_PROTOCOL_XMPP :
                IConnection = SmackConnectApi.getInstance();
                IConnection.connect(ip);
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb
                break;
            case CONNECT_PROTOCOL_TCP :
                break;
        }
<<<<<<< HEAD
        return imApi;
=======
        return IConnection;
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb
    }
}
