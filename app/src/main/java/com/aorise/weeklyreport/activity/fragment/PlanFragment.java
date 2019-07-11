package com.aorise.weeklyreport.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.adapter.BaseAdapter;
import com.aorise.weeklyreport.adapter.HeaderItemRecyclerAdapter;
import com.aorise.weeklyreport.adapter.WorkTypeRecyclerAdapter;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MulityTypeItem;
import com.aorise.weeklyreport.bean.WeeklyReportBean;
import com.aorise.weeklyreport.databinding.FragmentPlanBinding;
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
 * Use the {@link PlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanFragment extends Fragment implements BaseRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters

    private boolean isManager = true;
    private int userId = 2;
    private int projectId = 18;
    private int weeks = 27;
    private FragmentPlanBinding mViewDataBinding;
    //    private OnFragmentInteractionListener mListener;
    private List<WeeklyReportBean> mPlanWeeklyReport = new ArrayList<>();
    private List<MulityTypeItem> mMulityTypeList = new ArrayList<>();
    private List<HeaderItemBean.PlanDetailsListBean> memberWeeklyModelListBeans = new ArrayList<>();
    private HeaderItemRecyclerAdapter mHeaderAdapter;
    private WorkTypeRecyclerAdapter mAdapter;

    public PlanFragment() {
        // Required empty public constructor
        weeks = TimeUtil.getInstance().getDayofWeek();
    }

    /**
     * @return A new instance of fragment PlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanFragment newInstance(int projectId, int userId, int weeks) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
        args.putInt(ARG_PARAM2, userId);
        args.putInt(ARG_PARAM3, weeks);
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
            isManager = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan, container, false);
        mViewDataBinding.planPlt.setRefreshListener(this);
        mViewDataBinding.planPlt.setCanLoadMore(false);
        mViewDataBinding.planRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new WorkTypeRecyclerAdapter(getContext(), mMulityTypeList);
        mHeaderAdapter = new HeaderItemRecyclerAdapter(getContext(), memberWeeklyModelListBeans);
        if (isManager) {
            mViewDataBinding.planRecycler.setAdapter(mHeaderAdapter);
        } else {
            mViewDataBinding.planRecycler.setAdapter(mAdapter);
        }
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isManager) {
            updateManagerList(weeks);
        } else {
            updateList(weeks);
        }
    }

    @Override
    public void refresh() {
        LogT.d(" refresh " + isManager);
        if (isManager) {
            LogT.d(" refresh");
            updateManagerList(weeks);
        } else {
            updateList(weeks);
        }
        mViewDataBinding.planPlt.finishRefresh();
    }

    @Override
    public void loadMore() {

    }

    public void updateAdapter(boolean isNormalMode) {

       // mViewDataBinding.planRecycler.swapAdapter(isNormalMode ? mAdapter : mHeaderAdapter, false);
        if(isNormalMode){
            mViewDataBinding.planRecycler.setAdapter(mAdapter);
            isManager = false;
            updateList(weeks);
        }else {
            mViewDataBinding.planRecycler.setAdapter(mHeaderAdapter);
            isManager = true;
            updateManagerList(weeks);
        }
    }

    public void updateList(int weeks) {
        this.weeks = weeks;
        LogT.d(" weeks is " + weeks);
        ApiService.Utils.getInstance(getContext()).getWeeklyReport(userId, weeks, 2)
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
                            LogT.d("当前" + TimeUtil.getInstance().getDayofWeek() + ".....周的周报计划数目为" + mPlanWeeklyReport.size());
                            mMulityTypeList = CommonUtils.getInstance().resortWorkTypeMulityTypeList(mPlanWeeklyReport);
                            LogT.d("size is " + mMulityTypeList.size());
                            mAdapter.refreshData(mMulityTypeList);
                        }
                    }
                });
    }

    public void updateManagerList(int weeks) {
        this.weeks = weeks;
        LogT.d("project id is " + projectId + " weeks is " + weeks);
        ApiService.Utils.getInstance(getContext()).getHeaderList(projectId, weeks, 2)
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
                    }

                    @Override
                    public void onNext(Result<HeaderItemBean> o) {
                        super.onNext(o);
                        LogT.d(" O " + o.toString());
                        if (o.isRet()) {
                            LogT.d(" o " + o.getData().getPlanDetailsList().size());
                            if (memberWeeklyModelListBeans != null && memberWeeklyModelListBeans.size() != 0) {
                                memberWeeklyModelListBeans = o.getData().getPlanDetailsList();
                            } else {
                                memberWeeklyModelListBeans.clear();
                                memberWeeklyModelListBeans.addAll(o.getData().getPlanDetailsList());
                            }
                            HeaderItemBean.PlanDetailsListBean memberWeeklyModelListBean = new HeaderItemBean.PlanDetailsListBean();
                            memberWeeklyModelListBean.setPhase("整体情况");
                            memberWeeklyModelListBeans.add(memberWeeklyModelListBean);
                            LogT.d("当前" + TimeUtil.getInstance().getDayofWeek() + "周的周报计划数目为" + memberWeeklyModelListBeans.size());
                            mHeaderAdapter.updateData(memberWeeklyModelListBeans);
                        }
                    }
                });
    }
}
