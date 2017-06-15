package cn.dazhou.im.core;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.List;

import cn.dazhou.im.core.function.IChat;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.entity.UserBean;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public interface IMApi extends IChat, IConnection{
    Roster addFriend(String jid);

    boolean acceptFriendRequest(String jid);

    boolean rejectFriendRequest(String jid);

    List<UserBean> searchUserFromServer(String username);

    /**
     * 创建群聊聊天室
     * @param roomName		聊天室名字
     * @param nickName		创建者在聊天室中的昵称
     * @param password		聊天室密码
     * @return
     */
    MultiUserChat createChatRoom(String roomName, String nickName, String password);

    /**
     * 加入一个群聊聊天室
     * @param roomName		聊天室名字
     * @param nickName		用户在聊天室中的昵称
     * @param password		聊天室密码
     * @return
     */
    MultiUserChat joinChatRoom(String roomName,  String nickName, String password);

    void inviteUser(String roomName, String jid);

    UserBean getCurrentLoginedUserInfo();

    void saveVCard(String key, String value) throws Exception;
}
