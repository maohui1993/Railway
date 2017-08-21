package cn.dazhou.im;

import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jxmpp.jid.EntityBareJid;

import java.util.List;

import cn.dazhou.im.core.ConnectManager;
import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.im.util.OfflineMsgManager;

/**
 * Created by hooyee on 2017/5/5.
 */

public final class IMLauncher {
    private static final String TAG = "TAG";
    private static boolean login = false;

    public static IMApi getImApi() {
        return mImApi;
    }

    private static IMApi mImApi;

    public static boolean isLogin() {
        return login;
    }

    public static boolean connect(Context context, String ip, int port, int timeout) throws IMException {
        boolean result = true;
        try {
            mImApi = ConnectManager.getConnection(context, ConnectManager.CONNECT_PROTOCOL_XMPP, ip, port, timeout);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            throw new IMException(e);
        }
        return result;
    }

    public static void disconnect() throws IMException {
        checkConnected(mImApi);
        mImApi.disconnect();
    }

    /**
     *
     * @param username
     * @param password
     * @return 登录是否成功
     */
    public static boolean login(String username, String password) throws IMException {
        Log.d("TAG", "登录中··");
        try {
            mImApi.login(username, password);
            login = true;
        } catch (Exception e) {
            Log.w("TAG", "登录失败:" + e.getMessage());
            login = false;
            throw new IMException(e);
        }
        return login;
    }

    public static boolean logout() throws IMException {
        checkConnected(mImApi);
        mImApi.disconnect();
        return true;
    }

    private static boolean checkNotNull(Object o) throws IMException{
        if (o == null) {
            throw new IMException(new NullPointerException());
        }
        return true;
    }

    private static boolean checkConnected(IMApi imApi) throws IMException{
        if(!checkNotNull(imApi) || !imApi.isConnected()) {
            throw new IMException("链接断开");
        }
        return true;
    }

    public static boolean chatWith(String id, ChatMessageEntity msg) throws IMException{
        boolean result;
        try {
            mImApi.chatWith(id, msg);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IMException(e);
        }
        return result;
    }

    @Deprecated
    public static void chatWith(EntityBareJid id) throws IMException{
        checkConnected(mImApi);
        mImApi.chatWith(id);
    }


    public static Roster getRoster() throws IMException {
        checkConnected(mImApi);
        return mImApi.getRoster();
    }

    public static List<ChatMessageEntity> getOfflineMessage() {
        return OfflineMsgManager.getChatMessageEntities();
    }

    public static Roster addFriend(String jid) throws IMException {
        checkConnected(mImApi);
        return mImApi.addFriend(jid);
    }

    public static List<UserBean> searchUserFromServer(String username) throws IMException {
        checkNotNull(mImApi);
        return mImApi.searchUserFromServer(username);
    }

    public static Roster acceptFriendRequest(String jid) throws IMException {
        checkConnected(mImApi);
        return mImApi.acceptFriendRequest(jid);
    }

    public static boolean rejectFriendRequest(String jid) throws IMException {
        checkConnected(mImApi);
        return mImApi.rejectFriendRequest(jid);
    }

    public static MultiUserChat createChatRoom(String roomName, String nickName, String password) throws IMException {
        checkConnected(mImApi);
        return mImApi.createChatRoom(roomName, nickName, password);
    }

    public static MultiUserChat joinChatRoom(String roomName,  String nickName, String password) throws IMException {
        checkConnected(mImApi);
        return mImApi.joinChatRoom(roomName, nickName, password);
    }

    public static void inviteUser(String roomName, String jid) throws IMException {
        checkConnected(mImApi);
        mImApi.inviteUser(roomName, jid);
    }

    public static void saveVCard(ExtraInfo info) throws IMException{
        checkConnected(mImApi);
        try {
            mImApi.saveVCard(info);
        } catch (Exception e) {
            throw new IMException(e);
        }
    }

    public static ExtraInfo getVCard(String jid) throws IMException {
        checkConnected(mImApi);
        return mImApi.getVCard(jid);
    }

    public static class IMException extends Exception {
        public IMException(String message) {
            super(message);
        }

        public IMException(Throwable cause) {
            super(cause);
        }
    }
}
