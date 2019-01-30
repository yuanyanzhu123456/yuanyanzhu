package com.geo.rcs.modules.decision.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.ArraysUtil;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.decision.dao.EngineDecisionMapper;
import com.geo.rcs.modules.decision.entity.Decision;
import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.decision.service.EngineDecisionService;
import com.geo.rcs.modules.rule.inter.dao.EngineInterMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月03日 上午11:21
 */
@Service
public class EngineDecisionServiceImpl implements EngineDecisionService {

    @Autowired
    private EngineDecisionMapper engineDecisionMapper;
    @Autowired
    private EngineInterMapper engineInterMapper;

    @Override
    public EngineDecision getEngineDecisionByUserId(Long userId) {
        return engineDecisionMapper.getEngineDecisionByUserId(userId);
    }

    @Override
    public EngineDecision selectByPrimaryKey(Integer id) {
        return engineDecisionMapper.selectByPrimaryKey(id);
    }

    @Override
    public EngineDecision save(EngineDecision engineDecision) {

        engineDecision.setCreateTime(TimeUtil.dqsj());
        engineDecisionMapper.save(engineDecision);
        return engineDecision;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EngineDecision engineDecision) {
        List<Decision> list = JSONUtil.jsonToBean(engineDecision.getDecisionFlow(), List.class);
        Map<String,Object> map = new HashMap<>();

        if(list != null && list.size() > 1){
            for (int i =1;i<list.size();i++) {
                Decision decision1 = JSON.parseObject(JSON.toJSONString(list.get(i)), Decision.class);
                List<String> interParams = engineInterMapper.getInterParams(decision1.getRulesId());
                List<Map<String,Object>> mapList = JSONUtil.jsonToBean(JSONUtil.beanToJson(interParams),List.class);
                for (Map<String,Object> map1:mapList) {
                    if(map1 != null){
                        map.putAll(map1);
                    }
                }

            }
            //进件所需参数去重
            engineDecision.setParameters(JSONUtil.beanToJson(map));

        }
        engineDecision.setUpdateTime(TimeUtil.dqsj());
        engineDecisionMapper.update(engineDecision);
    }

    @Override
    public Page<EngineDecision> findByPage(Map<String, Object> map) {
        if(ValidateNull.isNull(map.get("order"))){
            map.put("order","desc");
        }
        PageHelper.startPage((int)map.get("pageNo"), (int)map.get("pageSize"));
        Page<EngineDecision> engineDecisions = engineDecisionMapper.findByPage(map);
        return engineDecisions;
    }

    @Override
    public void deleteByPrimaryKey(Integer id) {
        engineDecisionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<EngineDecision> getDecisionByUserId(Long uniqueCode) {
        return engineDecisionMapper.getDecisionByUserId(uniqueCode);
    }

    @Override
    public int getDecisionTotal(Map<String, Object> map) {
        return engineDecisionMapper.getDecisionTotal(map);
    }

    @Override
    public List<EngineDecision> verifyName(String name,Long uniqueCode,Integer businessId) {

        if (BlankUtil.isBlank(name) && BlankUtil.isBlank(uniqueCode) && BlankUtil.isBlank(businessId)){
            return null;
        }
        return engineDecisionMapper.verifyName(name,uniqueCode,businessId);
    }

    @Override
    public String selectNameById(Integer id) {
        return engineDecisionMapper.selectNameById(id);
    }

    @Override
    public List<Integer> selectBusinessIdById(Integer businessId) {
        return engineDecisionMapper.selectBusinessIdById(businessId);
    }



    @Override
    public List<EngineDecision> getDecisionByName(String name) {
        return engineDecisionMapper.getDecisionByName(name);
    }

    @Override
    public String[] getUsageRuleSet(Long uniqueCode) {
        List<String> usageRuleSet = engineDecisionMapper.getUsageRuleSet(uniqueCode);
        if(usageRuleSet.size() > 0){
            String[] strings = ArraysUtil.arrayListToArray(usageRuleSet);
            return strings;
        }
       return null;
    }

}
