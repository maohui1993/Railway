package cn.dazhou.railway.im.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.SplashActivity;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.chat.ChatFragment;
import cn.dazhou.railway.im.listener.IOnLoginListener;
import cn.dazhou.railway.util.ActivityUtils;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

public class LoginActivity extends AppCompatActivity {
    Toolbar mToolbar;

    private LoginPresenter mPresenter;
    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.addActivity(this);
        setContentView(R.layout.activity_login);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        mLoginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mLoginFragment == null) {
            // Create the fragment
            mLoginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mLoginFragment, R.id.contentFrame);
        }
        mPresenter = new LoginPresenter(this, mLoginFragment);
    }


    int clickNum;

    @Override
    public void onBackPressed() {
        clickNum++;
        if (clickNum == 2) {
            MyApp.exit();
            System.exit(0);
        }
        Handler handler = new Handler();
        Toast.makeText(LoginActivity.this, "再按一次将退出", Toast.LENGTH_SHORT).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (clickNum != 2) {
                    clickNum = 0;
                }
            }
        }, 2000);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
