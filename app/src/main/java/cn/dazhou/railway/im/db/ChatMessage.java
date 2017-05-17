package cn.dazhou.railway.im.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by hooyee on 2017/5/17.
 */

@Table(database = RailwayDatabase.class)
public class ChatMessage {
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String imagePath;  // 图片消息，记录图片的位置

    @Column
    String voicePath; // 语音消息，记录语音的位置

    @Column
    String content;  // 普通文本消息

    @Column
    String fromJid;  // 来自谁的聊天信息

    @Column
    String toJid; // 发送给谁的聊天信息

    private ChatMessage(long id, String imagePath, String voicePath, String content, String fromJid, String toJid) {
        this.id = id;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
        this.content = content;
        this.fromJid = fromJid;
        this.toJid = toJid;
    }

    public static class Builder {
        private long id;
        private String imagePath;
        private String voicePath;
        private String content;
        private String fromJid;
        private String toJid;

        public ChatMessage build() {
            return new ChatMessage(id, imagePath, voicePath, content, fromJid, toJid);
        }

        public Builder id(long id) {
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
    }
}
