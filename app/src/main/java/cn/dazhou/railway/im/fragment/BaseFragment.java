package cn.dazhou.railway.im.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.dazhou.railway.im.activity.LoginActivity;

/**
 * Created by hooyee on 2017/5/31.
 */

public class BaseFragment extends Fragment {
    protected static String ARG_PARAM1 = "param1";

    /** 是否必须登录 */
    protected boolean isMustLogin;

    public boolean isMustLogin() {
        return isMustLogin;
    }

    public void setMustLogin(boolean login) {
        isMustLogin = login;
    }

    public void requestLogin() {
        LoginActivity.startItself(getContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isMustLogin = getArguments().getBoolean(ARG_PARAM1);
        }
    }

}
