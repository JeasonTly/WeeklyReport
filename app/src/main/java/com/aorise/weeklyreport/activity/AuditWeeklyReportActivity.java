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
import com.aorise.weeklyreport.base.MenuPopup;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.databinding.ActivityAuditWeeklyReportBinding;

import java.util.ArrayList;
import java.util.List;

public class AuditWeeklyReportActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {

    private ActivityAuditWeeklyReportBinding mViewDataBinding;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = {ConclusionFragment.class, PlanFragment.class};
    private String FragmentTitle[] = {"本周工作总结", "下周工作计划"};
    private boolean addPlan = false;
    private int projectId = -1;
    private int userId = -1;
    private int weeks = -1;
    private List<String> weeksList = new ArrayList<>();
    private MenuPopup menuPopup;
    private String userName;

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
        weeksList = TimeUtil.getInstance().getHistoryWeeks();
        menuPopup = new MenuPopup(this, weeksList.size() - 1, this);
        mViewDataBinding.auditActionbar.actionBarTitle.setText("第" + TimeUtil.getInstance().getDayofWeek() + "周");
        mViewDataBinding.auditActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        mViewDataBinding.auditActionbar.actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.auditActionbar.actionBarTitle);

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
        userName = mIntent.getStringExtra("userName");
        LogT.d(" project Id is " + projectId + " userId is " + userId + " weeks " + weeks);
    }

    private void initFragment() {
        mConclusionFragment = ConclusionFragment.newInstance(projectId, userId, weeks, true);
        mPlanFragment = PlanFragment.newInstance(projectId, userId, weeks, true);
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
                if (tab.getText().equals("下周工作计划")) {
                    mViewDataBinding.viewpager.setCurrentItem(1);
                    addPlan = true;
                } else if (tab.getText().equals("本周工作总结")) {
                    addPlan = false;
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
        addPlan = (i == 1);
        mViewDataBinding.auditTabHost.setScrollPosition(i, 0, false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void selectPosistion(int position) {
        LogT.d("当前选择了。。。。" + position);
        mViewDataBinding.auditActionbar.actionBarTitle.setText(weeksList.get(position));

        //weeks = weeksList.get(position);
        if (addPlan) {
            if (mPlanFragment != null) {
                mPlanFragment.update(position + 1);
            } else {
                mPlanFragment = new PlanFragment();
                mFragmentList.add(mPlanFragment);
                mPlanFragment.update(position + 1);
            }
        } else {
            if (mConclusionFragment != null) {
                mConclusionFragment.update(position + 1);
            } else {
                mConclusionFragment = new ConclusionFragment();
                mFragmentList.add(mConclusionFragment);
                mConclusionFragment.update(position + 1);
            }
        }
    }
}
