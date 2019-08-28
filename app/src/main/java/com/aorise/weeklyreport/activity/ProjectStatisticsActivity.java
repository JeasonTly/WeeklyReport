package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;

import com.aorise.weeklyreport.databinding.ActivityProjectStatisticsBinding;


import java.util.List;

public class ProjectStatisticsActivity extends AppCompatActivity {
    private ActivityProjectStatisticsBinding mViewDataBinding;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_project_statistics);
        WRApplication.getInstance().addActivity(this);
        mViewDataBinding.statisticActionbar.actionBarTitle.setText("项目统计");
        mViewDataBinding.statisticActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectStatisticsActivity.this.finish();
            }
        });

    }
}
