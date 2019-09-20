package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/4.
 */
public class WeeklyReportDetailBean extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "WeeklyReportDetailBean{" +
                "id=" + id +
                ", byWeek=" + byWeek +
                ", projectId=" + projectId +
                ", type=" + type +
                ", workType=" + workType +
                ", planId=" + planId +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", percentComplete=" + percentComplete +
                ", workTime=" + workTime +
                ", output='" + output + '\'' +
                ", explain='" + explain + '\'' +
                ", issue='" + issue + '\'' +
                ", state=" + state +
                ", approvalState=" + approvalState +
                ", userId=" + userId +
                ", projectName='" + projectName + '\'' +
                ", planName='" + planName + '\'' +
                ", specificItem='" + specificItem + '\'' +
                ", approvalModelList=" + approvalModelList +
                ", weeklyDateModels=" + weeklyDateModels +
                '}';
    }

    /**
     * id : 165
     * byWeek : 33
     * projectId : 26
     * type : 1
     * workType : 1
     * planId : 114
     * startDate : 2019-08-12 00:00:00
     * endDate : 2019-08-16 00:00:00
     * percentComplete : 100
     * workTime : 5
     * output : bug清单，已提交《辰溪公安智能箱运维平台bug》、jira
     * explain : 1、完成用户账号、监控箱统计、工单走势分析、工单综合统计管理模块的第一轮功能测试100%
     2、完成《辰溪公安智能箱运维平台bug》回归测试、jira共提出10个bug,已回归10个
     * issue : 运行概览、补光规则模块后新增的，暂未测试
     * state : 2
     * approvalState : 1
     * userId : 45
     * projectName : 辰溪公安智能箱运维平台
     * planName : 叶婷功能测试
     * approvalModelList : [{"id":662,"weeklyId":165,"remark":"","approvalTime":"2019-08-16 17:50:24","approvalState":1}]
     * weeklyDateModels : [{"weeklyId":165,"workDate":"2019-08-12"},{"weeklyId":165,"workDate":"2019-08-13"},{"weeklyId":165,"workDate":"2019-08-14"},{"weeklyId":165,"workDate":"2019-08-15"},{"weeklyId":165,"workDate":"2019-08-16"}]
     */

    private int id;
    private int byWeek;
    private int projectId;
    private int type;
    private int workType;
    private int planId;
    private String startDate;
    private String endDate;
    private double percentComplete;
    private float workTime;
    private String output;
    private String explain;
    private String issue;
    private int state;
    private int approvalState;
    private int userId;
    private String projectName;
    private String planName;
    private String specificItem;
    private List<ApprovalModelListBean> approvalModelList;
    private List<WeeklyDateModelsBean> weeklyDateModels;
    private int weeklyType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getByWeek() {
        return byWeek;
    }

    public void setByWeek(int byWeek) {
        this.byWeek = byWeek;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public float getWorkTime() {
        return workTime;
    }

    public void setWorkTime(float workTime) {
        this.workTime = workTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(int approvalState) {
        this.approvalState = approvalState;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSpecificItem() {
        return specificItem;
    }

    public void setSpecificItem(String specificItem) {
        this.specificItem = specificItem;
    }

    public List<ApprovalModelListBean> getApprovalModelList() {
        return approvalModelList;
    }

    public void setApprovalModelList(List<ApprovalModelListBean> approvalModelList) {
        this.approvalModelList = approvalModelList;
    }

    public List<WeeklyDateModelsBean> getWeeklyDateModels() {
        return weeklyDateModels;
    }

    public void setWeeklyDateModels(List<WeeklyDateModelsBean> weeklyDateModels) {
        this.weeklyDateModels = weeklyDateModels;
    }

    public static class ApprovalModelListBean implements Serializable{
        @Override
        public String toString() {
            return "ApprovalModelListBean{" +
                    "id=" + id +
                    ", weeklyId=" + weeklyId +
                    ", remark='" + remark + '\'' +
                    ", approvalTime='" + approvalTime + '\'' +
                    ", approvalState=" + approvalState +
                    '}';
        }

        /**
         * id : 662
         * weeklyId : 165
         * remark :
         * approvalTime : 2019-08-16 17:50:24
         * approvalState : 1
         */

        private int id;
        private int weeklyId;
        private String remark;
        private String approvalTime;
        private int approvalState;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWeeklyId() {
            return weeklyId;
        }

        public void setWeeklyId(int weeklyId) {
            this.weeklyId = weeklyId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getApprovalTime() {
            return approvalTime;
        }

        public void setApprovalTime(String approvalTime) {
            this.approvalTime = approvalTime;
        }

        public int getApprovalState() {
            return approvalState;
        }

        public void setApprovalState(int approvalState) {
            this.approvalState = approvalState;
        }
    }

    public static class WeeklyDateModelsBean implements Serializable {
        @Override
        public String toString() {
            return "WeeklyDateModelsBean{" +
                    "weeklyId=" + weeklyId +
                    ", workDate='" + workDate + '\'' +
                    ", dayState='" + dayState + '\'' +
                    '}';
        }

        /**
         * weeklyId : 165
         * workDate : 2019-08-12
         */

        private int weeklyId;
        private String workDate;
        private int dayState;

        public int getWeeklyId() {
            return weeklyId;
        }

        public void setWeeklyId(int weeklyId) {
            this.weeklyId = weeklyId;
        }

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
        }

        public int getDayState() {
            return dayState;
        }

        public void setDayState(int dayState) {
            this.dayState = dayState;
        }
    }

    public int getWeeklyType() {
        return weeklyType;
    }

    public void setWeeklyType(int weeklyType) {
        this.weeklyType = weeklyType;
    }
}
