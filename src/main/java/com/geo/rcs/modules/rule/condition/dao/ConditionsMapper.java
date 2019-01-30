package com.geo.rcs.modules.rule.condition.dao;

import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "conditionsMapper")
public interface ConditionsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Conditions record);

    int insertSelective(Conditions record);

    Conditions selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Conditions record);

    int updateByPrimaryKey(Conditions record);

    List<Conditions> findById(Long id);

    Page<Conditions> findByPage(Conditions conditions);

    Conditions queryByName(Conditions conditions);

    void addFieldRs(Conditions conditions);

    Conditions getConditionAndFieldById(Long id);

    EngineRules queryEngineRulesActive(Long id);

    Conditions getConAndFieldInfo(Long id);

    EngineRules selectRulesActiveByRuleId(Long id);

    EngineRule selectEngineByCid(Long id);

    void deleteByRulesId(Long id);

    void deleteByRuleId(Long id);
}