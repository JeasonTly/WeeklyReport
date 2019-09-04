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
                ", property=" + property +
                '}';
    }

    /**
     * id : 42
     * name : 工作信息管理系统
     * property : 1
     */

    private int id;
    private String name;
    private int property;

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

    public int getProperty() {
        return property;
    }

    public void setProperty(int property) {
        this.property = property;
    }
}
