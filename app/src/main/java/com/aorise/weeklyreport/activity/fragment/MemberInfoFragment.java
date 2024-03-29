package com.aorise.weeklyreport.activity.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.AuditWeeklyReportActivity;
import com.aorise.weeklyreport.adapter.MemberListAdapter;
import com.aorise.weeklyreport.adapter.RecyclerListClickListener;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.databinding.FragmentMemberInfoBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目概况-成员信息列表fragment
 */
public class MemberInfoFragment extends Fragment implements BaseRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private FragmentMemberInfoBinding mViewDataBinding;
    /**
     * 当前页
     */
    private int currenIndex = 1;
    /**
     * 一页包含多少数据
     */
    private int pageNumber = 10;
    /**
     *  总页数
     */
    private int totalPage = 1;
    /**
     * 是否为审核
     */
    private boolean isReview = false;
    /**
     * 成员列表适配器
     */
    private MemberListAdapter mAdapter;
    private List<MemberListBean.ListBean> mList = new ArrayList<>();
    /**
     * 项目ID
     */
    private int projectId = -1;
    /**
     * 当前周数
     */
    private int week = -1;

    public MemberInfoFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param projectId 项目id
     * @param weeks  当前周数
     * @param isReview   是否为可以审核周报
     * @return MemberInfoFragment实例
     */
    public static MemberInfoFragment newInstance(int projectId, int weeks, boolean isReview) {
        MemberInfoFragment fragment = new MemberInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, projectId);
        args.putInt(ARG_PARAM2, weeks);
        args.putBoolean(ARG_PARAM3, isReview);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getInt(ARG_PARAM1);
            week = getArguments().getInt(ARG_PARAM2);
            isReview = getArguments().getBoolean(ARG_PARAM3);
        }
    }

    /**
     * 更换项目
     * @param projectId
     */
    @Deprecated
    public void updateProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_info, container, false);
        mViewDataBinding.memberPlt.setRefreshListener(this);
        mViewDataBinding.memberList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mViewDataBinding.memberList.addItemDecoration(new SpacesItemDecoration(8));
        mAdapter = new MemberListAdapter(this.getContext(), mList);
        mAdapter.setClickListener(new RecyclerListClickListener() {
            @Override
            public void onClick(int position) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", mList.get(position).getProjectId());
                mIntent.putExtra("userId", mList.get(position).getUserId());
                mIntent.putExtra("weeks", TimeUtil.getInstance().getDayofWeek());
                mIntent.putExtra("canAudit", false);
                mIntent.setClass(getContext(), AuditWeeklyReportActivity.class);
                startActivity(mIntent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        mViewDataBinding.memberList.setAdapter(mAdapter);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initMemberList();
    }

    /**
     * 初始化项目成员列表
     */
    private void initMemberList() {
        LogT.d("成员列表 projectid" + projectId + " week " + week);
        ApiService.Utils.getInstance(getContext()).getMemberList(currenIndex, pageNumber, projectId, week)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<MemberListBean>>(getActivity()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mViewDataBinding.memberPlt.finishRefresh();
                        mViewDataBinding.memberPlt.finishLoadMore();
                    }

                    @Override
                    public void onNext(Result<MemberListBean> data) {
                        super.onNext(data);
                        LogT.d("result is " + data.toString());
                        if (data.isRet()) {
                            mList.clear();
                            mList.addAll(data.getData().getList());
                            totalPage = data.getData().getTotalPage();
                            mAdapter.refreshData(data.getData().getList());
                            mViewDataBinding.memberPlt.finishRefresh();
                            mViewDataBinding.memberPlt.finishLoadMore();
                        }
                    }
                });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void refresh() {
        currenIndex = 1;
        initMemberList();
    }

    /**
     * 加载更多
     */
    @Override
    public void loadMore() {
        currenIndex++;
        if (currenIndex <= totalPage) {
            initMemberList();
        } else {
            ToastUtils.show("无更多成员信息");
            mViewDataBinding.memberPlt.finishLoadMore();
        }
    }

    /**
     * 更新周数对应的weeks
     * 当前已经废弃
     * @param weeks
     */
    @Deprecated
    public void updateWeeks(int weeks) {
        this.week = weeks;
        initMemberList();
    }
}
