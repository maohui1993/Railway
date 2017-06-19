package cn.dazhou.railway.im.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.jude.rollviewpager.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.adapter.RosterAdapter;
import cn.dazhou.railway.im.adapter.StickyHeaderAdapter;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;
import cn.dazhou.railway.im.presenter.ContactListPresenter;

public class ContactListFragment extends BaseFragment implements OnDataUpdateListener<FriendModel> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String DATA_KEY = "jid";

    private EasyRecyclerView mRosterView;
    private RosterAdapter mRosterAdapter;
    private ContactListPresenter mPresenter;

    public static ContactListFragment newInstance(boolean param1) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ContactListPresenter(getContext());
        mPresenter.setOnDataUpdateListener(this);
        EventBus.getDefault().register(this);
        registerLoginReceiver();
    }

    private void registerLoginReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGIN_SUCCESS_BROADCAST);
        getActivity().registerReceiver(loginReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mRosterView = (EasyRecyclerView) root.findViewById(R.id.roster_easy_recycler_view);
        initRoster();
        return root;
    }

    BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
//            mPresenter.init();
        }
    };

    private void initRoster() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRosterView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getContext(),0.5f), Util.dip2px(getContext(),72),0);
        itemDecoration.setDrawLastItem(false);
        mRosterView.addItemDecoration(itemDecoration);
        mRosterAdapter = new RosterAdapter(getContext());
        onUpdateData(MyApp.gCurrentUser.getMyFriends(), false);
        mRosterView.setAdapter(mRosterAdapter);
        // 添加非重用view部分的组件
        mRosterAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View v = inflater.inflate(R.layout.header_item, null);
                v.findViewById(R.id.new_friend).setOnClickListener(mPresenter);
                v.findViewById(R.id.chat_group).setOnClickListener(mPresenter);
                return v;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
//
//        // StickyHeader
//        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(getContext(), mRosterAdapter.getAllData()));
//        decoration.setIncludeHeader(false);
//        mRosterView.addItemDecoration(decoration);
    }

    /**
     * 当presenter中有friend数据更新时调用
     * @param datas
     */
    @Override
    public void onUpdateData(List<FriendModel> datas, boolean moveCursor) {
        Collections.sort(datas);
//        mRosterAdapter.clear();
        mRosterAdapter.addAll(datas);
        // StickyHeader
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(getContext(), mRosterAdapter.getAllData()));
        mRosterView.addItemDecoration(decoration);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTipMessage(TipMessage tipMessage) {
        mRosterAdapter.updateData(tipMessage);
    }

    public static class TipMessage {
        public String jid;
        public String info;

        public TipMessage(String jid, String info) {
            this.jid = jid;
            this.info = info;
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(loginReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
