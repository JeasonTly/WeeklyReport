package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.aorise.weeklyreport.view.MenuPopup;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目选择和成员选择界面
 * 可以拆分为项目选择 和 成员选择
 * 尚可优化项目选择时 也进行分页
 */
public class ChooseProjectActivity extends AppCompatActivity implements RecyclerListClickListener, BaseRefreshListener, MenuPopup.MenuPopupSelectedListener {

    private ActivityChooseProjectBinding mViewDataBinding;

    /**
     * 项目列表
     */
    private ArrayList<ProjectList> mProjectList = new ArrayList<>();
    private ProjectListAdapter mAdapter;
    /**
     * 成员内容@{
     */
    private List<MemberListBean.ListBean> mMemberList = new ArrayList<>();
    /**
     * 当前页
     */
    private int currentIndex = 1;
    /**
     * 一页多少数据
     */
    private int everPage = 10;
    /**
     * 总页数
     */
    private int totalPage = 1;
    private MemberListAdapter mMemberAdatper;
    /**
     * 成员列表@}
     */

    /**
     * 当前列表是否为项目列表
     */
    private boolean isProjectList = true;
    /**
     * 项目ID
     */
    private int projectId = -1;
    /**
     * 项目名称
     */
    private String projectName = "";
    /**
     * 是否为审核周报  true为审核周报界面    false则为项目概况界面
     */
    private boolean isReview = false;
    /**
     * 是否为审核项目周报  true为审核项目周报界面
     */
    private boolean isAuditProjectReport = false;
    /**
     * 是则为选择项目对应的项目周报 ，否则为负责人选择项目对应的成员周报
     */
    private boolean isHeaderReport = false;
    /**
     * 用户ID
     */
    private int userId = -1;

    /**
     * 筛选部门工作项目 和 项目工作项目
     */
    private List<String> filterList = new ArrayList<>();
    private MenuPopup menuPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_project);
        mProjectList = (ArrayList<ProjectList>) getIntent().getBundleExtra("projectList").getSerializable("_projectList");
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        isReview = getIntent().getBooleanExtra("isReview", false);
        isAuditProjectReport = getIntent().getBooleanExtra("isProjectReportAudit", false);
        isHeaderReport = getIntent().getBooleanExtra("isHeaderReport", false);
        isProjectList = mProjectList.size() != 1;
        mViewDataBinding.chooseProjectActionbar.actionMenu.setVisibility(View.GONE);

        LogT.d(" show projectList size is " + mProjectList.size());
        /**
         * 初始化MenuPopu选择分类
         */
        filterList.add("全部工作");
        filterList.add("项目工作");
        filterList.add("部门工作");
        menuPopup = new MenuPopup(this, 0, this, filterList);

        menuPopup.setPopupGravity(Gravity.BOTTOM);

        mAdapter = new ProjectListAdapter(this, mProjectList);
        mAdapter.setClickListener(this);
        mViewDataBinding.chooseSearch.fpClidQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //m.getFilter().filter(sequence.toString());
                if (isProjectList) {
                    mAdapter.getFilter().filter(s.toString());
                } else {
                    mMemberAdatper.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mMemberAdatper = new MemberListAdapter(this, mMemberList);
        mMemberAdatper.setClickListener(new RecyclerListClickListener() {
            @Override
            public void onClick(int position) {
                LogT.d("点击查看此人的项目详情咯 " + mMemberAdatper.getmFilterList().get(position).getUserName());
                if (isReview) {
                    Intent mIntent = new Intent();
                    mIntent.setClass(ChooseProjectActivity.this, AuditWeeklyReportActivity.class);
                    mIntent.putExtra("projectId", projectId);
                    mIntent.putExtra("userId", mMemberAdatper.getmFilterList().get(position).getUserId());
                    mIntent.putExtra("userName", mMemberAdatper.getmFilterList().get(position).getUserName());
                    mIntent.putExtra("weeks", TimeUtil.getInstance().getDayofWeek());
                    startActivity(mIntent);
                }
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        if (isProjectList) { //如果当前是审核周报选择，且该用户的项目列表不为1 则显示项目列表，隐藏项目成员列表
            mViewDataBinding.projectList.setVisibility(View.VISIBLE);
            mViewDataBinding.memberPltChoose.setVisibility(View.GONE);
            mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目选择 - " + filterList.get(0));
            mViewDataBinding.chooseProjectActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
        } else { //如果当前是审核周报选择，且该用户的项目列表不为1 则，隐藏项目列表 显示项目成员列表
            mViewDataBinding.memberPltChoose.setVisibility(View.VISIBLE);
            mViewDataBinding.projectList.setVisibility(View.GONE);
            mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("成员选择");
            mViewDataBinding.chooseProjectActionbar.actionBarDropdown.setVisibility(View.GONE);
        }

        mViewDataBinding.chooseProjectActionbar.actionBarTitleArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isProjectList) {
                    return;
                }
                menuPopup.showPopupWindow(mViewDataBinding.chooseProjectActionbar.actionBarTitleArea);
            }
        });
        mViewDataBinding.chooseProjectActionbar.actionbarBack.setVisibility(View.VISIBLE);
        mViewDataBinding.chooseProjectActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isProjectList) {
                    ChooseProjectActivity.this.finish();
                } else {
                    mViewDataBinding.projectList.setVisibility(View.VISIBLE);
                    mViewDataBinding.memberPltChoose.setVisibility(View.GONE);
                    mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目选择");
                    mViewDataBinding.chooseProjectActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
                    isProjectList = true;
                }
            }
        });


        mViewDataBinding.projectList.setLayoutManager(new LinearLayoutManager(this));
        mViewDataBinding.projectList.addItemDecoration(new SpacesItemDecoration(8));

        mViewDataBinding.memberList.setLayoutManager(new LinearLayoutManager(this));
        mViewDataBinding.memberList.addItemDecoration(new SpacesItemDecoration(8));

        mViewDataBinding.memberPltChoose.setRefreshListener(this);
        mViewDataBinding.projectList.setAdapter(mAdapter);
        mViewDataBinding.memberList.setAdapter(mMemberAdatper);

    }

    @Override
    public void onBackPressed() {

        LogT.d(" 项目和成员选择界面 onBackPressed ");
        if (mProjectList.size() == 1 && mMemberList.size() > 1) {
            // super.onBackPressed();
            this.finish();
        }
        if (!isProjectList) {
            mViewDataBinding.projectList.setVisibility(View.VISIBLE);
            mViewDataBinding.memberPltChoose.setVisibility(View.GONE);
            mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目选择");
            mViewDataBinding.chooseProjectActionbar.actionBarDropdown.setVisibility(View.VISIBLE);
            isProjectList = true;
        } else {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReview && mProjectList.size() == 1) {
            projectId = mProjectList.get(0).getId();
            projectName = mProjectList.get(0).getName();
            getMemberList(mProjectList.get(0).getId());
        }
    }

    /**
     * 根据项目ID获取成员列表，分页
     *
     * @param projectId
     */
    private void getMemberList(int projectId) {
        LogT.d("project id is " + projectId);
        ApiService.Utils.getInstance(this).getMemberList(currentIndex, everPage, projectId, TimeUtil.getInstance().getDayofWeek())
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<MemberListBean>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogT.d("error " + e.toString());
                        ToastUtils.show("当前项目组下无项目成员");
                        mViewDataBinding.memberPltChoose.finishLoadMore();
                    }

                    @Override
                    public void onNext(Result<MemberListBean> o) {
                        super.onNext(o);
                        LogT.d("o is " + o.toString());
                        if (o.isRet()) {
                            mMemberList.clear();
                            mMemberList.addAll(o.getData().getList());
                            mViewDataBinding.memberPltChoose.setVisibility(View.VISIBLE);
                            mViewDataBinding.projectList.setVisibility(View.GONE);
                            mMemberAdatper.refreshData(o.getData().getList());
                            totalPage = o.getData().getTotalPage();
                            mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("成员选择");
                            mViewDataBinding.chooseProjectActionbar.actionBarDropdown.setVisibility(View.GONE);
                            mViewDataBinding.memberPltChoose.finishLoadMore();
                            isProjectList = false;
                        }
                    }
                });
    }

    /**
     * 项目列表项点击事件
     *
     * @param position
     */
    @Override
    public void onClick(int position) {
        LogT.d("当前选择的projectInfo为" + mAdapter.getmFilterList().get(position));
        projectId = mAdapter.getmFilterList().get(position).getId();
        projectName = mAdapter.getmFilterList().get(position).getName();
        if (!isReview) {//非成员周报审核
            if (isAuditProjectReport) {//项目周报审批
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", mAdapter.getmFilterList().get(position).getId());
                mIntent.putExtra("projectName", mAdapter.getmFilterList().get(position).getName());
                mIntent.putExtra("userId", userId);
                mIntent.putExtra("isAudit", true);
                mIntent.setClass(this, ProjectReportManagerActivity.class);
                startActivity(mIntent);
                return;
            }
            if (!isHeaderReport) {//非项目周报,为项目概况
                Intent mIntent = new Intent();
                mIntent.putExtra("project_info", mAdapter.getmFilterList().get(position));
                mIntent.putExtra("isReview", isReview);
                mIntent.setClass(this, ProjectInfoActivity.class);
                startActivity(mIntent);

            } else {//项目周报
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", projectId);
                mIntent.putExtra("projectName", projectName);
                mIntent.putExtra("userId", userId);
                mIntent.setClass(this, ProjectReportManagerActivity.class);
                startActivity(mIntent);
            }
        } else {
            mViewDataBinding.projectList.setVisibility(View.GONE);
            getMemberList(projectId);
        }

    }

    @Override
    public void onLongClick(int position) {

    }

    @Override
    public void refresh() {
        currentIndex = 1;
        if (!isProjectList) {
            getMemberList(projectId);
        }
        mViewDataBinding.memberPltChoose.finishRefresh();
    }

    @Override
    public void loadMore() {
        currentIndex++;
        if (currentIndex <= totalPage) {
            if (!isProjectList) {
                getMemberList(projectId);
            }
        } else {
            ToastUtils.show("已加载全部成员列表!");
            mViewDataBinding.memberPltChoose.finishLoadMore();
        }

    }

    @Override
    public void selectPosistion(int position) {
        mAdapter.getFilter().filter(filterList.get(filterList.size() - 1 - position));
        // mViewDataBinding.chooseSearch.fpClidQuery.setText("");

        mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目选择 - " + filterList.get(filterList.size() - 1 - position));
    }
}
