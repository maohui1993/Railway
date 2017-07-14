package cn.dazhou.railway.im.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.railway.MyApp;

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

    public static FriendRequestModel saveFriendRequest(FriendRequest request) {
        FriendRequestModel model = new FriendRequestModel();
        model.setFromJid(request.getJid());
        model.setToJid(MyApp.gCurrentUsername);
        model.save();
        return model;
    }

    /**
     *
     * @param fromJid 请求的发送方
     * @param toJid 请求接收方
     * @return
     */
    public static FriendRequestModel getFriendRequest(String fromJid, String toJid) {
        return SQLite.select()
                .from(FriendRequestModel.class)
                // 存储的jid形式为  username@possessor
                .where(FriendRequestModel_Table.fromJid.eq(fromJid))
                .and(FriendRequestModel_Table.toJid.eq(toJid))
                .querySingle();
    }

    public static List<FriendRequestModel> getFriendRequests(String toJid) {
        return SQLite.select()
                .from(FriendRequestModel.class)
                // 存储的jid形式为  username@possessor
                .where(FriendRequestModel_Table.toJid.eq(toJid))
                .orderBy(FriendRequestModel_Table.id.desc())
                .queryList();
    }
}
