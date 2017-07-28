package cn.dazhou.railway.im.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.splash.SplashActivity;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/6/23.
 */

public class LoginFragment extends Fragment implements LoginContract.View {
    @BindView(R.id.btn_login)
    ActionProcessButton mLoginPbt;
    @BindView(R.id.edit_username)
    EditText mUsernameEdit;
    @BindView(R.id.content)
    android.view.View mContent;
    @BindView(R.id.password_warp)
    android.view.View mPasswordWrap;
    @BindView(R.id.username_wrap)
    android.view.View mUsernameWrap;
    InputMethodManager imm;

    @BindView(R.id.edit_password)
    EditText mPasswordEdit;
    private int inputState;
    private float offset;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                updateInputState(NOTINPUT);
            }
            super.handleMessage(msg);
        }
    };

    private LoginContract.Presenter mPresenter;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void success() {
        LogUtil.write("登录成功");
        mLoginPbt.setProgress(0);
        changeEditEnable();
        // 发送登录成功的广播
        IMUtil.sendBroadcast(getContext(), Constants.LOGIN_SUCCESS_BROADCAST);
        IMUtil.startServiceWhenLogin(getContext());
        SharedPreferenceUtil.putString(getContext(), Constants.LATEST_LOGIN_JID, MyApp.gCurrentUsername);
        SplashActivity.startItself(getContext());
        getActivity().finish();
    }
    @Override
    public void fail(String msg) {
        mLoginPbt.setProgress(0);
        changeEditEnable();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    public android.view.View getReLayoutView() {
        return mContent;
    }

    @Override
    public void updateInputState(int state) {
        inputState = state;
        switch (inputState) {
            case INPUTTING :
                mContent.setTranslationY(offset);
                mPresenter.listenerInputState(mHandler);
                break;
            case NOTINPUT :
                mContent.setTranslationY(0);
                break;
            default:
                break;
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_login, container, false);
        imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        ButterKnife.bind(this, root);
        // 设置按钮样式
        mLoginPbt.setMode(ActionProcessButton.Mode.ENDLESS);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow (v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                updateInputState(NOTINPUT);
            }
        });

        mPasswordWrap.setOnTouchListener(mPresenter);
        mUsernameWrap.setOnTouchListener(mPresenter);
        mUsernameEdit.setOnTouchListener(mPresenter);
        mPasswordEdit.setOnTouchListener(mPresenter);
        return root;
    }

    @OnClick(R.id.btn_login)
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_login :
                // Mode.ENDLESS情况下，progress大于0才会启动动画效果
                if (checkNotNull(mUsernameEdit.getText().toString()) && checkNotNull(mPasswordEdit.getText().toString())) {
                    mLoginPbt.setProgress(1);
                    changeEditEnable();
                    mPresenter.login(mUsernameEdit.getText().toString(), mPasswordEdit.getText().toString());
                } else {
                    Toast.makeText(getContext(), "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkNotNull(String s) {
        if (s == null || "".equals(s.trim())) {
            return false;
        }
        return true;
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
}
