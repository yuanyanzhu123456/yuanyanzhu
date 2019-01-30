package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.SysVersionLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author： wuzuqi
 * @email: wuzuqi@geotmt.com
 * @Description:
 * @Date： Created in 16:26 2018/12/9
 */

@Mapper
@Component(value = "sysVersionLogMapper")
public interface SysVersionLogMapper {
    void createVersionLog(SysVersionLog sysVersionLog);
}
