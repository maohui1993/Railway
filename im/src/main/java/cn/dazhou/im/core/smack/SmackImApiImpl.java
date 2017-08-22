package cn.dazhou.im.core.smack;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.PresenceEventListener;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.roster.packet.RosterPacket;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.FileUtils;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.dazhou.im.core.IMApi;
import cn.dazhou.im.core.function.IConnection;
import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.im.entity.FriendRequest;
import cn.dazhou.im.entity.ProcessEvent;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.im.entity.UserExtensionElement;
import cn.dazhou.im.util.Constants;
import cn.dazhou.im.util.JsonUtil;
import cn.dazhou.im.util.OfflineMsgManager;

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

    private boolean connected;
    private byte mState;
    private Chat mChat;
    private Roster mRoster;

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
    public IConnection connect(String ip, int port, int timeout) throws Exception {
        if (connected) {
            return this;
        }
        XMPPTCPConnectionConfiguration config;
        InetAddress inetAddress = InetAddress.getByName(ip);
        config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(JidCreate.from(ip).asDomainBareJid())
                .setHostAddress(inetAddress)
                .setPort(port)
                .setConnectTimeout(timeout)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setSendPresence(false)   // 处理离线消息
                .setResource("smack")
                .build();
        mConnection = new XMPPTCPConnection(config);
        mConnection.connect();
        ReconnectionManager manager = ReconnectionManager.getInstanceFor(mConnection);
        manager.enableAutomaticReconnection();
        connected = true;

        mRoster = Roster.getInstanceFor(mConnection);

        mRoster.addPresenceEventListener(new PresenceEventListener() {
            @Override
            public void presenceAvailable(FullJid address, Presence availablePresence) {

            }

            @Override
            public void presenceUnavailable(FullJid address, Presence presence) {

            }

            @Override
            public void presenceError(Jid address, Presence errorPresence) {

            }

            @Override
            public void presenceSubscribed(BareJid address, Presence subscribedPresence) {
                Log.i("TAG", "添加成功");
                if (mRoster.getEntry(address).getType() != RosterPacket.ItemType.both) {
                    EventBus.getDefault().post(new FriendRequest.RequestResult(address.toString().split("@")[0], FriendRequest.Result.ACCEPT));
                }
            }

            @Override
            public void presenceUnsubscribed(BareJid address, Presence unsubscribedPresence) {
                Log.i("TAG", "被拒绝了");
                try {
                    EventBus.getDefault().post(new FriendRequest.RequestResult(address.toString().split("@")[0], FriendRequest.Result.REJECT));
                    mRoster.removeEntry(mRoster.getEntry(address));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mRoster.addSubscribeListener(new SubscribeListener() {
            @Override
            public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
                FriendRequest friendRequest = new FriendRequest(subscribeRequest.getFrom().toString(), FriendRequest.Type.subscribe);
                // 处理好友添加请求
                /**
                 * @see 处理方法参见cn.dazhou.railway.im.service.IMFriendRequestService#handleFriendRequest
                 */
                if(mRoster.getEntry(from.asBareJid()) == null
                        || mRoster.getEntry(from.asBareJid()).getType() == RosterPacket.ItemType.none) {
                    // 满足此条件表示 该好友请求为对方发来，己方为接收方
                    EventBus.getDefault().post(friendRequest);
                } else {
                    // 满足此条件，表明己方为好友关系的首次请求方。
                    // 对方确认添加己方为好友后，会申请添加己为好友，应当直接予以通过
                    sendSubscription(from.toString(), Presence.Type.subscribed);
                }
                return null;
            }
        });
        return this;
    }

    @Override
    public void disconnect() {
        mConnection.disconnect();
        connected = false;
        mState = NOT_LOGIN_STATE;
    }

    public void login(String username, String password) throws Exception {
        if (mState == LOGINED_STATE) {
            return;
        }
        if (!connected) {
            throw new Exception("未能链接服务器");
        }
        try {
            mConnection.login(username, password);
            mChatManager = ChatManager.getInstanceFor(mConnection);
            mState = LOGINED_STATE;
            // 处理离线消息
            OfflineMsgManager.getInstance(mContext).dealOfflineMsg(mConnection);
        } catch (Exception e) {
            mState = NOT_LOGIN_STATE;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            Log.i("login-test", "login fail : " + sw.toString());
            disconnect();
            throw e;
        }
    }

    @Override
    public void chatWith(String jid, ChatMessageEntity msg) throws Exception {
        EntityBareJid id = JidCreate.entityBareFrom(jid);
        switch (msg.getDataType()) {
            case text:
            case voice:
            case picture:
                mChat = mChatManager.chatWith(id);
                String msgJson = JsonUtil.toJSON(msg);
                mChat.send(msgJson);
//                Message stanza = new Message();
//                stanza.setBody(msgJson);
//                stanza.setType(Message.Type.normal);
//                mChat.send(stanza);
                break;
            case video:
                mChat = mChatManager.chatWith(id);
                Presence presence = mRoster.getPresence(JidCreate.bareFrom(jid));
                if (presence.isAvailable()) {
                    sendFile(jid, msg.getFilePath());
                } else {
                    String path = msg.getFilePath();
                    msg.setFileContent(FileUtils.readFile(new File(path)));
                    mChat.send(JsonUtil.toJSON(msg));
                }
                break;
            case file:
                sendFile(jid, msg.getFilePath());
                break;
            default:
                break;
        }

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
        String msgJson = JsonUtil.toJSON(msg);
        mChat.send(msgJson);
    }

    @Override
    public void saveVCard(ExtraInfo info) throws Exception {
        VCard vCard = new VCard();
        vCard.setPhoneWork(info.getTelKey(), info.getTel());
        vCard.setNickName(info.getName());
        VCardManager.getInstanceFor(mConnection).saveVCard(vCard);
    }

    @Override
    public ExtraInfo getVCard(String jid) {
        ExtraInfo info = null;
        try {
            VCard vCard = VCardManager.getInstanceFor(mConnection).loadVCard(JidCreate.entityBareFrom(jid));
            info = new ExtraInfo();
            info.setName(vCard.getNickName());
            info.setTel(vCard.getPhoneWork(info.getTelKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    public boolean isConnected() {
        if (mConnection == null) {
            return false;
        }
        return mConnection.isConnected();
    }

    private void sendFile(String jid, String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        FileTransferManager transfer = FileTransferManager.getInstanceFor(getConnection());
        System.out.println("发送文件给: " + jid);
        OutgoingFileTransfer out = null;
        try {
            Presence p = Roster.getInstanceFor(mConnection).getPresence(JidCreate.bareFrom(jid));
            if (p == null) {
                Log.w("TAG", "SmackImApiImpl#sendFile:用户不存在");
                return;
            }
            Jid toUser = p.getFrom();//提取完整的用户名称
            out = transfer.createOutgoingFileTransfer(toUser.asEntityFullJidIfPossible());
            out.sendFile(file, file.getName());
            transferFileTask(out, filePath);

        } catch (SmackException e) {
            Log.i("TAG", "SmackImApiImpl#sendFile: 文件发送失败");
            throw e;
        }
    }

    private void transferFileTask(final OutgoingFileTransfer out, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = -1;
                ProcessEvent event = new ProcessEvent(filePath);
                event.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
                while (!out.isDone()) {
                    if (out.getStatus().equals(FileTransfer.Status.error)) {
                        Log.w("TAG", "文件传输失败：" + out.getError());
                    } else {
                        double progress = out.getProgress();
                        if (progress > 0 && startTime == -1) {
                            startTime = System.currentTimeMillis();
                        }
                        progress *= 100;
                        event.setProcess((int) progress);
                        EventBus.getDefault().post(event);
                        Log.i("TAG", "status = " + out.getStatus());
                        Log.i("TAG", "progress = " + progress + "%");
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                event.setProcess(100);
                EventBus.getDefault().post(event);
            }
        }).start();
    }

    @Override
    public Roster getRoster() {
        return Roster.getInstanceFor(mConnection);
    }

    @Override
    public Roster addFriend(String jid) {
        if (mConnection == null) return null;
        try {
//            mRoster.sendSubscriptionRequest(JidCreate.entityBareFrom(jid));
            mRoster.createEntry(JidCreate.entityBareFrom(jid), jid, null);
        } catch (Exception e) {
            Log.e("TAG", "SmackImApiImpl.class: " + "好友添加失败");
            Log.e("TAG", "SmackImApiImpl.class: " + e.getMessage());
        }
        return mRoster;
    }

    public void removeFriend(String jid) {

    }

    public Roster acceptFriendRequest(String jid) {
        try {
            sendSubscription(jid, Presence.Type.subscribed);
            mRoster.createEntry(JidCreate.entityBareFrom(jid), jid, null);
            return mRoster;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendSubscription(String jid, Presence.Type type) {
        try {
            Presence presenceRes = new Presence(type);
            presenceRes.setTo(JidCreate.entityBareFrom(jid));
            mConnection.sendStanza(presenceRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean rejectFriendRequest(String jid) {
        try {
            sendSubscription(jid, Presence.Type.unsubscribed);

            // 以下方法会确切删除数据库信息
            RosterEntry entry = mRoster.getEntry(JidCreate.entityBareFrom(jid));
            mRoster.removeEntry(entry);
            return true;
        } catch (Exception e) {
            Log.e("TAG", "SmackImApiImpl.class: " + "好友删除失败");
            Log.e("TAG", "SmackImApiImpl.class: " + e.getMessage());
        }
        return false;
    }

    private DomainBareJid getTargetServiceJid(String target, List<DomainBareJid> domainBareJids) {
        for (DomainBareJid domainBareJid : domainBareJids) {
            if (domainBareJid.toString().contains(target)) {
                return domainBareJid;
            }
        }
        return null;
    }

    @Override
    public List<UserBean> searchUserFromServer(String username) {
        try {
            List<UserBean> users = new ArrayList();
            UserSearchManager search = new UserSearchManager(mConnection);
            List<DomainBareJid> domainBareJids = search.getSearchServices();
            DomainBareJid targetJid = getTargetServiceJid("search", domainBareJids);
            Form searchForm = search.getSearchForm(targetJid);

            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);
            answerForm.setAnswer("search", username);
            ReportedData data = search.getSearchResults(answerForm, targetJid);
            for (ReportedData.Row row : data.getRows()) {
                Log.i("TAG", "SmackImApiImpl.class: " + "username = " + row.getValues("Username").toString());
                UserBean user = new UserBean();
                user.setUsername(row.getValues("Username").get(0));
                user.setName(row.getValues("Name").get(0));
                user.setEmail(row.getValues("Email").get(0));
                users.add(user);
            }
            Log.i("TAG", " mConnect: " + mConnection);
            return users;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            Log.i("TAG", "SmackImApiImpl.class : " + e.getMessage());
            Log.i("TAG", "SmackImApiImpl.class mConnect: " + mConnection);
            Log.i("TAG", "SmackImApiImpl.class : " + sw.toString());
        }
        return null;
    }

    @Override
    public MultiUserChat createChatRoom(final String roomName, final String nickName, final String password) {
        getHostRooms();
        if (!mConnection.isConnected()) {
            Log.i("TAG", "连接已断开");
        }
        MultiUserChat muc = null;

        try {
            // 创建一个MultiUserChat
            muc = MultiUserChatManager.getInstanceFor(mConnection).getMultiUserChat(JidCreate.entityBareFrom(roomName + "@conference." + mConnection.getServiceName().toString()));
            // 创建聊天室
            MultiUserChat.MucCreateConfigFormHandle mucCreateConfigFormHandle = muc.createOrJoin(Resourcepart.from(nickName));
            if (mucCreateConfigFormHandle != null) {
                // 获得聊天室的配置表单
                Form form = muc.getConfigurationForm();
                // 根据原始表单创建一个要提交的新表单。
                Form submitForm = form.createAnswerForm();
                // 向要提交的表单添加默认答复
                List<FormField> fields = form.getFields();
                for (int i = 0; fields != null && i < fields.size(); i++) {
                    if (FormField.Type.hidden != fields.get(i).getType() &&
                            fields.get(i).getVariable() != null) {
                        // 设置默认值作为答复
                        submitForm.setDefaultAnswer(fields.get(i).getVariable());
                    }
                }

                submitForm.setAnswer("muc#roomconfig_moderatedroom", true);
                submitForm.setAnswer("muc#roomconfig_whois", Arrays.asList("moderators"));
                // 设置聊天室的新拥有者
                List owners = new ArrayList();
                owners.add(mConnection.getUser().toString());// 用户JID
                submitForm.setAnswer("muc#roomconfig_roomowners", owners);
//                // 设置聊天室是持久聊天室，即将要被保存下来
                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
//                // 房间仅对成员开放
                submitForm.setAnswer("muc#roomconfig_membersonly", false);
//                // 允许占有者邀请其他人
                submitForm.setAnswer("muc#roomconfig_allowinvites", true);
                submitForm.setAnswer("muc#roomconfig_enablelogging", true);

                if (password != null && password.length() != 0) {
                    // 进入是否需要密码
                    submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
                    // 设置进入密码
                    submitForm.setAnswer("muc#roomconfig_roomsecret", password);
                }
                // 能够发现占有者真实 JID 的角色
                // submitForm.setAnswer("muc#roomconfig_whois", "anyone");
                // 登录房间对话
                submitForm.setAnswer("muc#roomconfig_enablelogging", true);
                // 仅允许注册的昵称登录
                submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
                // 允许使用者修改昵称
                submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
                // 允许用户注册房间
                submitForm.setAnswer("x-muc#roomconfig_registration", false);
                // 发送已完成的表单（有默认值）到服务器来配置聊天室
                muc.sendConfigurationForm(submitForm);
            }
        } catch (Exception e) {
            Log.i("TAG", "测试：" + mConnection);
            e.printStackTrace();
        }
        return muc;
    }

    /**
     * @param roomName 要加入的房间
     * @param jid      被邀请者的Jid
     */
    @Override
    public void inviteUser(String roomName, String jid) {
        try {
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(mConnection).getMultiUserChat(JidCreate.entityBareFrom(roomName + "@conference." + mConnection.getServiceName().toString()));

//            muc.invite(JidCreate.entityBareFrom(jid), "He is our friends");
            boolean b = muc.isJoined();
            if (!b) {
//                muc.join(Resourcepart.from(roomName));
            }
            List s1 = muc.getAdmins();
//            List s = muc.getMembers();
//            muc.grantAdmin(JidCreate.entityBareFrom(jid));
//            muc.grantMembership(JidCreate.entityBareFrom(jid));

            List l = muc.getMembers();
            List m = muc.getOccupants();
            //    List out = muc.getOutcasts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MultiUserChat joinChatRoom(String roomName, String nickName, String password) {
        if (!mConnection.isConnected()) {
            throw new NullPointerException("服务器连接失败，请先连接服务器");
        }
        try {
            // 使用XMPPConnection创建一个MultiUserChat窗口
            MultiUserChat muc = MultiUserChatManager.getInstanceFor(mConnection).
                    getMultiUserChat(JidCreate.entityBareFrom(roomName + "@conference." + mConnection.getServiceName().toString()));
            // history.setSince(new Date());
            // 用户加入聊天室
            muc.join(Resourcepart.from(nickName), password);
            getHostRooms();
            Message msg = new Message();

            msg.addExtension(new UserExtensionElement());
            msg.setBody("测试2");

            muc.addMessageListener(new MessageListener() {
                @Override
                public void processMessage(Message message) {
                    Log.i("TAG", "收到的  群聊消息 ： " + message.getBody());
                    Log.i("TAG", "收到的  群聊消息from ： " + message.getFrom());
                    Log.i("TAG", "收到的  群聊消息to ： " + message.getTo());
                    if (message.hasExtension(UserExtensionElement.NAME_SPACE)) {
                        Log.i("TAG", "收到的  群聊消息extension ： " + message.getExtension(UserExtensionElement.NAME_SPACE).getElementName());
                    }
                }
            });
            muc.sendMessage(msg);
            return muc;
        } catch (XMPPException | SmackException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化会议室列表 <br>
     * 该列表数据为会议服务列表，一个会议服务包含N个会议室
     */
    public List<HostedRoom> getHostRooms() {
        if (getConnection() == null)
            return null;
        Collection<HostedRoom> hostrooms = null;
        List<HostedRoom> roomInfos = new ArrayList();
        MultiUserChatManager mucManager = null;
        // 创建一个MultiUserChat
        mucManager = MultiUserChatManager.getInstanceFor(mConnection);
        Log.i("TAG", "加入的chatroom.size = " + mucManager.getJoinedRooms().size());
        return roomInfos;
    }

}
