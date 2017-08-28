package cn.dazhou.im.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hooyee on 2017/5/8.
 * 聊天的数据
 */

public class ChatMessageEntity implements Parcelable{
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
    private String filePath = "";       // 传送的文件的path
    private int sendState;
    private Type dataType;         // 数据类型
    private int fileProcess = 100;       // 文件传输进度
    private byte[] fileContent;    // 文件内容

    public ChatMessageEntity() {
    }

    public ChatMessageEntity(String imagePath, String voicePath, String content, String fromJid, String toJid, boolean state, int type, long date,
                             byte[] imageBytes, byte[] voiceBytes, long voiceTime, String jid, String roomJid, String fileUri, int sendState, Type dataType, byte[] fileContent) {
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
        this.fileContent = fileContent;
    }

    protected ChatMessageEntity(Parcel in) {
        id = in.readInt();
        imagePath = in.readString();
        voicePath = in.readString();
        content = in.readString();
        fromJid = in.readString();
        toJid = in.readString();
        state = in.readByte() != 0;
        type = in.readInt();
        date = in.readLong();
        imageBytes = in.createByteArray();
        voiceBytes = in.createByteArray();
        voiceTime = in.readLong();
        jid = in.readString();
        roomJid = in.readString();
        filePath = in.readString();
        sendState = in.readInt();
        fileProcess = in.readInt();
        fileContent = in.createByteArray();
    }

    public static final Creator<ChatMessageEntity> CREATOR = new Creator<ChatMessageEntity>() {
        @Override
        public ChatMessageEntity createFromParcel(Parcel in) {
            return new ChatMessageEntity(in);
        }

        @Override
        public ChatMessageEntity[] newArray(int size) {
            return new ChatMessageEntity[size];
        }
    };

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
        fileUri = fileUri == null ? "" : fileUri;
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

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChatMessageEntity) {
            ChatMessageEntity e = (ChatMessageEntity) obj;
            return filePath.equals(e.getFilePath())
                    && type == e.getType();
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imagePath);
        dest.writeString(voicePath);
        dest.writeString(content);
        dest.writeString(fromJid);
        dest.writeString(toJid);
        dest.writeByte((byte) (state ? 1 : 0));
        dest.writeInt(type);
        dest.writeLong(date);
        dest.writeByteArray(imageBytes);
        dest.writeByteArray(voiceBytes);
        dest.writeLong(voiceTime);
        dest.writeString(jid);
        dest.writeString(roomJid);
        dest.writeString(filePath);
        dest.writeInt(sendState);
        dest.writeInt(fileProcess);
        dest.writeParcelable(dataType, flags);
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
        private String filePath = "";           // 传送的文件的Uri
        private int sendState;
        private Type dataType = Type.text;         // 数据类型
        private byte[] fileContent;

        public ChatMessageEntity build() {
            return new ChatMessageEntity(imagePath, voicePath, content, fromJid, toJid, state, type, date
                    , imageBytes, voiceBytes, voiceTime, jid, roomJid, filePath, sendState, dataType, fileContent);
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

        public Builder fileContent(byte[] fileContent) {
            this.fileContent = fileContent;
            return this;
        }
    }

    public enum Type implements Parcelable{
        file(0),
        text(1),
        voice(2),
        picture(3),
        video(4);

        private int value;

        Type(int v) {
            value = v;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(value);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Type> CREATOR = new Creator<Type>() {
            @Override
            public Type createFromParcel(Parcel in) {
                return Type.values()[in.readInt()];
            }

            @Override
            public Type[] newArray(int size) {
                return new Type[size];
            }
        };
    }
}