package com.geo.rcs.modules.rule.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.condition.dao.ConditionsMapper;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.dao.EngineRuleMapper;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.dao.EngineFieldMapper;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月03日 下午5:17
 */
@Service
public class EngineRuleServiceImpl implements EngineRuleService {

    @Autowired
    private EngineRuleMapper engineRuleMapper;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private ConditionsMapper conditionsMapper;
    @Autowired
    private EngineFieldMapper engineFieldMapper;

    @Override
    public Page<EngineRule> findByPage(EngineRule engineRule) {
        PageHelper.startPage(engineRule.getPageNo(), engineRule.getPageSize());
        return engineRuleMapper.findByPage(engineRule);
    }

    @Override
    public EngineRule save(EngineRule engineRule) {
        engineRuleMapper.insertSelective(engineRule);
        EngineRules engineRules = new EngineRules();
        engineRules.setId(engineRule.getRulesId());
        engineRules.setVerify(0);
        engineRulesMapper.updateByPrimaryKeySelective(engineRules);
        return engineRule;
    }

    @Override
    public boolean usernameUnique(String name) {
        if (BlankUtil.isBlank(name))
            return false;
        EngineRule engineRule = engineRuleMapper.queryByName(name);
        return engineRule == null;
    }

    @Override
    public void updateEngineRule(EngineRule engineRule) {

        engineRuleMapper.updateByPrimaryKeySelective(engineRule);
    }

    @Override
    public void delete(Long id) {
        engineRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public EngineRule getEngineRuleById(Long id) {
        return engineRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateRuleVerify(Approval approval) {
        EngineRule engineRule = new EngineRule();
        engineRule.setVerify(1);
        engineRule.setId(approval.getOnlyId());
        engineRuleMapper.updateEngineRuleVerify(engineRule);
    }

    @Override
    public void saveBySelect(EngineRule engineRule) {
        engineRuleMapper.insertSelective(engineRule);
    }

    @Override
    public void updateRuleSelect(PatchData patchData) {
        engineRuleMapper.updateRuleSelect(patchData);
    }

    @Override
    public EngineRule selectByName(String name) {
        return engineRuleMapper.queryByName(name);
    }

    @Override
    public void addConditionsRs(EngineRule engineRule) {
        engineRuleMapper.addConditionsRs(engineRule);
    }

    @Override
    public List<EngineRule> getAllRuleName() {
        return engineRuleMapper.getAllRuleName();
    }

    @Override
    public FieldType selectByFieldTypeId(Integer fieldTypeId) {
        return engineRuleMapper.selectByFieldTypeId(fieldTypeId);
    }

    @Override
    public EngineRawField selectById(Long fieldId) {
        return engineRuleMapper.selectById(fieldId);
    }

    @Override
    public EngineRule getRuleAndConInfo(Long id) throws ServiceException {
        return engineRuleMapper.getRuleAndConInfo(id);
    }

    @Override
    public List<EngineRules> selectRulesById(Long id) {
        return engineRuleMapper.selectRulesById(id);
    }

    @Override
    public EngineRules selectRulesByRuleId(Long id) {
        return engineRuleMapper.selectRulesByRuleId(id);
    }

    @Override
    public void deleteByRulesId(Long id) {
        engineRuleMapper.deleteByRulesId(id);
    }

    @Override
    @Transactional
    public void copyRuleById(Long id) throws ServiceException {
        EngineRule ruleAndConInfo = engineRuleMapper.getRuleAndConInfo(id);

        if (ruleAndConInfo == null) {
            throw new ServiceException("拷贝内容为空");
        }

        Long aLong = engineRuleMapper.insertSelective(ruleAndConInfo);
        if(ruleAndConInfo.getConditionsList() != null){
            for(Conditions condition : ruleAndConInfo.getConditionsList()){
                conditionsMapper.insertSelective(condition);
                if(engineFieldMapper.selectFieldForCon(condition.getId()) != null){
                    for(EngineField engineField : engineFieldMapper.selectFieldForCon(condition.getId()) ){
                        engineFieldMapper.insertSelectiveEn(engineField);
                    }
                }
            }
        }
        }

}
