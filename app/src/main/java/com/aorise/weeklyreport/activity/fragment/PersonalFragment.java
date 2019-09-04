package com.aorise.weeklyreport.activity.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.bean.PersonalBean;
import com.aorise.weeklyreport.databinding.FragmentPersonalBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;

/**
 * 个人首页
 */
public class PersonalFragment extends Fragment {

    private int userId = 2;
    private FragmentPersonalBinding mViewDataBinding;

    public PersonalFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        initPersonalInfo();
    }

    /**
     * 获取个人信息接口，
     * 参数为userId
     */
    private void initPersonalInfo() {
        ApiService.Utils.getInstance(getContext()).getPersonalInfo(userId)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<PersonalBean>>(getContext()) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<PersonalBean> data) {
                        super.onNext(data);
                        mViewDataBinding.setPersonalInfo(data.getData());
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal, container, false);
        mViewDataBinding.personalActionbar.actionBarTitle.setText("个人信息");

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", 2);
        int isManager = sharedPreferences.getInt("userRole", -1);
        String isManagerText = "";
        switch (isManager) {
            case 0:
                isManagerText = "普通成员";
                break;
            case 1:
                isManagerText = "项目负责人";
                break;
            case 2:
                isManagerText = "超级管理员";
                break;
            default:
                isManagerText = "无信息";
                break;
        }
        mViewDataBinding.personalJobs.setText(isManagerText);
        mViewDataBinding.personalActionbar.actionbarBack.setVisibility(View.GONE);
        mViewDataBinding.personalExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("autoLogin", false);
                editor.commit();
                WRApplication.getInstance().LoginExit(getActivity());
            }
        });
        return mViewDataBinding.getRoot();
    }

}
