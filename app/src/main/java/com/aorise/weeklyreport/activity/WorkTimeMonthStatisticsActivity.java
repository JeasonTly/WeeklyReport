package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.WeeklyWorkTimeBean;
import com.aorise.weeklyreport.databinding.ActivityWorkTimeMonthStatisticsBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

import java.util.ArrayList;
import java.util.List;

import sysu.zyb.panellistlibrary.AbstractPanelListWithOutPlanAdapter;

public class WorkTimeMonthStatisticsActivity extends AppCompatActivity {
    private ActivityWorkTimeMonthStatisticsBinding mViewDataBinding;
    private int currentYear = 2019;
    private int currentMonth = 8;
    private String currentMonthStr = "";

    private List<String> columnData = new ArrayList<>();
    private List<List<String>> contentList = new ArrayList<>();
    private List<Integer> itemWidthList = new ArrayList<>();
    private List<String> rowDataList = new ArrayList<>();
    private List<String> planDataList = new ArrayList<>();

    private AbstractPanelListWithOutPlanAdapter mAdapter;
    private String set_workTime_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_work_time_month_statistics);
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
        mAdapter = new AbstractPanelListWithOutPlanAdapter(this, mViewDataBinding.idPlRoot, mViewDataBinding.idLvContent) {
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
        mAdapter.setItemHeight(40);// optional, dp
    }


    @Override
    protected void onResume() {
        super.onResume();
        initWorkTimeData();
    }

    private void initRowDataList() {
        rowDataList.add("第一周");
        rowDataList.add("第二周");
        rowDataList.add("第三周");
        rowDataList.add("第四周");
        rowDataList.add("第五周");
        rowDataList.add("总计");
    }

    private void initItemWidthList() {
        for (int i = 0; i < rowDataList.size(); i++) {
            itemWidthList.add(100);
        }
    }

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
     *
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
}
