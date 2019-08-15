package com.aorise.weeklyreport.base;

import android.content.Context;
import android.content.Intent;

//import aorise.com.food_safety_app.ui.CommonOpenPictureActivity;


/**
 * Created by Administrator on 2018/9/12.
 */

public class MJavascriptInterface {
    private Context context;
    private String[] imageUrls;

    public MJavascriptInterface(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

//    @android.webkit.JavascriptInterface
//    public void openImage(String img) {
//        Intent intent = new Intent();
//        intent.putExtra("url", img);
//        intent.setClass(context, CommonOpenPictureActivity.class);
//        context.startActivity(intent);
//    }
}
