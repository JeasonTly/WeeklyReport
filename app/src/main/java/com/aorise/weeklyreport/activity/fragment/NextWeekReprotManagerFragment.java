package com.aorise.weeklyreport.activity.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.OverAllSituationActivity;
import com.aorise.weeklyreport.adapter.MulityStageRecyclerAdapter;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MulityTypeItem;
import com.aorise.weeklyreport.databinding.FragmentMemeberCheckBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link NextWeekReprotManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class NextWeekReprotManagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters

    private FragmentMemeberCheckBinding mViewDataBinding;
    private int projectId = -1;
    private int weeks = -1;

    private List<HeaderItemBean.PlanDetailsListBean> memberWeeklyModelListBeans = new ArrayList<>();
    private MulityStageRecyclerAdapter mAdapter;
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
    public static NextWeekReprotManagerFragment newInstance(int useId, int projectId, int weeks) {
        NextWeekReprotManagerFragment fragment = new NextWeekReprotManagerFragment();
        Bundle args = new Bundle();
        // args.putInt(ARG_PARAM1, useId);
        args.putInt(ARG_PARAM2, projectId);
        args.putInt(ARG_PARAM3, weeks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            userId = getArguments().getInt(ARG_PARAM1);
            projectId = getArguments().getInt(ARG_PARAM2);
            weeks = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_memeber_check, container, false);

        mViewDataBinding.nextReportRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MulityStageRecyclerAdapter(getActivity(), memberWeeklyModelListBeans);
        mViewDataBinding.nextReportRecycler.addItemDecoration(new SpacesItemDecoration(9));
        mViewDataBinding.nextReportRecycler.setAdapter(mAdapter);

        mViewDataBinding.nextOverall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("weeks", weeks + 1);
                mIntent.putExtra("type", 2);
                mIntent.setClass(getActivity(), OverAllSituationActivity.class);
                startActivity(mIntent);
            }
        });

        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateManagerList(weeks);
        LogT.d("onResume");
    }


    public synchronized void updateManagerList(final int weeks) {
        this.weeks = weeks;
        LogT.d(" project id is " + projectId + " weeks is " + weeks);
        ApiService.Utils.getInstance(getContext()).getPlanHeaderList(projectId, weeks + 1, 2)
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
                            LogT.d("当前" + (weeks + 1) + ".....周的周报计划数目为" + memberWeeklyModelListBeans.size());

                            mViewDataBinding.setNextStage(mHeaderItemBean.getPercentComplete() + "%");
                            mViewDataBinding.setNextSpecificThings(TextUtils.isEmpty(mHeaderItemBean.getOverallSituation()) ? "未填写" : mHeaderItemBean.getOverallSituation());
                            mAdapter.refreshData(o.getData().getPlanDetailsList());
                        }
                    }
                });
    }

}
