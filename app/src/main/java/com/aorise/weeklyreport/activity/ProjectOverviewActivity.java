package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.databinding.ActivityProjectOverviewBinding;

public class ProjectOverviewActivity extends BaseActivity {
    private ActivityProjectOverviewBinding mViewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_project_overview);
        WRApplication.getInstance().addActivity(this);
    }
}
