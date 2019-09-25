package com.aorise.weeklyreport.activity.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.WeeklyReportDetailActivity;
import com.aorise.weeklyreport.adapter.RecyclerListClickListener;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.adapter.WorkTypeRecyclerAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.WeeklyReportBean;
import com.aorise.weeklyreport.databinding.FragmentConclusionBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是为总结使用的fragment，查看成员周报和审批成员周报时有使用此fragment
 */
public class ConclusionFragment extends Fragment implements BaseRefreshListener, RecyclerListClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    private FragmentConclusionBinding mViewDataBinding;
    // TODO: Rename and change types of parameters
//    private OnFragmentInteractionListener mListener;
    /**
     * 用户ID。如果初始化方式为newInstance 则为某个成员ID
     * 反之则为本人ID 通过sharedPreference获取
     */
    private int userId = -1;
    /**
     * 项目ID
     * 如果初始化方式为newInstance 则为某个项目的ID
     */
    private int projectId = -1;
    /**
     * 选择的周数，可通过审批或者查看周报的Activity进行update
     */
    private int weeks = -1;
    /**
     * 判断是否为项目负责人查看周报
     */
    private boolean isAuditMode = false;
    /**
     * 是否可以进行周报编辑，存在情况:
     * 其他成员查看未审核周报时，不可以修改周报内容
     */
    private boolean canAudit = true;
    /**
     * 周报信息
     */
    private List<WeeklyReportBean> mPlanWeeklyReport = new ArrayList<>();
    /**
     * recyclerview的适配器
     */
    private WorkTypeRecyclerAdapter mAdapter;


    public ConclusionFragment() {
        // Required empty public constructor
        weeks = TimeUtil.getInstance().getDayofWeek();
    }


    /**
     * 审核周报界面的初始化
     *
     * @param projectId   项目ID
     * @param userId      用户ID
     * @param weeks       选择的周数
     * @param isAuditMode 是否为项目负责人
     * @return 返回工作总结Fragment
     */
    public static ConclusionFragment newInstance(int projectId, int userId, int weeks, boolean isAuditMode, boolean canAudit) {
        ConclusionFragment fragment = new ConclusionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
        args.putInt(ARG_PARAM2, userId);
        args.putInt(ARG_PARAM3, weeks);
        args.putBoolean(ARG_PARAM4, isAuditMode);
        args.putBoolean(ARG_PARAM5, canAudit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt(ARG_PARAM1);
            userId = getArguments().getInt(ARG_PARAM2);
            weeks = getArguments().getInt(ARG_PARAM3);
            isAuditMode = getArguments().getBoolean(ARG_PARAM4);
            canAudit = getArguments().getBoolean(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_conclusion, container, false);
        mViewDataBinding.summaryPlt.setRefreshListener(this);
        mViewDataBinding.summaryPlt.setCanLoadMore(false);
        //为查看周报所初始化的本fragment
        if (!isAuditMode) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            userId = sharedPreferences.getInt("userId", 2);
        }

        mViewDataBinding.summaryRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new WorkTypeRecyclerAdapter(getContext(), mPlanWeeklyReport, this);
        mViewDataBinding.summaryRecycler.addItemDecoration(new SpacesItemDecoration(8));
        mViewDataBinding.summaryRecycler.setAdapter(mAdapter);

        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList(weeks);
    }

    @Override
    public void refresh() {
        updateList(weeks);
        mViewDataBinding.summaryPlt.finishRefresh();
    }

    @Override
    public void loadMore() {

    }

    /**
     * 审批界面或者周报查看界面在点击Actionbar的Title时，
     * 重新发起网络请求获取对应周数的周报信息
     *
     * @param weeks
     */
    public synchronized void update(int weeks) {
        updateList(weeks);
    }

    /**
     * 审批界面或者周报查看界面在点击Actionbar的Title时，
     * 重新发起网络请求获取对应周数的周报信息
     *
     * @param weeks
     */
    public synchronized void updateList(int weeks) {
        this.weeks = weeks;
        LogT.d("projectId is " + projectId + " userId is " + userId + " weeks is " + weeks + " 是否为项目负责人 " + isAuditMode);
        if (isAuditMode) {
            ApiService.Utils.getInstance(getContext()).getWeeklyReport(projectId, userId, weeks, 1)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<List<WeeklyReportBean>>>(this.getContext()) {
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
                        public void onNext(Result<List<WeeklyReportBean>> o) {
                            super.onNext(o);
                            if (o.isRet()) {
                                mPlanWeeklyReport.clear();
                                mPlanWeeklyReport.addAll(o.getData());
                                LogT.d("项目负责人根据用户ID审核周报界面 周报总结数目为" + mPlanWeeklyReport.size());
                                mAdapter.refreshData(o.getData());
                            }
                        }
                    });
        } else {
            ApiService.Utils.getInstance(getContext()).getWeeklyReport(userId, weeks, 1)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<List<WeeklyReportBean>>>(this.getContext()) {
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
                        public void onNext(Result<List<WeeklyReportBean>> o) {
                            super.onNext(o);
                            if (o.isRet()) {
                                mPlanWeeklyReport.clear();
                                mPlanWeeklyReport.addAll(o.getData());
                                LogT.d("自己查看周报界面，当前周的周报总结数目为" + mPlanWeeklyReport.size());
                                mAdapter.refreshData(o.getData());
                            }
                        }
                    });
        }
    }

    /**
     * RecycleView 即周报列表的点击事件
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        Intent mIntent = new Intent();
        mIntent.setClass(getContext(), WeeklyReportDetailActivity.class);
        mIntent.putExtra("reportId", mPlanWeeklyReport.get(position).getId());
        mIntent.putExtra("isAuditMode", isAuditMode);
        mIntent.putExtra("canAudit", canAudit);
        startActivity(mIntent);
    }

    @Override
    public void onLongClick(final int position) {
        if (!canAudit) {
            LogT.d("您不可修改此成员周报");
           // ToastUtils.show("您没有权限删除此周报!");
            return;
        }
        if (isAuditMode) {
            LogT.d("审批周报不可删除");
           // ToastUtils.show("您没有权限删除此周报!");
            return;
        }

        if (mPlanWeeklyReport.get(position).getApprovalState() == 2) {
            ToastUtils.show("此周报已完成审批，不可删除!");
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("警告!")
                .setMessage("您确定要删除此条周报吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiService.Utils.getInstance(getActivity())
                                .deleteWeeklyReprot(mPlanWeeklyReport.get(position).getId())
                                .compose(ApiService.Utils.schedulersTransformer())
                                .subscribe(new CustomSubscriber<Result>(getActivity()) {
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
                                        if (o.isRet()) {
                                            ToastUtils.show("删除成功!");
                                            updateList(weeks);
                                        }
                                    }
                                });
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
