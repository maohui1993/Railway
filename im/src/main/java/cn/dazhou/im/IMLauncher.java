package cn.dazhou.im;

import android.util.Log;

import java.util.List;

import cn.dazhou.im.core.ConnectManager;
import cn.dazhou.im.core.IConnection;
import cn.dazhou.im.core.smack.modle.User;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
    private static final String TAG = "IM";
    private static IConnection sConnection;

    public static boolean connect(String ip) {
        boolean result = true;
        try {
            sConnection = ConnectManager.getConnection(ConnectManager.CONNECT_PROTOCOL_XMPP, ip);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param username
     * @param password
     * @return 登录是否成功
     */
    public static boolean login(String username, String password) {
        Log.d("TAG", "登录中··");
        boolean result = true;
        try {
            sConnection.login(username, password);
        } catch (Exception e) {
            result = false;
            Log.w("TAG", "登录失败:" + e.getMessage());
        }
        return result;
    }

    public static List<User> getUserBook(){
        List<User> users = null;
        return users;
    }
}
