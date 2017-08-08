package cn.dazhou.railway.splash.functions.work;

import android.widget.BaseAdapter;


import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/8/8.
 */

public interface WorkContract {
    interface Presenter extends BasePresenter {
        void initData();
        void getDataFromServer(String url);
    }

    interface View extends BaseView<Presenter> {
        void setAdapter(BaseAdapter adapter);
    }
}
