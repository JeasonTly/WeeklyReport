package com.aorise.weeklyreport.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.StatisticBean;
import com.aorise.weeklyreport.databinding.ActivityProjectStatisticsBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

import java.util.List;

public class ProjectStatisticsActivity extends AppCompatActivity {
    private ActivityProjectStatisticsBinding mViewDataBinding;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_project_statistics);
        WRApplication.getInstance().addActivity(this);
        mViewDataBinding.statisticActionbar.actionBarTitle.setText("项目统计");
        mViewDataBinding.statisticActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectStatisticsActivity.this.finish();
            }
        });
        projectId = getIntent().getIntExtra("projectId", -1);
        ApiService.Utils.getInstance(this).getStatisticInfoByID(projectId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<StatisticBean>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<List<StatisticBean>> listResult) {
                        super.onNext(listResult);
                        LogT.d("获取到的统计信息列表为 " + listResult.toString());
                        if (listResult.isRet()) {
                            WindowManager wm = (WindowManager) (ProjectStatisticsActivity.this.getSystemService(Context.WINDOW_SERVICE));
                            DisplayMetrics dm = new DisplayMetrics();
                            wm.getDefaultDisplay().getMetrics(dm);
                            int mScreenWidth = dm.widthPixels;
                            LogT.d("mScreenWidth is " + mScreenWidth);
                            mViewDataBinding.charView.setData(listResult.getData(), mScreenWidth);
                        }
                    }
                });
    }
}
