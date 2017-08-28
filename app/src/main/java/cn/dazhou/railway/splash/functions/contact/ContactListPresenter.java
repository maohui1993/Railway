package cn.dazhou.railway.splash.functions.contact;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import cn.dazhou.database.util.DataHelper;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.broadcast.IMReceiverManager;
import cn.dazhou.railway.im.chat.room.ChatRoomActivity;
import cn.dazhou.railway.im.friend.message.list.MessageListActivity;
import cn.dazhou.railway.im.friend.request.FriendRequestActivity;

/**
 * Created by hooyee on 2017/5/31.
 */

public class ContactListPresenter implements ContactListContract.Presenter, IMReceiverManager.Observer{
    private Context mContext;
    private ContactListContract.View mView;

    public ContactListPresenter(Context context, ContactListContract.View view) {
        mContext = context;
        mView = view;
        IMReceiverManager.register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_friend:
                Toast.makeText(mContext, "new Friend", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, FriendRequestActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.chat_group:
                Toast.makeText(mContext, "Group Chat", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ChatRoomActivity.startItself(mContext);
                    }
                }).start();
                break;
            case R.id.message_list:
                Toast.makeText(mContext, "message list", Toast.LENGTH_SHORT).show();
                MessageListActivity.startItself(mContext);
                break;
        }
    }

    @Override
    public void destroy() {
        IMReceiverManager.unregister(this);
    }

    @Override
    public void updateRequestTipSate() {
        long count = DataHelper.countOfNotHandlerRequest(MyApp.gCurrentUsername);
        if (count == 0) {
            mView.hideRequestCountTip();
        } else {
            mView.showRequestCountTip(String.valueOf(count));
        }
    }

    @Override
    public void updateMessageTip() {
        long count = DataHelper.getNotReadMessage(MyApp.gCurrentUser);
        if (count == 0) {
            mView.hideMessageTip();
        } else {
            mView.showMessageTip();
        }
    }

    @Override
    public void whenLogined() {
        mView.onUpdateData(MyApp.gCurrentUser.getMyFriends());
    }

    @Override
    public void updateFriendList() {
        DataHelper.updateFriendFromServer(MyApp.gCurrentUser);
        mView.onUpdateData(MyApp.gCurrentUser.getMyFriends());
    }

    @Override
    public void updateFriendRequest() {
        updateRequestTipSate();
    }
}
