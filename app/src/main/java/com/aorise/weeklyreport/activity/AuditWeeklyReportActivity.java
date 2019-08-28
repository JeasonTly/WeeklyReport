package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.activity.fragment.ConclusionFragment;
import com.aorise.weeklyreport.activity.fragment.PlanFragment;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.databinding.ActivityAuditWeeklyReportBinding;
import com.aorise.weeklyreport.view.MenuPopup;

import java.util.ArrayList;
import java.util.List;

public class AuditWeeklyReportActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {

    private ActivityAuditWeeklyReportBinding mViewDataBinding;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = {ConclusionFragment.class, PlanFragment.class};
    private String FragmentTitle[] = {"本周总结", "下周计划"};
    private int projectId = -1;
    private int userId = -1;
    private int weeks = -1;
    private int totalweek = -1;
    private boolean canAudit = true;
    private List<String> weeksList = new ArrayList<>();
    private MenuPopup menuPopup;
    private ConclusionFragment mConclusionFragment;
    private PlanFragment mPlanFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_audit_weekly_report);
        WRApplication.getInstance().addActivity(this);
        initGetIntent();
        initFragment();
        initTabHost();
        initViewPager();

        //mViewDataBinding.auditActionbar.actionBarTitle.setText(userName + "的个人周报");
        totalweek = TimeUtil.getInstance().getDayofWeek();
        weeksList = TimeUtil.getInstance().getHistoryWeeks();
        menuPopup = new MenuPopup(this, 0, this);
        mViewDataBinding.auditActionbar.actionBarTitle.setText("第" + TimeUtil.getInstance().getDayofWeek() + "周");
        mViewDataBinding.auditActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        mViewDataBinding.auditActionbar.actionBarTitleArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.auditActionbar.actionBarTitleArea);

            }
        });

        mViewDataBinding.auditActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuditWeeklyReportActivity.this.finish();
            }
        });
    }

    private void initGetIntent() {
        Intent mIntent = getIntent();
        projectId = mIntent.getIntExtra("projectId", -1);
        userId = mIntent.getIntExtra("userId", -1);
        weeks = mIntent.getIntExtra("weeks", -1);
        canAudit = mIntent.getBooleanExtra("canAudit", true);

        LogT.d(" project Id is " + projectId + " userId is " + userId + " weeks " + weeks + " 是否可以编辑周报(默认为true)" + canAudit);
    }

    private void initFragment() {
        mConclusionFragment = ConclusionFragment.newInstance(projectId, userId, weeks, true, canAudit);
        mPlanFragment = PlanFragment.newInstance(projectId, userId, weeks, true, canAudit);
        mFragmentList.add(mConclusionFragment);
        mFragmentList.add(mPlanFragment);
    }

    private void initTabHost() {
        for (int i = 0; i < FragmentArray.length; i++) {
            mViewDataBinding.auditTabHost.addTab(mViewDataBinding.auditTabHost.newTab().setText(FragmentTitle[i]));
        }
        mViewDataBinding.auditTabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("下周计划")) {
                    mViewDataBinding.viewpager.setCurrentItem(1);
                } else if (tab.getText().equals("本周总结")) {
                    mViewDataBinding.viewpager.setCurrentItem(0);
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
        mViewDataBinding.viewpager.addOnPageChangeListener(this);
        mViewDataBinding.viewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewDataBinding.viewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        LogT.d("当前选择index为 " + i);
        mViewDataBinding.auditTabHost.setScrollPosition(i, 0, false);
        if(i == 1) {
            if (mPlanFragment != null) {
                mPlanFragment.update(weeks);
            }
        }else {
            if (mConclusionFragment != null) {
                mConclusionFragment.update(weeks);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void selectPosistion(int position) {
        LogT.d("当前选择了。。。。" + position);
        mViewDataBinding.auditActionbar.actionBarTitle.setText(weeksList.get(totalweek - position - 1));

        //weeks = weeksList.get(position);
        weeks = position + 1;
        // if (addPlan) {
        if (mPlanFragment != null) {
            mPlanFragment.update(weeks);
        } else {
            mPlanFragment = PlanFragment.newInstance(projectId, userId, weeks, true, canAudit);
            mFragmentList.add(mPlanFragment);
            mPlanFragment.update(weeks);
        }
        // } else {
        if (mConclusionFragment != null) {
            mConclusionFragment.update(position + 1);
        } else {
            mConclusionFragment = ConclusionFragment.newInstance(projectId, userId, weeks, true, canAudit);
            mFragmentList.add(mConclusionFragment);
            mConclusionFragment.update(position + 1);
        }
        // }
    }
}
