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
import com.aorise.weeklyreport.activity.AuditWeeklyReportActivity;
import com.aorise.weeklyreport.activity.MemberManagerActivity;
import com.aorise.weeklyreport.adapter.MemberListAdapter;
import com.aorise.weeklyreport.adapter.ProjectListAdapter;
import com.aorise.weeklyreport.adapter.RecyclerListClickListener;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.FragmentMemberBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.CustomSubscriberNoDialog;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MemberFragment} factory method to
 * create an instance of this fragment.
 */
public class MemberFragment extends Fragment implements RecyclerListClickListener {


    private FragmentMemberBinding mViewDataBinding;
    private List<ProjectList> mProjectList = new ArrayList<>();
    private List<MemberListBean> mMemberList = new ArrayList<>();
    private ProjectListAdapter mAdapter;
    private MemberListAdapter mMemberAdapter;

    private int userId = 2;
    private int projectId = -1;
    private boolean isProjectList = true;

    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isProjectList = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_member, container, false);

        mAdapter = new ProjectListAdapter(getContext(), mProjectList);
        mAdapter.setClickListener(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",-1);

        mViewDataBinding.fragmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewDataBinding.fragmentRecycler.setAdapter(mAdapter);
        mViewDataBinding.fmActionbar.actionbarBack.setVisibility(View.GONE);
        mViewDataBinding.fmActionbar.actionMenu.setVisibility(View.GONE);
        mViewDataBinding.fmActionbar.actionMenu.setImageResource(R.drawable.bianji);
        mMemberAdapter = new MemberListAdapter(getContext(), mMemberList);
        mMemberAdapter.setClickListener(new RecyclerListClickListener() {
            @Override
            public void onClick(int position) {
                LogT.d("点击查看此人的项目详情咯");
                Intent mIntent = new Intent();
                mIntent.setClass(getContext(), AuditWeeklyReportActivity.class);
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("userId", mMemberList.get(position).getUserId());
                mIntent.putExtra("userName", mMemberList.get(position).getUserName());
                mIntent.putExtra("weeks", TimeUtil.getInstance().getDayofWeek());
                startActivity(mIntent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        //mViewDataBinding.fmActionbar.actionBarTitle.setText("请选择");
        mViewDataBinding.fmActionbar.actionBarTitle.setText("项目管理");
        mViewDataBinding.fragmentMemberRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewDataBinding.fragmentMemberRecycler.setAdapter(mMemberAdapter);

        mViewDataBinding.fmActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isProjectList) {
                    mViewDataBinding.fragmentRecycler.setVisibility(View.VISIBLE);
                    mViewDataBinding.fragmentMemberRecycler.setVisibility(View.GONE);
                    mViewDataBinding.fmActionbar.actionbarBack.setVisibility(View.GONE);
                    mViewDataBinding.fmActionbar.actionMenu.setVisibility(View.GONE);
                    isProjectList = true;
                }

            }
        });

        mViewDataBinding.fmActionbar.actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("userId", userId);
                mIntent.setClass(getActivity(), MemberManagerActivity.class);
                startActivity(mIntent);
            }
        });
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initProjectList();
    }

    private void initProjectList() {
        ApiService.Utils.getInstance(getContext()).getProjectList(-1, userId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<ProjectList>>>(this.getContext()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("error msg" + e.toString());
                    }

                    @Override
                    public void onNext(Result<List<ProjectList>> o) {
                        super.onNext(o);
                        if (o.isRet()) {
                            mProjectList = o.getData();
                            LogT.d("mProjectList is " + mProjectList.toString());
                            if(mProjectList !=null && mProjectList.size()==1){
                               // getMemberList(mProjectList.get(0).getId());
                            }else {
                                mAdapter.refreshData(mProjectList);
                            }
                        }
                    }
                });
    }


    /**
     * 列表点击事件
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        if (isProjectList) {
           // getMemberList(mProjectList.get(position).getId());
        }
    }

    /**
     * 列表长按 事件
     *
     * @param position
     */
    @Override
    public void onLongClick(int position) {

    }

//    private void getMemberList(int projectId) {
//        this.projectId = projectId;
//        LogT.d("project id is " + projectId);
//        ApiService.Utils.getInstance(getContext()).getMemberList(projectId)
//                .compose(ApiService.Utils.schedulersTransformer())
//                .subscribe(new CustomSubscriber<Result<List<MemberListBean>>>(getContext()) {
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        LogT.d("error " + e.toString());
//                        ToastUtils.show("当前项目组下无项目成员");
//                    }
//
//                    @Override
//                    public void onNext(Result<List<MemberListBean>> o) {
//                        super.onNext(o);
//                        if (o.isRet()) {
//
//                            mMemberList = o.getData();
//                            LogT.d("mMemberList list " + mMemberList);
//                            isProjectList = false;
//                            mViewDataBinding.fragmentMemberRecycler.setVisibility(View.VISIBLE);
//                            mViewDataBinding.fragmentRecycler.setVisibility(View.GONE);
//                            mViewDataBinding.fmActionbar.actionbarBack.setVisibility(View.VISIBLE);
//                            mViewDataBinding.fmActionbar.actionMenu.setVisibility(View.VISIBLE);
//
//                            mMemberAdapter.refreshData(mMemberList);
//
//
//                        }
//                    }
//                });
//    }
}
