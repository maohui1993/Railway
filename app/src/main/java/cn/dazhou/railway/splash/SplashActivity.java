package cn.dazhou.railway.splash;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.util.ImageUtil;
import cn.dazhou.maputil.MapLauncher;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.SettingActivity;
import cn.dazhou.railway.im.broadcast.NetworkReceiver;
import cn.dazhou.railway.util.ActivityUtils;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;

public class SplashActivity extends AppCompatActivity {

    SplashPresenter mPresenter;
    SplashFragment mSplashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IMUtil.checkUser(this);
        setContentView(R.layout.activity_splash1);
        MapLauncher.init(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ImageUtil.checkPermission(this,
                    new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.VIBRATE,
                            Manifest.permission.ACCESS_NETWORK_STATE
                    });
        }
        LogUtil.init();
        MyApp.addActivity(this);

        mSplashFragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mSplashFragment == null) {
            // Create the fragment
            mSplashFragment = SplashFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mSplashFragment, R.id.contentFrame);
        }
        mPresenter = new SplashPresenter(this, mSplashFragment);

    }

    @Override
    protected void onResume() {
        mPresenter.changeText();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.canBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
//                SettingActivity.startItself(this);
                mSplashFragment.toSetting();
                break;
            case R.id.menu_barcode:
                mPresenter.parseQRcode();
                break;
        }
        return true;
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }
}
