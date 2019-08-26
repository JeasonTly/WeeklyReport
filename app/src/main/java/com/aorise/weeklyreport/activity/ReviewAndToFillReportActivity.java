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
import com.aorise.weeklyreport.activity.fragment.ConclusionFragment;
import com.aorise.weeklyreport.activity.fragment.PlanFragment;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.view.MenuPopup;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.databinding.ActivityToReviewReportBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目周报添加与查看界面
 */

public class ReviewAndToFillReportActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityToReviewReportBinding mViewDataBinding;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = {ConclusionFragment.class, PlanFragment.class,};
    private String FragmentTitle[] = {"本周工作总结", "下周工作计划"};
    private PlanFragment mPlanFragment;         //下周工作计划Fragment
    private ConclusionFragment mConclusionFragment; //本周工作总结Fragment
    // private Fragment mCurrentFragment;
    private int userId = -1;
    private int projectId = -1;
    private int weeks = -1;

    private boolean addPlan = false; //是否为添加工作计划，
    private String currentWeeks = ""; //当前需要设置的actionbarTitle
    private int currentWeek = -1;//当前选择的周，比如33 为第三十三周
    private List<String> weeksList = new ArrayList<>(); //周数选择框列表
    private int totalweek = -1;
    private MenuPopup menuPopup; //周数选择框
    private boolean fromHome = true;

    //private boolean isNormalMode = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_to_review_report);
        WRApplication.getInstance().addActivity(this);
        mViewDataBinding.toreviewActionbar.actionBarDropdown.setVisibility(View.VISIBLE);

        weeksList = TimeUtil.getInstance().getHistoryWeeks();
        totalweek = currentWeek = TimeUtil.getInstance().getDayofWeek();
        menuPopup = new MenuPopup(ReviewAndToFillReportActivity.this, 0, this);
        mViewDataBinding.toreviewActionbar.actionBarTitle.setText("第" + TimeUtil.getInstance().getDayofWeek() + "周");
        currentWeeks = "第" + TimeUtil.getInstance().getDayofWeek() + "周";
        mViewDataBinding.toreviewActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewAndToFillReportActivity.this.finish();
            }
        });
        menuPopup.setPopupGravity(Gravity.BOTTOM);
        //menuPopup.setOffsetY(34);//纵向偏移量
        mViewDataBinding.toreviewActionbar.actionBarTitleArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.toreviewActionbar.actionBarTitleArea);

            }
        });
       // initGetIntent();
        initFragment();
        initTabHost();
        initViewPager();


        mViewDataBinding.toreviewActionbar.actionMenu.setVisibility(View.VISIBLE);
        mViewDataBinding.toreviewActionbar.actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 跳转到添加会议内容的界面
                Intent mIntent = new Intent();
                mIntent.putExtra("isAddPlan", addPlan);
                mIntent.putExtra("title", currentWeeks);
                mIntent.putExtra("weeks", currentWeek);
                mIntent.setClass(ReviewAndToFillReportActivity.this, FillReportActivity.class);
                startActivity(mIntent);
            }
        });
    }


    //初始化Fragment
    private void initFragment() {
//        if (!fromHome) {
//            mConclusionFragment = ConclusionFragment.newInstance(projectId, userId, weeks, false);
//            mPlanFragment = PlanFragment.newInstance(projectId, userId, weeks, false);
//        } else {
            mPlanFragment = new PlanFragment();
            mConclusionFragment = new ConclusionFragment();
//        }
        mFragmentList.add(mConclusionFragment);
        mFragmentList.add(mPlanFragment);
        // mCurrentFragment = mPlanFragment;
    }


    //初始化TabHost
    private void initTabHost() {
        for (int i = 0; i < FragmentArray.length; i++) {
            mViewDataBinding.toreviewTabHost.addTab(mViewDataBinding.toreviewTabHost.newTab().setText(FragmentTitle[i]));
        }
        mViewDataBinding.toreviewTabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("下周工作计划")) {
                    mViewDataBinding.toreviewViewpager.setCurrentItem(1);
                    addPlan = true;
                    mViewDataBinding.toreviewActionbar.actionMenu.setVisibility(View.GONE);
                    // mCurrentFragment = mPlanFragment;
                } else if (tab.getText().equals("本周工作总结")) {
                    addPlan = false;
                    mViewDataBinding.toreviewViewpager.setCurrentItem(0);

                    mViewDataBinding.toreviewActionbar.actionMenu.setVisibility(View.VISIBLE);
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

    //初始化ViewPager
    private void initViewPager() {
        mViewDataBinding.toreviewViewpager.addOnPageChangeListener(this);
        mViewDataBinding.toreviewViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewDataBinding.toreviewViewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        LogT.d("当前选择index为 " + i);
        addPlan = (i == 1);
        mViewDataBinding.toreviewActionbar.actionMenu.setVisibility(addPlan ? View.GONE :View.VISIBLE);
        mViewDataBinding.toreviewTabHost.setScrollPosition(i, 0, false);
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

    //actionbarTitle的那个周数选择，选择时同时更新当前fragment的数据
    @Override
    public void selectPosistion(int position) {
        LogT.d("当前选择了。。。。" + position + " dddd " + weeksList.get(totalweek - position - 1));
        mViewDataBinding.toreviewActionbar.actionBarTitle.setText(weeksList.get(totalweek - position - 1));
        currentWeek = position + 1;
        currentWeeks = weeksList.get(totalweek - position - 1);
        // if (addPlan) {
        if (mPlanFragment != null) {
            mPlanFragment.update(currentWeek);
        } else {
            mPlanFragment = new PlanFragment();
//            if (!fromHome) {
//                mPlanFragment = PlanFragment.newInstance(projectId, userId, weeks, false);
//            } else {
                mPlanFragment = new PlanFragment();
//            }
            mFragmentList.add(mPlanFragment);
            mPlanFragment.update(currentWeek);
        }
        //    } else {
        if (mConclusionFragment != null) {
            mConclusionFragment.update(currentWeek);
        } else {
//            if (!fromHome) {
//                mConclusionFragment = ConclusionFragment.newInstance(projectId, userId, weeks, false);
//            } else {
                mConclusionFragment = new ConclusionFragment();
//            }
            mFragmentList.add(mConclusionFragment);
            mConclusionFragment.update(currentWeek);
        }
        //   }
    }
}
