package com.geo.rcs.modules.event.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.ObjectUtils;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 事件统计
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/1/23 17:03
 */
@RestController
@RequestMapping("/event/stat")
public class EventStatController extends BaseController {

    @Autowired
    private EventService eventService;

    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";

    /**
     * 今日事件统计
     * @return
     */
    @GetMapping("/todayStat")
	@RequiresPermissions("event:stat:todayStat")
    public Geo todayEventStat(){
        try {
			//获取当前登录用户
			SysUser user = getUser();
			if(user == null) {
			    return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			//获取今日时间范围
			DateUtils.DateRange dateRange = DateUtils.getTodayRange();

			//设置查询参数
			Map<String, Object> map = new HashMap<>();
			map.put("startTime", DateUtils.format(dateRange.getStart(), "yyyy-MM-dd HH:mm:ss"));
			map.put("endTime", DateUtils.format(dateRange.getEnd(), "yyyy-MM-dd HH:mm:ss"));
			map.put("userId", user.getUniqueCode());

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", eventService.todayEventStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("  今日事件统计","无参数", getUser().getName(), e);

			return Geo.error();
		}
    }

    /**
     * 当前规则集总事件统计
     * @return
     */
    @GetMapping("/thisRuleSetEventStat")
	@RequiresPermissions("event:stat:thisRuleSetEventStat")
    public Geo thisRuleSetEventStat(Long ruleSetId){
        try {
			//获取当前登录用户
			SysUser user = getUser();
			if(user == null) {
			    return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			//获取今日时间范围
			DateUtils.DateRange dateRange = DateUtils.getTodayRange();

			//设置查询参数
			Map<String, Object> map = new HashMap<>();
			map.put("ruleSetId", ruleSetId);
			map.put("userId", user.getUniqueCode());

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", eventService.thisRuleSetEventStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("当前规则集总事件统计", "ruleSetId:"+ruleSetId, getUser().getName(), e);
			return Geo.error();
		}
    }
    /**
     * 近期事件统计
     * @param map
     * @return
     */
    @PostMapping("/recentStat")
	@RequiresPermissions("event:stat:recentStat")
    public Geo riskEventStat(@RequestBody Map<String, Object> map){
        try {
			//获取当前登录用户
			SysUser user = getUser();
			if(user == null) {
			    return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			String startTime = (String)map.get(START_TIME);
			String endTime = (String)map.get(END_TIME);
			if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
			}
			if(!DateUtils.compareTo(startTime, endTime)) {
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
			}
			map.put("userId", user.getUniqueCode());

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", eventService.riskEventStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("近期事件统计", map.toString(), getUser().getName(), e);
			return Geo.error();
		}
    }
    /**
     * 单条规则集近期事件统计
     * @param map
     * @return
     */
    @PostMapping("/thisRuleSetRecentStat")
    public Geo thisRuleSetRecentStat(@RequestBody Map<String, Object> map){
        //获取当前登录用户
        try {
			SysUser user = getUser();
			if(user == null) {
			    return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			String startTime = (String)map.get(START_TIME);
			String endTime = (String)map.get(END_TIME);
			if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
			}
			if(!DateUtils.compareTo(startTime, endTime)) {
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
			}
			map.put("userId", user.getUniqueCode());

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", eventService.thisRuleSetRecentStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("单条规则集近期事件统计", map.toString(), getUser().getName(), e);
			return Geo.error();
		}
    }

    /**
     * 风险地图统计
     * @param map
     * @return
     */
    @PostMapping("/riskMapStat")
	@RequiresPermissions("event:stat:riskMapStat")
    public Geo mapEventStat(@RequestBody Map<String, Object> map){
        //获取当前登录用户
        try {
			SysUser user = getUser();
			if(user == null) {
			    return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			String startTime = (String)map.get(START_TIME);
			String endTime = (String)map.get(END_TIME);
			if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
			}
			if(!DateUtils.compareTo(startTime, endTime)) {
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
			}
			map.put("userId", user.getUniqueCode());

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", eventService.mapEventStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("风险地图统计", map.toString(), getUser().getName(), e);
			return Geo.error();
		}
    }

    /**
     * 导出风险事件统计数据
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/export")
	@RequiresPermissions("event:stat:export")
    public Geo export(HttpServletRequest request, HttpServletResponse response){
        //将请求参数转换成map
    	Map<String, Object> map = ObjectUtils.requestToMap(request);
        try {

			//获取当前登录用户
			SysUser user = getUser();
			if(user == null) {
			    return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
			}

			String startTime = (String)map.get(START_TIME);
			String endTime = (String)map.get(END_TIME);
			if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
			}
			if(!DateUtils.compareTo(startTime, endTime)) {
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
			}
			map.put("userId", user.getUniqueCode());

			EventStatEntry statEntry = eventService.riskEventStat(map);
			List<Map<String, Object>> mapList = new ArrayList<>();
			for (EventStatEntry entry : statEntry.getEventStatEntryList()) {
			    Map<String, Object> map1 = JSONUtil.beanToMap(entry);
			    map1.put("eventTime", DateUtils.format(entry.getEventTime()));
			    mapList.add(map1);
			}
			Map<String, Object> map2 = JSONUtil.beanToMap(statEntry);
			map2.put("total", "总计");
			mapList.add(map2);

			String fileName = "事件统计_" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			String[] headers = new String[]{"时间","客户名称","通过事件数","通过事件率","拒绝事件数","拒绝事件率",
			                                "人工审核数","人工审核率","失效事件数","失效事件率"};
			String[] props = new String[]{"eventTime","userName","passEventCount","passEventRatio","refuseEventCount","refuseEventRatio",
			                              "manualReviewCount","manualReviewRatio","invalidEventCount","invalidEventRatio"};

			ExportExcel excel = new ExportExcel("事件统计", headers,true).setDataList(mapList, props);

			String svg = request.getParameter("svg");
			try {
			    if (svg != null && svg != "") {
			        BASE64Decoder decoder = new BASE64Decoder();
			        byte[] bt = decoder.decodeBuffer(svg);
			        excel.setPictuce(bt, excel.getRowsIdx());
			    }
			    excel.write(response,fileName).dispose();
			    return Geo.ok(StatusCode.SUCCESS.getMessage());
			} catch (IOException e) {
			    e.printStackTrace();
			    throw e;
//			    return Geo.error(StatusCode.ERROR.getCode(),.ERROR.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("导出风险事件统计数据", map.toString(), getUser().getName(), e);
			return Geo.error();
		}
    }

    /**
     * 客户事件统计
     * @return
     */
    @PostMapping("/custStat")
    public Geo custEventStat(@RequestBody Map<String, Object> map){
        try {
			String startTime = (String)map.get(START_TIME);
			String endTime = (String)map.get(END_TIME);

			if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
			}
			if(!DateUtils.compareTo(startTime, endTime)) {
			    return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
			}

			return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", eventService.custEventStat(map));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.error("客户事件统计", map.toString(), getUser().getName(), e);
			return Geo.error();
		}
    }
}
