package com.aorise.weeklyreport.network;

import android.content.Context;
import android.util.Log;

import com.aorise.weeklyreport.base.LogT;

import rx.Subscriber;

/**
 * Created by Xun.Yang on 2017/8/14.
 */

public abstract class CustomSubscriberNoDialog<T> extends Subscriber<T> {
    private Context mContext;

    public CustomSubscriberNoDialog(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCompleted() {
        Log.d("Retrofit", "oCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        LogT.d("Retrofit Error is " + e);
    }

    @Override
    public void onNext(T t) {
        //反射获取私有属性code
    }
}
