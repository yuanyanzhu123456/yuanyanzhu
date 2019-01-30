package com.geo.rcs.modules.source.handler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldFormattor {

    public static void main(String[] args){

        System.out.println(DatetimeFormattor("1508083200000"));
        System.out.println(DatetimeFormattor("19931125"));
        System.out.println(DatetimeFormattor("2017/3/24 00:00:00"));
        System.out.println(DatetimeFormattor("2017年05月19日"));
        System.out.println(DatetimeFormattor("2017-05-19 00:00:00"));
        System.out.println(DatetimeFormattor("20"));
        System.out.println(DatetimeFormattor("12345678j9"));
        System.out.println(DatetimeFormattor("2305231993****231X"));
    }


    public static String DatetimeFormattor(String dateValue){

        if(dateValue== null|| dateValue.length()<8){
            return "";
        }else{
            SimpleDateFormat sdfIn1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfIn2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat sdfIn3 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdfIn0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try{
                if(dateValue.length()==8){
                    Date now = sdfIn1.parse(dateValue);
                    String nowDatetime = sdfOut.format(now);
                    System.out.println(-1);
                    return nowDatetime;
                }else{
                    Date now = new Date(Long.valueOf(dateValue));
                    String nowDatetime = sdfOut.format(now);
                    System.out.println(-2);
                    return nowDatetime;
                }
            }catch (Exception e0){
                try{
                    Date now = sdfIn0.parse(dateValue);
                    String nowDatetime = sdfOut.format(now);
                    System.out.println(0);
                    return nowDatetime;
                }catch (Exception e1){

                    try{
                        Date now = sdfIn2.parse(dateValue);
                        String nowDatetime = sdfOut.format(now);
                        System.out.println(2);
                        return nowDatetime;
                    }catch (Exception e3){
                        try{
                            Date now = sdfIn3.parse(dateValue);
                            String nowDatetime = sdfOut.format(now);
                            System.out.println(3);
                            return nowDatetime;
                        }catch (Exception e4){
                            return "";
                        }
                    }

                }
            }
        }


    }


    public static String DateFormattor(String dateValue){

        if(dateValue== null|| dateValue.length()<8){
            return "";
        }else{
            SimpleDateFormat sdfIn1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfIn2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat sdfIn3 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdfIn0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdfIn4 = new SimpleDateFormat("yyyy-MM-dd");

            SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");

            try{
                if(dateValue.length()==8){
                    Date now = sdfIn1.parse(dateValue);
                    String nowDatetime = sdfOut.format(now);
                    System.out.println(-1);
                    return nowDatetime;
                }else{
                    Date now = new Date(Long.valueOf(dateValue));
                    String nowDatetime = sdfOut.format(now);
                    System.out.println(-2);
                    return nowDatetime;
                }
            }catch (Exception e0){
                try{
                    Date now = sdfIn0.parse(dateValue);
                    String nowDatetime = sdfOut.format(now);
                    System.out.println(0);
                    return nowDatetime;
                }catch (Exception e1){

                    try{
                        Date now = sdfIn2.parse(dateValue);
                        String nowDatetime = sdfOut.format(now);
                        System.out.println(2);
                        return nowDatetime;
                    }catch (Exception e3){
                        try{
                            Date now = sdfIn3.parse(dateValue);
                            String nowDatetime = sdfOut.format(now);
                            System.out.println(3);
                            return nowDatetime;
                        }catch (Exception e4){
                            try{
                                Date now = sdfIn4.parse(dateValue);
                                String nowDatetime = sdfOut.format(now);
                                System.out.println(4);
                                return nowDatetime;
                            }catch (Exception e5){
                                System.out.println("dateValue日期无法解析："+dateValue);
                                return "";
                            }
                        }
                    }

                }
            }
        }


    }

    /**
     * 日期及日期时间类型判断
     * @param dateValue
     * @return
     */
    public static Boolean DateTypeMatch(String dateValue){


        return true;
    }

}
