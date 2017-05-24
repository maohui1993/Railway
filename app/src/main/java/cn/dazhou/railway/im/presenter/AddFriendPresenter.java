package cn.dazhou.railway.im.presenter;

import android.content.Context;

import cn.dazhou.im.core.function.IFriendRequest;

/**
 * Created by hooyee on 2017/5/24.
 */

public class AddFriendPresenter implements IFriendRequest {
    private Context mContext;
    public AddFriendPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void onReceiveRequest(String source) {

    }
}
