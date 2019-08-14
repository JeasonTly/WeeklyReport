package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

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
                ", percentComplete=" + percentComplete +
                '}';
    }

    /**
     * id : 24
     * name : 需求分析
     * percentComplete : 0
     */

    private int id;
    private String name;
    private int percentComplete;

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

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }


//
//    /**
//     * id : 1
//     * name : 弗雷尔卓德的使命
//     * parentId : 0
//     * startDate : 2019-07-01 09:43:16
//     * endDate : 2019-07-06 09:43:19
//     * owner : 2
//     * planChild : [{"id":5,"name":"123123","parentId":1,"startDate":"2018-08-08 00:00:00","endDate":"2018-08-11 00:00:00","owner":1,"planChild":[]}]
//     */
//
//    private int id;
//    private String name;
//    private int parentId;
//    private String startDate;
//    private String endDate;
//    private int owner;
//    private List<ProjectPlan> planChild;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(int parentId) {
//        this.parentId = parentId;
//    }
//
//    public String getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(String startDate) {
//        this.startDate = startDate;
//    }
//
//    public String getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }
//
//    public int getOwner() {
//        return owner;
//    }
//
//    public void setOwner(int owner) {
//        this.owner = owner;
//    }
//
//    public List<ProjectPlan> getPlanChild() {
//        return planChild;
//    }
//
//    public void setPlanChild(List<ProjectPlan> planChild) {
//        this.planChild = planChild;
//    }
//
//    public static class PlanChildBean {
//        /**
//         * id : 5
//         * name : 123123
//         * parentId : 1
//         * startDate : 2018-08-08 00:00:00
//         * endDate : 2018-08-11 00:00:00
//         * owner : 1
//         * planChild : []
//         */
//
//        private int id;
//        private String name;
//        private int parentId;
//        private String startDate;
//        private String endDate;
//        private int owner;
//        private List<?> planChild;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public int getParentId() {
//            return parentId;
//        }
//
//        public void setParentId(int parentId) {
//            this.parentId = parentId;
//        }
//
//        public String getStartDate() {
//            return startDate;
//        }
//
//        public void setStartDate(String startDate) {
//            this.startDate = startDate;
//        }
//
//        public String getEndDate() {
//            return endDate;
//        }
//
//        public void setEndDate(String endDate) {
//            this.endDate = endDate;
//        }
//
//        public int getOwner() {
//            return owner;
//        }
//
//        public void setOwner(int owner) {
//            this.owner = owner;
//        }
//
//        public List<?> getPlanChild() {
//            return planChild;
//        }
//
//        public void setPlanChild(List<?> planChild) {
//            this.planChild = planChild;
//        }
//    }
}
