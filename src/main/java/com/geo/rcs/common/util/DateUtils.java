package com.geo.rcs.common.util;

import org.apache.commons.lang.math.NumberUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/18 09:37
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static DateUtils dateUtils = new DateUtils();

    /**
     * 格式化日期("yyyy-MM-dd")
     * @param date
     * @return
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 格式化日期
     * @param date
     * @param pattern 例如："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 比较日期大小
     * @return
     */
    public static boolean compareTo(String startTime, String endTime) {
        if(startTime == null || endTime == null){
            return false;
        }

        int flag = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            flag = endDate.compareTo(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flag == 1;
    }

    /**
     * 获取月份字符串
     * @param date
     * @param pattern
     * @param flag  -1：上一个月， 0：当前月， 1：下一个月
     * @return
     */
    public static String formatMonth(Date date, String pattern, int flag) {
	    if (date != null) {
	        SimpleDateFormat df = new SimpleDateFormat(pattern);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, flag);
            return df.format(calendar.getTime());
        }
        return null;
    }

    /**
     * 获取每周五的日期字符串
     * @param date
     * @param pattern
     * @param flag  -1：上一周， 0：当前周， 1：下一周
     * @return
     */
    public static String formatWeekFriday(Date date, String pattern, int flag) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int offset = 7 - dayOfWeek;
            calendar.add(Calendar.DATE, offset - 9 + flag*7);
            return df.format(calendar.getTime());
        }
	    return null;
    }

    /**
     * 获取传入日期所在月份的第一天和最后一天
     * @param date
     * @param flag  0：第一天   1：最后一天
     * @return
     */
    public static String getMonthFirstOrLastDay(Date date, int flag) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            // 获取该月份第一天
            if(flag == 0){
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date firstDate = calendar.getTime();
                return df.format(firstDate) + " 00:00:00";
            }
            // 获取该月份最后一天
            if(flag == 1){
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date lastDate = calendar.getTime();
                return df.format(lastDate) + " 23:59:59";
            }
        }
        return null;
    }

    /**
     * 获取date的月份的时间范围
     * @param date
     * @return
     */
    public static DateRange getMonthRange(Date date) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(date);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMaxTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取当前季度的时间范围
     * @return current quarter
     */
    public static DateRange getThisQuarter() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, (startCalendar.get(Calendar.MONTH) / 3) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, (startCalendar.get(Calendar.MONTH) / 3) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取当前季度名称
     * @return
     */
    public static String getThisQuarterName() {
        String QName = "";

        DateRange dateRange = getThisQuarter();
        int start = NumberUtils.toInt(format(dateRange.getStart(), "MM"));
        int end = NumberUtils.toInt(format(dateRange.getEnd(), "MM"));

        if(start >= 1 && end <= 3) {
            QName = "Q1";
        }else if(start >= 4 && end <= 6) {
            QName = "Q2";
        }else if(start >= 7 && end <= 9) {
            QName = "Q3";
        }else if(start >= 10 && end <= 12) {
            QName = "Q4";
        }

        return QName;
    }

    /**
     * 获取今天的时间范围
     * @return
     */
    public static DateRange getTodayRange() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_MONTH, 0);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_MONTH, 0);
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取昨天的时间范围
     * @return
     */
    public static DateRange getYesterdayRange() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_MONTH, -1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取当前月份的时间范围
     * @return
     */
    public static DateRange getThisMonth(){
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取上个月的时间范围
     * @return
     */
    public static DateRange getLastMonth(){
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -1);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, -1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 获取上个季度的时间范围
     * @return
     */
    public static DateRange getLastQuarter() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, (startCalendar.get(Calendar.MONTH) / 3 - 1) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        setMinTime(startCalendar);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, (endCalendar.get(Calendar.MONTH) / 3 - 1) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMaxTime(endCalendar);

        return dateUtils.new DateRange(startCalendar.getTime(), endCalendar.getTime());
    }

    /**
     * 设置最小时间
     * @param calendar
     */
    private static void setMinTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 设置最大时间
     * @param calendar
     */
    private static void setMaxTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
    }

    /**
     * 自定义时间范围类
     */
    public class DateRange {
        private Date start;
        private Date end;

        public DateRange(Date start, Date end) {
            this.start = start;
            this.end = end;
        }

        public Date getStart() {
            return start;
        }
        public void setStart(Date start) {
            this.start = start;
        }
        public Date getEnd() {
            return end;
        }
        public void setEnd(Date end) {
            this.end = end;
        }
    }

//    public static void main(String[] args) {
//        String str1 = formatMonth(new Date(), "yyyyMM", -1);
//        String str2 = formatMonth(new Date(), "yyyyMM", 0);
//        String str3 = formatMonth(new Date(), "yyyyMM", 1);
//        System.out.println(str1);
//        System.out.println(str2);
//        System.out.println(str3);
//        for(int i = 0; i < 52; i++) {
//            String str4 = formatWeekFriday(new Date(), "yyyyMMdd", -i);
//            System.out.println(str4);
//        }
//        System.out.println(getMonthFirstOrLastDay(new Date(), 0));
//        System.out.println(getMonthFirstOrLastDay(new Date(), 1));
//        DateUtils.DateRange dateRange = getThisQuarter();
//        System.out.println(format(dateRange.getStart(),"yyyy-MM-dd HH:mm:ss"));
//        System.out.println(format(dateRange.getEnd(), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(getThisQuarterName());
//        DateUtils.DateRange dateRange = getTodayRange();
//        System.out.println("开始时间：" + format(dateRange.getStart(),"yyyy-MM-dd HH:mm:ss"));
//        System.out.println("结束时间：" + format(dateRange.getEnd(),"yyyy-MM-dd HH:mm:ss"));
//        boolean flag = DateUtils.compareTo("2018-01-24 00:00:00", "2018-01-24 23:59:59");
//        System.out.println(flag);
//    }

    /**
     * 日期加减天数
     * @param dateTime
     * @param n
     * @return
     */
    public static Date addAndSubtractDaysByGetTime(Date dateTime/*待处理的日期*/,int n/*加减天数*/){

        //日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println(df.format(new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L)));
        //System.out.println(dd.format(new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L)));
        //注意这里一定要转换成Long类型，要不n超过25时会出现范围溢出，从而得不到想要的日期值
        return new Date(dateTime.getTime() + n * 24 * 60 * 60 * 1000L);
    }

    public static Date addAndSubtractDaysByCalendar(Date dateTime/*待处理的日期*/,int n/*加减天数*/){

        //日期格式
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Calendar calstart = java.util.Calendar.getInstance();
        calstart.setTime(dateTime);

        calstart.add(java.util.Calendar.DAY_OF_WEEK, n);

        System.out.println(df.format(calstart.getTime()));
        //System.out.println(dd.format(calstart.getTime()));
        return calstart.getTime();
    }
    /**
     * 单位为年或月换算为日
     */
    public static int changToDay(String unit){
        if(!"".equals(unit)){
            if("年".equals(unit)){
                return 365;
            }
            else if("月".equals(unit)){
                return 30;
            }
            else if("日".equals(unit)){
                return 1;
            }
        }
        return 0;
    }



}
