package com.aorise.weeklyreport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.PersonWorkTimeBean;
import com.aorise.weeklyreport.bean.PlanWorkTimeSettingBean;
import com.aorise.weeklyreport.databinding.ActivityWorkTimeYearStatisticsBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import sysu.zyb.panellistlibrary.AbstractPanelListAdapter;
import sysu.zyb.panellistlibrary.WorkTimePlanClickListener;

public class WorkTimeYearStatisticsActivity extends AppCompatActivity implements WorkTimePlanClickListener {
    private ActivityWorkTimeYearStatisticsBinding mViewDataBinding;
    private int currentYear = 2019;

    private List<String> columnData = new ArrayList<>();
    private List<List<String>> contentList = new ArrayList<>();
    private List<Integer> itemWidthList = new ArrayList<>();
    private List<String> rowDataList = new ArrayList<>();
    private List<String> planDataList = new ArrayList<>();

    private AbstractPanelListAdapter mAdapter;
    private String set_workTime_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_time_year_statistics);
        initRowDataList();
        initItemWidthList();
        mViewDataBinding.worktimeYearActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkTimeYearStatisticsActivity.this.finish();
            }
        });

        mViewDataBinding.idLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(WorkTimeYearStatisticsActivity.this, "你选中的position为：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        initAdapter();
        mViewDataBinding.worktimeYearActionbar.actionBarTitle.setText("工时统计");
        mViewDataBinding.idPlRoot.setAdapter(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new AbstractPanelListAdapter(this, mViewDataBinding.idPlRoot, mViewDataBinding.idLvContent, this) {
            @Override
            protected BaseAdapter getContentAdapter() {
                return null;
            }
        };
        mAdapter.setInitPosition(10);
        mAdapter.setSwipeRefreshEnabled(false);
        mAdapter.setTitle("姓名");// optional
        // mAdapter.setOnRefreshListener(new CustomRefreshListener());// optional
        mAdapter.setContentDataList(contentList);// must have
        mAdapter.setItemWidthList(itemWidthList);// must have
        mAdapter.setColumnDataList(columnData);
        mAdapter.setRowDataList(rowDataList);// must have
        mAdapter.setRowDataList(planDataList);// must have
        mAdapter.setItemHeight(40);// optional, dp
    }


    @Override
    protected void onResume() {
        super.onResume();
        initDefaultWorkTime();
        initWorkTimeData();
    }

    private void initRowDataList() {
        rowDataList.add("一月");
        rowDataList.add("二月");
        rowDataList.add("三月");
        rowDataList.add("四月");
        rowDataList.add("五月");
        rowDataList.add("六月");
        rowDataList.add("七月");
        rowDataList.add("八月");
        rowDataList.add("九月");
        rowDataList.add("十月");
        rowDataList.add("十一月");
        rowDataList.add("十二月");
        rowDataList.add("总计");
    }

    private void initItemWidthList() {
        for (int i = 0; i < rowDataList.size(); i++) {
            itemWidthList.add(80);
        }
    }

    private void initDefaultWorkTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        currentYear = Integer.valueOf(simpleDateFormat.format(date));
        LogT.d(" currentYear " + currentYear);

        ApiService.Utils.getInstance(this).getDefaultWorkTime(currentYear)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<PlanWorkTimeSettingBean>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<PlanWorkTimeSettingBean> listResult) {
                        super.onNext(listResult);
                        LogT.d(" 获取到的预设工时统计信息为 " + listResult.toString());
                        if (listResult.isRet()) {
                            caculateDefaultWorkTime(listResult.getData());
                        }
                    }
                });
    }

    private void initWorkTimeData() {

        ApiService.Utils.getInstance(this).getTotalWorkTime(currentYear)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<PersonWorkTimeBean>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<List<PersonWorkTimeBean>> listResult) {
                        super.onNext(listResult);
                        LogT.d(" 获取到的工时统计信息为 " + listResult.toString());
                        if (listResult.isRet()) {
                            contentList.clear();
                            columnData.clear();
                            caculateStatisticData(listResult.getData());
                        }
                    }
                });
    }


    /**
     * 获取月份的计划工时
     *
     * @param workTimeBean
     */
    private void caculateDefaultWorkTime(PlanWorkTimeSettingBean workTimeBean) {

        planDataList.add(String.valueOf(workTimeBean.getJanuary()));
        planDataList.add(String.valueOf(workTimeBean.getFebruary()));
        planDataList.add(String.valueOf(workTimeBean.getMarch()));
        planDataList.add(String.valueOf(workTimeBean.getApril()));
        planDataList.add(String.valueOf(workTimeBean.getMay()));
        planDataList.add(String.valueOf(workTimeBean.getJune()));
        planDataList.add(String.valueOf(workTimeBean.getJuly()));
        planDataList.add(String.valueOf(workTimeBean.getAugust()));
        planDataList.add(String.valueOf(workTimeBean.getSeptember()));
        planDataList.add(String.valueOf(workTimeBean.getOctober()));
        planDataList.add(String.valueOf(workTimeBean.getNovember()));
        planDataList.add(String.valueOf(workTimeBean.getDecember()));
        planDataList.add(String.valueOf(workTimeBean.getJanuary()
                + workTimeBean.getFebruary()
                + workTimeBean.getMarch()
                + workTimeBean.getApril()
                + workTimeBean.getMay()
                + workTimeBean.getJune()
                + workTimeBean.getJuly()
                + workTimeBean.getAugust()
                + workTimeBean.getSeptember()
                + workTimeBean.getOctober()
                + workTimeBean.getNovember()
                + workTimeBean.getDecember()));
        mAdapter.setPlanDataList(planDataList);
    }

    /**
     * 计算获取全部人员的工时信息
     *
     * @param list
     */
    private void caculateStatisticData(List<PersonWorkTimeBean> list) {

        for (PersonWorkTimeBean workTimeBean : list) {
            LogT.d(" add person " + workTimeBean.getFullName());
            List<String> data = new ArrayList<>();

            for (String month : rowDataList) {
                switch (month) {
                    case "一月":
                        data.add(String.valueOf(workTimeBean.getJanuary()));
                        break;
                    case "二月":
                        data.add(String.valueOf(workTimeBean.getFebruary()));
                        break;
                    case "三月":
                        data.add(String.valueOf(workTimeBean.getMarch()));
                        break;
                    case "四月":
                        data.add(String.valueOf(workTimeBean.getApril()));
                        break;
                    case "五月":
                        data.add(String.valueOf(workTimeBean.getMay()));
                        break;
                    case "六月":
                        data.add(String.valueOf(workTimeBean.getJune()));
                        break;
                    case "七月":
                        data.add(String.valueOf(workTimeBean.getJuly()));
                        break;
                    case "八月":
                        data.add(String.valueOf(workTimeBean.getAugust()));
                        break;
                    case "九月":
                        data.add(String.valueOf(workTimeBean.getSeptember()));
                        break;
                    case "十月":
                        data.add(String.valueOf(workTimeBean.getOctober()));
                        break;
                    case "十一月":
                        data.add(String.valueOf(workTimeBean.getNovember()));
                        break;
                    case "十二月":
                        data.add(String.valueOf(workTimeBean.getDecember()));
                        break;
                    case "总计":
                        data.add(String.valueOf(workTimeBean.getJanuary()
                                + workTimeBean.getFebruary()
                                + workTimeBean.getMarch()
                                + workTimeBean.getApril()
                                + workTimeBean.getMay()
                                + workTimeBean.getJune()
                                + workTimeBean.getJuly()
                                + workTimeBean.getAugust()
                                + workTimeBean.getSeptember()
                                + workTimeBean.getOctober()
                                + workTimeBean.getNovember()
                                + workTimeBean.getDecember()));
                        break;
                }
            }
            contentList.add(data);//添加内容数据
            columnData.add(workTimeBean.getFullName());//添加角色名称
        }

        mAdapter.setContentDataList(contentList);// must have
        mAdapter.setItemWidthList(itemWidthList);// must have
        mAdapter.setColumnDataList(columnData);
        mAdapter.setRowDataList(rowDataList);// must have
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void monthClick(int position) {
        String string = "";
        switch (position) {
            case 0:
                string = "一月";
                break;
            case 1:
                string = "二月";
                break;
            case 2:
                string = "三月";
                break;
            case 3:
                string = "四月";
                break;
            case 4:
                string = "五月";
                break;
            case 5:
                string = "六月";
                break;
            case 6:
                string = "七月";
                break;
            case 7:
                string = "八月";
                break;
            case 8:
                string = "九月";
                break;
            case 9:
                string = "十月";
                break;
            case 10:
                string = "十一月";
                break;
            case 11:
                string = "十二月";
                break;

        }
        LogT.d(" 你点击了" + string);
    }

    @Override
    public void workTimePlanClick(int position) {
        LogT.d(" 选择的 postion 是 " + position);
        String string = "";
        switch (position) {
            case 0:
                string = "一月";
                break;
            case 1:
                string = "二月";
                break;
            case 2:
                string = "三月";
                break;
            case 3:
                string = "四月";
                break;
            case 4:
                string = "五月";
                break;
            case 5:
                string = "六月";
                break;
            case 6:
                string = "七月";
                break;
            case 7:
                string = "八月";
                break;
            case 8:
                string = "九月";
                break;
            case 9:
                string = "十月";
                break;
            case 10:
                string = "十一月";
                break;
            case 11:
                string = "十二月";
                break;

        }
        showAlertDialog(position,string);
    }

    private void showAlertDialog(final int position,final String month) {
        View inputView = LayoutInflater.from(this).inflate(R.layout.dialog_input_worktime, null);
        final EditText approvalMark = (EditText) inputView.findViewById(R.id.set_month_worktime);
        final TextView currentMonth = (TextView) inputView.findViewById(R.id.current_month);
        currentMonth.setText(month+"的计划天数");
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this)
                .setView(inputView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        set_workTime_str = approvalMark.getText().toString();
                        LogT.d(" 设置的时间是" + set_workTime_str);
                        //setMonthWorkTime(position);
                        dialog.dismiss();
                    }
                });
        mDialog.create().show();
    }


    private void setMonthWorkTime(int position) {
        Gson gson = new Gson();
        PlanWorkTimeSettingBean planWorkTimeSettingBean = new PlanWorkTimeSettingBean();
        switch (position) {
            case 0:
                planWorkTimeSettingBean.setJanuary(Float.valueOf(set_workTime_str));
                break;
            case 1:
                planWorkTimeSettingBean.setFebruary(Float.valueOf(set_workTime_str));
                break;
            case 2:
                planWorkTimeSettingBean.setMarch(Float.valueOf(set_workTime_str));
                break;
            case 3:
                planWorkTimeSettingBean.setApril(Float.valueOf(set_workTime_str));
                break;
            case 4:
                planWorkTimeSettingBean.setMay(Float.valueOf(set_workTime_str));
                break;
            case 5:
                planWorkTimeSettingBean.setJune(Float.valueOf(set_workTime_str));
                break;
            case 6:
                planWorkTimeSettingBean.setJuly(Float.valueOf(set_workTime_str));
                break;
            case 7:
                planWorkTimeSettingBean.setAugust(Float.valueOf(set_workTime_str));
                break;
            case 8:
                planWorkTimeSettingBean.setSeptember(Float.valueOf(set_workTime_str));
                break;
            case 9:
                planWorkTimeSettingBean.setOctober(Float.valueOf(set_workTime_str));
                break;
            case 10:
                planWorkTimeSettingBean.setNovember(Float.valueOf(set_workTime_str));
                break;
            case 11:
                planWorkTimeSettingBean.setDecember(Float.valueOf(set_workTime_str));
                break;

        }
        String json = gson.toJson(planWorkTimeSettingBean);
        RequestBody requestBody = CommonUtils.getRequestBody(json);
        ApiService.Utils.getInstance(this).setWorkTime(requestBody)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result result) {
                        super.onNext(result);
                    }
                });
    }
}
