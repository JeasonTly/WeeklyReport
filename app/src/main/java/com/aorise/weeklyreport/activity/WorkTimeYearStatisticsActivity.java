package com.aorise.weeklyreport.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.PersonWorkTimeBean;
import com.aorise.weeklyreport.bean.PlanWorkTimeSettingBean;
import com.aorise.weeklyreport.databinding.ActivityWorkTimeYearStatisticsBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.aorise.weeklyreport.view.MenuPopup;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import sysu.zyb.panellistlibrary.AbstractPanelListAdapter;
import sysu.zyb.panellistlibrary.WorkTimePlanClickListener;

public class WorkTimeYearStatisticsActivity extends AppCompatActivity implements WorkTimePlanClickListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityWorkTimeYearStatisticsBinding mViewDataBinding;
    private int currentYear = 2019;
    private int defaultYear = 2019;
    /**
     * 纵轴第一列固定列的数据，内容为各成员名称
     */
    private List<String> columnData = new ArrayList<>();
    /**
     * 工时统计信息，外层List为人的工时信息，内层为各个月份的工时信息
     */
    private List<List<String>> contentList = new ArrayList<>();
    /**
     * 单列宽度
     */
    private List<Integer> itemWidthList = new ArrayList<>();
    /**
     * 分多少列，最大值为13，内容为月份
     */
    private List<String> rowDataList = new ArrayList<>();
    /**
     * 计划工时 ，内容为各个月份的计划工时
     */
    private List<String> planDataList = new ArrayList<>();

    private AbstractPanelListAdapter mAdapter;
    private String set_workTime_str = "";
    private MenuPopup menuPopup;
    private List<String> mYearList = new ArrayList<>();

    private PlanWorkTimeSettingBean planWorkTimeSettingBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_time_year_statistics);
        WRApplication.getInstance().addActivity(this);
        initRowDataList();
        initItemWidthList();

        // 获取默认年份 tuliyuan add start @{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        defaultYear = Integer.valueOf(simpleDateFormat.format(date));
        LogT.d(" currentYear " + defaultYear);
        for (int i = 0; i < 5; i++) {
            mYearList.add(String.valueOf(defaultYear - i) + "年");
        }
        // 获取默认年份 tuliyuan add end @}


        /**
         * 初始化点击年份原则的弹出框
         */
        menuPopup = new MenuPopup(this, 0, this, mYearList);
        mViewDataBinding.worktimeYearActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        mViewDataBinding.worktimeYearActionbar.actionBarTitleArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.worktimeYearActionbar.actionBarTitleArea);
            }
        });
        mViewDataBinding.worktimeYearActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkTimeYearStatisticsActivity.this.finish();
            }
        });

        mViewDataBinding.idLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        initAdapter();
        mViewDataBinding.worktimeYearActionbar.actionBarTitle.setText("工时统计 -" + defaultYear);
        mViewDataBinding.idPlRoot.setAdapter(mAdapter);
    }

    /**
     * 初始化适配器！
     */
    private void initAdapter() {
        mAdapter = new AbstractPanelListAdapter(this, mViewDataBinding.idPlRoot, mViewDataBinding.idLvContent, this) {
            @Override
            protected BaseAdapter getContentAdapter() {
                return null;
            }
        };

        mAdapter.setInitPosition(0);
        mAdapter.setSwipeRefreshEnabled(false);
        mAdapter.setTitle("姓名");// optional
        mAdapter.setContentDataList(contentList);// must have
        mAdapter.setItemWidthList(itemWidthList);// must have
        mAdapter.setColumnDataList(columnData);
        mAdapter.setRowDataList(rowDataList);// must have
        mAdapter.setPlanDataList(planDataList);// must have
        mAdapter.setItemHeight(40);// optional, dp
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        mAdapter.setInitDefaultMonthPosition(Integer.valueOf(simpleDateFormat.format(new Date())) - 1);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initDefaultWorkTime();
        initWorkTimeData();
    }

    /**
     * 初始化横轴 多月份！
     */
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

    /**
     * 根据月份横轴的数据去设置每一列的宽度
     */
    private void initItemWidthList() {
        for (int i = 0; i < rowDataList.size(); i++) {
            itemWidthList.add(80);
        }
    }

    /**
     * 获取各个月份的工时信息
     */
    private void initDefaultWorkTime() {

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
                            planWorkTimeSettingBean = listResult.getData();
                            caculateDefaultWorkTime(listResult.getData());
                        }
                    }
                });
    }

    /**
     * 获取全部工时信息
     */
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
     * 获取各个月份的计划工时
     *
     * @param workTimeBean
     */
    private void caculateDefaultWorkTime(PlanWorkTimeSettingBean workTimeBean) {
        planDataList.clear();
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
     * 计算获取全部人员的工时信息，并进行数据整合
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

    /**
     * 月份点击事件
     * Intent跳转到WorkTimeMonthStatisticsActivity.class
     * 根据月份展示当前月份下周的统计数据
     *
     * @param position
     */
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
//        LogT.d(" 你点击了" + string);
        if (position == 12) {
            return;
        }
        Intent mIntent = new Intent();
        mIntent.putExtra("year", currentYear);
        mIntent.putExtra("month", position + 1);
        mIntent.putExtra("monthStr", string);
        mIntent.setClass(this, WorkTimeMonthStatisticsActivity.class);
        startActivity(mIntent);
    }

    /**
     * 当前计划工时行 点击事件
     *
     * @param position
     */
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
        if (position == 12) {
            return;
        }
        showAlertDialog(position, string);
    }

    /**
     * 弹出设置当前月总工时的对话框
     *
     * @param position 当前月份在X轴对应的position
     * @param month    当前月份String
     */
    private void showAlertDialog(final int position, final String month) {
        View inputView = LayoutInflater.from(this).inflate(R.layout.dialog_input_worktime, null);
        final EditText approvalMark = (EditText) inputView.findViewById(R.id.set_month_worktime);
        approvalMark.setText(planDataList.get(position));
        final TextView currentMonth = (TextView) inputView.findViewById(R.id.current_month);
        currentMonth.setText(month + "的计划天数");
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
                        setMonthWorkTime(position);
                        dialog.dismiss();
                    }
                });
        mDialog.create().show();
    }

    /**
     * 设置选中月份的工时
     * 总计不可设置
     *
     * @param position
     */
    private void setMonthWorkTime(final int position) {
        Gson gson = new Gson();
//        final PlanWorkTimeSettingBean planWorkTimeSettingBean = new PlanWorkTimeSettingBean();
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
        planWorkTimeSettingBean.setYear(currentYear);
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
                        LogT.d(" 设置工时" + result);
                        if (result.isRet()) {
                            ToastUtils.show("设置工时成功!");
                            /**
                             * 重新更新修改后的信息并刷新
                             */
                            planDataList.set(position, set_workTime_str);
                            planDataList.set(planDataList.size() - 1, String.valueOf(planWorkTimeSettingBean.getJanuary()
                                    + planWorkTimeSettingBean.getFebruary()
                                    + planWorkTimeSettingBean.getMarch()
                                    + planWorkTimeSettingBean.getApril()
                                    + planWorkTimeSettingBean.getMay()
                                    + planWorkTimeSettingBean.getJune()
                                    + planWorkTimeSettingBean.getJuly()
                                    + planWorkTimeSettingBean.getAugust()
                                    + planWorkTimeSettingBean.getSeptember()
                                    + planWorkTimeSettingBean.getOctober()
                                    + planWorkTimeSettingBean.getNovember()
                                    + planWorkTimeSettingBean.getDecember()));
                            //WorkTimeYearStatisticsActivity.this.onResume();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 月份选择器，选择的月份内容；倒序
     *
     * @param position
     */
    @Override
    public void selectPosistion(int position) {
        currentYear = defaultYear - (mYearList.size() - position - 1);
        LogT.d(" select year is " + currentYear);
        mViewDataBinding.worktimeYearActionbar.actionBarTitle.setText("工时统计 -" + currentYear);
        initDefaultWorkTime();
        initWorkTimeData();
    }
}
