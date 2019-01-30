package com.geo.rcs.common.log;

import org.apache.commons.lang.StringUtils;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.log
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月29日 下午2:20
 */
public enum LogFileName {


    //配置到logback.xml中的logger name="vipUser"
    API_LOG("apiLog"),
    API_ERROR_LOG("apiErrorLog"),
    DAILY_LOG("dailyLog");

    private String logFileName;

    LogFileName(String fileName) {
        this.logFileName = fileName;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public static LogFileName getAwardTypeEnum(String value) {
        LogFileName[] arr = values();
        for (LogFileName item : arr) {
            if (null != item && StringUtils.isNotBlank(item.logFileName)) {
                return item;
            }
        }
        return null;
    }
}
