package com.aorise.weeklyreport.network;

import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.bean.PersonalBean;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectPlan;
import com.aorise.weeklyreport.bean.WeeklyReportBean;
import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/26.
 */
public interface ApiService {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(NetworkURLConfig.LOGIN_URL)
    Observable<Result<String>> login(@Body RequestBody responseBody);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(NetworkURLConfig.POST_WEEKLY_REPORT)
    Observable<Result<Integer>> fillInWeeklyReprot(@Body RequestBody responseBody);

    /**
     * 获取当前项目下对应用户的计划安排
     *
     * @param userid
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.PROJECT_PLAN)
    Observable<Result<List<ProjectPlan>>> getProjectPlan(@Query("owner") int userid, @Query("projectId") int projectId);

    /**
     * 根据项目和用户ID还有第N周获取对应的周报
     *
     * @param userId
     * @param weekCount
     * @return
     */

    @GET(NetworkURLConfig.WEEKLY_REPORT_QUERY)
    Observable<Result<List<WeeklyReportBean>>> getWeeklyReport(@Query("userId") int userId, @Query("byWeek") int weekCount, @Query("type") int type);

    /**
     * 根据用户和项目负责人Id获取项目列表
     *
     * @param userId
     * @param leaderId
     * @return
     */
    @GET(NetworkURLConfig.LIST_PROJECT_BY_USERID)
    Observable<Result<List<ProjectList>>> getProjectList(@Query("userId") int userId, @Query("leaderId") int leaderId);

    /**
     * 获取项目组成员列表
     *
     * @param pageIndex
     * @param pageNum
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.LIST_MEMBER)
    Observable<Result<List<MemberListBean>>> getMemberList(@Path("pageIndex") String pageIndex,
                                                           @Path("pageNum") String pageNum,
                                                           @Query("projectId") int projectId,
                                                           @Query("byWeek") int byWeek);

    /**
     * 获取个人信息
     *
     * @param userId
     * @return
     */
    @GET(NetworkURLConfig.PERSONAL_INFO)
    Observable<Result<PersonalBean>> getPersonalInfo(@Path("id") int userId);

    @GET(NetworkURLConfig.WEEKLY_REPORT_DETAIL)
    Observable<Result<WeeklyReportDetailBean>> getWeeklyReportDetail(@Path("id") int id);

    /**
     * 审批周报
     * @param weekyId
     * @param status
     * @param approvalStatus
     * @param remark
     * @return
     */
    @FormUrlEncoded
    @POST(NetworkURLConfig.APPROVAL_WEEKLY_REPORT)
    Observable<Result> approvalWeeklyReport(@Field("weeklyId") int weekyId,
                                                     @Field("planStatus") int status,
                                                     @Field("approvalStatus") int approvalStatus,
                                                     @Field("remark") String remark);

    /**
     * 获取项目负责人的周报列表
     * @param projectId
     * @param weeks
     * @return
     */
    @GET(NetworkURLConfig.HEADER_WEEKY_REPORT_GET)
    Observable<Result<HeaderItemBean>> getHeaderList(@Path("id")int projectId,@Query("byWeek")int weeks ,@Query("type")int type);
    class Utils {
        public static ApiService getInstance() {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(NetworkURLConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                    .client(okHttpClient)
                    .build();
            return retrofit.create(ApiService.class);
        }

        public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)//超时时间
                .writeTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)
                .build();

        public static Observable.Transformer schedulersTransformer() {
            return new Observable.Transformer() {
                @Override
                public Object call(Object observable) {
                    return ((Observable) observable).subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            };
        }
    }
}
