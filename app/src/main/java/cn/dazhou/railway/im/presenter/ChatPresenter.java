package cn.dazhou.railway.im.presenter;

import android.content.Context;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.modle.ChatMessageEntity;
import cn.dazhou.im.widget.ChatContentView;

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

    @Override
    public void onSend(ChatMessageEntity msg) {
        IMLauncher.chatWith(mJid, msg);
    }
}
