package cn.dazhou.im.core.function;

import org.jivesoftware.smack.roster.Roster;
import org.jxmpp.jid.EntityBareJid;

import cn.dazhou.im.modle.ChatMsgEntity;

/**
 * Created by Hooyee on 2017/5/7.
 * mail: hooyee_moly@foxmail.com
 */

public interface IChat {
    void chatWith(String jid, ChatMsgEntity msg) throws Exception;

    void chatWith(EntityBareJid jid);

    void chat(ChatMsgEntity msg) throws Exception;

    Roster getRoster();
}
