package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.HeaderWeeklyReportDetailActivity;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MulityTypeItem;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/27.
 */
public class MulityStageRecyclerAdapter extends BaseAdapter<MulityTypeItem, BaseViewHolder> {
    private ViewDataBinding mViewDataBinding;

    public MulityStageRecyclerAdapter(Context context, List<MulityTypeItem> typeItems) {
        super(context);
        this.mList = typeItems;
    }

    public enum TypeTAG {
        TYPE_TAG,
        TYPE_CONTENT,
        TYEP_NULL,
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        if (viewType == TypeTAG.TYPE_TAG.ordinal()) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_stage_title, parent, false);
        } else if (viewType == TypeTAG.TYPE_CONTENT.ordinal()) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_stage_content, parent, false);
        } else if (viewType == TypeTAG.TYEP_NULL.ordinal()) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_stage_null, parent, false);
        }
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, final int position) {
        if (mList.get(position).getData_type() == TypeTAG.TYPE_TAG.ordinal()) {
            final HeaderItemBean.PlanDetailsListBean mBean = (HeaderItemBean.PlanDetailsListBean) mList.get(position).getData();
            TextView textView = (TextView) baseViewHolder.itemView.findViewById(R.id.item_stage_title);
            TextView textPercent = (TextView) baseViewHolder.itemView.findViewById(R.id.item_stage_percent);
            textView.setText(mList.get(position).getItem_name());
            textPercent.setText(mBean.getPercentComplete() + "%");
        } else if (mList.get(position).getData_type() == TypeTAG.TYPE_CONTENT.ordinal()) {
            final HeaderItemBean.PlanDetailsListBean mBean = (HeaderItemBean.PlanDetailsListBean) mList.get(position).getData();
            baseViewHolder.getBinding().setVariable(BR.stagecontent, mBean);
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent();
                    mIntent.setClass(mContext, HeaderWeeklyReportDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail", mBean);
                    mIntent.putExtra("item_detail", bundle);
                    mContext.startActivity(mIntent);
                }
            });
            baseViewHolder.getBinding().executePendingBindings();
        } else if (mList.get(position).getData_type() == TypeTAG.TYEP_NULL.ordinal()) {
            baseViewHolder.getBinding().setVariable(BR.stagenull, mList.get(position));
            baseViewHolder.getBinding().executePendingBindings();
        }
    }


    @Override
    public int getItemViewType(int position) {
        LogT.d(" type is " + mList.get(position).getData_type());
        return mList.get(position).getData_type();
    }

    @Override
    public int getItemCount() {
        LogT.d(" mList size is " + mList.size());
        return mList.size();
    }
}
