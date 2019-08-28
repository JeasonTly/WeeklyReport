package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/20.
 */
public class MemberListSpinnerBean extends BaseObservable implements Serializable {

    @Override
    public String toString() {
        return "MemberListSpinnerBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }

    /**
     * id : 23
     * userName : 涂立沅
     */

    private int id;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
