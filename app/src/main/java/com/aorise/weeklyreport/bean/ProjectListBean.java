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
     * totalPage : 3
     * currentPage : 1
     * beginIndex : 0
     * totalCount : 28
     * sort : false
     * list : [{"id":34,"name":"辰溪公安智能箱运维平台","leaderId":54,
     * "intor":"<p>辰溪县2018年公共安全视频监控联网应用系统建设项目内容为，在现有公共安全治安防控系统基础上，新增50套人脸识别扩容子系统、200套治安监控扩容子系统、3套高清智能卡口监控扩容子系统、孝坪分控中心系统和校时服务系统等内容。<\/p>","state":2,"code":"AS.HH.2019.010.001","startDate":"2019-07-22 00:00:00","endDate":"2019-08-31 00:00:00","type":3,"level":3,"dept":"怀化研发中心","property":1,"leaderName":"潘森林","countMeb":12,"weeklyState":3},{"id":37,"name":"小程序快速开发框架搭建工作","leaderId":57,"intor":"<p>所属部门：研发部-移动端<\/p><p>项目类型：技术预研<\/p><p>项目介绍：<\/p><p>该项目是基于微信小程序的API开发的一套小程序快速开发框架，主要开发的内容包括<\/p><p>1.研究小程序自带的云开发等功能，对云开发功能进行封装，今后项目如果涉及到云开发功能可直接使用，只需要去写对应的JS代码即可<\/p><p>2.对常用的UI组件进行封装，形成一个UI库，今后的项目如果需要此组件可直接使用<\/p>","state":2,"code":"AS.HH.2019.RD.004","startDate":"2019-07-15 00:00:00","endDate":"2019-08-19 00:00:00","type":1,"level":3,"dept":"怀化研发中心","property":1,"leaderName":"张力文","countMeb":3,"weeklyState":2},{"id":42,"name":"工作信息管理系统","leaderId":103,"intor":"<p><strong>目前公司员工工作汇报制度冗长，汇报工作不能做到客观真实，工作内容联系不准确，工作汇报内容审批不严格，工作工时与工作计划监督不到位，工作概况了解不直观等问题。所以用信息化手段，共同监督，真实汇报，逐级审批的方式来管理工作信息。<\/strong><\/p>","state":2,"code":"AS.HH.2019.RD.005","startDate":"2019-07-01 00:00:00","endDate":"2019-12-31 00:00:00","type":1,"level":1,"dept":"怀化研发中心","property":1,"leaderName":"李沅","countMeb":7,"weeklyState":2},{"id":35,"name":"溆浦县道路交通安全智能监管中心建设项目","leaderId":74,"intor":"<p>\r\n溆浦重点车辆监管中心系统原来为奥昇长沙研发中心负责研发，后由于部分厂商未完成对接，原项目组相关同事离职，故将项目交接到怀化研发中心，由怀化研发中心负责项目后期与厂商的对接工作；目前有厂商对接以及项目验收工作，故申请立项。\r\n\r\n\r\n<br><\/p>","state":2,"code":"AS.HH.2019.006.001","startDate":"2019-04-22 00:00:00","endDate":"2019-06-15 00:00:00","type":3,"level":3,"dept":"怀化研发中心","property":1,"leaderName":"阳根","countMeb":8,"weeklyState":2},{"id":46,"name":"溆浦智慧医疗项目一期","leaderId":77,"intor":"<p>溆浦智慧医疗项目一期项目开发工作已经完成，需要对溆浦县三家公立医院的HIS医疗系统进行数据对接，已完成HIS系统与智慧医疗系统平台的数据共享、公共服务、预约挂号等公共服务功能<\/p>","state":1,"code":"AS.HH.2019.004.001","startDate":"2019-04-01 00:00:00","endDate":"2019-12-31 00:00:00","type":3,"level":1,"dept":"怀化研发中心","property":1,"leaderName":"赵力","countMeb":9,"weeklyState":2},{"id":59,"name":"智慧政工项目","leaderId":84,"intor":"<p>智慧政工建设目标是运用互联网思维和新媒体意识，可以实现民警工作轨迹、工作状态等\u201c大数据\u201d一目了然，为提高公安队伍管理信息化、智能化水平实现队伍正规化建设从被动到主动的变革；可以增强公安队伍管理的开放性、实用性以及易操作性，做到对队伍状况的实时分析和掌控，提高管理的精细化、规范化的水平。<\/p><p>智慧政工系统源于\u201c互联网+\u201d的理念，旨在建设一个全警参与、全警监督、全警受益的平台<\/p>","state":1,"code":"AS.HH.2019.011.001","startDate":"2019-08-30 00:00:00","endDate":"2019-11-06 00:00:00","type":3,"level":3,"dept":"怀化研发中心","property":1,"leaderName":"陈逸飞","countMeb":6,"weeklyState":2},{"id":71,"name":"阿所发生的发顺丰1","leaderId":55,"intor":"<p>按时发斯蒂芬第三方11<\/p>","state":1,"dept":"水电费水电费1","property":2,"leaderName":"向开宇","countMeb":2,"weeklyState":2},{"id":47,"name":"智慧溆浦二期-溆浦社会综治网格化管理系统一期","leaderId":92,"intor":"<p>本项目是外包项目，由深圳互联精英公司负责开发。<\/p><p>合同建设金额384.23万元，开工时间为2017年3月1日，项目于2019年1月14完成项目变更，2019年6月13日完成初验，现在正在试运行阶段。<\/p><p><br><\/p>","state":1,"code":"AS.2017.002.002","startDate":"2019-07-25 00:00:00","endDate":"2019-12-31 00:00:00","type":3,"level":1,"dept":"怀化研发中心","property":1,"leaderName":"申嘉玮","countMeb":3,"weeklyState":3},{"id":57,"name":"项目服务部运维组部门工作","leaderId":88,"intor":"<p>负责运维部所有工作的组织、管理、协调和决策<\/p>","state":1,"code":"AS.HH.PI.002","startDate":"2019-08-26 00:00:00","endDate":"2019-12-31 00:00:00","type":1,"level":3,"dept":"怀化研发中心","property":2,"leaderName":"张先文","countMeb":5,"weeklyState":2},{"id":48,"name":"智慧溆浦二期-溆浦县数字城管系统","leaderId":87,"intor":"<p>外包项目<\/p>","state":1,"code":"AS.2017.002.003","startDate":"2019-08-26 00:00:00","endDate":"2019-12-31 00:00:00","type":3,"level":1,"dept":"怀化研发中心","property":1,"leaderName":"向超","countMeb":3,"weeklyState":2}]
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
                    ", property=" + property +
                    ", leaderName='" + leaderName + '\'' +
                    ", countMeb=" + countMeb +
                    ", weeklyState=" + weeklyState +
                    '}';
        }

        /**
         * id : 34
         * name : 辰溪公安智能箱运维平台
         * leaderId : 54
         * intor : <p>辰溪县2018年公共安全视频监控联网应用系统建设项目内容为，在现有公共安全治安防控系统基础上，新增50套人脸识别扩容子系统、200套治安监控扩容子系统、3套高清智能卡口监控扩容子系统、孝坪分控中心系统和校时服务系统等内容。</p>
         * state : 2
         * code : AS.HH.2019.010.001
         * startDate : 2019-07-22 00:00:00
         * endDate : 2019-08-31 00:00:00
         * type : 3
         * level : 3
         * dept : 怀化研发中心
         * property : 1
         * leaderName : 潘森林
         * countMeb : 12
         * weeklyState : 1 通过 2 未填写，3 未审核
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
        private int property;
        private String leaderName;
        private int countMeb;
        private int weeklyState;

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

        public int getProperty() {
            return property;
        }

        public void setProperty(int property) {
            this.property = property;
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

        public int getWeeklyState() {
            return weeklyState;
        }

        public void setWeeklyState(int weeklyState) {
            this.weeklyState = weeklyState;
        }
    }
}
