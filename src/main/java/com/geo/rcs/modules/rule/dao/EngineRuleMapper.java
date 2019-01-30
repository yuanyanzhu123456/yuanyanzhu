package com.geo.rcs.modules.rule.dao;

import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "engineRuleMapper")
public interface EngineRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EngineRule record);

    Long insertSelective(EngineRule record);

    EngineRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EngineRule record);

    int updateByPrimaryKey(EngineRule record);

    Page<EngineRule> findByPage(EngineRule engineRule);

    EngineRule queryByName(String name);

    void updateEngineRuleVerify(EngineRule engineRule);

    void updateRuleSelect(PatchData patchData);

    void addConditionsRs(EngineRule engineRule);

    List<EngineRule> getAllRuleName();

    FieldType selectByFieldTypeId(Integer fieldTypeId);

    EngineRawField selectById(Long fieldId);

    EngineRule getRuleAndConInfo(Long id);

    List<EngineRules> selectRulesById(Long id);

    EngineRules selectRulesByRuleId(Long id);

    void deleteByRulesId(Long id);

    EngineRule getRuleInfoById(Long id);
}