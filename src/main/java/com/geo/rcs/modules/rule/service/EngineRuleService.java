package com.geo.rcs.modules.rule.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月03日 下午5:16
 */
public interface EngineRuleService {

    Page<EngineRule> findByPage(EngineRule engineRule) throws ServiceException;

    EngineRule save(EngineRule engineRule) throws ServiceException;

    boolean usernameUnique(String name) throws ServiceException;

    void updateEngineRule(EngineRule engineRule) throws ServiceException;

    void delete(Long id) throws ServiceException;

    EngineRule getEngineRuleById(Long id) throws ServiceException;

    void updateRuleVerify(Approval approval);

    void saveBySelect(EngineRule engineRule);

    void updateRuleSelect(PatchData patchData);

    EngineRule selectByName(String name);

    void addConditionsRs(EngineRule engineRule) throws ServiceException;

    List<EngineRule> getAllRuleName();

    FieldType selectByFieldTypeId(Integer fieldTypeId);

    EngineRawField selectById(Long id);

    EngineRule getRuleAndConInfo(Long id)  throws ServiceException;

    List<EngineRules> selectRulesById(Long id);

    EngineRules selectRulesByRuleId(Long id);

    void deleteByRulesId(Long id);

    void copyRuleById(Long id) throws ServiceException;
}
