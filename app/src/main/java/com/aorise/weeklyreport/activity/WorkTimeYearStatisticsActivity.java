package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.PersonWorkTimeBean;
import com.aorise.weeklyreport.databinding.ActivityWorkTimeYearStatisticsBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sysu.zyb.panellistlibrary.AbstractPanelListAdapter;

public class WorkTimeYearStatisticsActivity extends AppCompatActivity {
    private ActivityWorkTimeYearStatisticsBinding mViewDataBinding;
    private int currentYear = 2019;

    private List<String> columnData = new ArrayList<>();
    private List<List<String>> contentList = new ArrayList<>();
    private List<Integer> itemWidthList = new ArrayList<>();
    private List<String> rowDataList = new ArrayList<>();

    private AbstractPanelListAdapter mAdapter;

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
        mAdapter = new AbstractPanelListAdapter(this, mViewDataBinding.idPlRoot, mViewDataBinding.idLvContent) {
            @Override
            protected BaseAdapter getContentAdapter() {
                return null;
            }
        };
        mAdapter.setInitPosition(10);
        mAdapter.setSwipeRefreshEnabled(false);
        mAdapter.setTitle("工时统计");// optional
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

    private void initWorkTimeData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        currentYear = Integer.valueOf(simpleDateFormat.format(date));
        LogT.d(" currentYear "+currentYear);
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

}
