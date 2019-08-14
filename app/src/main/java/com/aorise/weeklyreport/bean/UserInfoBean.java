package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/15.
 */
public class UserInfoBean extends BaseObservable {


    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleId=" + roleId +
                ", permissionModelList=" + permissionModelList +
                '}';
    }

    /**
     * id : 15
     * uuid : mltkIHgJCqpEA36j
     * fullName : 沈智维
     * roleName : 普通成员
     * roleId : 23
     * permissionModelList : [{"id":1,"name":"项目概况","parentId":0,"describes":"","href":"#/projects/survey"},{"id":3,"name":"周报填写","parentId":0,"describes":"","href":"#/user/weekly"},{"id":4,"name":"项目管理","parentId":0,"describes":""},{"id":5,"name":"项目周报","parentId":4,"href":"#/projects/weekly"},{"id":6,"name":"项目管理","parentId":4,"href":"#/projects/manage"},{"id":7,"name":"绩效管理","parentId":0},{"id":8,"name":"绩效查看","parentId":7},{"id":9,"name":"绩效审核","parentId":7}]
     */

    private int id;
    private String uuid;
    private String fullName;
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
        @Override
        public String toString() {
            return "PermissionModelListBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", parentId=" + parentId +
                    ", describes='" + describes + '\'' +
                    ", href='" + href + '\'' +
                    '}';
        }

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
