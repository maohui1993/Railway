package cn.dazhou.railway.im.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.FriendRequestModel;
import cn.dazhou.railway.im.friend.request.FriendRequestActivity;

/**
 * 好友请求服务.
 *
 * @author hooyee
 */
public class IMFriendRequestService extends Service {
    private Context context;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
        EventBus.getDefault().register(this);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleFriendRequest(FriendRequest request) {
        Log.i("TAG", "IMFriendRequestService: " + request.getJid());
        if (request.getType() == FriendRequest.Type.unsubscribed) {
            sendNotification(request.getJid());
        }
    }

    private void sendNotification(String jid) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent broadcast = new Intent(context, FriendRequestReceiver.class);
//        broadcast.putExtra(Constants.DATA_KEY, jid);
//        broadcast.putExtra(Constants.NOTIFICATION_ACTION_TYPE, Constants.NOTIFICATION_ACTION_TYPE_ACCEPT);
//        broadcast.putExtra(Constants.NOTIFICATION_ID_KEY, Constants.NOTIFICATION_ID_VALUE_ONE);
//        broadcast.setAction("friend.request.accept");
//        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(cn.dazhou.im.R.drawable.emotion_aini)
                .setContentTitle("好友请求")
                .setContentText(jid + "发来好友请求")
//                .addAction(new NotificationCompat.Action(cn.dazhou.im.R.mipmap.icon_chat_add, "接收", actionIntent))
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setTicker("一个新来的消息");//第一次提示消息的时候显示在通知栏上
        mBuilder.setAutoCancel(true);//自己维护通知的消失

        Intent intent = new Intent(context, FriendRequestActivity.class);
        FriendRequestModel request = new FriendRequestModel();
        request.setFromJid(jid);
        request.setToJid(MyApp.gCurrentUsername);
        request.setState(FriendRequestModel.State.NOT_HANDLE);
        intent.putExtra(Constants.DATA_KEY, request);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
//        mBuilder.setFullScreenIntent(pIntent,true);
        notificationManager.notify(Constants.NOTIFICATION_ID_VALUE_ONE, mBuilder.build());
    }

    @Override
    public boolean stopService(Intent name) {
        EventBus.getDefault().unregister(this);
        return super.stopService(name);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, IMFriendRequestService.class);
        context.startService(intent);
    }

    public static void stopItself(Context context) {
        Intent intent = new Intent(context, IMFriendRequestService.class);
        context.stopService(intent);
    }
}
