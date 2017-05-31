package cn.dazhou.railway.im.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.dazhou.railway.im.fragment.BaseFragment;

/**
 * Created by hooyee on 2017/5/31.
 */

public class FunctionTabAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    String[] titles;
    public FunctionTabAdapter(FragmentManager fm, List fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
