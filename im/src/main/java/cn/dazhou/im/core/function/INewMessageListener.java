package cn.dazhou.im.core.function;

import org.jivesoftware.smack.packet.Message;

/**
 * Created by hooyee on 2017/5/8.
 */

public interface INewMessageListener {
    void showNewMessage(Message msg);
}
