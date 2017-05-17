package cn.dazhou.railway;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by hooyee on 2017/5/17.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
