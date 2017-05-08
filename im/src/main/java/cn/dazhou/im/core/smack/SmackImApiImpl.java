package cn.dazhou.im.core.smack;

import android.content.Intent;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.util.Collection;

import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.core.function.INewMessageListener;

/**
 * Created by hooyee on 2017/5/5.
 */

public class SmackImApiImpl implements IMApi {
    private static final byte LOGINED_STATE = 1;
    private static final byte NOT_LOGIN_STATE  = -1;

    private static SmackImApiImpl singleton = new SmackImApiImpl();
    private AbstractXMPPConnection mConnection;
    private ChatManager mChatManager;

    private INewMessageListener mMsgListener;

    private byte mState;
    private SmackImApiImpl(){}

    public static SmackImApiImpl getInstance() {
        return singleton;
    }

    /**
     *
     * @param ip 服务器IP
     * @return
     * @throws Exception
     */
    @Override
    public IConnection connect(String ip) throws Exception {
        XMPPTCPConnectionConfiguration config = null;
        InetAddress inetAddress = InetAddress.getByName(ip);
        try {
            config = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(JidCreate.from(ip).asDomainBareJid())
                    .setHostAddress(inetAddress)
                    .setPort(5222)
                    .setConnectTimeout(5000)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
        } catch (Exception e) {
            throw e;
        }
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
                Localpart localpart = from.getLocalpart();
                Jid jid = message.getTo();
                if (mMsgListener != null) {
                    mMsgListener.showNewMessage(message.getBody());
                }
                Log.i("TAG", "New message from " + from + ": " + "to " + message.getTo() + "body:" + message.getBody());
            }
        });
    }

    @Override
    public void chatWith(String jid, String info) throws XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
        EntityBareJid id = JidCreate.entityBareFrom(jid);
        Chat chat = mChatManager.chatWith(id);
        chat.send(info);
    }

    @Override
    public Roster getRoster() {
        Roster roster = Roster.getInstanceFor(mConnection);
        try {
            roster.createEntry(JidCreate.entityBareFrom("maohui"+"@192.168.1.39"), "maohui", new String[]{"Friends"});
        } catch (Exception e) {

        }
        Log.i("TAG", "group-size: " + roster.getGroupCount());
        Collection<RosterEntry> entries = roster.getEntries();
        Log.i("TAG", "entrys-size: " + entries.size());
        for (RosterEntry entry : entries) {
            Log.i("TAG", "entry: " + entry);
        }

        return roster;
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
                Presence presence = (Presence)packet;
                Jid from = presence.getFrom();//发送方
                Jid to = presence.getTo();//接收方
                if (presence.getType().equals(Presence.Type.subscribe)) {
                    Log.i("TAG", "收到添加请求！" + "发送方："+ from.getLocalpartOrNull() + "接收方：" + to.getLocalpartOrNull());
                }
            }
        }
    }
}
