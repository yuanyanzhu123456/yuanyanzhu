package com.geo.rcs.modules.rule.ruleset.service;

import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:49
 */
public interface RuleSetService {
    Page<EngineRules> findByPage(EngineRules ruleSet) throws ServiceException;

    EngineRules findAllById(Long id,boolean active);

    EngineRules findAllByRedis(Long id);

    EngineRules findAllByIdForTest(Long id);

    EngineRules selectById(Long id) throws RcsException;

    void delete(Long id) throws ServiceException;

    void updateEngineRules(EngineRules engineRules) throws ServiceException;

    EngineRules addEngineRules(EngineRules engineRules) throws ServiceException;

    List<EngineRules> getRulesList(Long userId)  throws ServiceException;

    void updateEngineRulesVerify(Approval approval);

    void updateRulesSelect(PatchData patchData);

    EngineRules getRuleSetAndRuleInfo(Long id) throws ServiceException;

    void updateEngineRulesNo(EngineRules engineRules);

    Page<EngineRules> findAllByPage(EngineRules ruleSet) throws ServiceException;

    void deleteAbsolute(Long id);

    EngineRules findAllByIdForDelete(Long id);

    EngineHistoryLog findAllByIdFromHistory(Approval approval);

    EngineRules findAllByIdForView(Long id);

    EngineRules reviewEngineRuleSet(EngineRules engineRules) throws RcsException;

    List<EngineRules> getActiveRules(Long uniqueCode);

    List<EngineRules> selectByName(String name,Long uniqueCode) throws RcsException;

    List<Map<String,Object>> getApiEventData();
}
