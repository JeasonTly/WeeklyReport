package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.HeaderItemBean;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/27.
 * 项目周报适配器
 */
public class ProjectManagerReportRecclerAdapter extends BaseAdapter<HeaderItemBean.PlanDetailsListBean, BaseViewHolder> {
    private ViewDataBinding mViewDataBinding;
    private RecyclerListClickListener mClickListener;

    public ProjectManagerReportRecclerAdapter(Context context, List<HeaderItemBean.PlanDetailsListBean> typeItems) {
        super(context);
        this.mList = typeItems;
    }


    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_stage_content, parent, false);
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, final int position) {
        final HeaderItemBean.PlanDetailsListBean mBean = mList.get(position);
        baseViewHolder.getBinding().setVariable(BR.headerReport, mBean);
        String isComplete = "";
        switch (mBean.getIsComplete()) {
            case 1:
                isComplete = "完成";
                break;
            case 2:
            default:
                isComplete = "未完成";
                break;
        }
        String Stage = mBean.getStage() + "%";
        String PercentComplete = mBean.getPercentComplete() + "%";
        baseViewHolder.getBinding().setVariable(BR.isComplete, isComplete);
        baseViewHolder.getBinding().setVariable(BR.Stage, Stage);
        baseViewHolder.getBinding().setVariable(BR.PercentComplete, PercentComplete);
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(position);
                }
            }
        });
        baseViewHolder.getBinding().executePendingBindings();
    }

    public void setItemClickListener(RecyclerListClickListener recyclerListClickListener) {
        this.mClickListener = recyclerListClickListener;
    }

    @Override
    public int getItemCount() {
        LogT.d(" mList size is " + mList.size());
        return mList.size();
    }
}
