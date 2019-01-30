package com.geo.rcs.modules.rule.field.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.mail.MailService;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:52
 */
@RestController
@RequestMapping("/rule/field")
public class FieldController extends BaseController {

    @Autowired
    private MailService mailService;
    @Autowired
    private FieldService fieldService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private PatchDataService patchDataService;

    @Autowired
    private RuleSetService ruleSetService;

    /**
     * 查询前台字段管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param field
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:field:list")//权限管理
    public void getFieldList(HttpServletRequest request, HttpServletResponse response, EngineRawField field) {
        try {
            this.sendData(request, response, new PageInfo<>(fieldService.findByPage(field)));
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }


    /**
     * 字段详情页，包括字段以及接口信息
     *
     * @param request
     * @param response
     * @param id
     */
    @RequestMapping("/getFieldMsg")
    @RequiresPermissions("rule:field:detail")//权限管理
    public void getFieldMsg(HttpServletRequest request, HttpServletResponse response, Long id) {
        try {
            Map<Object, Object> map = new HashMap<>();
            EngineRawField engineRawField = fieldService.selectById(id);
            EngineInter engineInter = fieldService.selectInterByFieldId(id);
            map.put("engineRawField", engineRawField);
            map.put("engineInter", engineInter);
            this.sendData(request, response, map);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 字段分类类型
     *
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/getFieldDataType")
    public void getFieldDataType(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, fieldService.getFieldDataType());
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 根据字段分类类型获取所属字段
     *
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/getAllEngineRawField")
    public void getFieldByDataType(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, fieldService.getAllEngineRawField());
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 获取字段信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        this.sendData(request, response, fieldService.getFieldById(id));
    }

    /**
     * 获取数据源字段信息，用于回显数据
     */
    @RequestMapping("/getRawField")
    public void getRawField(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        this.sendData(request, response, fieldService.getRawFieldById(id));
    }

    /**
     * 字段类型
     *
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/toAdd")
    public void getFieldTypeList(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, fieldService.getFieldType());
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 添加字段时先纳入审批
     *
     * @param engineRawField
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalAdd")
    @RequiresPermissions("ruleset:field:save")
    public void toApprovalAdd(EngineRawField engineRawField, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(engineRawField, NotNull.RequestType.UPDATE);

        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            /*if (!fieldService.usernameUnique(engineRawField)){
                this.sendError(request, response, "字段已存在,请勿重复添加！");
            }*/
            engineRawField.setAddUser(getUser().getName());
            engineRawField.setAddTime(new Date());
            engineRawField.setVerify(0);
            fieldService.save(engineRawField);
            EngineRawField engineRawField1 = fieldService.queryByName(engineRawField);
            Approval approval = new Approval();
            approval.setActionType(1);
            approval.setObjId(4);
            approval.setSubmitter(engineRawField.getAddUser());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setOnlyId(engineRawField1.getId());
            approval.setDescription(engineRawField.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }

    /**
     * 管理员可以不经过审批添加字段
     */
    @SysLog("添加字段")
    @RequestMapping("/saveNoApproval")
    @RequiresPermissions("ruleset:field:save")
    public void save(EngineField engineField, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(engineField, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            EngineRules engineRules = ruleSetService.selectById(engineField.getRulesId());
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
                engineField.setAddUser(getGeoUser().getName());
                fieldService.saveField(engineField);
                this.sendOK(request, response);//code:200表示成功
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "添加字段失败！");
        }
    }

    /**
     * 修改字段时先纳入审批
     *
     * @param patchData
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalUpdate")
    public void toApprovalUpdate(PatchData patchData, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(patchData, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            patchData.setActionId(2);
            patchData.setObjId(4);
            patchDataService.insertSelective(patchData);
            Approval approval = new Approval();
            approval.setActionType(2);
            //approval.setBusinessId(engineRawField.getBusinessId());
            approval.setObjId(4);
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
     * 审批通过后修改字段
     */
    @SysLog("修改字段")
    @RequestMapping("/confirmUpdate")
    @RequiresPermissions("ruleset:field:update")
    public void updateField(@Validated EngineField engineField, HttpServletRequest request, HttpServletResponse response, Approval approval) {

        ResultType resultType = ValidateNull.check(engineField, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }

        try {
            EngineRules engineRules = fieldService.findRulesByFieldId(engineField.getId());
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
                fieldService.updateEngineField(engineField);
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "更新字段失败！");
        }
    }

    /**
     * 删除字段时先纳入审批
     *
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalDelete")
    public void toApprovalDelete(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {
            Approval approval = new Approval();
            approval.setActionType(3);
            approval.setObjId(4);
            approval.setOnlyId(id);
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }

    /**
     * 审批通过后删除字段
     */
    @RequestMapping("/confirmDelete")
    @RequiresPermissions("ruleset:field:delete")
    public void deleteRoster(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (id != null) {
                EngineRules engineRules = fieldService.findRulesByFieldId(id);
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
                    fieldService.delete(id);
                    ruleSetService.updateEngineRules(engineRules);
                    this.sendOK(request, response);
                }
            } else {
                this.sendError(request, response, "字段不存在！");
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }
    }

    /**
     * 拷贝字段
     */
    @RequestMapping("/copyField")
    @RequiresPermissions("ruleset:field:copy")
    public void copyFieldById(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (id != null) {
                EngineRules engineRules = fieldService.findRulesByFieldId(id);
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
                    EngineField fieldById = fieldService.getFieldById(id);
                    fieldService.saveField(fieldById);
                    ruleSetService.updateEngineRules(engineRules);
                    this.sendOK(request, response);
                }
            } else {
                this.sendError(request, response, "字段不存在！");
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "拷贝失败！");
        }
    }

    /**
     * 运营端接口开始==============================
     */

    /**
     * 查询后台字段管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param field
     */
    @RequestMapping("/geoFieldList")
    @RequiresPermissions("rule:field:list")//权限管理
    public void getGeoFieldList(HttpServletRequest request, HttpServletResponse response, EngineRawField field) {
        try {
            this.sendData(request, response, new PageInfo<>(fieldService.getGeoFieldList(field)));
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 后台获取字段信息，用于回显数据
     */
    @RequestMapping("/toUpdateAdmin")
    public void toUpdateSys(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        EngineRawField rawFieldById = fieldService.getRawFieldById(id);
        this.sendData(request, response,rawFieldById);
    }

    /**
     * 后台修改字段
     */
    @SysLog("修改字段")
    @RequestMapping("/confirmUpdateAdmin")
    @RequiresPermissions("rule:field:update")
    public void updateFiledAdmin(@Validated EngineRawField engineRawField, HttpServletRequest request, HttpServletResponse response, Approval approval){

        ResultType resultType = ValidateNull.check(engineRawField, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }

        try {
            fieldService.updateField(engineRawField);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新字段失败！");
        }
    }

    /**
     * 后台添加数据源字段
     */
    @SysLog("添加字段")
    @RequestMapping("/saveRawFieldAdmin")
    @RequiresPermissions("rule:field:save")
    public void saveRawFieldAdmin(EngineRawField engineField, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(engineField, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            try {
                engineField.setAddUser(getGeoUser().getName());
            } catch (Exception e) {
                engineField.setAddUser(getUser().getName());
            }
            fieldService.save(engineField);
            this.sendOK(request, response);//code:200表示成功
        } catch (Exception e) {
            this.sendError(request, response, "添加字段失败！");
        }
    }


    /**
     * 后台删除字段
     */
    @RequestMapping("/confirmDeleteAdmin")
    @RequiresPermissions("rule:field:delete")
    public void deleteRosterAdmin(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (id != null) {
                fieldService.deleteFieldAdmin(id);
                this.sendOK(request, response);
            } else {
                this.sendError(request, response, "字段不存在！");
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }
    }

    /**
     * 后台删除字段
     */
    @RequestMapping("/checkFieldUseful")
    public void checkFieldUseful(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (id != null) {
                boolean result = checkRulesField(id);
                Map used = new HashMap<>();
                used.put("used", result);
                this.sendData(request, response, used);
            } else {
                this.sendError(request, response, "字段不存在！");
            }
        } catch (Exception e) {
            this.sendError(request, response, "校验字段失败!");
        }
    }

    public boolean checkRulesField(Long id) {
        List<Long> fieldList = fieldService.selectRulesFieldUse();
        if (fieldList.contains(id)) {
            return true;
        } else {
            return false;
        }
    }
}
