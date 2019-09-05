package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/5.
 */
public class TimePickerBean extends BaseObservable {
    @Override
    public String toString() {
        return "TimePickerBean{" +
                "weekName='" + weekName + '\'' +
                ", dateName='" + dateName + '\'' +
                ", selectStates=" + selectStates +
                ", amSelected=" + amSelected +
                ", pmSelected=" + pmSelected +
                '}';
    }

    private String weekName;
    private String dateName;
    private int selectStates;
    private boolean amSelected;
    private boolean pmSelected;

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public int getSelectStates() {
        if(isAmSelected()){
            selectStates = 1;
            if(isPmSelected()){
                selectStates = 3;
            }
        }else if(isPmSelected()){
            selectStates = 2;
        }
        else{
            selectStates = -1;
        }
        return selectStates;
    }

//    public void setSelectStates(int selectStates) {
//        this.selectStates = selectStates;
//    }

    public boolean isAmSelected() {
        return amSelected;
    }

    public void setAmSelected(boolean amSelected) {
        this.amSelected = amSelected;
    }

    public boolean isPmSelected() {
        return pmSelected;
    }

    public void setPmSelected(boolean pmSelected) {
        this.pmSelected = pmSelected;
    }
}
