package com.geo.rcs.modules.rule.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.condition.service.ConditionService;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.dao.EngineFieldMapper;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月03日 下午5:19
 */
@RestController
@RequestMapping("/rule")
public class EngineRuleController extends BaseController {

    @Autowired
    private EngineRuleService ruleService;

    @Autowired
    private RuleSetService ruleSetService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private PatchDataService patchDataService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private EngineFieldMapper engineFieldMapper;

    @Autowired
    private FieldService fieldService;

    /**
     * 查询规则管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param rule
     *
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:list")
    public void getStaffList(HttpServletRequest request, HttpServletResponse response, EngineRule rule) {
        try {
            //添加unique_code （客户唯一标识）
            rule.setUniqueCode(getUser().getUniqueCode());

            this.sendData(request, response, new PageInfo<>(ruleService.findByPage(rule)));

            this.sendOK(request, response);

        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取规则列表", "", getUser().getName(), e);
        }
    }

    /**
     * 获取所有规则名称
     */
    @RequestMapping("/getRuleName")
    public void getRuleName(HttpServletRequest request, HttpServletResponse response) {
        this.sendData(request, response, ruleService.getAllRuleName());
    }

    /**
     * 添加规则时先纳入审批
     *
     * @param engineRule
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalAdd")
    @RequiresPermissions("rule:save")
    public void toApprovalAdd(EngineRule engineRule, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(engineRule, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            engineRule.setUniqueCode(getUserId());
            engineRule.setAddUser(getUser().getName());
            engineRule.setAddTime(new Date());
            engineRule.setVerify(0);
            ruleService.saveBySelect(engineRule);
            EngineRule engineRule1 = ruleService.selectByName(engineRule.getName());

            Approval approval = new Approval();
            approval.setActionType(1);
            //approval.setBusinessId(engineRawField.getBusinessId());
            approval.setObjId(3);
            approval.setOnlyId(engineRule1.getId());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(engineRule.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
            LogUtil.error("申请审批", engineRule.getId().toString(), getUser().getName(), e);
        }

    }

    /**
     * 管理员不经审批添加规则
     */
    @SysLog("添加规则")
    @RequestMapping(value = "/saveNoApproval", method = RequestMethod.POST)
    @RequiresPermissions("rule:save")
    public void save(EngineRule engineRule, HttpServletRequest request, HttpServletResponse response) {
        String decision = request.getParameter("decision");
        try {
            if (decision != null) {
                String decode = URLDecoder.decode(decision, "utf-8");
                engineRule.setDecision(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ResultType resultType = ValidateNull.check(engineRule, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            /*if (!ruleService.usernameUnique(engineRule.getName())) {
                this.sendError(request, response, "规则名称已存在！");
            }*/
            EngineRules engineRules = ruleSetService.selectById(engineRule.getRulesId());
            if (engineRules.getActive() == 1) {
                this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                return;
            } else if (engineRules.getVerify() == 1) {
                this.sendErrAc(request, response, "此规则集正在审核,不能进行此操作");
                return;
            } else {
                //添加unique_code （客户唯一标识）
                engineRule.setUniqueCode(getUser().getUniqueCode());
                engineRule.setAddUser(getUser().getName());
                engineRule.setAddTime(new Date());
                engineRule.setRulesId(engineRule.getRulesId());
                ruleService.save(engineRule);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);//code:200表示成功
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "添加规则失败！");
            LogUtil.error("添加规则", engineRule.toString(), getUser().getName(), e);
        }
    }

    /**
     * 修改规则时先纳入审批
     *
     * @param patchData
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalUpdate")
    @RequiresPermissions("rule:update")
    public void toApprovalUpdate(PatchData patchData, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(patchData, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            patchData.setActionId(2);
            patchData.setObjId(3);
            patchDataService.insertSelective(patchData);
            Approval approval = new Approval();
            approval.setActionType(2);
            approval.setObjId(3);
            approval.setOnlyId(patchData.getOnlyId());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(patchData.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }

    /**
     * 修改规则(不审批)
     */
    @RequestMapping("/confirmUpdate")
    public void updateRule(EngineRule engineRule, HttpServletRequest request, HttpServletResponse response) {
        String decision = request.getParameter("decision");
        try {
            if (decision != null) {
                String decode = URLDecoder.decode(decision, "utf-8");
                engineRule.setDecision(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ResultType resultType = ValidateNull.check(engineRule, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, "参数不能为空！");
            return;
        }
        try {
            EngineRules engineRules = conditionService.selectRulesActiveByRuleId(engineRule.getId());
            if (engineRules.getActive() == 1) {
                this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                return;
            } else if (engineRules.getVerify() == 1) {
                this.sendErrAc(request, response, "此规则集正在审核,不能进行此操作");
                return;
            } else {
                ruleService.updateEngineRule(engineRule);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "修改规则失败！");
            LogUtil.error("修改规则", engineRule.toString(), getUser().getName(), e);
        }
    }

    /**
     * 删除规则时先纳入审批
     *
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalDelete")
    @RequiresPermissions("rule:delete")
    public void toApprovalDelete(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "Id不能为空！");
            return;
        }
        try {
            EngineRules engineRules = conditionService.selectRulesActiveByRuleId(id);
            if (engineRules.getActive() == 1) {
                this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                return;
            } else {
                request.getSession().setAttribute("engineRule", engineRules);
                Approval approval = new Approval();
                approval.setActionType(3);
                approval.setObjId(3);
                approval.setOnlyId(id);
                approval.setSubmitter(getUser().getName());
                approval.setSubTime(new Date());
                approval.setUniqueCode(getUserId());
                approvalService.addToApproval(approval);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }

    /**
     * 删除规则(包括规则下面的条件字段)
     */
    @RequestMapping("/confirmDelete")
    public void deleteRule(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            EngineRules engineRules = conditionService.selectRulesActiveByRuleId(id);
            if (engineRules.getActive() == 1) {
                this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                return;
            } else if (engineRules.getVerify() == 1) {
                this.sendErrAc(request, response, "此规则集正在审核,不能进行此操作");
                return;
            } else {
                fieldService.deleteByRuleId(id);
                conditionService.deleteByRuleId(id);
                ruleService.delete(id);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
            LogUtil.error("删除规则", id.toString(), getUser().getName(), e);
        }
    }

    /**
     * 获取规则信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
            return;
        }
        try {
            EngineRules engineRules = conditionService.selectRulesActiveByRuleId(id);
            if (engineRules.getActive() == 1) {
                this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                return;
            } else {
                this.sendData(request, response, ruleService.getEngineRuleById(id));
                return;
            }
        } catch (ServiceException e) {
            LogUtil.error("获取规则信息", id.toString(), getUser().getName(), e);
            e.printStackTrace();
        }
    }

    /**
     * 获取规则信息，用于回显数据（不需要验证所属规则集是否处于激活状态）
     */
    @RequestMapping("/getRuleInfo")
    public void getRuleInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
            return;
        }
        try {
            this.sendData(request, response, ruleService.getEngineRuleById(id));
        } catch (ServiceException e) {
            LogUtil.error("获取规则信息", id.toString(), getUser().getName(), e);
            e.printStackTrace();
        }
    }

    /**
     * 获取规则以及条件信息
     */
    @RequestMapping("/getRuleAndConInfo")
    public void getRuleAndConInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
            return;
        }
        try {
            this.sendData(request, response, ruleService.getRuleAndConInfo(id));
        } catch (ServiceException e) {
            LogUtil.error("获取规则及条件信息", id.toString(), getUser().getName(), e);
            e.printStackTrace();
        }
    }

    /**
     * 复制规则
     */
    @RequestMapping("/copyRule")
    @RequiresPermissions("rule:copy")
    public void copyRuleById(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "拷贝内容为空！");
            return;
        }
        try {
            EngineRule engineRule = ruleService.getRuleAndConInfo(id);
            EngineRules engineRules = ruleService.selectRulesByRuleId(id);
            if (engineRules == null) {
                this.sendError(request, response, "规则集节点为空！");
                return;
            } else if (engineRules.getVerify() == 1) {
                this.sendErrAc(request, response, "此规则集正在审核,不能进行此操作");
                return;
            } else if (engineRules.getActive() == 1) {
                this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                return;
            } else {
                engineRule.setUniqueCode(getUser().getUniqueCode());
                engineRule.setAddTime(new Date());
                engineRule.setAddUser(getUser().getName());
                engineRule.setRulesId(engineRules.getId());
                engineRule.setConditionNumber(0);
                EngineRule engineRule1 = ruleService.save(engineRule);

                //获取原数据的条件关联关系
                String conditionRelationship = engineRule1.getConditionRelationship();

                //存储原数据的条件Id
                List<Long> oldIdList = new ArrayList<>();
                for (Conditions conditions : engineRule1.getConditionsList()) {
                    oldIdList.add(conditions.getId());
                }
                if (engineRule.getConditionsList() != null && engineRule.getConditionsList().get(0).getId() != null) {

                    //添加条件
                    for (Conditions conditions : engineRule.getConditionsList()) {
                        List<EngineField> engineFields1 = fieldService.selectFieldForCon(conditions.getId());
                        conditions.setUniqueCode(getUser().getUniqueCode());
                        conditions.setAddTime(new Date());
                        conditions.setAddUser(getUser().getName());
                        conditions.setRulesId(engineRules.getId());
                        conditions.setRuleId(engineRule1.getId());
                        Conditions conditions1 = conditionService.saveNoUp(conditions);

                        //字段关联关系编号替换并更新
                        for (int i = 0; i < engineRule.getConditionsList().size(); i++) {

                            if (conditionRelationship != null) {
                                conditionRelationship = conditionRelationship.replace(String.valueOf(oldIdList.get(i)), String.valueOf(engineRule.getConditionsList().get(i).getId()));

                            }
                            engineRule.setConditionRelationship(conditionRelationship);
                            //修改条件关联关系
                            ruleService.addConditionsRs(engineRule);
                        }


                        //获取原数据的字段关联关系
                        String fieldRelationship = conditions1.getFieldRelationship();

                        //存储原数据的条件Id
                        List<Long> oldFieldIdList = new ArrayList<>();
                        for (EngineField field : engineFields1) {
                            oldFieldIdList.add(field.getId());
                        }

                        if (engineFields1 != null) {
                            for (EngineField engineField : engineFields1) {
                                engineField.setUniqueCode(getUser().getUniqueCode());
                                engineField.setAddTime(new Date());
                                engineField.setAddUser(getUser().getName());
                                engineField.setRulesId(engineRules.getId());
                                engineField.setConditionId(conditions1.getId());
                            }
                            //新字段集合
                            List<EngineField> engineFields = fieldService.addFieldBatchNoUp(engineFields1);

                            if(fieldRelationship != null){
                                //字段关联关系编号替换并更新
                                for (int i = 0; i < engineFields.size(); i++) {
                                    fieldRelationship = fieldRelationship.replace(String.valueOf(oldFieldIdList.get(i)), String.valueOf(engineFields.get(i).getId()));
                                    conditions.setFieldRelationship(fieldRelationship);
                                    conditionService.addFieldRs(conditions);
                                }
                            }
                        }
                        this.sendOK(request, response);
                    }
                }
                this.sendOK(request, response);
                ruleSetService.updateEngineRules(engineRules);
            }
        } catch (ServiceException e) {
            LogUtil.error("拷贝规则", id.toString(), getUser().getName(), e);
            e.printStackTrace();
        }
    }
}
