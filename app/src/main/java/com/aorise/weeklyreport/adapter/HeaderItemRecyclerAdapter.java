package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.HeaderWeeklyReportDetailActivity;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.databinding.ItemListHeaderBinding;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/8.
 */
public class HeaderItemRecyclerAdapter extends BaseAdapter<HeaderItemBean.PlanDetailsListBean, BaseViewHolder> {
    public HeaderItemRecyclerAdapter(Context context, List<HeaderItemBean.PlanDetailsListBean> list) {
        super(context);
        mList = list;
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        LogT.d("onCreateVH");
        ViewDataBinding mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_header, parent, false);
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == (mList.size() - 1)) {
                    //TODO 编辑周报总结界面
                    LogT.d("编辑周报总结界面");
                } else {
                    //TODO 常规查看详情界面
                    LogT.d("常规查看详情界面");
                    Intent mIntent = new Intent();
                    mIntent.setClass(mContext, HeaderWeeklyReportDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail", mList.get(position));
                    mIntent.putExtra("item_detail", bundle);
                    mContext.startActivity(mIntent);
                }
            }
        });
        viewHolder.getBinding().setVariable(BR.header, mList.get(position));
        viewHolder.getBinding().executePendingBindings();
    }

}
