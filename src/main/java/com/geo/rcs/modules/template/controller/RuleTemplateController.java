package com.geo.rcs.modules.template.controller;

import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.RulesJsonUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.condition.service.ConditionService;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.rule.service.EngineRuleService;
import com.geo.rcs.modules.sys.controller.SysLoginController;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.geo.rcs.modules.template.entity.RuleTemplate;
import com.geo.rcs.modules.template.entity.TemplateLog;
import com.geo.rcs.modules.template.entity.TemplateType;
import com.geo.rcs.modules.template.service.RuleTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.template.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月20日 下午2:52
 */
@RestController
@RequestMapping("/rule/template")
public class RuleTemplateController extends BaseController {

    @Autowired
    private RuleSetService ruleSetService;

    @Autowired
    private RuleTemplateService ruleTemplateService;

    @Autowired
    private EngineRuleService engineRuleService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private FieldService fieldService;

    private static Logger logger = LoggerFactory.getLogger(SysLoginController.class);
    /**
     * 查询模版列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param ruleTemplate
     */
    @RequestMapping("/list")
    @RequiresPermissions("rule:template:list")
    public void getConditionList(HttpServletRequest request, HttpServletResponse response, RuleTemplate ruleTemplate) {
        try {

            this.sendData(request, response, new PageInfo<>(ruleTemplateService.findByPage(ruleTemplate)));

            this.sendOK(request, response);

        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取模版列表","",getUser().getName(),e);
        }
    }

    /**
     * 查询模版详情
     *
     * @param request
     * @param response
     * @param id
     */
    @RequestMapping("/detail")
    @RequiresPermissions("rule:template:detail")
    public void templateInfo(HttpServletRequest request, HttpServletResponse response, Integer id) {
        try {

            RuleTemplate ruleTemplate = ruleTemplateService.selectById(id);

            this.sendData(request, response, ruleTemplate);

            this.sendOK(request, response);

        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("查看模版详情",id.toString(),getUser().getName(),e);
        }
    }

    /**
     * 删除模版
     */
    @RequestMapping("/confirmDelete")
    public void deleteTemplate(Integer id, HttpServletRequest request, HttpServletResponse response){
        try {
                ruleTemplateService.delete(id);
                this.sendOK(request, response);
            }
        catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
            LogUtil.error("删除模版",id.toString(),getUser().getName(),e);
        }

    }
    /**
     * 修改模版
     */
    @RequestMapping("/confirmUpdate")
    public void deleteTemplate(RuleTemplate ruleTemplate, HttpServletRequest request, HttpServletResponse response){
        try {
            ruleTemplateService.updateByPrimaryKeySelective(ruleTemplate);
            this.sendOK(request, response);
        }
        catch (ServiceException e) {
            this.sendError(request, response, "修改失败！");
            LogUtil.error("修改模版",ruleTemplate.toString().toString(),getUser().getName(),e);
        }

    }
    /**
     * 前台根据规则集编号获取数据，完成导出
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/export")
    public void importTemplate(Long id, HttpServletRequest request, HttpServletResponse response){

        TemplateLog templateLog = new TemplateLog();
        templateLog.setUserId(getUser().getUniqueCode());
        templateLog.setImportTime(new Date());
        try {
            EngineRules allById = ruleSetService.findAllByIdForTest(id);
            if(new RulesJsonUtil().rulesJsonCheck(allById,request,response) == false){
                this.sendError(request,response,"规则集信息不完整！");
                logger.info("规则集信息不完整,导出结束！");
            }
            else {
                Date beginDate = new Date();
                String rulesTemplate = JSONUtil.beanToJson(allById);
                RuleTemplate ruleTemplate = new RuleTemplate();
                ruleTemplate.setRulesetJson(rulesTemplate);
                ruleTemplate.setAddUser(getUser().getName());
                ruleTemplate.setName(allById.getName());
                ruleTemplateService.save(ruleTemplate);
                this.sendOK(request, response);
                Date endDate = new Date();
                TimeUtil df = new TimeUtil();  //实例化方法
                long startT = df.fromDateStringToLong(beginDate.toString()); //定义上机时间
                long endT = df.fromDateStringToLong(endDate.toString());  //定义下机时间
                long ss = (startT - endT) / (1000); //共计秒数
                templateLog.setImportStatus(1);//成功
                templateLog.setExpendTime(ss + "ms");//成功
                logger.info(LogUtil.operation("规则集模版导出",allById.getId() + "-" +allById.getName(),getUser().getName(),TimeUtil.dqsj(),"完成导出"));
            }
        } catch (ServiceException e) {
            templateLog.setImportStatus(2);//失败
            this.sendError(request, response, "导出失败！");
            LogUtil.error("导出模版",id.toString().toString(),getUser().getName(),e);
        }
        finally {
            ruleTemplateService.saveLog(templateLog);
        }
    }
    /**
     * 后台根据规则集编号获取数据，完成导出
     * @param ruleTemplate
     * @param request
     * @param response
     */
    @RequestMapping("/geoExport")
    public void geoImportTemplate(RuleTemplate ruleTemplate, HttpServletRequest request, HttpServletResponse response,Long rulesId){
        try {
            logger.info(LogUtil.operation("规则集模版导出",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getGeoUser().getName(),TimeUtil.dqsj(),"初始化"));
            EngineRules allById = ruleSetService.findAllByIdForTest(rulesId);
            if(new RulesJsonUtil().rulesJsonCheck(allById,request,response) == false){
                this.sendError(request,response,"规则集信息不完整！");
                logger.info(LogUtil.operation("规则集模版导出",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getGeoUser().getName(),TimeUtil.dqsj(),"规则集信息不完整,导入结束！"));
                return;
            }
            else{
                String rulesTemplate = JSONUtil.beanToJson(allById);
                ruleTemplate.setRulesetJson(rulesTemplate);
                ruleTemplate.setAddUser(getGeoUser().getName());
                ruleTemplate.setName(allById.getName());
                ruleTemplateService.save(ruleTemplate);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "规则集模版导出失败！");
            LogUtil.error("规则集模版导出",ruleTemplate.getId()+ruleTemplate.getName().toString().toString(),getUser().getName(),e);
        }
    }
    /**
     * 校验数据
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/check")
    @RequiresPermissions("rule:template:import")
    public void check(HttpServletRequest request, HttpServletResponse response,Integer id){
        try {
            RuleTemplate ruleTemplate = ruleTemplateService.selectById(id);
            logger.info(LogUtil.operation("规则集模版导出",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getUser().getName(),TimeUtil.dqsj(),"初始化"));
            logger.info(LogUtil.operation("规则集模版导出",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getUser().getName(),TimeUtil.dqsj(),"正在校验数据"));
            //把拿到的json字符串转成 json对象
            JSONObject jsStr = JSONObject.parseObject(ruleTemplate.getRulesetJson());
            //json对象转换成java对象
            EngineRules engineRules = (EngineRules) JSONObject.toJavaObject(jsStr,EngineRules.class);
            if(new RulesJsonUtil().rulesJsonCheck(engineRules,request,response) == false){
                this.sendError(request,response,"规则集信息不完整！");
                logger.info(LogUtil.operation("规则集模版导出",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getUser().getName(),TimeUtil.dqsj(),"规则集信息不完整,导入结束！"));
            }
            else{
                this.sendOK(request, response);
            }
        } catch (Exception e) {
            this.sendError(request, response, "校验失败！");
            LogUtil.error("规则集模版导出",id.toString().toString(),getUser().getName(),e);
        }
    }
    /**
     * 将json解析存入对应的表并关联客户
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/import")
    @RequiresPermissions("rule:template:import")
    public void importTemplate(Integer id, HttpServletRequest request, HttpServletResponse response){
        TemplateLog templateLog = new TemplateLog();
        templateLog.setUserId(getUser().getUniqueCode());
        templateLog.setImportTime(new Date());
        templateLog.setModelId(id);
        try {
            RuleTemplate ruleTemplate = ruleTemplateService.selectById(id);
            ruleTemplate.setExportNum(ruleTemplate.getExportNum()+1);
            ruleTemplateService.updateByPrimaryKeySelective(ruleTemplate);
            //把拿到的json字符串转成 json对象
            JSONObject jsStr = JSONObject.parseObject(ruleTemplate.getRulesetJson());
            //json对象转换成java对象
            EngineRules engineRules = (EngineRules) JSONObject.toJavaObject(jsStr,EngineRules.class);
            if(new RulesJsonUtil().rulesJsonCheck(engineRules,request,response) == false){
                this.sendError(request,response,"规则集信息不完整！");
                logger.info(LogUtil.operation("规则集模版导出",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getUser().getName(),TimeUtil.dqsj(),"规则集信息不完整,导入结束！"));
            }else{

                logger.info(LogUtil.operation("规则集模版导入",ruleTemplate.getId() + "-" + ruleTemplate.getName(),getUser().getName(),TimeUtil.dqsj(),"校验通过,正在写入数据"));
                //首先添加规则集
                engineRules.setUniqueCode(getUser().getUniqueCode());
                engineRules.setAddTime(new Date());
                engineRules.setAddUser(getUser().getName());
                engineRules.setActive(0);
                //查询该规则集是否存在

                List<EngineRules> list = ruleSetService.selectByName(engineRules.getName(),getUser().getUniqueCode());
                if (list.size() >= 1){
                    engineRules.setName(engineRules.getName()+"("+ruleTemplate.getExportNum()+")");
                }

                EngineRules engineRules1 = ruleSetService.addEngineRules(engineRules);

                if(engineRules.getRuleList() != null){
                    //添加规则
                    for (EngineRule engineRule : engineRules.getRuleList()) {
                        engineRule.setUniqueCode(getUser().getUniqueCode());
                        engineRule.setAddTime(new Date());
                        engineRule.setAddUser(getUser().getName());
                        engineRule.setRulesId(engineRules1.getId());
                        engineRule.setConditionNumber(0);

                        EngineRule engineRule1 = engineRuleService.save(engineRule);

                        //获取原数据的条件关联关系
                        String conditionRelationship = engineRule1.getConditionRelationship();

                        //存储原数据的条件Id
                        List<Long> oldIdList = new ArrayList<>();
                        for (Conditions conditions : engineRule1.getConditionsList()) {
                            oldIdList.add(conditions.getId());
                        }
                        if(engineRule.getConditionsList() != null){
                            //添加条件
                            for (Conditions conditions : engineRule.getConditionsList()) {
                                conditions.setUniqueCode(getUser().getUniqueCode());
                                conditions.setAddTime(new Date());
                                conditions.setAddUser(getUser().getName());
                                conditions.setRulesId(engineRules1.getId());
                                conditions.setRuleId(engineRule1.getId());
                                Conditions conditions1 = conditionService.saveNoUp(conditions);

                                //字段关联关系编号替换并更新
                                for(int i=0; i<engineRule.getConditionsList().size(); i++){

                                    conditionRelationship =  conditionRelationship.replace(String.valueOf(oldIdList.get(i)),String.valueOf(engineRule.getConditionsList().get(i).getId()));
                                    engineRule.setConditionRelationship(conditionRelationship);
                                    //修改条件关联关系
                                    engineRuleService.addConditionsRs(engineRule);
                                }


                                //获取原数据的字段关联关系
                                String fieldRelationship = conditions1.getFieldRelationship();

                                //存储原数据的条件Id
                                List<Long> oldFieldIdList = new ArrayList<>();
                                for (EngineField field : conditions.getFieldList()) {
                                    oldFieldIdList.add(field.getId());
                                }

                                if(conditions.getFieldList() != null){
                                    for (EngineField engineField : conditions.getFieldList()) {
                                        engineField.setUniqueCode(getUser().getUniqueCode());
                                        engineField.setAddTime(new Date());
                                        engineField.setAddUser(getUser().getName());
                                        engineField.setRulesId(engineRules1.getId());
                                        engineField.setConditionId(conditions1.getId());
                                    }
                                    //新字段集合
                                    List<EngineField> engineFields = fieldService.addFieldBatchNoUp(conditions.getFieldList());

                                    //字段关联关系编号替换并更新
                                    for(int i=0; i<engineFields.size(); i++){
                                        fieldRelationship =  fieldRelationship.replace(String.valueOf(oldFieldIdList.get(i)),String.valueOf(engineFields.get(i).getId()));
                                        conditions.setFieldRelationship(fieldRelationship);
                                        conditionService.addFieldRs(conditions);
                                    }
                                }
                                this.sendData(request, response,engineRules);
                                templateLog.setImportStatus(1);//失败

                            }
                        }
                        this.sendError(request, response,"规则集信息不完整");
                    }
                }
                templateLog.setImportStatus(2);//失败
                this.sendError(request, response,"规则集信息不完整");
                logger.info(LogUtil.operation("规则集模版导入", ruleTemplate.getId() + "-" +ruleTemplate.getName(),getUser().getName(),TimeUtil.dqsj(),"导入完成"));
            }
        } catch (ServiceException e) {
            templateLog.setImportStatus(2);//失败
            this.sendError(request, response, "导入失败！");
            LogUtil.error("导入失败",id.toString().toString(),getUser().getName(),e);
        }
        finally {
            ruleTemplateService.saveLog(templateLog);
        }
    }

    @RequestMapping("/templateType")
    public void getTemplateType(HttpServletRequest request, HttpServletResponse response){
        List<TemplateType>  templateTypes = ruleTemplateService.getTemplateType();
        this.sendData(request,response,templateTypes);
    }
}
