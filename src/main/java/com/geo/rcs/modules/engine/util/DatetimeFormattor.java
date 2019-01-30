package com.geo.rcs.modules.engine.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatetimeFormattor {

    public static String now(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String nowDatetime = sdf.format(now);
        return nowDatetime;
    }

    public static String nowDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String nowDatetime = sdf.format(now);
        return nowDatetime;
    }

    public static String nowDateTime(int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = getRecentDateime(day);
        String nowDatetime = sdf.format(now);
        return nowDatetime;
    }

    public static String nowDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String nowDatetime = sdf.format(now);
        return nowDatetime;
    }

    public static String nowDate(int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = getRecentDateime(day);
        String nowDatetime = sdf.format(now);
        return nowDatetime;
    }

    /**
     * 解析日期时间字符串
     * @param dateTime
     * @return
     */
    public static Date parseDateTime(String dateTime){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date nowDatetime = sdf.parse(dateTime);
            return nowDatetime;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析日期字符串
     * @param date
     * @return
     */
    public static Date parseDate(String date){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date nowDate = sdf.parse(date);
            return nowDate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String formatDate(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            String nowDate = sdf.format(date);
            return nowDate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期时间
     * @param dateTime
     * @return
     */
    public static String formatDateTime(Date dateTime){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String nowDate = sdf.format(dateTime);
            return nowDate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取day天时间差的时间
     * @param day 天数差
     * @return
     */
    public static Date getRecentDateime(int day) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 获取Month时间差的时间
     * @param month 天数差
     * @return
     */
    public static Date getRecentMonthDateTime(int month) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 日期时间对比
     * @param dateTime
     * @param nextDateTime
     */
    public static int compareDateTimeString(String dateTime, String nextDateTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = sdf.parse(dateTime);
        Date end = sdf.parse(nextDateTime);

        return start.compareTo(end);

    }



    public static void main(String[] args) {


    }


}