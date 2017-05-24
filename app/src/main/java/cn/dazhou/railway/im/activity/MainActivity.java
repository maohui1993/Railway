package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.jude.rollviewpager.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.adapter.ChatPagerAdapter;
import cn.dazhou.railway.im.adapter.RosterAdapter;
import cn.dazhou.railway.im.adapter.StickyHeaderAdapter;
import cn.dazhou.railway.im.db.FriendModel;
import cn.dazhou.railway.im.listener.OnDataUpdateListener;
import cn.dazhou.railway.im.presenter.MainPresenter;
import cn.dazhou.railway.im.service.IMChatService;
import cn.dazhou.railway.im.service.IMFriendRequestService;

public class MainActivity extends AppCompatActivity implements OnDataUpdateListener<FriendModel>{
    private static final String DATA_KEY = "jid";

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;

    private EasyRecyclerView mRosterView;
    private List<View> mViewList = new ArrayList<View>();
    private MainPresenter mPresenter;
    private RosterAdapter mRosterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("通讯录");
        ButterKnife.bind(this);
        initRoster();
        mPresenter = new MainPresenter(this);
        mPresenter.setOnDataUpdateListener(this);
        mPresenter.init();

        mViewList.add(mRosterView);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[0]));//添加tab选项卡

        ChatPagerAdapter mAdapter = new ChatPagerAdapter(mViewList, mTitles);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        IMChatService.startItself(this);
        IMFriendRequestService.startItself(this);
    }

    private void initRoster() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        mRosterView = (EasyRecyclerView) mInflater.inflate(R.layout.roster_view, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRosterView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);
        itemDecoration.setDrawLastItem(false);
        mRosterView.addItemDecoration(itemDecoration);
        mRosterAdapter = new RosterAdapter(this);
        mRosterView.setAdapter(mRosterAdapter);
        mRosterAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
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
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(new StickyHeaderAdapter(this));
        decoration.setIncludeHeader(false);
        mRosterView.addItemDecoration(decoration);

        // 添加测试用户好友关系
//        IMLauncher.addFriend(Constants.SERVER_IP);
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(DATA_KEY, data);
        context.startActivity(intent);
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
