package cn.dazhou.railway.im.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.listener.IOnLoginListener;
import cn.dazhou.railway.im.presenter.LoginPresenter;
import cn.dazhou.railway.util.LogUtil;

public class LoginActivity extends AppCompatActivity implements IOnLoginListener {

    @BindView(R.id.btn_login)
    ActionProcessButton mLoginPbt;
    @BindView(R.id.edit_username)
    EditText mUsernameEdit;
    @BindView(R.id.edit_password)
    EditText mPasswordEdit;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 设置按钮样式
        mLoginPbt.setMode(ActionProcessButton.Mode.ENDLESS);
        mPresenter = new LoginPresenter(this, this);
    }

    @OnClick(R.id.btn_login)
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_login :
                // Mode.ENDLESS情况下，progress大于0才会启动动画效果
                mLoginPbt.setProgress(1);
                changeEditEnable();
                mPresenter.login(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString());
                break;
        }
    }

    /**
     * 变换EditText的可输入状态
     */
    private void changeEditEnable() {
        boolean userEdit = mUsernameEdit.isEnabled();
        boolean passEdit = mUsernameEdit.isEnabled();
        mUsernameEdit.setEnabled(!userEdit);
        mPasswordEdit.setEnabled(!passEdit);
    }

    @Override
    public void onSuccess() {
//        LogUtil.write("登录成功".getBytes());
        mLoginPbt.setProgress(0);
        changeEditEnable();
        MainActivity.startItself(this, "");
        finish();
    }

    @Override
    public void onFail(String info) {
        mLoginPbt.setProgress(0);
        changeEditEnable();
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
}
