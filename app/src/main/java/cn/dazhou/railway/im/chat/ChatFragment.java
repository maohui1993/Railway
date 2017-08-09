package cn.dazhou.railway.im.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.core.ChatContentView;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/6/23.
 */

public class ChatFragment extends Fragment implements ChatContract.View {
    private ChatContract.Presenter mPresenter;
    @BindView(R.id.chat_content)
    ChatContentView mChatContentView;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void setPresenter(@NonNull ChatContract.Presenter presenter) {
        if (presenter == null) {
            throw new NullPointerException();
        }
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_chat, container, false);
        ButterKnife.bind(this, root);
        mPresenter.init();
        // 点击发送按钮时
        mChatContentView.setOnSendListener(mPresenter);
        mChatContentView.setOnImageClickListener(mPresenter);
        mChatContentView.setRefreshListener(mPresenter);
        return root;
    }

    @Override
    public boolean back() {
        if (!mChatContentView.interceptBackPress()) {
            return true;
        }
        return false;
    }

    @Override
    public void refresh(List data) {
        mChatContentView.onRefresh(data);
    }

    @Override
    public void showLoadTip(String tip, boolean isAll) {
        mChatContentView.showLoadTip(tip, isAll);
    }

    @Override
    public void onDestroy() {
        mChatContentView.destroy();
        super.onDestroy();
    }
}
