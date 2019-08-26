package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class FillProjectPlan extends BaseObservable {

    @Override
    public String toString() {
        return "FillProjectPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workTime=" + workTime +
                ", useWorkTime=" + useWorkTime +
                ", percentComplete=" + percentComplete +
                ", ownerList=" + ownerList +
                '}';
    }

    /**
     * id : 20
     * name : bug修改
     * workTime : 0
     * ownerList : [{"id":208,"planId":20,"ownerId":35,"fullName":"李佳鑫","percentComplete":0},{"id":205,"planId":20,"ownerId":46,"fullName":"唐丽","percentComplete":0},{"id":204,"planId":20,"ownerId":39,"fullName":"杨英浦","percentComplete":0},{"id":207,"planId":20,"ownerId":24,"fullName":"潘森林","percentComplete":0},{"id":206,"planId":20,"ownerId":47,"fullName":"周浩","percentComplete":0},{"id":209,"planId":20,"ownerId":42,"fullName":"叶海宾","percentComplete":0}]
     * useWorkTime : 0
     * percentComplete : 0
     */

    private int id;
    private String name;
    private int workTime;
    private int useWorkTime;
    private int percentComplete;
    private List<OwnerListBean> ownerList;

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

    public int getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public List<OwnerListBean> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<OwnerListBean> ownerList) {
        this.ownerList = ownerList;
    }

    public static class OwnerListBean {
        /**
         * id : 208
         * planId : 20
         * ownerId : 35
         * fullName : 李佳鑫
         * percentComplete : 0
         */

        private int id;
        private int planId;
        private int ownerId;
        private String fullName;
        private int percentComplete;

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

        public int getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(int percentComplete) {
            this.percentComplete = percentComplete;
        }
    }
}
