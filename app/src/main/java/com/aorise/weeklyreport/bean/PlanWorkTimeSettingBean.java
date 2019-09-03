package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/9/3.
 */
public class PlanWorkTimeSettingBean extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "PlanWorkTimeSettingBean{" +
                "id=" + id +
                ", year=" + year +
                ", january=" + january +
                ", february=" + february +
                ", march=" + march +
                ", april=" + april +
                ", may=" + may +
                ", june=" + june +
                ", july=" + july +
                ", august=" + august +
                ", september=" + september +
                ", october=" + october +
                ", november=" + november +
                ", december=" + december +
                '}';
    }

    /**
     * id : 0
     * year : 2019
     * january : 0
     * february : 0
     * march : 0
     * april : 0
     * may : 0
     * june : 0
     * july : 0
     * august : 0
     * september : 0
     * october : 0
     * november : 0
     * december : 1
     */

    private float id;
    private float year;
    private float january;
    private float february;
    private float march;
    private float april;
    private float may;
    private float june;
    private float july;
    private float august;
    private float september;
    private float october;
    private float november;
    private float december;

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public float getYear() {
        return year;
    }

    public void setYear(float year) {
        this.year = year;
    }

    public float getJanuary() {
        return january;
    }

    public void setJanuary(float january) {
        this.january = january;
    }

    public float getFebruary() {
        return february;
    }

    public void setFebruary(float february) {
        this.february = february;
    }

    public float getMarch() {
        return march;
    }

    public void setMarch(float march) {
        this.march = march;
    }

    public float getApril() {
        return april;
    }

    public void setApril(float april) {
        this.april = april;
    }

    public float getMay() {
        return may;
    }

    public void setMay(float may) {
        this.may = may;
    }

    public float getJune() {
        return june;
    }

    public void setJune(float june) {
        this.june = june;
    }

    public float getJuly() {
        return july;
    }

    public void setJuly(float july) {
        this.july = july;
    }

    public float getAugust() {
        return august;
    }

    public void setAugust(float august) {
        this.august = august;
    }

    public float getSeptember() {
        return september;
    }

    public void setSeptember(float september) {
        this.september = september;
    }

    public float getOctober() {
        return october;
    }

    public void setOctober(float october) {
        this.october = october;
    }

    public float getNovember() {
        return november;
    }

    public void setNovember(float november) {
        this.november = november;
    }

    public float getDecember() {
        return december;
    }

    public void setDecember(float december) {
        this.december = december;
    }
}
