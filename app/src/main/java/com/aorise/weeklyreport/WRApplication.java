package com.aorise.weeklyreport;

import android.app.Application;

import com.hjq.toast.ToastUtils;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/2.
 */
public class WRApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}
