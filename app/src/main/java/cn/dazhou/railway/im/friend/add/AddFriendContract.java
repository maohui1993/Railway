package cn.dazhou.railway.im.friend.add;

import java.util.List;

import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/23.
 */

public interface AddFriendContract {
    interface View extends BaseView<AddFriendContract.Presenter> {
        void result(List data);
    }

    interface Presenter extends BasePresenter {
        void searchUser(String jid);
    }
}
