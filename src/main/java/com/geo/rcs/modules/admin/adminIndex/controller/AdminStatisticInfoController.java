package com.geo.rcs.modules.admin.adminIndex.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.admin.adminIndex.service.StatisticInfoService;
import com.geo.rcs.modules.api.modules.user.service.ApiUserTokenService;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.service.GeoUserService;
import com.geo.rcs.modules.sys.service.LoginLogService;
import com.geo.rcs.modules.sys.service.SysUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by geo on 2018/10/23.
 */
@RestController
@RequestMapping("/crm")
public class AdminStatisticInfoController extends BaseController{
    @Autowired
    private StatisticInfoService statisticInfoService;
    @Autowired
    private ApiUserTokenService apiUserTokenService;
    @Autowired
    private GeoUserService geoUserService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private SysUserService sysUserService;


    /**
     * 概览： 客户数、用户数、活跃用户数（最近一个月内登录至少1次的用户）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/overview")
    public String getOverviewInfo(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<Object,Object> result = statisticInfoService.getOverviewInfo();
            JSONObject jsonObject = JSONObject.fromObject(result);
            return jsonObject.toString();

        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof RcsException){
                this.sendError(request,response,((RcsException) e).getMsg());
            }
            return "获取概述"+StatusCode.ERROR.getMessage();
        }

    }


    /**
     * 用户活跃度分析图 参数 dayNum : 1、7、30、31代表一天、一周、一个月
     * @param request
     * @param response
     * @param dayNum
     */
    @RequestMapping("/activityGraph")
    public void getActivityGraph(HttpServletRequest request, HttpServletResponse response, Integer dayNum){

        try {
            validateDayNum(dayNum);
            Map<Object,Object> map = new HashMap<>();
            map.put("dayNum",dayNum);
            Map<String, Object> result = statisticInfoService.getActivityGraph(map);
            this.sendData(request,response,result);
        }catch(Exception e){
            e.printStackTrace();
            if (e instanceof RcsException){
                this.sendError(request,response,((RcsException) e).getMsg());
            }
            LogUtil.error("获取用户活跃度图表失败","",getGeoUser().getName(),e);
            this.sendError(request,response,"获取用户活跃度图表失败"+StatusCode.ERROR.getMessage());
        }

    }


    /**
     * 指定时间段内的新增客户趋势图
     * @param request
     * @param response
     * @param dayNum
     */
    @RequestMapping("/newCustomTrend")
    public void getNewCustomTrend(HttpServletRequest request,HttpServletResponse response,Integer dayNum){
        try {
            validateDayNum(dayNum);
            Map<Object,Object> map = new HashMap<>();
            map.put("dayNum",dayNum);
            Map<String, Object> result = statisticInfoService.getNewCustomTrend(map);
            this.sendData(request,response,result);
        }catch(Exception e){
            e.printStackTrace();
            LogUtil.error("获取新增时间趋势图失败","",getGeoUser().getName(),e);
            this.sendError(request,response,"获取新增时间趋势图失败"+StatusCode.ERROR.getMessage());
        }
    }

    /**
     * 客户活跃度排行
     * @param request
     * @param response
     * @param dayNum
     */
    @RequestMapping("/userActivityRankList")
    public void getUserActivityRankList(HttpServletRequest request,HttpServletResponse response,Integer dayNum){
        try {
            validateDayNum(dayNum);
            Map<String, Object> result = statisticInfoService.userActivityRankList(dayNum);
            this.sendData(request,response,result);
        }catch(Exception e){
            e.printStackTrace();
            if (e instanceof RcsException){
                this.sendError(request,response,((RcsException) e).getMsg());
            }
            LogUtil.error("客户活跃度排行","",getGeoUser().getName(),e);
            this.sendError(request,response,"客户活跃度排行"+StatusCode.ERROR.getMessage());
        }
    }

    /**
     * 验证参数一天、一周、一个月是否正确
     * @param dayNum
     */
    public static void validateDayNum(Integer dayNum){
        if (dayNum == null){
            throw new RcsException(StatusCode.PARAMS_ERROR.getMessage()+":参数不能为空");
        }
        if (dayNum != 1 && dayNum != 7 && dayNum != 31 && dayNum != 30){
            throw new RcsException(StatusCode.PARAMS_ERROR.getMessage()+":指定参数必须为1或者7或者30或者31代表一天、一周、一个月");
        }
    }

    /**
     * 客户员工总数和客户员工top10排行
     * @return
     */
    @PostMapping("/comEmpSum")
    public Geo comEmpSum() {
        try {
            GeoUser geo = getGeoUser();
            if (geo == null) {

                return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
            }
            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",sysUserService.comEmpCount());

        }catch (Exception e){
            e.printStackTrace();
            return Geo.error(StatusCode.ERROR.getMessage());
        }
    }


}
