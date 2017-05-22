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

import java.util.List;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.R;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;
import cn.dazhou.railway.MyApp;
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
        handleOfflineMessage();
    }

    private void handleOfflineMessage() {
        List<ChatMessageEntity> chatMessageEntities = IMLauncher.getOfflineMessage();
        if (chatMessageEntities != null && chatMessageEntities.size() > 0) {
            for (ChatMessageEntity message : chatMessageEntities) {
                // 标志为接收到的消息
                message.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                String fromUser = message.getFromJid().split("@")[0];
                ChatMessageModel chatMessageModel = new ChatMessageModel.Builder()
                        .content(message.getContent())
                        .voicePath(message.getVoicePath())
                        .imagePath(message.getImagePath())
                        .jid(fromUser)
                        .type(message.getType())
                        .build();
                chatMessageModel.setVoiceTime(message.getVoiceTime());
                chatMessageModel.setState(false);
                chatMessageModel.save();
                sendNotification(message, chatMessageModel.getJid());
            }
        }
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

    // 收到新消息统一在这里处理
    IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            ChatMessageEntity chatMessageEntity = (ChatMessageEntity) Tool.parseJSON(message.getBody(), ChatMessageEntity.class);
            // 标志为接收到的消息
            chatMessageEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            String fromUser = from.getLocalpart().toString().split("@")[0];
            ChatMessageModel chatMessageModel = new ChatMessageModel.Builder()
                    .content(chatMessageEntity.getContent())
                    .fromJid(message.getFrom().toString())
                    .toJid(message.getTo().toString())
                    .voicePath(chatMessageEntity.getVoicePath())
                    .imagePath(chatMessageEntity.getImagePath())
                    .type(chatMessageEntity.getType())
                    .jid(fromUser.toString())
                    .build();
            if (checkJid(fromUser)) {
                chatMessageModel.setState(true);
                EventBus.getDefault().post(chatMessageEntity);
                Log.d(Constants.TAG, "New message from " + from + ": " + "to " + message.getFrom() + "body:" + message.getBody());
            } else {
                chatMessageModel.setState(false);
                sendNotification(chatMessageEntity, chatMessageModel.getJid());
            }
            chatMessageModel.save();
        }
    };

    private void sendNotification(ChatMessageEntity msg, String jid) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.emotion_aini)
                .setContentTitle("薪消息")
                .setContentText(msg.getContent())
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
