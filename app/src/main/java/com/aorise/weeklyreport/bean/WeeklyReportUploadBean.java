package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/5.
 */
public class WeeklyReportUploadBean extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "WeeklyReportUploadBean{" +
                "approvalState=" + approvalState +
                ", byWeek=" + byWeek +
                ", endDate='" + endDate + '\'' +
                ", explain='" + explain + '\'' +
                ", id=" + id +
                ", issue='" + issue + '\'' +
                ", output='" + output + '\'' +
                ", percentComplete=" + percentComplete +
                ", percentStage=" + percentStage +
                ", planId=" + planId +
                ", projectId=" + projectId +
                ", specificItem='" + specificItem + '\'' +
                ", startDate='" + startDate + '\'' +
                ", state=" + state +
                ", type=" + type +
                ", userId=" + userId +
                ", workTime=" + workTime +
                ", workType=" + workType +
                ", weeklyDateModels=" + weeklyDateModels +
                '}';
    }

    /**
     * approvalState : 0
     * byWeek : 0
     * endDate : string
     * explain : string
     * id : 0
     * issue : string
     * output : string
     * percentComplete : 0
     * percentStage : 0
     * planId : 0
     * projectId : 0
     * specificItem : string
     * startDate : string
     * state : 0
     * type : 0
     * userId : 0
     * weeklyDateModels : [{"dayState":0,"workDate":"string"}]
     * workTime : 0
     * workType : 0
     */

    private int approvalState;
    private int byWeek;
    private String endDate;
    private String explain;
    private int id;
    private String issue;
    private String output;
    private double percentComplete;
    private int percentStage;
    private int planId;
    private int projectId;
    private String specificItem;
    private String startDate;
    private int state;
    private int type;
    private int userId;
    private float workTime;
    private int workType;
    private List<WeeklyDateModelsBean> weeklyDateModels;

    public int getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(int approvalState) {
        this.approvalState = approvalState;
    }

    public int getByWeek() {
        return byWeek;
    }

    public void setByWeek(int byWeek) {
        this.byWeek = byWeek;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getPercentStage() {
        return percentStage;
    }

    public void setPercentStage(int percentStage) {
        this.percentStage = percentStage;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getSpecificItem() {
        return specificItem;
    }

    public void setSpecificItem(String specificItem) {
        this.specificItem = specificItem;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getWorkTime() {
        return workTime;
    }

    public void setWorkTime(float workTime) {
        this.workTime = workTime;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public List<WeeklyDateModelsBean> getWeeklyDateModels() {
        return weeklyDateModels;
    }

    public void setWeeklyDateModels(List<WeeklyDateModelsBean> weeklyDateModels) {
        this.weeklyDateModels = weeklyDateModels;
    }

    public static class WeeklyDateModelsBean {
        @Override
        public String toString() {
            return "WeeklyDateModelsBean{" +
                    "dayState=" + dayState +
                    ", workDate='" + workDate + '\'' +
                    '}';
        }

        /**
         * dayState : 0
         * workDate : string
         */

        private int dayState;
        private String workDate;

        public int getDayState() {
            return dayState;
        }

        /**
         * 1 上午 2 下午 3全天
         *
         * @param dayState
         */
        public void setDayState(int dayState) {
            this.dayState = dayState;
        }

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
        }
    }
}
