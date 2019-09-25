package com.aorise.weeklyreport.bean;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/3.
 */
public class ProjectList implements Serializable {
    @Override
    public String toString() {
        return "ProjectList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", endDate='" + endDate + '\'' +
                ", property=" + property +
                ", weeklyState=" + weeklyState +
                ", percentComplete=" + percentComplete +
                ", percentPlan=" + percentPlan +
                '}';
    }

    /**
     * id : 37
     * name : 小程序快速开发框架搭建工作
     * endDate : 2019-08-19 00:00:00
     * property : 1
     * weeklyState : 2
     * percentComplete : 0
     * percentPlan : 0
     */

    private int id;
    private String name;
    private String endDate;
    private int property;
    private int weeklyState;
    private double percentComplete;
    private double percentPlan;

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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public int getWeeklyState() {
        return weeklyState;
    }

    public void setWeeklyState(int weeklyState) {
        this.weeklyState = weeklyState;
    }

    public double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public double getPercentPlan() {
        return percentPlan;
    }

    public void setPercentPlan(double percentPlan) {
        this.percentPlan = percentPlan;
    }
}
