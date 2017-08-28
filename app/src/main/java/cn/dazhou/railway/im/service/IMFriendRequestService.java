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

import cn.dazhou.database.FriendRequestModel;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.railway.MyActivityManager;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.friend.request.FriendRequestActivity;
import cn.dazhou.railway.splash.SplashActivity;
import cn.dazhou.railway.util.IMUtil;

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
        if (request.getType() == FriendRequest.Type.subscribe) {
            sendNotification(StringUtil.getUsername(request.getJid()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void requestResult(FriendRequest.RequestResult result) {
        switch (result.result) {
            case ACCEPT:
                IMUtil.sendBroadcast(context, Constants.UPDATE_FROM_SERVER_BROADCAST);
                sendNotification(result.jid, "已接受您的好友申请");
                break;
            case REJECT:
                IMUtil.sendBroadcast(context, Constants.UPDATE_FROM_SERVER_BROADCAST);
                sendNotification(result.jid, "已拒绝您的好友申请");
                break;
        }
    }

    private void sendNotification(String jid, String title) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent broadcast = new Intent(context, FriendRequestReceiver.class);
//        broadcast.putExtra(Constants.DATA_KEY, jid);
//        broadcast.putExtra(Constants.NOTIFICATION_ACTION_TYPE, Constants.NOTIFICATION_ACTION_TYPE_ACCEPT);
//        broadcast.putExtra(Constants.NOTIFICATION_ID_KEY, Constants.NOTIFICATION_ID_VALUE_ONE);
//        broadcast.setAction("friend.request.accept");
//        PendingIntent actionIntent = PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.message)
                .setContentTitle("好友通知")
                .setContentText(jid + title)
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setTicker("好友添加通知")
                .setAutoCancel(true);
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(Constants.NOTIFICATION_ID_VALUE_ONE, mBuilder.build());
    }

    private void sendNotification(String jid) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(cn.dazhou.im.R.drawable.emotion_aini)
                .setContentTitle("好友请求")
                .setContentText(jid + "发来好友请求")
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setTicker("收到好友请求");//第一次提示消息的时候显示在通知栏上
        mBuilder.setAutoCancel(true);//自己维护通知的消失

        Intent intent = new Intent(context, FriendRequestActivity.class);
        FriendRequestModel request = new FriendRequestModel();
        request.setFromJid(jid);
        request.setToJid(MyApp.gCurrentUsername);
        request.setState(FriendRequestModel.State.NOT_HANDLE);
        request.save();
        intent.putExtra(Constants.DATA_KEY, request);

        IMUtil.sendBroadcast(context, Constants.NEW_REQUEST_BROADCAST);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
//        mBuilder.setFullScreenIntent(pIntent,true);
        if (MyActivityManager.getInstance().getTopActivity() instanceof FriendRequestActivity) {
            ((FriendRequestActivity)MyActivityManager.getInstance().getTopActivity()).addItem(request);
        } else {
            notificationManager.notify(Constants.NOTIFICATION_ID_VALUE_ONE, mBuilder.build());
        }
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
