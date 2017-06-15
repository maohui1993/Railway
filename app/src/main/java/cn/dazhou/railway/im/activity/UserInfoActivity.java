package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.entity.ExtraInfo;
import cn.dazhou.railway.R;
import cn.dazhou.railway.widget.MultiText;

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.header)
    MultiText mHeaderMtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        mToolbar.setTitle("个人信息");
        setSupportActionBar(mToolbar);
    }

    @OnClick({R.id.header, R.id.name, R.id.jid})
    void click(View v) {
        switch (v.getId()) {
            case R.id.header:
                break;
            case R.id.name:
                MultiText multiText = (MultiText)v;
                ExtraInfo info = new ExtraInfo();
                info.setName(multiText.getText());
                EditInfoActivity.startItself(this, info);
                break;
            default:
                break;
        }
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }
}
