package cn.dazhou.railway.im.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.activity.AddFriendActivity;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;

/**
 * Created by hooyee on 2017/5/11.
 */

public class MainPresenter implements View.OnClickListener{
    private Context mContext;
    private OnDataUpdateListener mOnDataUpdateListener;

    public MainPresenter(Context context) {
        mContext = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener mOnDataUpdateListener) {
        this.mOnDataUpdateListener = mOnDataUpdateListener;
    }

    public void init() {
        List<FriendModel> friends = new ArrayList();
        if (MyApp.gCurrentUser.isFirstLogin()) {
            // 从服务器获取好友列表
            Roster roster = IMLauncher.getRoster();

            Set<RosterEntry> entries = roster.getEntries();
            for (RosterEntry entry : entries) {
                FriendModel friend = new FriendModel();
                // raw-jid形式为  username@hostname
                String rawJid = entry.getJid().toString();
                String possessor = MyApp.gCurrentUser.getUsername();
                // 存储的jid形式为  username@possessor
                friend.setJid(rawJid.split(Constants.JID_SEPARATOR)[0] + Constants.JID_SEPARATOR + possessor);
                friend.setRawJid(rawJid);
                friend.setName(entry.getName());
                friend.setPossessor(possessor);
//                friend.save();
                friends.add(friend);
                MyApp.gCurrentUser.getMyFriends().add(friend);
            }
            MyApp.gCurrentUser.setFirstLogin(false);
            MyApp.gCurrentUser.save();
        } else {
            friends = MyApp.gCurrentUser.getMyFriends();
        }
        if (mOnDataUpdateListener != null && friends != null && friends.size() > 0) {
            Collections.sort(friends);
            mOnDataUpdateListener.onUpdateData(friends);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_friend:
                Toast.makeText(mContext, "new Friend", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, AddFriendActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.chat_group:
                Toast.makeText(mContext, "Group Chat", Toast.LENGTH_SHORT).show();
        }
    }
}
