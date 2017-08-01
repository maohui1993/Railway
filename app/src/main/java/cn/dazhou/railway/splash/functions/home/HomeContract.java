package cn.dazhou.railway.splash.functions.home;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/8/1.
 */

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void setPresenter(Presenter presenter);

        void addView(android.view.View v);

        android.view.View getViewByPosition(int position);
    }

    interface Presenter extends BasePresenter, RecyclerArrayAdapter.OnItemClickListener{

    }
}
