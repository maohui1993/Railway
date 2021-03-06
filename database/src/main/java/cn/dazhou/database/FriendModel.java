package cn.dazhou.database;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import cn.dazhou.database.util.PingyinUtil;

/**
 * Created by hooyee on 2017/5/17.
 */

@Table(database = RailwayDatabase.class)
public class FriendModel extends BaseModel implements Comparable<FriendModel> {
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
    @Column
    private String tel;         // 手机号码
    @Column
    private String nickName;    // 好友备注

    private int onceMaxShown = 5;

    @Column
    private int notReadCount;
    @Column
    private boolean inMessageList;
    @Column
    private long lastChatTime;

    List<ChatMessageModel> chatMessages;

    private String pingyin;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "chatMessages")
    public List<ChatMessageModel> getMyChatMessages() {
        int count = (int) SQLite.selectCountOf(ChatMessageModel_Table.id)
                .from(ChatMessageModel.class)
                .where(ChatMessageModel_Table.jid.eq(jid))
                .count();
        if (chatMessages == null || chatMessages.isEmpty()) {
            chatMessages = SQLite.select()
                    .from(ChatMessageModel.class)
                    .where(ChatMessageModel_Table.jid.eq(jid))
                    .limit(onceMaxShown)
                    .offset(count - onceMaxShown)
                    .queryList();
        }
        return chatMessages;
    }

    public synchronized List<ChatMessageModel> getMyChatMessages(int page, int initPosition) {
        int count = (int) SQLite.selectCountOf(ChatMessageModel_Table.id)
                .from(ChatMessageModel.class)
                .where(ChatMessageModel_Table.jid.eq(jid))
                .count()
                - initPosition;
        // 避免查找出重复数据
        int limitCount = onceMaxShown;
        int offset = count - onceMaxShown * page;
        int surplusCount = offset + onceMaxShown;
        if (offset < 0) {
            if (surplusCount > 0) {
                limitCount = surplusCount;
            } else {
                return null;
            }
        }
        Log.i("TAG", "第几次page = " + page + "第几次count = " + count);
        chatMessages = SQLite.select()
                .from(ChatMessageModel.class)
                .where(ChatMessageModel_Table.jid.eq(jid))
                .limit(limitCount)
                .offset(offset)
                .queryList();
        return chatMessages;
    }

    public void notReadCountAutoAddOne() {
        notReadCount += 1;
    }

    public ChatMessageModel getLatestChatMessage() {
        if (chatMessages == null) {
            getMyChatMessages();
        }
        if (chatMessages.size() == 0) {
            return null;
        }
        return chatMessages.get(chatMessages.size() - 1);
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

    /**
     * 添加默认值(避免不必要的空指针问题)
     *
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getNotReadCount() {
        return notReadCount;
    }

    public void setNotReadCount(int notReadCount) {
        this.notReadCount = notReadCount;
    }

    public boolean isInMessageList() {
        return inMessageList;
    }

    public void setInMessageList(boolean inMessageList) {
        this.inMessageList = inMessageList;
    }

    public long getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(long lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public String getPingyin() {
        if (pingyin == null) {
            pingyin = PingyinUtil.cn2Spell(name);
        }
        return pingyin;
    }

    public static int typeToInt(String type) {
        switch (type) {
            case "remove":
                return -1;
            case "none":
                return 0;
            case "to":
                return 1;
            case "from":
                return 2;
            case "both":
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
        return this.getPingyin().compareTo(o.getPingyin());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FriendModel) {
            FriendModel obj1 = (FriendModel) obj;
            return jid.equals(obj1.getJid());
        } else {
            return false;
        }
    }
}
