package cn.dazhou.railway.im.presenter;

import android.content.Context;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.modle.ChatMessageEntity;
import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.im.db.ChatMessageModel;

/**
 * Created by hooyee on 2017/5/8.
 */

public class ChatPresenter implements ChatContentView.OnSendListener {
    private Context mContext;
    private String mJid;

    public ChatPresenter(Context mContext, String jid) {
        this.mContext = mContext;
        mJid = jid;
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
        model.setContent(msg.getContent());
        model.setFromJid(msg.getFromJid());
        model.setToJid(msg.getToJid());
        model.setJid(mJid);         // 正在聊天的人
        model.setState(msg.isState());
        model.save();
        IMLauncher.chatWith(mJid, msg);
    }
}
