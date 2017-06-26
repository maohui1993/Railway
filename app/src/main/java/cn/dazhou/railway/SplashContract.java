package cn.dazhou.railway;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

/**
 * Created by hooyee on 2017/6/26.
 */

public interface SplashContract {
    interface Presenter extends BasePresenter, NavigationView.OnNavigationItemSelectedListener {
        void changeText();
        boolean canBack();
    }

    interface View extends BaseView<SplashContract.Presenter> {
        DrawerLayout getDrawerLayout();
        void changeText();
        boolean canBack();
    }
}
