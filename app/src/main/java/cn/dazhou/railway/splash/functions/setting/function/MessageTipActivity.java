package cn.dazhou.railway.splash.functions.setting.function;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.SharedPreferenceUtil;

public class MessageTipActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CheckBox mRingtoneBox;
    private CheckBox mShakeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_tip);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle("消息提醒");

        mRingtoneBox = (CheckBox) findViewById(R.id.box_ringtone);
        mShakeBox = (CheckBox) findViewById(R.id.box_shake);

        mRingtoneBox.setChecked(SharedPreferenceUtil.getBoolean(this, Constants.CAN_RINGTONE, true));
        mShakeBox.setChecked(SharedPreferenceUtil.getBoolean(this, Constants.CAN_SHAKE, true));

        mRingtoneBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceUtil.putBoolean(MessageTipActivity.this, Constants.CAN_RINGTONE, isChecked);
            }
        });

        mShakeBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceUtil.putBoolean(MessageTipActivity.this, Constants.CAN_SHAKE, isChecked);
            }
        });
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, MessageTipActivity.class);
        context.startActivity(intent);
    }
}
