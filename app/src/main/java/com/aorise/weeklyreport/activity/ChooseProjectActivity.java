package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.adapter.MemberListAdapter;
import com.aorise.weeklyreport.adapter.ProjectListAdapter;
import com.aorise.weeklyreport.adapter.RecyclerListClickListener;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.ActivityChooseProjectBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ChooseProjectActivity extends AppCompatActivity implements RecyclerListClickListener {
    private ArrayList<ProjectList> mProjectList = new ArrayList<>();
    private List<MemberListBean> mMemberList = new ArrayList<>();
    private ActivityChooseProjectBinding mViewDataBinding;
    private ProjectListAdapter mAdapter;
    private MemberListAdapter mMemberAdatper;
    private boolean isProjectList = true;

    private int projectId = -1;
    private boolean isReview = false;//是否为审核周报  true为审核周报界面    false则为项目概况界面
    private boolean isHeaderReport = false;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_project);
        mProjectList = (ArrayList<ProjectList>) getIntent().getBundleExtra("projectList").getSerializable("_projectList");
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId",-1);
        isReview = getIntent().getBooleanExtra("isReview", false);
        isHeaderReport = getIntent().getBooleanExtra("isHeaderReport", false);
        isProjectList = mProjectList.size() != 1;
        LogT.d(" show projectList size is " + mProjectList.size());
        mAdapter = new ProjectListAdapter(this, mProjectList);
        mAdapter.setClickListener(this);


        mMemberAdatper = new MemberListAdapter(this, mMemberList);
        mMemberAdatper.setClickListener(new RecyclerListClickListener() {
            @Override
            public void onClick(int position) {
                LogT.d("点击查看此人的项目详情咯");
                Intent mIntent = new Intent();
                mIntent.setClass(ChooseProjectActivity.this, AuditWeeklyReportActivity.class);
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

        if (isProjectList) { //如果当前是审核周报选择，且该用户的项目列表不为1 则显示项目列表，隐藏项目成员列表
            mViewDataBinding.projectList.setVisibility(View.VISIBLE);
            mViewDataBinding.memberList.setVisibility(View.GONE);
        } else { //如果当前是审核周报选择，且该用户的项目列表不为1 则，隐藏项目列表 显示项目成员列表
            mViewDataBinding.memberList.setVisibility(View.VISIBLE);
            mViewDataBinding.projectList.setVisibility(View.GONE);
        }

        mViewDataBinding.chooseProjectActionbar.actionbarBack.setVisibility(View.VISIBLE);
        mViewDataBinding.chooseProjectActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProjectList) {
                    ChooseProjectActivity.this.finish();
                } else {
                    mViewDataBinding.projectList.setVisibility(View.VISIBLE);
                    mViewDataBinding.memberList.setVisibility(View.GONE);
                }
            }
        });

        mViewDataBinding.chooseProjectActionbar.actionMenu.setVisibility(View.GONE);
        mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目管理");
        mViewDataBinding.projectList.setLayoutManager(new LinearLayoutManager(this));
        mViewDataBinding.projectList.addItemDecoration(new SpacesItemDecoration(8));

        mViewDataBinding.memberList.setLayoutManager(new LinearLayoutManager(this));
        mViewDataBinding.memberList.addItemDecoration(new SpacesItemDecoration(8));

        mViewDataBinding.projectList.setAdapter(mAdapter);
        mViewDataBinding.memberList.setAdapter(mMemberAdatper);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        LogT.d("");
        if(!isProjectList){
            mViewDataBinding.projectList.setVisibility(View.VISIBLE);
            mViewDataBinding.memberList.setVisibility(View.GONE);
            isProjectList = true;
        }else{
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReview && mProjectList.size() == 1) {
            projectId = mProjectList.get(0).getId();
            getMemberList(mProjectList.get(0).getId());
        }
    }

    private void getMemberList(int projectId) {
        LogT.d("project id is " + projectId);
        ApiService.Utils.getInstance(this).getMemberList(1, 100, projectId, TimeUtil.getInstance().getDayofWeek())
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<MemberListBean>>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("error " + e.toString());
                        ToastUtils.show("当前项目组下无项目成员");
                    }

                    @Override
                    public void onNext(Result<List<MemberListBean>> o) {
                        super.onNext(o);
                        LogT.d("o is " + o.toString());
                        if (o.isRet()) {
                            mMemberList.clear();
                            mMemberList.addAll(o.getData());
                            mViewDataBinding.memberList.setVisibility(View.VISIBLE);
                            mViewDataBinding.projectList.setVisibility(View.GONE);

                            mMemberAdatper.refreshData(o.getData());
                            isProjectList = false;
                        }
                    }
                });
    }

    @Override
    public void onClick(int position) {
        LogT.d("当前选择的projectInfo为" + mProjectList.get(position));
        projectId = mProjectList.get(position).getId();
        if (!isReview) {//非周报审核
            if(!isHeaderReport) {//非项目周报
                Intent mIntent = new Intent();
                mIntent.putExtra("project_info", mProjectList.get(position));
                mIntent.setClass(this, ProjectInfoActivity.class);
                startActivity(mIntent);
            }else{//项目周报
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("userId", userId);
                mIntent.setClass(this, MemberManagerActivity.class);
                startActivity(mIntent);
            }
        } else {
//            mViewDataBinding.projectList.swapAdapter(mMemberAdatper, true);
            mViewDataBinding.projectList.setVisibility(View.GONE);
            getMemberList(projectId);
        }

    }

    @Override
    public void onLongClick(int position) {

    }
}
