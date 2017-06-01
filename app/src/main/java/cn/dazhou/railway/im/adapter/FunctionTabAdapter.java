package cn.dazhou.railway.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

import com.astuetz.PagerSlidingTabStrip;

import java.util.List;

import cn.dazhou.railway.R;
import cn.dazhou.railway.im.fragment.BaseFragment;

/**
 * Created by hooyee on 2017/5/31.
 */

public class FunctionTabAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    List<Fragment> fragments;
    String[] titles;
    Context context;
    public FunctionTabAdapter(Context context, FragmentManager fm, List fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        this.context = context;
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
        return R.mipmap.ic_launcher;
    }
}
