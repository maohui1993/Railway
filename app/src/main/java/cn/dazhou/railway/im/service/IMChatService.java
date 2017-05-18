package cn.dazhou.railway.im.service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.R;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;
import cn.dazhou.railway.im.activity.ChatActivity;
import cn.dazhou.railway.im.db.ChatMessageModel;

/**
 * 聊天服务.
 *
 * @author shimiso
 */
public class IMChatService extends Service {
    private Context context;
    private NotificationManager notificationManager;
    private ChatManager chatManager;
    private static String currentChattingUser = "";

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
        EventBus.getDefault().register(this);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		initChatManager();
    }

    private boolean checkJid(String jid) {
        return currentChattingUser.equals(jid);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initChatManager();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initChatManager() {
        XMPPConnection conn = (XMPPConnection) IMLauncher.getImApi().getConnection();
        chatManager = ChatManager.getInstanceFor(conn);
        chatManager.addIncomingListener(incomingChatMessageListener);
    }

    IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            ChatMsgEntity msgEntity = (ChatMsgEntity) Tool.parseJSON(message.getBody(), ChatMsgEntity.class);
            // 标志为接收到的消息
            msgEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            String fromUser = from.getLocalpart().toString().split("@")[0];
            if (checkJid(fromUser)) {
                EventBus.getDefault().post(msgEntity);
                Log.d(Constants.TAG, "New message from " + from + ": " + "to " + message.getFrom() + "body:" + message.getBody());
            } else {
                sendNotification(msgEntity, message.getTo().toString());
            }

            ChatMessageModel chatMessageModel = new ChatMessageModel.Builder()
                    .content(msgEntity.getMessage())
                    .fromJid(message.getFrom().toString())
                    .toJid(message.getTo().toString())
                    .build();
            chatMessageModel.save();
//            ModelAdapter<ChatMessageModel> adapter = FlowManager.getModelAdapter(ChatMessageModel.class);
//            adapter.insert(chatMessageModel);
        }
    };

    private void sendNotification(ChatMsgEntity msg, String jid) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.emotion_aini)
                .setContentTitle("薪消息")
                .setContentText(msg.getMessage())
                .setOngoing(true);
        mBuilder.setTicker("一个新来的消息");//第一次提示消息的时候显示在通知栏上
        mBuilder.setAutoCancel(true);//自己维护通知的消失
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.DATA_KEY, jid);
//        //使用TaskStackBuilder为“通知页面”设置返回关系
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mActivity);
//        //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
//        stackBuilder.addParentStack(LaunchActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
        notificationManager.notify(1, mBuilder.build());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(final String jid) {
        currentChattingUser = jid.split("@")[0];
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, IMChatService.class);
        context.startService(intent);
    }
}
