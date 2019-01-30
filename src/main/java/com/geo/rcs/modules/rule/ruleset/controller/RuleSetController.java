package com.geo.rcs.modules.rule.ruleset.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.approval.service.PatchDataService;
import com.geo.rcs.modules.decision.controller.EngineDecisionController;
import com.geo.rcs.modules.decision.service.EngineDecisionService;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.geo.rcs.modules.rule.ruleset.entity.EngineHistoryLog;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RecordRulesLogService;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:53
 */
@RestController
@RequestMapping("/rule/ruleSet")
public class RuleSetController extends BaseController{

    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private SceneService sceneService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private PatchDataService patchDataService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EngineRuleService engineRuleService;
    @Autowired
    private RecordRulesLogService recordRulesLogService;
    @Autowired
    private EngineDecisionService engineDecisionService;
    @RequestMapping("/query")
    @RequiresPermissions("rule:ruleset:list")
    public Geo queryRuleSetList(EngineRules ruleSet) {
        Geo geo = Geo.ok();
        try {
            //获取今日时间范围
            DateUtils.DateRange dateRange = DateUtils.getTodayRange();

            //设置查询参数
            Map<String, Object> map = new HashMap<>();
            map.put("startTime", DateUtils.format(dateRange.getStart(), "yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", DateUtils.format(dateRange.getEnd(), "yyyy-MM-dd HH:mm:ss"));
            map.put("userId", getUser().getUniqueCode());
            map.put("senceId", ruleSet.getSenceId());

            //获取昨日进件总量
            Map<String,Object> stat = new HashMap<>();
            List<EventStatEntry> list = eventService.yesterdayEventStat(map);
            if(list != null && !list.isEmpty()){
                stat = JSONUtil.beanToMap(list.get(0));
            }

            //获取所有规则集
            ruleSet.setUniqueCode(getUser().getUniqueCode());
            PageInfo<EngineRules> pageInfo = new PageInfo<>(ruleSetService.findByPage(ruleSet));

            //根据规则集id获取昨日对应进件量
            List<EngineRules> ruleSetList = pageInfo.getList();
            List<Long> rulesIdList = new ArrayList<>();
            if(ruleSetList != null && !ruleSetList.isEmpty()){
                for (EngineRules rules : ruleSetList) {
                    rulesIdList.add(rules.getId());
                }
                map.put("rulesIdList", rulesIdList);
                List<EventStatEntry> listEntry = eventService.yesterdayEventStat(map);
                if(listEntry != null && !listEntry.isEmpty()){
                    for (EngineRules rules : ruleSetList) {
                        for (EventStatEntry entry : listEntry) {
                            if(rules.getId().equals(entry.getRulesId())){
                                rules.setEventStatEntry(entry);
                            }
                        }
                    }
                }
            }
            pageInfo.setList(ruleSetList);

            stat.put("ruleSetCount", pageInfo.getTotal());
            geo.put("stat", stat);
            geo.put("data", pageInfo);
        }catch (Exception e) {
            return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
        }

        return geo;
    }

    /**
     * 前台查询规则管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param ruleSet
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:ruleset:list")//权限管理
    public void getRuleSetList(HttpServletRequest request, HttpServletResponse response, EngineRules ruleSet) {
        try {
            //添加unique_code （客户唯一标识）
            ruleSet.setUniqueCode(getUser().getUniqueCode());
            this.sendData(request, response, new PageInfo<>(ruleSetService.findByPage(ruleSet)));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 后台查询所有规则管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param ruleSet
     */
    @RequestMapping("/allList")
    @RequiresPermissions("rule:list")//权限管理
    public void allList(HttpServletRequest request, HttpServletResponse response, EngineRules ruleSet) {
        try {
            //添加unique_code （客户唯一标识）
            ruleSet.setUniqueCode(getGeoUser().getUniqueCode());

            this.sendData(request, response, new com.geo.rcs.modules.sys.entity.PageInfo<>(ruleSetService.findAllByPage(ruleSet)));

            this.sendOK(request, response);

        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 根据用户获取规则集（包括规则，条件，字段） json形式返回
     */
    @RequestMapping("/getRuleSet")
    @RequiresPermissions("rule:ruleset:detail")
    public void getRuleSet(HttpServletRequest request, HttpServletResponse response, Long id){
        try {
            this.sendData(request, response,ruleSetService.findAllByIdForView(id));
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 添加规则集时先纳入审批
     * @param engineRules
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalAdd")
    @RequiresPermissions("rule:ruleset:save")
    public void toApprovalAdd(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){
        try {
            engineRules.setUniqueCode(getUserId());
            engineRules.setAddUser(getUser().getName());
            engineRules.setAddTime(new Date());
            engineRules.setVerify(0);
            ruleSetService.addEngineRules(engineRules);
            EngineRules engineRules1 = ruleSetService.selectById(engineRules.getId());
            EngineScene byId = sceneService.findById(engineRules1.getSenceId());
            Approval approval = new Approval();
            approval.setActionType(1);
            approval.setThresholdMin(engineRules.getThresholdMin());
            approval.setThresholdMax(engineRules.getThresholdMax());
            approval.setScene(byId.getName());
            approval.setBusinessId(engineRules.getBusinessId());
            approval.setObjId(2);
            approval.setThreshold(engineRules.getThreshold());
            approval.setOnlyId(engineRules1.getId());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(engineRules.getDescrib());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后添加规则集
     */
    @RequestMapping("/confirmAdd")
    @RequiresPermissions("rule:ruleset:save")
    public void addRules(@Validated EngineRules engineRules, HttpServletRequest request, HttpServletResponse response, Approval approval){
        String describ = request.getParameter("describ");
        try {
            if(describ != null) {
                String decode = URLDecoder.decode(describ, "utf-8");
                engineRules.setDescrib(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            //查询该规则集是否存在
            List<EngineRules> list = ruleSetService.selectByName(engineRules.getName(),getUser().getUniqueCode());
            if (list.size() >= 1){
                this.sendError(request, response, "规则集名已存在！");
                return;
            }
            engineRules.setUniqueCode(getUser().getUniqueCode());
            engineRules.setAddUser(getUser().getName());
            engineRules.setAddTime(new Date());
            EngineRules engineRules1 = ruleSetService.addEngineRules(engineRules);
            this.sendData(request,response,engineRules1);
        } catch (ServiceException e) {
            this.sendError(request, response, "更新场景失败！");
        }
    }
    /**
     * 修改规则集时先纳入审批
     * @param patchData
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalUpdate")
    @RequiresPermissions("rule:ruleset:update")
    public void toApprovalUpdate(PatchData patchData, HttpServletRequest request, HttpServletResponse response){
        ResultType resultType = ValidateNull.check(patchData, NotNull.RequestType.UPDATE);

        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            patchData.setActionId(2);
            patchData.setObjId(2);
            patchDataService.insertSelective(patchData);
            EngineScene byId = sceneService.findById(patchData.getSceneId());
            Approval approval = new Approval();
            approval.setActionType(2);
            approval.setScene(byId.getName());
            approval.setBusinessId(patchData.getBusinessId());
            approval.setObjId(2);
            approval.setThresholdMin(patchData.getThresholdMin());
            approval.setThresholdMax(patchData.getThresholdMax());
            approval.setThreshold(patchData.getThreshold());
            approval.setOnlyId(patchData.getOnlyId());
            approval.setSubmitter(getUser().getName());
            approval.setSubTime(new Date());
            approval.setUniqueCode(getUserId());
            approval.setDescription(patchData.getDescribtion());
            approvalService.addToApproval(approval);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 修改规则集(需要查看此规则集激活状态)
     */
    @RequestMapping("/confirmUpdate")
    @RequiresPermissions("rule:ruleset:update")
    public void updateRules(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){

        String describ = request.getParameter("describ");
        try {
            if(describ != null){
                String decode = URLDecoder.decode(describ, "utf-8");
                engineRules.setDescrib(decode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ResultType resultType = ValidateNull.check(engineRules, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }

        try {
            EngineRules engineRules1 = ruleSetService.selectById(engineRules.getId());
            if(engineRules1.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
                return;
            }
            else if(engineRules1.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
                return;
            }
            else {
                engineRules.setVerify(0);
                List<EngineRules> lists = ruleSetService.selectByName(engineRules.getName(),getUser().getUniqueCode());
                if (lists != null) {
                    for (int i = 0; i < lists.size();i++){
                        if (lists.get(i).getId() != (long)engineRules.getId()){
                            this.sendError(request, response, "规则集名字重复！");
                            return;
                        }
                    }
                }
                ruleSetService.updateEngineRules(engineRules);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "更新规则集失败！");
        }
    }
    /**
     * 修改规则集激活状态
     */
    @RequestMapping("/updateActive")
    @RequiresPermissions("rule:ruleset:update")
    public void updateActive(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response, Approval approval){

        ResultType resultType = ValidateNull.check(engineRules, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }

        try {
            EngineRules engineRules1 = ruleSetService.selectById(engineRules.getId());
            String[] usageRuleSet = engineDecisionService.getUsageRuleSet(getUser().getUniqueCode());
            if(usageRuleSet != null){
                for (String s : usageRuleSet) {
                    if(engineRules1.getId().toString().equals(s)){
                        this.sendError(request, response,"该规则集已在决策集中使用，无法修改");
                        return;
                    }
                }
            }

            if(engineRules1.getVerify() != 2){
                this.sendErrAc(request, response,"此规则集未通过审核,不能进行此操作");
                return;
            }
            else if(engineRules1.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
                return;
            }else {
                ruleSetService.updateEngineRulesNo(engineRules);
                this.sendOK(request, response);
            }
        } catch (RcsException e) {
            this.sendError(request, response, "更新规则集失败！");
        }
    }
    /**
     * 删除规则集时先纳入审批
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/toApprovalDelete")
    @RequiresPermissions("rule:ruleset:delete")
    public void toApprovalDelete(Long id, HttpServletRequest request, HttpServletResponse response){
        if(id == null) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
                return;
            }
            else if(engineRules.getVerify() == 1){
                this.sendErrAc(request, response,"此规则集正在审核,不能进行此操作");
                return;
            }else {
                engineRules.setVerify(1);
                ruleSetService.updateEngineRules(engineRules);
                request.getSession().setAttribute("engineRules", engineRules);
                Approval approval = new Approval();
                EngineScene byId = sceneService.findById(engineRules.getSenceId());
                approval.setActionType(3);
                approval.setObjId(2);
                if(!ValidateNull.isNull(byId)){
                    approval.setScene(byId.getName());
                }
                approval.setBusinessId(engineRules.getBusinessId()==null?null:engineRules.getBusinessId());
                approval.setThresholdMin(engineRules.getThresholdMin());
                approval.setThresholdMax(engineRules.getThresholdMax());
                approval.setOnlyId(id);
                approval.setDescription(engineRules.getDescrib());
                approval.setSubmitter(getUser().getName());
                approval.setSubTime(new Date());
                approval.setUniqueCode(getUser().getUniqueCode());
                approvalService.deleteToApproval(approval);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批通过后删除规则集
     */
    @RequestMapping("/confirmDelete")
    public void deleteRules(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
                return;
            }else {
                ruleSetService.delete(id);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }

    }
    /**
     * 删除规则集所有关联
     */
    @RequestMapping("/deleteAbsolute")
    public void deleteAbsolute(Long id, HttpServletRequest request, HttpServletResponse response){
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
                return;
            }else {
                ruleSetService.deleteAbsolute(id);
                this.sendOK(request, response);
            }
        } catch (RcsException e) {
            this.sendError(request, response, "删除失败！");
        }

    }
    /**
     * 获取所属规则集列表
     */
    @RequestMapping("/getRulesType")
    public void getBusType(HttpServletRequest request, HttpServletResponse response){
        try {
            this.sendData(request, response,ruleSetService.getRulesList(getUser().getUniqueCode()));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取业务类型列表失败！");
        }
    }
    /**
     * 获取规则集信息，用于回显数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
            return;
        }
        try {
            EngineRules engineRules = ruleSetService.selectById(id);
            if(engineRules.getActive() == 1){
                this.sendErrAc(request, response,"此规则集处于激活状态,不能进行此操作");
            }
            else {
                this.sendData(request, response, engineRules);
            }
        } catch (RcsException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取规则集以及规则信息，用于回显数据
     */
    @RequestMapping("/getRuleSetAndRuleInfo")
    public void getRuleSetAndRuleInfo(Long id,HttpServletRequest request,HttpServletResponse response){
        if(id == null){
            this.sendError(request, response, "id不能为空！");
        }
        try {
            List<EngineRules> engineRules = engineRuleService.selectRulesById(id);
            if(engineRules.size()  == 0){
                this.sendData(request, response,ruleSetService.selectById(id));
            }else {
                this.sendData(request, response, ruleSetService.getRuleSetAndRuleInfo(id));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看规则集历史变更记录
     */
    @RequestMapping("/queryRulesLog")
    @RequiresPermissions("rule:ruleset:history")
    public Geo queryRulesLog(@Validated EngineHistoryLog engineHistoryLog){
        try {
            if(engineHistoryLog.getRuleSetId() != null){
                engineHistoryLog.setUniqueCode(getUser().getUniqueCode());
                return Geo.ok().put("data",new PageInfo<>(recordRulesLogService.getRecordById(engineHistoryLog)));
            }
            else{
                return Geo.error(StatusCode.RULES_NOTFOUND_ERROR.getCode(),StatusCode.RULES_NOTFOUND_ERROR.getMessage());
            }

        } catch (NumberFormatException e) {
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(),StatusCode.PARAMS_ERROR.getMessage());
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(),StatusCode.SERVER_ERROR.getMessage());
        }

    }
    /**
     * 删除规则集历史变更记录
     */
    @RequestMapping("/deleteRulesLog")
    public Geo deleteRulesLog(Long logId){
        try {
            recordRulesLogService.deleteById(logId);
            return Geo.ok();
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(),StatusCode.SERVER_ERROR.getMessage());
        }

    }

    /**
     * 删除规则集历史变更记录
     */
    @RequestMapping("/getActiveRules")
    public Geo getActiveRules(){
        try {
            List<EngineRules> engineRulesList = ruleSetService.getActiveRules(getUser().getUniqueCode());
            return Geo.ok().put("engineRulesList",engineRulesList);
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(),StatusCode.SERVER_ERROR.getMessage());
        }

    }

    /**
     * 获取测试数据
     */
    @RequestMapping("/getApiEventData")
    public Geo getApiEventData(){
        try {
            return Geo.ok().put("engineRulesList",ruleSetService.getApiEventData());
        }
        catch (Exception e) {
            return Geo.error(StatusCode.SERVER_ERROR.getCode(),StatusCode.SERVER_ERROR.getMessage());
        }

    }
}
