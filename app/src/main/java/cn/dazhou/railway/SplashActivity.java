package cn.dazhou.railway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.dazhou.im.util.Tool;
import cn.dazhou.maputil.MapLauncher;
import cn.dazhou.railway.im.activity.LoginActivity;
import cn.dazhou.railway.im.activity.SettingActivity;
import cn.dazhou.railway.im.adapter.FunctionTabAdapter;
import cn.dazhou.railway.im.fragment.BaseFragment;
import cn.dazhou.railway.im.fragment.ContactListFragment;
import cn.dazhou.railway.im.fragment.SettingFragment;
import cn.dazhou.railway.util.LogUtil;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;

    private List<BaseFragment> fragments = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapLauncher.init(getApplicationContext());
        ViewGroup content = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_splash, null);
        setContentView(content);
        Tool.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Tool.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        Tool.checkPermission(this, Manifest.permission.RECORD_AUDIO);
        Tool.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        Tool.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//        Tool.checkPermission(this);
        LogUtil.init();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[0]));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[1]));//添加tab选项卡

        fragments.add(SettingFragment.newInstance(false));
        // 必须登录才能正常使用
        fragments.add(ContactListFragment.newInstance(true));

        FunctionTabAdapter mAdapter = new FunctionTabAdapter(getSupportFragmentManager(), fragments, mTitles);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                BaseFragment fragment = fragments.get(tab.getPosition());
                if(fragment.isMustLogin() && MyApp.gCurrentUser == null) {
                    fragment.requestLogin();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_splash_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                SettingActivity.startItself(this);
                break;
        }
        return true;
    }

    @OnClick(R.id.btn_login)
    void login() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.bt_position)
    void position() {
        MapLauncher.loadMap(content);
        MapLauncher.getPosition();
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }
}
