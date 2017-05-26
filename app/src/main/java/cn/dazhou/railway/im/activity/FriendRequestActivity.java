package cn.dazhou.railway.im.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.adapter.FriendRequestAdapter;

public class FriendRequestActivity extends AppCompatActivity {
    @BindView(R.id.easy_recycler_ver)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    private FriendRequestAdapter mAdapter;
    private List<UserBean> datas = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        String jid = getIntent().getStringExtra(Constants.DATA_KEY);
        UserBean user = new UserBean();
        user.setUsername(jid);
        user.setName(jid);
        datas.add(user);

        mAdapter = new FriendRequestAdapter(this, datas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this, 0.5f), Util.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        mEasyRecyclerView.setAdapter(mAdapter);
        mEasyRecyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_request_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add_friend:
                AddFriendActivity.startItself(this);
                break;
        }
        return true;
    }
}
