package cn.dazhou.railway.splash.functions.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.railway.R;
import cn.dazhou.railway.splash.functions.BaseFragment;

public class HomeFragment extends BaseFragment implements HomeContract.View{
    private HomeContract.Presenter mPresenter;
    private TextAdapter mNoticeAdapter;
    private TextAdapter mLawAdapter;
    @BindView(R.id.notice_list)
    EasyRecyclerView mNoticeViews;
    @BindView(R.id.law_list)
    EasyRecyclerView mLawViews;
    @BindView(R.id.tx_tip)
    TextView mTipText;

    List<String> mNotices = new ArrayList<>();
    List<String> mLaws = new ArrayList<>();

    ViewGroup mContainer;

    public static HomeFragment newInstance(boolean param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo.State wifiState = null;
            NetworkInfo.State mobileState = null;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED == mobileState) {
                // 手机网络连接成功
                //      IMUtil.login(context);
                mTipText.setVisibility(View.GONE);
            } else if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED != mobileState) {
                // 手机没有任何的网络
                mTipText.setVisibility(View.VISIBLE);
            } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                // 无线网络连接成功
                //     IMUtil.login(context);
                mTipText.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotices.add("1、巡检计划公告");
        mNotices.add("2、维护计划公告");
        mNotices.add("3、人员调动计划公告");
        mNotices.add("4、巡检计划公告");
        mNotices.add("5、维护计划公告");
        mNotices.add("6、人员调动计划公告");        mNotices.add("3、人员调动计划公告");
        mNotices.add("4、巡检计划公告");
        mNotices.add("5、维护计划公告");
        mNotices.add("6、人员调动计划公告");

        mLaws.add("守则1");
        mLaws.add("守则2");
        mLaws.add("守则3");
        mLaws.add("守则4");
        mLaws.add("守则5");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mContainer = (ViewGroup) root;
        ButterKnife.bind(this, root);
        mPresenter = new HomePresenter(getContext(), this);

        mNoticeAdapter = new TextAdapter(getContext(), mNotices);
        mNoticeViews.setLayoutManager(new LinearLayoutManager(getContext()));
        mNoticeViews.setAdapter(mNoticeAdapter);
        mNoticeAdapter.setOnItemClickListener(mPresenter);

        mLawAdapter = new TextAdapter(getContext(), mLaws);
        mLawViews.setLayoutManager(new LinearLayoutManager(getContext()));
        mLawViews.setAdapter(mLawAdapter);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(receiver, filter);
        return root;
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addView(View v) {
        int[] position = new int[2];
        mContainer.getLocationOnScreen(position);
        v.setX(v.getX() - position[0]);
        v.setY(v.getY() - position[1]);
        mContainer.addView(v);
    }

    @Override
    public View getViewByPosition(int position) {
        return mNoticeAdapter.getViewHolders().get(position).getView();
    }
}
