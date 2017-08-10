package cn.dazhou.railway.splash.functions.contact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Toast;

import cn.dazhou.database.util.DataHelper;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.chat.room.ChatRoomActivity;
import cn.dazhou.railway.im.friend.request.FriendRequestActivity;

/**
 * Created by hooyee on 2017/5/31.
 */

public class ContactListPresenter implements ContactListContract.Presenter{
    private Context mContext;
    private ContactListContract.View mView;

    BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mView.onUpdateData(MyApp.gCurrentUser.getMyFriends());
        }
    };

    BroadcastReceiver requestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateRequestTipSate();
        }
    };

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataHelper.updateFriendFromServer(MyApp.gCurrentUser);
            mView.onUpdateData(MyApp.gCurrentUser.getMyFriends());
        }
    };

    public ContactListPresenter(Context context, ContactListContract.View view) {
        mContext = context;
        mView = view;
        register();
    }

    private void register() {
        IntentFilter intentFilter = new IntentFilter(Constants.LOGIN_SUCCESS_BROADCAST);
        mContext.registerReceiver(loginReceiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter(Constants.UPDATE_FROM_SERVER_BROADCAST);
        mContext.registerReceiver(updateReceiver, intentFilter1);

        IntentFilter intentFilter2 = new IntentFilter(Constants.NEW_REQUEST_BROADCAST);
        mContext.registerReceiver(requestReceiver, intentFilter2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_friend:
                Toast.makeText(mContext, "new Friend", Toast.LENGTH_SHORT).show();
//                AddFriendActivity.startItself(mContext);
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
        }
    }

    @Override
    public void destroy() {
        mContext.unregisterReceiver(loginReceiver);
        mContext.unregisterReceiver(updateReceiver);
        mContext.unregisterReceiver(requestReceiver);
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
}
