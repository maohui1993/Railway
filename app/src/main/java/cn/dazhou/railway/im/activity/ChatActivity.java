package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.view.ChatContentView;
import cn.dazhou.im.view.RosterView;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.adapter.ChatPagerAdapter;
import cn.dazhou.railway.im.presenter.ChatPresenter;

/**
 * 启动时需要知道是与谁聊天，故启动的时候要带一个data值传入。
 */
public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;

    private ChatContentView mChatContentView;
    private RosterView mRosterView;
    private ChatPresenter mPresenter;
    private List<View> mViewList = new ArrayList<View>();
    /**
     * 正在chat的用户jid
     */
    private String mJid;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mJid = getIntent().getStringExtra("jid");
//        mChatContentView.setOnSendListener(new ChatContentView.OnSendListener() {
//            @Override
//            public void onSend(String info) {
//                IMLauncher.chatWith(mJid, info);
//            }
//        });

        LayoutInflater mInflater = LayoutInflater.from(this);
        mChatContentView = (ChatContentView) mInflater.inflate(R.layout.tab_message, null);
        mRosterView = (RosterView) mInflater.inflate(R.layout.tab_roster, null);

        //添加页卡视图
        mViewList.add(mChatContentView);
        mViewList.add(mRosterView);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[0]));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[1]));

        ChatPagerAdapter mAdapter = new ChatPagerAdapter(mViewList, mTitles);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。

        Roster roster = IMLauncher.getRoster();
        Set<RosterEntry> entries = roster.getEntries();
        mRosterView.updateRosterData(new ArrayList(entries));

    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("jid", data);
        context.startActivity(intent);
    }
}
