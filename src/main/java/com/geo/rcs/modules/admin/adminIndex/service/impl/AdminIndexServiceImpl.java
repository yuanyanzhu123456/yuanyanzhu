package com.geo.rcs.modules.admin.adminIndex.service.impl;

import com.geo.rcs.modules.admin.adminIndex.service.AdminIndexService;
import com.geo.rcs.modules.event.dao.EventEntryMapper;
import com.geo.rcs.modules.geo.dao.GeoUserMapper;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.sys.dao.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 19:03 2018/8/27
 */
@Component
public class AdminIndexServiceImpl implements AdminIndexService {
    @Autowired
    private EventEntryMapper eventEntryMapper;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private GeoUserMapper geoUserMapper;

    @Override
    public Map<String, Map<String, Object>> getDateOvew() {
        HashMap<String, Map<String, Object>> resutMap = new HashMap<>();
        List<Map<String, Object>> entryStatistic = eventEntryMapper.getEntryStatistic();
        Map<String, Object> entryMap = entryStatistic.get(0);
        resutMap.put("events", entryMap);
        List<Map<String, Object>> rulesStatistic = engineRulesMapper.getRulesStatistic();
        Map<String, Object> rulesMap = rulesStatistic.get(0);
        resutMap.put("modles", rulesMap);

        Map<String, Object> customerMap = sysUserMapper.getCustomerStatistic().get(0);
        resutMap.put("customers", customerMap);

        Map<String, Object> userMap = geoUserMapper.getUserStatistics().get(0);
        resutMap.put("users", userMap);

        Map<String, Object> loginUserMap = geoUserMapper.getUserLoginStatistics().get(0);
        resutMap.put("activeUsers", loginUserMap);

        return resutMap;
    }

    @Override
    public Map<String, Object> dataOverviewTotal() {

        List<Map<String, Object>> entryStatistic = eventEntryMapper.getStatisticTotal();
        return entryStatistic.get(0);
    }

    @Override
    public List<Map<String, Object>> detailStatistics(Map<String, String> parmMap) {
        List<Map<String, Object>> resultList = null;
        if ("customer".equalsIgnoreCase(parmMap.get("type"))) {
            if ("week".equalsIgnoreCase(parmMap.get("date"))) {
                parmMap.put("num", "7");
                resultList = geoUserMapper.getActiveUsersDay(parmMap);
            } else if ("month".equalsIgnoreCase(parmMap.get("date"))) {
                parmMap.put("num", "30");
                resultList = geoUserMapper.getActiveUsersDay(parmMap);
            } else {
                parmMap.put("num", "365");
                resultList = geoUserMapper.getActiveUsersMonth(parmMap);
            }
        } else if ("model".equalsIgnoreCase(parmMap.get("type"))) {
            if ("week".equalsIgnoreCase(parmMap.get("date"))) {
                parmMap.put("num", "7");
                resultList = engineRulesMapper.getRulesAnalysisDay(parmMap);
            } else if ("month".equalsIgnoreCase(parmMap.get("date"))) {
                parmMap.put("num", "30");
                resultList = engineRulesMapper.getRulesAnalysisDay(parmMap);
            } else {
                parmMap.put("num", "365");
                resultList = engineRulesMapper.getRulesAnalysisMonth(parmMap);
            }
        } else if ("event".equalsIgnoreCase(parmMap.get("type"))) {
            if ("week".equalsIgnoreCase(parmMap.get("date"))) {
                parmMap.put("num", "7");
                resultList = eventEntryMapper.getEntryAnalysisDay(parmMap);
            } else if ("month".equalsIgnoreCase(parmMap.get("date"))) {
                parmMap.put("num", "30");
                resultList = eventEntryMapper.getEntryAnalysisDay(parmMap);
            } else if ("year".equalsIgnoreCase(parmMap.get("date")))  {
                parmMap.put("num", "365");
                resultList = eventEntryMapper.getEntryAnalysisMonth(parmMap);
            }else {
                parmMap.put("num", "1");
                resultList = eventEntryMapper.getEntryAnalysisHour(parmMap);
            }
        }
        return resultList;
    }
}
