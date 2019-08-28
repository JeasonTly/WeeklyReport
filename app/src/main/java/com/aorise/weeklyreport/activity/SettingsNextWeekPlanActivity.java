package com.aorise.weeklyreport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.FillProjectPlan;
import com.aorise.weeklyreport.bean.MemberListSpinnerBean;
import com.aorise.weeklyreport.bean.ProjectPlan;
import com.aorise.weeklyreport.bean.WeeklyReportUploadBean;
import com.aorise.weeklyreport.databinding.ActivitySettingsNextWeekPlanBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class SettingsNextWeekPlanActivity extends AppCompatActivity {
    private ActivitySettingsNextWeekPlanBinding mViewDataBinding;

    private List<MemberListSpinnerBean> mMemberList = new ArrayList<>();
    private List<FillProjectPlan> mProjectPlan = new ArrayList<>();
    private List<Double> mPercentList = new ArrayList<>();

    private List<String> mMemberNameList = new ArrayList<>();
    private List<String> mProjectPlanNameList = new ArrayList<>();
    private List<String> mPercentTextList = new ArrayList<>();
    private List<String> workType = new ArrayList<>();

    private OptionsPickerView<String> workTypeOptionsView;
    private OptionsPickerView<String> projectOptionsView;
    private OptionsPickerView<String> planOptionsView;
    private OptionsPickerView<String> percentOptionsView;

    private int DEFAULT_WORKTYPE_SELECTION = 0;
    private int DEFAULT_PROJECT_SELECTION = 0;
    private int DEFAULT_PLAN_SELECTION = 0;
    private int DEFAULT_PERCENT_SELECTION = 1;

    private int weeksNumber = -1;
    private int work_type = 1;
    private int userId = -1;
    private String userName = "";
    private int planId = -1;
    private double percent = 10;
    private int projectId = -1;
    private String projectName = "";

    private String[] dialog_item_name;
    private int dialog_select_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WRApplication.getInstance().addActivity(this);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings_next_week_plan);
        initGetIntent();
        initMemberList();
        initActionBar();
        initWorkTypePicker();
        mViewDataBinding.ownerArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMutilDialog();
            }
        });
        initPercentListPicker();
        mViewDataBinding.commitPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeeklyReportUploadBean mUploadInfo = new WeeklyReportUploadBean();
                mUploadInfo.setApprovalState(1);
                mUploadInfo.setWorkType(work_type);
                mUploadInfo.setType(2);
                mUploadInfo.setProjectId(projectId);
                mUploadInfo.setPlanId(planId);
                mUploadInfo.setPercentComplete(percent);
                mUploadInfo.setUserId(userId);
                mUploadInfo.setByWeek(weeksNumber);
                mUploadInfo.setSpecificItem(mViewDataBinding.specificThings.getText().toString());
                Gson gson = new Gson();
                String jsonData = gson.toJson(mUploadInfo);
                RequestBody mResponseBody = CommonUtils.getRequestBody(jsonData);
                ApiService.Utils.getInstance(SettingsNextWeekPlanActivity.this).fillInWeeklyReprot(mResponseBody)
                        .compose(ApiService.Utils.schedulersTransformer())
                        .subscribe(new CustomSubscriber<Result>(SettingsNextWeekPlanActivity.this) {
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
                                    ToastUtils.show("指派下周计划成功");
                                    SettingsNextWeekPlanActivity.this.finish();
                                } else {
                                    ToastUtils.show(o.getMessage());
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initActionBar() {
        mViewDataBinding.NWPlanActionbar.actionMenu.setVisibility(View.GONE);
        mViewDataBinding.NWPlanActionbar.actionBarDropdown.setVisibility(View.GONE);
        mViewDataBinding.NWPlanActionbar.actionbarBack.setVisibility(View.VISIBLE);
        mViewDataBinding.NWPlanActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsNextWeekPlanActivity.this.finish();
            }
        });
        mViewDataBinding.NWPlanActionbar.actionBarTitle.setText("指定项目计划");
    }

    private void initGetIntent() {
        Intent mIntent = getIntent();
        projectId = mIntent.getIntExtra("projectId", -1);
        weeksNumber = mIntent.getIntExtra("weeks", -1) + 1;
        projectName = mIntent.getStringExtra("projectName");
        mViewDataBinding.workProjectName.setText(projectName);
    }

    /**
     * 初始化项目类型滚轮
     */
    private void initWorkTypePicker() {
        workType.add("项目工作");
        workType.add("部门工作");
        workType.add("其他工作");
        mViewDataBinding.workType.setText(workType.get(DEFAULT_WORKTYPE_SELECTION));
        work_type = DEFAULT_WORKTYPE_SELECTION + 1;

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
//                if (inputMethodManager.isActive()) {
//                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workTypeArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
                workTypeOptionsView.show();
            }
        });
    }

    /**
     * 初始化计划滚轮
     */
    private void initPlanListPicker() {
        LogT.d(" initPlanListPicker.... " +mProjectPlan.size());
        if (mProjectPlan.size() == 0) {
            LogT.d("当前项目没有具体工作事项列表");
            mViewDataBinding.workPlanName.setText("");
            return;
        }
        for (FillProjectPlan projectPlan : mProjectPlan) {
            LogT.d("添加周报列表 " + projectPlan.getName());
            mProjectPlanNameList.add(projectPlan.getName());
        }
        mViewDataBinding.workPlanName.setText(mProjectPlanNameList.get(DEFAULT_PLAN_SELECTION));
        planId = mProjectPlan.get(DEFAULT_PLAN_SELECTION).getId();

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
                    LogT.d(" plan id is " + mProjectPlan.get(options1).getId());
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
                    .setTitleText("选择工作计划")
                    .setSelectOptions(DEFAULT_PLAN_SELECTION)
                    .build();
            planOptionsView.setPicker(mProjectPlanNameList);
        }
        mViewDataBinding.workPlanArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (inputMethodManager.isActive()) {
//                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workPlanArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
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
        mViewDataBinding.workPercentText.setText(mPercentTextList.get(DEFAULT_PERCENT_SELECTION));
        percent = mPercentList.get(DEFAULT_PERCENT_SELECTION);
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
//                if (inputMethodManager.isActive()) {
//                    inputMethodManager.hideSoftInputFromWindow(mViewDataBinding.workPercentArea.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
                percentOptionsView.show();
            }
        });
    }

    private void initPlanList() {
        LogT.d(" project id is " + projectId +" userId is "+userId);
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
                            initPlanListPicker();
                            // mViewDataBinding.specificThings.setAdapter(new CustomSpinnerAdapter(FillReportActivity.this, R.layout.item_spinner_list, mProjectPlan));
                        }
                    }
                });
    }

    private void initMemberList() {
        ApiService.Utils.getInstance(this).getMemberList(projectId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<MemberListSpinnerBean>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("error " + e.toString());
                        ToastUtils.show("当前项目组下无项目成员");

                    }

                    @Override
                    public void onNext(Result<List<MemberListSpinnerBean>> o) {
                        super.onNext(o);
                        LogT.d(" 成员选择框列表为 is " + o.toString());
                        if (o.isRet()) {
                            mMemberList.clear();
                            mMemberList.addAll(o.getData());
                            if (mMemberList != null && mMemberList.size() != 0) {
                                userId = mMemberList.get(dialog_select_index).getId();
                                mViewDataBinding.ownerName.setText(mMemberList.get(dialog_select_index).getUserName());
                                initPlanList();
                            }
                        }
                    }
                });
    }

    private void showMutilDialog() {
        //[1]构造对话框的实例
        dialog_item_name = new String[mMemberList.size()];
        for (int j = 0; j < mMemberList.size(); j++) {
            dialog_item_name[j] = mMemberList.get(j).getUserName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择计划负责人");
        builder.setSingleChoiceItems(dialog_item_name, dialog_select_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogT.d("dialog_item_name " + dialog_item_name[which]);
                dialog_select_index = which;
                userName = dialog_item_name[which];
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                userId = mMemberList.get(dialog_select_index).getId();
                if (TextUtils.isEmpty(userName)) {
                    userName = mMemberList.get(dialog_select_index).getUserName();
                }
                mViewDataBinding.ownerName.setText(userName);
                LogT.d(" userId is " + userId);
                initPlanList();
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
