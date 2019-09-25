package com.aorise.weeklyreport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.adapter.TimeSelectAdatper;
import com.aorise.weeklyreport.adapter.TimeSelectListener;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.FillProjectPlan;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.TimePickerBean;
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
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.RequestBody;

/**
 * 周报填写或者审批界面
 * 尚待优化，是否可以使用一个选择器去更新数据
 */
public class FillReportActivity extends AppCompatActivity implements TimeSelectListener {
    /**
     * 是否为添加计划
     */
    private boolean isAddPlan = false;
    private ActivityAddPlanOrSummaryBinding mViewDataBinding;
    /**
     * 当前周数
     */
    private int weeks;
    /**
     * 项目ID
     */
    private int projectId = 1;
    /**
     * 用户ID
     */
    private int userId = 2;
    /**
     * 类型：1添加或者修改总结，2添加或者计划
     */
    private int type = 1;//类型：1总结，2计划
    /**
     * 工作类型 '1-项目工作，2-部门工作，3-临时工作'
     */
    private int work_type = 1;
    /**
     * 工作计划ID
     */
    private int planId = -1;
    /**
     * 百分比
     */
    private double percent = 10;
    /**
     * 工时
     */
    private float work_time = 1;
    /**
     * 周报ID
     */
    private int reportId = -1;
    /**
     * 是否为编辑周报
     */
    private boolean isEdit = false;
    /**
     * 审批状态
     * 审批状态1,未审批，2,已通过，3驳回
     */
    private int approvalStatus = 1;
    /**
     * 输入法键盘管理器
     */
    private InputMethodManager inputMethodManager;
    /**
     * 本地轻量级数据库，获取userID
     */
    private SharedPreferences sp;

    /**
     * 项目原始列表
     */
    private List<ProjectList> mProjectList = new ArrayList<>();
    /**
     * 项目筛选过后的列表
     */
    private List<ProjectList> mSelectProjectList = new ArrayList<>();
    /**
     * 计划
     */
    private List<FillProjectPlan> mProjectPlan = new ArrayList<>();
    /**
     * 百分比列表
     */
    private List<Double> mPercentList = new ArrayList<>();
    /**
     * 工作类型选择器列表
     */
    private List<String> workType = new ArrayList<>();

    /**
     * 项目名称选择器列表
     */
    private List<String> mProjectNameList = new ArrayList<>();

    /**
     * 项目计划选择器列表
     */
    private List<String> mProjectPlanNameList = new ArrayList<>();

    /**
     * 完成百分比选择器列表
     */
    private List<String> mPercentTextList = new ArrayList<>();

    /**
     * 工作类型选择器
     */
    private OptionsPickerView<String> workTypeOptionsView;
    /**
     * 项目选择器
     */
    private OptionsPickerView<String> projectOptionsView;
    /**
     * 计划选择器
     */
    private OptionsPickerView<String> planOptionsView;
    /**
     * 百分比选择器
     */
    private OptionsPickerView<String> percentOptionsView;
    /**
     * 默认值@{
     */
    private int DEFAULT_WORKTYPE_SELECTION = 0;
    private int DEFAULT_PROJECT_SELECTION = 0;
    private int DEFAULT_PLAN_SELECTION = 0;
    private int DEFAULT_PERCENT_SELECTION = 1;
    private String isEdit_WorkType = "";
    private String isEdit_projectName = "";
    private String isEdit_planName = "";
    private String isEdit_Percent = "";
    /**
     * 默认值@}
     */


    private List<WeeklyReportUploadBean.WeeklyDateModelsBean> mUpdateDateList = new ArrayList<>();
    /**
     * 周报详情信息填充
     */
    private WeeklyReportDetailBean mDetailBean;

    /**
     * 周对应的日期
     * String列表
     */
    private List<TimePickerBean> currentWeekDateList = new ArrayList<>();
    private List<TimePickerBean> editcurrentWeekDateList = new ArrayList<>();

    /**
     * 时间选择适配器
     */
    private TimeSelectAdatper mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_plan_or_summary);
        WRApplication.getInstance().addActivity(this);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        LogT.d(" 是否在编辑" + isEdit);
        if (!isEdit) {
            mViewDataBinding.workTime.setText(String.valueOf(work_time));
        }

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sp.getInt("userId", -1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LogT.d(" mViewDataBinding.setSelectTime.sundayAm width is " + metrics.heightPixels / metrics.density);
        initGetIntent();
        initCalendar();
        initWorkTypePicker();
        initProjectList();
        initPercentListPicker();
        initClickListener();
    }

    /**
     * 初始化日历选择器控件
     */
    private void initCalendar() {


        currentWeekDateList = TimeUtil.getInstance().getWorkDateList(weeks);
        Calendar mondayCal = Calendar.getInstance(TimeZone.getDefault());//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        mondayCal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        mondayCal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天

        mondayCal.set(Calendar.WEEK_OF_YEAR, weeks - 1);
        mViewDataBinding.setCurrentMonth(mondayCal.get(Calendar.YEAR) + "年" + (mondayCal.get(Calendar.MONTH) + 1) + "月");
        LogT.d(" currentWeekDateList " + currentWeekDateList.toString());
        LogT.d(" editcurrentWeekDateList " + editcurrentWeekDateList.toString());
        if (isEdit) {
            LogT.d("编辑的情况下默认选择");
            if (editcurrentWeekDateList != null && editcurrentWeekDateList.size() != 0) {
                for (int i = 0; i < editcurrentWeekDateList.size(); i++) {
                    for (int j = 0; j < currentWeekDateList.size(); j++) {
                        if (editcurrentWeekDateList.get(i).getDateName().equals(currentWeekDateList.get(j).getDateName())) {
                            LogT.d("日期相同，替换数据 " + editcurrentWeekDateList.get(i).toString());
                            currentWeekDateList.get(j).setPmSelected(false);
                            currentWeekDateList.get(j).setAmSelected(false);
                            currentWeekDateList.set(j, editcurrentWeekDateList.get(i));
                        }
                    }
                }
            }
        } else {
            for (int j = 0; j < currentWeekDateList.size(); j++) {
                if (j < 5) {
                    currentWeekDateList.get(j).setPmSelected(true);
                    currentWeekDateList.get(j).setAmSelected(true);
                }
            }
        }

        mAdapter = new TimeSelectAdatper(this, currentWeekDateList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mViewDataBinding.timePicker.setLayoutManager(manager);
        mViewDataBinding.timePicker.setAdapter(mAdapter);
        updateCommitDate();
    }

    /**
     * 初始化项目类型滚轮
     */
    private void initWorkTypePicker() {
        workType.add("项目工作");
        workType.add("部门工作");
        // workType.add("其他工作");
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
                initProjectList();
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
        mProjectNameList.clear();
        mSelectProjectList.clear();
        for (ProjectList projectList : mProjectList) {
            LogT.d("添加的工作项目名称为：" + projectList.getName());
            if (work_type == 1) {//项目工作
                if (projectList.getProperty() == 1) {
                    mProjectNameList.add(projectList.getName());
                    mSelectProjectList.add(projectList);
                }
            } else if (work_type == 2) { //部门工作
                if (projectList.getProperty() == 2) {
                    mProjectNameList.add(projectList.getName());
                    mSelectProjectList.add(projectList);
                }
            }
        }

        if (!isEdit && mProjectNameList.size() != 0) {
            LogT.d("当前为新增周报 ,且周报列表长度不为0");
            mViewDataBinding.workProjectName.setText(mProjectNameList.get(DEFAULT_PROJECT_SELECTION));
            projectId = mSelectProjectList.get(DEFAULT_PROJECT_SELECTION).getId();
        } else {
            LogT.d("当前为编辑周报 ,isEdit_projectName " + isEdit_projectName);
            mViewDataBinding.workProjectName.setText(isEdit_projectName);
            // if(TextUtils.isEmpty())

        }
        LogT.d(" mProjectNameList size is " + mProjectNameList.size());
        if (mProjectNameList.size() != 0) {
            initPlanList();
        } else {
            mViewDataBinding.workPlanName.setText("");
            mProjectPlan.clear();
            mProjectPlanNameList.clear();
            initPlanListPicker();
        }

        if (projectOptionsView != null) {
            projectOptionsView.setPicker(mProjectNameList);
        } else {
            projectOptionsView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    LogT.d(" options1 " + options1 + " options2 " + options2 + " options3 " + options3);
                    //mViewDataBinding.workType.setText(workType.get(options1));
                    projectId = mSelectProjectList.get(options1).getId();
                    LogT.d(" project id is " + projectId);
                    mViewDataBinding.workProjectName.setText(mSelectProjectList.get(options1).getName());
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
                if (mProjectNameList.size() == 0) {
                    ToastUtils.show("当前无项目!");
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
        if (mProjectPlan.size() == 0) {
            LogT.d("当前项目没有具体工作事项列表");
            mViewDataBinding.workPlanName.setText("");
            return;
        }
        mProjectPlanNameList.clear();
        for (FillProjectPlan projectPlan : mProjectPlan) {
            LogT.d("添加周报计划列表 " + projectPlan.getName());
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
                if (mProjectPlanNameList.size() == 0) {
                    ToastUtils.show("当前无项目计划!");
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
            double percentNumber = (double) (i * 10);
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

    /**
     * 初始化跳转请求
     * 如果为编辑，则先覆盖数据到对应控件
     */
    private void initGetIntent() {
        Intent mIntent = getIntent();
        isAddPlan = mIntent.getBooleanExtra("isAddPlan", false);
        weeks = mIntent.getIntExtra("weeks", -1);

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

            editcurrentWeekDateList.clear();
            if (mDetailBean.getWeeklyDateModels().size() != 0) {
                mViewDataBinding.setCurrentMonth(TimeUtil.getInstance().endate2monthName(mDetailBean.getWeeklyDateModels().get(0).getWorkDate()));
            } else {
                SimpleDateFormat sm = new SimpleDateFormat("yyyy年MM月");
                mViewDataBinding.setCurrentMonth(sm.format(new Date()));
            }
            if (mDetailBean.getWeeklyDateModels() != null && mDetailBean.getWeeklyDateModels().size() != 0) {
                for (int i = 0; i < mDetailBean.getWeeklyDateModels().size(); i++) {
                    editcurrentWeekDateList.add(TimeUtil.getInstance().weeklyBean2TimePicker(mDetailBean.getWeeklyDateModels().get(i)));
                }
            }

            approvalStatus = mDetailBean.getApprovalState();
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

            if (isAddPlan) {
                type = 2;

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

            }
            mViewDataBinding.planOrSummaryTxt.setText("实际完成情况说明");
        }

    }

    /**
     * 设置actionbar返回键点击事件
     */
    private void initClickListener() {
        mViewDataBinding.addplanActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillReportActivity.this.finish();
            }
        });
    }

    /**
     * 网络请求 根据用户id
     * 获取项目列表
     */
    private void initProjectList() {
        ApiService.Utils.getInstance(this).getProjectList(userId, -1,0)
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

                        }
                    }
                });
    }

    /**
     * 根据用户ID 和项目ID
     * 获取对应的计划列表
     */
    private void initPlanList() {
        LogT.d(" project id is " + projectId + " user id is " + userId);
        ApiService.Utils.getInstance(this).getProjectPlan(userId, projectId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<FillProjectPlan>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<List<FillProjectPlan>> o) {
                        super.onNext(o);
                        if (o.isRet()) {
                            mProjectPlan.clear();
                            mProjectPlan.addAll(o.getData());
                            LogT.d("projectPlanList is " + mProjectPlan.size());
                            mProjectPlanNameList.clear();
                            if (!isEdit &&mProjectPlan.size() !=0) {
                                mViewDataBinding.workPlanName.setText(mProjectPlan.get(DEFAULT_PLAN_SELECTION).getName());
                            }
                            initPlanListPicker();
                        }
                    }
                });
    }

    /**
     * 添加选择的日期
     * 写recycleview 是简单，但是还得先计算单个view占得宽度。就这样吧。应该不会增加什么了
     */
    private void updateCommitDate() {
        if (mUpdateDateList != null) {
            mUpdateDateList.clear();
        }

        float workTime = 0.0f;
        for (int i = 0; i < currentWeekDateList.size(); i++) {
            WeeklyReportUploadBean.WeeklyDateModelsBean modelsBean = new WeeklyReportUploadBean.WeeklyDateModelsBean();
            if (currentWeekDateList.get(i).getSelectStates() == -1) {
                continue;
            }
            if (currentWeekDateList.get(i).getSelectStates() == 1 || currentWeekDateList.get(i).getSelectStates() == 2) {
                workTime = workTime + 0.5f;
            } else {
                workTime++;
            }
            modelsBean.setDayState(currentWeekDateList.get(i).getSelectStates());
            modelsBean.setWorkDate(TimeUtil.getInstance().cn2enDate(currentWeekDateList.get(i).getDateName()));
            mUpdateDateList.add(modelsBean);
        }
        LogT.d(" mUpdateDateList is " + mUpdateDateList.toString() + " workTime " + workTime);
        String startTime = TimeUtil.getInstance().cn2enDate(currentWeekDateList.get(0).getDateName());
        String endTime = TimeUtil.getInstance().cn2enDate(currentWeekDateList.get(currentWeekDateList.size() - 1).getDateName());
        for (int i = 0; i < currentWeekDateList.size(); i++) {
            if (currentWeekDateList.get(i).getSelectStates() != -1) {
                startTime = TimeUtil.getInstance().cn2enDate(currentWeekDateList.get(i).getDateName());
                break;
            }
        }
        for (int i = 0; i < currentWeekDateList.size(); i++) {
            if (currentWeekDateList.get(i).getSelectStates() > 0 && i > 0) {
                endTime = TimeUtil.getInstance().cn2enDate(currentWeekDateList.get(i).getDateName());
            }
        }
        mViewDataBinding.startToEndTxt.setText(startTime + "----" + endTime);
        mViewDataBinding.workTime.setText(String.valueOf(workTime));
    }

    /**
     * 提交按钮点击事件
     * 发起网络请求，提交该页面数据
     *
     * @param view
     */
    public void CommitClick(View view) {
        int weeks = this.weeks;

        try {
            work_time = Float.valueOf(mViewDataBinding.workTime.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String output = mViewDataBinding.output.getText().toString();
        String explain = mViewDataBinding.showHow.getText().toString();
        String issue = mViewDataBinding.needHelp.getText().toString();

        if (planId == -1) {
            ToastUtils.show("当前未选择项目计划!");
            return;
        }

        if (TextUtils.isEmpty(mViewDataBinding.specificThings.getText().toString())) {
            ToastUtils.show("当前未填写具体工作事项!");
            return;
        }
        if (TextUtils.isEmpty(mViewDataBinding.output.getText().toString())) {
            ToastUtils.show("当前未填写输出物!");
            return;
        }
        if (TextUtils.isEmpty(mViewDataBinding.showHow.getText().toString())) {
            ToastUtils.show("当前未填写完成情况!");
            return;
        }
        if(mUpdateDateList.size() == 0){
            ToastUtils.show("当前未选择工作日期!");
            return;
        }

        Gson gson = new Gson();
        WeeklyReportUploadBean mUploadInfo = new WeeklyReportUploadBean();
        if (approvalStatus == 3) {
            approvalStatus = 1;
        }
        mUploadInfo.setApprovalState(approvalStatus);//审批状态1,未审批，2,已通过，3驳回
        if (type == 2 && !isEdit) {
            weeks++;
        }
        mUploadInfo.setByWeek(weeks);//周数
        if (mUpdateDateList.size() != 0) {

            mUploadInfo.setStartDate(mUpdateDateList.get(0).getWorkDate());
            mUploadInfo.setEndDate(mUpdateDateList.get(mUpdateDateList.size() - 1).getWorkDate());
        }
        mUploadInfo.setExplain(explain);//情况说明
        mUploadInfo.setIssue(issue);//遇到的问题
        mUploadInfo.setOutput(output);//输出物
        mUploadInfo.setPercentComplete(percent);//完成百分比
        mUploadInfo.setPlanId(planId);//计划ID
        mUploadInfo.setProjectId(projectId);//项目ID
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
                                ToastUtils.show(o.getMessage());
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


    @Override
    public void amSelected(int position, boolean isChecked) {
        currentWeekDateList.get(position).setAmSelected(isChecked);
        updateCommitDate();
    }

    @Override
    public void pmSelected(int position, boolean isChecked) {
        currentWeekDateList.get(position).setPmSelected(isChecked);
        updateCommitDate();
    }
}
