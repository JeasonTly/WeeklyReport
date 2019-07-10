package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.databinding.ActivityHeaderWeeklyReportDetailBinding;

public class HeaderWeeklyReportDetailActivity extends AppCompatActivity {
    private ActivityHeaderWeeklyReportDetailBinding mViewDataBiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBiding = DataBindingUtil.setContentView(this, R.layout.activity_header_weekly_report_detail);
        mViewDataBiding.headerDetailActionbar.actionBarTitle.setText("项目阶段详情");
        mViewDataBiding.headerDetailActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeaderWeeklyReportDetailActivity.this.finish();
            }
        });
        HeaderItemBean.PlanDetailsListBean mBean = (HeaderItemBean.PlanDetailsListBean) getIntent().getBundleExtra("item_detail").get("detail");
        if (mBean != null) {
            LogT.d(" 输出 " + mBean);
            mViewDataBiding.setDetailInfo(mBean);
            mViewDataBiding.detailWorkTotalPercent.setText(mBean.getPercentComplete() + "%");
            mViewDataBiding.detailStageToComplete.setText(mBean.getStage() + "%");
            mViewDataBiding.detailIsCompleted.setText(mBean.getIsComplete() == 1 ? "已完成" : "未完成");
        }
    }
}
