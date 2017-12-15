package com.hrmp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by Ly on 2017/5/9.
 */

public class PageContentFragementAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    List<Fragment> fragmentList;
    public PageContentFragementAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return   fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
