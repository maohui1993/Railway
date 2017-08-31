package cn.dazhou.railway.im.friend.message.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cn.dazhou.railway.BaseActivity;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.ActivityUtils;

public class MessageListActivity extends BaseActivity {
    private MessageListFragment mFragment;
    private MessageListContract.Presenter mPresenter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayoutToBase(R.layout.activity_message_list);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mToolbar.setTitle("消息列表");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFragment = (MessageListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mFragment == null) {
            // Create the fragment
            mFragment = MessageListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mFragment, R.id.contentFrame);
        }
        mPresenter = new MessageListPresenter(mFragment, this);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        context.startActivity(intent);
    }
}
