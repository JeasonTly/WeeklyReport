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
                '}';
    }

    /**
     * id : 2
     * name : 无双剑姬
     */

    private int id;
    private String name;

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
}
