package com.geo.rcs.modules.engine.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EngineCalculator {

    public static int Not(int res) {
        if(res==1){
            res = 0;
        }else{
            res = 1;
        }
        return res;
    }

    public static int compareDate( String startDate, String endDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return start.compareTo(end);
    }

    public static int compareDatetime( String startDate, String endDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return start.compareTo(end);
    }

    public static int diffToday( String recentDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date();
        Date end = sdf.parse(recentDate);

        long diff = today.getTime() - end.getTime();
        return (int)(diff/(1000*60*60*24));
    }

    public static int diffTodayTime( String recentDate ) throws  Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date today = new Date();
        Date end = sdf.parse(recentDate);

        long diff = today.getTime() - end.getTime();
        return (int)(diff/(1000*60*60*24));
    }
}
