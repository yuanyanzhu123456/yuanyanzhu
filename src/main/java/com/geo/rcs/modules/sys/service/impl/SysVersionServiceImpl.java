package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.DateOperateUtil;
import com.geo.rcs.modules.sys.dao.CusVersionMapper;
import com.geo.rcs.modules.sys.dao.SysVersionMapper;
import com.geo.rcs.modules.sys.entity.CusVersion;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysVersion;
import com.geo.rcs.modules.sys.service.SysVersionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 版本
 *
 * @author wuzuqi
 * @email wuzuqi@geotmt.com
 * @date 2018-10-31
 */
@Service
public class SysVersionServiceImpl implements SysVersionService {
    @Autowired
    private SysVersionMapper sysVersionMapper;
    @Autowired
    private CusVersionMapper cusVersionMapper;



    @Override
    public SysVersion queryVersionName(String name) throws ServiceException {
        if (BlankUtil.isBlank(name)){
            return null;
       }
        return sysVersionMapper.queryVersionName(name);
    }

    @Override
    public SysVersion getVerInfoById(Long id){
        return sysVersionMapper.getVerInfoById(id);
    }

    @Override
    public void save(SysVersion sysVersion) throws ServiceException {

        sysVersionMapper.save(sysVersion);
    }



    @Override
    public Page<SysVersion> getVersionInfoByPage(SysVersion sysVersion) {
        PageHelper.startPage(sysVersion.getPageNo(), sysVersion.getPageSize());
        return sysVersionMapper.getVersionInfoByPage(sysVersion);
    }

    @Override
    public void updateVersion(SysVersion sysVersion) throws Exception {
        Integer oldDay = sysVersionMapper.queryDayById(sysVersion.getId());
        CusVersion cusVersion = new CusVersion();
        cusVersion.setVersionId(sysVersion.getId());
        //the version cycle has been updated,then update the cusVersion
        if (sysVersion.getDay() != null){
            if (oldDay != sysVersion.getDay().intValue()){
                List<CusVersion> list = cusVersionMapper.findAllByVersionId(sysVersion.getId());
                for (CusVersion cus : list){
                    cusVersion.setId(cus.getId());
                    Date cusUpdateTime = cus.getUpdateTime();
                    Date expireTime = DateOperateUtil.addDate(cusUpdateTime,sysVersion.getDay(),'+');
                    cusVersion.setExpireTime(expireTime);
                    cusVersionMapper.updateById(cusVersion);
                }
            }
        }

        sysVersionMapper.updateVersion(sysVersion);
    }

    @Override
    public void deleteVersion(Long id) {
        List<CusVersion> list = cusVersionMapper.findAllByVersionId(id);
        for (CusVersion cus : list){
            cusVersionMapper.delete(cus.getId());
        }
        sysVersionMapper.deleteVersion(id);
    }

    @Override
    public Map<String,Object> verUserSum() throws ServiceException {

        return  sysVersionMapper.verUserSum();
    }

    @Override
    public SysVersion getIdByUserNum(Long userNum) throws ServiceException {
        return sysVersionMapper.getIdByUserNum(userNum);
    }


    @Override
    public Integer queryDayById(Long id) {
        return sysVersionMapper.queryDayById(id);
    }

    @Override
    public Page<SysUser> findCustomOfVersion(SysVersion sysVersion) {
        PageHelper.startPage(sysVersion.getPageNo(),sysVersion.getPageSize());
        return sysVersionMapper.findCustomOfVersion(sysVersion);
    }

    @Override
    public  Double getPriceById(Long customerId) {
        return sysVersionMapper.getPriceById(customerId);
    }




}
