package cn.dazhou.railway.splash.functions.contact;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/26.
 */

public interface ContactListContract {
    interface Presenter extends BasePresenter, android.view.View.OnClickListener{

    }

    interface View extends BaseView<Presenter> {

    }
}
