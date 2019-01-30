package com.geo.rcs.modules.decision.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.*;
import com.geo.rcs.common.util.csv.CsvUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.modules.approval.controller.ApprovalController;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.service.ApprovalService;
import com.geo.rcs.modules.decision.entity.Decision;
import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.decision.engine.DecisionEngine;
import com.geo.rcs.modules.decision.service.EngineDecisionLogService;
import com.geo.rcs.modules.decision.service.EngineDecisionService;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月03日 上午11:48
 */
@RequestMapping("/engine/decision")
@RestController
public class EngineDecisionController extends BaseController {

    @Autowired
    private DecisionEngine decisionEngine;
    @Autowired
    private EngineDecisionService engineDecisionService;
    @Autowired
    private EngineDecisionLogService engineDecisionLogService;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private SceneService sceneService;

    private static Map map =new HashMap();

    private static Logger logger = LoggerFactory.getLogger(ApprovalController.class);


    /**
     * 新建决策
     * @param engineDecision
     * @param request
     * @param response
     */
    @RequestMapping("/save")
    @RequiresPermissions("engine:decision:save")
    public void save(@RequestBody EngineDecision engineDecision, HttpServletRequest request, HttpServletResponse response){
        try{
            ValidatorUtils.validateEntity(engineDecision,AddGroup.class);
            engineDecision.setCreater(getUser().getName());
            engineDecision.setUserId(getUser().getUniqueCode());
            List<EngineDecision> lists = engineDecisionService.verifyName(engineDecision.getName(),getUser().getUniqueCode(),engineDecision.getBusinessId());
            if (lists.size()>=1){
                this.sendError(request, response, "决策名已存在！");
                return;
            }
            EngineDecision engineDecision1 = engineDecisionService.save(engineDecision);
            this.sendData(request,response,engineDecision1);
        }catch (Exception e){
            this.sendError(request,response,"新建决策失败");
            LogUtil.error("新建决策",engineDecision.toString(),getUser().getName(),e);
        }
    }

    /**
     * 更新决策
     * @param engineDecision
     * @param request
     * @param response
     */
    @RequestMapping("/update")
    public void update(EngineDecision engineDecision, HttpServletRequest request, HttpServletResponse response){
        try{
            if(engineDecision.getActiveStatus() == 1){
                this.sendErrAc(request, response,"此决策集处于激活状态,不能进行此操作");
            }
            else if(engineDecision.getAppStatus() == 1){
                this.sendErrAc(request, response,"此决策集正在审核,不能进行此操作");
            }
            else {
                List<EngineDecision> engineDecisionList = engineDecisionService.verifyName(engineDecision.getName(),getUser().getUniqueCode(),engineDecision.getBusinessId());
                if (engineDecisionList != null){
                    for (int i = 0; i < engineDecisionList.size();i++){
                        if (engineDecisionList.get(i).getId() != (int)engineDecision.getId()){
                            this.sendError(request, response, "决策名已存在！");
                            return;
                        }
                    }
                }
                engineDecision.setCreater(getUser().getName());
                engineDecision.setUserId(getUser().getUniqueCode());
                engineDecision.setAppStatus(0);
                engineDecisionService.update(engineDecision);
                this.sendOK(request, response);
            }
        }catch (Exception e){
            this.sendError(request,response,"更新决策失败！");
            LogUtil.error("更新决策",engineDecision.toString(),getUser().getName(),e);
        }
    }
    /**
     * 更新决策
     * @param engineDecision
     * @param request
     * @param response
     */
    @RequestMapping("/updateFlow")
    @RequiresPermissions("engine:decision:update")
    public void updateFlow(EngineDecision engineDecision, HttpServletRequest request, HttpServletResponse response){
        try{
            engineDecision.setCreater(getUser().getName());
            engineDecision.setUserId(getUser().getUniqueCode());
            List<Decision> list = JSONUtil.jsonToBean(engineDecision.getDecisionFlow(), List.class);
            for (int i =1;i<list.size();i++) {
                Decision decision = JSON.parseObject(JSON.toJSONString(list.get(i)), Decision.class);
                EngineRules allById = ruleSetService.findAllByIdForTest(decision.getRulesId());
                if(new RulesJsonUtil().rulesJsonCheck(allById,request,response) == false){
                    this.sendError(request,response,"规则集信息不完整！");
                    LogUtil.error("更新决策",decision.getRulesId() + "-" + allById.getName(),getUser().getName(),TimeUtil.dqsj(),new Exception());
                    return;
                }
            }
            engineDecisionService.update(engineDecision);
            this.sendOK(request,response);
        }catch (Exception e){
            if(e instanceof RcsException){
                this.sendError(request,response,((RcsException) e).getMsg());
            }else {
                this.sendError(request,response,"更新决策失败");
            }
            LogUtil.error("更新决策",engineDecision.toString(),getUser().getName(),e);
        }
    }

    /**
     * 获取决策
     * @param request
     * @param response
     */
    @RequestMapping("/decisionInfo")
    @RequiresPermissions("engine:decision:list")//权限管理
    public void getDecisionInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map){
        try{
            map.put("userId",getUser().getUniqueCode());
            PageInfo<EngineDecision> pageInfo = new PageInfo<>(engineDecisionService.findByPage(map));
            this.sendData(request,response,pageInfo);
        }catch (Exception e){
            this.sendError(request,response,"获取决策失败");
            LogUtil.error("获取决策","用户"+getUser().getName(),getUser().getName(),e);
        }
    }

    /**
     * 根据编号获取决策
     * @param request
     * @param response
     */
    @RequestMapping("/decisionInfoById")
    @RequiresPermissions("engine:decision:detail")
    public void getDecisionInfoById(Integer id,HttpServletRequest request, HttpServletResponse response){
        try{
            EngineDecision engineDecision = engineDecisionService.selectByPrimaryKey(id);
            this.sendData(request,response,engineDecision);
        }catch (Exception e){
            this.sendError(request,response,"获取决策失败");
            LogUtil.error("获取决策","用户"+getUser().getName(),getUser().getName(),e);
        }
    }

    /**
     * 提交审批
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/toApproval")
    @RequiresPermissions("engine:decision:appr")
    @Transactional(rollbackFor = Exception.class)
    public void toApproval(Integer id, HttpServletRequest request, HttpServletResponse response){
        if(id == null) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {

            EngineDecision engineDecision = engineDecisionService.selectByPrimaryKey(id);
            if(engineDecision.getActiveStatus() == 1){
                this.sendErrAc(request, response,"此决策处于激活状态,不能进行此操作");
            }
            else if(engineDecision.getAppStatus() == 1){
                this.sendErrAc(request, response,"此决策正在审核,不能进行此操作");
            }else {
                engineDecision.setAppStatus(1);
                engineDecisionService.update(engineDecision);
                EngineScene byId = sceneService.findById(engineDecision.getSceneId());
                Approval approval = new Approval();
                if(byId != null){
                    approval.setScene(byId.getName());
                }
                approval.setActionType(4);
                approval.setObjId(5);
                approval.setBusinessId(engineDecision.getBusinessId() == null ? null :engineDecision.getBusinessId());
                approval.setOnlyId(Long.valueOf(id));
                approval.setDescription(engineDecision.getName() == null ? null :engineDecision.getName());
                approval.setSubmitter(getUser().getName());
                approval.setSubTime(new Date());
                approval.setUniqueCode(getUser().getUniqueCode());
                approvalService.addToApproval(approval);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "申请审批失败！");
        }

    }
    /**
     * 审批
     */
    @RequestMapping(value = "/accraditation", method = RequestMethod.POST)
    public void accraditation(HttpServletRequest request, HttpServletResponse response, Approval approval) {

        Approval app = approvalService.selectById(approval.getId());
        if(app != null){
            ResultType resultType = ValidateNull.check(approval, NotNull.RequestType.UPDATE);
            if (ResultType.FAILD.equals(resultType)) {
                this.sendError(request, response, resultType.getMsg());
                return;
            }
            try {
                app.setAppPerson(getUser().getName());
                app.setAppTime(new Date());
                app.setAppStatus(approval.getAppStatus());
                app.setRemarks(approval.getRemarks());
                approvalService.updateAppStatus(app);
                this.sendOK(request, response);
                if(approval.getAppStatus() == 2){
                    logger.info(LogUtil.operation("审批",app.getDescription(),getUser().getName(),TimeUtil.dqsj(),"不通过"));
                }
                else {
                    logger.info(LogUtil.operation("审批",app.getDescription(),getUser().getName(),TimeUtil.dqsj(),"通过"));
                }
            } catch (Exception e) {
                this.sendError(request, response, "审批失败！");
                LogUtil.error("审批", approval.toString(), getUser().getName(), e);

            }
        }
    }

    /**
     * 修改决策集激活状态
     */
    @RequestMapping("/updateActive")

    public void updateActive(EngineDecision engineDecision, HttpServletRequest request, HttpServletResponse response, Approval approval){

        ResultType resultType = ValidateNull.check(engineDecision, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }

        try {
            EngineDecision engineDecision1 = engineDecisionService.selectByPrimaryKey(engineDecision.getId());
            if(engineDecision1.getAppStatus() != 2){
                this.sendErrAc(request, response,"此决策集未通过审核,不能进行此操作");
            }
            else if(engineDecision1.getAppStatus() == 1){
                this.sendErrAc(request, response,"此决策集正在审核,不能进行此操作");
            }else {
                engineDecisionService.update(engineDecision);
                this.sendOK(request, response);
            }
        } catch (Exception e) {
            this.sendError(request, response, "更新决策集失败！");
        }
    }

    /**
     * 删除决策集
     * @param request
     * @param response
     */
    @RequestMapping("/delete")
    @RequiresPermissions("engine:decision:delete")
    public void deleteDecisionById(Integer id,HttpServletRequest request, HttpServletResponse response){
        try{
            engineDecisionService.deleteByPrimaryKey(id);
            this.sendOK(request,response);
        }catch (Exception e){
            this.sendError(request,response,"删除决策失败");
            LogUtil.error("删除决策","用户"+getUser().getName(),getUser().getName(),e);
        }
    }

    /**
     * 获取决策集名称
     * @param request
     * @param response
     */
    @RequestMapping("/getDecision")
    public void getDecisionByUserId(HttpServletRequest request, HttpServletResponse response){
        try{
            List<EngineDecision> engineDecisions = engineDecisionService.getDecisionByUserId(getUser().getUniqueCode());
            this.sendData(request,response,engineDecisions);
        }catch (Exception e){
            this.sendError(request,response,"获取决策集名称失败");
            LogUtil.error("获取决策集名称","用户"+getUser().getName(),getUser().getName(),e);
        }
    }

    /**
     * 获取决策集中所有使用到的规则集(去重)
     * @param request
     * @param response
     */
    @RequestMapping("/getUsageRuleSet")
    public void getUsageRuleSet(HttpServletRequest request, HttpServletResponse response){
        try{
            String[] strings= engineDecisionService.getUsageRuleSet(getUser().getUniqueCode());
            this.sendData(request,response,strings);
        }catch (Exception e){
            this.sendError(request,response,"获取决策集中所有使用到的规则集失败");
            LogUtil.error("获取决策集中所有使用到的规则集","用户"+getUser().getName(),getUser().getName(),e);
          }
    }
    /**
     * ;下载模版
     *
     * @param request
     * @param response
     */
    @SysLog("下载模版")
    @PostMapping("/downloadCsv")
    public void downloadCsv(HttpServletRequest request, HttpServletResponse response,String parameters) {

        if (parameters == null) {
            this.sendError(request, response, " 参数为空");
            return;
        }
        try {
            //定义头部
            StringBuilder headers = new StringBuilder();

            JSONObject  jasonObject = JSONObject.fromObject(parameters);
            Map map = (Map)jasonObject;
            //设置头部
            for (Object key : map.keySet()) {
                headers.append(key+",");
            }
            List exportData = new ArrayList<Map>();

            //第一列显示中文
            exportData.add(map);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileName = sdf.format(new Date()).toString() + "_模版.csv";

            String content =  CsvUtil.formatCsvData(exportData, headers.toString(), headers.toString());
            try {
                CsvUtil.exportCsv(fileName, content,request, response);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            LogUtil.error("下载模版", parameters, getUser().getName(), e);
            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }





}
