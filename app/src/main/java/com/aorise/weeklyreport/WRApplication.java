package com.aorise.weeklyreport;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.aorise.weeklyreport.activity.LoginActivity;
import com.hjq.toast.ToastUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/2.
 */

public class WRApplication extends Application {
    private List<Activity> activityList = new LinkedList<Activity>();
    private static WRApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
    //单例模式中获取唯一的MyApplication实例
    public static WRApplication getInstance() {
        if(null == instance) {
            instance = new WRApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity)  {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for(Activity activity:activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    public void LoginExit(Context context) {
        Intent mIntent = new Intent();
        mIntent.setClass(context,LoginActivity.class);
        context.startActivity(mIntent);
    }
}
