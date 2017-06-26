package cn.dazhou.railway.splash.functions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.util.SharedPreferenceUtil;

/**
 * Created by hooyee on 2017/5/31.
 */

public class SettingFragment extends BaseFragment {
    private EditText mEditText;
    public SettingFragment(){}

    public static SettingFragment newInstance(boolean param1) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        mEditText = (EditText) root.findViewById(R.id.edit_server_ip);
        mEditText.setText(SharedPreferenceUtil.getString(getContext(), Constants.SERVER_IP, Constants.SERVER_IP_DEFAULT));
        return root;
    }
}
