package com.aorise.weeklyreport.base;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.setTime(new Date());

        int weeks = cal.get(Calendar.WEEK_OF_YEAR) + 1;
        //   LogT.d("现在是第" + weeks + "周");
        LogT.d(" df format "+df.format(cal.getTime()));
        return weeks;
    }
    public String getMonDayofWeek() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);// 每周从周一开始
        cal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        cal.setTime(new Date());

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一
        //   LogT.d("现在是第" + weeks + "周");
        String monday = df.format(cal.getTime());
        LogT.d(" df format "+df.format(cal.getTime()));
        return monday;
    }


    public List<String> getHistoryWeeks() {
        List<String> weeklist = new ArrayList<>();
        for (int i = 0; i < getDayofWeek(); i++) {
            weeklist.add("第" + String.valueOf(getDayofWeek() - i) + "周");
            LogT.d("添加第N周" + String.valueOf(getDayofWeek() - i));
        }
        return weeklist;
    }

    public String date2date(String time) {
        String dateStr = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(time, new ParsePosition(0));
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = simpleDateFormat1.format(date);
        }catch (Exception e){

        }
        return dateStr;
    }

    public com.haibin.calendarview.Calendar stringToCalendar(String time) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar1 = simpleDateFormat.parse(time, new ParsePosition(0));
//        LogT.d("date is "+date.toString());
        int year = Integer.valueOf(time.substring(0, 4));
        int month = Integer.valueOf(time.substring(5, 7));
        int day = Integer.valueOf(time.substring(8, 10));
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        LogT.d(" stringToCalendar " + calendar.toString());
        return calendar;
    }

    public String getSpecifiedDayAfter(String specifiedDay, int appendCount) {
        Calendar c = Calendar.getInstance();
        Date date = null;

        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay, new ParsePosition(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + appendCount);

        String dayAfter = new SimpleDateFormat("dd").format(c.getTime());
        return dayAfter;
    }

    public List<String> getWorkDateList() {
        List<String> workDateList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar mondayCal = Calendar.getInstance(TimeZone.getDefault());//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        mondayCal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        mondayCal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        mondayCal.setTime(new Date());
        mondayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一

        Calendar sundayCal = Calendar.getInstance(TimeZone.getDefault());//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        sundayCal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        sundayCal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        sundayCal.setTime(new Date());
        sundayCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);// 每周从周一开始

        for (int i = 0; i < caclulateDiffByDate(mondayCal.getTime(), sundayCal.getTime()); i++) {
            workDateList.add(getSpecifiedDayAfter(df.format(mondayCal.getTime()), i));
        }
        LogT.d("workDateList " + workDateList.toString());
        return workDateList;
    }

    public int caclulateDiffByDate(Date date, Date nextDate) {
        int diff = -1;
        long from1 = date.getTime();
        long to1 = nextDate.getTime();
        LogT.d(" from1 " + from1 + " to1 " + to1);

        diff = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        LogT.d("差距了" + diff + "天");


        return diff + 1;
    }

    public Date String2Date(String time){
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = simpleDateFormat.parse(time,new ParsePosition(0));
        return date;
    }

    public static String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
