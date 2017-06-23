package cn.dazhou.railway.im.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import java.util.List;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.friendinfo.FriendInfoActivity;
import cn.dazhou.railway.im.activity.FullImageActivity;
import cn.dazhou.railway.im.db.ChatMessageModel;
import cn.dazhou.railway.im.db.DataHelper;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.util.StringUtil;

/**
 * Created by hooyee on 2017/6/23.
 */

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mChatView;
    private String mJid;
    private Context mContext;
    private FriendModel mFriendModel;

    public ChatPresenter(Context context, ChatContract.View view, String jid) {
        mContext = context;
        mChatView = view;
        mJid = jid;
        mChatView.setPresenter(this);
    }

    @Override
    public void init() {
        mFriendModel = DataHelper.getFriend(mJid);
        onRefresh();
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
        // 还原真实jid
        String jid = StringUtil.getRealJid(mJid);
        try {
            IMLauncher.chatWith(jid, msg);
            msg.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
        } catch (Exception e) {
            msg.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void onImageClick() {
        mContext.startActivity(new Intent(mContext, FullImageActivity.class));
        ((Activity)mContext).overridePendingTransition(0, 0);
    }

    int page = 1;
    @Override
    public void onRefresh() {
        if (mFriendModel != null) {
            List<ChatMessageModel> chatMessageModels = mFriendModel.getMyChatMessages(page);
            page ++;
            mChatView.refresh(ChatMessageModel.toChatMessageEntity(chatMessageModels));
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                FriendInfoActivity.startItself(mContext, StringUtil.getRealJid(mJid));
                break;
        }
        return false;
    }
}
