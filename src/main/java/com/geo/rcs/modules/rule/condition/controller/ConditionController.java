package com.geo.rcs.modules.rule.condition.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.condition.service.ConditionService;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.condition.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月05日 上午10:21
 */
@RestController
@RequestMapping("/rule/condition")
public class ConditionController extends BaseController {

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private EngineRuleService engineRuleService;

    @Autowired
    private RuleSetService ruleSetService;

    /**
     * 查询规则管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param conditions
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:condition:list")//权限管理
    public void getConditionList(HttpServletRequest request, HttpServletResponse response, Conditions conditions) {
        try {
                //添加unique_code （客户唯一标识）
                conditions.setUniqueCode(getUser().getUniqueCode());

                this.sendData(request, response, new PageInfo<>(conditionService.findByPage(conditions)));

                this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取规则列表",conditions.toString(),getUser().getName(),e);
        }
    }
    /**
     * 删除条件
     */
    @RequestMapping(value = "/delete")
    @RequiresPermissions("rule:condition:delete")
    public void deleteRoster(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            //根据条件id查询所属规则集激活状态
            EngineRules engineRules = conditionService.queryEngineRulesActive(id);

            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }
            else if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            }
            else{
                conditionService.delete(id);
                fieldService.deleteByConId(id);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }

    }
    /**
     * 获取条件以及字段信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        //根据条件id查询所属规则集激活状态
        EngineRules engineRules = conditionService.queryEngineRulesActive(id);
        if(engineRules.getActive() == 1){
            this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
        }
        else if(engineRules.getVerify() == 1){
            this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
        }
        else {
            this.sendData(request, response, conditionService.getConditionAndFieldById(id));
        }
    }
    /**
     * 修改条件（Firstep）
     */
    @RequestMapping("/updateConditions")
    @RequiresPermissions("rule:condition:update")
    public void updateConditions(Conditions conditions, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(conditions, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            EngineRules engineRules = conditionService.queryEngineRulesActive(conditions.getId());
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }
            else if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            }
            else {
                conditions.setAddUser(getUser().getName());
                conditions.setUniqueCode(getUser().getUniqueCode());
                this.sendData(request, response, conditionService.updateConditionById(conditions));
                engineRules.setVerify(0);
                ruleSetService.updateEngineRules(engineRules);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "修改条件失败！");
        }
    }
    /**
     * 修改字段（Second Step）／先删除后添加
     */
    /*@RequestMapping(value = "/updateField",method = RequestMethod.POST)
    public void updateField(@RequestBody List<EngineRawField> engineRawFields, HttpServletRequest request, HttpServletResponse response) {
        try {
            for (EngineRawField engineRawField : engineRawFields) {
                engineRawField.setAddTime(new Date());
                engineRawField.setAddUser(getUser().getName());
                Long[] ids = fieldService.selectByConditionId(engineRawField.getConditionId());
                fieldService.deleteBatch(ids);
            }
            this.sendData(request, response,fieldService.addFieldBatch(engineRawFields));//code:200表示成功
        } catch (Exception e) {
            this.sendError(request, response, "修改字段失败！");
        }
    }*/
    /**
     * 添加字段（Second Step）
     */
    @RequestMapping(value = "/addFieldBatch",method = RequestMethod.POST)
    public void addField(@RequestBody List<EngineField> engineFields, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(engineFields, NotNull.RequestType.NEW);
        EngineRules engineRules = null;
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            for (EngineField engineField : engineFields) {
                engineRules = ruleSetService.selectById(engineField.getRulesId());
                if (engineRules == null) {
                    this.sendError(request, response, "规则集节点为空！");
                    return;
                } else if (engineRules.getVerify() == 1) {
                    this.sendErrAc(request, response, "此规则集正在审核,不能进行此操作");
                    return;
                } else if (engineRules.getActive() == 1) {
                    this.sendErrAc(request, response, "此规则集处于激活状态,不能进行此操作");
                    return;
                }else {
                    FieldType fieldType = engineRuleService.selectByFieldTypeId(engineField.getFieldTypeId());
                    engineField.setFieldRawName(fieldType.getType());
                    engineField.setAddTime(new Date());
                    EngineRawField engineRawField = engineRuleService.selectById(engineField.getFieldId().longValue());
                    engineField.setFieldName(engineRawField.getName());
                    engineField.setAddUser(getUser().getName());
                    engineField.setUniqueCode(getUser().getUniqueCode());
                    if ("[lt]".equals(engineField.getOperator())) {
                        engineField.setOperator("<");
                    } else if ("[gt]".equals(engineField.getOperator())) {
                        engineField.setOperator(">");
                    } else if ("[gte]".equals(engineField.getOperator())) {
                        engineField.setOperator(">=");
                    } else if ("[lte]".equals(engineField.getOperator())) {
                        engineField.setOperator("<=");
                    } else if ("[dlt]".equals(engineField.getOperator())) {
                        engineField.setOperator("-<");
                    } else if ("[dgt]".equals(engineField.getOperator())) {
                        engineField.setOperator("->");
                    } else if ("[dgte]".equals(engineField.getOperator())) {
                        engineField.setOperator("->=");
                    } else if ("[dlte]".equals(engineField.getOperator())) {
                        engineField.setOperator("-<=");
                    }
                }
            }
            this.sendData(request, response,fieldService.addFieldBatch(engineFields));//code:200表示成功
            ruleSetService.updateEngineRules(engineRules);
        } catch (Exception e) {
        	e.printStackTrace();
            this.sendError(request, response, "添加字段失败！");
            LogUtil.error("addFieldBatch",engineFields.toString(), getUser().getName(), e);
        }
    }
    /**
     * 添加条件（Firstep）
     */
    @RequestMapping("/addConditions")
    @RequiresPermissions("rule:condition:save")
    public void save(Conditions conditions, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(conditions, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            /*if (!conditionService.usernameUnique(conditions)){
                this.sendError(request, response, "条件已存在,请勿重复添加！");
            }*/
            EngineRules engineRules = conditionService.selectRulesActiveByRuleId(conditions.getRuleId());
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }
            else if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            }
            else {
                conditions.setAddUser(getUser().getName());
                conditions.setUniqueCode(getUser().getUniqueCode());
                this.sendData(request, response, conditionService.save(conditions));//code:2000表示成功
                ruleSetService.updateEngineRules(engineRules);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "添加条件失败！");
        }
    }
    /**
     * 获取字段类型，字段名，接口名，主属性列表
     */
    @RequestMapping("/toAdd")
    public void getBusType(HttpServletRequest request, HttpServletResponse response){
        try {
            List typeList = new ArrayList();
            List<FieldType> fieldType = fieldService.getFieldType();
            List<EngineRawField> fieldName = fieldService.getFieldName();
            List<EngineInter> interName = fieldService.getInterName();
            typeList.add(fieldType);
            typeList.add(fieldName);
            typeList.add(interName);
            this.sendData(request, response,typeList);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 添加字段关联关系
     */
    @RequestMapping(value = "/addFieldRs",method = RequestMethod.POST)
    public void addFieldRs(@RequestBody Conditions conditions, HttpServletRequest request, HttpServletResponse response){
        if (conditions.getFieldRelationship() == null && conditions.getFieldRelationship() == "" ) {
            this.sendError(request, response,"请添加字段关联关系！");
        }
        try {
            EngineRules engineRules = conditionService.queryEngineRulesActive(conditions.getId());
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }
            else if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            }
            else {
                String fieldRelationship = conditions.getFieldRelationship();
                String strEscape = StringEscapeUtils.unescapeHtml(fieldRelationship);
                conditions.setFieldRelationship(strEscape);
                conditionService.addFieldRs(conditions);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "添加失败！");
        }
    }
    /**
     * 添加条件关联关系
     */
    @RequestMapping(value = "/addConditionsRs",method = RequestMethod.POST)
    @RequiresPermissions("rule:condition:update")
    public void addConditionsRs(@RequestBody EngineRule engineRule, HttpServletRequest request, HttpServletResponse response){
        ResultType resultType = ValidateNull.check(engineRule, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            EngineRules engineRules = conditionService.selectRulesActiveByRuleId(engineRule.getId());
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }
            else if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
            }
            else {
                String conditionRelationship = engineRule.getConditionRelationship();
                String strEscape = StringEscapeUtils.unescapeHtml(conditionRelationship);
                engineRule.setConditionRelationship(strEscape);
                engineRuleService.addConditionsRs(engineRule);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "添加失败！");
        }
    }
    /**
     * 根据字段类型获取字段名
     */
    @RequestMapping("/getFieldName")
    public void getFieldName(HttpServletRequest request, HttpServletResponse response,Integer fieldType){
        try {
            this.sendData(request, response, fieldService.getFieldNameById(fieldType));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 获取条件以及字段信息，用于回显数据
     */
    @RequestMapping("/getConAndFieldInfo")
    public void getConAndFieldInfo(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        try {
            this.sendData(request, response, conditionService.getConAndFieldInfo(id));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 根据规则id获取规则集激活状态
     */
    @RequestMapping("/getActiveByRuleId")
    public void getActiveByRuleId(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        try {
            this.sendData(request, response,conditionService.selectRulesActiveByRuleId(id));
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 复制条件
     */
    @RequestMapping("/copyCondition")
    @RequiresPermissions("rule:condition:copy")
    public void copyConditionById(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "拷贝内容为空！");
            return;
        }
        try {
            Conditions conAndFieldInfo = conditionService.getConAndFieldInfo(id);
            EngineRules engineRules = engineRuleService.selectRulesByRuleId(conAndFieldInfo.getRuleId());
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
                conAndFieldInfo.setUniqueCode(getUser().getUniqueCode());
                conAndFieldInfo.setAddTime(new Date());
                conAndFieldInfo.setAddUser(getUser().getName());
                conAndFieldInfo.setRulesId(engineRules.getId());
                Conditions newCondition = conditionService.insertSelective(conAndFieldInfo);

                //获取原数据的字段关联关系
                String fieldRelationship = conAndFieldInfo.getFieldRelationship();

                //存储原数据的条件Id
                List<Long> oldFieldIdList = new ArrayList<>();
                for (EngineField field : conAndFieldInfo.getFieldList()) {
                    oldFieldIdList.add(field.getId());
                }
                if (conAndFieldInfo.getFieldList() != null) {
                    for (EngineField engineField : conAndFieldInfo.getFieldList()) {
                        engineField.setUniqueCode(getUser().getUniqueCode());
                        engineField.setAddTime(new Date());
                        engineField.setAddUser(getUser().getName());
                        engineField.setRulesId(engineRules.getId());
                        engineField.setConditionId(conAndFieldInfo.getId());
                    }
                    //新字段集合
                    List<EngineField> engineFields = fieldService.addFieldBatchNoUp(conAndFieldInfo.getFieldList());

                    if(fieldRelationship != null) {
                        //字段关联关系编号替换并更新
                        for (int i = 0; i < engineFields.size(); i++) {
                            fieldRelationship = fieldRelationship.replace(String.valueOf(oldFieldIdList.get(i)), String.valueOf(engineFields.get(i).getId()));
                            newCondition.setFieldRelationship(fieldRelationship);
                            conditionService.addFieldRs(newCondition);
                        }
                    }
                }
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
