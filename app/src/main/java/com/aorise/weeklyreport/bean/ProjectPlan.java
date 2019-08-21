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
                ", workTime=" + workTime +
                ", percentComplete=" + percentComplete +
                ", ownerList=" + ownerList +
                '}';
    }

    /**
     * id : 115
     * name : 需求和产品原型评审
     * workTime : 0
     * ownerList : []
     * percentComplete : 0
     */

    private int id;
    private String name;
    private float workTime;
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

    public float getWorkTime() {
        return workTime;
    }

    public void setWorkTime(float workTime) {
        this.workTime = workTime;
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
        private int id;
        private int planId;
        private int ownerId;

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
    }
}
