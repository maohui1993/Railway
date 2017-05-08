package cn.dazhou.im.core.smack;

import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.util.Collection;

import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;

/**
 * Created by hooyee on 2017/5/5.
 */

public class SmackImApiImpl implements IMApi {
    private static final byte LOGINED_STATE = 1;
    private static final byte NOT_LOGIN_STATE  = -1;


    private static SmackImApiImpl singleton = new SmackImApiImpl();
    private AbstractXMPPConnection mConnection;
    private ChatManager mChatManager;
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
        return this;
    }

    public void login(String username, String password) throws Exception {
        if (mState == LOGINED_STATE) {
            return;
        }
        try {
            mConnection.login(username, password);
            mState = LOGINED_STATE;
        } catch (Exception e) {
            mState = NOT_LOGIN_STATE;
            throw e;
        }
    }

    /**
     * 与jid聊天
     * @param jid
     * @param info
     * @throws XmppStringprepException
     * @throws SmackException.NotConnectedException
     * @throws InterruptedException
     */
    @Override
    public void chatWith(String jid, String info) throws XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
        mChatManager = ChatManager.getInstanceFor(mConnection);
        mChatManager.addListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                Log.i("TAG", "New message from " + from + ": " + message.getBody() + chat.toString());
            }
        });
        EntityBareJid id = JidCreate.entityBareFrom(jid);
        Chat chat = mChatManager.chatWith(id);
        chat.send(info);
        Log.i("TAG", "New message from " + jid );
    }

    @Override
    public void showRoster() {
        Roster roster = Roster.getInstanceFor(mConnection);
        try {
            roster.createEntry(JidCreate.entityBareFrom("test"+"@10.0.0.4"), "maohui", new String[]{"Friends"});
        } catch (Exception e) {

        }
        Log.i("TAG", "group-size: " + roster.getGroupCount());
        Collection<RosterEntry> entries = roster.getEntries();
        Log.i("TAG", "entrys-size: " + entries.size());
        for (RosterEntry entry : entries) {
            Log.i("TAG", "entry: " + entry);
        }
    }
}
