package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.fragment.LastWeekReportManagerFragment;
import com.aorise.weeklyreport.activity.fragment.NextWeekReprotManagerFragment;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.MenuPopup;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.databinding.ActivityMemberManagerBinding;

import java.util.ArrayList;
import java.util.List;

public class MemberManagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityMemberManagerBinding mViewDataBinding;

    private Class mFragmentArray[] = {NextWeekReprotManagerFragment.class, LastWeekReportManagerFragment.class,};
    private static final String TITLE_ONE = "本周周报总结";
    private static final String TITLE_TWO = "下周周报计划";
    private String mFragmentTitle[] = {TITLE_ONE, TITLE_TWO};
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private LastWeekReportManagerFragment mLastReportFragment;
    private NextWeekReprotManagerFragment mNextReportFragment;

    private MenuPopup menuPopup;
    private List<String> weeks = new ArrayList<>();
    private String currentWeeks = "";

    private boolean addPlan = false;

    private int userId = 1;
    private int projectId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_member_manager);
        mViewDataBinding.managerActionbar.actionBarTitle.setText("第" + TimeUtil.getInstance().getDayofWeek() + "周");
        mViewDataBinding.managerActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        weeks = TimeUtil.getInstance().getHistoryWeeks();
        menuPopup = new MenuPopup(this, weeks.size() - 1, this);
        menuPopup.setPopupGravity(Gravity.BOTTOM);
        mViewDataBinding.managerActionbar.actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.managerActionbar.actionBarTitle);
            }
        });
        mViewDataBinding.managerActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberManagerActivity.this.finish();
            }
        });
        getDefaultIntent();
        initFragment();
        initViewPager();
    }

    private void getDefaultIntent() {
        Intent mIntent = getIntent();
        userId = mIntent.getIntExtra("userId", 1);
        projectId = mIntent.getIntExtra("projectId", 1);
        LogT.d(" getDefaultIntent userId is " + userId + " projectId is  " + projectId);
    }

    private void initFragment() {
        mLastReportFragment = LastWeekReportManagerFragment.newInstance(userId, projectId);
        mNextReportFragment = NextWeekReprotManagerFragment.newInstance(userId, projectId);
        mFragmentList.add(mLastReportFragment);
        mFragmentList.add(mNextReportFragment);

        for (int i = 0; i < mFragmentArray.length; i++) {
            mViewDataBinding.managerTabHost.addTab(mViewDataBinding.managerTabHost.newTab().setText(mFragmentTitle[i]));
        }
        mViewDataBinding.managerTabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(TITLE_ONE)) {
                    mViewDataBinding.managerViewpager.setCurrentItem(0);
                    addPlan = true;
                } else if (tab.getText().equals(TITLE_TWO)) {
                    addPlan = false;
                    mViewDataBinding.managerViewpager.setCurrentItem(1);
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

    private void initViewPager() {
        mViewDataBinding.managerViewpager.addOnPageChangeListener(this);
        mViewDataBinding.managerViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewDataBinding.managerViewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        addPlan = i == 1;
        mViewDataBinding.managerTabHost.setScrollPosition(i, 0, false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void selectPosistion(int position) {
        LogT.d("当前选择了。。。。" + position);
        mViewDataBinding.managerActionbar.actionBarTitle.setText(weeks.get(position));

        currentWeeks = weeks.get(position);
        if (addPlan) {
            if (mLastReportFragment != null) {
                mLastReportFragment.updateManagerList(position + 1);
            } else {
                mLastReportFragment = new LastWeekReportManagerFragment();
                mFragmentList.add(mLastReportFragment);
                mLastReportFragment.updateManagerList(position + 1);
            }
        } else {
            if (mNextReportFragment != null) {
                mNextReportFragment.updateManagerList(position + 1);
            } else {
                mNextReportFragment = new NextWeekReprotManagerFragment();
                mFragmentList.add(mNextReportFragment);
                mNextReportFragment.updateManagerList(position + 1);
            }
        }
    }
}
