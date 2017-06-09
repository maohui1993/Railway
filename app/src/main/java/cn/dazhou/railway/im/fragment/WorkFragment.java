package cn.dazhou.railway.im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dazhou.railway.R;

public class WorkFragment extends BaseFragment {
    private GridView mGridView;
    private SimpleAdapter mAdapter;
    private List mDataList;

    // 图标
    private int[] mEffectArray = {
            R.drawable.record, R.drawable.check,
            R.drawable.breakdown, R.drawable.date
    };

    private String[] mNames;

    public static WorkFragment newInstance(boolean param1) {
        WorkFragment fragment = new WorkFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_work, container, false);
        mNames = getResources().getStringArray(R.array.names);
        mGridView = (GridView) root.findViewById(R.id.grid);
        initGridview();
        return root;
    }

    private void initGridview() {
        mDataList = new ArrayList<Map<String, Object>>();
        for(int i=0;i<mEffectArray.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", mEffectArray[i]);
            map.put("text", mNames[i]);
            mDataList.add(map);
        }
        String [] from ={"image", "text"};
        int [] to = {R.id.image, R.id.text};
        mAdapter = new SimpleAdapter(getContext(), mDataList, R.layout.grid_item, from, to);
        mGridView.setAdapter(mAdapter);
    }

}
