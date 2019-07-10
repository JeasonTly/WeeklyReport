package com.aorise.weeklyreport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aorise.weeklyreport.R;
import com.aorise.weeklyreport.bean.ProjectBaseInfo;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/2.
 */
public class CustomProjectSpinnerAdapter extends ArrayAdapter {
    private List<ProjectList> dataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private int layoutRes;
    public CustomProjectSpinnerAdapter(Context context, int resource, List<ProjectList> objects) {
        super(context, resource, objects);
        dataList = objects;
        layoutRes = resource;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public String getItem(int position) {
        return dataList.get(position).getName();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(layoutRes, null);
        TextView textView = (TextView)view;
        textView.setText(getItem(position));
        return view;
    }
}
