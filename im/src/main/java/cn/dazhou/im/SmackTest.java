package cn.dazhou.im;


import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.impl.LocalAndDomainpartJid;
import org.jxmpp.jid.impl.LocalDomainAndResourcepartJid;
import org.jxmpp.jid.parts.Domainpart;
import org.jxmpp.jid.parts.Localpart;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by hooyee on 2017/5/4.
 */

public class SmackTest {

    public void connect() throws IOException, InterruptedException, XMPPException, SmackException {
        Log.i("TAG", "connect");
        XMPPTCPConnectionConfiguration config = null;
        InetAddress inetAddress = InetAddress.getByName("192.168.1.39");
        try {
            config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("hooyee", "hooyee")
                    .setXmppDomain(JidCreate.from("192.168.1.39").asDomainBareJid())
                    .setHostAddress(inetAddress)
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AbstractXMPPConnection conn1 = new XMPPTCPConnection(config);
        conn1.setReplyTimeout(60000);
        conn1.setPacketReplyTimeout(60000);
        conn1.connect();
        conn1.login("hooyee", "hooyee");
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus("I am online");
        conn1.sendStanza(presence);
        Message msg = new Message();
        msg.setTo("admin@192.168.1.39");
        msg.setBody("hooyee给admin发消息1112=====");
        // 自定义扩展message

        conn1.sendStanza(msg);

//        ChatManager.getInstanceFor(conn1).chatWith(new LocalAndDomainpartJid(Localpart.from("admin"), Domainpart.from("cn.hooyee")).asEntityBareJid());
    }
}
