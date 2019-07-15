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
import com.aorise.weeklyreport.databinding.ActivityAuditWeeklyReportBinding;

import java.util.ArrayList;
import java.util.List;

public class AuditWeeklyReportActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ActivityAuditWeeklyReportBinding mViewDataBinding;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = {ConclusionFragment.class, PlanFragment.class};
    private String FragmentTitle[] = {"本周工作总结", "下周工作计划"};
    private boolean addPlan = false;
    private int projectId = 3;
    private int userId = 2;
    private int weeks = 28;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_audit_weekly_report);
        WRApplication.getInstance().addActivity(this);
        initGetIntent();
        initFragment();
        initTabHost();
        initViewPager();
        mViewDataBinding.auditActionbar.actionBarTitle.setText(userName + "的个人周报");
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
        mFragmentList.add(ConclusionFragment.newInstance(projectId, userId, weeks,true));
        mFragmentList.add(PlanFragment.newInstance(projectId, userId, weeks,true));
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
}
