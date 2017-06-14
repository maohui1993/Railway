package cn.dazhou.railway.im.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.activity.FullImageActivity;
import cn.dazhou.railway.im.db.ChatMessageModel;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.FriendModel_Table;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/5/8.
 */

public class ChatPresenter implements ChatContentView.OnSendListener, ChatContentView.OnImageClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Context mContext;
    private String mJid;
    private FriendModel friendModel;

    private OnDataUpdateListener<ChatMessageEntity> mOnDataUpdateListener;

    public ChatPresenter(Context mContext, String jid) {
        this.mContext = mContext;
        mJid = jid;
    }

    public void setOnDataUpdateListener(OnDataUpdateListener<ChatMessageEntity> mOnDataUpdateListener) {
        this.mOnDataUpdateListener = mOnDataUpdateListener;
    }

    public void init() {
        String possessor = MyApp.gCurrentUser.getUsername();
        friendModel = SQLite.select()
                .from(FriendModel.class)
                .where(FriendModel_Table.possessor.eq(possessor))
                // 存储的jid形式为  username@possessor
                .and(FriendModel_Table.jid.eq(mJid))
                .querySingle();

        if(friendModel != null && mOnDataUpdateListener != null) {
            List<ChatMessageModel> chatMessageModels = friendModel.getMyChatMessages();
            mOnDataUpdateListener.onUpdateData(ChatMessageModel.toChatMessageEntity(chatMessageModels), true);
        }
    }

    /**
     * 发送消息时，存储数据
     * @param msg
     */
    @Override
    public void onSend(ChatMessageEntity msg) {
        ChatMessageModel model = new ChatMessageModel();
        model.setType(msg.getType());
        model.setDate(msg.getDate());
        model.setImagePath(msg.getImagePath());
        model.setVoicePath(msg.getVoicePath());
        model.setVoiceTime(msg.getVoiceTime());
        model.setContent(msg.getContent());
        model.setFromJid(msg.getFromJid());
        model.setToJid(msg.getToJid());
        // model保存的jid应该是 【接收方+@+当前用户】
        model.setJid(mJid);         // 正在聊天的人
        model.setState(msg.isState());

        model.save();
        String jid = mJid.split(Constants.JID_SEPARATOR)[0] + Constants.JID_SEPARATOR + MyApp.gServerIp;
        IMLauncher.chatWith(jid, msg);
    }

    @Override
    public void onImageClick() {
        mContext.startActivity(new Intent(mContext, FullImageActivity.class));
        ((Activity)mContext).overridePendingTransition(0, 0);
    }

    int page = 2;

    @Override
    public void onRefresh() {
        if (friendModel != null) {
            List<ChatMessageModel> chatMessageModels = friendModel.getMyChatMessages(page);
            page ++;
            mOnDataUpdateListener.onUpdateData(ChatMessageModel.toChatMessageEntity(chatMessageModels), false);
        }
    }
}
