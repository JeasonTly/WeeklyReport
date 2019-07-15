package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/15.
 */
public class AuditReportBean extends BaseObservable {

    /**
     * planStatus : 0 周报完成状态
     * remark : string  审批意见
     * statue : 0 审批状态
     * weeklyId : 0 周报ID
     */

    private int planStatus;
    private String remark;
    private int statue;
    private int weeklyId;

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public int getWeeklyId() {
        return weeklyId;
    }

    public void setWeeklyId(int weeklyId) {
        this.weeklyId = weeklyId;
    }
}
