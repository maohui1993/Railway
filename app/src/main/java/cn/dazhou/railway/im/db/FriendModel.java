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

import cn.dazhou.railway.im.db.ChatMessageModel;
import cn.dazhou.railway.im.db.RailwayDatabase;

/**
 * Created by hooyee on 2017/5/17.
 */

@Table(database = RailwayDatabase.class)
public class FriendModel extends BaseModel{
    @PrimaryKey
    @Column
    private long id;
    @Column
    private String possessor;   // 好友的持有者（持有人账号）
    @PrimaryKey
    @Column
    private String jid;         // 好友账号
    @Column
    private String name;        // 好友名称
    @Column
    private String chatMessageJid;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        this.jid = jid;
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
}
