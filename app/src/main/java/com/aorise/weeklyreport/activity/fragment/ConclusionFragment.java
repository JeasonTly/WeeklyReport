package com.aorise.weeklyreport.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.adapter.WorkTypeRecyclerAdapter;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.MulityTypeItem;
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
public class ConclusionFragment extends Fragment implements BaseRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private FragmentConclusionBinding mViewDataBinding;
    // TODO: Rename and change types of parameters
//    private OnFragmentInteractionListener mListener;
    private int userId = 2;
    private int projectId = 1;
    private int weeks = 28;
    private boolean isManagerMode = false;
    //    private OnFragmentInteractionListener mListener;
    private List<WeeklyReportBean> mPlanWeeklyReport = new ArrayList<>();
    private List<MulityTypeItem> mMulityTypeList = new ArrayList<>();
    private WorkTypeRecyclerAdapter mAdapter;


    public ConclusionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConclusionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConclusionFragment newInstance(int projectId, int userId, int weeks,boolean isManagerMode) {
        ConclusionFragment fragment = new ConclusionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
        args.putInt(ARG_PARAM2, userId);
        args.putInt(ARG_PARAM3, weeks);
        args.putBoolean(ARG_PARAM4, isManagerMode);
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
            isManagerMode = getArguments().getBoolean(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_conclusion, container, false);
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_conclusion, container, false);
        mViewDataBinding.summaryPlt.setRefreshListener(this);
        mViewDataBinding.summaryPlt.setCanLoadMore(false);
        mViewDataBinding.summaryRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new WorkTypeRecyclerAdapter(getContext(), mMulityTypeList);

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
        LogT.d("projectId is " + projectId + " userId is " + userId + " weeks is " + weeks);
        if (isManagerMode) {
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
                                LogT.d("当前" + TimeUtil.getInstance().getDayofWeek() + "周的周报总结数目为" + mPlanWeeklyReport.size());
                                mMulityTypeList = CommonUtils.getInstance().resortWorkTypeMulityTypeList(mPlanWeeklyReport);
                                LogT.d("size is " + mMulityTypeList.size());
                                mAdapter.refreshData(mMulityTypeList);
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
                                LogT.d("当前" + TimeUtil.getInstance().getDayofWeek() + "周的周报总结数目为" + mPlanWeeklyReport.size());
                                mMulityTypeList = CommonUtils.getInstance().resortWorkTypeMulityTypeList(mPlanWeeklyReport);
                                LogT.d("size is " + mMulityTypeList.size());
                                mAdapter.refreshData(mMulityTypeList);
                            }
                        }
                    });
        }
    }

}
