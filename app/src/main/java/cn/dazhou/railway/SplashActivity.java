package cn.dazhou.railway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.im.util.Tool;
import cn.dazhou.maputil.MapLauncher;
import cn.dazhou.pagerslidingtabstrip.PagerSlidingTabStrip;
import cn.dazhou.railway.config.Constants;
import cn.dazhou.railway.im.activity.LoginActivity;
import cn.dazhou.railway.im.activity.MyselfInfoActivity;
import cn.dazhou.railway.im.activity.SettingActivity;
import cn.dazhou.railway.im.adapter.FunctionTabAdapter;
import cn.dazhou.railway.im.broadcast.NetworkReceiver;
import cn.dazhou.railway.im.fragment.BaseFragment;
import cn.dazhou.railway.im.fragment.ContactListFragment;
import cn.dazhou.railway.im.fragment.HomeFragment;
import cn.dazhou.railway.im.fragment.SettingFragment;
import cn.dazhou.railway.im.fragment.WorkFragment;
import cn.dazhou.railway.util.LogUtil;
import cn.dazhou.railway.util.SharedPreferenceUtil;

public class SplashActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;
    @BindView(R.id.tabs_1)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    View headerLayout;
//    @BindView(R.id.tx_name)
//    TextView mNameTx;
//    @BindView(R.id.tx_jid)
//    TextView mJidTx;
    private List<BaseFragment> fragments = new ArrayList();

    private int[] icons = {
            R.drawable.home,
            R.drawable.work,
            R.drawable.contacts,
            R.drawable.set
    };

    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        MyApp.addActivity(this);
        MapLauncher.init(getApplicationContext());
        Tool.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Tool.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        Tool.checkPermission(this, Manifest.permission.RECORD_AUDIO);
        Tool.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        Tool.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        Tool.checkPermission(this, Manifest.permission.CAMERA);
        Tool.checkPermission(this, Manifest.permission.VIBRATE);
        Tool.checkPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
//        Tool.checkPermission(this);
        LogUtil.init();
        ButterKnife.bind(this);
        mPresenter = new SplashPresenter(this);
        fragments.add(HomeFragment.newInstance(false));
        fragments.add(WorkFragment.newInstance(false));
        fragments.add(ContactListFragment.newInstance(false));
        fragments.add(SettingFragment.newInstance(false));

        FunctionTabAdapter mAdapter = new FunctionTabAdapter(this, getSupportFragmentManager(), fragments, mTitles, icons);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.clearOnPageChangeListeners();
        pagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = fragments.get(position);
                toolbar.setTitle(mTitles[position]);
                if(fragment.isMustLogin() && MyApp.gCurrentUser == null) {
                    fragment.requestLogin();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pagerSlidingTabStrip.setViewPager(mViewPager);
        // 抽屉式导航栏设置
        toolbar.setTitle(mTitles[0]);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_splash1);
        navigationView.setNavigationItemSelectedListener(this);

        receiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    NetworkReceiver receiver;


    void openUserInfo(View v) {
        Toast.makeText(this, "image", Toast.LENGTH_SHORT).show();
        MyselfInfoActivity.startItself(this);
    }

    @Override
    protected void onResume() {
        if (MyApp.gCurrentUser != null) {
            TextView mNameTx = (TextView) headerLayout.findViewById(R.id.tx_name);
            mNameTx.setText(MyApp.gCurrentUser.getNickName());
            TextView mJidTx = (TextView) headerLayout.findViewById(R.id.tx_jid);
            mJidTx.setText(MyApp.gCurrentUser.getUsername());
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                SettingActivity.startItself(this);
                break;
            case R.id.menu_barcode:
                mPresenter.parseQRcode();
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        SharedPreferenceUtil.putString(this, Constants.LATEST_LOGIN_JID, MyApp.gCurrentUsername);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }
}
