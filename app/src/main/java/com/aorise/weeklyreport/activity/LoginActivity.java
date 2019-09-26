package com.aorise.weeklyreport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.aorise.weeklyreport.MainActivity;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.WRApplication;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.PermissionBean;
import com.aorise.weeklyreport.bean.UserInfoBean;
import com.aorise.weeklyreport.bean.UserRole;
import com.aorise.weeklyreport.databinding.ActivityLoginBinding;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding mViewDataBinding;
    private SharedPreferences sp, spAccount;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        spAccount = getSharedPreferences("UserAccount", Context.MODE_PRIVATE);
        String userName = spAccount.getString("userName", "");
        String pwd = spAccount.getString("pwd", "");
        boolean shouldAutonLogin = sp.getBoolean("autoLogin", false);
        mViewDataBinding.userName.setText(userName);
        mViewDataBinding.pwd.setText(pwd);
        if (shouldAutonLogin && !TextUtils.isEmpty(mViewDataBinding.userName.getText().toString()) && !TextUtils.isEmpty(mViewDataBinding.pwd.getText().toString())) {
            LoginClick(mViewDataBinding.btnLogin);
        }
        WRApplication.getInstance().addActivity(this);
        mViewDataBinding.userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewDataBinding.pwd.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mViewDataBinding.pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    LoginClick(mViewDataBinding.btnLogin);
                }
                return false;
            }
        });
    }

    public void LoginClick(View view) {
        if (TextUtils.isEmpty(mViewDataBinding.userName.getText())) {
            ToastUtils.show("账户名为空，请输入！");
            return;
        }
        if (TextUtils.isEmpty(mViewDataBinding.pwd.getText())) {
            ToastUtils.show("密码为空，请输入！");
            return;
        }
        String userName = mViewDataBinding.userName.getText().toString();
        String pwd = mViewDataBinding.pwd.getText().toString();

        ApiService.Utils.getInstance(this).login(userName, pwd)
                .compose(ApiService.Utils.schedulersTransformer())
                .subscribe(new CustomSubscriber<Result<UserInfoBean>>(this) {
                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // ToastUtils.show("登录失败");
                    }

                    @Override
                    public void onNext(Result<UserInfoBean> o) {
                        super.onNext(o);
                        LogT.d(" 11111111 o is " + o.toString());
                        if (o.isRet()) {

                            editor = sp.edit();
                            editor.putInt("userId", o.getData().getId());
                            editor.putString("fullName", o.getData().getFullName());
                            editor.putString("uuid", o.getData().getUuid());
                            editor.putInt("userRole", o.getData().getRoleId());
                            initDefaultPermission(editor);

                            for(UserInfoBean.PermissionModelListBean permissionModelListBean : o.getData().getPermissionModelList()){
                                if(permissionModelListBean.getName().equals("项目概况")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PROJECT_BASE_INFO, true);
                                }
                                if(permissionModelListBean.getName().equals("项目管理")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PROJECT_MANAGER, true);
                                }
                                if(permissionModelListBean.getName().equals("工作周报")){
                                    editor.putBoolean(PermissionBean.PERMISSION_WORK_REPORT, true);
                                }
                                if(permissionModelListBean.getName().equals("成员周报审核")){
                                    editor.putBoolean(PermissionBean.PERMISSION_MEMBER_AUDIT, true);
                                }
                                if(permissionModelListBean.getName().equals("项目周报审核")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PROJECT_AUDIT, true);
                                }
                                if(permissionModelListBean.getName().equals("成员周报填写")){
                                    editor.putBoolean(PermissionBean.PERMISSION_WEEKLY_WRITE, true);
                                }
                                if(permissionModelListBean.getName().equals("项目周报填写")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PROJECT_WRITE, true);
                                }
                                if(permissionModelListBean.getName().equals("绩效管理")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PERFORMANCE_MANAGER, true);
                                }
                                if(permissionModelListBean.getName().equals("绩效查看")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PERFORMANCE_VIEW, true);
                                }
                                if(permissionModelListBean.getName().equals("绩效审核")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PERFORMANCE_AUDIT, true);
                                }
                                if(permissionModelListBean.getName().equals("工时统计")){
                                    editor.putBoolean(PermissionBean.PERMISSION_WORKTIME, true);
                                }
                                if(permissionModelListBean.getName().equals("系统管理")){
                                    editor.putBoolean(PermissionBean.PERMISSION_SYSTEM_MANAGER, true);
                                }
                                if(permissionModelListBean.getName().equals("立项管理")){
                                    editor.putBoolean(PermissionBean.PERMISSION_NEW_PROJECT, true);
                                }
                                if(permissionModelListBean.getName().equals("角色管理")){
                                    editor.putBoolean(PermissionBean.PERMISSION_ROLE_MANAGER, true);
                                }
                                if(permissionModelListBean.getName().equals("用户管理")){
                                    editor.putBoolean(PermissionBean.PERMISSION_USER_MANAGER, true);
                                }
                                if(permissionModelListBean.getName().equals("项目周报查看")){
                                    editor.putBoolean(PermissionBean.PERMISSION_PROJECT_REPORT_VIEW, true);
                                }
                            }

                            editor.putBoolean("autoLogin", true);
                            editor.apply();
                            SharedPreferences.Editor accountEditor = spAccount.edit();
                            accountEditor.putString("userName", mViewDataBinding.userName.getText().toString());
                            accountEditor.putString("pwd", mViewDataBinding.pwd.getText().toString());
                            accountEditor.commit();

                            Intent mIntent = new Intent();
                            mIntent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(mIntent);
                        } else {

                            ToastUtils.show(o.getMessage());
                        }
                    }
                });

    }

    private void initDefaultPermission(SharedPreferences.Editor editor) {
        //项目概况
        editor.putBoolean(PermissionBean.PERMISSION_PROJECT_BASE_INFO, false);
        //项目管理
        editor.putBoolean(PermissionBean.PERMISSION_PROJECT_MANAGER, false);
        //成员周报填写
        editor.putBoolean(PermissionBean.PERMISSION_WEEKLY_WRITE, false);
        //项目周报填写
        editor.putBoolean(PermissionBean.PERMISSION_PROJECT_WRITE, false);
        //工作周报
        editor.putBoolean(PermissionBean.PERMISSION_WORK_REPORT, false);
        //绩效管理
        editor.putBoolean(PermissionBean.PERMISSION_PERFORMANCE_MANAGER, false);
        //绩效审核
        editor.putBoolean(PermissionBean.PERMISSION_PERFORMANCE_AUDIT, false);
        //绩效查看
        editor.putBoolean(PermissionBean.PERMISSION_PERFORMANCE_VIEW, false);
        //系统管理
        editor.putBoolean(PermissionBean.PERMISSION_SYSTEM_MANAGER, false);
        //立项管理
        editor.putBoolean(PermissionBean.PERMISSION_NEW_PROJECT, false);
        //角色管理
        editor.putBoolean(PermissionBean.PERMISSION_ROLE_MANAGER, false);
        //用户管理
        editor.putBoolean(PermissionBean.PERMISSION_USER_MANAGER, false);
        //工时统计
        editor.putBoolean(PermissionBean.PERMISSION_WORKTIME, false);
        //项目周报审核
        editor.putBoolean(PermissionBean.PERMISSION_PROJECT_AUDIT, false);
        //成员周报审核
        editor.putBoolean(PermissionBean.PERMISSION_MEMBER_AUDIT, false);
        //项目周报查看
        editor.putBoolean(PermissionBean.PERMISSION_PROJECT_REPORT_VIEW, false);
    }

    @Override
    public void onBackPressed() {
        WRApplication.getInstance().exit();
    }
}
