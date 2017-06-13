package cn.dazhou.im.entity;

/**
 * Created by hooyee on 2017/5/8.
 * 聊天的数据
 */

public class ChatMessageEntity {
    private String imagePath;      // 图片消息，记录图片的位置
    private String voicePath;      // 语音消息，记录语音的位置
    private String content;        // 普通文本消息
    private String fromJid;        // 来自谁的聊天信息
    private String toJid;          // 发送给谁的聊天信息
    private boolean state;         // 是否已读
    private int type;              // 是发送还是接收消息
    private long date;           // 消息日期
    private byte[] imageBytes;     // 图片的字节信息，传输时使用
    private byte[] voiceBytes;     // 语音的字节信息，传输时使用
    private long voiceTime;
    private String jid;            // 当前用户为发送方，则记录接收方jid，为接收方则记录发送方jid
    private String roomJid;        // 如果为群聊，记录群的Jid
    private int sendState;

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
}