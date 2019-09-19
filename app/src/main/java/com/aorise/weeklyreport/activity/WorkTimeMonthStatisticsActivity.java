package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.WeeklyWorkTimeBean;
import com.aorise.weeklyreport.databinding.ActivityWorkTimeMonthStatisticsBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

import java.util.ArrayList;
import java.util.List;

import sysu.zyb.panellistlibrary.AbstractPanelListWithOutPlanAdapter;
import sysu.zyb.panellistlibrary.WorkTimePlanClickListener;

public class WorkTimeMonthStatisticsActivity extends AppCompatActivity implements WorkTimePlanClickListener {
    private ActivityWorkTimeMonthStatisticsBinding mViewDataBinding;
    /**
     * 当前年、月，用于网络请求
     */
    private int currentYear = 2019;
    private int currentMonth = 8;
    /**
     * 当前月的Str类型，用于设置actionbar标题
     */
    private String currentMonthStr = "";
    /**
     * 纵轴上的：角色名称
     */
    private List<String> columnData = new ArrayList<>();
    /**
     * 是项目工时数据，
     */
    private List<List<String>> contentList = new ArrayList<>();
    /**
     * 单个数据的宽度
     */
    private List<Integer> itemWidthList = new ArrayList<>();
    /**
     *  横轴上的数据 月份
     */
    private List<String> rowDataList = new ArrayList<>();

    /**
     * 没有计划工时的适配器
     */
    private AbstractPanelListWithOutPlanAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_time_month_statistics);
        WRApplication.getInstance().addActivity(this);
        initRowDataList();
        initItemWidthList();
        currentMonth = getIntent().getIntExtra("month", -1);
        currentYear = getIntent().getIntExtra("year", -1);
        currentMonthStr = getIntent().getStringExtra("monthStr");

        mViewDataBinding.worktimeYearActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkTimeMonthStatisticsActivity.this.finish();
            }
        });

        mViewDataBinding.idLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(WorkTimeMonthStatisticsActivity.this, "你选中的position为：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        initAdapter();
        mViewDataBinding.worktimeYearActionbar.actionBarTitle.setText(currentMonthStr + "工时统计");
        mViewDataBinding.idPlRoot.setAdapter(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new AbstractPanelListWithOutPlanAdapter(this, mViewDataBinding.idPlRoot, mViewDataBinding.idLvContent,this) {
            @Override
            protected BaseAdapter getContentAdapter() {
                return null;
            }
        };
        mAdapter.setInitPosition(0);
        mAdapter.setSwipeRefreshEnabled(false);
        mAdapter.setTitle("姓名");// optional
        // mAdapter.setOnRefreshListener(new CustomRefreshListener());// optional
        mAdapter.setContentDataList(contentList);// must have
        mAdapter.setItemWidthList(itemWidthList);// must have
        mAdapter.setColumnDataList(columnData);
        mAdapter.setRowDataList(rowDataList);// must have
        mAdapter.setItemHeight(40);// optional, dp
    }


    @Override
    protected void onResume() {
        super.onResume();
        initWorkTimeData();
    }

    /**
     * 初始化横轴数据，即，当月周数
     */
    private void initRowDataList() {
        rowDataList.add("第一周");
        rowDataList.add("第二周");
        rowDataList.add("第三周");
        rowDataList.add("第四周");
        rowDataList.add("第五周");
        rowDataList.add("总计");
    }

    /**
     * 根据周数确定单列的宽度是多少
     */
    private void initItemWidthList() {
        for (int i = 0; i < rowDataList.size(); i++) {
            itemWidthList.add(100);
        }
    }

    /**
     * 初始化工时数据。
     * 网络请求使用的参数为intent传递过来的年，intent传递过来的当前月，
     */
    private void initWorkTimeData() {
        LogT.d(" current year " + currentYear + " current month " + currentMonth);
        ApiService.Utils.getInstance(this).getMonthWorkTime(currentYear, currentMonth)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<WeeklyWorkTimeBean>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<List<WeeklyWorkTimeBean>> listResult) {
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
     * 计算获取全部人员的工时信息
     * 给纵轴添加数据(人) rowDataList
     * 给横轴添加对应人的工时信息 contentList
     * @param list
     */
    private void caculateStatisticData(List<WeeklyWorkTimeBean> list) {

        for (WeeklyWorkTimeBean workTimeBean : list) {
            LogT.d(" add person " + workTimeBean.getFullName());
            List<String> data = new ArrayList<>();

            for (String month : rowDataList) {
                switch (month) {
                    case "第一周":
                        data.add(String.valueOf(workTimeBean.getWeekOne()));
                        break;
                    case "第二周":
                        data.add(String.valueOf(workTimeBean.getWeekTow()));
                        break;
                    case "第三周":
                        data.add(String.valueOf(workTimeBean.getWeekThree()));
                        break;
                    case "第四周":
                        data.add(String.valueOf(workTimeBean.getWeekFour()));
                        break;
                    case "第五周":
                        data.add(String.valueOf(workTimeBean.getWeekFive()));
                        break;
                    case "总计":
                        data.add(String.valueOf(workTimeBean.getWeekOne()
                                + workTimeBean.getWeekTow()
                                + workTimeBean.getWeekThree()
                                + workTimeBean.getWeekFour()
                                + workTimeBean.getWeekFive()));
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
    public void monthClick(int postion) {

    }

    @Override
    public void workTimePlanClick(int position) {

    }
}
