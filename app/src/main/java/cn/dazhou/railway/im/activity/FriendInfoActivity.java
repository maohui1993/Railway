package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.util.SharedPreferenceUtil;
import cn.dazhou.railway.util.StringUtil;

public class FriendInfoActivity extends AppCompatActivity {
    @BindView(R.id.tx_nick_name)
    TextView mNickNameTx;
    @BindView(R.id.tx_jid)
    TextView mJidTx;
    @BindView(R.id.tx_tel)
    TextView mTelTx;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;

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
        if (!"".equals(latestUser)) {
            FriendModel friendModel = FriendModel.getMyFriend(StringUtil.getWrapJid(jid, latestUser));
            mNickNameTx.setText(friendModel.getNickName());
            mTelTx.setText(friendModel.getTel());
        }
//        else {
//            ExtraInfo info = IMLauncher.getVCard(StringUtil.getRealJid(latestUser));
//            mNickNameTx.setText(info.getName());
//            mTelTx.setText(info.getTel());
//        }
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, FriendInfoActivity.class);
        intent.putExtra(Constants.DATA_KEY, data);
        context.startActivity(intent);
    }
}
