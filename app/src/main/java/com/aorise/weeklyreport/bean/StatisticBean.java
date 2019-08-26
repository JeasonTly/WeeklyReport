package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class StatisticBean extends BaseObservable {

    @Override
    public String toString() {
        return "StatisticBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", workTime=" + workTime +
                ", useWorkTime=" + useWorkTime +
                ", percentComplete=" + percentComplete +
                ", planChild=" + planChild +
                ", ownerList=" + ownerList +
                '}';
    }

    /**
     * id : 15
     * name : 测试
     * startDate : 2019-08-01 00:00:00
     * endDate : 2019-08-30 00:00:00
     * workTime : 0
     * planChild : [{"id":17,"name":"第一轮测试","parentId":15,"startDate":"2019-08-01 00:00:00","endDate":"2019-08-16 00:00:00","workTime":16,"projectId":34,"planChild":[],"ownerList":[{"id":215,"planId":17,"ownerId":45,"fullName":"叶婷","percentComplete":0},{"id":214,"planId":17,"ownerId":49,"fullName":"肖映秋","percentComplete":0}],"useWorkTime":0,"percentComplete":0},{"id":18,"name":"第二轮测试","parentId":15,"startDate":"2019-08-19 00:00:00","endDate":"2019-08-23 00:00:00","workTime":5,"projectId":34,"planChild":[],"ownerList":[{"id":217,"planId":18,"ownerId":45,"fullName":"叶婷","percentComplete":0},{"id":216,"planId":18,"ownerId":49,"fullName":"肖映秋","percentComplete":0}],"useWorkTime":0,"percentComplete":0},{"id":19,"name":"第三轮测试","parentId":15,"startDate":"2019-08-26 00:00:00","endDate":"2019-08-30 00:00:00","workTime":5,"projectId":34,"planChild":[],"ownerList":[{"id":219,"planId":19,"ownerId":45,"fullName":"叶婷","percentComplete":0},{"id":218,"planId":19,"ownerId":49,"fullName":"肖映秋","percentComplete":0}],"useWorkTime":0,"percentComplete":0}]
     * ownerList : [{"id":229,"planId":15,"ownerId":45,"fullName":"叶婷","percentComplete":0}]
     * useWorkTime : 0
     * percentComplete : 0
     */

    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private int workTime;
    private int useWorkTime;
    private double percentComplete;
    private List<PlanChildBean> planChild;
    private List<OwnerListBeanX> ownerList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getUseWorkTime() {
        return useWorkTime;
    }

    public void setUseWorkTime(int useWorkTime) {
        this.useWorkTime = useWorkTime;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public List<PlanChildBean> getPlanChild() {
        return planChild;
    }

    public void setPlanChild(List<PlanChildBean> planChild) {
        this.planChild = planChild;
    }

    public List<OwnerListBeanX> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<OwnerListBeanX> ownerList) {
        this.ownerList = ownerList;
    }

    public static class PlanChildBean {
        /**
         * id : 17
         * name : 第一轮测试
         * parentId : 15
         * startDate : 2019-08-01 00:00:00
         * endDate : 2019-08-16 00:00:00
         * workTime : 16
         * projectId : 34
         * planChild : []
         * ownerList : [{"id":215,"planId":17,"ownerId":45,"fullName":"叶婷","percentComplete":0},{"id":214,"planId":17,"ownerId":49,"fullName":"肖映秋","percentComplete":0}]
         * useWorkTime : 0
         * percentComplete : 0
         */

        private int id;
        private String name;
        private int parentId;
        private String startDate;
        private String endDate;
        private int workTime;
        private int projectId;
        private int useWorkTime;
        private double percentComplete;
        private List<PlanChildBean> planChild;
        private List<OwnerListBeanX> ownerList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
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

        public int getWorkTime() {
            return workTime;
        }

        public void setWorkTime(int workTime) {
            this.workTime = workTime;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getUseWorkTime() {
            return useWorkTime;
        }

        public void setUseWorkTime(int useWorkTime) {
            this.useWorkTime = useWorkTime;
        }

        public double getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(double percentComplete) {
            this.percentComplete = percentComplete;
        }

        public List<PlanChildBean> getPlanChild() {
            return planChild;
        }

        public void setPlanChild(List<PlanChildBean> planChild) {
            this.planChild = planChild;
        }

        public List<OwnerListBeanX> getOwnerList() {
            return ownerList;
        }

        public void setOwnerList(List<OwnerListBeanX> ownerList) {
            this.ownerList = ownerList;
        }

        @Override
        public String toString() {
            return "PlanChildBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", parentId=" + parentId +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", workTime=" + workTime +
                    ", projectId=" + projectId +
                    ", useWorkTime=" + useWorkTime +
                    ", percentComplete=" + percentComplete +
                    ", planChild=" + planChild +
                    ", ownerList=" + ownerList +
                    '}';
        }
    }

    public static class OwnerListBeanX {
        @Override
        public String toString() {
            return "OwnerListBeanX{" +
                    "id=" + id +
                    ", planId=" + planId +
                    ", ownerId=" + ownerId +
                    ", fullName='" + fullName + '\'' +
                    ", percentComplete=" + percentComplete +
                    '}';
        }

        /**
         * id : 229
         * planId : 15
         * ownerId : 45
         * fullName : 叶婷
         * percentComplete : 0
         */

        private int id;
        private int planId;
        private int ownerId;
        private String fullName;
        private double percentComplete;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPlanId() {
            return planId;
        }

        public void setPlanId(int planId) {
            this.planId = planId;
        }

        public int getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(int ownerId) {
            this.ownerId = ownerId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public double getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(int percentComplete) {
            this.percentComplete = percentComplete;
        }
    }
}
