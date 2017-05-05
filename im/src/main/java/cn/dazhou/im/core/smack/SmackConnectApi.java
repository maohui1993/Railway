package cn.dazhou.im.core.smack;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.impl.JidCreate;

import java.net.InetAddress;

import cn.dazhou.im.core.IConnection;

/**
 * Created by hooyee on 2017/5/5.
 */

public class SmackConnectApi implements IConnection {

    private AbstractXMPPConnection mConnection;
    private static SmackConnectApi singleton = new SmackConnectApi();

    private SmackConnectApi(){}

    public static SmackConnectApi getInstance() {
        return singleton;
    }

    @Override
    public IConnection connect(String ip) throws Exception {
        XMPPTCPConnectionConfiguration config = null;
        InetAddress inetAddress = InetAddress.getByName(ip);
        try {
            config = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(JidCreate.from("192.168.1.39").asDomainBareJid())
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
        try {
            mConnection.login(username, password);
        } catch (Exception e) {
            throw e;
        }
    }
}
