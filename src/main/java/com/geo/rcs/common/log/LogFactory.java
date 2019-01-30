package com.geo.rcs.common.log;

import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.util.IPUtils;
import com.geo.rcs.common.util.http.HttpContextUtils;
import com.geo.rcs.modules.sys.entity.*;

import java.util.Date;

/**
 * 日志对象创建工厂
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date  2017-12-22
 */
public class LogFactory {
    /**
     * 创建登录日志
     */
    public static LoginLog createLoginLog(Constant.LogType logType, SysUser user) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLogname(logType.getMessage());
        loginLog.setUserId(user.getId());
        loginLog.setUsername(user.getUsername());
        loginLog.setCreatetime(new Date());
        loginLog.setSucceed(Constant.LogSucceed.SUCCESS.getMessage());
        loginLog.setIp(IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
        return loginLog;
    }

    /**
     * 创建版本日志
     */
    public static SysVersionLog createVersionLog(Constant.LogType logType, CusVersion cusVersion) {
       SysVersionLog sysVersionLog = new SysVersionLog();
       sysVersionLog.setName(logType.getMessage());
       sysVersionLog.setNewVersionId(cusVersion.getVersionId());
       sysVersionLog.setCustomerId(cusVersion.getCustomerId());
       sysVersionLog.setCreater(cusVersion.getUpdater());
       sysVersionLog.setCreateTime(cusVersion.getUpdateTime());
       sysVersionLog.setStatus(1);
       return sysVersionLog;
    }

}
