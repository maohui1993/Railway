package cn.dazhou.railway.im.addfriend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.adapter.UserAdapter;

/**
 * Created by hooyee on 2017/6/23.
 */

public class AddFriendFragment extends Fragment implements AddFriendContract.View {
    private AddFriendContract.Presenter mPresenter;
    private UserAdapter mAdapter;
    private List<UserBean> datas = new ArrayList();

    @BindView(R.id.edit_search_user)
    EditText mEditText;
    @BindView(R.id.easy_recycler_ver)
    EasyRecyclerView mEasyRecyclerView;

    public static AddFriendFragment newInstance() {
        return new AddFriendFragment();
    }

    @Override
    public void setPresenter(AddFriendContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_add_friend, container, false);
        ButterKnife.bind(this, root);

        mAdapter = new UserAdapter(getContext(), datas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mEasyRecyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getContext(), 0.5f), 0, 0);
        itemDecoration.setDrawLastItem(true);
        mEasyRecyclerView.setAdapter(mAdapter);
        mEasyRecyclerView.addItemDecoration(itemDecoration);

        mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StringBuilder sb = new StringBuilder(mAdapter.getAllData().get(position).getUsername());
                // 拼写jid
                sb.append(Constants.JID_SEPARATOR).append(MyApp.gServerIp);
                Log.i("TAG", "添加的用户Jid: " + sb.toString());
                IMLauncher.addFriend(sb.toString());
            }
        });
        return root;
    }

    @OnClick(R.id.bt_search_user)
    public synchronized void searchUser() {
        final String username = mEditText.getText().toString();
        mPresenter.searchUser(username);
    }

    @Override
    public void result(List data) {
        mAdapter.clear();
        mAdapter.addAll(data);
    }
}
