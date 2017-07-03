package cn.dazhou.railway.im.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jxmpp.jid.EntityBareJid;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.chat.ChatActivity;
import cn.dazhou.railway.im.db.ChatMessageModel;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.splash.functions.contact.ContactListFragment;
import cn.dazhou.railway.util.LogUtil;

/**
 * 聊天服务.
 *
 * @author hooyee
 */
public class IMChatService extends Service {
    private Context context;
    private NotificationManager notificationManager;
    private ChatManager chatManager;
    private static String currentChattingUser = "";
    private XMPPConnection conn;
    private Timer timer = new Timer();
    private FileTransferManager transferManager;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        EventBus.getDefault().register(this);
//        reConnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initManager();
        handleOfflineMessage();
        return super.onStartCommand(intent, flags, startId);
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
            chatMessageEntities.clear();
        }
    }

    private boolean checkJid(String jid) {
        return currentChattingUser.equals(jid);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void reConnect() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!conn.isConnected()) {
                    try {
                        ((AbstractXMPPConnection) conn).connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
        timer.schedule(task, 1000, 3000);
    }

    private void initManager() {
        try {
            conn = (XMPPConnection) IMLauncher.getImApi().getConnection();
            if (conn == null) {
                Log.i("TAG", "IMChatService#initManager(),获取服务器连接为null");
            }
            chatManager = ChatManager.getInstanceFor(conn);
            chatManager.addIncomingListener(incomingChatMessageListener);

            transferManager = FileTransferManager.getInstanceFor(conn);
            transferManager.addFileTransferListener(fileTransferListener);
        } catch (Exception e) {
            Log.i("TAG", "IMChatService#initManager(),获取服务器连接失败:" + e.getMessage());
            LogUtil.write(e);
        }

    }

    /**
     * 收到的消息统一在这里处理
     */
    IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            Log.i("TAG", message.getBody());
            ChatMessageEntity chatMessageEntity = (ChatMessageEntity) Tool.parseJSON(message.getBody(), ChatMessageEntity.class);
            // 标志为接收到的消息
            chatMessageEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            chatMessageEntity.setDate(System.currentTimeMillis());
            chatMessageEntity.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            String fromUser = from.getLocalpart().toString().split("@")[0];
            String imagePath = null;
            String voicePath = null;
            // 语音与图片不能同时发送
            if (chatMessageEntity.getImageBytes() != null) {
                imagePath = Tool.saveByteToLocalFile(chatMessageEntity.getImageBytes(), System.currentTimeMillis() + ".png");
            } else if (chatMessageEntity.getVoiceBytes() != null) {
                voicePath = Tool.saveByteToLocalFile(chatMessageEntity.getVoiceBytes(), System.currentTimeMillis() + ".aar");
            }
            ChatMessageModel chatMessageModel = new ChatMessageModel.Builder()
                    .content(chatMessageEntity.getContent())
                    .fromJid(chatMessageEntity.getFromJid())
                    .toJid(chatMessageEntity.getToJid())
                    .voicePath(voicePath)
                    .voiceTime(chatMessageEntity.getVoiceTime())
                    .imagePath(imagePath)
                    .type(chatMessageEntity.getType())
                    .jid(fromUser.toString())
                    .date(System.currentTimeMillis())
                    .build();
            chatMessageEntity.setVoicePath(voicePath);
            chatMessageEntity.setImagePath(imagePath);
            // 若聊天对象的窗口已经打开，则不发送通知
            if (checkJid(fromUser)) {
                chatMessageModel.setState(true);
                // 统一交由ChatContentView#showMessage中展示
                EventBus.getDefault().post(chatMessageEntity);
                Log.d(Constants.TAG, "New message from " + from + ": " + "to " + message.getFrom() + "body:" + message.getBody());
            } else {
                chatMessageModel.setState(false);
                sendNotification(chatMessageEntity, chatMessageModel.getJid());
            }
            // 好友聊天的最后一条消息
            EventBus.getDefault().post(new ContactListFragment.TipMessage(chatMessageModel.getJid(), chatMessageModel.getContent()));
            // 有新消息则在对应的好友上面加上 消息数量
            FriendModel friend = new FriendModel();
            friend.setJid(chatMessageModel.getJid());
            EventBus.getDefault().post(friend);
            chatMessageModel.save();
        }
    };

    private void sendNotification(ChatMessageEntity msg, String jid) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.message)
                .setContentTitle(getResources().getString(R.string.new_message))
                .setContentText(msg.getContent())
                .setOngoing(true);
        mBuilder.setTicker("一个新来的消息");//第一次提示消息的时候显示在通知栏上
        mBuilder.setAutoCancel(true);//自己维护通知的消失
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(cn.dazhou.railway.config.Constants.DATA_KEY, jid);
        //使用TaskStackBuilder为“通知页面”设置返回关系
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
//        stackBuilder.addParentStack(SplashActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pIntent);
        notificationManager.notify(1, mBuilder.build());
    }

    /**
     * 文件接收监听器
     */
    FileTransferListener fileTransferListener = new FileTransferListener() {

        @Override
        public void fileTransferRequest(FileTransferRequest request) {
            //每次有文件发送过来都会调用些方法
            //调用request的accetp表示接收文件，也可以调用reject方法拒绝接收
            final IncomingFileTransfer inTransfer = request.accept();
            try {
                Log.i("TAG", "接收到文件发送请求，文件名称：" + request.getFileName());
                //接收到的文件要放在哪里
                File dir = new File(cn.dazhou.railway.config.Constants.FILE_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                final File file = new File(dir, request.getFileName());
                inTransfer.recieveFile(file);
                //如果要时时获取文件接收的状态必须在线程中监听，如果在当前线程监听文件状态会导致一下接收为0
                new Thread() {
                    @Override
                    public void run() {
                        long startTime = System.currentTimeMillis();
                        while (!inTransfer.isDone()) {
                            if (inTransfer.getStatus().equals(FileTransfer.Status.error)) {
                                Log.w("TAG", "error: " + inTransfer.getError());
                            } else {
                                double progress = inTransfer.getProgress();
                                progress *= 100;
                                Log.i("TAG", "status=" + inTransfer.getStatus());
                                Log.i("TAG", "progress=" + progress + "%");
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        fileScan(file);
                        Log.i("TAG", "used " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds  ");
                    }
                }.start();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void fileScan(File file){
        Uri data = Uri.fromFile(file);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE , data));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventBus(final String jid) {
        currentChattingUser = jid.split("@")[0];
    }

    @Override
    public boolean stopService(Intent name) {
        EventBus.getDefault().unregister(this);
        return super.stopService(name);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, IMChatService.class);
        context.startService(intent);
    }

    public static void stopItself(Context context) {
        Intent intent = new Intent(context, IMChatService.class);
        context.stopService(intent);
    }
}
