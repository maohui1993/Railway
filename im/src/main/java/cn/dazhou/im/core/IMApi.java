package cn.dazhou.im.core;

import org.jivesoftware.smack.roster.Roster;

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

    List<UserBean> searchUserFromServer(String username);
}
