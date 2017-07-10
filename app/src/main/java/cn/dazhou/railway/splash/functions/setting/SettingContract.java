package cn.dazhou.railway.splash.functions.setting;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/7/10.
 */

public interface SettingContract {
    interface Presenter extends BasePresenter, android.view.View.OnClickListener {
        void destroy();
    }

    interface View extends BaseView<Presenter> {
        String getPort();
        String getIp();
    }
}
