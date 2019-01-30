package com.geo.rcs.modules.rule.ruleset.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.condition.service.ConditionService;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:50
 */
@Service
public class RuleSetServiceImpl implements RuleSetService {

    static final String  DATASOURCE_TABLE = "RCS24_RULESETCACHE";

    @Value("${geo.redis.open}")
    private  boolean redisSwitch;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private EngineInterService engineInterService;
    @Autowired
    private EngineRuleService engineRuleService;
    @Autowired
    private ConditionService conditionService;
    @Autowired
    private FieldService fieldService;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Page<EngineRules> findByPage(EngineRules ruleSet) {
        PageHelper.startPage(ruleSet.getPageNo(), ruleSet.getPageSize());
        return engineRulesMapper.findByPage(ruleSet);
    }


    @Override
    public EngineRules findAllById(Long id,boolean active) {
        //如果从redis取到规则集，直接返回
        EngineRules engineRules = null;
        try{
            engineRules = findAllByRedis(id);
        }
        catch (Exception e){
            LogUtil.error("redis查找规则集",id.toString(),"",e);
        }
        if( engineRules == null ) {
            //从mysql获取
            engineRules = engineRulesMapper.findRulesById(id);
            if(engineRules == null) {
                throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
            }
            List<EngineRule> ruleList = engineRulesMapper.findRuleByRulesId(id);
            List<Conditions> conditionsList = engineRulesMapper.findConditionByRulesId(id);
            List<EngineField> fieldList = engineRulesMapper.findFieldByRulesId(id);

            try {
                if(ruleList.isEmpty() || conditionsList.isEmpty() || fieldList.isEmpty()){
                    throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
                }

                for (EngineRule rule : ruleList) {
                    if(rule.getConditionRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    List<Conditions> conditions = new ArrayList<>();
                    for (Conditions condition : conditionsList) {
                        if(condition.getRuleId().longValue() == rule.getId()){
                            conditions.add(condition);
                        }
                    }
                    rule.setConditionsList(conditions);
                }

                for (Conditions conditions : conditionsList) {
                    if(conditions.getFieldRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    List<EngineField> fields = new ArrayList<>();
                    for (EngineField field : fieldList) {
                        if(field.getConditionId().longValue() == conditions.getId()){
                            fields.add(field);
                        }
                    }
                    conditions.setFieldList(fields);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
            }

            engineRules.setRuleList(ruleList);

            if(StringUtils.isEmpty(engineRules.getParameters())){
                String params = engineInterService.getInterParams(id);
                engineRules.setParameters(params);
                engineRulesMapper.updateParams(id, params);
            }
            saveInRedis(engineRules);
        } else {
            if(engineRules.getRuleList()!=null && !engineRules.getRuleList().isEmpty()){
                List<EngineRule> activeRules = new ArrayList<>();
                int activeConditionsCount = 0;
                int activeFieldsCount = 0;
                for(EngineRule engineRule : engineRules.getRuleList()){
                    if(engineRule.getActive() == 0) continue;
                    if(engineRule.getConditionRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    List<Conditions> activeConditions = new ArrayList<>();
                    for(Conditions conditions : engineRule.getConditionsList()){
                        if(conditions.getActive() == 0) continue;
                        if(conditions.getFieldRelationship() == null){
                            throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                        }
                        List<EngineField> activeFields = new ArrayList<>();
                        for(EngineField engineField : conditions.getFieldList()){
                            if(engineField.getActive() == 0) continue;
                            activeFields.add(engineField);
                            activeFieldsCount++;
                        }
                        conditions.setFieldList(activeFields);
                        activeConditions.add(conditions);
                        activeConditionsCount++;
                    }
                    engineRule.setConditionsList(activeConditions);
                    activeRules.add(engineRule);
                }
                engineRules.setRuleList(activeRules);
                if(engineRules.getRuleList().isEmpty() || activeConditionsCount==0 || activeFieldsCount==0){
                    throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
                }
            }
        }

        if(active&&engineRules.getActive()==0){
            throw new RcsException("规则集未激活", StatusCode.RULE_INIT_ERROR.getCode());
        }

        return engineRules;
    }

    @Override
    @SuppressWarnings("unchecked")
    public EngineRules findAllByRedis(Long id) {
        EngineRules engineRules = engineRulesMapper.findRulesById(id);
        return  !redisSwitch?null:(EngineRules) redisTemplate.execute((RedisCallback<EngineRules>) connection -> {
            byte[] serialize = redisTemplate.getStringSerializer().serialize(DATASOURCE_TABLE+engineRules.getName()+engineRules.getId());
            byte[] b = connection.get(serialize);
            Object o = redisTemplate.getValueSerializer().deserialize(b);
            JSONObject s = (JSONObject) JSONObject.toJSON(o);
            return JSONObject.parseObject(s.toJSONString(),EngineRules.class);
        });
    }

    @Override
    public EngineRules findAllByIdForTest(Long id) {
        return findAllById(id,false);
    }


    @Override
    public EngineRules selectById(Long id) {
        return engineRulesMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long id) {
        engineRulesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateEngineRules(EngineRules engineRules) {
        engineRules.setVerify(0);
        engineRulesMapper.updateByPrimaryKeySelective(engineRules);
        //更新规则集参数
        String params = engineInterService.getInterParams(engineRules.getId());
        engineRulesMapper.updateParams(engineRules.getId(), params);
        saveInRedis(engineRules);


    }

    @SuppressWarnings("unchecked")
    private void saveInRedis(EngineRules engineRules){
        try{
            if (redisSwitch) {
                EngineRules allByIdForView = findAllByIdForView(engineRules.getId());
                //修改完成后更新缓存
                redisTemplate.execute((RedisCallback<EngineRules>) connection -> {
                    byte[] serialize = redisTemplate.getStringSerializer().serialize(DATASOURCE_TABLE + allByIdForView.getName() + allByIdForView.getId());
                    byte[] serialize2 = redisTemplate.getValueSerializer().serialize(allByIdForView);
                    connection.set(serialize, serialize2, Expiration.seconds(86400), RedisStringCommands.SetOption.UPSERT);
                    return null;
                });
            }
        }
        catch (Exception e) {
            //TODO: redis连接异常时处理，推送邮件，打印日志等等
            e.printStackTrace();
        }
    }

    @Override
    public EngineRules addEngineRules(EngineRules engineRules) {
        engineRulesMapper.insertSelective(engineRules);
        return engineRules;
    }

    @Override
    public List<EngineRules> getRulesList(Long userId) {
        return engineRulesMapper.getRulesList(userId);
    }

    @Override
    @Transactional
    public void updateEngineRulesVerify(Approval approval) {
        EngineRules engineRules = new EngineRules();
        engineRules.setVerify(1);
        engineRules.setId(approval.getOnlyId());
        engineRulesMapper.updateEngineRulesVerify(engineRules);
    }

    @Override
    public void updateRulesSelect(PatchData patchData) {
        engineRulesMapper.updateRulesSelect(patchData);
    }

   /* @Override
    public EngineRules selectByName(String name) {
        return engineRulesMapper.selectByName(name);
    }*/

    @Override
    public EngineRules getRuleSetAndRuleInfo(Long id) {
        return engineRulesMapper.getRuleSetAndRuleInfo(id);
    }

    @Override
    public void updateEngineRulesNo(EngineRules engineRules) {
        engineRulesMapper.updateByPrimaryKeySelective(engineRules);
        saveInRedis(engineRules);
    }

    @Override
    public Page<EngineRules> findAllByPage(EngineRules ruleSet) {
        PageHelper.startPage(ruleSet.getPageNo(), ruleSet.getPageSize());
        return engineRulesMapper.findAllByPage(ruleSet);
    }

    @Override
    @Transactional
    public void deleteAbsolute(Long id) {
        engineRulesMapper.deleteByPrimaryKey(id);
        engineRuleService.deleteByRulesId(id);
        conditionService.deleteByRulesId(id);
        fieldService.deleteByRulesId(id);
    }

    @Override
    @SuppressWarnings("all")
    public EngineRules findAllByIdForDelete(Long id) {
        EngineRules engineRules = engineRulesMapper.findRulesById(id);
        if(engineRules == null){
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        if(StringUtils.isEmpty(engineRules.getParameters())){
            String params = engineInterService.getInterParams(id);
            engineRules.setParameters(params);
            engineRulesMapper.updateParams(id, params);
        }

        List<EngineRule> ruleList = engineRulesMapper.findRuleByRulesId(id);
        List<Conditions> conditionsList = engineRulesMapper.findConditionByRulesId(id);
        List<EngineField> fieldList = engineRulesMapper.findFieldByRulesId(id);

        try {
            if(!ruleList.isEmpty()){
                for (EngineRule rule : ruleList) {
                    List<Conditions> conditions = new ArrayList<>();
                    for (Conditions condition : conditionsList) {
                        if(condition.getRuleId().longValue() == rule.getId()){
                            conditions.add(condition);
                        }
                    }
                    rule.setConditionsList(conditions);
                }
                if(!conditionsList.isEmpty()){
                    for (Conditions conditions : conditionsList) {
                        List<EngineField> fields = new ArrayList<>();
                        if(!fieldList.isEmpty()){
                            for (EngineField field : fieldList) {
                                if(field.getConditionId().longValue() == conditions.getId()){
                                    fields.add(field);
                                }
                            }
                            conditions.setFieldList(fields);
                        }
                    }
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
        }

        engineRules.setRuleList(ruleList);
        return engineRules;
    }

    @Override
    public EngineHistoryLog findAllByIdFromHistory(Approval approval) {
        return engineRulesMapper.findAllByIdFromHistory(approval);
    }

    @Override
    @SuppressWarnings("all")
    public EngineRules findAllByIdForView(Long id) {
        EngineRules engineRules = engineRulesMapper.findRulesById(id);
        if(engineRules == null){
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        if(StringUtils.isEmpty(engineRules.getParameters())){
            String params = engineInterService.getInterParams(id);
            engineRules.setParameters(params);
            engineRulesMapper.updateParams(id, params);
        }

        List<EngineRule> ruleList = engineRulesMapper.findRuleByRulesIdForView(id);
        List<Conditions> conditionsList = engineRulesMapper.findConditionByRulesIdForView(id);
        List<EngineField> fieldList = engineRulesMapper.findFieldByRulesIdForView(id);

        try {

            if(ruleList != null){
                for (EngineRule rule : ruleList) {
                    List<Conditions> conditions = new ArrayList<>();
                    for (Conditions condition : conditionsList) {
                        if(condition.getRuleId().longValue() == rule.getId()){
                            conditions.add(condition);
                        }
                    }
                    rule.setConditionsList(conditions);
                }
            }

            if(conditionsList != null) {

                for (Conditions conditions : conditionsList) {
                    List<EngineField> fields = new ArrayList<>();
                    for (EngineField field : fieldList) {
                        if (field.getConditionId() != null && field.getConditionId().longValue() == conditions.getId()) {
                            fields.add(field);
                        }
                    }
                    conditions.setFieldList(fields);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
        }

        engineRules.setRuleList(ruleList);

        return engineRules;
    }

    @Override
    public EngineRules reviewEngineRuleSet(EngineRules engineRules){

        if(engineRules == null){
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
        if(StringUtils.isEmpty(engineRules.getParameters())){
            String params = engineInterService.getInterParams(engineRules.getId());
            engineRules.setParameters(params);
            engineRulesMapper.updateParams(engineRules.getId(), params);
        }

        if(engineRules.getRuleList() == null || engineRules.getRuleList().isEmpty()){
            List<EngineRule> ruleList = engineRulesMapper.findRuleByRulesId(engineRules.getId());
            List<Conditions> conditionsList = engineRulesMapper.findConditionByRulesId(engineRules.getId());
            List<EngineField> fieldList = engineRulesMapper.findFieldByRulesId(engineRules.getId());

            try {
                if(ruleList.isEmpty() || conditionsList.isEmpty() || fieldList.isEmpty()){
                    throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
                }


                for (EngineRule rule : ruleList) {
                    if(rule.getConditionRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    List<Conditions> conditions = new ArrayList<>();
                    for (Conditions condition : conditionsList) {
                        if(condition.getRuleId().longValue() == rule.getId()){
                            conditions.add(condition);
                        }
                    }
                    rule.setConditionsList(conditions);
                }

                for (Conditions conditions : conditionsList) {
                    List<EngineField> fields = new ArrayList<>();
                    if(conditions.getFieldRelationship() == null){
                        throw new RcsException(StatusCode.RULE_CON_ERROR.getMessage(), StatusCode.RULE_CON_ERROR.getCode());
                    }
                    for (EngineField field : fieldList) {
                        if(field.getConditionId() != null && field.getConditionId().longValue() == conditions.getId()){
                            fields.add(field);
                        }
                    }
                    conditions.setFieldList(fields);
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new RcsException(StatusCode.RULE_INIT_ERROR.getMessage(), StatusCode.RULE_INIT_ERROR.getCode());
            }
            engineRules.setRuleList(ruleList);
            saveInRedis(engineRules);
        }
        return engineRules;
    }

    @Override
    public List<EngineRules> getActiveRules(Long uniqueCode) {
        return engineRulesMapper.getActiveRules(uniqueCode);
    }

    @Override
    public List<EngineRules> selectByName(String name,Long uniqueCode) throws RcsException {
        if (BlankUtil.isBlank(name) || BlankUtil.isBlank(uniqueCode)){
            return null;
        }
        return engineRulesMapper.selectByName(name,uniqueCode);
    }

    @Override
    public List<Map<String,Object>> getApiEventData() {
        return engineRulesMapper.getApiEventData();
    }


}
