package cn.dazhou.im.acpect.db;

import cn.dazhou.im.entity.ChatMessageEntity;

/**
 * Created by hooyee on 2017/7/20.
 */

public interface ChatMessageDbApi {

//    ChatMessageDbApi initBy(ChatMessageEntity message);
    boolean saveMessage();
    void updateState(boolean state);
    String jid();
    void updateJid(String jid);
    ChatMessageEntity.Type dataType();
    String textContent();
}
