package cn.dazhou.railway.im.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.jude.rollviewpager.Util;

import java.util.List;

import cn.dazhou.railway.R;
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
    }

    @Override
    public void onResume() {
        mPresenter.init();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_list, container, false);
        mRosterView = (EasyRecyclerView) root.findViewById(R.id.roster_easy_recycler_view);
        initRoster();
        return root;
    }

    private void initRoster() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRosterView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(getContext(),0.5f), Util.dip2px(getContext(),72),0);
        itemDecoration.setDrawLastItem(false);
        mRosterView.addItemDecoration(itemDecoration);
        mRosterAdapter = new RosterAdapter(getContext());
        mRosterView.setAdapter(mRosterAdapter);
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

        // StickyHeader
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(getContext()));
        decoration.setIncludeHeader(false);
        mRosterView.addItemDecoration(decoration);
    }

    /**
     * 当presenter中有friend数据更新时调用
     * @param datas
     */
    @Override
    public void onUpdateData(List<FriendModel> datas) {
        mRosterAdapter.clear();
        mRosterAdapter.addAll(datas);
    }
}