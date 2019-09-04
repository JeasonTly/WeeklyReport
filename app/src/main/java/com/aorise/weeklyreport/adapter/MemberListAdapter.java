package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.AuditWeeklyReportActivity;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.base.TimeUtil;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.ItemListMemberBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * 项目成员列表
 * Date: 2019/6/28.
 */
public class MemberListAdapter extends BaseAdapter<MemberListBean.ListBean, BaseViewHolder> implements Filterable {

    private RecyclerListClickListener mRecyclerListListener;
    private List<MemberListBean.ListBean> mFilterList = new ArrayList<>();
    private List<MemberListBean.ListBean> mSourceList = new ArrayList<>();

    public MemberListAdapter(Context context, List<MemberListBean.ListBean> projectBaseInfos) {
        super(context);
        this.mList = projectBaseInfos;
        this.mSourceList = projectBaseInfos;
        this.mFilterList = projectBaseInfos;
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ItemListMemberBinding mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_member, parent, false);
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mRecyclerListListener.onClick(position);
                if(mRecyclerListListener !=null){
                    mRecyclerListListener.onClick(position);
                }

            }
        });
        TextView reportStates = (TextView)viewHolder.itemView.findViewById(R.id.reportStates_txt);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
             //   mRecyclerListListener.onLongClick(position);
                return true;
            }
        });
        String weeklyStates = "";
        switch (mFilterList.get(position).getWeeklyState()){
            case 1:
                weeklyStates ="已审批";
                reportStates.setBackground(mContext.getResources().getDrawable(R.drawable.report_states_green));
                reportStates.setTextColor(Color.rgb(61,208,120));
                break;
            case 2:
                weeklyStates ="未填写";
                reportStates.setBackground(mContext.getResources().getDrawable(R.drawable.report_states_orange));
                reportStates.setTextColor(Color.rgb(255,108,74));
                break;
            case 3:
                weeklyStates ="待审批";
                reportStates.setBackground(mContext.getResources().getDrawable(R.drawable.report_states_orange));
                reportStates.setTextColor(Color.rgb(255,108,74));
                break;
        }
        reportStates.setText(weeklyStates);
        String planTime = mFilterList.get(position).getPlanWorkTime()+"天";
        String useTime = mFilterList.get(position).getUseWorkTime()+"天";
        viewHolder.getBinding().setVariable(BR.planTime, planTime);
        viewHolder.getBinding().setVariable(BR.useTime, useTime);
        viewHolder.getBinding().setVariable(BR.startTime,TimeUtil.getInstance().date2date(mFilterList.get(position).getStartTime()));
        viewHolder.getBinding().setVariable(BR.endTime,TimeUtil.getInstance().date2date(mFilterList.get(position).getEndTime()));
        //viewHolder.getBinding().setVariable(BR.reportStates, weeklyStates);
        viewHolder.getBinding().setVariable(BR.memberlist, mFilterList.get(position));
        viewHolder.getBinding().executePendingBindings();
    }

    public void setClickListener(RecyclerListClickListener reyclerListClickListener) {
        this.mRecyclerListListener = reyclerListClickListener;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = mSourceList;
                    LogT.d(" mFilter..... " + mSourceList.toString());
                } else {
                    List<MemberListBean.ListBean> filteredList = new ArrayList<>();
                    for (MemberListBean.ListBean str : mSourceList) {
                        //这里根据需求，添加匹配规则
                        if (str.getUserName().contains(charString)) {
                            filteredList.add(str);
                            LogT.d(" filteredList add " + str.toString());
                        }
                    }
                    mFilterList = filteredList;

                }
                LogT.d(" mFilter List is " + mFilterList.toString());

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<MemberListBean.ListBean>) filterResults.values;

                LogT.d(" mFilter..... List is " + mFilterList.toString());
                // refreshData(mFilterList);
                notifyDataSetChanged();
            }
        };
    }

}
