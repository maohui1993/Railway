package cn.dazhou.railway.splash.functions.contact;

import java.util.List;

import cn.dazhou.database.FriendModel;
import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/26.
 */

public interface ContactListContract {
    interface Presenter extends BasePresenter, android.view.View.OnClickListener{
        void destroy();
        void updateRequestTipSate();
    }

    interface View extends BaseView<Presenter> {
        void onUpdateData(List datas);

        void hideRequestCountTip();

        void showRequestCountTip(String s);
    }
}
