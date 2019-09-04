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
import com.aorise.weeklyreport.adapter.ProjectManagerReportRecclerAdapter;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.databinding.FragmentMemeberCheckBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目周报下周工作计划
 *
 */


public class NextWeekReprotManagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters

    private FragmentMemeberCheckBinding mViewDataBinding;
    /**
     * 项目ID
     */
    private int projectId = -1;
    /**
     * 当前周数
     */
    private int weeks = -1;

    /**
     * 项目周报 下周计划列表
     */
    private List<HeaderItemBean.PlanDetailsListBean> memberWeeklyModelListBeans = new ArrayList<>();
    private ProjectManagerReportRecclerAdapter mAdapter;
    /**
     * 整体情况
     */
    private HeaderItemBean mHeaderItemBean;

    public NextWeekReprotManagerFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param useId      用户ID 暂时无效
     * @param projectId  项目ID
     * @param weeks   当前周数
     * @return
     */
    public static NextWeekReprotManagerFragment newInstance(int useId, int projectId, int weeks) {
        NextWeekReprotManagerFragment fragment = new NextWeekReprotManagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, projectId);
        args.putInt(ARG_PARAM3, weeks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        mAdapter = new ProjectManagerReportRecclerAdapter(getActivity(), memberWeeklyModelListBeans);
        mViewDataBinding.nextReportRecycler.addItemDecoration(new SpacesItemDecoration(9));
        mViewDataBinding.nextReportRecycler.setAdapter(mAdapter);

        mViewDataBinding.nextOverall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("weeks", weeks + 1);
                mIntent.putExtra("type", 2);
                mIntent.setClass(getActivity(), OverAllSituationActivity.class);//整体情况
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

    /**
     * 根据周数更新当前项目周报计划的列表信息
     * @param weeks
     */
    public synchronized void updateManagerList(final int weeks) {
        this.weeks = weeks;
        LogT.d(" project id is " + projectId + " weeks is " + weeks);
        ApiService.Utils.getInstance(getContext()).getHeaderList(projectId, weeks + 1, 2)
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
                        LogT.d("当前项目周报 计划列表result " + o.toString());
                        if (o.isRet()) {
                            LogT.d(" 当前项目周报 计划列表长度为 " + o.getData().getPlanDetailsList().size());
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
