package cn.dazhou.im.core.smack;

import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.core.function.INewMessageListener;
import cn.dazhou.im.modle.ChatMsgEntity;
import cn.dazhou.im.util.Tool;

/**
 * Created by hooyee on 2017/5/5.
 */

public class SmackImApiImpl implements IMApi {
    private static final byte LOGINED_STATE = 1;
    private static final byte NOT_LOGIN_STATE = -1;

    private static SmackImApiImpl singleton = new SmackImApiImpl();
    private AbstractXMPPConnection mConnection;
    private ChatManager mChatManager;
    private List<Message> mMsgCache = new ArrayList<>();

    private INewMessageListener mMsgListener;

    private byte mState;

    private Chat mChat;

    private SmackImApiImpl() {
    }

    public static SmackImApiImpl getInstance() {
        return singleton;
    }

    /**
     * @param ip 服务器IP
     * @return
     * @throws Exception
     */
    @Override
    public IConnection connect(String ip) throws Exception {
        XMPPTCPConnectionConfiguration config = null;
        InetAddress inetAddress = InetAddress.getByName(ip);
        config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(JidCreate.from(ip).asDomainBareJid())
                .setHostAddress(inetAddress)
                .setPort(5222)
                .setConnectTimeout(5000)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

        mConnection = new XMPPTCPConnection(config);
        mConnection.connect();
        addPacketSendListener(new MyStanzaListener());

        return this;
    }

    public void addPacketSendListener(StanzaListener stanzaListener) {
        //条件过滤器
        StanzaFilter filter = (StanzaFilter) new AndFilter(new PacketTypeFilter(Presence.class));
        addPacketSendListener(stanzaListener, filter);
    }

    public void addPacketSendListener(StanzaListener packetListener, StanzaFilter packetFilter) {
        mConnection.addPacketSendingListener(packetListener, packetFilter);
    }

    public void login(String username, String password) throws Exception {
        if (mState == LOGINED_STATE) {
            return;
        }
        try {
            mConnection.login(username, password);
            mState = LOGINED_STATE;
            listenNewMassage();
        } catch (Exception e) {
            mState = NOT_LOGIN_STATE;
            throw e;
        }
    }

    private void listenNewMassage() {
        if (mChatManager == null) {
            mChatManager = ChatManager.getInstanceFor(mConnection);
        }
        mChatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                // 回调，将数据传给ChatActivity显示出来
                if (mMsgListener != null) {
                    mMsgListener.showNewMessage(message);
                } else {
                    mMsgCache.add(message);
                    handleOfflineMessage();
                }
                Log.i("TAG", "New message from " + from + ": " + "to " + message.getTo() + "body:" + message.getBody());
            }
        });
    }

    Thread thread;

    void handleOfflineMessage() {
        // 已经起了线程去处理离线消息
        if (thread != null) {
            return;
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mMsgListener != null) {
                        for (Message msg : mMsgCache) {
                            mMsgListener.showNewMessage(msg);
                        }
                        mMsgCache.clear();
                        return;
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void chatWith(String jid, ChatMsgEntity msg) throws XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
        EntityBareJid id = JidCreate.entityBareFrom(jid);
        mChat = mChatManager.chatWith(id);
        String msgJson = Tool.toJSON(msg);
        mChat.send(msgJson);
    }

    @Override
    public void chatWith(EntityBareJid jid) {
        mChat = mChatManager.chatWith(jid);
    }

    @Override
    public void chat(ChatMsgEntity msg) throws Exception {
        if (mChat == null) {
            return;
        }
        String msgJson = Tool.toJSON(msg);
        mChat.send(msgJson);
    }

    @Override
    public Roster getRoster() {
        return Roster.getInstanceFor(mConnection);
    }

    @Override
    public Roster addFriend(String jid) {
        if (mConnection == null) return null;
        Roster roster = Roster.getInstanceFor(mConnection);
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);

        try {
            // 添加好友
            roster.createEntry(JidCreate.entityBareFrom("maohui@"+jid), "MAOHUI", new String[]{"Friends"});
            roster.createEntry(JidCreate.entityBareFrom("hooyee@"+jid), "HOOYEE", new String[]{"Friends"});
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public void setOnNewMessageListener(INewMessageListener listener) {
        mMsgListener = listener;
    }

    class MyStanzaListener implements StanzaListener {

        @Override
        public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
            Log.i("TAG", "processStanza");
            if (packet instanceof Presence) {
                Presence presence = (Presence) packet;
                Jid from = presence.getFrom();//发送方
                Jid to = presence.getTo();//接收方
                if (presence.getType().equals(Presence.Type.subscribe)) {
                    Log.i("TAG", "收到添加请求！" + "发送方：" + from.getLocalpartOrNull() + "接收方：" + to.getLocalpartOrNull());
                }
            }
        }
    }
}
