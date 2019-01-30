package com.geo.rcs.modules.rule.condition.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.condition.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月05日 上午10:20
 */
public interface ConditionService {

    Page<Conditions> findByPage(Conditions conditions) throws ServiceException;

    void delete(Long id)  throws ServiceException;

    boolean usernameUnique(Conditions conditions);

    Conditions save(Conditions conditions) throws ServiceException;

    Conditions queryByName(Conditions conditions) throws ServiceException;

    EngineRule selectById(Conditions conditions);

    void addFieldRs(Conditions conditions) throws ServiceException;

    Conditions updateConditionById(Conditions conditions) throws ServiceException;

    List<Conditions> getConditionAndFieldById(Long id);

    EngineRules queryEngineRulesActive(Long id);

    Conditions getConAndFieldInfo(Long id) throws ServiceException;

    EngineRules selectRulesActiveByRuleId(Long id);

    Conditions selectByPrimaryKey(Long id);

    Conditions saveNoUp(Conditions conditions);

    void deleteByRulesId(Long id);

    void copyConditionById(Long id) throws ServiceException;

    Conditions insertSelective(Conditions conditions);

    void deleteByRuleId(Long id);
}
