package cn.dazhou.im.modle;

import java.io.UnsupportedEncodingException;

/**
 * Created by hooyee on 2017/5/8.
 */

public class ChatMsgEntity{
    private int image;//头像
    private String date;//消息日期
    private String message;//消息内容
    private byte[] mesImage;//发送图片
    private boolean isComMeg = true;// 是否为收到的消息
    private byte[] msgSoundRecord;
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

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }

    public byte[] getMsgSoundRecord() {
        return msgSoundRecord;
    }

    public void setMsgSoundRecord(byte[] msgSoundRecord) {
        this.msgSoundRecord = msgSoundRecord;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity( String date, String text,int im, boolean isComMsg,byte[] mesImage) {
        super();
        this.image = im;
        this.date = date;
        this.message = text;
        this.isComMeg = isComMsg;
        this.mesImage=mesImage;
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