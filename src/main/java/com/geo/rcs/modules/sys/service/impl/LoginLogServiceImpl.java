package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.modules.sys.dao.LoginLogMapper;
import com.geo.rcs.modules.sys.entity.LoginLog;
import com.geo.rcs.modules.sys.service.LoginLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 登录日志
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */

@Service("loginLogService")
public class LoginLogServiceImpl implements LoginLogService {
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public LoginLog queryObject(Long id) {
        return loginLogMapper.queryObject(id);
    }


    @Override
    public List<LoginLog> queryList(Map<String, Object> map) {
        return loginLogMapper.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return loginLogMapper.queryTotal(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(LoginLog loginLog) {
        loginLogMapper.save(loginLog);
    }

    @Override
    public void update(LoginLog loginLog) {
        loginLogMapper.update(loginLog);
    }

    @Override
    public void delete(Long id) {
        loginLogMapper.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        loginLogMapper.deleteBatch(ids);
    }

    @Override
    public Page<LoginLog> findLoginLogByPage(LoginLog loginLog) {
        PageHelper.startPage(loginLog.getPageNo(), loginLog.getPageSize());
        return loginLogMapper.findLoginLogByPage(loginLog);
    }

    @Override
    public Map<String,Object> logCountByEmp(Map<String, Object> map) {
        List<Map<String,Object>> logRank;
        logRank = loginLogMapper.logRankByEmp(map);

        List<Map<String,Object>> logTotal;
        logTotal = loginLogMapper.logTotalByEmp(map);

        map.clear();
        map.put("logRank",logRank);
        map.put("logTotal",logTotal);

        return  map;
    }

    @Override
    public List<LoginLog> getLogsByMacIddr(String macIddr) {
        return null;
    }
}

