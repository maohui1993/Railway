package cn.dazhou.im.entity;

/**
 * Created by hooyee on 2017/5/8.
 * 聊天的数据
 */

public class ChatMessageEntity {
    private int id;
    private String imagePath;      // 图片消息，记录图片的位置
    private String voicePath;      // 语音消息，记录语音的位置
    private String content;        // 普通文本消息
    private String fromJid;        // 来自谁的聊天信息
    private String toJid;          // 发送给谁的聊天信息
    private boolean state;         // 是否已读
    private int type;              // 是发送还是接收消息
    private long date;             // 消息日期
    private byte[] imageBytes;     // 图片的字节信息，传输时使用
    private byte[] voiceBytes;     // 语音的字节信息，传输时使用
    private long voiceTime;
    private String jid;            // 当前用户为发送方，则记录接收方jid，为接收方则记录发送方jid
    private String roomJid;        // 如果为群聊，记录群的Jid
    private String filePath;       // 传送的文件的path
    private int sendState;
    private Type dataType;         // 数据类型
    private int fileProcess = 100;       // 文件传输进度

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(String imagePath, String voicePath, String content, String fromJid, String toJid, boolean state, int type, long date,
                             byte[] imageBytes, byte[] voiceBytes, long voiceTime, String jid, String roomJid, String fileUri, int sendState, Type dataType) {
        this.imagePath = imagePath;
        this.voicePath = voicePath;
        this.content = content;
        this.fromJid = fromJid;
        this.toJid = toJid;
        this.state = state;
        this.type = type;
        this.date = date;
        this.imageBytes = imageBytes;
        this.voiceBytes = voiceBytes;
        this.voiceTime = voiceTime;
        this.jid = jid;
        this.roomJid = roomJid;
        this.filePath = fileUri;
        this.sendState = sendState;
        this.dataType = dataType;
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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
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

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public byte[] getVoiceBytes() {
        return voiceBytes;
    }

    public void setVoiceBytes(byte[] voiceBytes) {
        this.voiceBytes = voiceBytes;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getRoomJid() {
        return roomJid;
    }

    public void setRoomJid(String roomJid) {
        this.roomJid = roomJid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String fileUri) {
        this.filePath = fileUri;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public int getFileProcess() {
        return fileProcess;
    }

    public void setFileProcess(int fileProcess) {
        this.fileProcess = fileProcess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChatMessageEntity) {
            ChatMessageEntity e = (ChatMessageEntity) obj;
//            return filePath.equals(e.getFilePath());
            return date == e.getDate()
                    && type == e.getType()
                    && dataType == e.getDataType();
        }
        return false;
    }

    public static class Builder {
        private String imagePath;      // 图片消息，记录图片的位置
        private String voicePath;      // 语音消息，记录语音的位置
        private String content;        // 普通文本消息
        private String fromJid;        // 来自谁的聊天信息
        private String toJid;          // 发送给谁的聊天信息
        private boolean state;         // 是否已读
        private int type;              // 是发送还是接收消息
        private long date;             // 消息日期
        private byte[] imageBytes;     // 图片的字节信息，传输时使用
        private byte[] voiceBytes;     // 语音的字节信息，传输时使用
        private long voiceTime;
        private String jid;            // 当前用户为发送方，则记录接收方jid，为接收方则记录发送方jid
        private String roomJid;        // 如果为群聊，记录群的Jid
        private String filePath;           // 传送的文件的Uri
        private int sendState;
        private Type dataType = Type.text;         // 数据类型

        public ChatMessageEntity build() {
            return new ChatMessageEntity(imagePath, voicePath, content, fromJid, toJid, state, type, date
                    , imageBytes, voiceBytes, voiceTime, jid, roomJid, filePath, sendState, dataType);
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

        public Builder state(boolean state) {
            this.state = state;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder date(long date) {
            this.date = date;
            return this;
        }

        public Builder imageBytes(byte[] imageBytes) {
            this.imageBytes = imageBytes;
            return this;
        }

        public Builder voiceBytes(byte[] voiceBytes) {
            this.voiceBytes = voiceBytes;
            return this;
        }

        public Builder voiceTime(long voiceTime) {
            this.voiceTime = voiceTime;
            return this;
        }

        public Builder jid(String jid) {
            this.jid = jid;
            return this;
        }

        public Builder roomJid(String roomJid) {
            this.roomJid = roomJid;
            return this;
        }

        public Builder filePath(String fileUri) {
            this.filePath = fileUri;
            return this;
        }

        public Builder sendState(int sendState) {
            this.sendState = sendState;
            return this;
        }

        public Builder dataType(Type dataType) {
            this.dataType = dataType;
            return this;
        }
    }

    public enum Type {
        file,
        text,
        voice,
        picture,
        video
    }
}