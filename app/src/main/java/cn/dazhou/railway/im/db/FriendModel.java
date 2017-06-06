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
public class FriendModel extends BaseModel implements Comparable<FriendModel>{
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
    private int relation;       // remove:-1 ; none:0 ; to:1 ; from:2 ; both:3
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

    /**
     * 添加默认值(避免不必要的空指针问题)
     * @return
     */
    public String getName() {
        if (name == null) {
            return " ";
        }
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

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public static int typeToInt(String type) {
        switch (type) {
            case "remove" :
                return -1;
            case "none" :
                return 0;
            case "to" :
                return 1;
            case "from" :
                return 2;
            case "both" :
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public int compareTo(FriendModel o) {
        if (o == null || o.getName() == null) {
            return 1;
        }
        if (getName() == null) {
            return -1;
        }
        return this.getName().toLowerCase().compareTo(o.getName());
    }
}
