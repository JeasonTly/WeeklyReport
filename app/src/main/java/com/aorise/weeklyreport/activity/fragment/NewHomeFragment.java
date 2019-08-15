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
import com.aorise.weeklyreport.activity.MemberManagerActivity;
import com.aorise.weeklyreport.activity.ProjectInfoActivity;
import com.aorise.weeklyreport.activity.ReviewAndToFillReportActivity;
import com.aorise.weeklyreport.base.GlideImageLoader;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.FragmentNewHomeBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.CustomSubscriberNoDialog;
import com.aorise.weeklyreport.network.Result;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
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
    private int userId; //用户或者项目负责人ID
    private boolean isHeader = false; //是否为项目负责人


    public NewHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        isHeader = sharedPreferences.getInt("userRole", -1) == 1; // 1为项目负责人，0为项目成员

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
                queryProjectList(false);
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
//                Intent mIntent = new Intent();
//                mIntent.setClass(getActivity(), ReviewAndToFillReportActivity.class);
//                startActivity(mIntent);
                queryProjectList(true);
            }
        });
        //项目负责人周报
        mViewDataBinding.projectReportArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseProject();
            }
        });
        initBanner();
        return mViewDataBinding.getRoot();
    }

    private void initSpan() {
        SpannableString projectText = new SpannableString("项目概况 Project");
        RelativeSizeSpan largeSpan = new RelativeSizeSpan(2.0f);
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
    }

    private void queryProjectList(final boolean isReview) {
        if (!isHeader) {
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
                                        mIntent.putExtra("isReview", isReview);
                                        mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                        startActivity(mIntent);
                                    }
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
                                }
                            }
                        }
                    });
        }
    }

    private void startChooseProject() {

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
                                    mIntent.putExtra("projectId", mProjectList.get(0));
                                    mIntent.putExtra("userId", userId);
                                    mIntent.setClass(getActivity(), MemberManagerActivity.class);
                                    startActivity(mIntent);
                                } else {
                                    LogT.d("mProjectList size 大于1");
                                    Intent mIntent = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("_projectList", (Serializable) mProjectList);
                                    mIntent.putExtra("projectList", bundle);
                                    mIntent.putExtra("userId", userId);
                                    mIntent.putExtra("isHeaderReport",true);
                                    mIntent.setClass(getActivity(), ChooseProjectActivity.class);
                                    startActivity(mIntent);
                                }
                            }
                        }
                    }
                });

    }
    private void initBanner(){
        //设置banner样式
        List<String> list = new ArrayList<>();
        list.add("http://ym-moblie.oss-cn-shenzhen.aliyuncs.com//oss/63aa83e49b3f45dfa1a72520b93bb603.jpg");
        list.add("http://ym-moblie.oss-cn-shenzhen.aliyuncs.com//oss/e1d86d1197214d60a2756f013a9b31b1.jpg");
        list.add("http://ym-moblie.oss-cn-shenzhen.aliyuncs.com//oss/71ceab6cf6254c859fa06f63175b2b8e.jpg");
        mViewDataBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mViewDataBinding.banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mViewDataBinding.banner.setImages(list);
        mViewDataBinding.banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                System.out.println((String) path);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
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
