package cn.dazhou.im.modle;


/**
 * Created by hooyee on 2017/5/8.
 */

public class ChatMsgEntity{
    private int image;//头像
    private String date;//消息日期
    private String message;//消息内容
    private byte[] mesImage;//发送图片
    private byte[] msgSoundRecord;
    private String time;
    private int sendState;
    private int type;

    public int getImage() {
        return image;
    }

    public void setImage(int im) {
        this.image=im;
    }

    public String getDate() {
        return date;
    }

    public void setMesImage(byte[] image){
        mesImage=image;
    }

    public byte[] getMesImage(){
        return mesImage;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getMsgSoundRecord() {
        return msgSoundRecord;
    }

    public void setMsgSoundRecord(byte[] msgSoundRecord) {
        this.msgSoundRecord = msgSoundRecord;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //    @Override
//    public String toString() {
//        String imageStr = "";
//        try {
//            imageStr = new String(mesImage, "GB2312");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "image=" + image + "," + "date=" + date + "," + "message=" + message + "," + "mesImage=" + imageStr;
//    }
}