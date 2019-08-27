package com.aorise.weeklyreport.base;

import android.content.Context;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/21.
 */
public class DensityUtil {

    public static int dip2px(Context mContext,float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(Context mContext,float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
