package cn.dazhou.railway.splash;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/26.
 */

public interface SplashContract {
    interface Presenter extends BasePresenter, NavigationView.OnNavigationItemSelectedListener {
        void changeText();
        boolean canBack();
    }

    interface View extends BaseView<Presenter> {
        DrawerLayout getDrawerLayout();
        void changeText();
        boolean canBack();
    }
}
