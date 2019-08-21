package com.aorise.weeklyreport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectPlan;
import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;
import com.aorise.weeklyreport.bean.WeeklyReportUploadBean;
import com.aorise.weeklyreport.databinding.ActivityAddPlanOrSummaryBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class FillReportActivity extends AppCompatActivity {
    private boolean isAddPlan = false;
    private ActivityAddPlanOrSummaryBinding mViewDataBinding;
    private int weeks;
    private int projectId = 1;
    private int userId = 2;
    private int type = 1;//类型：1总结，2计划
    private int work_type = 1;//工作类型 '1-项目工作，2-部门工作，3-临时工作'
    private int planId = 1;//计划ID
    private float percent = 10;//百分比
    private int status = 1;//完成状态
    private float work_time = 1;
    private int reportId = -1;
    private boolean isEdit = false;

    private SimpleDateFormat sdf;
    private InputMethodManager inputMethodManager;
    private SharedPreferences sp;

    private List<ProjectList> mProjectList = new ArrayList<>();
    private List<ProjectPlan> mProjectPlan = new ArrayList<>();
    private List<Float> mPercentList = new ArrayList<>();

    private List<String> mProjectNameList = new ArrayList<>();
    private List<String> mProjectPlanNameList = new ArrayList<>();
    private List<String> mPercentTextList = new ArrayList<>();

    private OptionsPickerView<String> workTypeOptionsView;
    private OptionsPickerView<String> projectOptionsView;
    private OptionsPickerView<String> planOptionsView;
    private OptionsPickerView<String> percentOptionsView;

    private int DEFAULT_WORKTYPE_SELECTION = 0;
    private int DEFAULT_PROJECT_SELECTION = 0;
    private int DEFAULT_PLAN_SELECTION = 0;
    private int DEFAULT_PERCENT_SELECTION = 1;
    private String isEdit_WorkType = "";
    private String isEdit_projectName = "";
    private String isEdit_planName = "";
    private String isEdit_Percent = "";

    private Calendar start_calendar;
    private Calendar end_calendar;

    private List<Calendar> mSelectDateList;
    private List<String> workType = new ArrayList<>();
    private List<WeeklyReportUploadBean.WeeklyDateModelsBean> mUpdateDateList = new ArrayList<>();
    private WeeklyReportDetailBean mDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan_or_summary);
        WRApplication.getInstance().addActivity(this);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        LogT.d(" 是否在编辑" + isEdit);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        mSelectDateList = new ArrayList<>();
        if (!isEdit) {
            mViewDataBinding.workTime.setText(String.valueOf(work_time));
        }

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sp.getInt("userId", -1);
        initGetIntent();
        initCalendar();
        initWorkTypePicker();
        initProjectList();
        initPercentListPicker();
        initClickListener();
    }

    private void initCalendar() {

        mViewDataBinding.monthInfo.setText(mViewDataBinding.muliticalendar.getCurYear() + "年" + mViewDataBinding.muliticalendar.getCurMonth() + "月");
        mViewDataBinding.muliticalendar.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {
                mViewDataBinding.monthInfo.setText(year + "年" + month + "月");
            }
        });
        if (mSelectDateList != null && mSelectDateList.size() != 0) {
            for (Calendar calendar : mSelectDateList) {
                mViewDataBinding.muliticalendar.putMultiSelect(calendar);
            }
        }
        mViewDataBinding.muliticalendar.setOnCalendarMultiSelectListener(new CalendarView.OnCalendarMultiSelectListener() {
            @Override
            public void onCalendarMultiSelectOutOfRange(com.haibin.calendarview.Calendar calendar) {

            }

            @Override
            public void onMultiSelectOutOfSize(com.haibin.calendarview.Calendar calendar, int maxSize) {

            }

            @Override
            public void onCalendarMultiSelect(com.haibin.calendarview.Calendar calendar, int curSize, int maxSize) {
                if (mViewDataBinding.muliticalendar.getMultiSelectCalendars() != null && mViewDataBinding.muliticalendar.getMultiSelectCalendars().size() != 0) {
                    start_calendar = mViewDataBinding.muliticalendar.getMultiSelectCalendars().get(0);
                    end_calendar = mViewDataBinding.muliticalendar.getMultiSelectCalendars().get(mViewDataBinding.muliticalendar.getMultiSelectCalendars().size() - 1);
                    String startTime = start_calendar.getYear() + "-" + TimeUtil.appendZero(start_calendar.getMonth()) + "-" + TimeUtil.appendZero(start_calendar.getDay());
                    String endTime = end_calendar.getYear() + "-" + TimeUtil.appendZero(end_calendar.getMonth()) + "-" + TimeUtil.appendZero(end_calendar.getDay());
                    mViewDataBinding.startToEndTxt.setText(startTime + "----" + endTime);

                    mViewDataBinding.workTime.setText(String.valueOf(Float.valueOf(mViewDataBinding.muliticalendar.getMultiSelectCalendars().size())));
                }
            }
        });
    }

    /**
     * 初始化项目类型滚轮
     */
    private void initWorkTypePicker() {
        workType.add("项目工作");
        workType.add("部门工作");
        workType.add("其他工作");
        if (!isEdit) {
            mViewDataBinding.workType.setText(workType.get(DEFAULT_WORKTYPE_SELECTION));
            work_type = DEFAULT_WORKTYPE_SELECTION + 1;
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
                .setSelectOptions(DEFAULT_WORKTYPE_SELECTION)
                //标题文字
                .setTitleText("选择工作类型")
                .build();
        workTypeOptionsView.setPicker(workType);
        mViewDataBinding.workTypeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workTypeArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (isAddPlan) {
                    ToastUtils.show("计划为项目负责人指定,无法修改工作类型!");
                    return;
                }
                workTypeOptionsView.show();
            }
        });
    }

    /**
     * 初始化项目列表滚轮
     */
    private void initProjectListPicker() {
        if (mProjectList.size() == 0) {
            LogT.d("当前没有工作项目");
            mViewDataBinding.workProjectName.setText("");
            return;
        }
        for (ProjectList projectList : mProjectList) {
            LogT.d("添加的工作项目名称为：" + projectList.getName());
            mProjectNameList.add(projectList.getName());
        }
        if (!isEdit && mProjectNameList.size() != 0) {
            LogT.d("当前为新增周报 ,且周报列表长度不为0");
            mViewDataBinding.workProjectName.setText(mProjectNameList.get(DEFAULT_PROJECT_SELECTION));
            projectId = mProjectList.get(DEFAULT_PROJECT_SELECTION).getId();
        } else {
            LogT.d("当前为编辑周报 ,isEdit_projectName " + isEdit_projectName);
            mViewDataBinding.workProjectName.setText(isEdit_projectName);
            // if(TextUtils.isEmpty())
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
                    .setSelectOptions(DEFAULT_PROJECT_SELECTION)
                    .build();
            projectOptionsView.setPicker(mProjectNameList);
        }
        mViewDataBinding.workProjectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workProjectArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (isAddPlan) {
                    ToastUtils.show("计划为项目负责人指定,无法修改项目名称!");
                    return;
                }
                projectOptionsView.show();
            }
        });
    }

    /**
     * 初始化计划滚轮
     */
    private void initPlanListPicker() {
        if (mProjectPlan.size() == 0 && TextUtils.isEmpty(mViewDataBinding.workPlanName.getText())) {
            LogT.d("当前项目没有具体工作事项列表");
            mViewDataBinding.workPlanName.setText("");
            return;
        }
        for (ProjectPlan projectPlan : mProjectPlan) {
            LogT.d("添加周报列表 " + projectPlan.getName());
            mProjectPlanNameList.add(projectPlan.getName());
        }

        if (!isEdit && mProjectPlanNameList.size() != 0) {
            LogT.d("当前为新增周报,且默认周报位置为第一个");
            mViewDataBinding.workPlanName.setText(mProjectPlanNameList.get(DEFAULT_PLAN_SELECTION));
            planId = mProjectPlan.get(DEFAULT_PLAN_SELECTION).getId();
        } else {
            LogT.d("当前为编辑周报 isEdit_planName " + isEdit_planName);
            mViewDataBinding.workPlanName.setText(isEdit_planName);
        }

        if (planOptionsView != null) {
            planOptionsView.setPicker(mProjectPlanNameList);
            // planOptionsView.setSelectOptions(DEFAULT_PLAN_SELECTION);
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
                    .setTitleText("选择具体工作计划")
                    .setSelectOptions(DEFAULT_PLAN_SELECTION)
                    .build();
            planOptionsView.setPicker(mProjectPlanNameList);
        }
        mViewDataBinding.workPlanArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workPlanArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (isAddPlan) {
                    ToastUtils.show("计划为项目负责人指定,无法修改工作计划!");
                    return;
                }
                planOptionsView.show();
            }
        });
    }

    /**
     * 初始化百分比滚轮
     */
    private void initPercentListPicker() {
        for (int i = 0; i <= 10; i++) {
            float percentNumber = (float) (i * 10);
            mPercentList.add(percentNumber);
            mPercentTextList.add(i * 10 + "%");
        }
        if (!isEdit) {
            mViewDataBinding.workPercentText.setText(mPercentTextList.get(DEFAULT_PERCENT_SELECTION));
            percent = mPercentList.get(DEFAULT_PERCENT_SELECTION);
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
                .setSelectOptions(DEFAULT_PERCENT_SELECTION)
                .build();
        percentOptionsView.setPicker(mPercentTextList);
        mViewDataBinding.workPercentArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workPercentArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if (isAddPlan) {
                    ToastUtils.show("计划为项目负责人指定,无法修改百分比!");
                    return;
                }
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
            //  mViewDataBinding.specificThings.setFocusable(false);
            mViewDataBinding.specificThings.setEnabled(false);
            mViewDataBinding.specificThings.setBackground(null);
            mViewDataBinding.workType.setEnabled(false);
            mViewDataBinding.workTypeDropImg.setVisibility(View.GONE);
            mViewDataBinding.workProjectName.setEnabled(false);
            mViewDataBinding.workProjectImg.setVisibility(View.GONE);
            mViewDataBinding.workPlanName.setEnabled(false);
            mViewDataBinding.workPlanImg.setVisibility(View.GONE);
            mViewDataBinding.workPercentText.setEnabled(false);
            mViewDataBinding.percentDropImg.setVisibility(View.GONE);
        } else {
            type = 1;
            mViewDataBinding.planOrSummaryTxt.setText("实际完成情况说明");
        }
//        LogT.d(" m detail bean is " + ((WeeklyReportDetailBean) mIntent.getSerializableExtra("detailBean")).toString());

        String title = "";
        title = mIntent.getStringExtra("title");
        String isAddplan = !isAddPlan ? "工作总结" : "工作计划";
        if (!isEdit) {
            mViewDataBinding.addplanActionbar.actionBarTitle.setText(title + isAddplan);
        } else {
            mViewDataBinding.addplanActionbar.actionBarTitle.setText(title);
        }


        if (isEdit) {
            mDetailBean = (WeeklyReportDetailBean) mIntent.getSerializableExtra("detailBean");
            LogT.d(" mDetailBean is " + mDetailBean.toString());
            reportId = mDetailBean.getId();
            int _workType = mDetailBean.getWorkType();
            weeks = mDetailBean.getByWeek();
            if (mDetailBean.getWeeklyDateModels() != null && mDetailBean.getWeeklyDateModels().size() != 0) {
                for (int i = 0; i < mDetailBean.getWeeklyDateModels().size(); i++) {
                    mSelectDateList.add(TimeUtil.getInstance().stringToCalendar(mDetailBean.getWeeklyDateModels().get(i).getWorkDate()));
                }
            }
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
            projectId = mDetailBean.getProjectId();
            planId = mDetailBean.getPlanId();
            isEdit_projectName = mDetailBean.getProjectName();
            isEdit_planName = mDetailBean.getPlanName();
            mViewDataBinding.workPlanName.setText(isEdit_planName);
            String startDate = mIntent.getStringExtra("startDate");
            String endDate = mIntent.getStringExtra("endDate");
            if (TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate)) {
                mViewDataBinding.startToEndTxt.setText("尚未设置起止时间");
            } else {
                mViewDataBinding.startToEndTxt.setText(startDate + "----" + endDate);
            }
            String workTime = String.valueOf(mDetailBean.getWorkTime());
            mViewDataBinding.workTime.setText(workTime);
            isEdit_Percent = mDetailBean.getPercentComplete() + "%";
            percent = mDetailBean.getPercentComplete();
            String output = mDetailBean.getOutput();
            String explain = mDetailBean.getExplain();
            String issue = mDetailBean.getIssue();
            mViewDataBinding.specificThings.setText(mDetailBean.getSpecificItem());
            mViewDataBinding.output.setText(output);
            mViewDataBinding.showHow.setText(explain);
            mViewDataBinding.needHelp.setText(issue);
        }

    }


    private void initClickListener() {
        mViewDataBinding.addplanActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillReportActivity.this.finish();
            }
        });
    }

    private void initProjectList() {
        ApiService.Utils.getInstance(this).getProjectList(userId, -1)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<ProjectList>>>(this) {
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
                .subscribe(new CustomSubscriber<Result<List<ProjectPlan>>>(this) {
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
                            // mViewDataBinding.specificThings.setAdapter(new CustomSpinnerAdapter(FillReportActivity.this, R.layout.item_spinner_list, mProjectPlan));
                        }
                    }
                });
    }


    public void CommitClick(View view) {
        int weeks = this.weeks;
        for (int i = 0; i < mViewDataBinding.muliticalendar.getMultiSelectCalendars().size(); i++) {
            //判断一下是选中的
            Calendar calendar = mViewDataBinding.muliticalendar.getMultiSelectCalendars().get(i);

            //把选中的水果取出来     数据在哪里存着就去哪里取
            LogT.d("calendar " + calendar.toString());
            WeeklyReportUploadBean.WeeklyDateModelsBean modelsBean = new WeeklyReportUploadBean.WeeklyDateModelsBean();
            modelsBean.setWorkDate(calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay());
            LogT.d("添加的日期为" + calendar.getYear() + "-" + TimeUtil.appendZero(calendar.getMonth()) + "-" + TimeUtil.appendZero(calendar.getDay()));
            mUpdateDateList.add(modelsBean);
        }
        try {
            work_time = Float.valueOf(mViewDataBinding.workTime.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String output = mViewDataBinding.output.getText().toString();
        String explain = mViewDataBinding.showHow.getText().toString();
        String issue = mViewDataBinding.needHelp.getText().toString();
        Gson gson = new Gson();

        WeeklyReportUploadBean mUploadInfo = new WeeklyReportUploadBean();
        mUploadInfo.setApprovalState(3);//1,通过，2驳回，3未审批
        if (type == 2 && !isEdit) {
            weeks++;
        }
        mUploadInfo.setByWeek(weeks);//周数
        mUploadInfo.setEndDate(end_calendar.getYear() + "-" + TimeUtil.appendZero(end_calendar.getMonth()) + "-" + TimeUtil.appendZero(end_calendar.getDay()));//结束时间
        mUploadInfo.setExplain(explain);//情况说明
        mUploadInfo.setIssue(issue);//遇到的问题
        mUploadInfo.setOutput(output);//输出物
        mUploadInfo.setPercentComplete(percent);//完成百分比
        mUploadInfo.setPlanId(planId);//计划ID
        mUploadInfo.setProjectId(projectId);//项目ID
        mUploadInfo.setStartDate(start_calendar.getYear() + "-" + TimeUtil.appendZero(start_calendar.getMonth()) + "-" + TimeUtil.appendZero(start_calendar.getDay()));//开始日期
        mUploadInfo.setType(type);//工作类型 计划还是总结
        mUploadInfo.setUserId(userId);//用户ID
        mUploadInfo.setWorkTime(work_time);//工作时间
        mUploadInfo.setWorkType(work_type);//工作类型
        mUploadInfo.setSpecificItem(mViewDataBinding.specificThings.getText().toString());//具体工作事项
        LogT.d(isEdit ? "修改周报" : "创建周报");
        if (isEdit) {
            mUploadInfo.setId(reportId);
        }
        mUploadInfo.setWeeklyDateModels(mUpdateDateList);
        LogT.d(" upload info is " + mUploadInfo.toString());
        String jsonData = gson.toJson(mUploadInfo);
        RequestBody mResponseBody = CommonUtils.getRequestBody(jsonData);
        if (!isEdit) {
            ApiService.Utils.getInstance(FillReportActivity.this).fillInWeeklyReprot(mResponseBody)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result>(FillReportActivity.this) {
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
                            LogT.d(" fillInWeeklyReprot data is " + o);
                            if (o.isRet()) {
                                ToastUtils.show("添加成功");
                                FillReportActivity.this.finish();
                            } else {
                                ToastUtils.show("添加失败");
                            }
                        }
                    });
        } else {
            ApiService.Utils.getInstance(FillReportActivity.this).updateWeeklyReprot(mResponseBody)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<Integer>>(FillReportActivity.this) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            LogT.d("updateweeklyReportFail " + e.toString());
                        }

                        @Override
                        public void onNext(Result<Integer> o) {
                            super.onNext(o);
                            LogT.d(" updateweeklyReport data is " + o);
                            if (o.isRet()) {
                                ToastUtils.show("修改成功");
                                FillReportActivity.this.finish();
                            } else {
                                ToastUtils.show("修改失败");
                            }
                        }
                    });
        }
    }


}