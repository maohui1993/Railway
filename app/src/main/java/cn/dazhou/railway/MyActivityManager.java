package cn.dazhou.railway;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by hooyee on 2017/8/7.
 */

public class MyActivityManager {
    private static MyActivityManager customActivityManager = new MyActivityManager();
    private WeakReference<Activity> topActivity;

    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        return customActivityManager;
    }

    public Activity getTopActivity() {
        if (topActivity != null) {
            return topActivity.get();
        }
        return null;
    }

    public void setTopActivity(Activity topActivity) {
        this.topActivity = new WeakReference<>(topActivity);
    }
}
