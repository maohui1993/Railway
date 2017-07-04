package cn.dazhou.railway.im.db;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.im.entity.ChatMessageEntity;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.config.Constants;

/**
 * Created by hooyee on 2017/5/17.
 */

@Table(database = RailwayDatabase.class)
public class ChatMessageModel extends BaseModel{
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String imagePath; // 图片消息，记录图片的位置

    @Column
    String voicePath; // 语音消息，记录语音的位置

    @Column
    long voiceTime;   // 语音时长

    @Column
    String content;  // 普通文本消息

    @Column
    String fromJid;  // 来自谁的聊天信息

    @Column
    String toJid;   // 发送给谁的聊天信息

    @Column
    boolean state;  // 是否已读

    @Column
    int type;       // 是发送还是接收消息
    @Column
    long date;    // 消息日期

    @Column
    private ChatMessageEntity.Type dataType;         // 数据类型

    @Column
    private String filePath;


    @Column
    @ForeignKey(tableClass = FriendModel.class,
    references = {@ForeignKeyReference(columnName = "jid", foreignKeyColumnName = "jid")})
    String jid;    // 当前用户为发送方，则记录接收方jid，为接收方则记录发送方jid
//
//    @Column
//    @ForeignKey(tableClass = ChatRoomModel.class,
//            references = {@ForeignKeyReference(columnName = "roomJid", foreignKeyColumnName = "roomJid")})
    String roomJid;

    public ChatMessageModel() {
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public static List<ChatMessageEntity> toChatMessageEntity(List<ChatMessageModel> models) {
        if (models == null || models.size() == 0) {
            return null;
        }
        List<ChatMessageEntity> messages = new ArrayList<ChatMessageEntity>();
        Log.i("TAG", "models.size = " + models.size());
        for(ChatMessageModel model : models) {
            ChatMessageEntity message = new ChatMessageEntity();
            message.setImagePath(model.imagePath);
            message.setVoicePath(model.voicePath);
            message.setContent(model.content);
            message.setState(model.state);
            message.setDate(model.date);
            // model存储的为 聊天对象的jid + @ +自身的jid
            // 发送的消息应该只要包含接受方的jid
            message.setJid(model.jid.split(Constants.JID_SEPARATOR)[0]);
            message.setToJid(model.toJid);
            message.setFromJid(model.fromJid);
            message.setType(model.getType());
            message.setVoiceTime(model.voiceTime);
            message.setDataType(model.getDataType());
            message.setFilePath(model.getFilePath());
            messages.add(message);
        }
        return messages;
    }

    public static ChatMessageModel newInstances(ChatMessageEntity info) {
        return new Builder()
                .imagePath(info.getImagePath())
                .voicePath(info.getVoicePath())
                .voiceTime(info.getVoiceTime())
                .content(info.getContent())
                .state(info.isState())
                .date(info.getDate())
                .jid(info.getJid())
                .toJid(info.getToJid())
                .fromJid(info.getFromJid())
                .type(info.getType())
                .filePath(info.getFilePath())
                .dataType(info.getDataType())
                .build();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromJid() {
        return fromJid;
    }

    public void setFromJid(String fromJid) {
        this.fromJid = fromJid;
    }

    public String getToJid() {
        return toJid;
    }

    public void setToJid(String toJid) {
        this.toJid = toJid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        if (jid.contains(Constants.JID_SEPARATOR)) {
            jid = jid.split(Constants.JID_SEPARATOR)[0];
        }
        this.jid = jid + Constants.JID_SEPARATOR +  MyApp.gCurrentUsername;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getRoomJid() {
        return roomJid;
    }

    public void setRoomJid(String roomJid) {
        this.roomJid = roomJid;
    }

    public ChatMessageEntity.Type getDataType() {
        return dataType;
    }

    public void setDataType(ChatMessageEntity.Type dataType) {
        this.dataType = dataType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private ChatMessageModel(int id, String imagePath, String voicePath, String content, String fromJid, String toJid, String jid,
                             int type, long voiceTime, long date, ChatMessageEntity.Type dataType, String filePath) {
        this.id = id;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
        this.content = content;
        this.fromJid = fromJid;
        this.toJid = toJid;
        if (jid != null) {
            if (jid.contains(Constants.JID_SEPARATOR)) {
                jid = jid.split(Constants.JID_SEPARATOR)[0];
            }
            this.jid = jid + Constants.JID_SEPARATOR + MyApp.gCurrentUsername;
        }
        this.type = type;
        this.voiceTime = voiceTime;
        this.date = date;
        this.dataType = dataType;
        this.filePath = filePath;
    }

    public static class Builder {
        private int id;
        private String imagePath;
        private String voicePath;
        private String content;
        private String fromJid;
        private String toJid;
        private String jid;
        private int type;
        private long voiceTime;
        private long date;
        boolean state;  // 是否已读
        private ChatMessageEntity.Type dataType;
        private String filePath;

        public ChatMessageModel build() {
            return new ChatMessageModel(id, imagePath, voicePath, content, fromJid, toJid, jid, type, voiceTime, date, dataType, filePath);
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder voicePath(String voicePath) {
            this.voicePath = voicePath;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder fromJid(String fromJid) {
            this.fromJid = fromJid;
            return this;
        }

        public Builder toJid(String toJid) {
            this.toJid = toJid;
            return this;
        }

        public Builder jid(String jid) {
            if (jid == null) {
                return this;
            }
            if (jid.contains(Constants.JID_SEPARATOR)) {
                jid = jid.split(Constants.JID_SEPARATOR)[0];
            }
            this.jid = jid + Constants.JID_SEPARATOR +  MyApp.gCurrentUsername;
            return this;
        }

        public Builder state(boolean state) {
            this.state = state;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder voiceTime(long voiceTime) {
            this.voiceTime = voiceTime;
            return this;
        }

        public Builder date(long date) {
            this.date = date;
            return this;
        }

        public Builder dataType(ChatMessageEntity.Type type) {
            this.dataType = type;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }
    }
}
