package cn.dazhou.railway.im.service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.EntityBareJid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.R;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.Tool;
import cn.dazhou.im.util.Utils;
import cn.dazhou.railway.im.activity.ChatActivity;

/**
 * 
 * 聊天服务.
 * 
 * @author shimiso
 */
public class IMChatService extends Service {
	private Context context;
	private NotificationManager notificationManager;
	private ChatManager chatManager;
	private static String currentJid;
	private static Class currentActivityTyped;
	private static Class neededActivityTyped = ChatActivity.class;

	@Override
	public void onCreate() {
		context = this;
		super.onCreate();
		EventBus.getDefault().register(this);
		initChatManager();
	}

	public static void updateCurrentJid(String jid) {
		currentJid = jid;
	}

	public static void updateCurrentActivityTyped(Class type) {
		currentActivityTyped = type;
	}

	private boolean checkActivityType() {
		return currentActivityTyped == neededActivityTyped;
	}

	private boolean checkJid(String jid) {
		return currentJid.equals(jid);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
			//if (checkActivityType()) {
				if(checkJid(from.getLocalpart().toString())) {
					EventBus.getDefault().post(msgEntity);
					Log.d(Constants.TAG, "New message from " + from + ": " + "to " + message.getFrom() + "body:" + message.getBody());
		//		}
			} else {
				sendNotification(msgEntity, message.getTo().toString());
			}
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
		currentJid = jid;
	}

	public static void startItself(Context context) {
		Intent intent = new Intent(context, IMChatService.class);
		context.startService(intent);
	}
}
