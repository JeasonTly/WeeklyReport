package com.aorise.weeklyreport.network;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/26.
 */
public class NetworkURLConfig {
    //public static final String BASE_URL = " ";
     //public static final String BASE_URL = "http://192.168.1.201:8089/";//沈智维
    public static final String BASE_URL = "http://192.168.1.106:8089/";//卿燚
    //public static final String BASE_URL = "http://week.console.umxwe.com/api/";

    public static final String LOGIN_URL = "api/user/login";
    public static final String LOGIN_URL_N = "/api/v1/login";
    public static final String LIST_MEMBER = "api/projectMember/pageIndex/{pageIndex}/pageNum/{pageNum}";//查询项目成员列表
    public static final String LIST_MEMBER_SPINNER = "api/projectMember/projectId/{projectId}";//根据项目id查询项目成员列表
    public static final String LIST_PROJECT_BY_USERID = "api/project";//查询项目列表
    public static final String LIST_PROJECT = "api/project/pageIndex/{pageIndex}/pageNum/{pageNum}";//查询项目列表
    public static final String PROJECT_BASE_INFO = "api/project/{id}";//项目基本信息
    public static final String POST_WEEKLY_REPORT = "api/memberWeekly";//添加周报
    public static final String PUT_WEEKLY_REPORT = "api/memberWeekly";//修改周报
    public static final String DELETE_WEEKLY_REPORT = "api/memberWeekly";//删除周报
    public static final String APPROVAL_WEEKLY_REPORT = "api/approval";//审核周报
    public static final String PROJECT_PLAN = "api/projectPlan/owner/{owner}";//当前项目下的计划列表
    public static final String PROJECT_PLAN_BYPROJECTID = "api/projectPlan";//当前项目下的计划列表
    public static final String WEEKLY_REPORT_QUERY = "api/memberWeekly";//根据项目、人员id、周数查询周报
    public static final String WEEKLY_REPORT_DETAIL = "api/memberWeekly/byParam";//周报详情
    public static final String PERSONAL_INFO = "api/user/{id}";//根据项目、人员id、周数查询周报
    public static final String HEADER_WEEKY_REPORT_GET = "api/projectWeekly/{id}";//根据项目、人员id、周数查询周报
    public static final String HEADER_WEEKY_REPORT_POST = "api/projectWeekly";//根据项目、人员id、周数查询周报
    public static final String PROJECT_STATISTIC = "api/statistics";//根据项目id查询统计信息
    public static final String WORKTIME_TOTALYEAR_STATISTIC = "api/userWorkTime";//根据年份获取当前工时
    public static final String WORKTIME_TOTALMONTH_STATISTIC = "api/userWorkTime/year/{year}";//根据年 月获取当前工时
    public static final String DEFAULT_WORKTIME= "api/workTime";//根据年份获取当前工时
    public static final String DEFAULT_PROJECT_WORKTIME= "api/userWorkTimeProjectAPP";//根据年份和项目ID获取工时
    public static final String DEFAULT_PROJECT_WEEKLY_WORKTIME= "api/userWorkTimeProject/year/{year}";//根据年份 月份 和项目ID获取工时

}
