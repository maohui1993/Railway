package cn.dazhou.railway.im.friend.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import butterknife.BindView;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.ActivityUtils;

public class AddFriendActivity extends AppCompatActivity {

    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    private AddFriendFragment mAddFriendFragment;
    private AddFriendContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAddFriendFragment = (AddFriendFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mAddFriendFragment == null) {
            mAddFriendFragment = AddFriendFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mAddFriendFragment, R.id.contentFrame);
        }

        mPresenter = new AddFriendPresenter(this, mAddFriendFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_request_activity, menu);
        return true;
    }

    public static final void startItself(Context context) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
    }
}
