package com.geo.rcs.modules.admin.adminIndex.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.admin.adminIndex.dao.StatisticInfoMapper;
import com.geo.rcs.modules.admin.adminIndex.service.StatisticInfoService;
import com.geo.rcs.modules.sys.dao.SysUserMapper;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/10/23  11:00
 **/
@Component
public class StatisticInfoServiceImpl implements StatisticInfoService {
    @Autowired
    StatisticInfoMapper statisticInfoMapper;
    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public Map<Object, Object> getSingleCustomInfo(Long id) {
        return statisticInfoMapper.getSingleCustomInfo(id);
    }

    @Override
    public Page<SysUser> findEmployerListByPage(SysUser sysUser) throws ServiceException {
        PageHelper.startPage(sysUser.getPageNo(),sysUser.getPageSize());
        return sysUserMapper.findEmployerListByPage(sysUser);
    }

    @Override
    public Map<Object, Object> getOverviewInfo() {
        return statisticInfoMapper.StatisticInfoService();
    }

    @Override
    public Map<String, Object> getActivityGraph(Map<Object,Object> map) {
        List<Map<Object,Object> > graphUserData;
        graphUserData = statisticInfoMapper.getActivityUserGraph(map);

        List<Map<Object,Object> > graphCmpData;
        graphCmpData  = statisticInfoMapper.getActivityCmpGraph(map);

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("graphUserData",graphUserData);
        result.put("graphCmpData",graphCmpData);
        return result;
    }

    /**
     * 指定时间内的新增用户趋势图
     */
    @Override
    public Map<String,Object> getNewCustomTrend(Map<Object,Object> map){
        Map<String,Object> result = new HashMap<String,Object>();

        List<Map<Object,Object> > getNewCustomsTrend;
        List<Map<Object,Object> > getPerCompanyNewUserNum;
        List<Map<Object,Object> > getCustomAndUserNumForTime;

        int dayNum = (Integer)map.get("dayNum");
        getPerCompanyNewUserNum =  statisticInfoMapper.getPerCompanyNewUserNum(dayNum);
        getCustomAndUserNumForTime = statisticInfoMapper.getCustomAndUserNumForTime(dayNum);
        getNewCustomsTrend = statisticInfoMapper.getNewCustomsTrend(map);

        result.put("newUserNumRank",getPerCompanyNewUserNum);
        result.put("customAndUserNum",getCustomAndUserNumForTime);
        result.put("newCustomsTrend",getNewCustomsTrend);
        return result;
    }

    @Override
    public Map<String, Object> userActivityRankList(Integer dayNum) {
        Map<String,Object> result  = new HashMap<>();
        List<Map<Object,Object> > logCountByCom = statisticInfoMapper.logCountByCom(dayNum);
        Map<Object,Object> cmpAndUserNum = statisticInfoMapper.getLoginCmpAndUserNum(dayNum);

        result.put("activityRankList",logCountByCom);
        result.put("cmpAndUserNum",cmpAndUserNum);
        return result;
    }




}
