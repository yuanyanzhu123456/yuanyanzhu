package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.modules.sys.dao.SysVersionLogMapper;
import com.geo.rcs.modules.sys.entity.SysVersionLog;
import com.geo.rcs.modules.sys.service.SysVersionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 15:18 2018/12/9
 */
@Service
public class SysVersionLogServiceImpl implements SysVersionLogService {

    @Autowired
    private SysVersionLogMapper sysVersionLogMapper;
    @Override
    public void createVersionLog(SysVersionLog sysVersionLog) {

         sysVersionLogMapper.createVersionLog(sysVersionLog);
    }
}
