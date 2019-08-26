package com.aorise.weeklyreport.network;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.aorise.weeklyreport.base.LogT;
import com.aorise.weeklyreport.bean.FillProjectPlan;
import com.aorise.weeklyreport.bean.HeaderItemBean;
import com.aorise.weeklyreport.bean.MemberListBean;
import com.aorise.weeklyreport.bean.MemberListSpinnerBean;
import com.aorise.weeklyreport.bean.PersonalBean;
import com.aorise.weeklyreport.bean.ProjectBaseInfo;
import com.aorise.weeklyreport.bean.ProjectList;
import com.aorise.weeklyreport.bean.ProjectListBean;
import com.aorise.weeklyreport.bean.ProjectPlan;
import com.aorise.weeklyreport.bean.StatisticBean;
import com.aorise.weeklyreport.bean.UserInfoBean;
import com.aorise.weeklyreport.bean.WeeklyReportBean;
import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
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

    //@Headers("Content-Type: application/json;charset=UTF-8")
    @FormUrlEncoded
    @POST(NetworkURLConfig.LOGIN_URL)
    Observable<Result<UserInfoBean>> login(@Field("userName")String userName, @Field("passWord")String passWord);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST(NetworkURLConfig.LOGIN_URL_N)
    Observable<Result<String>> login(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(NetworkURLConfig.POST_WEEKLY_REPORT)
    Observable<Result> fillInWeeklyReprot(@Body RequestBody responseBody);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT(NetworkURLConfig.PUT_WEEKLY_REPORT)
    Observable<Result> updateWeeklyReprot(@Body RequestBody responseBody);
    /**
     * 获取当前项目下对应用户的计划安排
     *
     * @param userid
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.PROJECT_PLAN)
    Observable<Result<List<FillProjectPlan>>> getProjectPlan(@Path("owner") int userid, @Query("projectId") int projectId);

    /**
     * 获取当前项目下对应用户的计划安排
     *
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.PROJECT_PLAN_BYPROJECTID)
    Observable<Result<List<ProjectPlan>>> getProjectPlan(@Query("projectId") int projectId);

    /**
     * 根据项目和用户ID还有第N周获取对应的周报
     *
     * @param userId
     * @param weekCount
     * @return
     */

    @GET(NetworkURLConfig.WEEKLY_REPORT_QUERY)
    Observable<Result<List<WeeklyReportBean>>> getWeeklyReport(@Query("userId") int userId, @Query("byWeek") int weekCount, @Query("type") int type);

    @GET(NetworkURLConfig.WEEKLY_REPORT_QUERY)
    Observable<Result<List<WeeklyReportBean>>> getWeeklyReport(@Query("projectId") int projectId,@Query("userId") int userId, @Query("byWeek") int weekCount, @Query("type") int type);

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
     * 分页获取项目列表
     *
     * @return
     */
    @GET(NetworkURLConfig.LIST_PROJECT)
    Observable<Result<ProjectListBean>> getProjectList(@Path("pageIndex") String pageIndex, @Path("pageNum") String pageNum);

    /**
     * 根据项目ID获取项目信息
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.PROJECT_BASE_INFO)
    Observable<Result<ProjectBaseInfo>> getProjectInfoById(@Path("id")int projectId);
    /**
     * 获取项目组成员列表
     *
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.LIST_MEMBER)
    Observable<Result<MemberListBean>> getMemberList(@Path("pageIndex") int pageIndex,
                                                           @Path("pageNum") int pageNum,
                                                           @Query("projectId") int projectId,
                                                           @Query("byWeek") int byWeek);

    @GET(NetworkURLConfig.LIST_MEMBER_SPINNER)
    Observable<Result<List<MemberListSpinnerBean>>> getMemberList(@Path("projectId")int projectId);
    /**
     * 获取个人信息
     *
     * @param userId
     * @return
     */
    @GET(NetworkURLConfig.PERSONAL_INFO)
    Observable<Result<PersonalBean>> getPersonalInfo(@Path("id") int userId);

    @GET(NetworkURLConfig.WEEKLY_REPORT_DETAIL)
    Observable<Result<WeeklyReportDetailBean>> getWeeklyReportDetail(@Query("id") int id);

    /**
     * 审批周报
     *
     * @return
     */
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(NetworkURLConfig.APPROVAL_WEEKLY_REPORT)
    Observable<Result> approvalWeeklyReport(@Body RequestBody model);
    /**
     * 获取项目负责人的周报列表
     *
     * @param projectId
     * @param weeks
     * @return
     */
    @GET(NetworkURLConfig.HEADER_WEEKY_REPORT_GET)
    Observable<Result<HeaderItemBean>> getHeaderList(@Path("id") int projectId, @Query("byWeek") int weeks, @Query("type") int type);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(NetworkURLConfig.HEADER_WEEKY_REPORT_POST)
    Observable<Result<Integer>> postHeaderReport(@Body RequestBody model);


    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT(NetworkURLConfig.HEADER_WEEKY_REPORT_POST)
    Observable<Result<Integer>> putHeaderReport(@Body RequestBody model);

    /**
     * 获取项目统计信息
     * @param projectId
     * @return
     */
    @GET(NetworkURLConfig.PROJECT_STATISTIC)
    Observable<Result<List<StatisticBean>>> getStatisticInfoByID(@Query("id") int projectId);

    class Utils {

        public static ApiService getInstance(Context context) {
            OkHttpClient okHttpClient ;
            OkHttpClient.Builder mOkhttpBuilder = new OkHttpClient.Builder()
                    .connectTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)//超时时间
                    .writeTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)
                    .readTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS);
            String simpleName = "";

            if (context instanceof AppCompatActivity) {
                LogT.d("这是登录界面");
                simpleName = ((Activity) context).getClass().getSimpleName();
                LogT.d(" context instanceof AppCompatActivity simpleName is " + simpleName);
                if (simpleName.equals("LoginActivity")) {
                    mOkhttpBuilder.addInterceptor(new ReceivedCookiesInterceptor(context));
                }
            }
            okHttpClient = mOkhttpBuilder.build();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(NetworkURLConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                    .client(okHttpClient)
                    .build();
            return retrofit.create(ApiService.class);
        }

//        public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)//超时时间
//                .writeTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)
//                .readTimeout(NetworkTimeBuildConfig.CONNECT_TIME, TimeUnit.SECONDS)
//                .addInterceptor(new ReceivedCookiesInterceptor(mContext))
//                .build();

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

        static class ReceivedCookiesInterceptor implements Interceptor {
            private Context context;

            public ReceivedCookiesInterceptor(Context context) {
                super();
                this.context = context;
            }

            @Override
            public Response intercept(Chain chain) throws IOException {

                Response originalResponse = chain.proceed(chain.request());
                //这里获取请求返回的cookie
                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    final StringBuffer cookieBuffer = new StringBuffer();
                    //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
                    //解析Cookie

                    for (String header : originalResponse.headers("Set-Cookie")) {
                        LogT.d(" header " + header);
                        cookieBuffer.append(header);
                    }

                    LogT.d(" now cookiebuffer content is " + String.valueOf(cookieBuffer));
                    SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("cookie", cookieBuffer.toString());
                    editor.commit();
                }

                return originalResponse;
            }
        }
    }
}
