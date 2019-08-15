package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/3.
 */
public class MemberListBean extends BaseObservable {
    @Override
    public String toString() {
        return "MemberListBean{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", planWorkTime=" + planWorkTime +
                ", post='" + post + '\'' +
                ", useWorkTime=" + useWorkTime +
                ", userName='" + userName + '\'' +
                ", weeklyState=" + weeklyState +
                '}';
    }

    /**
     * id : 2
     * projectId : 2
     * userId : 2
     * startTime : 2019-07-03 09:55:16
     * endTime : 2019-07-07 09:55:19
     * planWorkTime : 5
     * post : 多少电阿萨德阿萨德去
     * useWorkTime : 1154
     * userName : 涂立沅
     * weeklyState : 2
     */

    private int id;
    private int projectId;
    private int userId;
    private String startTime;
    private String endTime;
    private int planWorkTime;
    private String post;
    private int useWorkTime;
    private String userName;
    private int weeklyState;

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

    public int getUseWorkTime() {
        return useWorkTime;
    }

    public void setUseWorkTime(int useWorkTime) {
        this.useWorkTime = useWorkTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWeeklyState() {
        return weeklyState;
    }

    public void setWeeklyState(int weeklyState) {
        this.weeklyState = weeklyState;
    }
}
