package com.geo.rcs.modules.rule.condition.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.modules.rule.condition.dao.ConditionsMapper;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.condition.service.ConditionService;
import com.geo.rcs.modules.rule.dao.EngineRuleMapper;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.dao.EngineFieldMapper;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.condition.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月05日 上午10:21
 */
@Service
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    private ConditionsMapper conditionsMapper;

    @Autowired
    private EngineRuleMapper engineRuleMapper;

    @Autowired
    private EngineRulesMapper engineRulesMapper;

    @Autowired
    private EngineFieldMapper engineFieldMapper;

    @Autowired
    private EngineInterService engineInterService;

    @Override
    public Page<Conditions> findByPage(Conditions conditions) {
        PageHelper.startPage(conditions.getPageNo(), conditions.getPageSize());
        return conditionsMapper.findByPage(conditions);
    }

    @Override
    public void delete(Long id) {
        Conditions conditions = new Conditions();
        conditions.setId(id);
        EngineRule engineRule = conditionsMapper.selectEngineByCid(conditions.getId());
        conditionsMapper.deleteByPrimaryKey(id);
        engineRule.setConditionNumber(engineRule.getConditionNumber()-1);
        engineRuleMapper.updateByPrimaryKeySelective(engineRule);
    }

    @Override
    public boolean usernameUnique(Conditions conditions) {
        if (BlankUtil.isBlank(conditions))
            return false;
        Conditions condition = conditionsMapper.queryByName(conditions);
        return condition == null;
    }

    @Override
    @Transactional
    public Conditions save(Conditions conditions) {
        conditions.setAddTime(new Date());
        EngineRule engineRule = selectById(conditions);
        conditions.setRulesId(engineRule.getRulesId());
        if(conditions.getId() == null){
            conditionsMapper.insertSelective(conditions);
            engineRule.setConditionNumber(engineRule.getConditionNumber()+1);
            engineRuleMapper.updateByPrimaryKeySelective(engineRule);
        }
        else{
            conditionsMapper.updateByPrimaryKeySelective(conditions);
        }

        return conditions;
    }

    @Override
    public Conditions insertSelective(Conditions conditions){
        conditionsMapper.insertSelective(conditions);
        return conditions;

    }

    @Override
    public void deleteByRuleId(Long id) {
        conditionsMapper.deleteByRuleId(id);
    }


    @Override
    public Conditions queryByName(Conditions conditions) {
        return conditionsMapper.queryByName(conditions);
    }

    @Override
    public EngineRule selectById(Conditions conditions) {
        return engineRuleMapper.selectByPrimaryKey(conditions.getRuleId());
    }

    @Override
    public void addFieldRs(Conditions conditions) {
        conditionsMapper.addFieldRs(conditions);
        EngineRules engineRules1 = conditionsMapper.queryEngineRulesActive(conditions.getId());
        //更新规则集参数
        String params = engineInterService.getInterParams(engineRules1.getId());
        engineRulesMapper.updateParams(engineRules1.getId(), params);
    }

    @Override
    public Conditions updateConditionById(Conditions conditions) {
        conditions.setAddTime(new Date());
        Conditions conditions1 = conditionsMapper.selectByPrimaryKey(conditions.getId());
        conditions.setRulesId(conditions1.getRulesId());
        conditions.setRuleId(conditions1.getRuleId());
        conditionsMapper.updateByPrimaryKeySelective(conditions);
        return conditions;
    }

    @Override
    @Transactional
    public List<Conditions> getConditionAndFieldById(Long id){
        List list = new ArrayList();
        Conditions conditionAndFieldById = conditionsMapper.getConditionAndFieldById(id);
        List<EngineField> engineFields = engineFieldMapper.selectFieldForCon(conditionAndFieldById.getId());
        list.add(conditionAndFieldById);
        list.add(engineFields);
        return list;
    }

    @Override
    public EngineRules queryEngineRulesActive(Long id) {
        return conditionsMapper.queryEngineRulesActive(id);
    }

    @Override
    public Conditions getConAndFieldInfo(Long id){
        return conditionsMapper.getConAndFieldInfo(id);
    }

    @Override
    public EngineRules selectRulesActiveByRuleId(Long id) {
        return conditionsMapper.selectRulesActiveByRuleId(id);
    }

    @Override
    public Conditions selectByPrimaryKey(Long id) {
        return conditionsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Conditions saveNoUp(Conditions conditions) {
        conditions.setAddTime(new Date());
        EngineRule engineRule = selectById(conditions);
        conditions.setRulesId(engineRule.getRulesId());
        conditionsMapper.insertSelective(conditions);
        engineRule.setConditionNumber(engineRule.getConditionNumber()+1);
        engineRuleMapper.updateByPrimaryKeySelective(engineRule);
        return conditions;
    }

    @Override
    public void deleteByRulesId(Long id) {
        conditionsMapper.deleteByRulesId(id);
    }

    @Override
    public void copyConditionById(Long id) throws ServiceException {

        Conditions conAndFieldInfo = conditionsMapper.getConAndFieldInfo(id);
        if(conAndFieldInfo == null){
            throw new ServiceException("拷贝内容为空");
        }
        conditionsMapper.insertSelective(conAndFieldInfo);
        if(conAndFieldInfo.getFieldList() != null){
            for(EngineField engineField : conAndFieldInfo.getFieldList()){
                engineFieldMapper.insertSelectiveEn(engineField);
            }
        }

    }


}
