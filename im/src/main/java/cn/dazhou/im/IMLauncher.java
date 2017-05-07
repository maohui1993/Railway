package cn.dazhou.im;

import android.util.Log;

import java.util.List;

import cn.dazhou.im.core.ConnectManager;
<<<<<<< HEAD
import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;
=======
import cn.dazhou.im.core.IConnection;
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb
import cn.dazhou.im.core.smack.modle.User;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
<<<<<<< HEAD
    private static final String TAG = "IMApi";
    private static IMApi mImApi;
=======
    private static final String TAG = "IM";
    private static IConnection sConnection;
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb

    public static boolean connect(String ip) {
        boolean result = true;
        try {
<<<<<<< HEAD
            mImApi = ConnectManager.getConnection(ConnectManager.CONNECT_PROTOCOL_XMPP, ip);
=======
            sConnection = ConnectManager.getConnection(ConnectManager.CONNECT_PROTOCOL_XMPP, ip);
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb
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
<<<<<<< HEAD
            mImApi.login(username, password);
=======
            sConnection.login(username, password);
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb
        } catch (Exception e) {
            result = false;
            Log.w("TAG", "登录失败:" + e.getMessage());
        }
        return result;
    }

<<<<<<< HEAD
    public static boolean chatWith(String id, String info) {
        boolean result;
        try {
            mImApi.chatWith(id, info);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public static void showRoster() {
        mImApi.showRoster();
=======
    public static List<User> getUserBook(){
        List<User> users = null;
        return users;
>>>>>>> a2be3e9ba6dfb8f50286524352bb9a1b31e49adb
    }
}
