package com.geo.rcs.common.util;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月31日 下午3:34
 */
public class TimeUtil {

        public TimeUtil() {
        }

        public long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
            //定义时间类型
            Date date = null;
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:ss");
            try {
                date = inputFormat.parse(inVal); //将字符型转换成日期型
            } catch (Exception e) {
                e.printStackTrace();
            }
            return date.getTime();   //返回毫秒数
        }

        public static  String dqsj() {  //此方法用于获得当前系统时间（格式类型2007-11-6 15:10:58）
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            return format;  //返回当前时间
        }

        public static void main(String[] args) {
            String dqsj = dqsj();   //获得String dqsj = dqsj();   //获得当前系统时间
            TimeUtil df = new TimeUtil();  //实例化方法

            long startT=df.fromDateStringToLong("2005-03-03 14:51:23"); //定义上机时间
            long endT=df.fromDateStringToLong("2004-03-03 13:50:23");  //定义下机时间

            long ss=(startT-endT)/(1000); //共计秒数
            int hh=(int)ss/3600;  //共计小时数
            int dd=(int)hh/24;   //共计天数

        }


    }
