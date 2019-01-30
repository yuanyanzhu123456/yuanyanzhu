package com.geo.rcs.modules.approval.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.approval.dao.ApprovalMapper;
import com.geo.rcs.modules.approval.dao.PatchDataMapper;
import com.geo.rcs.modules.approval.entity.ActionType;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.ObjectType;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.decision.dao.EngineDecisionMapper;
import com.geo.rcs.modules.decision.dao.DecisionHistoryMapper;
import com.geo.rcs.modules.decision.entity.DecisionHistoryLog;
import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.dao.RecordRulesLogMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.approval.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月29日 上午11:56
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApprovalMapper approvalMapper;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private EngineRuleService engineRuleService;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private PatchDataMapper patchDataMapper;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private RecordRulesLogMapper recordRulesLogMapper;
    @Autowired
    private EngineDecisionMapper engineDecisionMapper;
    @Autowired
    private DecisionHistoryMapper decisionHistoryMapper;

    private EngineRules allById;
    @Override
    public Page<Approval> findByPage(Approval approval) {
        PageHelper.startPage(approval.getPageNo(), approval.getPageSize());
        return approvalMapper.findByPage(approval);
    }

    @Override
    public Approval getApprovalById(Long id) {
        return approvalMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加至审批列表
     * @param approval
     * @throws ServiceException
     */
    @Override
    @Transactional
    public void addToApproval(Approval approval) {
        approvalMapper.insertSelective(approval);
        if(approval.getObjId() != 5){
            ruleSetService.updateEngineRulesVerify(approval);
            List<EngineRule> ruleList = engineRulesMapper.findRuleByRulesIdForView(approval.getOnlyId());
            List<Conditions> conditionsList = engineRulesMapper.findConditionByRulesIdForView(approval.getOnlyId());
            List<EngineField> fieldList = engineRulesMapper.findFieldByRulesIdForView   (approval.getOnlyId());
            if(ruleList.isEmpty() || conditionsList.isEmpty() || fieldList.isEmpty()){
                throw new RcsException(StatusCode.RULE_APPR_ERROR.getMessage(), StatusCode.RULE_APPR_ERROR.getCode());
            }
        }
    }
    /**
     * 规则集审批
     * @param approval
     * @throws ServiceException
     */
    @Override
    public void updateAppStatus(Approval approval) throws ServiceException  {
        try {
            //记录规则集
            EngineHistoryLog engineHistoryLog = new EngineHistoryLog();
            if(approval.getActionType() != null && approval.getActionType() == 3){
                engineHistoryLog.setActionType(3);
            }
            else if(approval.getActionType() != null && approval.getActionType() == 4){
                engineHistoryLog.setActionType(4);
            }
            engineHistoryLog.setRecordTime(approval.getSubTime());
            engineHistoryLog.setUniqueCode(approval.getUniqueCode());
            if(approval.getActionType() == 3){
                allById = ruleSetService.findAllByIdForDelete(approval.getOnlyId());
            }
            else{
                allById = ruleSetService.findAllByIdForTest(approval.getOnlyId());
            }
            String rulesTemplate = JSONUtil.beanToJson(allById);
            engineHistoryLog.setRulesMap(rulesTemplate);
            engineHistoryLog.setRuleSetId(approval.getOnlyId());
            engineHistoryLog.setName(allById.getName());
            recordRulesLogMapper.insertBySelective(engineHistoryLog);
            approval.setRecordId(engineHistoryLog.getId());
            approvalMapper.updateAppStatus(approval);
            if(approval.getObjId() == 1){//场景
                if(approval.getActionType() == 2 && approval.getAppStatus() == 1){//修改
                    PatchData patchData = patchDataMapper.selectByOnlyId(approval);
                    sceneService.updateSceneSelect(patchData);
                    patchDataMapper.deleteByPrimaryKey(patchData.getId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getActionType() == 3 && approval.getAppStatus() == 1){//删除
                    sceneService.delete(approval.getOnlyId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
            }else if(approval.getObjId() == 2){//规则集
                if(approval.getActionType() == 1 && approval.getAppStatus() == 1){//添加
                    ruleSetService.updateEngineRulesVerify(approval);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }else if(approval.getActionType() == 2 && approval.getAppStatus() == 1){//修改
                    PatchData patchData = patchDataMapper.selectByOnlyId(approval);
                    patchData.setVerify(2);
                    patchData.setOnlyId(approval.getOnlyId());
                    ruleSetService.updateRulesSelect(patchData);
                    patchDataMapper.deleteByPrimaryKey(patchData.getId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getActionType() == 3 && approval.getAppStatus() == 1){//删除
                    ruleSetService.deleteAbsolute(approval.getOnlyId());
                    PatchData patchData = new PatchData();
                    patchData.setVerify(2);
                    patchData.setOnlyId(approval.getOnlyId());
                    ruleSetService.updateRulesSelect(patchData);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getActionType() == 4 && approval.getAppStatus() == 1){//手动提交审批
                    EngineRules engineRules = new EngineRules();
                    engineRules.setVerify(2);
                    engineRules.setId(approval.getOnlyId());
                    engineRulesMapper.updateEngineRulesVerify(engineRules);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getAppStatus() == 2 ){//删除
                    PatchData patchData = new PatchData();
                    patchData.setVerify(3);
                    patchData.setOnlyId(approval.getOnlyId());
                    ruleSetService.updateRulesSelect(patchData);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
            }
            else if(approval.getObjId() == 3){//规则
                if(approval.getActionType() == 1 && approval.getAppStatus() == 1){//添加
                    engineRuleService.updateRuleVerify(approval);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }else if(approval.getActionType() == 2 && approval.getAppStatus() == 1){//修改
                    PatchData patchData = patchDataMapper.selectByOnlyId(approval);
                    engineRuleService.updateRuleSelect(patchData);
                    patchDataMapper.deleteByPrimaryKey(patchData.getId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getActionType() == 3 && approval.getAppStatus() == 1){//删除
                    engineRuleService.delete(approval.getOnlyId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
            }
            else if(approval.getObjId() == 4){//字段
                if(approval.getActionType() == 1 && approval.getAppStatus() == 1){//添加
                    fieldService.updateFieldVerify(approval);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }else if(approval.getActionType() == 2 && approval.getAppStatus() == 1){//修改
                    PatchData patchData = patchDataMapper.selectByOnlyId(approval);
                    fieldService.updateFieldSelect(patchData);
                    patchDataMapper.deleteByPrimaryKey(patchData.getId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getActionType() == 3 && approval.getAppStatus() == 1){//删除
                    fieldService.delete(approval.getOnlyId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
            }
            else if(approval.getObjId() == 5){//决策
                if(approval.getActionType() == 1 && approval.getAppStatus() == 1){//添加
                    fieldService.updateFieldVerify(approval);
                    approvalMapper.updateRemarksById(approval);
                    return;
                }else if(approval.getActionType() == 2 && approval.getAppStatus() == 1){//修改
                    PatchData patchData = patchDataMapper.selectByOnlyId(approval);
                    fieldService.updateFieldSelect(patchData);
                    patchDataMapper.deleteByPrimaryKey(patchData.getId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
                else if(approval.getActionType() == 3 && approval.getAppStatus() == 1){//删除
                    fieldService.delete(approval.getOnlyId());
                    approvalMapper.updateRemarksById(approval);
                    return;
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 决策审批
     * @param approval
     * @throws ServiceException
     */
    @Override
    public void updateAppStatusForDecision(Approval approval) throws ServiceException  {
        try {
            //记录决策
            DecisionHistoryLog decisionHistoryLog = new DecisionHistoryLog();
            if(approval.getActionType() != null && approval.getActionType() == 3){
                decisionHistoryLog.setActionType(3);
            }
            else if(approval.getActionType() != null && approval.getActionType() == 4){
                decisionHistoryLog.setActionType(4);
            }
            decisionHistoryLog.setRecordTime(approval.getSubTime());
            decisionHistoryLog.setUniqueCode(approval.getUniqueCode());
            EngineDecision engineDecision = engineDecisionMapper.selectByPrimaryKey(Integer.valueOf(approval.getOnlyId().toString()));
            String rulesTemplate = JSONObject.toJSONString(engineDecision);
            decisionHistoryLog.setDecisionMap(rulesTemplate);
            decisionHistoryLog.setDecisionId(Integer.valueOf(approval.getOnlyId().toString()));
            decisionHistoryLog.setDecisionName(engineDecision.getName());
            decisionHistoryMapper.insertBySelective(decisionHistoryLog);
            approval.setRecordId(decisionHistoryLog.getId());
            approvalMapper.updateAppStatus(approval);
            if(approval.getAppStatus() == 1){//通过
                engineDecision.setAppStatus(2);
            }else if( approval.getAppStatus() == 2){//拒绝
                engineDecision.setAppStatus(3);
            }
            engineDecisionMapper.update(engineDecision);
        } catch (Exception e) {
            throw new ServiceException();
        }
    }

    @Override
    public Approval selectById(Long id) {
        return approvalMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ObjectType> getObjList() {
        return approvalMapper.getObjList();
    }

    @Override
    public List<ActionType> getActionList() {
        return approvalMapper.getActionList();
    }

    @Override
    public Page<Approval> findUnByPage(Approval approval) {
        PageHelper.startPage(approval.getPageNo(), approval.getPageSize());
        return approvalMapper.findUnByPage(approval);
    }

    @Override
    public void deleteToApproval(Approval approval) throws ServiceException {
        approvalMapper.insertSelective(approval);
        ruleSetService.updateEngineRulesVerify(approval);
    }
}
