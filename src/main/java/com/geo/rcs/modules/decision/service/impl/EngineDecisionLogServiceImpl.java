package com.geo.rcs.modules.decision.service.impl;

import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.modules.decision.dao.EngineDecisionLogMapper;
import com.geo.rcs.modules.decision.entity.DecisionAnalyse;
import com.geo.rcs.modules.decision.entity.EngineDecisionLog;
import com.geo.rcs.modules.decision.entity.UserDecision;
import com.geo.rcs.modules.decision.service.EngineDecisionLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月04日 下午6:31
 */
@Service
public class EngineDecisionLogServiceImpl implements EngineDecisionLogService {

    @Autowired
    private EngineDecisionLogMapper engineDecisionLogMapper;

    @Override
    public Page<EngineDecisionLog> findByPage(EngineDecisionLog engineDecisionLog) {
        PageHelper.startPage(engineDecisionLog.getPageNo(), engineDecisionLog.getPageSize());
        return engineDecisionLogMapper.findByPage(engineDecisionLog);
    }

    @Override
    public Page<Map<String, Object>> findByPageAll(EngineDecisionLog engineDecisionLog) {
        return engineDecisionLogMapper.findByPageAll(engineDecisionLog);
    }

    @Override
    public Page<Map<String, Object>> findByPageIds(Long[] ids) {
        return engineDecisionLogMapper.findByPageIds(ids);
    }

    @Override
    public List<Map<String,Object> > findAllPdfData(EngineDecisionLog engineDecisionLog) {
        return engineDecisionLogMapper.findAllPdfDataList(engineDecisionLog);
    }

    @Override
    public Map<String, Object> selectBySelective(Map<String, Object> map) {
        DecisionAnalyse decisionAnalyse = engineDecisionLogMapper.getEventCountBySelective(map);
        map.put("data", decisionAnalyse);
        map.put("updateTime", DateUtils.format(new Date(),"yyyy.MM.dd HH:mm:ss"));
        return map;
    }

    @Override
    public Map<String, Object> getEventCountTrend(Map<String, Object> map) {
        List<Map<String, Object>> countTrend = new ArrayList<>();
        switch (map.get("type").toString()) {
            case "HOUR":
                countTrend = engineDecisionLogMapper.getCountTrendByHour(map);
                break;
            case "DAY":
                countTrend = engineDecisionLogMapper.getCountTrendByDay(map);
                break;
            case "MONTH":
                countTrend = engineDecisionLogMapper.getCountTrendByMonth(map);
                break;
        }

        map.clear();
        map.put("countTrend", countTrend);
        return map;
    }

    @Override
    public List<Map<String, Object>> getEventCostTrend(Map<String, Object> map) {
        return engineDecisionLogMapper.getEventCostTrend(map);
    }

    @Override
    public List<Map<String, Object>> getEventScoreTrend(Map<String, Object> map) {
        return engineDecisionLogMapper.getEventScoreTrend(map);
    }

    @Override
    public Map<String, Object> getEventStatusTrend(Map<String, Object> map) {
        List<Map<String, Object>> passCountTrend = new ArrayList<>();
        List<Map<String, Object>> manualCountTrend = new ArrayList<>();
        List<Map<String, Object>> refuseCountTrend = new ArrayList<>();
        List<Map<String, Object>> invalidCountTrend = new ArrayList<>();
        List<Map<String, Object>> failCountTrend = new ArrayList<>();
        if(map.get("sysStatus") != null && !map.get("sysStatus").toString().equals("")){
            if(map.get("sysStatus").toString().equals("1")){
                passCountTrend = engineDecisionLogMapper.getPassCountTrendByType(map);
                map.clear();
                map.put("passCountTrend", passCountTrend);
            }else if(map.get("sysStatus").toString().equals("2")){
                manualCountTrend = engineDecisionLogMapper.getManualCountTrendByType(map);
                map.clear();
                map.put("manualCountTrend", manualCountTrend);
            }
            else if(map.get("sysStatus").toString().equals("3")){
                refuseCountTrend = engineDecisionLogMapper.getRefuseCountTrendByType(map);
                map.clear();
                map.put("refuseCountTrend", refuseCountTrend);
            }
            else if(map.get("sysStatus").toString().equals("4")){
                invalidCountTrend = engineDecisionLogMapper.getInvalidCountTrendByType(map);
                map.clear();
                map.put("invalidCountTrend", invalidCountTrend);
            }
            else if(map.get("sysStatus").toString().equals("5")){
                failCountTrend = engineDecisionLogMapper.getFailCountTrendByType(map);
                map.clear();
                map.put("failCountTrend", failCountTrend);
            }
        }else{
            passCountTrend = engineDecisionLogMapper.getPassCountTrendByType(map);
            manualCountTrend = engineDecisionLogMapper.getManualCountTrendByType(map);
            refuseCountTrend = engineDecisionLogMapper.getRefuseCountTrendByType(map);
            invalidCountTrend = engineDecisionLogMapper.getInvalidCountTrendByType(map);
            failCountTrend = engineDecisionLogMapper.getFailCountTrendByType(map);

            map.clear();
            map.put("passCountTrend", passCountTrend);
            map.put("manualCountTrend", manualCountTrend);
            map.put("refuseCountTrend", refuseCountTrend);
            map.put("invalidCountTrend", invalidCountTrend);
            map.put("failCountTrend", failCountTrend);
        }
        return map;
    }

    @Override
    public EngineDecisionLog save(EngineDecisionLog engineDecisionLog) {
        engineDecisionLogMapper.save(engineDecisionLog);
        return engineDecisionLog;
    }

    @Override
    public EngineDecisionLog selectByPrimaryKey(Long id) {
        return engineDecisionLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveToUserModel(UserDecision userDecision) {
        engineDecisionLogMapper.saveToUserModel(userDecision);
    }
}
