package com.aorise.weeklyreport.base;

import android.util.Log;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/22.
 */
public class DateUtil {
    private static final String TAG = "DateUtil";

    public static String getDayAfterToday(String today, int appendDay, String endDateStr) {

        String nextDay = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(today, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + appendDay);
        Log.d("DateUtil", "获取" + appendDay + "天后的日期为" + calendar.getTime().toString());
        Date endDate = dateFormat.parse(endDateStr, new ParsePosition(0));
        Date endCalendar = dateFormat.parse(dateFormat.format(calendar.getTime()), new ParsePosition(0));
        if (endCalendar.after(endDate)) {
            nextDay = dateFormat.format(endDate);
        } else {
            nextDay = dateFormat.format(endCalendar);
        }
        return nextDay;
    }

    public static int getDiffDay(String firstDate, String NextDate) {
        int diffDay = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date firdate = dateFormat.parse(firstDate, new ParsePosition(0));
        Date nextdate = dateFormat.parse(NextDate, new ParsePosition(0));
        long from1 = firdate.getTime();
        long to1 = nextdate.getTime();
        Log.d(TAG, " from1 " + from1 + " to1 " + to1);
        diffDay = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        int diffhour = (int) ((to1 - from1) / (1000 * 60 * 60));
        if (diffhour >= 0) {
            diffDay++;
        }
        Log.d(TAG, "差距了......." + Math.abs(diffDay) + "天");
        return Math.abs(diffDay);
    }

    public static int getDiffMonth(String firstDate, String NextDate) {
        int diffMonth = 0;
        diffMonth = getDiffDay(firstDate, NextDate) / 30;
        int diffDay = getDiffDay(firstDate, NextDate) % 30;
        Log.d(TAG, "差距了" + diffMonth + "月");
        Log.d(TAG, "差距了" + diffDay + "天");
        if (diffDay < 30 && diffDay > 0) {
            diffMonth++;
        }
        return diffMonth;
    }

    public static String compare2Date(String datestr1, String datestr2) {
        String datestr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        Date date1 = simpleDateFormat.parse(datestr1, new ParsePosition(0));
        Date date2 = simpleDateFormat.parse(datestr2, new ParsePosition(0));

        if (date1.after(date2)) {
            date = date1;
        } else {
            date = date2;
        }
        datestr = simpleDateFormat.format(date);
        LogT.d(" 比比哪个更大" + datestr);
        return datestr;
    }
    public static String compareSmallDate(String datestr1, String datestr2) {
        String datestr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        Date date1 = simpleDateFormat.parse(datestr1, new ParsePosition(0));
        Date date2 = simpleDateFormat.parse(datestr2, new ParsePosition(0));

        if (date1.before(date2)) {
            date = date1;
        } else {
            date = date2;
        }
        datestr = simpleDateFormat.format(date);
        LogT.d(" 比比哪个更小" + datestr);
        return datestr;
    }
}
