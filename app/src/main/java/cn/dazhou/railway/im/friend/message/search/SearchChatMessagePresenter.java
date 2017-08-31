package cn.dazhou.railway.im.friend.message.search;

import android.content.Context;

/**
 * Created by lenovo on 2017/8/31.
 */

public class SearchChatMessagePresenter implements SearchChatMessageContract.Presenter {
    private Context mContext;
    private SearchChatMessageContract.View mView;

    public SearchChatMessagePresenter(Context context, SearchChatMessageContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }
}
