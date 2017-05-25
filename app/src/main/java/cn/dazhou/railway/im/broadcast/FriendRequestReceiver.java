package cn.dazhou.railway.im.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.FriendModel;

public class FriendRequestReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("TAG", "接收到广播事件");
        final String jid = intent.getStringExtra(Constants.DATA_KEY);
        int actionType = intent.getIntExtra(Constants.NOTIFICATION_ACTION_TYPE, -1);
        int notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID_KEY, -1);
        if (actionType == Constants.NOTIFICATION_ACTION_TYPE_ACCEPT) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    acceptFriendRequest(jid);
                }
            }).start();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

    private void acceptFriendRequest(String jid) {
        IMLauncher.addFriend(jid);
        FriendModel friend = new FriendModel();
        friend.setJid(jid);
        friend.setPossessor(MyApp.gCurrentUsername);
        friend.setName("你好");
        MyApp.gCurrentUser.getMyFriends().add(friend);
        MyApp.gCurrentUser.save();
    }
}
