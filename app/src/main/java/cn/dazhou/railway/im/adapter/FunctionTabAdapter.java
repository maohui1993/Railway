package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.dazhou.pagerslidingtabstrip.PagerSlidingTabStrip;
import cn.dazhou.railway.R;

/**
 * Created by hooyee on 2017/5/31.
 */

public class FunctionTabAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    List<Fragment> fragments;
    String[] titles;
    Context context;
    int[] icons;
    public FunctionTabAdapter(Context context, FragmentManager fm, List fragments, String[] titles, @DrawableRes int[] icons) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        this.context = context;
        this.icons = icons;
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

    @Override
    public int getPageIconResId(int position) {
        return icons[position];
    }
}
