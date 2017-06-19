package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.db.UserModel;
import cn.dazhou.railway.util.SharedPreferenceUtil;
import cn.dazhou.railway.widget.MultiText;

public class MyselfInfoActivity extends AppCompatActivity {
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.header)
    MultiText mHeaderMtx;
    @BindView(R.id.name)
    MultiText mNameMtx;
    @BindView(R.id.jid)
    MultiText mJidMtx;
    @BindView(R.id.tel)
    MultiText mTelMtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_info);
        ButterKnife.bind(this);
        mToolbar.setTitle("个人信息");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ExtraInfo info = IMLauncher.getVCard(MyApp.gCurrentUsername + "@192.168.1.39");
//        ExtraInfo info = IMLauncher.getVCard(StringUtil.getRealJid(MyApp.gCurrentUsername));
        UserModel user = null;
        String latestUser = SharedPreferenceUtil.getString(this, Constants.LATEST_LOGIN_JID, "");
        if (MyApp.gCurrentUser != null) {
            user = MyApp.gCurrentUser;
        } else if (!"".equals(latestUser)){
            user = UserModel.getUser(latestUser);
        }
        if (user == null) {
            return;
        }
        mJidMtx.setText(user.getUsername());
        mNameMtx.setText(user.getNickName());
        mTelMtx.setText(user.getTel());
    }

    @OnClick({R.id.header, R.id.name, R.id.jid, R.id.tel})
    void click(View v) {
        switch (v.getId()) {
            case R.id.header:
                break;
            case R.id.name:
                MultiText name = (MultiText)v;
                ExtraInfo nameInfo = new ExtraInfo();
                nameInfo.setName(name.getText());
                nameInfo.setTitle(EditInfoActivity.TITLE_NAME);
                EditInfoActivity.startItself(this, nameInfo);
                break;
            case R.id.tel:
                MultiText tel = (MultiText)v;
                ExtraInfo telInfo = new ExtraInfo();
                telInfo.setName(tel.getText());
                telInfo.setTitle(EditInfoActivity.TITLE_TEL);
                EditInfoActivity.startItself(this, telInfo);
                break;
            default:
                break;
        }
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, MyselfInfoActivity.class);
        context.startActivity(intent);
    }
}
