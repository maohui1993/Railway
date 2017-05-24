package cn.dazhou.railway.im.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.EditText;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.adapter.UserAdapter;
import cn.dazhou.railway.im.presenter.AddFriendPresenter;

public class AddFriendActivity extends AppCompatActivity {
    @BindView(R.id.edit_search_user)
    EditText mEditText;
    @BindView(R.id.easy_recycler_ver)
    EasyRecyclerView mEasyRecyclerView;
    private UserAdapter mAdapter;
    private AddFriendPresenter mPresenter;
    private List<UserBean> datas = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        mPresenter = new AddFriendPresenter(this);
        mAdapter = new UserAdapter(this, datas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);
        itemDecoration.setDrawLastItem(false);
        mEasyRecyclerView.addItemDecoration(itemDecoration);
    }

    @OnClick(R.id.bt_search_user)
    public void searchUser() {
        String username = mEditText.getText().toString();
        final List<UserBean> users = IMLauncher.searchUserFromServer(username);
        if (users != null) {
            datas.clear();
            datas.addAll(users);
            mEasyRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    StringBuilder sb = new StringBuilder(users.get(position).getUsername());
                    // 拼写jid
                    sb.append(Constants.JID_SEPARATOR).append(Constants.SERVER_IP);
                    Log.i("TAG", "添加的用户Jid: " + sb.toString());
                    IMLauncher.addFriend(sb.toString());
                }
            });
        }
    }
}
