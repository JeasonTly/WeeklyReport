package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.activity.WeeklyReportDetailActivity;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.MulityTypeItem;
import com.aorise.weeklyreport.bean.WeeklyReportBean;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/27.
 */
public class WorkTypeRecyclerAdapter extends BaseAdapter<MulityTypeItem, BaseViewHolder> {
    private ViewDataBinding mViewDataBinding;

    public WorkTypeRecyclerAdapter(Context context, List<MulityTypeItem> typeItems) {
        super(context);
        this.mList = typeItems;
    }

    public enum TypeTAG {
        TYPE_TAG,
        TYPE_CONTENT,
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        LogT.d("onCreateVH");
        if (viewType == TypeTAG.TYPE_TAG.ordinal()) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_tag_description, parent, false);
        } else if (viewType == TypeTAG.TYPE_CONTENT.ordinal()) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_content_list_description, parent, false);
        }
        return new BaseViewHolder(mViewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, final int position) {
        LogT.d(" data is " + mList.get(position));
        if (mList.get(position).getData_type() == TypeTAG.TYPE_TAG.ordinal()) {
            TextView mTagInfo = baseViewHolder.itemView.findViewById(R.id.item_tag_name_txt);
            LogT.d(" mTag info is empty ? " +(mTagInfo == null) + " item view is empty? "+(baseViewHolder.itemView == null));
            mTagInfo.setText(mList.get(position).getItem_name());
            baseViewHolder.itemView.setOnClickListener(null);
            int iconRes = R.drawable.xiangmu;
            switch (mList.get(position).getItem_name()) {
                case "项目工作":
                    iconRes = R.drawable.xiangmu;
                    break;
                case "部门工作":
                    iconRes = R.drawable.bumen;
                    break;
                case "临时工作":
                    iconRes = R.drawable.qita;
                    break;
            }
            ImageView imageView = baseViewHolder.itemView.findViewById(R.id.item_tag_img);
            imageView.setImageResource(iconRes);

        } else if (mList.get(position).getData_type() == TypeTAG.TYPE_CONTENT.ordinal()) {
            final WeeklyReportBean weeklyReportBean = (WeeklyReportBean) mList.get(position).getData();
            baseViewHolder.getBinding().setVariable(BR.workThings, weeklyReportBean);
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogT.d("detail id is " + weeklyReportBean.getId());
                    Intent mIntent = new Intent();
                    mIntent.setClass(mContext, WeeklyReportDetailActivity.class);
                    mIntent.putExtra("reportId", weeklyReportBean.getId());
                    mContext.startActivity(mIntent);
                }
            });
            baseViewHolder.getBinding().executePendingBindings();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getData_type();
    }
}
