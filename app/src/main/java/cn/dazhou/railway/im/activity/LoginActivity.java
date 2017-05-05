package cn.dazhou.railway.im.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.aspect.ICallback;
import cn.dazhou.railway.im.listener.IOnLoginListener;

public class LoginActivity extends AppCompatActivity implements IOnLoginListener {

    @BindView(R.id.btn_login)
    ActionProcessButton mLoginPbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPbt.setMode(ActionProcessButton.Mode.ENDLESS);
        mLoginPbt.setProgress(1);
    }

    @OnClick(R.id.btn_login)
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_login :
                mLoginPbt.setProgress(1);
        }
    }

    @Override
    public void onSuccess() {
        mLoginPbt.setProgress(1);
    }
}
