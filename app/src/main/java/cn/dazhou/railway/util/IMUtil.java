package cn.dazhou.railway.util;

import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.List;
import java.util.Set;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.im.service.IMChatService;
import cn.dazhou.railway.im.service.IMFriendRequestService;

/**
 * Created by hooyee on 2017/6/19.
 */

public class IMUtil {
    public static List<FriendModel> updateFriendFromServer(UserModel user) {
        // 从服务器获取好友列表
        Roster roster = IMLauncher.getRoster();

        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            FriendModel friend = new FriendModel();
            // raw-jid形式为  username@hostname
            String rawJid = entry.getJid().toString();
            Log.i("TAG", "friend type = " + entry.getType().name());
            String possessor = user.getUsername();
            // 存储的jid形式为  username@possessor
            friend.setJid(rawJid.split(Constants.JID_SEPARATOR)[0] + Constants.JID_SEPARATOR + possessor);
            friend.setRelation(FriendModel.typeToInt(entry.getType().name()));
            friend.setRawJid(rawJid);
            friend.setName(entry.getName());
            friend.setPossessor(possessor);
            ExtraInfo info = IMLauncher.getVCard(rawJid);
            friend.setNickName(info.getName());
            friend.setTel(info.getTel());
            user.getMyFriends().add(friend);
            user.setFirstLogin(false);
            user.save();
        }
        return user.getMyFriends();
    }

    public static void startServiceWhenLogin(Context context) {
        Log.i("TAG", "start service");
        IMChatService.startItself(context);
        IMFriendRequestService.startItself(context);
    }

    public static void stopServiceWhenLogout(Context context) {
        IMChatService.stopItself(context);
        IMFriendRequestService.stopItself(context);
    }

    public static void login(final Context context) {
        try {
            IMLauncher.connect(context, MyApp.gServerIp);
            boolean login = IMLauncher.login(MyApp.gCurrentUsername, MyApp.gCurrentUser.getPassword());
            Log.i("TAG", "hasLog = " + login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
