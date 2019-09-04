package com.aorise.weeklyreport.adapter;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/28.
 * recyclerview的点击事件
 */
public interface RecyclerListClickListener {
    void onClick(final int position);
    void onLongClick(final int position);
}
