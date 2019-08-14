package com.aorise.weeklyreport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectPlan;
import com.aorise.weeklyreport.bean.WeeklyReportUploadBean;
import com.aorise.weeklyreport.databinding.ActivityAddPlanOrSummaryBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.CustomSubscriberNoDialog;
import com.aorise.weeklyreport.network.Result;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;

public class EditReportActivity extends AppCompatActivity {
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
    private int reportId = -1;
    private boolean isEdit = false;

    private SimpleDateFormat sdf;
    private InputMethodManager inputMethodManager;
    private SharedPreferences sp;

    private List<ProjectList> mProjectList = new ArrayList<>();
    private List<ProjectPlan> mProjectPlan = new ArrayList<>();
    private List<Integer> mPercentList = new ArrayList<>();

    private List<String> mProjectNameList = new ArrayList<>();
    private List<String> mProjectPlanNameList = new ArrayList<>();
    private List<String> mPercentTextList = new ArrayList<>();

    private OptionsPickerView<String> workTypeOptionsView;
    private OptionsPickerView<String> projectOptionsView;
    private OptionsPickerView<String> planOptionsView;
    private OptionsPickerView<String> percentOptionsView;

    private String isEdit_WorkType = "";
    private String isEdit_projectName = "";
    private String isEdit_planName = "";
    private String isEdit_Percent = "";
    private List<String> workType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan_or_summary);
        WRApplication.getInstance().addActivity(this);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        LogT.d(" 是否在编辑" + isEdit);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mViewDataBinding.startTime.setText(sdf.format(new Date()));
        mViewDataBinding.endTime.setText(sdf.format(new Date()));

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sp.getInt("userId", -1);
        initGetIntent();

        initWorkTypePicker();
        initProjectList();
        initPercentListPicker();
        initClickListener();
    }

    /**
     * 初始化项目类型滚轮
     */
    private void initWorkTypePicker() {
        workType.add("项目工作");
        workType.add("部门工作");
        workType.add("其他工作");
        if (!isEdit) {
            mViewDataBinding.workType.setText(workType.get(0));
        } else {
            mViewDataBinding.workType.setText(isEdit_WorkType);
        }
        workTypeOptionsView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                LogT.d(" options1 " + options1 + " options2 " + options2 + " options3 " + options3);
                mViewDataBinding.workType.setText(workType.get(options1));
                work_type = options1 + 1;
            }
        }).setTitleBgColor(0xFF3dd078)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setTitleColor(0xFFffffff)
                .setDividerColor(0xFF7af1c8)//
                .setTextColorCenter(0xFF7af1c8)
                .setTextColorOut(0xFF919ac6)
                .setCancelColor(0xFFffffff)//取消颜色
                .setSubmitColor(0xFFffffff)//确定颜色
                .isCenterLabel(true)
                .setLabels("", "", "")
                .setSelectOptions(0)
                //标题文字
                .setTitleText("选择工作类型")
                .build();
        workTypeOptionsView.setPicker(workType);
        mViewDataBinding.workTypeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workTypeOptionsView.show();
            }
        });
    }

    /**
     * 初始化项目列表滚轮
     */
    private void initProjectListPicker() {
        if (mProjectList.size() == 0) {
            mViewDataBinding.workProjectName.setText("");
            return;
        }
        for (ProjectList projectList : mProjectList) {
            mProjectNameList.add(projectList.getName());
        }
        if (!isEdit && mProjectNameList.size() != 0) {
            mViewDataBinding.workProjectName.setText(mProjectNameList.get(0));
        } else {
            mViewDataBinding.workProjectName.setText(isEdit_projectName);
        }
        if (projectOptionsView != null) {
            projectOptionsView.setPicker(mProjectNameList);
        } else {
            projectOptionsView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    LogT.d(" options1 " + options1 + " options2 " + options2 + " options3 " + options3);
                    //mViewDataBinding.workType.setText(workType.get(options1));
                    projectId = mProjectList.get(options1).getId();
                    LogT.d(" project id is " + projectId);
                    mViewDataBinding.workProjectName.setText(mProjectList.get(options1).getName());
                    initPlanList();
                }
            }).setTitleBgColor(0xFF3dd078)//标题背景颜色 Night mode
                    .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                    .setTitleColor(0xFFffffff)
                    .setDividerColor(0xFF7af1c8)//
                    .setTextColorCenter(0xFF7af1c8)
                    .setTextColorOut(0xFF919ac6)
                    .setCancelColor(0xFFffffff)//取消颜色
                    .setSubmitColor(0xFFffffff)//确定颜色
                    .isCenterLabel(true)
                    .setLabels("", "", "")
                    //标题文字
                    .setTitleText("选择项目")
                    .setSelectOptions(0)
                    .build();
            projectOptionsView.setPicker(mProjectNameList);
        }
        mViewDataBinding.workProjectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectOptionsView.show();
            }
        });
    }

    /**
     * 初始化计划滚轮
     */
    private void initPlanListPicker() {
        if (mProjectPlan.size() == 0) {
            mViewDataBinding.workPlanName.setText("");
            return;
        }
        for (ProjectPlan projectPlan : mProjectPlan) {
            mProjectPlanNameList.add(projectPlan.getName());
        }
        if (!isEdit && mProjectPlanNameList.size() != 0) {
            mViewDataBinding.workPlanName.setText(mProjectPlanNameList.get(0));
        } else {
            mViewDataBinding.workPlanName.setText(isEdit_planName);
        }
        if (planOptionsView != null) {
            planOptionsView.setPicker(mProjectPlanNameList);
        } else {
            planOptionsView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    LogT.d(" options1 " + options1 + " options2 " + options2 + " options3 " + options3);
                    //mViewDataBinding.workType.setText(workType.get(options1));
                    planId = mProjectPlan.get(options1).getId();
                    LogT.d(" plan id is " + mProjectPlan);
                    mViewDataBinding.workPlanName.setText(mProjectPlan.get(options1).getName());
                }
            }).setTitleBgColor(0xFF3dd078)//标题背景颜色 Night mode
                    .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                    .setTitleColor(0xFFffffff)
                    .setDividerColor(0xFF7af1c8)//
                    .setTextColorCenter(0xFF7af1c8)
                    .setTextColorOut(0xFF919ac6)
                    .setCancelColor(0xFFffffff)//取消颜色
                    .setSubmitColor(0xFFffffff)//确定颜色
                    .isCenterLabel(true)
                    .setLabels("", "", "")
                    //标题文字
                    .setTitleText("选择计划")
                    .setSelectOptions(0)
                    .build();
            planOptionsView.setPicker(mProjectPlanNameList);
        }
        mViewDataBinding.workPlanArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planOptionsView.show();
            }
        });
    }

    /**
     * 初始化百分比滚轮
     */
    private void initPercentListPicker() {
        for (int i = 0; i <= 10; i++) {
            mPercentList.add(i * 10);
            mPercentTextList.add(i * 10 + "%");
        }
        if (!isEdit) {
            mViewDataBinding.workPercentText.setText(mPercentTextList.get(0));
        } else {
            mViewDataBinding.workPercentText.setText(isEdit_Percent);
        }
        percentOptionsView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                LogT.d(" options1 " + options1 + " options2 " + options2 + " options3 " + options3);
                LogT.d(" 百分比为 " + mPercentList.get(options1) + "%");
                mViewDataBinding.workPercentText.setText(mPercentList.get(options1) + "%");
                percent = mPercentList.get(options1);
            }
        }).setTitleBgColor(0xFF3dd078)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setTitleColor(0xFFffffff)
                .setDividerColor(0xFF7af1c8)//
                .setTextColorCenter(0xFF7af1c8)
                .setTextColorOut(0xFF919ac6)
                .setCancelColor(0xFFffffff)//取消颜色
                .setSubmitColor(0xFFffffff)//确定颜色
                .isCenterLabel(true)
                .setLabels("", "", "")
                //标题文字
                .setTitleText("选择百分比")
                .setSelectOptions(0)
                .build();
        percentOptionsView.setPicker(mPercentTextList);
        mViewDataBinding.workPercentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                percentOptionsView.show();
            }
        });
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

        if (isEdit) {
            reportId = mIntent.getIntExtra("reportId", -1);
            int _workType = mIntent.getIntExtra("workType", -1);

            switch (_workType) {
                case 1:
                    isEdit_WorkType = "项目工作";
                    break;
                case 2:
                    isEdit_WorkType = "部门工作";
                    break;
                case 3:
                    isEdit_WorkType = "其他工作";
                    break;
            }
            projectId = mIntent.getIntExtra("projectId", -1);
            planId = mIntent.getIntExtra("planId", -1);
            isEdit_projectName = mIntent.getStringExtra("projectName");
            isEdit_planName = mIntent.getStringExtra("planName");
            String startDate = mIntent.getStringExtra("startDate");
            String endDate = mIntent.getStringExtra("endDate");
            mViewDataBinding.startTime.setText(startDate);
            mViewDataBinding.endTime.setText(endDate);
            String workTime = String.valueOf(mIntent.getIntExtra("workTime", -1));
            mViewDataBinding.workTime.setText(workTime);
            isEdit_Percent = mIntent.getIntExtra("percent", 0) + "%";
            percent = mIntent.getIntExtra("percent", 0);
            String output = mIntent.getStringExtra("output");
            String explain = mIntent.getStringExtra("explain");
            String issue = mIntent.getStringExtra("issue");
            mViewDataBinding.output.setText(output);
            mViewDataBinding.showHow.setText(explain);
            mViewDataBinding.needHelp.setText(issue);
        }
    }


    private void initClickListener() {
        mViewDataBinding.addplanActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditReportActivity.this.finish();
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

        // initSpinner();
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
                            mProjectNameList.clear();
                            initProjectListPicker();
                            initPlanList();
                        }
                    }
                });
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
                            LogT.d("projectPlanList is " + mProjectPlan.size());
                            mProjectPlanNameList.clear();
                            initPlanListPicker();
                            // mViewDataBinding.specificThings.setAdapter(new CustomSpinnerAdapter(EditReportActivity.this, R.layout.item_spinner_list, mProjectPlan));
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
        // mUploadInfo.setState(status);//完成状态
        mUploadInfo.setType(type);//工作类型 计划还是总结
        mUploadInfo.setUserId(userId);//用户ID
        mUploadInfo.setWorkTime(work_time);//工作时间
        mUploadInfo.setWorkType(work_type);//工作类型
        LogT.d(isEdit ?"修改周报":"创建周报" );
        if(isEdit){
            mUploadInfo.setId(reportId);
        }
        LogT.d(" upload info is " + mUploadInfo.toString());
        String jsonData = gson.toJson(mUploadInfo);

        RequestBody mResponseBody = CommonUtils.getRequestBody(jsonData);
        if (!isEdit) {
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
                                EditReportActivity.this.finish();
                            } else {
                                ToastUtils.show("添加失败");
                            }
                        }
                    });
        } else {
            ApiService.Utils.getInstance(this).updateWeeklyReprot(mResponseBody)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<Integer>>(this) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            LogT.d("updateweeklyReportFail "+e.toString());
                        }

                        @Override
                        public void onNext(Result<Integer> o) {
                            super.onNext(o);
                            LogT.d(" updateweeklyReport data is " + o);
                            if (o.isRet()) {
                                ToastUtils.show("修改成功");
                                EditReportActivity.this.finish();
                            } else {
                                ToastUtils.show("修改失败");
                            }
                        }
                    });
        }
    }

    private void selectDate(final Button view) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        //正确设置方式 原因：注意事项有说明
        startDate.set(2000, 0, 1,0,0,0);
        endDate.set(2099, 11, 30,0,0,0);

        TimePickerBuilder timePickerBuilder = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                tvTime.setText(getTime(date));

                String format = sdf.format(date);
                view.setText(format);

                String start_time = mViewDataBinding.startTime.getText().toString();
                String end_time = mViewDataBinding.endTime.getText().toString();
                Date startDate = sdf.parse(start_time, new ParsePosition(0));
                Date endDate = sdf.parse(end_time, new ParsePosition(0));
                if (endDate.before(startDate)) {
                    LogT.d("开始时间在结束时间之后");
                    view.setText(sdf.format(new Date()));
                    ToastUtils.show((isAddPlan ? "计划" : "总结") + "结束时间不得小于开始时间!");
                    return;
                }
                mViewDataBinding.workTime.setText(String.valueOf(TimeUtil.getInstance().caclulateDifferenceBySimpleDateFormat(startDate, endDate)));

            }
        });
        timePickerBuilder
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(14)//确定和取消文字大小
                .setTitleSize(16)//标题文字大小
                .setContentTextSize(14)//滚轮文字大小
                .setTitleText("选择日期")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleBgColor(0xFF3dd078)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setTitleColor(0xFFffffff)
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

}
