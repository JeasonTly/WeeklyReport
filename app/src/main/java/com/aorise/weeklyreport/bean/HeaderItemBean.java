package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/8.
 */
public class HeaderItemBean extends BaseObservable {
    @Override
    public String toString() {
        return "HeaderItemBean{" +
                "percentComplete=" + percentComplete +
                ", planDetailsList=" + planDetailsList +
                '}';
    }

    /**
     * percentComplete : 0
     * planDetailsList : [{"phase":"sdgsd","stage":0,"specificPhase":"4234","percentComplete":0,"person":"沅","isComplete":2},{"phase":"123123","stage":0,"percentComplete":0,"person":"沅","isComplete":2},{"phase":"测试需求","stage":0,"percentComplete":0,"person":"沅","isComplete":2}]
     */

    private int percentComplete;
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
                    "phase='" + phase + '\'' +
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

        private String phase;
        private int stage;
        private String specificPhase;
        private int percentComplete;
        private String person;
        private int isComplete;

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
