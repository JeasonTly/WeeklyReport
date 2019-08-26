package com.aorise.weeklyreport.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class ProjectListBean extends BaseObservable implements Serializable {
    @Override
    public String toString() {
        return "ProjectListBean{" +
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
     * totalCount : 4
     * sort : false
     * list : [{"id":34,"name":"辰溪公安智能箱运维平台","leaderId":24,"intor":"<p>辰溪县2018年公共安全视频监控联网应用系统建设项目内容为，在现有公共安全治安防控系统基础上，新增50套人脸识别扩容子系统、200套治安监控扩容子系统、3套高清智能卡口监控扩容子系统、孝坪分控中心系统和校时服务系统等内容。<\/p>","state":2,"code":"AS.HH.2019.010.001","startDate":"2019-07-22 00:00:00","endDate":"2019-08-31 00:00:00","type":3,"level":3,"dept":"怀化研发中心","leaderName":"潘森林","countMeb":8},{"id":35,"name":"溆浦县道路交通安全智能监管中心建设项目","leaderId":36,"intor":"<p>\n溆浦重点车辆监管中心系统原来为奥昇长沙研发中心负责研发，后由于部分厂商未完成对接，原项目组相关同事离职，故将项目交接到怀化研发中心，由怀化研发中心负责项目后期与厂商的对接工作；目前有厂商对接以及项目验收工作，故申请立项。\n\n\n<br><\/p>","state":2,"code":"AS.HH.2019.006.001","startDate":"2019-04-22 00:00:00","endDate":"2019-06-15 00:00:00","type":3,"level":3,"dept":"怀化研发中心","leaderName":"阳根","countMeb":2},{"id":36,"name":"保安服务监管平台-综合服务网项目","leaderId":32,"intor":"<p><strong>&nbsp;系统能够实现保安服务公司、保安培训单位、保安员证三个行政许可的网上受理、网上审查、网上审核、网上审批功能。行政许可的办理时限监督、预警能有效控制许可流程的办理效率，加快办理流程。综上所述，保安服务监管平台项目建设的必要性。<\/strong><br><\/p>","state":2,"code":"AS.HH.2019.008.002","startDate":"2019-08-12 00:00:00","endDate":"2019-09-30 00:00:00","type":3,"level":3,"dept":"怀化研发中心","leaderName":"李沅","countMeb":1},{"id":37,"name":"小程序快速开发框架搭建工作","leaderId":34,"intor":"<p>小程序快速开发框架<\/p>","state":2,"code":"AS.HH.2019.RD.004","startDate":"2019-07-15 00:00:00","endDate":"2019-08-19 00:00:00","type":1,"level":3,"dept":"怀化研发中心","leaderName":"张力文","countMeb":3}]
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

    public static class ListBean implements Serializable{
        @Override
        public String toString() {
            return "ListBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", leaderId=" + leaderId +
                    ", intor='" + intor + '\'' +
                    ", state=" + state +
                    ", code='" + code + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", type=" + type +
                    ", level=" + level +
                    ", dept='" + dept + '\'' +
                    ", leaderName='" + leaderName + '\'' +
                    ", countMeb=" + countMeb +
                    '}';
        }

        /**
         * id : 34
         * name : 辰溪公安智能箱运维平台
         * leaderId : 24
         * intor : <p>辰溪县2018年公共安全视频监控联网应用系统建设项目内容为，在现有公共安全治安防控系统基础上，新增50套人脸识别扩容子系统、200套治安监控扩容子系统、3套高清智能卡口监控扩容子系统、孝坪分控中心系统和校时服务系统等内容。</p>
         * state : 2
         * code : AS.HH.2019.010.001
         * startDate : 2019-07-22 00:00:00
         * endDate : 2019-08-31 00:00:00
         * type : 3
         * level : 3
         * dept : 怀化研发中心
         * leaderName : 潘森林
         * countMeb : 8
         */

        private int id;
        private String name;
        private int leaderId;
        private String intor;
        private int state;
        private String code;
        private String startDate;
        private String endDate;
        private int type;
        private int level;
        private String dept;
        private String leaderName;
        private int countMeb;

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

        public int getLeaderId() {
            return leaderId;
        }

        public void setLeaderId(int leaderId) {
            this.leaderId = leaderId;
        }

        public String getIntor() {
            return intor;
        }

        public void setIntor(String intor) {
            this.intor = intor;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getLeaderName() {
            return leaderName;
        }

        public void setLeaderName(String leaderName) {
            this.leaderName = leaderName;
        }

        public int getCountMeb() {
            return countMeb;
        }

        public void setCountMeb(int countMeb) {
            this.countMeb = countMeb;
        }
    }
}
