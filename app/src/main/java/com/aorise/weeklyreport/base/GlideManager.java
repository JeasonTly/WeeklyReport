package com.aorise.weeklyreport.base;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideManager {

    private static GlideManager ourInstance = new GlideManager();

    public static GlideManager getInstance() {
        return ourInstance;
    }

    private GlideManager() {
    }

    public <T> void load(Context context, ImageView view, T uri, @DrawableRes int placeholder, @DrawableRes int error) {
        Glide.with(context).load(uri).apply(new RequestOptions().placeholder(placeholder).error(error).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)).into(view);
    }
}