package cn.dazhou.railway.im.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.FriendRequest;

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
            IMLauncher.addFriend(request.getJid());
        }
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, IMFriendRequestService.class);
        context.startService(intent);
    }
}
