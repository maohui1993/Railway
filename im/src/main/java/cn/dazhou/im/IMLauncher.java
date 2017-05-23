package cn.dazhou.im;

import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.search.ReportedData;
import org.jxmpp.jid.EntityBareJid;

import java.util.List;

import cn.dazhou.im.core.ConnectManager;
import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.OfflineMsgManager;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
    private static final String TAG = "IMApi";

    public static IMApi getImApi() {
        return mImApi;
    }

    private static IMApi mImApi;

    public static boolean connect(Context context, String ip) {
        boolean result = true;
        try {
            mImApi = ConnectManager.getConnection(context, ConnectManager.CONNECT_PROTOCOL_XMPP, ip);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        return result;
    }

    public static void disconnect() {
        mImApi.disconnect();
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

    public static boolean chatWith(String id, ChatMessageEntity msg) {
        boolean result;
        try {
            mImApi.chatWith(id, msg);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public static void chatWith(EntityBareJid id) {
        mImApi.chatWith(id);
    }


    public static Roster getRoster() {
        return mImApi.getRoster();
    }

    public static List<ChatMessageEntity> getOfflineMessage() {
        return OfflineMsgManager.getChatMessageEntities();
    }

    public static Roster addFriend(String jid) throws SmackException.NoResponseException {
        return mImApi.addFriend(jid);
    }

    public static List<ReportedData.Row> searchUserFromServer(String username) {
        return mImApi.searchUserFromServer(username);
    }
}
