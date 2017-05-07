package cn.dazhou.im;

import android.util.Log;

import java.util.List;

import cn.dazhou.im.core.ConnectManager;
import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.core.smack.modle.User;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
    private static final String TAG = "IMApi";
    private static IMApi mImApi;

    public static boolean connect(String ip) {
        boolean result = true;
        try {
            mImApi = ConnectManager.getConnection(ConnectManager.CONNECT_PROTOCOL_XMPP, ip);
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
            mImApi.login(username, password);
        } catch (Exception e) {
            result = false;
            Log.w("TAG", "登录失败:" + e.getMessage());
        }
        return result;
    }

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
    }
}
