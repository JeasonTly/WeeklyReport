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
                ", planDetailsList=" + planDetailsList +
                '}';
    }

    private int id;
    private Integer projectId;
    private Integer byWeek;
    private Integer type;
    private String startDate;
    private String endDate;
    private String overallSituation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getByWeek() {
        return byWeek;
    }

    public void setByWeek(Integer byWeek) {
        this.byWeek = byWeek;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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

    public void setPercentComplete(Integer percentComplete) {
        this.percentComplete = percentComplete;
    }

    private Integer percentComplete;

    private List<PlanDetailsListBean> planDetailsList;

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
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
        private int stage;
        private String specificPhase;
        private int percentComplete;
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

        public int getStage() {
            return stage;
        }

        public void setStage(int stage) {
            this.stage = stage;
        }

        public String getSpecificPhase() {
            return specificPhase;
        }

        public void setSpecificPhase(String specificPhase) {
            this.specificPhase = specificPhase;
        }

        public int getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(int percentComplete) {
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
