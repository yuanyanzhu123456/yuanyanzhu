package com.geo.rcs.modules.kafka;

import java.util.Arrays;
import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:23 2018/11/7
 */
public class HadoopConsts {

    public static  final String LINE_SPLITE="\n";
    public static  final String FIELD_SPLITE="\001";
    public static  final String  TOPIC_API_EVENT_ENTRY="API_EVENT_ENTRY";
    public static  final String  TOPIC_BRCB_TXINFO="TOPIC_BRCB_TXINFO";
    public static  final String  TOPIC_BRCB_LOGIN="TOPIC_BRCB_LOGIN";

    public static  final String  PROJECT_SPACE="/Rcs";
    public static  final String  PROJECT_RCS2_SERVER="/rcs2-server";
    public static  final List TABLES_RCS2_SERVER= Arrays.asList(TOPIC_API_EVENT_ENTRY);


}
