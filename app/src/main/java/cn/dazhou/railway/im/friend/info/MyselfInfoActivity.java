package cn.dazhou.railway.im.friend.info;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.database.UserModel;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.BaseActivity;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.login.LoginActivity;
import cn.dazhou.railway.util.IMUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;
import cn.dazhou.railway.widget.MultiText;

public class MyselfInfoActivity extends BaseActivity {
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.header)
    MultiText mHeaderMtx;
    @BindView(R.id.name)
    MultiText mNameMtx;
    @BindView(R.id.jid)
    MultiText mJidMtx;
    @BindView(R.id.qr_code)
    MultiText mQRCode;
    @BindView(R.id.tel)
    MultiText mTelMtx;
    @BindView(R.id.mt_logout)
    MultiText mLogoutMtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayoutToBase(R.layout.activity_myself_info);
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

    @OnClick({R.id.header, R.id.name, R.id.jid, R.id.tel, R.id.mt_logout, R.id.qr_code})
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
            case R.id.mt_logout:
                new AlertDialog.Builder(this)
                        .setTitle("退出当前账号")
                        .setMessage("是否确认要退出当前账号？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IMUtil.logout(MyselfInfoActivity.this);
                                LoginActivity.startItself(MyselfInfoActivity.this);
                                finish();
                            }
                        })
                        .create()
                .show();

                break;
            case R.id.qr_code:
                final ImageView image = new ImageView(this);
                Bitmap bmp = CodeUtils.createImage(MyApp.gCurrentUsername, 400, 400, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
                image.setImageBitmap(bmp);
                new AlertDialog.Builder(this)
                        .setView(image)
                        .setTitle(R.string.qr_code)
                        .setPositiveButton(R.string.sure, null)
                        .create()
                        .show();
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
