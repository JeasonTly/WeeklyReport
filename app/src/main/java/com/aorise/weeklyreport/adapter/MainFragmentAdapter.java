package com.aorise.weeklyreport.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/26.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction ;
    public MainFragmentAdapter(FragmentManager fm,List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Fragment fragment = (Fragment) object;

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        mCurTransaction.remove(fragment);
    }
}
