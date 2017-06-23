package cn.dazhou.railway.im.chat;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;

import java.util.List;

import cn.dazhou.im.widget.ChatContentView;
import cn.dazhou.railway.BasePresenter;
import cn.dazhou.railway.BaseView;

/**
 * Created by hooyee on 2017/6/23.
 */

public interface ChatContract {
    interface View extends BaseView<ChatContract.Presenter> {
        boolean back();
        void refresh(List data);
    }

    interface Presenter extends BasePresenter, ChatContentView.OnSendListener, ChatContentView.OnImageClickListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener{
        void init();
    }
}
