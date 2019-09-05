package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.aorise.weeklyreport.BR;
import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.TimePickerBean;
import com.aorise.weeklyreport.databinding.ItemTimeSelectBinding;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/5.
 */
public class TimeSelectAdatper extends BaseAdapter<TimePickerBean, BaseViewHolder> {
    private TimeSelectListener timeSelectListener;

    public TimeSelectAdatper(Context context, List<TimePickerBean> timePickerBeanList, TimeSelectListener timeSelectListener) {
        super(context);
        this.mList = timePickerBeanList;
        this.timeSelectListener = timeSelectListener;
    }

    @Override
    public BaseViewHolder onCreateVH(ViewGroup parent, int viewType) {
        ItemTimeSelectBinding viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_time_select, parent, false);
        return new BaseViewHolder(viewDataBinding);
    }

    @Override
    public void onBindVH(BaseViewHolder baseViewHolder, final int position) {
        baseViewHolder.getBinding().setVariable(BR.timePicker, mList.get(position));
        baseViewHolder.getBinding().setVariable(BR.currentDate,getDay(mList.get(position).getDateName()));
        baseViewHolder.getBinding().executePendingBindings();
        ((ItemTimeSelectBinding) baseViewHolder.getBinding()).am.setChecked(mList.get(position).isAmSelected());
        if(timeSelectListener == null){
            ((ItemTimeSelectBinding) baseViewHolder.getBinding()).am.setEnabled(false);
            ((ItemTimeSelectBinding) baseViewHolder.getBinding()).pm.setEnabled(false);
        }
        ((ItemTimeSelectBinding) baseViewHolder.getBinding()).am.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogT.d("Am select" + isChecked);
                mList.get(position).setAmSelected(isChecked);
                if(timeSelectListener == null){
                    return;
                }
                timeSelectListener.amSelected(position, isChecked);
            }
        });
        ((ItemTimeSelectBinding) baseViewHolder.getBinding()).pm.setChecked(mList.get(position).isPmSelected());
        ((ItemTimeSelectBinding) baseViewHolder.getBinding()).pm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogT.d("Pm select" + isChecked);
                mList.get(position).setPmSelected(isChecked);
                if(timeSelectListener == null){
                    return;
                }
                timeSelectListener.pmSelected(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        LogT.d(" tuliyuan " + super.getItemCount());
        return super.getItemCount();
    }
    private String getDay(String date){
        return date.substring(8,11);
    }
}
