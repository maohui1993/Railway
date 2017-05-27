package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.SharedPreferenceUtil;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.edit_server_ip)
    EditText mServerIpEdit;
    @BindView(R.id.bt_submit)
    Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_submit)
    void submit() {
        String ip = mServerIpEdit.getText().toString().trim();
        if (!"".equals(ip)) {
            SharedPreferenceUtil.putString(this, Constants.SERVER_IP, ip);
            MyApp.gServerIp = ip;
        }
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
