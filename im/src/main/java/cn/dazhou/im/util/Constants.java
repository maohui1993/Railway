package cn.dazhou.im.util;

import android.os.Environment;

/**
 * Created by hooyee on 2017/5/10.
 */

public class Constants {
    public static final String TAG = "railway";
    /** 0x001-接受消息  0x002-发送消息**/
    public static final int CHAT_ITEM_TYPE_LEFT = 0x001;
    public static final int CHAT_ITEM_TYPE_RIGHT = 0x002;
    /** 0x003-发送中  0x004-发送失败  0x005-发送成功**/
    public static final int CHAT_ITEM_SENDING = 0x003;
    public static final int CHAT_ITEM_SEND_ERROR = 0x004;
    public static final int CHAT_ITEM_SEND_SUCCESS = 0x005;
    public static final int RESULT_LOAD_IMAGE = 200;

    public static final String JID_SEPARATOR = "@";

    public static final String MEDIA_PATH = Environment.getExternalStorageDirectory() + "/railway/mediafile/";
}
