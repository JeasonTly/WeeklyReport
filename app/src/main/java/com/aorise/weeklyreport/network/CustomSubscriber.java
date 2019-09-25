package com.aorise.weeklyreport.network;

import android.content.Context;
import android.util.Log;

import com.aorise.weeklyreport.base.LogT;

import dmax.dialog.SpotsDialog;
import rx.Subscriber;


/**
 * Created by Xun.Yang on 2017/8/14.
 */

public abstract class CustomSubscriber<T> extends Subscriber<T> {
    private Context mContext;
    private SpotsDialog mSpotsDialog;

    public CustomSubscriber(Context context) {
        mContext = context;
    }


    @Override
    public void onStart() {
        super.onStart();
        mSpotsDialog = new SpotsDialog(mContext);
        mSpotsDialog.show();
    }

    @Override
    public void onCompleted() {
        Log.d("Retrofit", "oCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        //隐藏loading
        mSpotsDialog.dismiss();
        LogT.d("Retrofit Error is " + e);
    }

    //
    @Override
    public void onNext(T t) {
        //隐藏loading
        mSpotsDialog.dismiss();
    }
}
