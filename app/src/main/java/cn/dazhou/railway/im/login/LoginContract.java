package cn.dazhou.railway.im.login;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/23.
 */

public interface LoginContract {
    interface View extends BaseView<LoginContract.Presenter> {
        void success();

        void fail(String msg);
    }

    interface Presenter extends BasePresenter {
        boolean login(String username, String password);
    }
}
