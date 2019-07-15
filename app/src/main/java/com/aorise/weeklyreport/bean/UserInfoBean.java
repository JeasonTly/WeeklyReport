package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/15.
 */
public class UserInfoBean extends BaseObservable {

    /**
     * id : 15
     * uuid : mltkIHgJCqpEA36j
     * fullName : 沈智维
     * roleName : 普通成员
     */

    private int id;
    private String uuid;
    private String fullName;
    private String roleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
