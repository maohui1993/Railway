package cn.dazhou.railway.im.chat;

import android.content.Context;
import android.os.Handler;
import android.view.MenuItem;

import java.util.Date;
import java.util.List;

import cn.dazhou.database.ChatMessageModel;
import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.friend.info.FriendInfoActivity;

/**
 * Created by hooyee on 2017/6/23.
 */

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mChatView;
    private String mJid;
    private Context mContext;
    private FriendModel mFriendModel;
    private int page = 1;

    private Date mDate;

    private int initPosition;

    public ChatPresenter(Context context, ChatContract.View view, String jid, Date date) {
        mContext = context;
        mChatView = view;
        mJid = jid;
        mDate = date;
        mChatView.setPresenter(this);
    }

    @Override
    public void init() {
        List<ChatMessageModel> chatMessageModels;
        mFriendModel = DataHelper.getFriend(mJid);

        // 从查找聊天记录进来这个页面的
        if (mDate != null) {
            chatMessageModels = DataHelper.getChatMessages(mJid, mDate);
            mChatView.refresh(ChatMessageModel.toChatMessageEntity(chatMessageModels));
            initPosition = chatMessageModels.size();
            return;
        }

        if (mFriendModel != null) {
            // 进入了聊天界面，便说明该用户所有未读消息为已读
            mFriendModel.setNotReadCount(0);
            chatMessageModels = mFriendModel.getMyChatMessages(page, 0);
            mFriendModel.update();
            page++;
            mChatView.refresh(ChatMessageModel.toChatMessageEntity(chatMessageModels));
        }
    }

    /**
     * 发送消息时，存储数据
     *
     * @param msg
     * @param saveMessage 是否存储数据
     */
    @Override
    public void onSend(ChatMessageEntity msg, boolean saveMessage) {
        // 还原真实jid
        String jid = StringUtil.getRealJid(mJid);
        try {
            IMLauncher.chatWith(jid, msg);
            msg.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
        } catch (Exception e) {
            msg.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
            e.printStackTrace();
        } finally {
            if (saveMessage) {
                ChatMessageModel model = ChatMessageModel.newInstance(msg);
                model.setJid(mJid);         // 正在聊天的人

                // 是否显示时间戳
                boolean show = DataHelper.whetherShowTimestamp(model);
                model.setShowTimestamp(show);
                msg.setShowTimestamp(show);

                DataHelper.save(model);
                msg.setId(model.getId());
            } else {
                ChatMessageModel model = DataHelper.getChatMessageById(msg.getId());
                model.setSendState(msg.getSendState());
                model.update();
            }
        }
    }

    Handler handler = new Handler();

    boolean wholeLoaded = false;

    @Override
    public void onRefresh() {
        if (mFriendModel == null) {
            return;
        }
        final List<ChatMessageModel> chatMessageModels = mFriendModel.getMyChatMessages(page, initPosition);
        if (chatMessageModels != null && chatMessageModels.size() > 0) {
            mChatView.showLoadTip("加载更多...", false);
            wholeLoaded = false;
            page++;
        } else if (!wholeLoaded){
            mChatView.showLoadTip("已全部加载...", true);
            wholeLoaded = true;
        }
        // 一秒钟后执行数据加载
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
