package cn.dazhou.database.util;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;
import java.util.Set;

import cn.dazhou.database.ChatMessageModel;
import cn.dazhou.database.ChatMessageModel_Table;
import cn.dazhou.database.FriendModel;
import cn.dazhou.database.FriendModel_Table;
import cn.dazhou.database.FriendRequestModel;
import cn.dazhou.database.FriendRequestModel_Table;
import cn.dazhou.database.FunctionItemModel;
import cn.dazhou.database.FunctionItemModel_Table;
import cn.dazhou.database.UserModel;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.im.util.Config;

/**
 * Created by hooyee on 2017/6/23.
 */

public class DataHelper {
    public static List<FunctionItemModel> queryFunctions(UserModel user) {
        return SQLite.select()
                .from(FunctionItemModel.class)
                .where(FunctionItemModel_Table.jid.eq(user.getUsername()))
                .orderBy(FunctionItemModel_Table.position, true)
                .queryList();
    }

    public static List<FriendModel> updateFriendFromServer(UserModel user) {
        // 从服务器获取好友列表
        Roster roster = null;
        try {
            roster = IMLauncher.getRoster();
            Set<RosterEntry> entries = roster.getEntries();
            for (RosterEntry entry : entries) {
                String possessor = user.getUsername();
                FriendModel friend = toFriendModel(entry, possessor);
                if (!friend.exists()) {
                    user.getMyFriends().add(friend);
                    user.setFirstLogin(false);
                }
            }
            user.save();
            return user.getMyFriends();
        } catch (IMLauncher.IMException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FriendModel toFriendModel(RosterEntry entry, String possessor) {
        FriendModel friend = new FriendModel();
        // raw-jid形式为  username@hostname
        String rawJid = entry.getJid().toString();
        // 存储的jid形式为  username@possessor
        friend.setJid(StringUtil.getWrapJid(rawJid, possessor));
        friend.setRelation(FriendModel.typeToInt(entry.getType().name()));
        friend.setRawJid(rawJid);
        friend.setName(entry.getName());
        friend.setPossessor(possessor);
        try {
            ExtraInfo info = IMLauncher.getVCard(rawJid);
            friend.setNickName(info.getName());
            friend.setTel(info.getTel());
        } catch (IMLauncher.IMException e) {
            e.printStackTrace();
        }
        return friend;
    }

    public static FriendModel getFriend(String jid) {
        if(jid == null) {
            return null;
        }
        return SQLite.select()
                .from(FriendModel.class)
                // 存储的jid形式为  username@possessor
                .where(FriendModel_Table.jid.eq(jid))
                .querySingle();
    }

    public static ChatMessageModel getChatMessageById(int id) {
        return SQLite.select()
                .from(ChatMessageModel.class)
                .where(ChatMessageModel_Table.id.eq(id))
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
        model.setToJid(Config.gCurrentUsername);
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
                .orderBy(FriendRequestModel_Table.id.desc())
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