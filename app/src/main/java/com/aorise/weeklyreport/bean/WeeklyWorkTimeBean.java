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
                "year=" + year +
                ", month=" + month +
                ", weekOne=" + weekOne +
                ", weekTow=" + weekTow +
                ", weekThree=" + weekThree +
                ", weekFour=" + weekFour +
                ", weekFive=" + weekFive +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    /**
     * year : 0
     * month : 0
     * weekOne : 0
     * weekTow : 0
     * weekThree : 0
     * weekFour : 0
     * weekFive : 0
     * fullName : 潘森林
     */

    private float year;
    private float month;
    private float weekOne;
    private float weekTow;
    private float weekThree;
    private float weekFour;
    private float weekFive;
    private String fullName;

    public float getYear() {
        return year;
    }

    public void setYear(float year) {
        this.year = year;
    }

    public float getMonth() {
        return month;
    }

    public void setMonth(float month) {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
