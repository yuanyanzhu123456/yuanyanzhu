package com.geo.rcs.modules.rule.ruleset.dao;

import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "engineRulesMapper")
public interface EngineRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EngineRules record);

    Long insertSelective(EngineRules record);

    EngineRules selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EngineRules record);

    int updateByPrimaryKey(EngineRules record);

    Page<EngineRules> findByPage(EngineRules ruleSet);

    List<EngineRules> findById(Long id);

    EngineRules findRulesById(Long id);

    List<EngineRule> findRuleByRulesId(Long id);

    List<Conditions> findConditionByRulesId(Long id);

    List<EngineField> findFieldByRulesId(Long id);

    List<EngineRules> getRulesList(Long userId);

    void updateEngineRulesVerify(EngineRules engineRules);

    void updateRulesSelect(PatchData patchData);

    List<EngineRules> selectByName(@Param("name") String name,@Param("uniqueCode") Long uniqueCode);

    int updateParams(@Param("id") Long id, @Param("parameters") String parameters);

    EngineRules getRuleSetAndRuleInfo(Long id);

    Page<EngineRules> findAllByPage(EngineRules ruleSet);

    EngineHistoryLog findAllByIdFromHistory(Approval approval);

    List<EngineRule> findRuleByRulesIdForView(Long id);

    List<Conditions> findConditionByRulesIdForView(Long id);

    List<EngineField> findFieldByRulesIdForView(Long id);

    List<EngineRules> getActiveRules(Long uniqueCode);

    List<Map<String, Object>>  getRulesStatistic();
    List<Map<String, Object>>  getRulesAnalysisDay(Map<String, String> parmMap);
    List<Map<String, Object>>  getRulesAnalysisMonth(Map<String, String> parmMap);

    List<Map<String,Object>> getApiEventData();
}