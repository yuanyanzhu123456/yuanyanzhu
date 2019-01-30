package com.geo.rcs.common.util;

import java.util.Date;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/11/9  10:59
 **/
public class DateOperateUtil {
    public static Date addDate(Date date,Long day,char operator) throws Exception {
        Long time = date.getTime();
        Long add = day*24*60*60*1000;
        if (operator == '+'){
            time+=add;
        }else if (operator == '-'){
            time-=add;
            if (time < 0){
                throw new Exception("The days of the date need to be greater than the days of the change");
            }
        }else{
            throw new Exception("the operator is wrong");
        }
        return new Date(time);
    }
}
