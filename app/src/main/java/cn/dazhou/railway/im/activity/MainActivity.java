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

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.railway.R;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.adapter.ChatPagerAdapter;
import cn.dazhou.railway.im.adapter.RosterAdapter;
import cn.dazhou.railway.im.presenter.ChatPresenter;
import cn.dazhou.railway.im.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {
    private static final String DATA_KEY = "jid";

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;

    private EasyRecyclerView mRosterView;
    private ChatPresenter mPresenter;
    private List<View> mViewList = new ArrayList<View>();
    private MainPresenter mPrensenter;
    private RosterAdapter mRosterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRoster();

        mViewList.add(mRosterView);
//        mViewList.add(mRosterView);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[0]));//添加tab选项卡
//        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[1]));

        ChatPagerAdapter mAdapter = new ChatPagerAdapter(mViewList, mTitles);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。

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

//        IMLauncher.addFriend(Constants.SERVER_IP);
        Roster roster = IMLauncher.getRoster();
        Set<RosterEntry> entries = roster.getEntries();
        mRosterAdapter.addAll(entries);
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(DATA_KEY, data);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        IMLauncher.disconnect();
        super.onBackPressed();
    }
}
