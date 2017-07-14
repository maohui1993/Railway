package cn.dazhou.railway.im.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;

import java.util.List;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.db.ChatMessageModel;
import cn.dazhou.railway.im.db.DataHelper;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.friend.info.FriendInfoActivity;
import cn.dazhou.railway.im.friend.info.FullImageActivity;
import cn.dazhou.railway.util.StringUtil;

/**
 * Created by hooyee on 2017/6/23.
 */

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mChatView;
    private String mJid;
    private Context mContext;
    private FriendModel mFriendModel;
    private int page = 1;


    public ChatPresenter(Context context, ChatContract.View view, String jid) {
        mContext = context;
        mChatView = view;
        mJid = jid;
        mChatView.setPresenter(this);
    }

    @Override
    public void init() {
        mFriendModel = DataHelper.getFriend(mJid);
        if (mFriendModel != null) {
            List<ChatMessageModel> chatMessageModels = mFriendModel.getMyChatMessages(page);
            page++;
            mChatView.refresh(ChatMessageModel.toChatMessageEntity(chatMessageModels));
        }
    }

    /**
     * 发送消息时，存储数据
     *
     * @param msg
     */
    @Override
    public void onSend(ChatMessageEntity msg) {
        ChatMessageModel model = ChatMessageModel.newInstances(msg);
        model.setJid(mJid);         // 正在聊天的人
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
        ((Activity) mContext).overridePendingTransition(0, 0);
    }

    Handler handler = new Handler();

    @Override
    public void onRefresh() {
        if (mFriendModel == null) {
            return;
        }
        final List<ChatMessageModel> chatMessageModels = mFriendModel.getMyChatMessages(page);
        if (chatMessageModels != null && chatMessageModels.size() > 0) {
            mChatView.showLoadingTip("加载更多...");
        } else {
            mChatView.showLoadingTip("已全部加载...");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                mChatView.refresh(ChatMessageModel.toChatMessageEntity(chatMessageModels));
            }
        }, 1000);

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
