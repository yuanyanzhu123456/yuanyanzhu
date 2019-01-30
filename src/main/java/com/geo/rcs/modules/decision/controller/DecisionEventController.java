package com.geo.rcs.modules.decision.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.constant.ConfigConstant;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.common.util.pdf.PdfUtil;
import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.decision.entity.EngineDecisionLog;
import com.geo.rcs.modules.decision.entity.ExcuteFlowForApi;
import com.geo.rcs.modules.decision.entity.ImgBaseForApi;
import com.geo.rcs.modules.decision.service.EngineDecisionLogService;
import com.geo.rcs.modules.decision.service.EngineDecisionService;
import com.geo.rcs.modules.decision.util.Base64Utils;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.decision.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年09月19日 上午11:59
 */
@RequestMapping("/decision/event")
@RestController
public class DecisionEventController extends BaseController{

    @Autowired
    private EngineDecisionLogService engineDecisionLogService;
    @Autowired
    private EngineDecisionService engineDecisionService;
    @Autowired
    RuleSetService ruleSetService;


    private static final String START_TIME = "startTime";

    private static final String END_TIME = "endTime";

    private static final String RESULT_REPORT_IMAGE = "static/pdf/result/image";

    @Value("${geo.event.upperDownload}")
    private Long  upperDownload;

    /**
     * 查询决策进件列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param engineDecisionLog
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @RequiresPermissions("decision:event:list")//权限管理
    public void getDecisionLogList(HttpServletRequest request, HttpServletResponse response, @RequestBody EngineDecisionLog engineDecisionLog) {
        try {
            //添加unique_code （客户唯一标识）
            engineDecisionLog.setUserId(getUser().getUniqueCode());
            PageInfo<EngineDecisionLog> pageInfo = new PageInfo<>(engineDecisionLogService.findByPage(engineDecisionLog));
            this.sendData(request, response, pageInfo);

            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取决策历史列表",engineDecisionLog.toString(),getUser().getName(),e);
        }
    }

    /**
     * 进件记录详情
     *
     * @param id
     * @return
     */
    @PostMapping("/getEventDetail")
    @RequiresPermissions("decision:event:detail")
    public Geo getEntryDetail(Long id) {
        try {
            if (id != null) {
                EngineDecisionLog engineDecisionLog = engineDecisionLogService.selectByPrimaryKey(id);
                return Geo.ok().put("engineDecisionLog", engineDecisionLog);
            }
            return Geo.error("编号不能为空，获取数据失败！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LogUtil.error("历史详情",id.toString(), getUser().getName(), e);
            e.printStackTrace();
            return  Geo.error();
        }

    }

    /**
     * 决策进件概览
     */
    @RequestMapping("/eventSurvey")
    @RequiresPermissions("decision:event:eventSurvey")//权限管理
    public void eventSurvey(HttpServletRequest request,HttpServletResponse response){

        try{
            Map<String, Object> map = new HashMap<>();
            //Todo redis拓展，非实时更新 key-eventSurvey value-hashMap1
            map.put("userId",getUser().getUniqueCode());
            int total = engineDecisionService.getDecisionTotal(map);
            Map<String, Object> eventSurvey = engineDecisionLogService.selectBySelective(map);
            eventSurvey.put("decisionTotal",total);
            this.sendData(request,response,eventSurvey);
        }catch (Exception e){
            LogUtil.error("进件概览","", getUser().getName(), e);
            e.printStackTrace();
            this.sendError(request,response,"进件分析失败");
        }

    }

    /**
     * 决策进件趋势分析
     */
    @RequestMapping("/eventTrend")
    @RequiresPermissions("decision:event:eventTrend")//权限管理
    public void eventTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

        try{
            map.put("userId",getUser().getUniqueCode());
            Map<String, Object> stringObjectMap = engineDecisionLogService.selectBySelective(map);
            this.sendData(request,response,stringObjectMap);
        }catch (Exception e){
            LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
            e.printStackTrace();
            this.sendError(request,response,"进件分析失败");
        }

    }

    /**
     * 决策进件总量趋势分析
     */
    @RequestMapping("/eventCountTrend")
    @RequiresPermissions("decision:event:eventCountTrend")//权限管理
    public Geo eventCountTrend(@RequestBody Map<String,Object> map){

        if (map == null || map.isEmpty()) {
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "参数不能为空！");
        }
        String startTime = (String)map.get(START_TIME);
        String endTime = (String)map.get(END_TIME);
        if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
        }
        if(!DateUtils.compareTo(startTime, endTime)) {
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
        }
        map.put("userId",getUser().getUniqueCode());
        Map<String,Object> map1 = engineDecisionLogService.getEventCountTrend(map);

        return Geo.ok().put("data", map1);

    }
    /**
     * 决策进件结果趋势分析
     */
    @RequestMapping("/eventStatusTrend")
    @RequiresPermissions("decision:event:eventStatusTrend")//权限管理
    public void eventStatusTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

        try{
            map.put("userId",getUser().getUniqueCode());
            Map<String, Object> stringObjectMap = engineDecisionLogService.getEventStatusTrend(map);
            this.sendData(request,response,stringObjectMap);
        }catch (Exception e){
            LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
            e.printStackTrace();
            this.sendError(request,response,"进件分析失败");
        }

    }
    /**
     * 决策进件分数趋势分析
     */
    @RequestMapping("/eventScoreTrend")
    @RequiresPermissions("decision:event:eventScoreTrend")//权限管理
    public void eventScoreTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

        try{
            map.put("userId",getUser().getUniqueCode());
            List<Map<String, Object>> eventScoreTrend = engineDecisionLogService.getEventScoreTrend(map);
            this.sendData(request,response,eventScoreTrend);
        }catch (Exception e){
            LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
            e.printStackTrace();
            this.sendError(request,response,"进件分析失败");
        }

    }
    /**
     * 决策进件耗时趋势分析
     */
    @RequestMapping("/eventCostTrend")
    @RequiresPermissions("decision:event:eventCostTrend")//权限管理
    public void eventCostTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

        try{
            map.put("userId",getUser().getUniqueCode());
            List<Map<String, Object>> eventCostTrend = engineDecisionLogService.getEventCostTrend(map);
            this.sendData(request,response,eventCostTrend);
        }catch (Exception e){
            LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
            e.printStackTrace();
            this.sendError(request,response,"进件分析失败");
        }

    }

    /**
     * 决策管理进件列表导出 excel
     *
     * @param request
     * @param response
     * @param engineDecisionLog
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    @RequiresPermissions("decision:excel:export")
    public Geo exportExcel(HttpServletRequest request, HttpServletResponse response, String exportType, Long[] ids, EngineDecisionLog engineDecisionLog) {

        try {
            SysUser user = getUser();
            engineDecisionLog.setUserId(user.getUniqueCode());
            if (user == null) {
                return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
            }
            Page<Map<String, Object>> byPageAll = null;
            if ("array".equalsIgnoreCase(exportType)) {
                byPageAll = engineDecisionLogService.findByPageIds(ids);
            } else {
                if ("all".equalsIgnoreCase(exportType)) {
                    engineDecisionLog = new EngineDecisionLog();
                    engineDecisionLog.setUserId(user.getUniqueCode());
                    byPageAll = engineDecisionLogService.findByPageAll(engineDecisionLog);
                } else if ("condition".equalsIgnoreCase(exportType)) {
                    byPageAll = engineDecisionLogService.findByPageAll(engineDecisionLog);
                } else {
                    return Geo.error(StatusCode.PARAMS_ERROR.getMessage());
                }
            }
            if (byPageAll != null && byPageAll.size() > 0) {
                if (byPageAll.size() > ConfigConstant.EXCEL_EXPORT_ROWS_MAX) {
                    return Geo.error("数据量太大,请重新筛选!");
                }
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.addAll(byPageAll);
                String fileName = "决策进件统计_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
                String[] headers = new String[]{"ID","渠道", "业务类型", "场景","决策集", "姓名", "身份证", "手机号", "进件时间", "决策结果"};
                String[] props = new String[]{"id","channel", "businessId","senceName", "decisionId", "realName", "idCard", "mobile",
                        "createTime", "sysStatus"};
                ExportExcel excel = new ExportExcel("决策进件统计", headers, 0).setDataList2(mapList, props);
                try {
                    excel.write(request,response, fileName).dispose();
                    return Geo.ok(StatusCode.SUCCESS.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
                }
            } else {
                return Geo.ok("暂无可导出的数据!");
            }
        } catch (Exception e) {
            LogUtil.error("决策管理进件列表,导出Excel失败!", engineDecisionLog.toString(), getUser().getName(), e);
            return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());

        }
    }

    /**
     * 打印在线决策pdf
     * @param idd
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/print")
    @RequiresPermissions("decision:event:print")
    public Geo printDecisionEvent(@RequestParam("decisionId") String decisionIdd,@RequestParam("id") String idd,@RequestParam("imgBase") String imgStr, HttpServletRequest request, HttpServletResponse response){
        try {
            //set ContentType:application/pdf
            response.setHeader("Content-Type","application/pdf");

            //save result_image.png to local
            String s = "data:image/png;base64,";
            String imgBase = imgStr.replace(s,"");
            Base64Utils.Base64ToImage(imgBase,RESULT_REPORT_IMAGE);

            Long id = Long.valueOf(idd);
            Integer decisionId = Integer.valueOf(decisionIdd);
            //generate pdf data
            EngineDecision engineDecision = engineDecisionService.selectByPrimaryKey(decisionId);
            EngineDecisionLog data = engineDecisionLogService.selectByPrimaryKey(id);
            //参数key英文替换为中文
            data.setParameters(MatchChEn(engineDecision,data));

            Map<String,Object> map = ParseData(data);
            String name = data.getRealName()==null?"":data.getRealName();
            String fileName = String.format("事件报告" + name + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS") + ".pdf");

            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //以附件形式下载
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            //send pdf to browser
            boolean flag = PdfUtil.pdfWriterForDecision(map, response);

            if (!flag) {
                return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
            }
        }catch (Exception e){

            e.printStackTrace();
            return  Geo.error("下载报告失败");
        }

        return Geo.ok(StatusCode.SUCCESS.getMessage());

    }

    /**
     * 查看在线决策pdf
     *
     * @param id
     * @param imgStr
     * @return
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("decision:event:info")
    public Geo viewDecisionEvent(@PathVariable("id") Long id,@RequestParam("imgBase") String imgStr, HttpServletResponse response) {
        try {

            //save result_image.png to local
            String s = "data:image/png;base64,";
            String imgBase = imgStr.replace(s,"");
            Base64Utils.Base64ToImage(imgBase,RESULT_REPORT_IMAGE);

            //generate pdf data
            EngineDecisionLog data = engineDecisionLogService.selectByPrimaryKey(id);
            Map<String,Object> map = ParseData(data);
            String fileName = "事件报告"+data.getRealName()+DateUtils.format(new Date(),"yyyyMMddHHmmssSSS")+".pdf";
            try {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setHeader("Content-Type","application/pdf");
            response.setHeader("Content-Disposition","inline;filename="+fileName);

            //send pdf to Browser
            boolean flag = PdfUtil.pdfWriterForDecision(map, response);

            if (!flag) {
                return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
            }

            return Geo.ok(StatusCode.SUCCESS.getMessage());
        } catch (RcsException e) {
            LogUtil.error(" 查看报告", id.toString(), getUser().getName(), e);
            e.printStackTrace();
            return  Geo.error(e.getCode(),e.getMsg());
        }catch (Exception e) {
            LogUtil.error(" 查看报告",id.toString(), getUser().getName(), e);

            e.printStackTrace();
            return  Geo.error("查看报告失败");
        }
    }


    /**
     * 批量下载报告
     * @param imgBaseForApis  id,imgBases
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/batchExport",method = RequestMethod.POST)
    public void batchExport(@RequestBody List<ImgBaseForApi> imgBaseForApis, HttpServletRequest request, HttpServletResponse response){
        List<Map<String,Object>> dataList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        try{
            if (imgBaseForApis == null || imgBaseForApis.size() == 0){
                this.sendError(request,response,"请先选择数据");
            }else if (imgBaseForApis.size() > upperDownload){
                this.sendError(request,response,"单量批次上线不能超过" + upperDownload.toString()+"条");
            }

                for (ImgBaseForApi api : imgBaseForApis){
                    EngineDecisionLog event = engineDecisionLogService.selectByPrimaryKey(api.getId());
                    if (event != null){
                        Map<String,Object> eventData = ParseData(event);
                        eventData.put("imgBase",api.getImgBase());
                        dataList.add(eventData);
                    }
                }

                //generate zip and send to browser
                map = PdfUtil.zipWriterForDecision(dataList,request,response,RESULT_REPORT_IMAGE);
            }catch (Exception e){
            LogUtil.error("批量导出报告","","",e);
                e.printStackTrace();
            }
        this.sendData(request,response,map);
    }

    @RequestMapping("/downloadZip")
    public void downloadZip(HttpServletRequest request,HttpServletResponse response,String zipPath,String fileName,String dir){
        try {
            PdfUtil.downloadZip(response,request,zipPath,fileName,dir);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析EngineDecisionLog数据
     * @param data
     * @return
     */
    private  Map<String,Object>  ParseData(EngineDecisionLog data){
        //解析数据
        String excuteFlow = data.getExcuteFlow();
        List<ExcuteFlowForApi> decisionsForBean = new ArrayList<>();
        Map<Long,String> rulesNamesMap = new HashMap<>();
        Map<Long,Integer> rulesMatchTypeMap = new HashMap<>();
        String decisionName = null;
        Integer count = 0;

        if (!StringUtils.isEmpty(excuteFlow)){
            //只转为list,并未解析ExcuteFlowForApi，需要再解析
            List<ExcuteFlowForApi> decisions = JSONUtil.jsonToBean(excuteFlow,List.class);
            for (Object obj : decisions){
                String decisionStr = JSON.toJSONString(obj);
                ExcuteFlowForApi decision = JSONUtil.jsonToBean(decisionStr,ExcuteFlowForApi.class);
                decisionsForBean.add(decision);
                Long rulesId = decision.getRulesId();
                EngineRules engineRules = ruleSetService.selectById(rulesId);
                String rulesName = engineRules.getName();
                Integer matchType = engineRules.getMatchType();
                rulesNamesMap.put(rulesId,rulesName);
                rulesMatchTypeMap.put(rulesId,matchType);
            }
        }

        Integer decisionId = data.getDecisionId();
        EngineDecision engineDecision = engineDecisionService.selectByPrimaryKey(decisionId);
        if (engineDecision == null){
            decisionName = "暂无";
        }else{
            decisionName = engineDecision.getName();
        }

        Map<String,Object> map = new HashMap<>();
        map.put("data",data);
        map.put("rulesNamesMap",rulesNamesMap);
        map.put("rulesMatchTypeMap",rulesMatchTypeMap);
        map.put("decisions",decisionsForBean);
        map.put("decisionName",decisionName);
        map.put("count",count);
        return map;
    }

    /**
     * 查询决策进件列表（模糊，全部）
     *
     * @param request
     * @param response
     * @param engineDecisionLog
     */
    @RequestMapping(value = "/all",method = RequestMethod.POST)
    public void getDecisionLogAll(HttpServletRequest request, HttpServletResponse response,  @RequestBody EngineDecisionLog engineDecisionLog) {
        try {
            //添加unique_code （客户唯一标识）
            engineDecisionLog.setUserId(getUser().getUniqueCode());
            List<Map<String,Object>> dataList = engineDecisionLogService.findAllPdfData(engineDecisionLog);
            this.sendData(request, response, dataList);

            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "获取全部数据失败！");
            LogUtil.error("获取全部数据失败",engineDecisionLog.toString(),getUser().getName(),e);
        }
    }

    /**
     * 参数中英文匹配
     * 例：
     * params1：{"name":"zxw"},params2:{"name":"姓名"}
     * params:{"姓名":"zxw"}
     * @param engineDecision
     * @param engineDecisionLog
     * @return
             */
    public String MatchChEn(EngineDecision engineDecision,EngineDecisionLog engineDecisionLog){

        //映射中英文匹配
        String params1 = engineDecision.getParameters();
        String params2 = engineDecisionLog.getParameters();
        Map<String,String> params = new HashedMap();
        if (params1 != null) {
            Map<String, String> paramsMap1 = JSONUtil.jsonToMap(params1);
            if (params2 != null){
                Map<String,String> paramsMap2 = JSONUtil.jsonToMap(params2);
                for (Map.Entry<String,String> entry1 : paramsMap1.entrySet()){
                    for (Map.Entry<String,String> entry2: paramsMap2.entrySet()){
                        if (entry1.getKey().equalsIgnoreCase(entry2.getKey())){
                            params.put(entry1.getValue(), entry2.getValue());
                        }
                    }
                }
            }
        }
        String paramsJson = JSONUtil.beanToJson(params);
        return paramsJson;
    }



}
