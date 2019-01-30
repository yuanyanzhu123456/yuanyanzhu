package com.geo.rcs.modules.event.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.constant.ConfigConstant;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.csv.CsvUtil;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.common.util.pdf.PdfUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.entity.EventHistoryLog;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.event.vo.EventReport;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 事件管理
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/1/22 16:43
 */
@RestController
@RequestMapping("/event/manage")
public class EventManageController extends BaseController {

	@Autowired
	private EventService eventService;
	@Autowired
	private EngineInterService engineInterService;
	@Value("${geo.event.upperDownload}")
	private Long upperDownload;
	private static final String START_TIME = "startTime";

	private static final String END_TIME = "endTime";

	/**
	 * 进件列表 （模糊，分页）
	 *
	 * @param eventEntry
	 * @return
	 */
	@PostMapping("/list")
	@RequiresPermissions("event:manage:list")//权限管理
	public Geo list(@RequestBody EventEntry eventEntry) {
		try {
			SysUser user = getUser();
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			if (eventEntry == null ) {
				return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "进件参数不能为空！");
			}
			eventEntry.setUserId(getUser().getUniqueCode());
			PageInfo<EventEntry> pageInfo = new PageInfo<>(eventService.findByPage(eventEntry));

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", pageInfo);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error(" 进件列表 （模糊，分页）", eventEntry.toString(), getUser().getName(), e);
			return Geo.error();

		}
	}

	/**
	 * 进件详情
	 *
	 * @param entryId
	 * @return
	 */
	@PostMapping("/getEntryDetail")
	@RequiresPermissions("event:manage:detail")
	public Geo getEntryDetail(Long entryId) {
		try {
			if (entryId != null) {
				EventEntry entryDetail = eventService.getEntryDetail(entryId);
				return Geo.ok().put("entryDetail", entryDetail);
			}
			return Geo.error("编号不能为空，获取数据失败！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.error("进件详情",entryId.toString(), getUser().getName(), e);
			e.printStackTrace();
			return  Geo.error();
		}

	}

	/**
	 * 查看报告
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("event:report:view")
	public Geo view(@PathVariable("id") Long id, HttpServletResponse response) {
		try {
			SysUser user = getUser();
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			if (id == null) {
				return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "参数不能为空");
			}

			// 获取数据
			EventReport report = eventService.findById(id);
			String reportName = report.getName() == null ? "" : report.getName();
			String fileName = "事件报告"+reportName+DateUtils.format(new Date(),"yyyyMMddHHmmssSSS")+".pdf";
			try {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			}catch(Exception e){
				e.printStackTrace();
			}
			response.setHeader("Content-Type","application/pdf");
			response.setHeader("Content-Disposition","inline;filename="+fileName);


			// 生成pdf并发送到浏览器
			boolean flag = PdfUtil.pdfWriter(report, response);

			if (!flag) {
				return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
			}

			return Geo.ok(StatusCode.SUCCESS.getMessage());
		} catch (RcsException e) {
			// TODO Auto-generated catch block
			LogUtil.error(" 查看报告", id.toString(), getUser().getName(), e);

			e.printStackTrace();
			return  Geo.error(e.getCode(),e.getMsg());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.error(" 查看报告", id.toString(), getUser().getName(), e);

			e.printStackTrace();
			return  Geo.error();
		}
	}

	/**
	 * 下载报告
	 *
	 * @return
	 */
	@GetMapping("/export/{id}")
	@RequiresPermissions("event:report:download")
	public Geo export(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 告诉浏览器用什么软件可以打开此文件
			response.setHeader("Content-Type", "application/pdf");

			// 获取数据
			EventReport report = eventService.findById(id);

			// 下载文件的默认名称
			String reportName = report.getName() == null ? "" : report.getName();
			String fileName = "进件报告" + reportName + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
			//=============
			filnameEncode(request, response, fileName);
			//=============
			// 生成pdf并发送到浏览器
			boolean flag = PdfUtil.pdfWriter(report, response);

			if (!flag) {
				return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
			}

			return Geo.ok(StatusCode.SUCCESS.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.error(" 下载报告", id.toString(), getUser().getName(), e);

			e.printStackTrace();
			return  Geo.error("下载报告失败");
		}
	}

	private void filnameEncode(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
		String userAgent = request.getHeader("User-Agent");
		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
		response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName));
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("UTF-8");
	}

	/**
	 * 规则引擎批量下载报告
	 *
	 * @param ids
	 * @return
	 */
	@PostMapping("/batchExport")
	@RequiresPermissions("event:batch:export")
	public Geo batchExport(String exportType, Long[] ids, EventEntry eventEntry,HttpServletRequest request,HttpServletResponse response) {

		try {
			List<EventReport> reportList = new ArrayList<>();
			if(exportType != null && "all".equals(exportType)){
				eventEntry.setUserId(getUser().getUniqueCode());
				eventEntry.setPageNo(0);
				eventEntry.setPageSize(Integer.parseInt(upperDownload.toString()));
				Page<EventEntry> byPage = eventService.findByPage(eventEntry);
				if(byPage.size() >= upperDownload){
					return  Geo.error("单次批量上限不能超过"+upperDownload+"条");
				}else{
					for (EventEntry eventEntry1 : byPage) {
						reportList.add(eventService.findById(eventEntry1.getId()));
					}
				}
				// 生成zip并发送到浏览器
				boolean flag = PdfUtil.zipWriter(reportList,request,response);

				if (!flag) {
					return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
				}

			}else{
				Geo x = checkParm(ids);
				if (x != null) return x;
				// 获取数据
				if (ids != null && ids.length != 0) {
					for (Long id : ids) {
						reportList.add(eventService.findById(id));
					}
				}

				// 生成zip并发送到浏览器
//				boolean flag = PdfUtil.zipWriter(reportList, response);
				boolean flag = PdfUtil.zipWriter(reportList,request,response);

				if (!flag) {
					return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
				}
			}
			return Geo.ok(StatusCode.SUCCESS.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ids == null){
				LogUtil.error(" 批量下载", "all", getUser().getName(), e);
			}
			else{
				LogUtil.error(" 批量下载", Arrays.asList(ids).toString(), getUser().getName(), e);
			}

			if(e instanceof RcsException){
				return  Geo.error(((RcsException) e).getCode(),((RcsException) e).getMsg());
			}
			return  Geo.error("批量下载失败");
		}
	}

	/**
	 * 进件管理下载Excel报告
	 *
	 * @param ids
	 * @return
	 */
	@PostMapping("/exportExcel")
	@RequiresPermissions("event:excel:batch")
	public Geo exportExcel(Long[] ids, HttpServletResponse response,HttpServletRequest request) {

		try {
			Geo x = checkParm(ids);
			if (x != null)
				return x;
			// 获取数据
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

			// 获取数据
			List<EventEntry> reportList = new ArrayList<>();
			EventEntry entryDetail = null;
			Map<String, Object> entryMap = null;
			int itemId = 0;
			for (Long id : ids) {
				entryDetail = eventService.getEntryDetail(id);
				entryMap = JSONUtil.jsonToMap(JSONUtil.beanToJson(entryDetail));
				entryMap.put("createTime", DateUtils.format(entryDetail.getCreateTime(), "yyyy-MM-dd HH:mm"));
				entryMap.put("id", ++itemId);
				entryMap.put("sysStatus",
						entryDetail.getSysStatus() == 0 ? "-"
								: entryDetail.getSysStatus() == 1 ? "通过"
										: entryDetail.getSysStatus() == 2 ? "人工审核"
												: entryDetail.getSysStatus() == 3 ? "拒绝" : "失效");
				entryMap.put("score", JSONUtil.jsonToMap(entryDetail.getResultMap()).get("score"));
				mapList.add(entryMap);
			}

			// 状态码,规则结果,规则评分,返回结果
			String fileName = "进件结果统计_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			String[] headers = new String[] { "序号", "手机号", "身份证", "姓名", "规则结果", "规则评分", "进件时间" };
			String[] props = new String[] { "id", "mobile", "idCard", "userName", "sysStatus", "score", "createTime" };
			ExportExcel excel = new ExportExcel("进件统计", headers, 1).setDataList2(mapList, props);

			try {

				excel.write(request,response, fileName).dispose();
				return Geo.ok(StatusCode.SUCCESS.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error(" 下载Excel报告", Arrays.asList(ids).toString(), getUser().getName(), e);

			return Geo.error();
		}
	}

	private Geo checkParm(Long[] ids) {
		if (ids.length==0) {
            return Geo.error("请先选择需要下载的报告!");
        }
		if (ids.length>1000) {
            return Geo.error("最多只能下载1000条报告!");
        }
		return null;
	}

	/**
	 * 审批
	 *
	 * @param event
	 * @return
	 */
	@PostMapping("/approval")
	public Geo approval(@RequestBody EventEntry event) {
		try {
			if (event == null || event.getId() == null) {
				return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "参数不能为空");
			}

			event.setApproverId(getUserId());
			event.setApproverName(getUser().getName());
			event.setStatus(1);
			event.setManApprovalTime(new Date());
			eventService.update(event);

			return Geo.ok(StatusCode.SUCCESS.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("审批", event.toString(), getUser().getName(), e);
			return  Geo.error();
		}
	}

	/**
	 * 下载模版
	 *
	 * @param request
	 * @param response
	 */
	@SysLog("下载模版")
	@PostMapping("/downloadCsv")
	public void downloadCsv(HttpServletRequest request, HttpServletResponse response,Long[] rulesIds) {

		if (rulesIds == null) {
			this.sendError(request, response, "未选规则集");
			return;
		}
		try {
			//定义头部
			StringBuilder headers = new StringBuilder();
			//获取规则集参数，文件列名
			Map<String, Object> map = engineInterService.getInterParamsByArray(rulesIds);
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
			LogUtil.error("下载模版", Arrays.asList(rulesIds).toString(), getUser().getName(), e);
			this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
		}
	}

	/**
	 * 决策集批量下载报告
	 *
	 * @param ids
	 * @return
	 */
	@PostMapping("/batchExportDecision")
	public Geo batchExportDecision(Long[] ids,String type,EventEntry eventEntry,HttpServletRequest request, HttpServletResponse response) {

		try {
			List<EventReport> reportList = new ArrayList<>();
			if(type != null && "all".equals(type)){
				eventEntry.setUserId(getUser().getUniqueCode());
				eventEntry.setPageNo(0);
				eventEntry.setPageSize(Integer.parseInt(upperDownload.toString()));
				Page<EventEntry> byPage = eventService.findByPage(eventEntry);
				if(byPage.size() >= upperDownload){
					return  Geo.error("单次批量上限不能超过"+upperDownload+"条");
				}else{
					for (EventEntry eventEntry1 : byPage) {
						reportList.add(eventService.findById(eventEntry1.getId()));
					}
				}
				// 生成zip并发送到浏览器
//				boolean flag = PdfUtil.zipWriter(reportList, response);
				boolean flag = PdfUtil.zipWriter(reportList,request,response);


				if (!flag) {
					return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
				}

			}else{
				Geo x = checkParm(ids);
				if (x != null) return x;
				// 获取数据
				if (ids != null && ids.length != 0) {
					for (Long id : ids) {
						reportList.add(eventService.findById(id));
					}
				}

				// 生成zip并发送到浏览器
//				boolean flag = PdfUtil.zipWriter(reportList, response);
				boolean flag = PdfUtil.zipWriter(reportList,request,response);

				if (!flag) {
					return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());
				}
			}
			return Geo.ok(StatusCode.SUCCESS.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(ids == null){
				LogUtil.error(" 批量下载", "all", getUser().getName(), e);
			}
			else{
				LogUtil.error(" 批量下载", Arrays.asList(ids).toString(), getUser().getName(), e);
			}


			return  Geo.error();
		}
	}


	/**
	 * 规则集进件概览
	 */
	@RequestMapping("/eventSurvey")
	public void eventSurvey(HttpServletRequest request,HttpServletResponse response){

		try{
			Map<String, Object> map = new HashMap<>();
			//Todo redis拓展，非实时更新 key-eventSurvey value-hashMap1
			map.put("userId",getUser().getUniqueCode());
			Map<String, Object> stringObjectMap  = eventService.selectBySelective(map);
			this.sendData(request,response,stringObjectMap);
		}catch (Exception e){
			LogUtil.error("进件概览","", getUser().getName(), e);
			e.printStackTrace();
			this.sendError(request,response,"进件分析失败");
		}

	}

	/**
	 * 规则集进件趋势分析
	 */
	@RequestMapping("/eventTrend")
	public void eventTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

		try{
			map.put("userId",getUser().getUniqueCode());
			Map<String, Object> stringObjectMap = eventService.selectBySelective(map);
			this.sendData(request,response,stringObjectMap);
		}catch (Exception e){
			LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
			e.printStackTrace();
			this.sendError(request,response,"进件分析失败");
		}

	}

	/**
	 * 规则集进件总量趋势分析
	 */
	@RequestMapping("/eventCountTrend")
	@RequiresPermissions("event:manage:eventCountTrend")
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
		Map<String,Object> map1 = eventService.getEventCountTrend(map);

		return Geo.ok().put("data", map1);

	}
	/**
	 * 规则集进件结果趋势分析
	 */
	@RequestMapping("/eventStatusTrend")
	@RequiresPermissions("event:manage:eventStatusTrend")
	public void eventStatusTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

		try{
			map.put("userId",getUser().getUniqueCode());
			Map<String, Object> stringObjectMap = eventService.getEventStatusTrend(map);
			this.sendData(request,response,stringObjectMap);
		}catch (Exception e){
			LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
			e.printStackTrace();
			this.sendError(request,response,"进件分析失败");
		}

	}
	/**
	 * 规则集进件分数趋势分析
	 */
	@RequestMapping("/eventScoreTrend")
	@RequiresPermissions("event:manage:eventScoreTrend")
	public void eventScoreTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

		try{
			map.put("userId",getUser().getUniqueCode());
			List<Map<String, Object>> eventScoreTrend = eventService.getEventScoreTrend(map);
			this.sendData(request,response,eventScoreTrend);
		}catch (Exception e){
			LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
			e.printStackTrace();
			this.sendError(request,response,"进件分析失败");
		}

	}
	/**
	 * 规则集进件耗时趋势分析
	 */
	@RequestMapping("/eventCostTrend")
	@RequiresPermissions("event:manage:eventCostTrend")
	public void eventCostTrend(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map){

		try{
			map.put("userId",getUser().getUniqueCode());
			List<Map<String, Object>> eventCostTrend = eventService.getEventCostTrend(map);
			this.sendData(request,response,eventCostTrend);
		}catch (Exception e){
			LogUtil.error("进件分析",map.toString(), getUser().getName(), e);
			e.printStackTrace();
			this.sendError(request,response,"进件分析失败");
		}

	}


	/**
	 * 进件管理 进件列表导出 excel
	 *
	 * @param request
	 * @param response
	 * @param eventEntry
	 */
	@RequestMapping(value = "/exportExcelNew", method = RequestMethod.POST)
	public Geo exportExcel(HttpServletRequest request, HttpServletResponse response, String exportType, Long[] ids, EventEntry eventEntry) {

		try {
			SysUser user = getUser();
			eventEntry.setUserId(user.getUniqueCode());
			if (user == null) {
				return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}
			Page<Map<String, Object>> byPageAll = null;
			if ("array".equalsIgnoreCase(exportType)) {
				byPageAll = eventService.findByPageIds(ids);
			} else {
				if ("all".equalsIgnoreCase(exportType)) {
					eventEntry = new EventEntry();
					eventEntry.setUserId(user.getUniqueCode());
					byPageAll = eventService.findByPageAll(eventEntry);
				} else if ("condition".equalsIgnoreCase(exportType)) {
					byPageAll = eventService.findByPageAll(eventEntry);
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
				// 状态码,规则结果,规则评分,返回结果
				String fileName = "进件结果统计_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
				String[] headers = new String[]{"序号", "渠道", "业务类型", "场景", "规则集", "手机号", "身份证", "姓名", "审核人", "规则结果", "进件时间", "进件类型"};
				String[] props = new String[]{"id", "channel", "businessId", "senceId", "rulesName", "mobile", "idCard", "userName", "approverName", "sysStatus", "createTime", "type"};
				ExportExcel excel = new ExportExcel("进件统计", headers, 1).setDataList2(mapList, props);
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
			LogUtil.error("决策管理进件列表,导出Exceol失败!", eventEntry.toString(), getUser().getName(), e);
			return Geo.error(StatusCode.ERROR.getCode(), StatusCode.ERROR.getMessage());

		}
	}

	/**
	 * 批量进件日志记录
	 *
	 * @param request
	 * @param response
	 * @param map
	 */
	@RequestMapping(value = "/batchEventLog", method = RequestMethod.POST)
	public void batchEventLog (HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,Object> map) {
		try {

			map.put("uniqueCode", getUser().getUniqueCode());
			EventHistoryLog historyLog = eventService.getHistoryLog(map);
			if(ValidateNull.isNull(historyLog)){
				eventService.saveLog(map);
			}else{
				eventService.updateLog(map);
			}
			this.sendOK(request,response);
		} catch (RcsException e) {
			LogUtil.error("批量进件日志记录失败", map.toString(), getUser().getName(), e);
			this.sendError(request,response,e.getMessage());
		}
	}

	/**
	 * 批量进件日志记录列表 （模糊，分页）
	 *
	 * @param eventHistoryLog
	 * @return
	 */
	@PostMapping("/eventLogList")
	@RequiresPermissions("event:manage:history")
	public Geo eventLogList(@RequestBody EventHistoryLog eventHistoryLog) {
		try {
			eventHistoryLog.setUniqueCode(getUser().getUniqueCode());
			PageInfo<EventHistoryLog> pageInfo = new PageInfo<>(eventService.findEventLogByPage(eventHistoryLog));
			EventHistoryLog jobInfo = eventService.getJobInfo(eventHistoryLog);
			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", pageInfo).put("jobInfo",jobInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error(" 批量进件日志记录列表 （模糊，分页）", eventHistoryLog.toString(), getUser().getName(), e);
			return Geo.error();

		}
	}
}
