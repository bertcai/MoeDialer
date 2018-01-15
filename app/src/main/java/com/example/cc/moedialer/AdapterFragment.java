package com.example.cc.moedialer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 18-1-10.
 */

public class AdapterFragment extends FragmentPagerAdapter {
    private List<Fragment> mFragment;
    ArrayList<String> titleList = new ArrayList<>();

    public AdapterFragment(FragmentManager fragmentManager, List<Fragment> mFragment,
                           ArrayList<String> titleList) {
        super(fragmentManager);
        this.mFragment = mFragment;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
