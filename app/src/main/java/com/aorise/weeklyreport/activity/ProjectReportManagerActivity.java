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
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.activity.fragment.LastWeekReportManagerFragment;
import com.aorise.weeklyreport.activity.fragment.NextWeekReprotManagerFragment;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.databinding.ActivityMemberManagerBinding;
import com.aorise.weeklyreport.view.MenuPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目周报 查看界面
 */
public class ProjectReportManagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityMemberManagerBinding mViewDataBinding;
    /**
     * 两个fragment 一个总结LastWeekReportManagerFragment  一个计划NextWeekReprotManagerFragment
     */
    private Class mFragmentArray[] = {LastWeekReportManagerFragment.class, NextWeekReprotManagerFragment.class};
    private static final String TITLE_ONE = "本周总结";
    private static final String TITLE_TWO = "下周计划";
    private String mFragmentTitle[] = {TITLE_ONE, TITLE_TWO};
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private LastWeekReportManagerFragment mLastReportFragment;
    private NextWeekReprotManagerFragment mNextReportFragment;
    /**
     * 周数选择器
     */
    private MenuPopup menuPopup;
    /**
     * 总周数
     */
    private int totalWeeks = -1;
    /**
     * 周数列表
     */
    private List<String> weeksList = new ArrayList<>();
    /**
     * 当前周
     */
    private int currentWeekNumber = -1;

    /**
     * 是否为添加计划
     */
    private boolean addPlan = false;
    // private int type = -1;
    //private boolean isProjectLeader = false;
    /**
     * 用户ID
     */
    private int userId = 1;
    /**
     * 项目ID
     */
    private int projectId = -1;
    /**
     * 项目名称
     */
    private String projectName = "";

    /**
     * 是否为审核项目周报
     */
    private boolean auditProjectReport = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_member_manager);
        WRApplication.getInstance().addActivity(this);
        totalWeeks = TimeUtil.getInstance().getDayofWeek();
        currentWeekNumber = totalWeeks;
        mViewDataBinding.managerActionbar.actionBarTitle.setText("第" + currentWeekNumber + "周");
        mViewDataBinding.managerActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        mViewDataBinding.managerActionbar.actionMenu.setImageResource(R.drawable.xiafarenwu);
        weeksList = TimeUtil.getInstance().getHistoryWeeks();
        menuPopup = new MenuPopup(this, 0, this, null);
        menuPopup.setPopupGravity(Gravity.BOTTOM);
        mViewDataBinding.managerActionbar.actionBarTitleArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.managerActionbar.actionBarTitleArea);
            }
        });
        mViewDataBinding.managerActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectReportManagerActivity.this.finish();
            }
        });
        mViewDataBinding.managerActionbar.actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.setClass(ProjectReportManagerActivity.this, SettingsNextWeekPlanActivity.class);
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("projectName", projectName);
                mIntent.putExtra("weeks", currentWeekNumber);
                startActivity(mIntent);
            }
        });

        getDefaultIntent();
        initFragment();
        initViewPager();
    }

    /**
     * 获取默认Intent 跳转携带的参数信息
     */
    private void getDefaultIntent() {
        Intent mIntent = getIntent();
        userId = mIntent.getIntExtra("userId", 1);
        projectId = mIntent.getIntExtra("projectId", 1);
        projectName = mIntent.getStringExtra("projectName");
        auditProjectReport = mIntent.getBooleanExtra("isAudit", false);
        LogT.d(" getDefaultIntent userId is " + userId + " projectId is  " + projectId + " auditProjectReport " + auditProjectReport);
    }

    private void initFragment() {
        mLastReportFragment = LastWeekReportManagerFragment.newInstance(userId, projectId, TimeUtil.getInstance().getDayofWeek(), auditProjectReport);
        mNextReportFragment = NextWeekReprotManagerFragment.newInstance(userId, projectId, TimeUtil.getInstance().getDayofWeek(), auditProjectReport);
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
                    addPlan = false;
                } else if (tab.getText().equals(TITLE_TWO)) {
                    addPlan = true;
                    mViewDataBinding.managerViewpager.setCurrentItem(1);

                }
                mViewDataBinding.managerActionbar.actionMenu.setVisibility(addPlan ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getText().equals(TITLE_ONE)) {
                    mViewDataBinding.managerViewpager.setCurrentItem(0);
                    addPlan = false;
                } else if (tab.getText().equals(TITLE_TWO)) {
                    addPlan = true;
                    mViewDataBinding.managerViewpager.setCurrentItem(1);

                }
                mViewDataBinding.managerActionbar.actionMenu.setVisibility(addPlan ? View.VISIBLE : View.GONE);
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

        LogT.d(" update CurrentPage");
        if (!addPlan) {
            if (mLastReportFragment != null) {
                mLastReportFragment.updateManagerList(currentWeekNumber);
            }
        } else {
            if (mNextReportFragment != null) {
                mNextReportFragment.updateManagerList(currentWeekNumber);
            }
        }
        mViewDataBinding.managerActionbar.actionMenu.setVisibility(addPlan ? View.VISIBLE : View.GONE);
        mViewDataBinding.managerTabHost.setScrollPosition(i, 0, false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 周数选择器
     * 更新总结和计划的信息
     *
     * @param position
     */
    @Override
    public void selectPosistion(int position) {
        LogT.d("当前选择了。。。。" + position);
        mViewDataBinding.managerActionbar.actionBarTitle.setText(weeksList.get(totalWeeks - position - 1));
        currentWeekNumber = position + 1;
        if (!addPlan) {
            if (mLastReportFragment != null) {
                mLastReportFragment.updateManagerList(currentWeekNumber);
            } else {
                mLastReportFragment = LastWeekReportManagerFragment.newInstance(userId, projectId, currentWeekNumber, auditProjectReport);
                mFragmentList.add(mLastReportFragment);
                mLastReportFragment.updateManagerList(currentWeekNumber);
            }

        } else {
            if (mNextReportFragment != null) {
                mNextReportFragment.updateManagerList(currentWeekNumber);
            } else {
                mNextReportFragment = NextWeekReprotManagerFragment.newInstance(userId, projectId, currentWeekNumber, auditProjectReport);
                mFragmentList.add(mNextReportFragment);
                mNextReportFragment.updateManagerList(currentWeekNumber);
            }
        }
    }
}
