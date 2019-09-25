package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.ItemListProjectBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * 项目列表
 * Date: 2019/6/28.
 */
public class ProjectListAdapter extends BaseAdapter<ProjectList, BaseViewHolder> implements Filterable {

    private RecyclerListClickListener mRecyclerListListener;
    private List<ProjectList> mFilterList = new ArrayList<>();
    private List<ProjectList> mSourceList = new ArrayList<>();
    private boolean isProjectAudit = false;

    public ProjectListAdapter(Context context, List<ProjectList> projectBaseInfos,boolean isProjectAudit) {
        super(context);
        this.mSourceList = projectBaseInfos;
        this.mList = projectBaseInfos;
        this.mFilterList = projectBaseInfos;
        this.isProjectAudit = isProjectAudit;
    }


    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ItemListProjectBinding mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_project, parent, false);
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder viewHolder, final int position) {
       // LogT.d(" mFilter...aaaaa.list is " + mFilterList.toString() +"  mList.get(position) " + mFilterList.get(position).toString());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerListListener.onClick(position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mRecyclerListListener.onLongClick(position);
                return true;
            }
        });
        TextView mReportStates = (TextView)viewHolder.itemView.findViewById(R.id.project_report_status);
        String reportStatus = "";
        int textColor = 0;
        switch (mFilterList.get(position).getWeeklyState()){
            case 1:
                reportStatus = "已审批";
                textColor = Color.rgb(61,208,120);
                break;

            case 3:
                reportStatus = "待审批";
                textColor = Color.rgb(255,108,74);
                break;
                default:
            case 2:
                reportStatus = "未填写";
                textColor = Color.rgb(255,108,74);
                break;
        }
        mReportStates.setVisibility(isProjectAudit? View.VISIBLE:View.INVISIBLE);
        mReportStates.setText(reportStatus);
        mReportStates.setTextColor(textColor);
        viewHolder.getBinding().setVariable(BR.projectbaseinfo, mFilterList.get(position));
        viewHolder.getBinding().executePendingBindings();
    }

    public void setClickListener(RecyclerListClickListener reyclerListClickListener) {
        this.mRecyclerListListener = reyclerListClickListener;
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
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
                } else if (charSequence.equals("项目工作")) {
                    List<ProjectList> filteredList = new ArrayList<>();
                    for (ProjectList str : mSourceList) {
                        if (str.getProperty() == 1) {
                            filteredList.add(str);
                            LogT.d("添加项目工作... " + str.toString());
                        }

                    }
                    mFilterList = filteredList;

                } else if (charSequence.equals("全部工作")) {
                    LogT.d("添加全部工作... " + mSourceList.toString());
                    mFilterList = mSourceList;

                } else if (charSequence.equals("部门工作")) {
                    List<ProjectList> filteredList = new ArrayList<>();
                    for (ProjectList str : mSourceList) {
                        if (str.getProperty() == 2) {
                            filteredList.add(str);
                            LogT.d("添加部门工作... " + str.toString());
                        }
                    }
                    mFilterList = filteredList;

                } else {
                    List<ProjectList> filteredList = new ArrayList<>();
                    for (ProjectList str : mSourceList) {
                        //这里根据需求，添加匹配规则
                        if (str.getName().contains(charString)) {
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
                mFilterList = (ArrayList<ProjectList>) filterResults.values;

                LogT.d(" mFilter..... List is " + mFilterList.toString());
                // refreshData(mFilterList);
                notifyDataSetChanged();
            }
        };
    }
    public List<ProjectList> getmFilterList(){
        if(mFilterList == null){
            mFilterList = new ArrayList<>();
        }
        return mFilterList;
    }
}
