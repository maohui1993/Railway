package cn.dazhou.railway.splash.functions.work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import cn.dazhou.railway.R;
import cn.dazhou.railway.splash.functions.BaseFragment;

public class WorkFragment extends BaseFragment implements WorkContract.View{
    private GridView mGridView;
    private WorkContract.Presenter mPresenter;

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
        View root = inflater.inflate(R.layout.fragment_work, container, false);
        mGridView = (GridView) root.findViewById(R.id.grid);
        return root;
    }

    @Override
    public void setPresenter(WorkContract.Presenter presenter) {

    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
//        if (mGridView != null) {
            mGridView.setAdapter(adapter);
//        }
    }
}
