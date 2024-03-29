package com.aorise.weeklyreport.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.WeeklyReportBean;
import com.aorise.weeklyreport.network.ApiService;
import com.aorise.weeklyreport.network.CustomSubscriber;
import com.aorise.weeklyreport.network.Result;
import com.hjq.toast.ToastUtils;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/27.
 *  周报详情适配器
 */
public class WorkTypeRecyclerAdapter extends BaseAdapter<WeeklyReportBean, BaseViewHolder> {
    private ViewDataBinding mViewDataBinding;
    private RecyclerListClickListener recyclerListClickListener;

    public WorkTypeRecyclerAdapter(Context context, List<WeeklyReportBean> typeItems, RecyclerListClickListener recyclerListClickListener) {
        super(context);
        this.mList = typeItems;
        this.recyclerListClickListener = recyclerListClickListener;
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_content_list_description, parent, false);
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, final int position) {
        LogT.d(" data is " + mList.get(position));
        final WeeklyReportBean weeklyReportBean = mList.get(position);
        String workType = "";
        switch (weeklyReportBean.getWorkType()){
            case 1:
                workType = "项目工作";
                break;
            case 2:
                workType = "部门工作";
                break;
            case 3:
                workType = "其他工作";
                break;
        }

        String approvalStatus = "";
        switch (weeklyReportBean.getApprovalState()){
            case 1:
                approvalStatus = "未审批";
                break;
            case 2:
                approvalStatus = "已通过";
                break;
            case 3:
                approvalStatus = "已驳回";
                break;
        }

        String itemworkTime = weeklyReportBean.getWorkTime() + "天";
        String percentComplete = weeklyReportBean.getPercentComplete() + "%";
        baseViewHolder.getBinding().setVariable(BR.workThings, weeklyReportBean);
        baseViewHolder.getBinding().setVariable(BR.workType, workType);
        baseViewHolder.getBinding().setVariable(BR.itemworkTime, itemworkTime);
        baseViewHolder.getBinding().setVariable(BR.percentComplete, percentComplete);
        baseViewHolder.getBinding().setVariable(BR.approvalStatus, approvalStatus);
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerListClickListener.onClick(position);
            }
        });
        baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               if(recyclerListClickListener!=null){
                   recyclerListClickListener.onLongClick(position);
               }
                return true;
            }
        });
        baseViewHolder.getBinding().executePendingBindings();

    }

}
