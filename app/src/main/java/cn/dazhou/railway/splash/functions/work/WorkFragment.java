package cn.dazhou.railway.splash.functions.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Collections;

import cn.dazhou.railway.R;
import cn.dazhou.railway.splash.functions.BaseFragment;

public class WorkFragment extends BaseFragment implements WorkContract.View {
    private GridView mGridView;
    private WorkContract.Presenter mPresenter;
    private View mRootView;

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
        mPresenter = new WorkPresenter(getContext(), this);
        mPresenter.initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_work, container, false);
            mGridView = (GridView) mRootView.findViewById(R.id.grid);
            mGridView.setOnItemClickListener(mPresenter);
        } else {
            // 同一个parent不能添加相同的view，因此要先移除
            ViewGroup parent = (ViewGroup) mGridView.getParent();
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void setPresenter(WorkContract.Presenter presenter) {

    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        GridAdapter gridAdapter = (GridAdapter) adapter;
        if (gridAdapter.getAllData() != null && gridAdapter.getAllData().size() > 0) {
            Collections.sort(((GridAdapter) adapter).getAllData());
        }
        mGridView.setAdapter(adapter);
    }
}
