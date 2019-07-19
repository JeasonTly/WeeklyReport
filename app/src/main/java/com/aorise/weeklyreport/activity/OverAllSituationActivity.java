package com.aorise.weeklyreport.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.databinding.ActivityOverAllSituationBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;

public class OverAllSituationActivity extends AppCompatActivity {
    private ActivityOverAllSituationBinding mViewDataBinding;
    private int projectId;
    private int weeks;
    private int type;
    private HeaderItemBean mBean;
    private boolean updateOverallSituation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_over_all_situation);
        WRApplication.getInstance().addActivity(this);

        mBean = (HeaderItemBean) getIntent().getBundleExtra("item_detail").get("detail");
        LogT.d("mBean info is " + mBean.toString());
        if (mBean != null) {
            updateOverallSituation = (mBean.getId() != 0);
            mViewDataBinding.oveallSituationPercent.setText(mBean.getPercentComplete() + "%");
            mViewDataBinding.oveallSituationSpth.setText(mBean.getOverallSituation());
        }
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

    private void postCommand() {
        ApiService.Utils.getInstance(this).
                postHeaderReport(projectId,weeks,type,mViewDataBinding.oveallSituationSpth.getText().toString(),mBean.getPercentComplete())
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
                        if(data.isRet()){
                            ToastUtils.show("项目负责人周报提交成功!");
                            OverAllSituationActivity.this.finish();
                        }else{
                            ToastUtils.show("项目负责人周报提交失败!");
                        }
                    }
                });
    }

    private void putCommand() {
        ApiService.Utils.getInstance(this).
                putHeaderReport(projectId,weeks,type,mViewDataBinding.oveallSituationSpth.getText().toString(),mBean.getPercentComplete())
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
                        if(data.isRet()){
                            ToastUtils.show("项目负责人周报提交成功!");
                            OverAllSituationActivity.this.finish();
                        }else{
                            ToastUtils.show("项目负责人周报提交失败!");
                        }
                    }
                });
    }
}
