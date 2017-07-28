package cn.dazhou.railway.im.login;

import android.app.Activity;
import android.os.Handler;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/23.
 */

public interface LoginContract {
    interface View extends BaseView<LoginContract.Presenter> {
        int INPUTTING = 0x101;
        int NOTINPUT = 0x102;
        void success();

        void fail(String msg);

        void setOffset(float offset);

        android.view.View getReLayoutView();

        Activity getActivity();

        void updateInputState(int state);

    }

    interface Presenter extends BasePresenter, android.view.View.OnTouchListener{
        boolean login(String username, String password);
        float calculateOffset(Activity activity, android.view.View v);
        float getOffset();
        void listenerInputState(Handler handler);
    }
}
