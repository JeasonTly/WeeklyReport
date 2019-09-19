package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/18.
 */
public class ProjectWorkTimeBean extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "ProjectWorkTimeBean{" +
                "June=" + June +
                ", May=" + May +
                ", September=" + September +
                ", year=" + year +
                ", fullName='" + fullName + '\'' +
                ", March=" + March +
                ", userId=" + userId +
                ", April=" + April +
                ", August=" + August +
                ", October=" + October +
                ", December=" + December +
                ", post='" + post + '\'' +
                ", July=" + July +
                ", January=" + January +
                ", February=" + February +
                ", November=" + November +
                '}';
    }

    /**
     * June : 0
     * May : 0
     * September : 10
     * year : 2019
     * fullName : 张力文
     * March : 0
     * userId : 57
     * April : 0
     * August : 5
     * October : 0
     * December : 0
     * post : Android工程师
     * July : 0
     * January : 0
     * February : 0
     * November : 0
     */

    private float June;
    private float May;
    private float September;
    private int year;
    private String fullName;
    private float March;
    private int userId;
    private float April;
    private float August;
    private float October;
    private float December;
    private String post;
    private float July;
    private float January;
    private float February;
    private float November;

    public float getJune() {
        return June;
    }

    public void setJune(float June) {
        this.June = June;
    }

    public float getMay() {
        return May;
    }

    public void setMay(float May) {
        this.May = May;
    }

    public float getSeptember() {
        return September;
    }

    public void setSeptember(float September) {
        this.September = September;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public float getMarch() {
        return March;
    }

    public void setMarch(float March) {
        this.March = March;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getApril() {
        return April;
    }

    public void setApril(float April) {
        this.April = April;
    }

    public float getAugust() {
        return August;
    }

    public void setAugust(float August) {
        this.August = August;
    }

    public float getOctober() {
        return October;
    }

    public void setOctober(float October) {
        this.October = October;
    }

    public float getDecember() {
        return December;
    }

    public void setDecember(float December) {
        this.December = December;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public float getJuly() {
        return July;
    }

    public void setJuly(float July) {
        this.July = July;
    }

    public float getJanuary() {
        return January;
    }

    public void setJanuary(float January) {
        this.January = January;
    }

    public float getFebruary() {
        return February;
    }

    public void setFebruary(float February) {
        this.February = February;
    }

    public float getNovember() {
        return November;
    }

    public void setNovember(float November) {
        this.November = November;
    }
}
