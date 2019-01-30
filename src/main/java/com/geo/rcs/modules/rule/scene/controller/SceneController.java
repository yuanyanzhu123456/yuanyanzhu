package com.geo.rcs.modules.rule.scene.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.scene.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:53
 */
@RestController
@RequestMapping("/rule/scene")
public class SceneController extends BaseController{

    @Autowired
    private SceneService sceneService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private PatchDataService patchDataService;

    private static Logger logger = LogUtil.logger(LogFileName.API_LOG);

    /**
     * 查询规则管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param scene
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:scene:list")//权限管理
    public void getSceneList(HttpServletRequest request, HttpServletResponse response, EngineScene scene) {
        try {
                //添加unique_code （客户唯一标识）
                scene.setUniqueCode(getUser().getUniqueCode());
                this.sendData(request, response, new PageInfo<>(sceneService.findByPage(scene)));
                this.sendOK(request, response);
            logger.info("场景列表");
        } catch (ServiceException e) {
            //logger.error("获取列表失败！", e);
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 添加场景
     */
    @SysLog("添加场景")
    @RequestMapping("/save")
    @RequiresPermissions("rule:scene:save")
    public void save(EngineScene scene, HttpServletRequest request, HttpServletResponse response) {
        ValidatorUtils.validateEntity(scene,AddGroup.class);
        ResultType resultType = ValidateNull.check(scene, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            List<EngineScene> lists = sceneService.querySceneName(scene.getName(),getUser().getUniqueCode(),scene.getBusinessId());
            if (lists.size()>=1) {
                this.sendError(request, response, "场景名已存在！");
                return;
            }
                //添加unique_code （客户唯一标识）
                scene.setUniqueCode(getUser().getUniqueCode());
                scene.setAddUser(getUser().getName());
                scene.setVerify(1);
                sceneService.save(scene);
                this.sendOK(request, response);//code:200表示成功
        } catch (ServiceException e) {
            this.sendError(request, response, "添加场景失败！");
        }
    }
    /**
     * 获取场景信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        this.sendData(request, response,sceneService.getSceneById(id));
    }

    /**
     * 修改场景时先纳入审批
     * @param patchData
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalUpdate")
    @RequiresPermissions("rule:scene:update")
    public void toApprovalUpdate(PatchData patchData, HttpServletRequest request, HttpServletResponse response){
        ResultType resultType = ValidateNull.check(patchData, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            patchData.setActionId(2);
            patchData.setObjId(1);
            patchDataService.insertSelective(patchData);
            Approval approval = new Approval();
            approval.setActionType(2);
            approval.setBusinessId(patchData.getBusinessId());
            approval.setObjId(1);
            approval.setOnlyId(patchData.getOnlyId());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(patchData.getDescrib());
            approval.setSubmitter(getUser().getName());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后修改场景
     */
    @SysLog("修改场景")
    @RequestMapping("/confirmUpdate")
    @RequiresPermissions("rule:scene:update")
    public void updateScene(@Validated EngineScene scene, HttpServletRequest request, HttpServletResponse response,Approval approval){

        ResultType resultType = ValidateNull.check(scene, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }

        try {
            List<EngineScene> lists = sceneService.querySceneName(scene.getName(),getUser().getUniqueCode(),scene.getBusinessId());
            if (lists != null) {
                for (int i = 0; i < lists.size();i++){
                    if (lists.get(i).getId() != (long)scene.getId()){
                        this.sendError(request, response, "场景名已存在！");
                        return;
                    }
                }
            }
            sceneService.updateScene(scene);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            //logger.error("更新用户失败！", e);
            this.sendError(request, response, "更新场景失败！");
        }
    }
    /**
     * 删除场景时先纳入审批
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalDelete")
    @RequiresPermissions("rule:scene:delete")
    public void toApprovalDelete(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {
            EngineScene scene = sceneService.getSceneById(id);
            request.getSession().setAttribute("scene",scene);
            Approval approval = new Approval();
            approval.setActionType(3);
            approval.setBusinessId(scene.getBusinessId());
            approval.setObjId(1);
            approval.setDescription(scene.getDescrib());
            approval.setOnlyId(id);
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            //logger.error("更新用户失败！", e);
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后删除场景
     */
    @RequestMapping("/confirmDelete")
    public void deleteRoster(Long id, HttpServletRequest request, HttpServletResponse response){
        try {

            sceneService.delete(id);

            this.sendOK(request, response);

        } catch (ServiceException e) {
            //logger.error("获取列表失败！", e);
            this.sendError(request, response, "删除失败！");
        }

    }
    /**
     * 后台获取业务类型列表
     */
    @RequestMapping("/getGeoUserBusType")
    public void getGeoUserBusType(HttpServletRequest request, HttpServletResponse response){
        try {
            List typeList = new ArrayList();
            List<BusinessType> busType = sceneService.getBusType(getGeoUser().getUniqueCode());
            List<EngineScene> sceneType = sceneService.getGeoUserBusType(getGeoUser().getUniqueCode());
            typeList.add(busType);
            typeList.add(sceneType);
            this.sendData(request, response,typeList);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取业务类型列表失败！");
        }
    }

    /**
     * 前台获取业务类型列表
     */
    @RequestMapping("/getBusType")
    public void getUserBusType(HttpServletRequest request, HttpServletResponse response){
        try {
            List typeList = new ArrayList();
            List<BusinessType> busType = sceneService.getBusType(getUser().getUniqueCode());
            List<EngineScene> sceneType = sceneService.getSceneType(getUser().getUniqueCode());
            typeList.add(busType);
            typeList.add(sceneType);
            this.sendData(request, response,typeList);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取业务类型列表失败！");
        }
    }
}
