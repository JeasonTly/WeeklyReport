package com.aorise.weeklyreport.activity.fragment;

import android.content.Context;
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
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ConclusionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConclusionFragment extends Fragment implements BaseRefreshListener, RecyclerListClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5= "param5";

    private FragmentConclusionBinding mViewDataBinding;
    // TODO: Rename and change types of parameters
//    private OnFragmentInteractionListener mListener;
    private int userId = -1;
    private int projectId = -1;
    private int weeks = -1;
    private boolean isAuditMode = false;//这个判断来源是否为项目负责人查看周报
    private List<WeeklyReportBean> mPlanWeeklyReport = new ArrayList<>();
    private WorkTypeRecyclerAdapter mAdapter;
    private boolean canAudit = true; //是否可以审批

    public ConclusionFragment() {
        // Required empty public constructor
        weeks = TimeUtil.getInstance().getDayofWeek();
    }


    /**
     * 审核周报界面的初始化
     *
     * @param projectId     项目ID
     * @param userId        用户ID
     * @param weeks         选择的周数
     * @param isAuditMode 是否为项目负责人
     * @return 返回工作总结Fragment
     */
    public static ConclusionFragment newInstance(int projectId, int userId, int weeks, boolean isAuditMode,boolean canAudit) {
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
        if(!isAuditMode) {
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


    public void update(int weeks) {
        updateList(weeks);
    }

    public void updateList(int weeks) {
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
    public void onLongClick(int position) {

    }
}
