package cn.dazhou.railway.splash.functions;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/5/31.
 */

public class SettingFragment extends BaseFragment {
    private EditText mIpEdit;
    private EditText mPortEdit;
    private boolean needReboot;
    public SettingFragment(){}

    public static SettingFragment newInstance(boolean param1) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        final String defaultIp = SharedPreferenceUtil.getString(getContext(), Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT);
        final int defaultPort = SharedPreferenceUtil.getInt(getContext(), Constants.SERVER_PORT, Constants.SERVER_PORT_DEFAULT);
        mIpEdit = (EditText) root.findViewById(R.id.edit_server_ip);
        mIpEdit.setText(defaultIp);
        mPortEdit = (EditText) root.findViewById(R.id.edit_server_port);
        mPortEdit.setText(String.valueOf(defaultPort));

        root.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIpEdit.getText().toString().equals(defaultIp)
                        && mPortEdit.getText().toString().equals(String.valueOf(defaultPort))) {

                } else {
                    SharedPreferenceUtil.putString(getContext(), Constants.SERVER_IP, mIpEdit.getText().toString().trim());
                    try {
                        SharedPreferenceUtil.putInt(getContext(), Constants.SERVER_PORT, Integer.valueOf(mPortEdit.getText().toString().trim()));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "请输入正确的端口号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getContext(), "服务器配置已修改，应用即将重启", Toast.LENGTH_SHORT).show();
                    needReboot = true;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 1000);
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        if (needReboot) {
            restartApplication();
        }
        super.onDestroy();
    }

    private void restartApplication() {
        final Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
