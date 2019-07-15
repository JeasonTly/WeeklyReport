package com.aorise.weeklyreport.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.adapter.MulityStageRecyclerAdapter;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MulityTypeItem;
import com.aorise.weeklyreport.databinding.FragmentHeaderBinding;
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
 * Use the {@link LastWeekReportManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LastWeekReportManagerFragment extends Fragment implements BaseRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private FragmentHeaderBinding mViewDataBinding;
    private int userId = 2;
    private int projectId = 1;
    private int weeks = 28;

    private List<HeaderItemBean.PlanDetailsListBean> memberWeeklyModelListBeans = new ArrayList<>();
    private List<MulityTypeItem> mMulityTypeList = new ArrayList<>();
    private MulityStageRecyclerAdapter mAdapter;
    private HeaderItemBean mHeaderItemBean;

    public LastWeekReportManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LastWeekReportManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LastWeekReportManagerFragment newInstance(int useId, int projectId) {
        LastWeekReportManagerFragment fragment = new LastWeekReportManagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, useId);
        args.putInt(ARG_PARAM2, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_PARAM1);
            projectId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_header, container, false);
        mViewDataBinding.lastReportPlt.setCanLoadMore(false);
        mViewDataBinding.lastReportPlt.setRefreshListener(this);
        mViewDataBinding.lastReportRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MulityStageRecyclerAdapter(getActivity(),mMulityTypeList);
        mViewDataBinding.lastReportRecycler.setAdapter(mAdapter);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateManagerList(weeks);
    }

    public void updateManagerList(int weeks) {
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
                    }

                    @Override
                    public void onNext(Result<HeaderItemBean> o) {
                        super.onNext(o);
                        LogT.d(" O " + o.toString());
                        if (o.isRet()) {
                            LogT.d(" o " + o.getData().getPlanDetailsList().size());
                            mHeaderItemBean = o.getData();
                            if (memberWeeklyModelListBeans != null && memberWeeklyModelListBeans.size() != 0) {
                                memberWeeklyModelListBeans = o.getData().getPlanDetailsList();
                            } else {
                                memberWeeklyModelListBeans.clear();
                                memberWeeklyModelListBeans.addAll(o.getData().getPlanDetailsList());
                            }
                            mMulityTypeList = CommonUtils.getInstance().resortStage(memberWeeklyModelListBeans);

                            for (MulityTypeItem mulityTypeItem : mMulityTypeList) {
                                LogT.d("......111" + mulityTypeItem.toString());
                            }

                            mAdapter.refreshData(mMulityTypeList);
                        }
                    }
                });
    }

    @Override
    public void refresh() {
        updateManagerList(weeks);
        mViewDataBinding.lastReportPlt.finishRefresh();
    }

    @Override
    public void loadMore() {

    }
}