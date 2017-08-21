package cn.dazhou.railway.im.service;

import android.app.Notification;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.dazhou.database.ChatMessageModel;
import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.ProcessEvent;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.ImageUtil;
import cn.dazhou.im.util.JsonUtil;
import cn.dazhou.im.util.JudgeMultiMediaType;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.chat.ChatActivity;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

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
        Log.i("IM-Service", "onCreate");
        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        EventBus.getDefault().register(this);
        handleOfflineMessage();
//        checkHeartbeat();
        Log.i("IM-Service", notificationManager.toString());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("IM-Service", "start");
        initManager();
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleOfflineMessage() {
        Log.i("TAG", "offline!!!!!!!!");
        List<ChatMessageEntity> chatMessageEntities = IMLauncher.getOfflineMessage();
        if (chatMessageEntities != null && chatMessageEntities.size() > 0) {
            for (ChatMessageEntity message : chatMessageEntities) {
                // 标志为接收到的消息
                message.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                String fromUser = message.getFromJid().split("@")[0];
                message.setJid(fromUser);
                ChatMessageModel chatMessageModel = ChatMessageModel.newInstance(message);
                chatMessageModel.setJid(StringUtil.getWrapJid(fromUser));
                chatMessageModel.save();
                sendNotification(message, chatMessageModel.getJid());
            }
            chatMessageEntities.clear();
            Log.i("TAG", "clear!!!!!!!!");
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

    private void checkHeartbeat() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendHeartbeatPackage();
            }
        };

        timer.schedule(task, 1000 * 10, 1000 * 10);
    }

    private void initManager() {
        try {
            conn = (XMPPConnection) IMLauncher.getImApi().getConnection();
            if (conn == null) {
                Log.i("TAG", "IMChatService#initManager(),获取服务器连接为null");
                return;
            }
            chatManager = ChatManager.getInstanceFor(conn);
            chatManager.addIncomingListener(incomingChatMessageListener);

            transferManager = FileTransferManager.getInstanceFor(conn);
            transferManager.addFileTransferListener(fileTransferListener);
        } catch (Exception e) {
            Log.i("TAG", "IMChatService#initManager(),获取服务器连接失败:" + e.getMessage());
            LogUtil.write(e);
            stopItself(this);
        }

    }

    /**
     * 收到的消息统一在这里处理
     */
    IncomingChatMessageListener incomingChatMessageListener = new IncomingChatMessageListener() {
        @Override
        public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
            Log.i("TAG", message.getBody());
            ChatMessageEntity chatMessageEntity = JsonUtil.parseJSON(message.getBody(), ChatMessageEntity.class);
            if (chatMessageEntity == null) {
                return;
            }
            chatMessageEntity.setDate(System.currentTimeMillis());
            // 标志为接收到的消息
            chatMessageEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            chatMessageEntity.setDate(System.currentTimeMillis());
            chatMessageEntity.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
            String fromUser = from.getLocalpart().toString().split(Constants.JID_SEPARATOR)[0];
            chatMessageEntity.setJid(fromUser);
            // 语音与图片不能同时发送
            if (chatMessageEntity.getImageBytes() != null) {
                String imagePath = ImageUtil.saveByteToLocalFile(chatMessageEntity.getImageBytes(), System.currentTimeMillis() + ".png");
                chatMessageEntity.setImagePath(imagePath);
                chatMessageEntity.setImageBytes(null);
            } else if (chatMessageEntity.getVoiceBytes() != null) {
                String voicePath = ImageUtil.saveByteToLocalFile(chatMessageEntity.getVoiceBytes(), System.currentTimeMillis() + ".aar");
                chatMessageEntity.setVoicePath(voicePath);
                chatMessageEntity.setVoiceBytes(null);
            }

            ChatMessageModel chatDb = ChatMessageModel.newInstance(chatMessageEntity);
            chatDb.setJid(StringUtil.getWrapJid(fromUser));
            // 若聊天对象的窗口已经打开，则不发送通知
            if (checkJid(fromUser)) {
                chatDb.setState(true);
                // 统一交由ChatContentView#showMessage中展示
                EventBus.getDefault().post(chatMessageEntity);
                Log.d(Constants.TAG, "New message from " + from + ": " + "to " + message.getFrom() + "body:" + message.getBody());
            } else {
                chatDb.setState(false);
                sendNotification(chatMessageEntity, chatDb.getJid());
            }

            FriendModel friend = DataHelper.getFriend(chatDb.getJid());

            // 新消息+1
            friend.notReadCountAutoAddOne();
            // 加入到消息列表
            friend.setInMessageList(true);
            // 更新上次聊天时间为当前时间
            friend.setLastChatTime(System.currentTimeMillis());
            friend.save();

            /**
             * 有新消息则在消息列表中置顶
             * @see cn.dazhou.railway.im.friend.message.list.MessageListFragment#updateTipMessage
             * @param friendModel
             */
            EventBus.getDefault().post(friend);
            chatDb.save();
        }
    };

    private String getTipString(ChatMessageModel chatDbApi) {
        String tip = null;
        // 好友聊天的最后一条消息
        switch (chatDbApi.getDataType()) {
            case file:
                tip = "[文件消息]";
                break;
            case picture:
                tip = "[图片消息]";
                break;
            case video:
                tip = "[视频消息]";
                break;
            case voice:
                tip = "[语音消息]";
                break;
            case text:
                tip = chatDbApi.getContent();
                break;
            default:
                break;
        }
        return tip;
    }

    private void sendHeartbeatPackage() {
        try {
            URL url = new URL("http://192.168.1.6:8088");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();
            int code = connection.getResponseCode();
            Log.i("login-test", "code = " + code);
            if (code == 200 && !conn.isConnected()) {
                IMUtil.login(context);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(ChatMessageEntity msg, String jid) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.message)
                .setContentTitle(getResources().getString(R.string.new_message))
                .setContentText(msg.getContent())
                .setDefaults(Notification.DEFAULT_ALL)
                .setOngoing(true);
        mBuilder.setTicker("一个新的消息");//第一次提示消息的时候显示在通知栏上
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
        Notification notification = mBuilder.build();
        boolean canRingtone = SharedPreferenceUtil.getBoolean(context, cn.dazhou.railway.config.Constants.CAN_RINGTONE, true);
        boolean canShake = SharedPreferenceUtil.getBoolean(context, cn.dazhou.railway.config.Constants.CAN_SHAKE, true);
        // 同为true
        if (canRingtone && canShake) {
            notification.defaults = Notification.DEFAULT_ALL;
            // 同为false
        } else if (!canRingtone && !canShake) {
            notification.defaults = Notification.DEFAULT_LIGHTS;
            // ringtone为true
        } else if (canRingtone) {
            notification.defaults = Notification.DEFAULT_SOUND;
            // shake为false
        } else {
            notification.defaults = Notification.DEFAULT_VIBRATE;
        }

        notificationManager.notify(1, notification);
    }

    /**
     * 文件接收监听器
     */
    FileTransferListener fileTransferListener = new FileTransferListener() {
        JudgeMultiMediaType judgeMultiMediaType = new JudgeMultiMediaType();
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
                String jid = request.getRequestor().toString().split(cn.dazhou.railway.config.Constants.JID_SEPARATOR)[0];

                int fileType = judgeMultiMediaType.getMediaFileType(file.getPath());
                ChatMessageEntity.Type type = judgeMultiMediaType.isVideoFile(fileType) ? ChatMessageEntity.Type.video : ChatMessageEntity.Type.file;

                final ChatMessageEntity chatMessage = new ChatMessageEntity.Builder()
                        .jid(StringUtil.getRealJid(jid))
                        .filePath(file.getAbsolutePath())
                        .dataType(type)
                        .date(System.currentTimeMillis())
                        .type(Constants.CHAT_ITEM_TYPE_LEFT)
                        .build();
                ChatMessageModel chatMessageModel = ChatMessageModel.newInstance(chatMessage);
                chatMessageModel.setJid(StringUtil.getWrapJid(chatMessage.getJid()));
                chatMessageModel.save();
                // 目标聊天窗口是否已经打开
                if (checkJid(jid)) {
                    // 统一交由ChatContentView#showMessage中展示
                    EventBus.getDefault().post(chatMessage);
                } else {
                    sendNotification(chatMessage, chatMessageModel.getJid());
                }
                //如果要时时获取文件接收的状态必须在线程中监听，如果在当前线程监听文件状态会导致一下接收为0
                new Thread() {
                    @Override
                    public void run() {
                        long startTime = System.currentTimeMillis();
                        ProcessEvent event = new ProcessEvent(file.getAbsolutePath());
                        event.setType(chatMessage.getType());
                        event.setDate(chatMessage.getDate());
                        event.setDataType(chatMessage.getDataType());
                        while (!inTransfer.isDone()) {
                            if (inTransfer.getStatus().equals(FileTransfer.Status.error)) {
                                Log.w("TAG", "error: " + inTransfer.getError());
                            } else {
                                double progress = inTransfer.getProgress();
                                progress *= 100;
                                event.setProcess((int)progress);
                                EventBus.getDefault().post(event);
                                Log.i("TAG", "progress=" + progress + "%");
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        event.setProcess(100);
                        EventBus.getDefault().post(event);
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
