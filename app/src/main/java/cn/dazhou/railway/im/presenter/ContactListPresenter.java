package cn.dazhou.railway.im.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.activity.AddFriendActivity;
import cn.dazhou.railway.im.activity.ChatRoomActivity;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hooyee on 2017/5/31.
 */

public class ContactListPresenter implements View.OnClickListener{
    private Context mContext;
    private OnDataUpdateListener mOnDataUpdateListener;
    private WeakReference<List<FriendModel>> cache;

    public ContactListPresenter(Context context) {
        mContext = context;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener mOnDataUpdateListener) {
        this.mOnDataUpdateListener = mOnDataUpdateListener;
    }

    public void init() {
        if (MyApp.gCurrentUser == null) {
            Toast.makeText(mContext, "提示登录", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cache != null) {
            mOnDataUpdateListener.onUpdateData(cache.get());
            return;
        }
        if (MyApp.gCurrentUser.isFirstLogin()) {
            Observable.create(new ObservableOnSubscribe() {
                @Override
                public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                    updateFriendFromServer();
                    e.onNext(1);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            List<FriendModel> friends = MyApp.gCurrentUser.getMyFriends();
                            if (mOnDataUpdateListener != null && friends != null && friends.size() > 0) {
                                Collections.sort(friends);
                                cache = new WeakReference(friends);
                                mOnDataUpdateListener.onUpdateData(friends);
                            }
                        }
                    });
        } else {
            cache = new WeakReference(MyApp.gCurrentUser.getMyFriends());
            mOnDataUpdateListener.onUpdateData(MyApp.gCurrentUser.getMyFriends());
        }
    }

    private List<FriendModel> updateFriendFromServer() {
        // 从服务器获取好友列表
        Roster roster = IMLauncher.getRoster();

        Set<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            FriendModel friend = new FriendModel();
            // raw-jid形式为  username@hostname
            String rawJid = entry.getJid().toString();
            Log.i("TAG", "friend type = " + entry.getType().name());
            String possessor = MyApp.gCurrentUser.getUsername();
            // 存储的jid形式为  username@possessor
            friend.setJid(rawJid.split(Constants.JID_SEPARATOR)[0] + Constants.JID_SEPARATOR + possessor);
            friend.setRelation(FriendModel.typeToInt(entry.getType().name()));
            friend.setRawJid(rawJid);
            friend.setName(entry.getName());
            friend.setPossessor(possessor);
            MyApp.gCurrentUser.getMyFriends().add(friend);
            MyApp.gCurrentUser.setFirstLogin(false);
            MyApp.gCurrentUser.save();
        }
        return MyApp.gCurrentUser.getMyFriends();
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
////                        IMLauncher.createChatRoom("test2", "liaotian", "123");
//                        IMLauncher.joinChatRoom("test2", MyApp.gCurrentUsername, "123");
                        ChatRoomActivity.startItself(mContext);
                    }
                }).start();
                break;
        }
    }
}
