package com.aorise.weeklyreport.base;

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
        LogT.d("现在是第" + weeks + "周");
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
}
