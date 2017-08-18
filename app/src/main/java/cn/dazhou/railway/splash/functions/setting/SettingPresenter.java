package cn.dazhou.railway.splash.functions.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.splash.functions.setting.function.MessageTipActivity;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/7/10.
 */

public class SettingPresenter implements SettingContract.Presenter {
    private SettingContract.View mView;
    private Context mContext;
    private boolean needReboot;
    Handler mHandler = new Handler();

    public SettingPresenter(SettingContract.View view, Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_accept:
                final String defaultIp = SharedPreferenceUtil.getString(mContext, Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT);
                final int defaultPort = SharedPreferenceUtil.getInt(mContext, Constants.SERVER_PORT, Constants.SERVER_PORT_DEFAULT);
                if (mView.getIp().equals(defaultIp)
                        && mView.getPort().equals(String.valueOf(defaultPort))) {

                } else {
                    try {
                        SharedPreferenceUtil.putInt(mContext, Constants.SERVER_PORT, Integer.valueOf(mView.getPort()));
                        SharedPreferenceUtil.putString(mContext, Constants.SERVER_IP, mView.getIp());
                    } catch (Exception e) {
                        Toast.makeText(mContext, "请输入正确的端口号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    IMUtil.logout(mContext);
                    Toast.makeText(mContext, "服务器配置已修改，应用即将重启", Toast.LENGTH_SHORT).show();
                    needReboot = true;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((Activity)mContext).finish();
                        }
                    }, 1000);
                }
                break;
            case R.id.tip:
                MessageTipActivity.startItself(mContext);
                break;
        }
    }

    private void restartApplication() {
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    @Override
    public void destroy() {
        if (needReboot) {
            restartApplication();
        }
    }
}
