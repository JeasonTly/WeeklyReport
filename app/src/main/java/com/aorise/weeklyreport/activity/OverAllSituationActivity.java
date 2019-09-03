package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.UploadManagerReport;
import com.aorise.weeklyreport.databinding.ActivityOverAllSituationBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import okhttp3.RequestBody;

/**
 * 整体情况
 */

public class OverAllSituationActivity extends AppCompatActivity {
    private ActivityOverAllSituationBinding mViewDataBinding;
    private int projectId;
    private int managerReportId = -1;
    private int weeks;
    private int type;
    private HeaderItemBean mBean;
    private boolean updateOverallSituation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_over_all_situation);
        WRApplication.getInstance().addActivity(this);


        projectId = getIntent().getIntExtra("projectId", -1);
        weeks = getIntent().getIntExtra("weeks", -1);
        type = getIntent().getIntExtra("type", 1);
        LogT.d(" OverAllSituation projectId is " + projectId + " weeks is " + weeks + " type is " + type);
        mViewDataBinding.overallSituationActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OverAllSituationActivity.this.finish();
            }
        });
        mViewDataBinding.overallSituationActionbar.actionBarTitle.setText("项目整体情况");
        mViewDataBinding.overallSituationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogT.d(" updateOverallSituation " + updateOverallSituation);
                if (updateOverallSituation) {
                    putCommand();
                } else {
                    postCommand();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryBean();

    }

    /**
     * 查询整体情况
     */
    private void queryBean() {
        ApiService.Utils.getInstance(this).getHeaderList(projectId, weeks, type)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<HeaderItemBean>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("错误" + e.toString());
                    }

                    @Override
                    public void onNext(Result<HeaderItemBean> o) {
                        super.onNext(o);
                        LogT.d("整体情况" + o.toString());
                        if (o.isRet()) {
                            LogT.d(" 整体情况列表 " + o.getData().getPlanDetailsList().size());
                            mBean = o.getData();
                            LogT.d("HeaderItemBean info is " + mBean.toString());
                            if (mBean != null) {
                                updateOverallSituation = (mBean.getId() != 0);
                                managerReportId = mBean.getId();
                                mViewDataBinding.oveallSituationPercent.setText(mBean.getPercentComplete() + "%");
                                mViewDataBinding.oveallSituationSpth.setText(mBean.getOverallSituation());
                            }
                        }
                    }
                });
    }

    private void postCommand() {
        if (TextUtils.isEmpty(mViewDataBinding.oveallSituationSpth.getText())) {
            ToastUtils.show("具体工作事项不能为空!");
            return;
        }
        Gson gson = new Gson();
        UploadManagerReport managerReport = new UploadManagerReport();
        managerReport.setProjectId(projectId);
//        if (type == 2) {
//            weeks++;
//        }
        managerReport.setByWeek(type == 2 ? (weeks + 1) : weeks);
        managerReport.setOverallSituation(mViewDataBinding.oveallSituationSpth.getText().toString());
        managerReport.setPercentComplete(mBean.getPercentComplete());
        managerReport.setType(type);
        String json = gson.toJson(managerReport);
        RequestBody requestBody = CommonUtils.getRequestBody(json);

        ApiService.Utils.getInstance(this).
                postHeaderReport(requestBody)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<Integer>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.show("项目负责人周报提交失败!");
                    }

                    @Override
                    public void onNext(Result<Integer> data) {
                        super.onNext(data);
                        if (data.isRet()) {
                            ToastUtils.show("项目负责人周报提交成功!");
                            OverAllSituationActivity.this.finish();
                        } else {
                            ToastUtils.show("项目负责人周报提交失败!");
                        }
                    }
                });
    }

    private void putCommand() {
        if (TextUtils.isEmpty(mViewDataBinding.oveallSituationSpth.getText())) {
            ToastUtils.show("具体工作事项不能为空!");
            return;
        }
        Gson gson = new Gson();
        UploadManagerReport managerReport = new UploadManagerReport();
        managerReport.setProjectId(projectId);
        managerReport.setId(managerReportId);
        managerReport.setByWeek(weeks);
        managerReport.setOverallSituation(mViewDataBinding.oveallSituationSpth.getText().toString());
        managerReport.setPercentComplete(mBean.getPercentComplete());
        managerReport.setType(type);

        String json = gson.toJson(managerReport);
        RequestBody requestBody = CommonUtils.getRequestBody(json);

        ApiService.Utils.getInstance(this).
                putHeaderReport(requestBody)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<Integer>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.show("项目负责人周报修改失败!");
                    }

                    @Override
                    public void onNext(Result<Integer> data) {
                        super.onNext(data);
                        if (data.isRet()) {
                            ToastUtils.show("项目负责人周报修改成功!");
                            OverAllSituationActivity.this.finish();
                        } else {
                            ToastUtils.show("项目负责人周报修改失败!");
                        }
                    }
                });
    }
}
