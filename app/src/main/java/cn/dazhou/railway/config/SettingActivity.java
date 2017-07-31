package cn.dazhou.railway.config;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.util.Config;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.util.SharedPreferenceUtil;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.edit_server_ip)
    EditText mServerIpEdit;
    @BindView(R.id.bt_accept)
    Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mServerIpEdit.setText(SharedPreferenceUtil.getString(this, Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT));
    }

    @OnClick(R.id.bt_accept)
    void submit() {
        String ip = mServerIpEdit.getText().toString().trim();
        if (!"".equals(ip)) {
            SharedPreferenceUtil.putString(this, Constants.SERVER_IP, ip);
            SharedPreferenceUtil.putString(this, Constants.LATEST_LOGIN_JID, "");
            MyApp.gServerIp = ip;
            Config.gServerIp = ip;
        }
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
