package com.aorise.weeklyreport.base;

import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;
import com.aorise.weeklyreport.bean.WeeklyReportUploadBean;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/6/26.
 */
public class TimeUtil {
    private static TimeUtil INSTANCE;

    public static TimeUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TimeUtil();
        }
        return INSTANCE;
    }

    public int getDayofWeek() {
        Calendar cal = Calendar.getInstance();//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.setTime(new Date());
        int weeks = cal.get(Calendar.WEEK_OF_YEAR) + 1;
        //   LogT.d("现在是第" + weeks + "周");
        return weeks;
    }

    public List<String> getHistoryWeeks() {
        List<String> weeklist = new ArrayList<>();
        for (int i = 0; i < getDayofWeek(); i++) {
            weeklist.add("第" + String.valueOf(i + 1) + "周");
            LogT.d("添加第N周" + String.valueOf(i + 1));
        }
        return weeklist;
    }

    public int caclulateDifferenceBySimpleDateFormat(Date date, Date nextDate) {
        int diff = -1;
        long from1 = date.getTime();
        long to1 = nextDate.getTime();
        diff = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        LogT.d("差距了" + diff + "天");
        return diff;
    }

    public String getSpecifiedDayAfter(String specifiedDay, int appendCount) {
        Calendar c = Calendar.getInstance();
        Date date = null;

        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(specifiedDay, new ParsePosition(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + appendCount);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
        return dayAfter;
    }

    public List<WeeklyReportUploadBean.WeeklyDateModelsBean> getWorkDateList(String startDate, String endDate) {
        List<WeeklyReportUploadBean.WeeklyDateModelsBean> workDateList = new ArrayList<>();
        Date _startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate, new ParsePosition(0));
        Date _endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate, new ParsePosition(0));
        //getSpecifiedDayAfter(startDate);
       // caclulateDiffByDate(_startDate, _endDate);
        for (int i = 1; i <= caclulateDiffByDate(_startDate, _endDate); i++) {
            WeeklyReportUploadBean.WeeklyDateModelsBean modelsBean = new WeeklyReportUploadBean.WeeklyDateModelsBean();
            modelsBean.setWorkDate(getSpecifiedDayAfter(startDate, i));
            workDateList.add(modelsBean);
        }
        LogT.d("workDateList "+workDateList.toString());
        return workDateList;
    }

    public int caclulateDiffByDate(Date date, Date nextDate) {
        int diff = -1;
        long from1 = date.getTime();
        long to1 = nextDate.getTime();
        int diffhour = (int) ((to1 - from1) / (1000 * 60 * 60));

        diff = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        if (diff > 0 && diffhour < 24) {
            diff = 1;
        }
        LogT.d("差距了" + diff + "天");
        return diff;
    }
}
