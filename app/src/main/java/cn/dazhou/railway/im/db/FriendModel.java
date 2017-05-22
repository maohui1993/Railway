package cn.dazhou.railway.im.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;

/**
 * Created by hooyee on 2017/5/17.
 */

@Table(database = RailwayDatabase.class)
public class FriendModel extends BaseModel{
    @PrimaryKey
    @Column
    private String jid;         // 好友账号@所属人账号<好友username@所属人username>
    @Column
    private String rawJid;      // 好友jid<username@hostname>
    @Column
    private String name;        // 好友名称
    @Column
    private String chatMessageJid;
    @Column
    @ForeignKey(tableClass = UserModel.class,
            references = {@ForeignKeyReference(columnName = "possessor", foreignKeyColumnName = "username")})
    private String possessor;   // 好友的持有者（持有人账号）

    List<ChatMessageModel> chatMessages;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "chatMessages")
    public List<ChatMessageModel> getMyChatMessages() {
        if (chatMessages == null || chatMessages.isEmpty()) {
            chatMessages = SQLite.select()
                    .from(ChatMessageModel.class)
                    .where(ChatMessageModel_Table.jid.eq(jid))
                    .queryList();
        }
        return chatMessages;
    }

    public String getPossessor() {
        return possessor;
    }

    public void setPossessor(String possessor) {
        this.possessor = possessor;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        if (jid.contains(Constants.JID_SEPARATOR)) {
            jid = jid.split(Constants.JID_SEPARATOR)[0];
        }
        this.jid = jid + Constants.JID_SEPARATOR + MyApp.gCurrentUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatMessageJid() {
        return chatMessageJid;
    }

    public void setChatMessageJid(String chatMessageJid) {
        this.chatMessageJid = chatMessageJid;
    }

    public String getRawJid() {
        return rawJid;
    }

    public void setRawJid(String rawJid) {
        this.rawJid = rawJid;
    }
}
