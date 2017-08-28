package cn.dazhou.railway.im.friend.message.list;

import android.content.Context;

/**
 * Created by hooyee on 2017/8/11.
 */

public class MessageListPresenter implements MessageListContract.Presenter {
    private MessageListContract.View mView;
    private Context mContext;

    public MessageListPresenter(MessageListContract.View mView, Context context) {
        this.mView = mView;
        this.mContext = context;
        mView.setPresenter(this);
    }
}
