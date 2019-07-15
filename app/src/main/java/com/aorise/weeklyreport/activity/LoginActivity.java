package com.aorise.weeklyreport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.aorise.weeklyreport.MainActivity;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.UserInfoBean;
import com.aorise.weeklyreport.databinding.ActivityLoginBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding mViewDataBinding;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        WRApplication.getInstance().addActivity(this);
    }

    public void LoginClick(View view) {
//        if(TextUtils.isEmpty(mViewDataBinding.editAccountName.getText())){
//            ToastUtils.show("账户名为空，请输入！");
//            return;
//        }
//        if(TextUtils.isEmpty(mViewDataBinding.editPwd.getText())){
//            ToastUtils.show("密码为空，请输入！");
//            return;
//        }
//        Gson gson = new Gson();
//        Map<String, Object> requestParam = new HashMap<>();
//        requestParam.put("username", "shenzhiwei");
//        requestParam.put("password", "szw123..");
//        String json = gson.toJson(requestParam);
//        LogT.d(" json " + json);
        String userName = mViewDataBinding.userName.getText().toString();
        String pwd = mViewDataBinding.pwd.getText().toString();
        //RequestBody requestBody = CommonUtils.getRequestBody(json);
        ApiService.Utils.getInstance(this).login(userName,pwd)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<UserInfoBean>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<UserInfoBean> o) {
                        super.onNext(o);
                        LogT.d(" 11111111 o is " + o);
                        if(o.isRet()){
                            editor = sp.edit();
                            editor.putString("userId",String.valueOf(o.getData().getId()));
                            editor.putString("fullName",o.getData().getFullName());
                            editor.putString("uuid",o.getData().getUuid());
                            if(o.getData().getRoleName().equals("普通成员")){
                                editor.putInt("userRole",0);
                            }else{
                                editor.putInt("userRole",1);
                            }
                            editor.apply();
                        }
                    }
                });
        Intent mIntent = new Intent();
        mIntent.setClass(this, MainActivity.class);
        startActivity(mIntent);
    }
}
