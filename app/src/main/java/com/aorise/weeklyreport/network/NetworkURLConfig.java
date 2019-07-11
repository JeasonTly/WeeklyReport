package com.aorise.weeklyreport.network;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/26.
 */
public class NetworkURLConfig {
    //public static final String BASE_URL = " ";
    //public static final String BASE_URL = "http://192.168.1.201:8089/";//沈智维
    //public static final String BASE_URL = "http://192.168.1.144:8089/";//YYP
    public static final String BASE_URL = "http://account.aorisetest.com/";//YYP


    public static final String LOGIN_URL = "api/v1/login";
    public static final String LIST_MEMBER = "api/projectMember/pageIndex/{pageIndex}/pageNum/{pageNum}";//查询项目程序按列表
    public static final String LIST_PROJECT_BY_USERID = "api/project";//查询项目列表
    public static final String POST_WEEKLY_REPORT = "api/memberWeekly";//添加周报
    public static final String APPROVAL_WEEKLY_REPORT = "api/approval";//审核周报
    public static final String PROJECT_PLAN = "api/projectPlan/owner/{owner}";//当前项目下的计划列表
    public static final String WEEKLY_REPORT_QUERY = "api/memberWeekly";//根据项目、人员id、周数查询周报
    public static final String WEEKLY_REPORT_DETAIL = "api/memberWeekly/{id}";//根据项目、人员id、周数查询周报
    public static final String PERSONAL_INFO = "api/user/{id}";//根据项目、人员id、周数查询周报
    public static final String HEADER_WEEKY_REPORT_GET = "api/projectWeekly/{id}";//根据项目、人员id、周数查询周报
}
