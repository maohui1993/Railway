package cn.dazhou.railway.splash.functions.contact;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import cn.dazhou.im.acpect.db.FriendDbApi;
import cn.dazhou.railway.MyApp;
import cn.dazhou.railway.R;
import cn.dazhou.railway.splash.functions.BaseFragment;

import static android.view.View.GONE;

public class ContactListFragment extends BaseFragment implements ContactListContract.View {
    private static final String ARG_PARAM1 = "param1";

    private EasyRecyclerView mRosterView;
    private RosterAdapter mRosterAdapter;
    private ContactListContract.Presenter mPresenter;

    private TextView mRequestTipTx;
    private View mRootView;
    private ImageView mTipImage;

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
                v.findViewById(R.id.message_list).setOnClickListener(mPresenter);
                mRequestTipTx = (TextView) v.findViewById(R.id.tx_request_count);
                mTipImage = (ImageView) v.findViewById(R.id.iv_tip);
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
        if (mTipImage != null) {
            mPresenter.updateMessageTip();
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
            mRequestTipTx.setVisibility(GONE);
        }
    }

    @Override
    public void showRequestCountTip(String s) {
        if (mRequestTipTx != null) {
            mRequestTipTx.setVisibility(View.VISIBLE);
            mRequestTipTx.setText(s);
        }
    }

    @Override
    public void showMessageTip() {
        if (mTipImage != null) {
            mTipImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideMessageTip() {
        if (mTipImage != null) {
            mTipImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(ContactListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }
}
