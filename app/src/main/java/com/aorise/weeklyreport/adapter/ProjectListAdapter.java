package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.bean.ProjectBaseInfo;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.databinding.ItemListProjectBinding;

import java.util.List;

/**
 * Created by Tuliyuan.
 * 项目列表
 * Date: 2019/6/28.
 */
public class ProjectListAdapter extends BaseAdapter<ProjectList, BaseViewHolder> {

    private RecyclerListClickListener mRecyclerListListener;

    public ProjectListAdapter(Context context, List<ProjectList> projectBaseInfos) {
        super(context);
        this.mList = projectBaseInfos;
    }


    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ItemListProjectBinding mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_list_project, parent, false);
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder viewHolder, final int position) {

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
        viewHolder.getBinding().setVariable(BR.projectbaseinfo, mList.get(position));
        viewHolder.getBinding().executePendingBindings();
    }

    public void setClickListener(RecyclerListClickListener reyclerListClickListener) {
        this.mRecyclerListListener = reyclerListClickListener;
    }

}
