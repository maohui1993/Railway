package cn.dazhou.railway.im.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.IMLauncher;
import cn.dazhou.im.view.ChatContentView;
import cn.dazhou.railway.R;
import cn.dazhou.railway.im.adapter.ChatPagerAdapter;

/**
 * 启动时需要知道是与谁聊天，故启动的时候要带一个data值传入。
 */
public class ChatActivity extends AppCompatActivity {

//    @BindView(R.id.chat_content_view)
//    ChatContentView mChatContentView;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;


    private List<View> mViewList = new ArrayList<View>();
    private List<String> mTitleList = new ArrayList<String>();
    /**
     * 正在chat的用户jid
     */
    private String mJid;

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
        View view1 = mInflater.inflate(R.layout.chat_content_view, null);
        View view2 = mInflater.inflate(R.layout.chat_content_view, null);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);

        //添加页卡标题
        mTitleList.add("消息");
        mTitleList.add("通讯录");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));


        ChatPagerAdapter mAdapter = new ChatPagerAdapter(mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        IMLauncher.showRoster();
    }

    public static void startItself(Context context, String data) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("jid", data);
        context.startActivity(intent);
    }
}
