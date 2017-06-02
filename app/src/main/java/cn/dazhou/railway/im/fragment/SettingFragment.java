package cn.dazhou.railway.im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/5/31.
 */

public class SettingFragment extends BaseFragment {
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

        return root;
    }
}
