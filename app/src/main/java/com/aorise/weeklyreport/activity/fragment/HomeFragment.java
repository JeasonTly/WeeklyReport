package com.aorise.weeklyreport.activity.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.EditReportActivity;
import com.aorise.weeklyreport.adapter.MainFragmentAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.MenuPopup;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener, MenuPopup.MenuPopupSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHomeBinding mViewDataBinding;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Class FragmentArray[] = {ConclusionFragment.class, PlanFragment.class,};
    private String FragmentTitle[] = {"本周工作总结", "下周工作计划"};
    private PlanFragment mPlanFragment;
    private ConclusionFragment mConclusionFragment;
    private Fragment mCurrentFragment;

    private boolean addPlan = false;
    private String currentWeeks = "";
    private List<String> weeks = new ArrayList<>();
    private MenuPopup menuPopup;

    private boolean isNormalMode = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void initFragment() {
        mPlanFragment = new PlanFragment();
        mConclusionFragment = new ConclusionFragment();
        mFragmentList.add(mConclusionFragment);
        mFragmentList.add(mPlanFragment);
        mCurrentFragment = mPlanFragment;
    }

    private void initTabHost() {
        for (int i = 0; i < FragmentArray.length; i++) {
            mViewDataBinding.fragmentTabHost.addTab(mViewDataBinding.fragmentTabHost.newTab().setText(FragmentTitle[i]));
        }
        mViewDataBinding.fragmentTabHost.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("下周工作计划")) {
                    mViewDataBinding.viewpager.setCurrentItem(1);
                    addPlan = true;
                    mCurrentFragment = mPlanFragment;
                } else if (tab.getText().equals("本周工作总结")) {
                    addPlan = false;
                    mViewDataBinding.viewpager.setCurrentItem(0);
                    mCurrentFragment = mConclusionFragment;
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
        mViewDataBinding.viewpager.setAdapter(new MainFragmentAdapter(getFragmentManager(), mFragmentList));
        mViewDataBinding.viewpager.setCurrentItem(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mViewDataBinding.fhActionbar.actionBarDropdown.setVisibility(View.VISIBLE);

        weeks = TimeUtil.getInstance().getHistoryWeeks();
        menuPopup = new MenuPopup(getActivity(), weeks.size() - 1, this);
        mViewDataBinding.fhActionbar.actionBarTitle.setText("第" + TimeUtil.getInstance().getDayofWeek() + "周");
        currentWeeks = "第" + TimeUtil.getInstance().getDayofWeek() + "周";
        mViewDataBinding.fhActionbar.actionbarBack.setVisibility(View.GONE);
        menuPopup.setPopupGravity(Gravity.BOTTOM);
        menuPopup.setOffsetY(36);
        mViewDataBinding.fhActionbar.actionBarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.fhActionbar.actionBarTitle);

            }
        });

        initFragment();
        initTabHost();
        initViewPager();


        mViewDataBinding.fhActionbar.actionMenu.setVisibility(isNormalMode ? View.VISIBLE : View.GONE);
        mViewDataBinding.fhActionbar.actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 跳转到添加会议内容的界面
                Intent mIntent = new Intent();
                mIntent.putExtra("isAddPlan", addPlan);
                mIntent.putExtra("title", currentWeeks);
                mIntent.putExtra("weeks", TimeUtil.getInstance().getDayofWeek());
                mIntent.setClass(getActivity(), EditReportActivity.class);
                startActivity(mIntent);
            }
        });
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
        LogT.d("当前选择index为 " + i);
        addPlan = (i == 1);
        mViewDataBinding.fragmentTabHost.setScrollPosition(i, 0, false);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void selectPosistion(int position) {
        LogT.d("当前选择了。。。。" + position);
        mViewDataBinding.fhActionbar.actionBarTitle.setText(weeks.get(position));

        currentWeeks = weeks.get(position);
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
