package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/3.
 */
public class MemberListBean extends BaseObservable {

    /**
     * id : 3
     * projectId : 3
     * userId : 2
     * startTime : 2019-07-03 09:59:54
     * endTime : 2019-07-28 09:59:57
     * planWorkTime : 5
     * post : 是爱迪生ad阿萨德
     * userName : 涂立沅
     */

    private int id;
    private int projectId;
    private int userId;
    private String startTime;
    private String endTime;
    private int planWorkTime;
    private String post;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPlanWorkTime() {
        return planWorkTime;
    }

    public void setPlanWorkTime(int planWorkTime) {
        this.planWorkTime = planWorkTime;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
