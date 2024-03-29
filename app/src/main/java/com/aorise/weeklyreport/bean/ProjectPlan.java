package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/2.
 */
public class ProjectPlan extends BaseObservable {

    @Override
    public String toString() {
        return "ProjectPlan{" +
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

    /**
     * id : 12
     * name : 开发
     * parentId : 0
     * startDate : 2019-07-22 00:00:00
     * endDate : 2019-08-16 00:00:00
     * workTime : 26
     * projectId : 34
     * planChild : [{"id":20,"name":"bug修改","parentId":12,"startDate":"2019-08-01 00:00:00","endDate":"2019-08-30 00:00:00","workTime":30,"projectId":34,"planChild":[],"ownerList":[{"id":208,"planId":20,"ownerId":35,"fullName":"李佳鑫","percentComplete":0},{"id":205,"planId":20,"ownerId":46,"fullName":"唐丽","percentComplete":0},{"id":204,"planId":20,"ownerId":39,"fullName":"杨英浦","percentComplete":0},{"id":207,"planId":20,"ownerId":24,"fullName":"潘森林","percentComplete":0},{"id":206,"planId":20,"ownerId":47,"fullName":"周浩","percentComplete":0},{"id":209,"planId":20,"ownerId":42,"fullName":"叶海宾","percentComplete":0}],"useWorkTime":10,"percentComplete":33.333332}]
     * ownerList : [{"id":247,"planId":12,"ownerId":49,"fullName":"肖映秋","percentComplete":0}]
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

    public static class PlanChildBean {
        /**
         * id : 20
         * name : bug修改
         * parentId : 12
         * startDate : 2019-08-01 00:00:00
         * endDate : 2019-08-30 00:00:00
         * workTime : 30
         * projectId : 34
         * planChild : []
         * ownerList : [{"id":208,"planId":20,"ownerId":35,"fullName":"李佳鑫","percentComplete":0},{"id":205,"planId":20,"ownerId":46,"fullName":"唐丽","percentComplete":0},{"id":204,"planId":20,"ownerId":39,"fullName":"杨英浦","percentComplete":0},{"id":207,"planId":20,"ownerId":24,"fullName":"潘森林","percentComplete":0},{"id":206,"planId":20,"ownerId":47,"fullName":"周浩","percentComplete":0},{"id":209,"planId":20,"ownerId":42,"fullName":"叶海宾","percentComplete":0}]
         * useWorkTime : 10
         * percentComplete : 33.333332
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
         * id : 247
         * planId : 12
         * ownerId : 49
         * fullName : 肖映秋
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

        public void setPercentComplete(double percentComplete) {
            this.percentComplete = percentComplete;
        }
    }
}
