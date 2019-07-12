package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;
import com.aorise.weeklyreport.databinding.ActivityWeeklyReportDetailBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriberNoDialog;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;

public class WeeklyReportDetailActivity extends AppCompatActivity {
    private ActivityWeeklyReportDetailBinding mViewDataBinding;
    private int id = -1;
    private int userId = 2;
    private WeeklyReportDetailBean mDetailBean;
    private String approvalText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_weekly_report_detail);

        id = getIntent().getIntExtra("reportId", -1);
        initDetailInfo();
        mViewDataBinding.detailActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyReportDetailActivity.this.finish();
            }
        });
        mViewDataBinding.detailActionbar.actionBarTitle.setText("周报详情");

    }

    public void AllowClick(View view) {
        CommitApproveResult(true);
    }

    public void NotAllowClick(View view) {
        CommitApproveResult(false);
    }

    private void CommitApproveResult(boolean pass) {
        int approvestatus = pass ? 1 : 2;
        approvalText = pass ? "通过" : "不通过";
        LogT.d(" param id = " + mDetailBean.getId() + " stauts is " + mDetailBean.getState() + " approvalText " + approvalText);
        ApiService.Utils.getInstance(this).approvalWeeklyReport(mDetailBean.getId(), mDetailBean.getState(), approvestatus, approvalText)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriberNoDialog<Result>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result o) {
                        super.onNext(o);
                        LogT.d(" o answer is " + o.toString());
                        if (o.isRet()) {
                            ToastUtils.show("审批成功");
                            WeeklyReportDetailActivity.this.finish();
                        } else {
                            ToastUtils.show("审批失败");
                        }
                    }
                });
    }

    private void initDetailInfo() {
        LogT.d("id is " + id);
        ApiService.Utils.getInstance(this).getWeeklyReportDetail(id)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriberNoDialog<Result<WeeklyReportDetailBean>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<WeeklyReportDetailBean> o) {
                        super.onNext(o);
                        if (o.isRet()) {
                            initData(o.getData());
                            mDetailBean = o.getData();
                            LogT.d(" mDetailData is " + mDetailBean.toString());
                        }
                    }
                });
    }

    private void initData(WeeklyReportDetailBean data) {
        LogT.d(" data is " + data);
        String workType = "";
        String workState = "";
        String checkStatus = "";
        switch (data.getWorkType()) {
            case 1:
                workType = "项目工作";
                break;
            case 2:
                workType = "部门工作";
                break;
            case 3:
                workType = "其他工作";
                break;
        }
        switch (data.getState()) {
            case 1:
                workState = "完成";
                break;
            case 2:
                workState = "正常";
                break;
            case 3:
                workState = "滞后";
                break;
            case 4:
                workState = "终止";
                break;
        }

        switch (data.getApprovalState()) {
            case 1:
                checkStatus = "已通过";
                mViewDataBinding.auditArea.setVisibility(View.GONE);
                mViewDataBinding.pass.setVisibility(View.VISIBLE);
                mViewDataBinding.notpass.setVisibility(View.GONE);
                break;
            case 2:
                checkStatus = "已驳回";
                mViewDataBinding.auditArea.setVisibility(View.GONE);
                mViewDataBinding.pass.setVisibility(View.GONE);
                mViewDataBinding.notpass.setVisibility(View.VISIBLE);
                break;
            case 3:
                checkStatus = "未审批";
                mViewDataBinding.auditArea.setVisibility(View.VISIBLE);
                mViewDataBinding.pass.setVisibility(View.GONE);
                mViewDataBinding.notpass.setVisibility(View.GONE);
                break;
        }

        mViewDataBinding.detailWorkType.setText(workType);
        mViewDataBinding.detailWorkContentList.setText(data.getProjectName());
        mViewDataBinding.detailSpecificThings.setText(data.getPlanName());
        mViewDataBinding.detailStartTime.setText(data.getStartDate());
        mViewDataBinding.detailEndTime.setText(data.getEndDate());
        mViewDataBinding.detailPlanPercent.setText(data.getPercentComplete() + "%");
        mViewDataBinding.detailWorkTime.setText(data.getWorkTime() + "天");
        mViewDataBinding.detailOutput.setText(data.getOutput());
        mViewDataBinding.detailShowHow.setText(data.getExplain());
        mViewDataBinding.detailWorkStatus.setText(workState);
        mViewDataBinding.detailNeedHelp.setText(data.getIssue());
        mViewDataBinding.detailCheckStatus.setText(checkStatus);
    }
}
