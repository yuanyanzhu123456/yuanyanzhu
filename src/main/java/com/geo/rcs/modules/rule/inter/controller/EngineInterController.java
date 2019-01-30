package com.geo.rcs.modules.rule.inter.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.source.handler.ResponseParser;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.inter.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午11:10
 */
@RestController
@RequestMapping("/rule/inter")
public class EngineInterController extends BaseController{
    @Autowired
    private EngineInterService engineInterService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private PatchDataService patchDataService;

    /**
     * 查询规则管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param engineInter
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:inter:list")//权限管理
    public void list(HttpServletRequest request, HttpServletResponse response, EngineInter engineInter) {
        try {
            this.sendData(request, response, new PageInfo<>(engineInterService.findByPage(engineInter)));
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 接口类型
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/getInterList")
    public void getInterList(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response,engineInterService.getInterList());
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 添加接口时先纳入审批
     * @param engineInter
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalAdd")
    @RequiresPermissions("rule:inter:save")
    public void toApprovalAdd(EngineInter engineInter, HttpServletRequest request, HttpServletResponse response){
        ResultType resultType = ValidateNull.check(engineInter, NotNull.RequestType.UPDATE);

        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            /*if (!engineInterService.usernameUnique(engineInter)){
                this.sendError(request, response, "接口已存在,请勿重复添加！");
            }*/
            try {
                engineInter.setAddUser(getGeoUser().getName());
            }catch (Exception e){
                engineInter.setAddUser(getUser().getName());
            }
            engineInter.setAddTime(new Date());
            engineInter.setVerify(0);
            engineInterService.save(engineInter);
            EngineInter engineInter1 = engineInterService.queryByName(engineInter);
            Approval approval = new Approval();
            approval.setActionType(1);
            approval.setObjId(4);
            approval.setSubmitter(engineInter1.getAddUser());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getGeoId());
            approval.setOnlyId(engineInter1.getId());
            approval.setDescription(engineInter1.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }

    /**
     * 解析接口返回字段
     */
    @SysLog("解析接口返回字段")
    @RequestMapping("/parseInter")
    @RequiresPermissions("rule:inter:save")
    public void parseInter(EngineInter engineInter, HttpServletRequest request, HttpServletResponse response) {
        try {
            if(engineInter.getTestParameters() == null){
                this.sendError(request, response, "解析数据不能为空！");
            }
            Map parseData = ResponseParser.ParserBasic(engineInter.getTestParameters(), engineInter.getName());
            this.sendData(request, response,parseData);
        } catch (Exception e) {
            if(e instanceof RcsException){
                this.sendError(request, response, ((RcsException) e).getMsg());
            }
            else{
                this.sendError(request, response, "添加接口失败！");
            }
        }
    }
    /**
     * 管理员可以不经过审批添加接口
     */
    @SysLog("添加接口")
    @RequestMapping("/save")
    @RequiresPermissions("rule:inter:save")
    public void save(EngineInter engineInter, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(engineInter, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            /*if (!engineInterService.usernameUnique(engineInter)){
                this.sendError(request, response, "接口已存在,请勿重复添加！");
            }*/
            try {
                engineInter.setAddUser(getGeoUser().getName());
                engineInter.setUserId(getGeoUser().getUniqueCode());
            }catch (Exception e){
                engineInter.setAddUser(getUser().getName());
                engineInter.setUserId(getUser().getUniqueCode());
            }
            this.sendData(request, response,engineInterService.save(engineInter));
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof RcsException){
                this.sendError(request, response, ((RcsException) e).getMsg());
            }
            else{
                this.sendError(request, response, "添加接口失败！");
            }
        }
    }
    /**
     * 添加接口及字段
     */
    @SysLog("添加接口及字段")
    @RequestMapping("/saveInterAndFields")
    @RequiresPermissions("rule:inter:save")
    public void saveInterAndFields(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response) {
        try {
            EngineInter engineInter = new EngineInter();
            engineInter.setName(map.get("name").toString()==null?null:map.get("name").toString());
            engineInter.setParameters(map.get("parameters").toString()==null?null:map.get("parameters").toString());
            try {
                engineInter.setAddUser(getGeoUser().getName());
                engineInter.setUserId(getGeoUser().getUniqueCode());
            }catch (Exception e){
                engineInter.setAddUser(getUser().getName());
                engineInter.setUserId(getUser().getUniqueCode());
            }
            List<Map<String,String>> fields = (List)map.get("fields") == null ? null :(List)map.get("fields");
            engineInterService.saveInterAndFields(engineInter,fields);
            this.sendOK(request,response);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof RcsException){
                this.sendError(request, response, ((RcsException) e).getMsg());
            }
            else{
                this.sendError(request, response, "添加接口失败！");
            }
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
        this.sendData(request, response,engineInterService.getInterById(id));
    }
    /**
     * 修改接口时先纳入审批
     * @param patchData
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalUpdate")
    @RequiresPermissions("rule:inter:update")


    public void toApprovalUpdate(PatchData patchData, HttpServletRequest request, HttpServletResponse response){
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
            try {
                approval.setSubmitter(getGeoUser().getName());
            }catch (Exception e){
                approval.setSubmitter(getUser().getName());
            }
            approval.setSubTime(new Date());
            approval.setUniqueCode(getGeoId());
            approval.setDescription(patchData.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 修改接口
     */
    @SysLog("修改接口")
    @RequestMapping("/update")
    @RequiresPermissions("rule:inter:update")
    public void updateScene(@Validated EngineInter engineInter, HttpServletRequest request, HttpServletResponse response, Approval approval){

        ResultType resultType = ValidateNull.check(engineInter, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }

        try {
            engineInterService.updateInter(engineInter);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新接口失败！");
        }
    }
    /**
     * 删除接口时先纳入审批
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalDelete")
    @RequiresPermissions("rule:inter:delete")
    public void toApprovalDelete(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {
            EngineInter fieldById = engineInterService.getFieldById(id);
            request.getSession().setAttribute("engineInter",fieldById);
            Approval approval = new Approval();
            approval.setActionType(3);
            approval.setObjId(4);
            approval.setOnlyId(id);
            try {
                approval.setSubmitter(getGeoUser().getName());
            }catch (Exception e){
                approval.setSubmitter(getUser().getName());
            }
            approval.setSubTime(new Date());
            approval.setUniqueCode(getGeoId());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后删除接口
     */
    @RequestMapping("/delete")
    public void deleteRoster(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            engineInterService.delete(id);
            this.sendOK(request, response);

        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }

    }

    /**
     * 获取接口类型
     */
    @RequestMapping("/getInterType")
    public void getInterType(HttpServletRequest request, HttpServletResponse response){
        try {
            this.sendData(request, response,engineInterService.getInterType());
        } catch (Exception e) {
            this.sendError(request, response, "获取接口类型失败！");
        }

    }

}
