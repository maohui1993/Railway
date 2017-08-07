package cn.dazhou.railway;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.database.UserModel;
import cn.dazhou.railway.util.CrashHandler;

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

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {


            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setTopActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }


            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

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
