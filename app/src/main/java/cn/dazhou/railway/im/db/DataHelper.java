package cn.dazhou.railway.im.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by hooyee on 2017/6/23.
 */

public class DataHelper {
    public static FriendModel getFriend(String jid) {
        checkNotNull(jid);
        return SQLite.select()
                .from(FriendModel.class)
                // 存储的jid形式为  username@possessor
                .where(FriendModel_Table.jid.eq(jid))
                .querySingle();
    }

    public static List<ChatMessageModel> getChatMessages(FriendModel friend) {
        List<ChatMessageModel> chatMessageModels = null;
        if(friend != null) {
            chatMessageModels = friend.getMyChatMessages();
        }
        return chatMessageModels;
    }

    public static List<ChatMessageModel> getChatMessages(String friendJid) {
        FriendModel friend = getFriend(friendJid);
        return getChatMessages(friend);
    }
}
