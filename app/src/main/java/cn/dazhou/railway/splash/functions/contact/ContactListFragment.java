package cn.dazhou.railway.splash.functions.contact;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import cn.dazhou.database.FriendModel;
import cn.dazhou.im.acpect.db.FriendDbApi;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.splash.functions.BaseFragment;

public class ContactListFragment extends BaseFragment implements ContactListContract.View {
    private static final String ARG_PARAM1 = "param1";

    private EasyRecyclerView mRosterView;
    private RosterAdapter mRosterAdapter;
    private ContactListContract.Presenter mPresenter;

    private TextView mRequestTipTx;
    private View mRootView;

    private StickyHeaderDecoration mDecoration;

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
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        mPresenter = new ContactListPresenter(getContext(), this);
        mRosterAdapter = new RosterAdapter(getContext());
        mRosterAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View v = inflater.inflate(R.layout.header_item, null);
                v.findViewById(R.id.new_friend).setOnClickListener(mPresenter);
                v.findViewById(R.id.chat_group).setOnClickListener(mPresenter);
                mRequestTipTx = (TextView) v.findViewById(R.id.tx_request_count);

                return v;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
            mRosterView = (EasyRecyclerView) mRootView.findViewById(R.id.roster_easy_recycler_view);
            initRoster();
        } else {
            // 同一个parent不能添加相同的view，因此要先移除
            ViewGroup parent = (ViewGroup) mRosterView.getParent();
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        if (mRequestTipTx != null) {
            mPresenter.updateRequestTipSate();
        }
        super.onResume();
    }

    private void initRoster() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRosterView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getContext(), 0.5f), Util.dip2px(getContext(), 72), 0);
        itemDecoration.setDrawLastItem(false);
        mRosterView.addItemDecoration(itemDecoration);
        onUpdateData(MyApp.gCurrentUser.getMyFriends());
        mRosterView.setAdapter(mRosterAdapter);
    }

    /**
     * 当presenter中有friend数据更新时调用
     *
     * @param datas
     */
    public void onUpdateData(List datas) {
        Collections.sort(datas);
        mRosterAdapter.clear();
        mRosterAdapter.addAll(datas);
        // StickyHeader
        if (mDecoration != null) {
            mRosterView.removeItemDecoration(mDecoration);
        }
        mDecoration = new StickyHeaderDecoration(new StickyHeaderAdapter(getContext(), mRosterAdapter.getAllData()));
        mRosterView.addItemDecoration(mDecoration);
    }

    @Override
    public void hideRequestCountTip() {
        if (mRequestTipTx != null) {
            mRequestTipTx.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRequestCountTip(String s) {
        if (mRequestTipTx != null) {
            mRequestTipTx.setVisibility(View.VISIBLE);
            mRequestTipTx.setText(s);
        }
    }

    /**
     * 更新当前好友最后一条消息
     *
     * @param tipMessage
     * @see cn.dazhou.railway.im.service.IMChatService#incomingChatMessageListener
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTipMessage(TipMessage tipMessage) {
        mRosterAdapter.updateData(tipMessage);
    }

    /**
     * 1、更新当前好友未读消息量
     * 2、置顶正在聊天的对象
     *
     * @param friendModel
     * @see cn.dazhou.railway.im.service.IMChatService#incomingChatMessageListener
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTipMessage(FriendDbApi friendModel) {
        mRosterAdapter.markNewMsg((FriendModel) friendModel);

        if (mDecoration != null) {
            mRosterView.removeItemDecoration(mDecoration);
        }
        mDecoration = new StickyHeaderDecoration(new StickyHeaderAdapter(getContext(), mRosterAdapter.getAllData()));
        mRosterView.addItemDecoration(mDecoration);

        mRosterAdapter.showMsgCount((FriendModel) friendModel);
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
        mPresenter.destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
