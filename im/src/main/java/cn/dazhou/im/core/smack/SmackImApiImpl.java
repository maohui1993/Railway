package cn.dazhou.im.core.smack;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.im.util.OfflineMsgManager;
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
    private Context mContext;

    private byte mState;
    private Chat mChat;

    private SmackImApiImpl() {
    }

    public static SmackImApiImpl getInstance(Context context) {
        singleton.mContext = context;
        return singleton;
    }

    @Override
    public XMPPConnection getConnection() {
        return mConnection;
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
                .setConnectTimeout(10000)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(false)
                .build();

        mConnection = new XMPPTCPConnection(config);
        mConnection.connect();
        addPacketSendListener(new MyStanzaListener());

        return this;
    }

    @Override
    public void disconnect() {
        mConnection.disconnect();
    }

    public void addPacketSendListener(StanzaListener stanzaListener) {
        //条件过滤器
        StanzaFilter filter = new AndFilter(new StanzaTypeFilter(Presence.class));
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
            mChatManager = ChatManager.getInstanceFor(mConnection);
            mState = LOGINED_STATE;
            // 处理离线消息
            OfflineMsgManager.getInstance(mContext).dealOfflineMsg(mConnection);
        } catch (Exception e) {
            mState = NOT_LOGIN_STATE;
            throw e;
        }
    }

    @Override
    public void chatWith(String jid, ChatMessageEntity msg) throws XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
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
    public void chat(ChatMessageEntity msg) throws Exception {
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
//        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        Presence presenceRes = new Presence(Presence.Type.subscribe);
        presenceRes.setTo(jid);
        try {
            mConnection.sendStanza(presenceRes);
//            roster.createEntry(JidCreate.entityBareFrom(jid), jid, new String[]{"Friends"});
        } catch (Exception e) {
            Log.e("TAG", "SmackImApiImpl.class: " + "好友添加失败");
            Log.e("TAG", "SmackImApiImpl.class: " + e.getMessage());
        }
        return null;
    }

    public void sendSubscribe(String jid) {

    }

    public boolean acceptFriendRequest(String jid) {
        Presence presenceRes = new Presence(Presence.Type.subscribed);
        try {
            presenceRes.setTo(JidCreate.entityBareFrom(jid));
            mConnection.sendStanza(presenceRes);
            return true;
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<UserBean> searchUserFromServer(String username) {
        try {
            List<UserBean> users = new ArrayList();
            UserSearchManager search = new UserSearchManager(mConnection);
            Form searchForm = search.getSearchForm(search.getSearchServices().get(0));
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", username);
            ReportedData data = search.getSearchResults(answerForm, search.getSearchServices().get(0));
            for (ReportedData.Row row : data.getRows()) {
                Log.i("TAG", "SmackImApiImpl.class: " + "username = " + row.getValues("Username").toString());
                UserBean user = new UserBean();
                user.setUsername(row.getValues("Username").get(0));
                user.setName(row.getValues("Name").get(0));
                user.setEmail(row.getValues("Email").get(0));
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            Log.i("TAG", "SmackImApiImpl.class : " + e.getMessage());
        }
        return null;
    }

    class MyStanzaListener implements StanzaListener {

        @Override
        public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException {
            Log.i("TAG", "PresenceService-" + packet.toXML());
            if (packet instanceof Presence) {
                Presence presence = (Presence) packet;
                Jid to = presence.getTo();//接收方
                FriendRequest friendRequest = null;
                // 未经处理的好友请求

                Log.i("TAG", "Type = " + presence.getType());

                if (presence.getType().equals(Presence.Type.unsubscribed)) {
                    Log.i("TAG", "Type.UNsubscribe");
                    friendRequest = new FriendRequest(to.toString(), FriendRequest.Type.unsubscribed);
                    // 处理好友添加请求
                    EventBus.getDefault().post(friendRequest);
                }
            }
        }
    }
}
