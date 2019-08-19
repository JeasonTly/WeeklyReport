package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/7/3.
 */
public class MemberListBean extends BaseObservable {

    @Override
    public String toString() {
        return "MemberListBean{" +
                "everyPage=" + everyPage +
                ", totalPage=" + totalPage +
                ", currentPage=" + currentPage +
                ", beginIndex=" + beginIndex +
                ", totalCount=" + totalCount +
                ", sort=" + sort +
                ", list=" + list +
                '}';
    }

    /**
     * everyPage : 10
     * totalPage : 1
     * currentPage : 1
     * beginIndex : 0
     * totalCount : 2
     * sort : false
     * list : [{"id":1,"projectId":3,"userId":5,"startTime":"2019-07-03 00:00:00","endTime":"2019-07-05 00:00:00","planWorkTime":5,"post":"add撒打算","useWorkTime":0,"userName":"SPL","weeklyState":2},{"id":3,"projectId":3,"userId":2,"startTime":"2019-07-03 09:59:54","endTime":"2019-07-28 09:59:57","planWorkTime":5,"post":"移动端开发","useWorkTime":1536,"userName":"涂立沅","weeklyState":3}]
     */

    private int everyPage;
    private int totalPage;
    private int currentPage;
    private int beginIndex;
    private int totalCount;
    private boolean sort;
    private List<ListBean> list;

    public int getEveryPage() {
        return everyPage;
    }

    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        @Override
        public String toString() {
            return "ListBean{" +
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
         * id : 1
         * projectId : 3
         * userId : 5
         * startTime : 2019-07-03 00:00:00
         * endTime : 2019-07-05 00:00:00
         * planWorkTime : 5
         * post : add撒打算
         * useWorkTime : 0
         * userName : SPL
         * weeklyState : 2
         */

        private int id;
        private int projectId;
        private int userId;
        private String startTime;
        private String endTime;
        private float planWorkTime;
        private String post;
        private float useWorkTime;
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

        public float getPlanWorkTime() {
            return planWorkTime;
        }

        public void setPlanWorkTime(float planWorkTime) {
            this.planWorkTime = planWorkTime;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public float getUseWorkTime() {
            return useWorkTime;
        }

        public void setUseWorkTime(float useWorkTime) {
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
}
