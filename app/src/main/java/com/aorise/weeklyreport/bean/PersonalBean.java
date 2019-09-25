package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/3.
 */
public class PersonalBean extends BaseObservable {


    /**
     * id : 74
     * uuid : QqhfeGkZSbK0lHr2
     * fullName : 阳根
     * dingUserId : 02045855361218662
     * mobile : 18569685495
     * roleName : 普通成员
     * roleId : 23
     * permissionModelList : [{"id":1,"name":"项目概况","parentId":0,"describes":"","href":"#/projects/survey"},{"id":6,"name":"工作周报","parentId":0,"describes":""},{"id":3,"name":"成员周报填写","parentId":6,"describes":"","href":"#/user/weekly"}]
     */

    private int id;
    private String uuid;
    private String fullName;
    private String dingUserId;
    private String mobile;
    private String roleName;
    private int roleId;
    private List<PermissionModelListBean> permissionModelList;

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

    public String getDingUserId() {
        return dingUserId;
    }

    public void setDingUserId(String dingUserId) {
        this.dingUserId = dingUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public List<PermissionModelListBean> getPermissionModelList() {
        return permissionModelList;
    }

    public void setPermissionModelList(List<PermissionModelListBean> permissionModelList) {
        this.permissionModelList = permissionModelList;
    }

    public static class PermissionModelListBean {
        /**
         * id : 1
         * name : 项目概况
         * parentId : 0
         * describes :
         * href : #/projects/survey
         */

        private int id;
        private String name;
        private int parentId;
        private String describes;
        private String href;

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

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getDescribes() {
            return describes;
        }

        public void setDescribes(String describes) {
            this.describes = describes;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
