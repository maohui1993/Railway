package cn.dazhou.railway.im.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.util.Set;

import cn.dazhou.database.ChatMessageModel;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.ImageUtil;
import cn.dazhou.im.util.JsonUtil;
import cn.dazhou.railway.util.LogUtil;

public class IMChatRoomService extends Service {
    // 群聊管理器
    private MultiUserChatManager multiUserChatManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initMultiUserChatManager();
    }

    private void initMultiUserChatManager() {
        try {
            XMPPConnection conn = (XMPPConnection) IMLauncher.getImApi().getConnection();
            if (conn == null) {
                Log.i("TAG", "IMChatService#initChatManager(),获取服务器连接为null");
            }
            multiUserChatManager = MultiUserChatManager.getInstanceFor(conn);
            Set<EntityBareJid> roomJids = multiUserChatManager.getJoinedRooms();
            for (EntityBareJid jid : roomJids) {
                MultiUserChat multiUserChat = multiUserChatManager.getMultiUserChat(JidCreate.entityBareFrom(jid));
                multiUserChat.addMessageListener(messageListener);
            }
        }catch (Exception e) {
            Log.i("TAG", "IMChatService#initChatManager(),获取服务器连接失败");
            LogUtil.write(e);
        }
    }

    MessageListener messageListener = new MessageListener() {
        @Override
        public void processMessage(Message message) {
            ChatMessageEntity chatMessageEntity = JsonUtil.parseJSON(message.getBody(), ChatMessageEntity.class);
            // 标志为接收到的消息
            chatMessageEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            String fromUser = message.getFrom().getLocalpartOrNull().toString().split("@")[0];
            String imagePath = null;
            String voicePath = null;
            // 语音与图片不能同时发送
            if (chatMessageEntity.getImageBytes() != null) {
                imagePath = ImageUtil.saveByteToLocalFile(chatMessageEntity.getImageBytes(), System.currentTimeMillis() + ".png");
            } else if (chatMessageEntity.getVoiceBytes() != null) {
                voicePath = ImageUtil.saveByteToLocalFile(chatMessageEntity.getVoiceBytes(), System.currentTimeMillis() + ".aar");
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
                    .build();
            chatMessageEntity.setVoicePath(voicePath);
            chatMessageEntity.setImagePath(imagePath);
            // 若聊天对象的窗口已经打开，则不发送通知
//            if (checkJid(fromUser)) {
//                chatMessageModel.setState(true);
//                EventBus.getDefault().post(chatMessageEntity);
//                Log.d(Constants.TAG, "New message from " + from + ": " + "to " + message.getFrom() + "body:" + message.getBody());
//            } else {
//                chatMessageModel.setState(false);
//                sendNotification(chatMessageEntity, chatMessageModel.getJid());
//            }
            chatMessageModel.save();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
