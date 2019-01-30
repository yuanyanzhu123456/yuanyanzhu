package com.geo.rcs.modules.monitor.task;

import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.task
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月29日 上午10:53
 */
@Component("cleanLogsOnTime")
public class CleanLogsOnTime {

    @Autowired
    private TaskLogService taskLogService;

    @Transactional(rollbackFor = Exception.class)
    public void cleanLogsOnTime(Long parm1,Long parm2){
        try{
            //查找超出时间的日志

            //进行删除
        }catch (Exception e){
            LogUtil.error("定时删除日志", new Date().toString(), "系统", e);
        }
    }
}
