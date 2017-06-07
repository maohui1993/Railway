package cn.dazhou.im;

import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.search.ReportedData;
import org.jxmpp.jid.EntityBareJid;

import java.util.List;

import cn.dazhou.im.core.ConnectManager;
import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.im.util.OfflineMsgManager;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
    private static final String TAG = "TAG";

    public static IMApi getImApi() {
        return mImApi;
    }

    private static IMApi mImApi;

    public static boolean connect(Context context, String ip) throws Exception {
        boolean result = true;
        try {
            mImApi = ConnectManager.getConnection(context, ConnectManager.CONNECT_PROTOCOL_XMPP, ip);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            throw e;
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
    public static boolean login(String username, String password) throws Exception {
        Log.d("TAG", "登录中··");
        boolean result = true;
        try {
            mImApi.login(username, password);
        } catch (Exception e) {
            Log.w("TAG", "登录失败:" + e.getMessage());
            throw e;
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

    public static Roster addFriend(String jid){
        return mImApi.addFriend(jid);
    }

    public static List<UserBean> searchUserFromServer(String username) {
        return mImApi.searchUserFromServer(username);
    }

    public static boolean acceptFriendRequest(String jid) {
        return mImApi.acceptFriendRequest(jid);
    }

    public static boolean rejectFriendRequest(String jid) {
        return mImApi.rejectFriendRequest(jid);
    }

    public static MultiUserChat createChatRoom(String roomName, String nickName, String password) {
        return mImApi.createChatRoom(roomName, nickName, password);
    }

    public static MultiUserChat joinChatRoom(String roomName,  String nickName, String password) {
        return mImApi.joinChatRoom(roomName, nickName, password);
    }

    public static void inviteUser(String roomName, String jid) {
        mImApi.inviteUser(roomName, jid);
    }
}
