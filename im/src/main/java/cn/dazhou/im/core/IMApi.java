package cn.dazhou.im.core;

import org.jivesoftware.smack.roster.Roster;

import cn.dazhou.im.core.function.IChat;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.core.function.INewMessageListener;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public interface IMApi extends IChat, IConnection{
    void setOnNewMessageListener(INewMessageListener listener);

    Roster addFriend(String jid);

}
