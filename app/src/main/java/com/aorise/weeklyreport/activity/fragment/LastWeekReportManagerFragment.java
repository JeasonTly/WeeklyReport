package com.aorise.weeklyreport.activity.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.OverAllSituationActivity;
import com.aorise.weeklyreport.activity.WeeklyReportDetailActivity;
import com.aorise.weeklyreport.adapter.ProjectManagerReportRecclerAdapter;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.AuditReportBean;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.databinding.FragmentHeaderBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * 周报管理系统的项目周报总结
 */
public class LastWeekReportManagerFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    /**
     * 审批状态。完成程度
     */
    private int reamarkStatus = 1;
    // TODO: Rename and change types of parameters

    private FragmentHeaderBinding mViewDataBinding;
    private int userId = -1;
    /**
     * 项目ID
     */
    private int projectId = -1;
    /**
     * 当前周数
     */
    private int where=0;
    private int weeks = -1;
private String approvalText;
    /**
     * 项目周报列表信息
     */
    private List<HeaderItemBean.PlanDetailsListBean> memberWeeklyModelListBeans = new ArrayList<>();
    /**
     *  项目列表适配器
     */
    private ProjectManagerReportRecclerAdapter mAdapter;
    /**
     *  项目整体情况信息
     *  整体情况和项目周报列表分开。
     *  未使用复合型适配器
     */
    private HeaderItemBean mHeaderItemBean;

    public LastWeekReportManagerFragment() {
        // Required empty public constructor
    }

    /**
     * 初始化此fragment .
     * @param useId 用户ID
     * @param projectId 项目ID
     * @param weeks  默认周数
     */
    // TODO: Rename and change types and number of parameters
    public static LastWeekReportManagerFragment newInstance(int useId, int projectId, int weeks,int where) {
        LastWeekReportManagerFragment fragment = new LastWeekReportManagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, useId);
        args.putInt(ARG_PARAM2, projectId);
        args.putInt(ARG_PARAM3, weeks);
        args.putInt(ARG_PARAM4,where);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_PARAM1);
            projectId = getArguments().getInt(ARG_PARAM2);
            weeks = getArguments().getInt(ARG_PARAM3);
            where = getArguments().getInt(ARG_PARAM4);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_header, container, false);
        mViewDataBinding.lastReportRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProjectManagerReportRecclerAdapter(getActivity(), memberWeeklyModelListBeans);
        mViewDataBinding.lastReportRecycler.addItemDecoration(new SpacesItemDecoration(9));
        mViewDataBinding.lastReportRecycler.setAdapter(mAdapter);
        /**
         * 项目周报整体情况总结
         */
        mViewDataBinding.lastOverall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("weeks", weeks);
                mIntent.putExtra("type", 1);
                mIntent.setClass(getActivity(), OverAllSituationActivity.class);
                startActivity(mIntent);
            }
        });
        if (where==1) {
            mViewDataBinding.auditArea.setVisibility(View.VISIBLE);
            mViewDataBinding.lastOverall.setClickable(false);
        }
        mViewDataBinding.allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApproveDialog(true);
            }
        });
        mViewDataBinding.notAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApproveDialog(true);
            }
        });
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogT.d("onResume");
        updateManagerList(weeks);
    }

    /**
     * 根据周数获取最新的项目周报内容
     * @param weeks
     */
    public synchronized void updateManagerList(int weeks) {
        this.weeks = weeks;
        LogT.d("project id is " + projectId + " weeks is " + weeks);
        ApiService.Utils.getInstance(getContext()).getHeaderList(projectId, weeks, 1)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<HeaderItemBean>>(this.getContext()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("错误" + e.toString());
                        memberWeeklyModelListBeans.clear();
                        mAdapter.refreshData(memberWeeklyModelListBeans);
                    }

                    @Override
                    public void onNext(Result<HeaderItemBean> o) {
                        super.onNext(o);
                        LogT.d("项目周报 " + o.toString());
                        if (o.isRet()) {
                           // LogT.d(" o " + o.getData().getPlanDetailsList().size());
                            mHeaderItemBean = o.getData();
                            if (memberWeeklyModelListBeans != null && memberWeeklyModelListBeans.size() != 0) {
                                memberWeeklyModelListBeans = o.getData().getPlanDetailsList();
                            } else {
                                memberWeeklyModelListBeans.clear();
                                memberWeeklyModelListBeans.addAll(o.getData().getPlanDetailsList());
                            }
                            LogT.d(" 具体周报列表数为"+o.getData().getPlanDetailsList().size());
                            mViewDataBinding.setLastStage(mHeaderItemBean.getPercentComplete() +"%");
                            mViewDataBinding.setLastSpecificThings(TextUtils.isEmpty(mHeaderItemBean.getOverallSituation()) ? "未填写" : mHeaderItemBean.getOverallSituation());
                            mAdapter.refreshData(o.getData().getPlanDetailsList());
                        }
                    }
                });
    }
    private void showApproveDialog(final boolean pass) {
        View inputView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_input_contentview, null);
        final EditText approvalMark = (EditText) inputView.findViewById(R.id.approval_mark);
        final RadioGroup radioGroup = (RadioGroup) inputView.findViewById(R.id.remark_level_radio);
        final RadioButton remarkHigh = (RadioButton) inputView.findViewById(R.id.remark_high);
        remarkHigh.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.remark_high:
                        reamarkStatus = 1;
                        break;
                    case R.id.remark_middle:
                        reamarkStatus = 2;
                        break;
                    case R.id.remark_low:
                        reamarkStatus = 3;
                        break;
                }
            }
        });
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity())
                .setTitle("周报审批")
                .setView(inputView)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        approvalText = approvalMark.getText().toString();
                        CommitApproveResult(pass);
                    }
                });
        mDialog.create().show();
    }

    private void CommitApproveResult(boolean pass) {
        //审批状态1,未审批，2,已通过，3,驳回
        int approvestatus = pass ? 2 : 3;
        //approvalText = pass ? "通过" : "不通过";
        LogT.d(" param id = " + mHeaderItemBean.getId() + " stauts is "  + " approvalText " + approvalText);
        Gson gson = new Gson();
        AuditReportBean mModel = new AuditReportBean();
        mModel.setWeeklyId(mHeaderItemBean.getId());//周报ID
        //mModel.setPlanStatus(workStatus);//项目周报上的完成状态
        mModel.setRemark(approvalText);//备注
        mModel.setRemarkState(reamarkStatus);
        mModel.setStatue(approvestatus);//审批状态
        mModel.setWeeklyType(1);
        String json = gson.toJson(mModel);
        LogT.d("json is " + json);
        RequestBody model = CommonUtils.getRequestBody(json);
        ApiService.Utils.getInstance(getActivity()).approvalWeeklyReport(model)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result>(getActivity()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtils.show("审批失败");
                    }

                    @Override
                    public void onNext(Result o) {
                        super.onNext(o);
                        LogT.d(" o answer is " + o.toString());
                        if (o.isRet()) {
                            ToastUtils.show("审批成功");
                            getActivity().finish();
                        } else {
                            ToastUtils.show("审批失败");
                        }
                    }
                });
    }
}
