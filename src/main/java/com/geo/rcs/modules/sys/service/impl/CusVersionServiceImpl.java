package com.geo.rcs.modules.sys.service.impl;

import com.geo.rcs.common.util.DateOperateUtil;
import com.geo.rcs.modules.sys.dao.CusVersionMapper;
import com.geo.rcs.modules.sys.entity.CusVersion;
import com.geo.rcs.modules.sys.service.CusVersionService;
import com.geo.rcs.modules.sys.service.SysVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/11/9  10:15
 **/
@Service("cusVersionService")
public class CusVersionServiceImpl implements CusVersionService {
    @Autowired
    SysVersionService sysVersionService;
    @Autowired
    CusVersionMapper cusVersionMapper;

    @Override
    public void save(CusVersion cusVersion) throws Exception {
        cusVersion.setCreateTime(new Date());
        cusVersion.setUpdateTime(new Date());
        Integer day = sysVersionService.queryDayById(cusVersion.getVersionId());
        Date expireTime = DateOperateUtil.addDate(new Date(),Integer.toUnsignedLong(day),'+');
        cusVersion.setExpireTime(expireTime);
        cusVersionMapper.save(cusVersion);
    }

    @Override
    public Long queryVersionId(Long customerId) {
        return cusVersionMapper.queryVersionId(customerId);
    }

    @Override
    public void updateCusVersion(CusVersion cusVersion) throws Exception {
        cusVersion.setUpdateTime(new Date());
        Integer day = sysVersionService.queryDayById(cusVersion.getVersionId());
        Date expireTime = DateOperateUtil.addDate(new Date(),Integer.toUnsignedLong(day),'+');
        cusVersion.setExpireTime(expireTime);
        cusVersionMapper.update(cusVersion);
    }

    @Override
    public Map<String, Object> getCusVersionInfo(Long customerId) {
        Map<String,Object> map =  cusVersionMapper.getCusVersionInfo(customerId);
        if (map == null){
            return map;
        }
        Date expireTime = (Date)map.get("expiretime");
        String format="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
        String expiretime = sdf.format(expireTime);
        map.remove("expiretime");
        map.put("expiretime",expiretime);
        return map;
    }

    @Override
    public void renewal(CusVersion cusVersion) {

         cusVersionMapper.renewal(cusVersion);
    }

    @Override
    public CusVersion selectByCustomId(Long customId) {
        return cusVersionMapper.selectByCustomId(customId);
    }

    @Override
    public CusVersion getCusInfoById(Long customerId) {
        return cusVersionMapper.getCusInfoById(customerId);
    }

    @Override
    public List<CusVersion> getAllCusVerInfo() {

        return cusVersionMapper.getAllCusVerInfo();
    }



}




