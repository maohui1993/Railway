package cn.dazhou.railway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import cn.dazhou.pagerslidingtabstrip.PagerSlidingTabStrip;
import cn.dazhou.railway.im.activity.LoginActivity;
import cn.dazhou.railway.im.activity.SettingActivity;
import cn.dazhou.railway.im.adapter.FunctionTabAdapter;
import cn.dazhou.railway.im.fragment.BaseFragment;
import cn.dazhou.railway.im.fragment.ContactListFragment;
import cn.dazhou.railway.im.fragment.HomeFragment;
import cn.dazhou.railway.im.fragment.SettingFragment;
import cn.dazhou.railway.im.fragment.WorkFragment;
import cn.dazhou.railway.im.presenter.SplashPresenter;
import cn.dazhou.railway.util.LogUtil;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.content)
    ViewGroup content;
    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;
    @BindView(R.id.tabs_1)
    PagerSlidingTabStrip pagerSlidingTabStrip;
    private List<BaseFragment> fragments = new ArrayList();
    private SplashPresenter mPresenter;

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
        Tool.checkPermission(this, Manifest.permission.CAMERA);
        Tool.checkPermission(this, Manifest.permission.VIBRATE);
//        Tool.checkPermission(this);
        LogUtil.init();
        ButterKnife.bind(this);
        mPresenter = new SplashPresenter(this);
        toolbar.setTitle(mTitles[0]);
        setSupportActionBar(toolbar);
        fragments.add(HomeFragment.newInstance(false));
        fragments.add(WorkFragment.newInstance(false));
        fragments.add(ContactListFragment.newInstance(true));
        fragments.add(SettingFragment.newInstance(false));

        FunctionTabAdapter mAdapter = new FunctionTabAdapter(this, getSupportFragmentManager(), fragments, mTitles);
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
            case R.id.menu_barcode:
                mPresenter.parseQRcode();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    public static void startItself(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }
}
