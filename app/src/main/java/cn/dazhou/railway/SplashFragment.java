package cn.dazhou.railway;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.dazhou.pagerslidingtabstrip.PagerSlidingTabStrip;
import cn.dazhou.railway.fragment.BaseFragment;
import cn.dazhou.railway.fragment.ContactListFragment;
import cn.dazhou.railway.fragment.HomeFragment;
import cn.dazhou.railway.fragment.SettingFragment;
import cn.dazhou.railway.fragment.WorkFragment;
import cn.dazhou.railway.im.adapter.FunctionTabAdapter;
import cn.dazhou.railway.im.friend.info.MyselfInfoActivity;

/**
 * Created by hooyee on 2017/6/26.
 */

public class SplashFragment extends Fragment implements SplashContract.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindArray(R.array.titles)
    String[] mTitles;
    @BindView(R.id.pager_tabs)
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    View mHeaderLayout;

    private SplashContract.Presenter mPresenter;
    private List<BaseFragment> mTabFragments = new ArrayList();

    private int[] icons = {
            R.drawable.home,
            R.drawable.work,
            R.drawable.contacts,
            R.drawable.set
    };

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static SplashFragment newInstance() {

        Bundle args = new Bundle();

        SplashFragment fragment = new SplashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_content_splash, container, false);
        ButterKnife.bind(this, root);


        mTabFragments.add(HomeFragment.newInstance(false));
        mTabFragments.add(WorkFragment.newInstance(false));
        mTabFragments.add(ContactListFragment.newInstance(false));
        mTabFragments.add(SettingFragment.newInstance(false));

        FunctionTabAdapter mAdapter = new FunctionTabAdapter(getContext(), getFragmentManager(), mTabFragments, mTitles, icons);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.clearOnPageChangeListeners();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = mTabFragments.get(position);
                toolbar.setTitle(mTitles[position]);
                if (fragment.isMustLogin() && MyApp.gCurrentUser == null) {
                    fragment.requestLogin();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        initNavigationView();
        return root;
    }

    public void initNavigationView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mHeaderLayout = navigationView.inflateHeaderView(R.layout.nav_header_splash1);
        mHeaderLayout.findViewById(R.id.iv_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyselfInfoActivity.startItself(getContext());
            }
        });
        navigationView.setNavigationItemSelectedListener(mPresenter);
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return mDrawer;
    }

    @Override
    public void changeText() {
        if (MyApp.gCurrentUser != null) {
            TextView mNameTx = (TextView) mHeaderLayout.findViewById(R.id.tx_name);
            mNameTx.setText(MyApp.gCurrentUser.getNickName());
            TextView mJidTx = (TextView) mHeaderLayout.findViewById(R.id.tx_jid);
            mJidTx.setText(MyApp.gCurrentUser.getUsername());
        }
    }

    @Override
    public boolean canBack() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return false;
        } else {
            return true;
        }
    }
}
