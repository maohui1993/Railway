package cn.dazhou.railway;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import cn.dazhou.railway.im.db.UserModel;

/**
 * Created by hooyee on 2017/5/17.
 */

public class MyApp extends Application {
    public static UserModel gCurrentUser; // 当前用户
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
    }
}
