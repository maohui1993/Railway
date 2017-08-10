package cn.dazhou.railway.config;

import android.os.Environment;

/**
 * Created by hooyee on 2017/5/11.
 */
public final class Constants {
    public static final String LOG_TAG_HTTP = "http";
    public static final String SERVER_IP = "server_ip";
    public static final String SERVER_PORT = "server_port";
    public static final String SERVER_CONNECT_TIMEOUT = "server_timeout";
    public static final int SERVER_CONNECT_TIMEOUT_DEFAULT = 1000 * 5;
    public static final int SERVER_PORT_DEFAULT = 5222;
    public static final String SERVER_IP_DEFAULT = "192.168.1.39";
    public static final String JID_SEPARATOR = "@";
    public static final String DATA_KEY = "jid";
    public static final String NOTIFICATION_ACTION_TYPE = "notificationActionType";
    public static final String NOTIFICATION_ID_KEY = "notificationID";
    public static final int NOTIFICATION_ID_VALUE_ONE = 0x01;
    public static final int NOTIFICATION_ACTION_TYPE_ACCEPT = 0x01;
    public static final int NOTIFICATION_ACTION_TYPE_REFUSE = 0x02;

    public static final String LOGIN_SUCCESS_BROADCAST = "action.login.success";
    public static final String UPDATE_FROM_SERVER_BROADCAST = "action.friend.refresh";
    public static final String NEW_REQUEST_BROADCAST = "action.friend.new.request";

    public static final String LATEST_LOGIN_JID = "latestLoginJid";

    public static final String ACTION_RESPOND_VIA_MESSAGE =
            "android.intent.action.RESPOND_VIA_MESSAGE";

    public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/railway/file/";
    public static final String FILE_NAME = "railway.log";
}
