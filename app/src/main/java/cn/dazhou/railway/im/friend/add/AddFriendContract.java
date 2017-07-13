package cn.dazhou.railway.im.friend.add;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/23.
 */

public interface AddFriendContract {
    interface View extends BaseView<AddFriendContract.Presenter> {
        void result(List data);
        List<UserBean> getData();
    }

    interface Presenter extends BasePresenter, RecyclerArrayAdapter.OnItemClickListener {
        void searchUser(String jid);
    }
}
