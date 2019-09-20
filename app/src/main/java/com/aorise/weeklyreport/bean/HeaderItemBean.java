package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/8.
 */
public class HeaderItemBean implements Serializable {
    @Override
    public String toString() {
        return "HeaderItemBean{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", byWeek=" + byWeek +
                ", type=" + type +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", overallSituation='" + overallSituation + '\'' +
                ", percentComplete=" + percentComplete +
                ", approvalState=" + approvalState +
                ", planDetailsList=" + planDetailsList +
                '}';
    }

    private int id;
    private int projectId;
    private int byWeek;
    private int type;
    private String startDate;
    private String endDate;
    private String overallSituation;
    private double percentComplete;
    private int approvalState; //1.未审核，2。已审核，3 已驳回
    private List<PlanDetailsListBean> planDetailsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getByWeek() {
        return byWeek;
    }

    public void setByWeek(int byWeek) {
        this.byWeek = byWeek;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getOverallSituation() {
        return overallSituation;
    }

    public void setOverallSituation(String overallSituation) {
        this.overallSituation = overallSituation;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(int approvalState) {
        this.approvalState = approvalState;
    }

    public List<PlanDetailsListBean> getPlanDetailsList() {
        return planDetailsList;
    }

    public void setPlanDetailsList(List<PlanDetailsListBean> planDetailsList) {
        this.planDetailsList = planDetailsList;
    }

    public static class PlanDetailsListBean implements Serializable {
        @Override
        public String toString() {
            return "PlanDetailsListBean{" +
                    "memberId=" + memberId +
                    ", phase='" + phase + '\'' +
                    ", stage=" + stage +
                    ", specificPhase='" + specificPhase + '\'' +
                    ", specificItem='" + specificItem + '\'' +
                    ", percentComplete=" + percentComplete +
                    ", person='" + person + '\'' +
                    ", isComplete=" + isComplete +
                    '}';
        }

        /**
         * phase : sdgsd
         * stage : 0
         * specificPhase : 4234
         * percentComplete : 0
         * person : 沅
         * isComplete : 2
         */
        private int memberId;
        private String phase;
        private String stage;
        private String specificPhase;
        private String specificItem;
        private float percentComplete;
        private String person;
        private int isComplete;

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getSpecificPhase() {
            return specificPhase;
        }

        public String getSpecificItem() {
            return specificItem;
        }

        public void setSpecificItem(String specificItem) {
            this.specificItem = specificItem;
        }

        public void setSpecificPhase(String specificPhase) {
            this.specificPhase = specificPhase;
        }

        public float getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(float percentComplete) {
            this.percentComplete = percentComplete;
        }

        public String getPerson() {
            return person;
        }

        public void setPerson(String person) {
            this.person = person;
        }

        public int getIsComplete() {
            return isComplete;
        }

        public void setIsComplete(int isComplete) {
            this.isComplete = isComplete;
        }
    }
}
