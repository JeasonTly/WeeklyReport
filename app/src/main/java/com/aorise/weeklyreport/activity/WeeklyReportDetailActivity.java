package com.aorise.weeklyreport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.AuditReportBean;
import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;
import com.aorise.weeklyreport.databinding.ActivityWeeklyReportDetailBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.google.gson.Gson;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class WeeklyReportDetailActivity extends AppCompatActivity {
    private ActivityWeeklyReportDetailBinding mViewDataBinding;
    private int id = -1;
    private WeeklyReportDetailBean mDetailBean;
    private String approvalText = "";
    private boolean isAuditMode = false;
    private boolean canAudit = false; //是否可以审批
    private int workStatus = 1;
    private int reamarkStatus = 1;
    private List<Calendar> mSelectDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_weekly_report_detail);
        WRApplication.getInstance().addActivity(this);
        mSelectDateList = new ArrayList<>();

        id = getIntent().getIntExtra("reportId", -1);
        isAuditMode = getIntent().getBooleanExtra("isAuditMode", false);
        canAudit = getIntent().getBooleanExtra("canAudit", false);
        LogT.d(" isManager Mode " + isAuditMode + " canAudit " + canAudit);
        mViewDataBinding.auditArea.setVisibility(isAuditMode && canAudit ? View.VISIBLE : View.GONE);
        mViewDataBinding.detailActionbar.actionMenu.setImageResource(R.drawable.bianji);
        mViewDataBinding.detailActionbar.actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailBean != null) {
                    Intent mIntent = new Intent();
                    mIntent.setClass(WeeklyReportDetailActivity.this, FillReportActivity.class);
                    mIntent.putExtra("isEdit", true);
                    mIntent.putExtra("title", "周报编辑");
                    mIntent.putExtra("isAddPlan", mDetailBean.getType() == 2);
                    mIntent.putExtra("startDate", TimeUtil.getInstance().date2date(mDetailBean.getStartDate()));
                    mIntent.putExtra("endDate", TimeUtil.getInstance().date2date(mDetailBean.getEndDate()));
                    mIntent.putExtra("detailBean", mDetailBean);
                    startActivity(mIntent);
                } else {
                    ToastUtils.show("当前无周报详情!");
                }
            }
        });
        initCalendar();

        mViewDataBinding.detailWorkStatusSpinner.setVisibility(isAuditMode && canAudit ? View.VISIBLE : View.GONE);
        mViewDataBinding.detailWorkStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                workStatus = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //mViewDataBinding.pass.setVisibility(isAuditMode ? View.VISIBLE : View.GONE);

        mViewDataBinding.detailActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyReportDetailActivity.this.finish();
            }
        });
        mViewDataBinding.detailActionbar.actionBarTitle.setText("周报详情");

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDetailInfo();

    }

    public void AllowClick(View view) {
        // CommitApproveResult(true);
        showApproveDialog(true);
    }

    public void NotAllowClick(View view) {
        showApproveDialog(false);
    }

    private void showApproveDialog(final boolean pass) {
        View inputView = LayoutInflater.from(this).inflate(R.layout.dialog_input_contentview, null);
        final EditText approvalMark = (EditText) inputView.findViewById(R.id.approval_mark);
        final RadioGroup radioGroup = (RadioGroup) inputView.findViewById(R.id.remark_level_radio);
        final RadioButton remarkHigh = (RadioButton) inputView.findViewById(R.id.remark_high);
        remarkHigh.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.remark_high:
                        reamarkStatus = 1;
                        break;
                    case R.id.remark_middle:
                        reamarkStatus = 2;
                        break;
                    case R.id.remark_low:
                        reamarkStatus = 3;
                        break;
                }
            }
        });
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this)
                .setTitle("周报审批")
                .setView(inputView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        approvalText = approvalMark.getText().toString();
                        CommitApproveResult(pass);
                    }
                });
        mDialog.create().show();
    }

    private void CommitApproveResult(boolean pass) {
        //审批状态1,未审批，2,已通过，3,驳回
        int approvestatus = pass ? 2 : 3;
        //approvalText = pass ? "通过" : "不通过";
        LogT.d(" param id = " + mDetailBean.getId() + " stauts is " + workStatus + " approvalText " + approvalText);
        Gson gson = new Gson();
        AuditReportBean mModel = new AuditReportBean();
        mModel.setWeeklyId(mDetailBean.getId());//周报ID
        mModel.setPlanStatus(workStatus);//项目周报上的完成状态
        mModel.setRemark(approvalText);//备注
        mModel.setRemarkState(reamarkStatus);
        mModel.setStatue(approvestatus);//审批状态
        String json = gson.toJson(mModel);
        LogT.d("json is " + json);
        RequestBody model = CommonUtils.getRequestBody(json);
        ApiService.Utils.getInstance(this).approvalWeeklyReport(model)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.show("审批失败");
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
                .subscribe(new CustomSubscriber<Result<WeeklyReportDetailBean>>(this) {
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
                            if (mDetailBean.getWeeklyDateModels() != null && mDetailBean.getWeeklyDateModels().size() != 0) {
                                for (int i = 0; i < mDetailBean.getWeeklyDateModels().size(); i++) {
                                    mSelectDateList.add(TimeUtil.getInstance().stringToCalendar(mDetailBean.getWeeklyDateModels().get(i).getWorkDate()));
                                }
                            }
                            LogT.d(" mDetailBean.getWeeklyDateModels() size " + mDetailBean.getWeeklyDateModels() + "\n mSelectDateSize " + mSelectDateList.size());
                            if (mSelectDateList != null && mSelectDateList.size() != 0) {
                                for (Calendar calendar : mSelectDateList) {
                                    mViewDataBinding.detailCalendar.addSchemeDate(calendar);
                                }
                            }
                        }
                    }
                });
    }

    private void initCalendar() {

        mViewDataBinding.monthInfo.setText(mViewDataBinding.detailCalendar.getCurYear() + "年" + mViewDataBinding.detailCalendar.getCurMonth() + "月");
        mViewDataBinding.detailCalendar.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                mViewDataBinding.monthInfo.setText(year + "年" + month + "月");
            }
        });
        if (mSelectDateList != null && mSelectDateList.size() != 0) {
            for (Calendar calendar : mSelectDateList) {
                mViewDataBinding.detailCalendar.putMultiSelect(calendar);
            }
        }
        mViewDataBinding.detailCalendar.setSelected(false);
        mViewDataBinding.detailCalendar.setOnCalendarMultiSelectListener(new CalendarView.OnCalendarMultiSelectListener() {
            @Override
            public void onCalendarMultiSelectOutOfRange(Calendar calendar) {

            }

            @Override
            public void onMultiSelectOutOfSize(Calendar calendar, int maxSize) {

            }

            @Override
            public void onCalendarMultiSelect(Calendar calendar, int curSize, int maxSize) {

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
            default:
                workState = "尚未审批状态";
                break;
        }

        switch (data.getApprovalState()) {
            case 1:
                checkStatus = "未审批";
                mViewDataBinding.pass.setVisibility(View.GONE);
                mViewDataBinding.auditArea.setVisibility(isAuditMode && canAudit ? View.VISIBLE : View.GONE);
                if (isAuditMode && canAudit) {
                    mViewDataBinding.detailWorkStatusSpinner.setVisibility(View.VISIBLE);
                    mViewDataBinding.detailWorkStatus.setVisibility(View.GONE);
                } else {
                    LogT.d("为本人周报且可以登录周报");
                    mViewDataBinding.detailStatusArea.setVisibility(View.GONE);
                    mViewDataBinding.detailActionbar.actionMenu.setVisibility(canAudit ? View.VISIBLE : View.GONE);
                }
                break;
            case 2:
                checkStatus = "已通过";
                mViewDataBinding.auditArea.setVisibility(View.GONE);
                mViewDataBinding.detailStatusArea.setVisibility(View.VISIBLE);
                mViewDataBinding.detailWorkStatusSpinner.setVisibility(View.GONE);
                mViewDataBinding.detailWorkStatus.setVisibility(View.VISIBLE);
                mViewDataBinding.pass.setText("已通过");
                mViewDataBinding.pass.setVisibility(View.VISIBLE);
                break;
            case 3:
                checkStatus = "已驳回";
                mViewDataBinding.auditArea.setVisibility(isAuditMode && canAudit ? View.VISIBLE : View.GONE);

                //mViewDataBinding.pass.setText("已驳回");
                // mViewDataBinding.pass.setVisibility(View.VISIBLE);
                mViewDataBinding.pass.setVisibility(View.GONE);
                mViewDataBinding.detailStatusArea.setVisibility(View.VISIBLE);
                mViewDataBinding.detailActionbar.actionMenu.setVisibility(View.VISIBLE);
                if (isAuditMode && canAudit) {
                    mViewDataBinding.detailWorkStatusSpinner.setVisibility(View.VISIBLE);
                    mViewDataBinding.detailWorkStatus.setVisibility(View.GONE);
                }

                break;

        }

        mViewDataBinding.detailWorkType.setText(workType);
        mViewDataBinding.detailWorkContentList.setText(data.getProjectName());
        mViewDataBinding.detailPlanName.setText(data.getPlanName());
        mViewDataBinding.detailSpecificth.setText(data.getSpecificItem());
        if (TextUtils.isEmpty(data.getStartDate()) && TextUtils.isEmpty(data.getEndDate())) {
            mViewDataBinding.startToEndTxt.setText("尚未设置起止时间");
        } else {
            mViewDataBinding.startToEndTxt.setText(TimeUtil.getInstance().date2date(data.getStartDate()) + "----" + TimeUtil.getInstance().date2date(data.getEndDate()));
        }
        mViewDataBinding.detailPlanPercent.setText(data.getPercentComplete() + "%");
        mViewDataBinding.detailWorkTime.setText(data.getWorkTime() + "天");
        mViewDataBinding.detailOutput.setText(data.getOutput());
        mViewDataBinding.detailShowHow.setText(data.getExplain());
        mViewDataBinding.detailWorkStatus.setText(workState);
        mViewDataBinding.detailNeedHelp.setText(data.getIssue());
        //mViewDataBinding.detailCheckStatus.setText(checkStatus);
    }
}
