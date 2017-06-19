package cn.dazhou.railway;

import android.app.Application;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.activity.LoginActivity;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/5/17.
 */

public class MyApp extends Application {
    public static UserModel gCurrentUser; // 当前用户
    public static String gCurrentUsername; // 当前账号
    // 初始化位置在LoginPresenter#login
    public static String gServerIp;
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
        ZXingLibrary.initDisplayOpinion(this);

        String lastLogin = SharedPreferenceUtil.getString(this, Constants.LATEST_LOGIN_JID, "");
        if (!"".equals(lastLogin)) {
            gCurrentUser =UserModel.getUser(lastLogin);
            gCurrentUsername = lastLogin;
        } else {
            LoginActivity.startItself(this);
        }
    }
}
