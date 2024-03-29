package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/4.
 */
public class WeeklyWorkTimeBean extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "WeeklyWorkTimeBean{" +
                "userId=" + userId +
                ", year=" + year +
                ", month=" + month +
                ", weekOne=" + weekOne +
                ", weekTow=" + weekTow +
                ", weekThree=" + weekThree +
                ", weekFour=" + weekFour +
                ", weekFive=" + weekFive +
                ", weekSix=" + weekSix +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    /**
     * userId : 54
     * year : 2019
     * month : 9
     * weekOne : 5
     * weekTow : 4
     * weekThree : 0
     * weekFour : 0
     * weekFive : 0
     * weekSix : 0
     * fullName : 潘森林
     */

    private int userId;
    private int year;
    private int month;
    private float weekOne;
    private float weekTow;
    private float weekThree;
    private float weekFour;
    private float weekFive;
    private float weekSix;
    private String fullName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
}
