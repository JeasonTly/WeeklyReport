package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.adapter.CustomProjectSpinnerAdapter;
import com.aorise.weeklyreport.adapter.CustomSpinnerAdapter;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectPlan;
import com.aorise.weeklyreport.bean.WeeklyReportUploadBean;
import com.aorise.weeklyreport.databinding.ActivityAddPlanOrSummaryBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.CustomSubscriberNoDialog;
import com.aorise.weeklyreport.network.Result;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;

public class AddPlanOrSummaryActivity extends AppCompatActivity {
    private boolean isAddPlan = false;
    private ActivityAddPlanOrSummaryBinding mViewDataBinding;
    private int weeks;
    private int projectId = 1;
    private int userId = 2;
    private int type = 1;//类型：1总结，2计划
    private int work_type = 1;//工作类型 '1-项目工作，2-部门工作，3-临时工作'
    private int planId = 1;//计划ID
    private int percent = 1;//百分比
    private int status = 1;//完成状态
    private List<ProjectList> mProjectList = new ArrayList<>();
    private List<ProjectPlan> mProjectPlan = new ArrayList<>();
    private CustomProjectSpinnerAdapter mProjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan_or_summary);
        initGetIntent();
        initClickListener();
    }

    private void initGetIntent() {
        Intent mIntent = getIntent();
        isAddPlan = mIntent.getBooleanExtra("isAddPlan", false);
        if (isAddPlan) {
            type = 2;
            mViewDataBinding.planOrSummaryTxt.setText("计划完成进度");
        } else {
            type = 1;
            mViewDataBinding.planOrSummaryTxt.setText("实际完成情况说明");
        }
        String title = "";
        title = mIntent.getStringExtra("title");
        String isAddplan = !isAddPlan ? "工作总结" : "工作计划";
        mViewDataBinding.addplanActionbar.actionBarTitle.setText(title + isAddplan);
        weeks = mIntent.getIntExtra("weeks", -1);

    }

    private void initProjectList() {
        ApiService.Utils.getInstance(this).getProjectList(userId, -1)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriberNoDialog<Result<List<ProjectList>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("error msg" + e.toString());
                    }

                    @Override
                    public void onNext(Result<List<ProjectList>> o) {
                        super.onNext(o);
                        if (o.isRet()) {
                            mProjectList.clear();
                            mProjectList.addAll(o.getData());
                            LogT.d("mProjectList is " + mProjectList.toString());
                            mProjectAdapter = new CustomProjectSpinnerAdapter(AddPlanOrSummaryActivity.this, R.layout.item_spinner_list, mProjectList);
                            mViewDataBinding.workContentList.setAdapter(mProjectAdapter);
                            initPlanList();
                        }
                    }
                });
    }

    private void initClickListener() {
        mViewDataBinding.addplanActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlanOrSummaryActivity.this.finish();
            }
        });
        mViewDataBinding.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(mViewDataBinding.startTime);
            }
        });
        mViewDataBinding.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(mViewDataBinding.endTime);
            }
        });
        initSpinnerList();
        initSpinner();
    }

    private void initSpinnerList() {
        initProjectList();
    }

    private void initPlanList() {
        LogT.d(" project id is " + projectId + " user id is " + userId);
        ApiService.Utils.getInstance(this).getProjectPlan(userId, projectId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriberNoDialog<Result<List<ProjectPlan>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<List<ProjectPlan>> o) {
                        super.onNext(o);
                        if (o.isRet()) {
                            mProjectPlan.clear();
                            mProjectPlan.addAll(o.getData());
                            mViewDataBinding.specificThings.setAdapter(new CustomSpinnerAdapter(AddPlanOrSummaryActivity.this, R.layout.item_spinner_list, mProjectPlan));
                        }
                    }
                });

    }

    public void CommitClick(View view) {
        int weeks = this.weeks;

        String start_time = mViewDataBinding.startTime.getText().toString();
        String end_time = mViewDataBinding.endTime.getText().toString();
        int work_time = 1;
        try {
            work_time = Integer.valueOf(mViewDataBinding.workTime.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String output = mViewDataBinding.output.getText().toString();
        String explain = mViewDataBinding.showHow.getText().toString();
        String issue = mViewDataBinding.needHelp.getText().toString();

        Gson gson = new Gson();
        WeeklyReportUploadBean mUploadInfo = new WeeklyReportUploadBean();
        mUploadInfo.setApprovalState(3);//1,通过，2驳回，3未审批
        mUploadInfo.setByWeek(weeks);//周数
        mUploadInfo.setEndDate(end_time);//结束时间
        mUploadInfo.setExplain(explain);//情况说明
        mUploadInfo.setIssue(issue);//遇到的问题
        mUploadInfo.setOutput(output);//输出物
        mUploadInfo.setPercentComplete(percent);//完成百分比
        mUploadInfo.setPlanId(planId);//计划ID
        mUploadInfo.setProjectId(projectId);//项目ID
        mUploadInfo.setStartDate(start_time);//开始日期
        mUploadInfo.setState(status);//完成状态
        mUploadInfo.setType(type);//工作类型 计划还是总结
        mUploadInfo.setUserId(userId);//用户ID
        mUploadInfo.setWorkTime(work_time);//工作时间
        mUploadInfo.setWorkType(work_type);//工作类型

        LogT.d(" upload info is " + mUploadInfo.toString());
        String jsonData = gson.toJson(mUploadInfo);
        RequestBody mResponseBody = CommonUtils.getRequestBody(jsonData);
        ApiService.Utils.getInstance(this).fillInWeeklyReprot(mResponseBody)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<Integer>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<Integer> o) {
                        super.onNext(o);
                        LogT.d(" fillInWeeklyReprot data is " + o);
                        if (o.isRet()) {
                            ToastUtils.show("添加成功");
                            AddPlanOrSummaryActivity.this.finish();
                        } else {
                            ToastUtils.show("添加失败");
                        }
                    }
                });
    }

    private void selectDate(final Button view) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2000, 0, 1);
        endDate.set(2099, 11, 30);

        TimePickerBuilder timePickerBuilder = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                tvTime.setText(getTime(date));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(date);
                view.setText(format);
            }
        });
        timePickerBuilder
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setContentTextSize(14)//滚轮文字大小
                .setTitleText("选择日期")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleBgColor(0xFF2d348f)//标题背景颜色 Night mode
                .setBgColor(0xFF474faf)//滚轮背景颜色 Night mode
                .setTitleColor(0xFF7af1c8)
                .setDividerColor(0xFF7af1c8)//
                .setTextColorCenter(0xFF7af1c8)
                .setTextColorOut(0xFF919ac6)
                .setCancelColor(0xFFffffff)//取消颜色
                .setSubmitColor(0xFFffffff)//确定颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build().show();
    }

    private void initSpinner() {

        mViewDataBinding.workType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LogT.d(" workType  selected position is " + position);
                work_type = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mViewDataBinding.workContentList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mProjectList != null && mProjectList.size() != 0) {
                    projectId = mProjectList.get(position).getId();
                    LogT.d(" projectId is " + projectId);
                    initPlanList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mViewDataBinding.specificThings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mProjectPlan != null && mProjectPlan.size() != 0) {
                    planId = mProjectPlan.get(position).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mViewDataBinding.planPercent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                percent = (position + 1) * 10;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mViewDataBinding.workStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
