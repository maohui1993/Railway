package cn.dazhou.im;

import android.util.Log;

import cn.dazhou.im.core.ConnectManager;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
    private static final String TAG = "IM";
    public static void connect() {
        try {
            ConnectManager.getConnection(ConnectManager.CONNECT_PROTOCOL_XMPP);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    public static void login() {

    }
}
