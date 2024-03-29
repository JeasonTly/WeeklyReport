package com.aorise.weeklyreport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.aorise.weeklyreport.base.StatusBarUtil;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/9.
 */
@Deprecated
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
    }
    public void openActivity(Class<?> pClass) {
        this.openActivity(pClass, (Bundle) null);
    }

    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        this.startActivity(intent);
    }

}
