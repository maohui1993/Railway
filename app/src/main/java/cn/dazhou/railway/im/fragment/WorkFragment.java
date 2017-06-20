package cn.dazhou.railway.im.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.railway.R;
import cn.dazhou.railway.im.adapter.GridAdapter;

public class WorkFragment extends BaseFragment {
    private GridView mGridView;
    private GridAdapter adapter;

    // 图标
    private int[] mEffectArray = {
            R.drawable.record, R.drawable.check,
            R.drawable.breakdown, R.drawable.date
    };

    List<GridAdapter.Item> datas = new ArrayList<>();

    private String[] mNames;

    public static WorkFragment newInstance(boolean param1) {
        WorkFragment fragment = new WorkFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNames = getResources().getStringArray(R.array.names);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_work, container, false);
        mGridView = (GridView) root.findViewById(R.id.grid);
        mGridView.setAdapter(adapter);
        return root;
    }

    private void initData() {
        for (int i = 0; i < mEffectArray.length; i++) {
            datas.add(new GridAdapter.Item(mEffectArray[i], mNames[i]));
        }
        adapter = new GridAdapter(getContext(), datas, R.layout.grid_item);
    }
}
