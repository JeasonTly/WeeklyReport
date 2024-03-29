package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.fragment.MemberInfoFragment;
import com.aorise.weeklyreport.activity.fragment.ProjectInfoFragment;
import com.aorise.weeklyreport.activity.fragment.ProjectStatisticsFragment;
import com.aorise.weeklyreport.activity.fragment.ProjectWorkHoursFragment;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.ActivityProjectInfoBinding;
import com.aorise.weeklyreport.view.MenuPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目概况界面
 */
public class ProjectInfoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityProjectInfoBinding mViewDataBinding;
    /**
     * 当前项目的项目ID 来源为getIntent()传递的projectInfo;
     */
    private int projectId = -1;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = { ProjectStatisticsFragment.class,ProjectWorkHoursFragment.class,ProjectInfoFragment.class, MemberInfoFragment.class};
    private String FragmentTitle[] = { "项目统计","项目工时","基本信息", "成员信息"};

    private ProjectInfoFragment mProjectInfoFragment;//基本信息所对应的Fragment
    private MemberInfoFragment mMemberInfoFragment;//成员信息所对应的Fragment
    private ProjectStatisticsFragment mProjectStatisticsFragment;//成员信息所对应的Fragment
    private ProjectWorkHoursFragment projectWorkHoursFragment;//项目工时Fragment

    private boolean isReview = false;
    /**
     * 当前周数
     */
    private int currentWeeks = -1;
    /**
     * 总周数
     */
    private int totalweek = -1;

//    private List<String> weeksList = new ArrayList<>();
//    private String currentWeeksTitle = "";
//    private MenuPopup menuPopup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_project_info);
        totalweek = currentWeeks = TimeUtil.getInstance().getDayofWeek();
//        weeksList = TimeUtil.getInstance().getHistoryWeeks();
//        currentWeeksTitle = "第" + currentWeeks + "周";
//        menuPopup = new MenuPopup(this, 0, this);
//        menuPopup.setPopupGravity(Gravity.BOTTOM);
//        menuPopup.setOffsetY(36);
//        mViewDataBinding.infoActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
//        mViewDataBinding.infoActionbar.actionBarTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                menuPopup.showPopupWindow(mViewDataBinding.infoActionbar.actionBarTitle);
//            }
//        });
//        mViewDataBinding.infoActionbar.actionBarTitle.setText("项目概况" + currentWeeksTitle);
        mViewDataBinding.infoActionbar.actionBarTitle.setText("项目概况");
        mViewDataBinding.infoActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectInfoActivity.this.finish();
            }
        });
        ProjectList projectinfo = (ProjectList) getIntent().getSerializableExtra("project_info");
        if (projectinfo != null) {
            projectId = projectinfo.getId();
        }
        isReview = getIntent().getBooleanExtra("isReview", false);
        initFragment();
        initTabHost();
        initViewPager();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ProjectList projectinfo = (ProjectList) getIntent().getSerializableExtra("project_info");
        if (projectinfo != null) {
            projectId = projectinfo.getId();
            if (mProjectInfoFragment != null) {
                mProjectInfoFragment.updateProjectId(projectId);
            }
            if (mMemberInfoFragment != null) {
                mMemberInfoFragment.updateProjectId(projectId);//meifuren
            }
        }
    }

    private void initFragment() {
        mProjectInfoFragment = ProjectInfoFragment.newInstance(projectId);
        mMemberInfoFragment = MemberInfoFragment.newInstance(projectId, currentWeeks, isReview);
        mProjectStatisticsFragment = ProjectStatisticsFragment.newInstance(projectId);
        projectWorkHoursFragment = ProjectWorkHoursFragment.newInstance(projectId);
        mFragmentList.add(mProjectStatisticsFragment);
        mFragmentList.add(projectWorkHoursFragment);
        mFragmentList.add(mProjectInfoFragment);
        mFragmentList.add(mMemberInfoFragment);

        // mCurrentFragment = mPlanFragment;
    }

    private void initTabHost() {
        for (int i = 0; i < FragmentArray.length; i++) {
            mViewDataBinding.infoTabHost.addTab(mViewDataBinding.infoTabHost.newTab().setText(FragmentTitle[i]));
        }
        mViewDataBinding.infoTabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogT.d(" 当前选择的是哪个页卡？ "+tab.getText());
                LogT.d("页卡1为空？ "+(mProjectInfoFragment  ==null));
                LogT.d("页卡2为空？ "+(mMemberInfoFragment  ==null));
                LogT.d("页卡3为空？ "+(mProjectStatisticsFragment  ==null));
                LogT.d("页卡4为空？ "+(projectWorkHoursFragment  ==null));
                if (tab.getText().equals("项目统计")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(0);
                } else if (tab.getText().equals("项目工时")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(1);
                } else if (tab.getText().equals("基本信息")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(2);
                } else if (tab.getText().equals("成员信息")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(3);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogT.d(" 页卡 onTabUnselected "+ tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogT.d(" 页卡 reselect "+ tab.getText());
                if (tab.getText().equals("项目统计")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(0);
                } else if (tab.getText().equals("项目工时")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(1);
                } else if (tab.getText().equals("基本信息")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(2);
                } else if (tab.getText().equals("成员信息")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(3);
                }
            }
        });
    }

    //初始化ViewPager
    private void initViewPager() {
        mViewDataBinding.infoViewpager.addOnPageChangeListener(this);
        mViewDataBinding.infoViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewDataBinding.infoViewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        LogT.d(" 当前选择的页卡是 "+i);
        mViewDataBinding.infoTabHost.setScrollPosition(i, 0, false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    @Deprecated
    @Override
    public void selectPosistion(int position) {
        currentWeeks = totalweek - position - 1;
        if (mMemberInfoFragment != null) {
            mMemberInfoFragment.updateWeeks(currentWeeks);
        } else {
            mMemberInfoFragment = MemberInfoFragment.newInstance(projectId, currentWeeks, isReview);
        }
    }
}
