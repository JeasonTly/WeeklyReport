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
                ", approvalModelList=" + approvalModelList +
                ", weeklyDateModels=" + weeklyDateModels +
                '}';
    }

    /**
     * id : 175
     * byWeek : 34
     * projectId : 28
     * type : 2
     * workType : 1
     * planId : 98
     * startDate : 2019-08-21 00:00:00
     * endDate : 2019-08-23 00:00:00
     * percentComplete : 100
     * workTime : 3
     * output : 《测试大纲》、《测试计划》
     * explain : 完成测试大纲及测试计划的编写，预计完成100%
     * issue :
     * state : 0
     * approvalState : 3
     * userId : 22
     * projectName : 保安服务监管平台-综合服务网项目
     * planName : 测试用例编写
     * approvalModelList : []
     * weeklyDateModels : [{"weeklyId":175,"workDate":"2019-08-21"},{"weeklyId":175,"workDate":"2019-08-22"},{"weeklyId":175,"workDate":"2019-08-23"}]
     */

    private int id;
    private int byWeek;
    private int projectId;
    private int type;
    private int workType;
    private int planId;
    private String startDate;
    private String endDate;
    private int percentComplete;
    private int workTime;
    private String output;
    private String explain;
    private String issue;
    private int state;
    private int approvalState;
    private int userId;
    private String projectName;
    private String planName;
    private List<ApprovalModelBean> approvalModelList;
    private List<WeeklyDateModelsBean> weeklyDateModels;

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

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
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

    public List<ApprovalModelBean> getApprovalModelList() {
        return approvalModelList;
    }

    public void setApprovalModelList(List<ApprovalModelBean> approvalModelList) {
        this.approvalModelList = approvalModelList;
    }

    public List<WeeklyDateModelsBean> getWeeklyDateModels() {
        return weeklyDateModels;
    }

    public void setWeeklyDateModels(List<WeeklyDateModelsBean> weeklyDateModels) {
        this.weeklyDateModels = weeklyDateModels;
    }

    public static class ApprovalModelBean implements Serializable  {
        @Override
        public String toString() {
            return "ApprovalModelBean{" +
                    "id=" + id +
                    ", weeklyId=" + weeklyId +
                    ", remark='" + remark + '\'' +
                    ", approvalTime='" + approvalTime + '\'' +
                    ", statue=" + statue +
                    ", planStatus=" + planStatus +
                    ", approvalState=" + approvalState +
                    '}';
        }

        private int id;
        private int weeklyId;//周报ID
        private String remark;//审核理由
        private String approvalTime;
        //审核状态1-已通过，2-已驳回
        private int statue;
        //计划或总结状态 1-完成，2-正常，3-滞后，4-终止"
        private int planStatus;
        //审核状态1-已通过，2-已驳回
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

        public int getStatue() {
            return statue;
        }

        public void setStatue(int statue) {
            this.statue = statue;
        }

        public int getPlanStatus() {
            return planStatus;
        }

        public void setPlanStatus(int planStatus) {
            this.planStatus = planStatus;
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
                    '}';
        }

        /**
         * weeklyId : 175
         * workDate : 2019-08-21
         */

        private int weeklyId;
        private String workDate;

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
    }
}
