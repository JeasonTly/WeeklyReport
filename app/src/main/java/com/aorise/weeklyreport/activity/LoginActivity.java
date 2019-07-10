package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.aorise.weeklyreport.MainActivity;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.CommonUtils;
import com.aorise.weeklyreport.base.LogT;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

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
        Gson gson = new Gson();
        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("username", "shenzhiwei");
        requestParam.put("password", "szw123..");
        String json = gson.toJson(requestParam);
        LogT.d(" json " + json);
        RequestBody requestBody = CommonUtils.getRequestBody(json);
        ApiService.Utils.getInstance().login(requestBody)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<String>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Result<String> o) {
                        super.onNext(o);
                        LogT.d(" 11111111 o is " + o);
                    }
                });
        Intent mIntent = new Intent();
        mIntent.setClass(this, MainActivity.class);
        startActivity(mIntent);
    }
}
