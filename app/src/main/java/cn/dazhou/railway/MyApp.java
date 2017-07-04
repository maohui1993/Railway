package cn.dazhou.railway;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.login.LoginActivity;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/5/17.
 */

public class MyApp extends Application {
    public static UserModel gCurrentUser; // 当前用户
    public static String gCurrentUsername; // 当前账号
    // 初始化位置在LoginPresenter#login
    public static String gServerIp;
    public static int gServerPort;
    public static int gServerTimeout;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
        ZXingLibrary.initDisplayOpinion(this);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        String lastLogin = SharedPreferenceUtil.getString(this, Constants.LATEST_LOGIN_JID, "");

        MyApp.gServerIp = SharedPreferenceUtil.getString(this, Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT);
        MyApp.gServerPort = SharedPreferenceUtil.getInt(this, Constants.SERVER_PORT,  Constants.SERVER_PORT_DEFAULT);
        MyApp.gServerTimeout = SharedPreferenceUtil.getInt(this, Constants.SERVER_CONNECT_TIMEOUT,  Constants.SERVER_CONNECT_TIMEOUT_DEFAULT);

        if (!"".equals(lastLogin)) {
            gCurrentUsername = lastLogin;
            gCurrentUser = UserModel.getUser(lastLogin);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    IMUtil.login(MyApp.this);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    IMUtil.startServiceWhenLogin(MyApp.this);
                }
            }).start();

        } else {
            LoginActivity.startItself(this);
        }

    }

    static List<Activity> activitys = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }

    public static void exit() {
        for (Activity activity : activitys) {
            activity.finish();
        }
    }
}
