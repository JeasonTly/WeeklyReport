package com.aorise.weeklyreport.activity.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.adapter.HeaderItemRecyclerAdapter;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.databinding.FragmentMemeberCheckBinding;
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
 * Use the {@link NextWeekReprotManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NextWeekReprotManagerFragment extends Fragment implements BaseRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentMemeberCheckBinding mViewDataBinding;
    private int userId = 2;
    private int projectId = 1;
    private int weeks = 28;

    private List<HeaderItemBean.PlanDetailsListBean> memberWeeklyModelListBeans = new ArrayList<>();
    private HeaderItemRecyclerAdapter mHeaderAdapter;
    private HeaderItemBean mHeaderItemBean;

    public NextWeekReprotManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NextWeekReprotManagerFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static NextWeekReprotManagerFragment newInstance(int useId, int projectId) {
        NextWeekReprotManagerFragment fragment = new NextWeekReprotManagerFragment();
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
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_memeber_check, container, false);
        mViewDataBinding.nextReportPlt.setCanLoadMore(false);
        mViewDataBinding.nextReportPlt.setRefreshListener(this);
        mViewDataBinding.nextReportRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeaderAdapter = new HeaderItemRecyclerAdapter(getActivity(),memberWeeklyModelListBeans);
        mViewDataBinding.nextReportRecycler.setAdapter(mHeaderAdapter);
        return mViewDataBinding.getRoot();
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
                            mHeaderItemBean = o.getData();
                            if (memberWeeklyModelListBeans != null && memberWeeklyModelListBeans.size() != 0) {
                                memberWeeklyModelListBeans = o.getData().getPlanDetailsList();
                            } else {
                                memberWeeklyModelListBeans.clear();
                                memberWeeklyModelListBeans.addAll(o.getData().getPlanDetailsList());
                            }
                            HeaderItemBean.PlanDetailsListBean memberWeeklyModelListBean = new HeaderItemBean.PlanDetailsListBean();
                            memberWeeklyModelListBean.setPhase("整体情况");
                            memberWeeklyModelListBeans.add(memberWeeklyModelListBean);
                            LogT.d("当前" + TimeUtil.getInstance().getDayofWeek() + ".....周的周报计划数目为" + memberWeeklyModelListBeans.size());
                            mHeaderAdapter.updateData(memberWeeklyModelListBeans);
                        }
                    }
                });
    }

    @Override
    public void refresh() {
        updateManagerList(weeks);
        mViewDataBinding.nextReportPlt.finishRefresh();
    }

    @Override
    public void loadMore() {

    }
}
