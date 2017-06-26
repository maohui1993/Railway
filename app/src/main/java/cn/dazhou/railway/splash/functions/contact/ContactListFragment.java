package cn.dazhou.railway.splash.functions.contact;

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
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.splash.functions.BaseFragment;
import cn.dazhou.railway.splash.functions.StickyHeaderAdapter;
import cn.dazhou.railway.util.IMUtil;

public class ContactListFragment extends BaseFragment implements ContactListContract.View {
    private static final String ARG_PARAM1 = "param1";

    private EasyRecyclerView mRosterView;
    private RosterAdapter mRosterAdapter;
    private ContactListContract.Presenter mPresenter;

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
        mPresenter = new ContactListPresenter(getContext(), this);
        EventBus.getDefault().register(this);
        registerLoginReceiver();
    }

    private void registerLoginReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.LOGIN_SUCCESS_BROADCAST);
        getContext().registerReceiver(loginReceiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_FROM_SERVER_BROADCAST);
        getContext().registerReceiver(updateReceiver, intentFilter1);
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
            onUpdateData(MyApp.gCurrentUser.getMyFriends());
        }
    };

    BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            IMUtil.updateFriendFromServer(MyApp.gCurrentUser);
            onUpdateData(MyApp.gCurrentUser.getMyFriends());
        }
    };

    private void initRoster() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRosterView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getContext(),0.5f), Util.dip2px(getContext(),72),0);
        itemDecoration.setDrawLastItem(false);
        mRosterView.addItemDecoration(itemDecoration);
        mRosterAdapter = new RosterAdapter(getContext());
        onUpdateData(MyApp.gCurrentUser.getMyFriends());
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
    }

    /**
     * 当presenter中有friend数据更新时调用
     * @param datas
     */
    public void onUpdateData(List<FriendModel> datas) {
        Collections.sort(datas);
        mRosterAdapter.addAll(datas);
        // StickyHeader
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(getContext(), mRosterAdapter.getAllData()));
        mRosterView.addItemDecoration(decoration);
    }

    /**
     * 更新当前好友最后一条消息
     * @see cn.dazhou.railway.im.service.IMChatService#incomingChatMessageListener
     * @param tipMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTipMessage(TipMessage tipMessage) {
        mRosterAdapter.updateData(tipMessage);
    }

    /**
     * 更新当前好友未读消息量
     * @see cn.dazhou.railway.im.service.IMChatService#incomingChatMessageListener
     * @param friendModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTipMessage(FriendModel friendModel) {
        mRosterAdapter.updateData(friendModel);
    }

    @Override
    public void setPresenter(ContactListContract.Presenter presenter) {
        mPresenter = presenter;
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
        getContext().unregisterReceiver(loginReceiver);
        getContext().unregisterReceiver(updateReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
