package com.geo.rcs.modules.abtest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.*;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.common.util.excel.ReadExcel;
import com.geo.rcs.common.util.pdf.PdfUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.abtest.service.AbTestService;
import com.geo.rcs.modules.decision.entity.DecisionForApi;
import com.geo.rcs.modules.decision.entity.EngineDecision;
import com.geo.rcs.modules.decision.entity.ExcuteFlowForApi;
import com.geo.rcs.modules.decision.service.EngineDecisionService;
import com.geo.rcs.modules.decision.util.Base64Utils;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.sys.controller.SysLoginController;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年10月25日 下午4:47
 */
@RestController
@RequestMapping("/abTest")
public class AbTestController extends BaseController {

	@Autowired
	private AbTestService abTestService;
	@Autowired
	private EngineDecisionService engineDecisionService;
	@Autowired
	private RuleSetService ruleSetService;

	private static final String RESULT_REPORT_IMAGE = "static/pdf/result/image";

	private static Logger logger = LoggerFactory.getLogger(AbTestController.class);

	@Value("${geo.event.upperDownload}")
	private Long  upperDownload;

	/**
	 * 获取abTest任务列表（模糊，分页）
	 *
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/list")
	@RequiresPermissions("ab:job:list")
	public void getAbJobList(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map) {
		try {
			// 添加unique_code （客户唯一标识）
			map.put("userId",getUser().getUniqueCode());
			this.sendData(request, response,new PageInfo<>(abTestService.findByPage(map)));
		} catch (Exception e) {
			this.sendError(request, response, "获取列表失败！");
			LogUtil.error("获取abTest任务列表（模糊，分页）", map.toString(),getUser().getName(), e);
		}
	}

	/**
	 * 保存abJob
	 *
	 * @param request
	 * @param response
	 * @param abTest
	 */
	@PostMapping("/save")
	@RequiresPermissions("ab:job:save")
	public void saveAbJob(HttpServletRequest request, HttpServletResponse response, AbTest abTest,MultipartFile file) {
		try {
			// 添加unique_code （客户唯一标识）
			abTest.setUniqueCode(getUser().getUniqueCode());
			AbTest abTestJobByName = abTestService.getAbTestJobByName(abTest);
			if(!ValidateNull.isNull(abTestJobByName)){
				throw new RcsException("任务名称已存在",StatusCode.ERROR.getCode());
			}
			Map<String, Object> abTestParmJson = JSONUtil.jsonToMap(abTest.getParameters());

			if (!BlankUtil.isBlank(file)) {
				List<AbScheduleTask> excelInfo = new ReadExcel().getExcelInfoDynamic(file, 0);
				if(excelInfo.size() > 5000) {
					throw new RcsException("单批名单数据不能大于5000条",StatusCode.ERROR.getCode());
				}
				if(excelInfo.size() < 1) {
					throw new RcsException("解析无数据，请上传正确格式的文件",StatusCode.ERROR.getCode());
				}
				ArrayList<AbScheduleTask> scheduleTasks = new ArrayList<>();
				if (!BlankUtil.isBlank(excelInfo)) {
					//比例之和小于100
					if(abTest.getScaleA() + abTest.getScaleB() <= 100){
						for (int i = 0; i < excelInfo.size(); i++) {
							Map<String, Object> taskParmJson = JSONUtil.jsonToMap(excelInfo.get(i).getParmsJson());
							if (!ValidateNull.isNull(excelInfo.get(i).getName()) && i < Math.round(excelInfo.size() * (abTest.getScaleA() / 100.0f))) {
								checkParamMap(taskParmJson,abTestParmJson);
								excelInfo.get(i).setGoalId(abTest.getGoalA());
								excelInfo.get(i).setUserId(getUser().getUniqueCode());
								excelInfo.get(i).setRemark(Integer.valueOf(i).toString());
								excelInfo.get(i).setRole("A");
								scheduleTasks.add(excelInfo.get(i));
							} else if (!ValidateNull.isNull(excelInfo.get(i).getName())  && i >= Math.round(excelInfo.size() * (abTest.getScaleA() / 100.0f)) && i < Math.round(excelInfo.size() * abTest.getScaleB() / 100.0f) + Math.round(excelInfo.size() * abTest.getScaleA() / 100.0f)) {
								checkParamMap(taskParmJson,abTestParmJson);
								excelInfo.get(i).setGoalId(abTest.getGoalB());
								excelInfo.get(i).setUserId(getUser().getUniqueCode());
								excelInfo.get(i).setRemark(Integer.valueOf(i).toString());
								excelInfo.get(i).setRole("B");
								scheduleTasks.add(excelInfo.get(i));
							}
						}
					}
					//比例之和大于100
					else {
						for (int i =0;i<excelInfo.size();i++) {
							if(!ValidateNull.isNull(excelInfo.get(i).getName())  &&  i<=Math.round(excelInfo.size() * abTest.getScaleA() / 100)){
								excelInfo.get(i).setGoalId(abTest.getGoalA());
								excelInfo.get(i).setUserId(getUser().getUniqueCode());
								excelInfo.get(i).setRemark(Integer.valueOf(i).toString());
								excelInfo.get(i).setRole("A");
								scheduleTasks.add(excelInfo.get(i));
							}

						}
						List<AbScheduleTask> excelInfo1 = new ReadExcel().getExcelInfoDynamic(file, 0);
						for (int i =0;i<excelInfo1.size();i++) {
							if (!ValidateNull.isNull(excelInfo.get(i).getName())  &&  i >= Math.round(excelInfo.size() * (abTest.getScaleA() / 100) - Math.round(excelInfo.size() * abTest.getScaleB() / 100)) && i <= Math.round(excelInfo.size() * abTest.getScaleB() / 100) + Math.round(excelInfo.size() * abTest.getScaleA() / 100)) {
								excelInfo1.get(i).setGoalId(abTest.getGoalB());
								excelInfo1.get(i).setUserId(getUser().getUniqueCode());
								excelInfo1.get(i).setRemark(Integer.valueOf(i).toString());
								excelInfo1.get(i).setRole("B");
								scheduleTasks.add(excelInfo1.get(i));
							}
						}

					}
					if(scheduleTasks.size() == 0){
						throw new RcsException("解析无数据，请重新分配比例",StatusCode.FAIL_PARSE_ERRO.getCode());
					}
					else{
						AbTest abTest1 = abTestService.saveAbJob(abTest);
						for (AbScheduleTask scheduleTask : scheduleTasks) {
							scheduleTask.setJobId(abTest1.getJobId());
						}
						abTestService.saveTaskBatch(scheduleTasks);
						this.sendData(request,response,abTest1);
					}
				}
				else{
					throw new RcsException("解析无数据，请上传正确格式的文件",StatusCode.FAIL_PARSE_ERRO.getCode());
				}

			}
			else{
				throw new RcsException("解析无数据，请上传正确格式的文件",StatusCode.FAIL_PARSE_ERRO.getCode());
			}
		} catch (RcsException e) {
			this.sendError(request, response,e.getMsg());
			LogUtil.error("保存abJob", abTest.toString(),getUser().getName(), e);
		}
		catch (Exception e) {
			this.sendError(request, response,e.getMessage());
			LogUtil.error("保存abJob", abTest.toString(),getUser().getName(), e);
		}
	}


	/**
	 * 删除任务预查询（是否有未执行完的自任务）
	 */
	@SysLog("删除任务预查询")
	@RequestMapping("/deleteJob")
	public Geo beforeDelete(@RequestBody Map<String, Object> map){
		map.put("userId",getUser().getUniqueCode());
		List<AbScheduleTask> scheduleTasks = abTestService.getTaskByJobId(map);
		if(scheduleTasks.size() > 0){
			return Geo.error().put("msg","该任务下还有名单暂未执行");
		}else{
			abTestService.deleteBatch(Long.valueOf(map.get("jobId").toString()));
			return Geo.ok();
		}
	}

	/**
	 * 结果统计列表（模糊，分页）
	 *
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping("/resultStat")
	@RequiresPermissions("ab:task:list")
	public void getResultStat(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map) {
		try {
			// 添加unique_code （客户唯一标识）
			map.put("userId",getUser().getUniqueCode());
			this.sendData(request, response, new PageInfo<>(abTestService.getTaskResultList(map)));
		} catch (Exception e) {
			this.sendError(request, response, "获取列表失败！");
			LogUtil.error("获取abTest任务列表（模糊，分页）", map.toString(),getUser().getName(), e);
		}
	}

	/**
	 * 进件量统计
	 * @return
	 */
	@PostMapping("/eventStat")
	@RequiresPermissions("ab:event:stat")
	public Geo todayEventStat(@RequestBody Map<String,Object> map){
		try {
			//设置查询参数
			map.put("userId", getUser().getUniqueCode());
			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", abTestService.getEventStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("  今日事件统计",map.toString(), getUser().getName(), e);
			return Geo.error();
		}
	}
	/**
	 * 查看详情
	 * @return
	 */
	@PostMapping("/detail")
	@RequiresPermissions("ab:job:detail")
	public Geo getTaskDetail(@RequestBody Map<String,Object> map){
		try {
			//设置查询参数
			map.put("userId", getUser().getUniqueCode());
			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", abTestService.getTaskDetail(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("  查看详情",map.toString(), getUser().getName(), e);
			return Geo.error();
		}
	}

	/**
	 * 导出Excel
	 * @return
	 */
	@SysLog("下载名单模版")
	@RequestMapping(value = "/export",method = RequestMethod.POST)
	@ResponseBody
	public Geo export(HttpServletResponse response, HttpServletRequest request, String parmsMapStr){

		try {
			HashMap<String, String> parmsMap = JSON.parseObject(parmsMapStr, HashMap.class);
			String fileName = "名单模版_"+ DateUtils.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
			String[] enParms=new String[parmsMap.size()];
			String[] chParms=new String[parmsMap.size()];
			int i=0;
			for (String key: parmsMap.keySet()){
             enParms[i]=key;
             chParms[i]= parmsMap.get(key);
             i++;
			}
			ExportExcel excel = new ExportExcel("名单模版", enParms,chParms);
			excel.write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.error("下载名单模版", "", getUser().getName(), e);
			return Geo.error(9000,LogUtil.getStackTraceInfo(e));

		}
		return Geo.ok();
	}
    /**
     * 中断节点统计
     *
     * @return
     */
    @PostMapping("/interruptNode")
	@RequiresPermissions("ab:event:stat")
    public void interruptNode(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) {
        try {
            // 添加unique_code （客户唯一标识）
            map.put("userId", getUser().getUniqueCode());
            Map taskResultListAll = abTestService.getTaskResultListAll(map);
            this.sendData(request, response, taskResultListAll);
        } catch (Exception e) {
            this.sendError(request, response, "获取统计结果失败！");
            LogUtil.error("获取统计结果失败", map.toString(), getUser().getName(), e);
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
	@RequiresPermissions("ab:report:view")
	public Geo printDecisionEvent(@RequestParam("id") String idd,@RequestParam("imgBase") String imgStr, HttpServletRequest request, HttpServletResponse response){
		try {
			//set ContentType:application/pdf
			response.setHeader("Content-Type","application/pdf");

			//save result_image.png to local
			String s = "data:image/png;base64,";
			String imgBase = imgStr.replace(s,"");
			Base64Utils.Base64ToImage(imgBase,RESULT_REPORT_IMAGE);

			Long id = Long.valueOf(idd);
			//generate pdf data
			Map<String, Object> hashMap = new HashMap<>();
			hashMap.put("id",idd);
			hashMap.put("userId",getUser().getUniqueCode());
			AbScheduleTask taskDetail = abTestService.getTaskDetail(hashMap);
			Map<String,Object> map = ParseData(taskDetail);

			String fileName = String.format("事件报告" + taskDetail.getName() + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf");

			try {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//以附件形式下载
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			//send pdf to browser
			boolean flag = PdfUtil.pdfWriterForAbTest(map, response);

			if (!flag) {
				return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
			}
		}catch (Exception e){

			e.printStackTrace();
			return  Geo.error(e.getMessage());
		}

		return Geo.ok(StatusCode.SUCCESS.getMessage());

	}

	/**
	 * 导出pdf报告
	 * @param
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/exportPdf")
	@RequiresPermissions("ab:report:export")
	public Geo printDecisionEvent(@RequestParam("imgBase") String imgStr, HttpServletRequest request, HttpServletResponse response){
		try {
			//set ContentType:application/pdf
			response.setHeader("Content-Type","application/pdf");

			//save result_image.png to local
			String s = "data:image/png;base64,";
			String imgBase = imgStr.replace(s,"");
			Base64Utils.Base64ToImage(imgBase,RESULT_REPORT_IMAGE);

			//generate pdf data
			Map<String, Object> hashMap = new HashMap<>();

			String fileName = String.format("结果统计" + "" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf");

			try {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//以附件形式下载
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			//send pdf to browser
			boolean flag = PdfUtil.pdfWriterForAbTestResult(hashMap, response);

			if (!flag) {
				return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
			}
		}catch (Exception e){

			e.printStackTrace();
			return  Geo.error(e.getMessage());
		}

		return Geo.ok(StatusCode.SUCCESS.getMessage());

	}



	/**
	 * 解析EngineDecisionLog数据
	 * @param data
	 * @return
	 */
	private  Map<String,Object>  ParseData(AbScheduleTask data){
		//解析数据
		DecisionForApi decisionForApi = JSONObject.parseObject(data.getResultMap(),DecisionForApi.class) ;
		Map<Long,String> rulesNamesMap = new HashMap<>();
		Map<Long,Integer> rulesMatchTypeMap = new HashMap<>();
		String decisionName = null;
		//命中风险数量，暂保存，待计算
		Integer count = 0;
		List<ExcuteFlowForApi> decisions = new ArrayList<>();

		if (!ValidateNull.isNull(decisionForApi)){
			decisions =  decisionForApi.getExcuteFlow();
			for (ExcuteFlowForApi api : decisions){
				Long rulesId = api.getRulesId();
				EngineRules engineRules = ruleSetService.selectById(rulesId);
				if (engineRules != null){
					String rulesName = engineRules.getName();
					Integer matchType = engineRules.getMatchType();
					rulesNamesMap.put(rulesId,rulesName);
					rulesMatchTypeMap.put(rulesId,matchType);
				}
			}
		}

		Integer decisionId = data.getGoalId();
		EngineDecision engineDecision = engineDecisionService.selectByPrimaryKey(decisionId);
		if (engineDecision == null){
			decisionName = "暂无";
		}else{
			decisionName = engineDecision.getName();
		}

		Map<String,Object> map = new HashMap<>();
		map.put("data",data);
		map.put("decisions",decisions);
		map.put("rulesNamesMap",rulesNamesMap);
		map.put("rulesMatchTypeMap",rulesMatchTypeMap);
		map.put("decisionName",decisionName);
		map.put("count",count);
		return map;
	}

	public void checkParamMap(Map<String, Object> map, Map<String, Object> parametersMap) {
		int paramCount = 0;
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (parametersMap.keySet().contains(key)) {
				paramCount += 1;
			}
		}
		if (paramCount < parametersMap.size()) {
			throw new RcsException("名单参数不齐全,请检查后重试", StatusCode.PARAMS_ERROR.getCode());
		}
	}

}
