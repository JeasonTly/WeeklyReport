package com.aorise.weeklyreport.activity.projectweekly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.ChooseProjectActivity;
import com.aorise.weeklyreport.activity.ProjectInfoActivity;
import com.aorise.weeklyreport.activity.ProjectReportManagerActivity;
import com.aorise.weeklyreport.adapter.MemberListAdapter;
import com.aorise.weeklyreport.adapter.ProjectListAdapter;
import com.aorise.weeklyreport.adapter.RecyclerListClickListener;
import com.aorise.weeklyreport.adapter.SpacesItemDecoration;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.baseadapter.CommonAdapter;
import com.aorise.weeklyreport.baseadapter.ViewHolder;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectListBean;
import com.aorise.weeklyreport.databinding.ActivityProjectweeklyCheckBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.aorise.weeklyreport.view.MenuPopup;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectweeklyCheckActivity extends AppCompatActivity implements RecyclerListClickListener, MenuPopup.MenuPopupSelectedListener {
    private ActivityProjectweeklyCheckBinding mViewDataBinding;
    /**
     * 项目列表
     */
    private List<ProjectInfo.DataBean.ListBean> mProjectList = new ArrayList<>();
    private List<ProjectInfo.DataBean.ListBean> mProjectList01;
    private CommonAdapter<ProjectInfo.DataBean.ListBean> adapter;
    private SharedPreferences sp;
    private int userId;
    /**
     * 成员内容@{
     */
    private List<MemberListBean.ListBean> mMemberList = new ArrayList<>();

    /**
     * 筛选部门工作项目 和 项目工作项目
     */
    private List<String> filterList = new ArrayList<>();
    private MenuPopup menuPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_projectweekly_check);
        adapter = new CommonAdapter<ProjectInfo.DataBean.ListBean>(this, R.layout.item_list_project_01, mProjectList) {
            @Override
            protected void convert(ViewHolder viewHolder, ProjectInfo.DataBean.ListBean item, int position) {
                viewHolder.setText(R.id.txt_title, item.getName());
                if (item.getWeeklyState() == 1) {

                } else if (item.getWeeklyState() == 1) {

                } else if (item.getWeeklyState() == 1) {

                } else if (item.getWeeklyState() == 1) {

                }

            }

        };

        mViewDataBinding.chooseProjectActionbar.actionMenu.setVisibility(View.GONE);
        sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sp.getInt("userId", 0);
        /**
         * 初始化MenuPopu选择分类
         */
        filterList.add("全部工作");
        filterList.add("项目工作");
        filterList.add("部门工作");
        menuPopup = new MenuPopup(this, 0, this, filterList);

        menuPopup.setPopupGravity(Gravity.BOTTOM);


        mViewDataBinding.chooseSearch.fpClidQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //m.getFilter().filter(sequence.toString());
                mProjectList.clear();
                for (int i = 0; i < mProjectList01.size(); i++) {
                    if (mProjectList01.get(i).getName().contains(s.toString().trim())){
                        mProjectList.add(mProjectList01.get(i));
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目选择 - " + filterList.get(0));
        mViewDataBinding.chooseProjectActionbar.actionBarDropdown.setVisibility(View.VISIBLE);


        mViewDataBinding.chooseProjectActionbar.actionBarTitleArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPopup.showPopupWindow(mViewDataBinding.chooseProjectActionbar.actionBarTitleArea);
            }
        });
        mViewDataBinding.chooseProjectActionbar.actionbarBack.setVisibility(View.VISIBLE);
        mViewDataBinding.chooseProjectActionbar.actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectweeklyCheckActivity.this.finish();

            }
        });

        mViewDataBinding.projectList.setAdapter(adapter);
        mViewDataBinding.projectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent mIntent = new Intent();
                mIntent.putExtra("projectId", mProjectList.get(i).getId());
                mIntent.putExtra("projectName", mProjectList.get(i).getName());
                mIntent.putExtra("userId", userId);
                mIntent.putExtra("where", 1);
                mIntent.setClass(ProjectweeklyCheckActivity.this, ProjectReportManagerActivity.class);
                startActivity(mIntent);
            }
        });
        getProjectList();

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 项目列表项点击事件
     *
     * @param position
     */
    @Override
    public void onClick(int position) {

    }

    @Override
    public void onLongClick(int position) {

    }


    @Override
    public void selectPosistion(int position) {

        mViewDataBinding.chooseProjectActionbar.actionBarTitle.setText("项目选择 - " + filterList.get(filterList.size() - 1 - position));
        if (filterList.get(position).equals("项目工作")) {
            mProjectList.clear();
            for (int i = 0; i < mProjectList01.size(); i++) {
                if (mProjectList01.get(i).getProperty() == 1) {
                    mProjectList.add(mProjectList01.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        } else if (filterList.get(position).equals("部门工作")) {

            mProjectList.clear();
            for (int i = 0; i < mProjectList01.size(); i++) {
                if (mProjectList01.get(i).getProperty() == 2) {
                    mProjectList.add(mProjectList01.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }else if (filterList.get(position).equals("全部工作")) {
            mProjectList.clear();
            for (int i = 0; i < mProjectList01.size(); i++) {
                    mProjectList.add(mProjectList01.get(i));
            }
            adapter.notifyDataSetChanged();
        }
        mViewDataBinding.chooseSearch.fpClidQuery.setText("");

    }

    private void getProjectList() {
        ApiService.Utils.getInstance(this).getProjectList01("1", "999")
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<ProjectInfo>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(ProjectInfo listResult) {
                        super.onNext(listResult);
                        System.out.println(listResult.toString());
                        if (listResult.isRet()) {

                            mProjectList.clear();
                            for (int i = 0; i < listResult.getData().getList().size(); i++) {
                                mProjectList.add(listResult.getData().getList().get(i));
                                mProjectList01.add(listResult.getData().getList().get(i));
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}