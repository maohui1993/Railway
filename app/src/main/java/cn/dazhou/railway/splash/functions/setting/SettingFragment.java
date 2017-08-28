package cn.dazhou.railway.splash.functions.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.splash.functions.BaseFragment;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/5/31.
 */

public class SettingFragment extends BaseFragment implements SettingContract.View{
    private EditText mIpEdit;
    private EditText mPortEdit;
    private EditText mWorkPortEdit;

    private SettingContract.Presenter mPresenter;
    public SettingFragment(){}

    public static SettingFragment newInstance(boolean param1) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SettingPresenter(this, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        final InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        String defaultIp = SharedPreferenceUtil.getString(getContext(), Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT);
        int defaultPort = SharedPreferenceUtil.getInt(getContext(), Constants.SERVER_PORT, Constants.SERVER_PORT_DEFAULT);
        int workPort = SharedPreferenceUtil.getInt(getContext(), "work_port", 8080);
        mIpEdit = (EditText) root.findViewById(R.id.edit_server_ip);
        mIpEdit.setText(defaultIp);
        mPortEdit = (EditText) root.findViewById(R.id.edit_server_port);
        mPortEdit.setText(String.valueOf(defaultPort));
        mWorkPortEdit = (EditText) root.findViewById(R.id.edit_work_port);
        mWorkPortEdit.setText(String.valueOf(workPort));
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        root.findViewById(R.id.bt_accept).setOnClickListener(mPresenter);
        root.findViewById(R.id.tip).setOnClickListener(mPresenter);
        return root;
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getPort() {
        return mPortEdit.getText().toString().trim();
    }

    @Override
    public String getIp() {
        return mIpEdit.getText().toString().trim();
    }

    @Override
    public String getWorkPort() {
        return mWorkPortEdit.getText().toString().trim();
    }
}
