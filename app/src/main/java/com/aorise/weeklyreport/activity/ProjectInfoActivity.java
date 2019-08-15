package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.fragment.MemberInfoFragment;
import com.aorise.weeklyreport.activity.fragment.ProjectInfoFragment;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.MenuPopup;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.ActivityProjectInfoBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectInfoActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityProjectInfoBinding mViewDataBinding;
    private int projectId = -1;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = {ProjectInfoFragment.class, MemberInfoFragment.class,};
    private String FragmentTitle[] = {"基本信息", "成员信息"};
    private ProjectInfoFragment mProjectInfoFragment;
    private MemberInfoFragment mMemberInfoFragment;
    private int currentWeeks = -1;
    private List<String> weeksList = new ArrayList<>();
    private String currentWeeksTitle = "";
    private MenuPopup menuPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_project_info);
        currentWeeks = TimeUtil.getInstance().getDayofWeek();
        weeksList = TimeUtil.getInstance().getHistoryWeeks();
        currentWeeksTitle = "第" + currentWeeks + "周";
        menuPopup = new MenuPopup(this, weeksList.size() - 1, this);
        menuPopup.setPopupGravity(Gravity.BOTTOM);
        menuPopup.setOffsetY(36);
        mViewDataBinding.infoActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        mViewDataBinding.infoActionbar.actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.infoActionbar.actionBarTitle);
            }
        });
        mViewDataBinding.infoActionbar.actionBarTitle.setText("项目概况" + currentWeeksTitle);
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
                mMemberInfoFragment.updateProjectId(projectId);
            }
        }
    }

    private void initFragment() {
        mProjectInfoFragment = ProjectInfoFragment.newInstance(projectId);
        mMemberInfoFragment = MemberInfoFragment.newInstance(projectId,currentWeeks);
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
                if (tab.getText().equals("成员信息")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(1);

                } else if (tab.getText().equals("基本信息")) {
                    mViewDataBinding.infoViewpager.setCurrentItem(0);
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
        mViewDataBinding.infoViewpager.addOnPageChangeListener(this);
        mViewDataBinding.infoViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        mViewDataBinding.infoViewpager.setCurrentItem(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        mViewDataBinding.infoTabHost.setScrollPosition(i, 0, false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void selectPosistion(int position) {
        currentWeeks = position + 1;
        currentWeeksTitle = weeksList.get(position);
        mViewDataBinding.infoActionbar.actionBarTitle.setText("项目概况" + currentWeeksTitle);
        if (mMemberInfoFragment != null) {
            mMemberInfoFragment.updateWeeks(currentWeeks);
        } else {
            mMemberInfoFragment = MemberInfoFragment.newInstance(projectId, currentWeeks);
        }
    }
}
