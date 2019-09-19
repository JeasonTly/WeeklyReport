package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/19.
 */
public class ProjectReportWeeklyWorkTime extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "ProjectReportWeeklyWorkTime{" +
                "userId=" + userId +
                ", post='" + post + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", weekOne=" + weekOne +
                ", weekTow=" + weekTow +
                ", weekThree=" + weekThree +
                ", weekFour=" + weekFour +
                ", weekFive=" + weekFive +
                ", weekSix=" + weekSix +
                ", fullName='" + fullName + '\'' +
                ", projectId=" + projectId +
                '}';
    }

    /**
     * userId : 66
     * post : 项目负责人
     * year : 0
     * month : 0
     * weekOne : 0
     * weekTow : 0
     * weekThree : 0
     * weekFour : 0
     * weekFive : 0
     * weekSix : 0
     * fullName : 田宜鑫
     * projectId : 58
     */

    private int userId;
    private String post;
    private int year;
    private int month;
    private float weekOne;
    private float weekTow;
    private float weekThree;
    private float weekFour;
    private float weekFive;
    private float weekSix;
    private String fullName;
    private int projectId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getWeekOne() {
        return weekOne;
    }

    public void setWeekOne(float weekOne) {
        this.weekOne = weekOne;
    }

    public float getWeekTow() {
        return weekTow;
    }

    public void setWeekTow(float weekTow) {
        this.weekTow = weekTow;
    }

    public float getWeekThree() {
        return weekThree;
    }

    public void setWeekThree(float weekThree) {
        this.weekThree = weekThree;
    }

    public float getWeekFour() {
        return weekFour;
    }

    public void setWeekFour(float weekFour) {
        this.weekFour = weekFour;
    }

    public float getWeekFive() {
        return weekFive;
    }

    public void setWeekFive(float weekFive) {
        this.weekFive = weekFive;
    }

    public float getWeekSix() {
        return weekSix;
    }

    public void setWeekSix(float weekSix) {
        this.weekSix = weekSix;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
