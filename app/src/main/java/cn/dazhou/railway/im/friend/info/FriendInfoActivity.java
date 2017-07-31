package cn.dazhou.railway.im.friend.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.database.FriendModel;
import cn.dazhou.database.util.StringUtil;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.SharedPreferenceUtil;

public class FriendInfoActivity extends AppCompatActivity {
    @BindView(R.id.tx_nick_name)
    TextView mNickNameTx;
    @BindView(R.id.tx_jid)
    TextView mJidTx;
    @BindView(R.id.tx_tel)
    TextView mTelTx;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    FriendModel friendModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        ButterKnife.bind(this);

        String jid = getIntent().getStringExtra(Constants.DATA_KEY);
        mJidTx.setText(jid);
        mToolbar.setTitle("好友信息");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 有ARN风险
        String latestUser = SharedPreferenceUtil.getString(this, Constants.LATEST_LOGIN_JID, "");
//        if (!"".equals(latestUser)) {
            friendModel = FriendModel.getMyFriend(StringUtil.getWrapJid(jid));
            mNickNameTx.setText(friendModel.getNickName());
            mTelTx.setText(friendModel.getTel());
//        }
    }

    @OnClick(R.id.search_record)
    void searchChatRecord() {
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, FriendInfoActivity.class);
        intent.putExtra(Constants.DATA_KEY, data);
        context.startActivity(intent);
    }
}
