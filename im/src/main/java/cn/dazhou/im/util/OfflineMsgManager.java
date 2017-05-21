package cn.dazhou.im.util;

/**
 * Created by hooyee on 2017/5/16.
 */

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import java.util.List;

import cn.dazhou.im.entity.ChatMessageEntity;

/**
 * 离线信息管理类.
 */
public class OfflineMsgManager {
    private static OfflineMsgManager offlineMsgManager = null;
    private Context context;

    private OfflineMsgManager(Context context) {
        this.context = context;
    }

    public static OfflineMsgManager getInstance(Context context) {
        if (offlineMsgManager == null) {
            offlineMsgManager = new OfflineMsgManager(context);
        }

        return offlineMsgManager;
    }

    /**
     * 处理离线消息.
     *
     * @param connection
     */
    public void dealOfflineMsg(XMPPConnection connection) {
        OfflineMessageManager offlineManager = new OfflineMessageManager(
                connection);

        try {
            List<Message> messages = offlineManager.getMessages();
            Log.i("离线消息数量: ", "" + offlineManager.getMessageCount());
            for (Message message : messages){
                Log.i("收到离线消息", "Received from 【" + message.getFrom() + "】 message: " + message.getBody());
                if (message != null && message.getBody() != null
                        && !message.getBody().equals("null")) {

                    String from = message.getFrom().toString().split("/")[0];
                    message.setSubject(from);
                    ChatMessageEntity msgEntity = (ChatMessageEntity) Tool.parseJSON(message.getBody(), ChatMessageEntity.class);
                    // 标志为接收到的消息
                    msgEntity.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                    EventBus.getDefault().post(msgEntity);
                }
            }
            offlineManager.deleteMessages();
            //将状态设置成在线
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        }
    }
}