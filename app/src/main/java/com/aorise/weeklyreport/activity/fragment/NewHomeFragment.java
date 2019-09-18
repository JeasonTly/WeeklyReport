package com.aorise.weeklyreport.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.ChooseProjectActivity;
import com.aorise.weeklyreport.activity.ProjectInfoActivity;
import com.aorise.weeklyreport.activity.ProjectReportManagerActivity;
import com.aorise.weeklyreport.activity.ReviewAndToFillReportActivity;
import com.aorise.weeklyreport.activity.WorkTimeYearStatisticsActivity;
import com.aorise.weeklyreport.activity.projectweekly.ProjectweeklyCheckActivity;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectListBean;
import com.aorise.weeklyreport.databinding.FragmentNewHomeBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页fragment 包括
 *  广告推送
 *  项目概况
 *  周报填写
 *  周报审批
 *  项目周报
 *  绩效考核 (目前为工时统计)
 */
public class NewHomeFragment extends Fragment implements OnBannerListener {
    private FragmentNewHomeBinding mViewDataBinding;
    /**
     * SharedPreference 获取基本登录信息
     */
    private SharedPreferences sharedPreferences;
    /**
     * 该projectlist 判断长度 mProjectListSize 为1, 则直接跳转到周报概况界面ProjectInfoActivity
     * mProjectList.size() > 1，则进入列表选择界面ChooseProjectActivity;
     */
    private ArrayList<ProjectList> mProjectList = new ArrayList<>();
    /**
     *  超级管理员获取的项目列表信息，
     *  将会重构为ProjectList并传递给选择项目界面或者对应的界面
     */
    private ArrayList<ProjectListBean.ListBean> mManagerProjectList = new ArrayList<>();
    /**
     * 用户或者项目负责人ID
     */
    private int userId;
    /**
     * 是否为项目负责人
     */
    private boolean isHeader = false;
    /**
     * 是否为超级管理员
     */
    private boolean isSuperManager = false;
    private int userType;//用户身份

    public NewHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        isHeader = sharedPreferences.getInt("userRole", -1) != 0; // 1为项目负责人，0为项目成员 ,2为超级管理员
        isSuperManager = sharedPreferences.getInt("userRole", -1) == 2;
        userType = sharedPreferences.getInt("userRole",-1) ;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_home, container, false);
        initSpan();
        mViewDataBinding.newHomeActionbar.actionBarTitle.setText("首页");
        mViewDataBinding.newHomeActionbar.actionbarBack.setVisibility(View.GONE);
        //项目概况Intent跳转
        mViewDataBinding.projectInfoArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogT.d("您点击了项目概况!!!");
                if (!isHeader) {
                    queryProjectInfoList();
                } else {
                    queryProjectInfoAsHeaderList(false);
                }


            }
        });

        //周报填写Intent跳转
        mViewDataBinding.reportFillArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.setClass(getActivity(), ReviewAndToFillReportActivity.class);
                startActivity(mIntent);
            }
        });
        //周报审核跳转
        mViewDataBinding.reportReviewArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // queryProjectList(true);
                queryProjectInfoAsHeaderList(true);
            }
        });
        //项目周报
        mViewDataBinding.projectReportArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseProject();
            }
        });
        //工时统计
        mViewDataBinding.jixiaoArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent mIntent = new Intent();
               mIntent.setClass(getActivity(), WorkTimeYearStatisticsActivity.class);
               startActivity(mIntent);
            }
        });
        mViewDataBinding.llProjectWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userType == 3||userType ==2) {
                    Intent mIntent = new Intent();
                    mIntent.setClass(getActivity(), ProjectweeklyCheckActivity.class);
                    startActivity(mIntent);
                }
            }
        });
        if (!isHeader) {
            mViewDataBinding.projectReportArea.setVisibility(View.GONE);
            mViewDataBinding.reportReviewArea.setVisibility(View.GONE);
            mViewDataBinding.jixiaoArea.setVisibility(View.GONE);
            mViewDataBinding.llProjectWeekly.setVisibility(View.GONE);
        }

        initBanner();
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 根据普通用户的用户ID查询 项目概况
     */
    private void queryProjectInfoList() {
        ApiService.Utils.getInstance(getActivity()).getProjectList(userId, -1)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<List<ProjectList>>>(getActivity()) {
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
                            mProjectList.clear();
                            mProjectList.addAll(o.getData());
                            if (mProjectList != null && mProjectList.size() != 0) {
                                if (mProjectList.size() == 1) {
                                    LogT.d("普通成员 mProjectList size 为1");
                                    Intent mIntent = new Intent();
                                    mIntent.putExtra("project_info", mProjectList.get(0));
                                    mIntent.setClass(getActivity(), ProjectInfoActivity.class);
                                    startActivity(mIntent);
                                } else {
                                    LogT.d("mProjectList size 大于1");
                                    Intent mIntent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                    mIntent.putExtra("projectList", bundle);
                                    mIntent.putExtra("isReview", false);
                                    mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                    startActivity(mIntent);
                                }
                            } else {
                                ToastUtils.show("当前用户角色下无项目!");
                            }
                        }
                    }
                });
    }

    /**
     * 根据负责人用户的用户ID查询 项目概况 和进行周报审核
     */
    private void queryProjectInfoAsHeaderList(final boolean isReview) {
        LogT.d(" 是否为审核项目" +isReview);
        if (isSuperManager) {
            ApiService.Utils.getInstance(getActivity()).getProjectList("0", "0")
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<ProjectListBean>>(getActivity()) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(Result<ProjectListBean> listResult) {
                            super.onNext(listResult);

                            if (listResult.isRet()) {
                                mManagerProjectList.clear();
                                mProjectList.clear();
                                mManagerProjectList.addAll(listResult.getData().getList());
                                LogT.d("超级管理员 mProjectList is " + mProjectList.toString());
                                for (int i = 0; i < mManagerProjectList.size(); i++) {
                                    ProjectList projectList = new ProjectList();
                                    projectList.setId(mManagerProjectList.get(i).getId());
                                    projectList.setName(mManagerProjectList.get(i).getName());
                                    LogT.d("周报审核 原始类型 ProjectListBean "+ mManagerProjectList.get(i).toString());
                                    LogT.d("周报审核 类型转换为projectList "+projectList.toString());
                                    mProjectList.add(projectList);
                                }

                                if (mProjectList != null && mProjectList.size() != 0) {
                                    if (mProjectList.size() == 1) {
                                        if (!isReview) {//项目概况
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("isReview", isReview);
                                            mIntent.putExtra("project_info", mProjectList.get(0));
                                            mIntent.setClass(getActivity(), ProjectInfoActivity.class);
                                            startActivity(mIntent);
                                        } else {//项目组周报审核
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("isReview", isReview);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                            mIntent.putExtra("projectList", bundle);
                                            mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                            startActivity(mIntent);
                                        }
                                    } else {
                                        LogT.d("mProjectList size 大于1");
                                        Intent mIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                        mIntent.putExtra("projectList", bundle);
                                        mIntent.putExtra("isReview", isReview);
                                        mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                        startActivity(mIntent);
                                    }
                                } else {
                                    ToastUtils.show("当前用户角色下无项目!");
                                }
                            }
                        }
                    });
        }else if(isHeader) {
            ApiService.Utils.getInstance(getActivity()).getProjectList(isReview ? -1 : userId, userId)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<List<ProjectList>>>(getActivity()) {
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
                                mProjectList.clear();
                                mProjectList.addAll(o.getData());
                                LogT.d("项目组负责人 mProjectList is " + mProjectList.toString());
                                if (mProjectList != null && mProjectList.size() != 0) {
                                    if (mProjectList.size() == 1) {
                                        if (!isReview) {//项目概况
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("isReview", isReview);
                                            mIntent.putExtra("project_info", mProjectList.get(0));
                                            mIntent.setClass(getActivity(), ProjectInfoActivity.class);
                                            startActivity(mIntent);
                                        } else {//项目组周报审核
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("isReview", isReview);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                            mIntent.putExtra("projectList", bundle);
                                            mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                            startActivity(mIntent);
                                        }
                                    } else {
                                        LogT.d("mProjectList size 大于1");
                                        Intent mIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                        mIntent.putExtra("projectList", bundle);
                                        mIntent.putExtra("isReview", isReview);
                                        mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                        startActivity(mIntent);
                                    }
                                } else {
                                    ToastUtils.show("当前用户角色下无项目!");
                                }
                            }
                        }
                    });
        }else{
            ApiService.Utils.getInstance(getActivity()).getProjectList(isReview ? -1 : userId, userId)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<List<ProjectList>>>(getActivity()) {
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
                                mProjectList.clear();
                                mProjectList.addAll(o.getData());
                                LogT.d("普通成员 mProjectList is " + mProjectList.toString());
                                if (mProjectList != null && mProjectList.size() != 0) {
                                    if (mProjectList.size() == 1) {
                                        if (!isReview) {//项目概况
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("isReview", isReview);
                                            mIntent.putExtra("project_info", mProjectList.get(0));
                                            mIntent.setClass(getActivity(), ProjectInfoActivity.class);
                                            startActivity(mIntent);
                                        } else {//项目组周报审核
                                            Intent mIntent = new Intent();
                                            mIntent.putExtra("isReview", isReview);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                            mIntent.putExtra("projectList", bundle);
                                            mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                            startActivity(mIntent);
                                        }
                                    } else {
                                        LogT.d("mProjectList size 大于1");
                                        Intent mIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                        mIntent.putExtra("projectList", bundle);
                                        mIntent.putExtra("isReview", isReview);
                                        mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                        startActivity(mIntent);
                                    }
                                } else {
                                    ToastUtils.show("当前用户角色下无项目!");
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 初始化阵列字体
     */
    private void initSpan() {
        SpannableString projectText = new SpannableString("项目概况 Project");
        RelativeSizeSpan largeSpan = new RelativeSizeSpan(1.8f);
        RelativeSizeSpan smallSpan = new RelativeSizeSpan(0.4f);
        projectText.setSpan(largeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        projectText.setSpan(smallSpan, 0, projectText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mViewDataBinding.projectInfo.setText(projectText);
        SpannableString fillReportText = new SpannableString("周报填写 Fill");
        fillReportText.setSpan(largeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        fillReportText.setSpan(smallSpan, 0, fillReportText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mViewDataBinding.fillReport.setText(fillReportText);
        SpannableString reviewText = new SpannableString("周报审核 Review");
        reviewText.setSpan(largeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        reviewText.setSpan(smallSpan, 0, reviewText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mViewDataBinding.review.setText(reviewText);
        SpannableString weeklyReportText = new SpannableString("项目周报 Weekly");
        weeklyReportText.setSpan(largeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        weeklyReportText.setSpan(smallSpan, 0, weeklyReportText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mViewDataBinding.weeklyReport.setText(weeklyReportText);

        SpannableString jixiaoText = new SpannableString("工时统计 workTime");
        jixiaoText.setSpan(largeSpan, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        jixiaoText.setSpan(smallSpan, 0, jixiaoText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mViewDataBinding.jixiao.setText(jixiaoText);
    }


    /**
     * 跳转至项目周报或者项目选择界面
     * 因为该功能只对负责人可见，所以只传入了isHeaderReport = true
     */
    private void startChooseProject() {
        if (isSuperManager) {
            ApiService.Utils.getInstance(getActivity()).getProjectList("0", "0")
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<ProjectListBean>>(getActivity()) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(Result<ProjectListBean> listResult) {
                            super.onNext(listResult);

                            if (listResult.isRet()) {
                                mManagerProjectList.clear();
                                mProjectList.clear();
                                mManagerProjectList.addAll(listResult.getData().getList());
                                for (int i = 0; i < mManagerProjectList.size(); i++) {
                                    ProjectList projectList = new ProjectList();
                                    projectList.setId(mManagerProjectList.get(i).getId());
                                    projectList.setName(mManagerProjectList.get(i).getName());
                                    LogT.d("原始类型 ProjectListBean "+ mManagerProjectList.get(i).toString());
                                    LogT.d("类型转换为projectList "+projectList.toString());
                                    mProjectList.add(projectList);
                                }

                                LogT.d("超級管理員 mProjectList is " + mManagerProjectList.toString());
                                if (mProjectList != null && mProjectList.size() != 0) {
                                    if (mProjectList.size() == 1) {
                                        Intent mIntent = new Intent();

                                        mIntent.putExtra("projectId", mProjectList.get(0).getId());
                                        mIntent.putExtra("projectName", mManagerProjectList.get(0).getName());
                                        mIntent.putExtra("userId", userId);
                                        mIntent.setClass(getActivity(), ProjectReportManagerActivity.class);
                                        startActivity(mIntent);
                                    } else {
                                        LogT.d("mProjectList size 大于1");
                                        Intent mIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                        mIntent.putExtra("projectList", bundle);
                                        mIntent.putExtra("userId", userId);
                                        mIntent.putExtra("isHeaderReport", true);
                                        mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                        startActivity(mIntent);
                                    }
                                } else {
                                    ToastUtils.show("当前用户角色下无负责的项目!");
                                }
                            }
                        }
                    });
        } else {
            ApiService.Utils.getInstance(getActivity()).getProjectList(-1, userId)
                    .compose(ApiService.Utils.schedulersTransformer())
                    .subscribe(new CustomSubscriber<Result<List<ProjectList>>>(getActivity()) {
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
                                mProjectList.clear();
                                mProjectList.addAll(o.getData());
                                LogT.d("项目组负责人 mProjectList is " + mProjectList.toString());
                                if (mProjectList != null && mProjectList.size() != 0) {
                                    if (mProjectList.size() == 1) {
                                        Intent mIntent = new Intent();
                                        mIntent.putExtra("projectId", mProjectList.get(0).getId());
                                        mIntent.putExtra("projectName", mProjectList.get(0).getName());
                                        mIntent.putExtra("userId", userId);
                                        mIntent.setClass(getActivity(), ProjectReportManagerActivity.class);
                                        startActivity(mIntent);
                                    } else {
                                        LogT.d("mProjectList size 大于1");
                                        Intent mIntent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                        mIntent.putExtra("projectList", bundle);
                                        mIntent.putExtra("userId", userId);
                                        mIntent.putExtra("isHeaderReport", true);
                                        mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                        startActivity(mIntent);
                                    }
                                } else {
                                    ToastUtils.show("当前用户角色下无负责的项目!");
                                }
                            }
                        }
                    });
        }

    }

    /**
     * 初始化banner广告区域
     */
    private void initBanner() {
        //设置banner样式
        List<String> list = new ArrayList<>();
        list.add("http://ym-moblie.oss-cn-shenzhen.aliyuncs.com//oss/63aa83e49b3f45dfa1a72520b93bb603.jpg");
        list.add("http://ym-moblie.oss-cn-shenzhen.aliyuncs.com//oss/e1d86d1197214d60a2756f013a9b31b1.jpg");
        list.add("http://ym-moblie.oss-cn-shenzhen.aliyuncs.com//oss/71ceab6cf6254c859fa06f63175b2b8e.jpg");
        mViewDataBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        // mViewDataBinding.banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mViewDataBinding.banner.setImages(list);
        mViewDataBinding.banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                System.out.println((String) path);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(context).load((String) path).apply(new RequestOptions().error(R.drawable.banner).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)).into(imageView);
            }
        });
        //设置banner动画效果
        mViewDataBinding.banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(strings);
        //设置自动轮播，默认为true
        mViewDataBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mViewDataBinding.banner.setDelayTime(4000);
        //设置指示器位置（当banner模式中有指示器时）
        mViewDataBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        mViewDataBinding.banner.setOnBannerListener(this);
        //banner设置方法全部调用完毕时最后调用
        mViewDataBinding.banner.start();
    }

    @Override
    public void OnBannerClick(int position) {

    }
}
