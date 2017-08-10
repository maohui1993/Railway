package cn.dazhou.railway.im.friend.request;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.database.FriendRequestModel;
import cn.dazhou.database.util.DataHelper;
import cn.dazhou.im.entity.UserBean;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.friend.add.AddFriendActivity;

public class FriendRequestActivity extends AppCompatActivity {
    @BindView(R.id.easy_recycler_ver)
    EasyRecyclerView mEasyRecyclerView;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    private FriendRequestAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FriendRequestModel request = getIntent().getParcelableExtra(Constants.DATA_KEY);

        List<FriendRequestModel> requests = DataHelper.getFriendRequests(MyApp.gCurrentUsername);
        if (request != null) {
            int index = requests.indexOf(request);
            if (index >= 0) {
                requests.remove(index);
            }
            requests.add(request);
        }
        mAdapter = new FriendRequestAdapter(this, requests);

//        DataHelper.getFriendRequests(MyApp.gCurrentUsername);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEasyRecyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this, 0.5f), Util.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        mEasyRecyclerView.setAdapter(mAdapter);
        mEasyRecyclerView.addItemDecoration(itemDecoration);
    }

    public void addItem(FriendRequestModel request) {
        int index = mAdapter.getAllData().indexOf(request);
        if (index >= 0) {
            mAdapter.remove(index);
        }

        mAdapter.add(request);
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
