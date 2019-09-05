package com.aorise.weeklyreport.base;

import com.aorise.weeklyreport.bean.TimePickerBean;
import com.aorise.weeklyreport.bean.WeeklyReportDetailBean;

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
        LogT.d(" df format " + df.format(cal.getTime()));
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
        LogT.d(" df format " + df.format(cal.getTime()));
        return monday;
    }

    /**
     * 获取周数列表
     *
     * @return
     */
    public List<String> getHistoryWeeks() {
        List<String> weeklist = new ArrayList<>();
        for (int i = 0; i < getDayofWeek(); i++) {
            weeklist.add("第" + String.valueOf(getDayofWeek() - i) + "周");
            LogT.d("添加第N周" + String.valueOf(getDayofWeek() - i));
        }
        return weeklist;
    }

    /**
     * 年-月-日 时:分:秒 转年-月-日
     *
     * @param time
     * @return
     */
    public String date2date(String time) {
        String dateStr = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(time, new ParsePosition(0));
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            dateStr = simpleDateFormat1.format(date);
        } catch (Exception e) {

        }
        return dateStr;
    }

    public com.haibin.calendarview.Calendar stringToCalendar(String time) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        int year = Integer.valueOf(time.substring(0, 4));
        int month = Integer.valueOf(time.substring(5, 7));
        int day = Integer.valueOf(time.substring(8, 10));
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        LogT.d(" stringToCalendar " + calendar.toString());
        return calendar;
    }

    /**
     * 获取多少天后的日期
     *
     * @param specifiedDay 当前日期
     * @param appendCount  当前日期多少天后
     * @return 多少天后的日期
     */

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

        String dayAfter = new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
        return dayAfter;
    }

    /**
     * 计算当前周的日历
     */
    public List<TimePickerBean> getWorkDateList(int week) {
        List<TimePickerBean> workDateList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar mondayCal = Calendar.getInstance(TimeZone.getDefault());//这一句必须要设置，否则美国认为第一天是周日，而我国认为是周一，对计算当期日期是第几周会有错误
        mondayCal.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        mondayCal.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天

        mondayCal.set(Calendar.WEEK_OF_YEAR, week - 1);
        mondayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一

        LogT.d("开始时间为" + mondayCal.toString());

        for (int i = 0; i < 7; i++) {
            String weeklyName = "";
            TimePickerBean timePickerBean = new TimePickerBean();
            boolean defaultSelect = false;
            switch (i) {
                case 0:
                    weeklyName = "周一";
                    break;
                case 1:
                    weeklyName = "周二";
                    break;
                case 2:
                    weeklyName = "周三";
                    break;
                case 3:
                    weeklyName = "周四";
                    break;
                case 4:
                    weeklyName = "周五";
                    break;
                case 5:
                    weeklyName = "周六";
                    break;
                case 6:
                    weeklyName = "周日";
                    break;
            }
            timePickerBean.setWeekName(weeklyName);
            timePickerBean.setAmSelected(defaultSelect);
            timePickerBean.setPmSelected(defaultSelect);
            timePickerBean.setDateName(getSpecifiedDayAfter(df.format(mondayCal.getTime()), i));
            workDateList.add(timePickerBean);
        }
        LogT.d("workDateList " + workDateList.toString());
        return workDateList;
    }

    /**
     * 2019年09月08日 转为 2019-09-08
     *
     * @param dateBefor
     * @return
     */
    public String cn2enDate(String dateBefor) {

        String afterReplaceyear = dateBefor.replace("年", "-");
        String afterReplaceMonth = afterReplaceyear.replace("月", "-");
        String afterReplaceDay = afterReplaceMonth.replace("日", "");
        return afterReplaceDay;
    }

    /**
     * 2019-09-08 转为 2019年09月08日
     *
     * @param dateBefor
     * @return
     */
    @Deprecated
    public String en2cnDate(String dateBefor) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat cnsp = new SimpleDateFormat("yyyy年MM月dd日");

        return cnsp.format(simpleDateFormat.parse(dateBefor, new ParsePosition(0)));
    }

    /**
     * 根据日期获取对应周数
     *
     * @param date
     * @return
     */
    public String date2weekName(String date) {
        String weekName = "";
        SimpleDateFormat cnsp = new SimpleDateFormat("yyyy年MM月dd日");
        Date date1 = cnsp.parse(date, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        calendar.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        switch (calendar.get(Calendar.WEEK_OF_MONTH)) {
            case 1:
                weekName = "周一";
                break;
            case 2:
                weekName = "周二";
                break;
            case 3:
                weekName = "周三";
                break;

            case 4:
                weekName = "周四";
                break;
            case 5:
                weekName = "周五";
                break;
            case 6:
                weekName = "周六";
                break;
            case 0:
            case 7:
                weekName = "周日";
                break;
        }
        return weekName;
    }

    /**
     * 根据日期获取对应月份
     *
     * @param date
     * @return
     */
    public String endate2monthName(String date) {
        SimpleDateFormat ensp = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat cnsp = new SimpleDateFormat("yyyy年MM月");
        Date date1 = ensp.parse(date, new ParsePosition(0));

        return cnsp.format(date1);
    }

    /**
     * 在FillReport中如果是编辑 则将WeeklyDateModelsBean 转为TimePickerBean
     *
     * @param bean
     * @return
     */
    public TimePickerBean weeklyBean2TimePicker(WeeklyReportDetailBean.WeeklyDateModelsBean bean) {
        TimePickerBean timePickerBean = new TimePickerBean();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat cnsp = new SimpleDateFormat("yyyy年MM月dd日");
        switch (bean.getDayState()) {
            case 1:
                timePickerBean.setAmSelected(true);
                break;
            case 2:
                timePickerBean.setPmSelected(true);
                break;
            case 3:
                timePickerBean.setAmSelected(true);
                timePickerBean.setPmSelected(true);
                break;
        }
        String dateName = cnsp.format(simpleDateFormat.parse(bean.getWorkDate(), new ParsePosition(0)));
        timePickerBean.setDateName(dateName);


        String weekName = "";
        Date date1 = cnsp.parse(dateName, new ParsePosition(0));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // 设置每周的第一天为星期一
        calendar.setMinimalDaysInFirstWeek(7); // 设置每周最少为7天
        LogT.d(" calendar.get(Calendar.WEEK_OF_MONTH) " + (calendar.get(Calendar.DAY_OF_WEEK) - 1));
        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 1:
                weekName = "周一";
                break;
            case 2:
                weekName = "周二";
                break;
            case 3:
                weekName = "周三";
                break;
            case 4:
                weekName = "周四";
                break;
            case 5:
                weekName = "周五";
                break;
            case 6:
                weekName = "周六";
                break;
            case 0:
            case 7:
                weekName = "周日";
                break;
        }
        timePickerBean.setWeekName(weekName);
        return timePickerBean;
    }

    /**
     * 计算时间差
     *
     * @param date     前一个时间
     * @param nextDate 后一个
     * @return 天数
     */
    public int caclulateDiffByDate(Date date, Date nextDate) {
        int diff = -1;
        long from1 = date.getTime();
        long to1 = nextDate.getTime();
        LogT.d(" from1 " + from1 + " to1 " + to1);

        diff = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));
        LogT.d("差距了" + diff + "天");


        return diff + 1;
    }

    public Date String2Date(String time) {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = simpleDateFormat.parse(time, new ParsePosition(0));
        return date;
    }

    /**
     * 日期时间戳补0
     *
     * @param time
     * @return
     */
    public static String appendZero(int time) {
        if (time < 10) {
            return "0" + String.valueOf(time);
        } else {
            return String.valueOf(time);
        }
    }
}
