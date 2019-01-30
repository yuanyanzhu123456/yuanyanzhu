package com.geo.rcs.modules.admin.adminIndex.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.admin.adminIndex.service.AdminIndexService;
import com.geo.rcs.modules.geo.entity.GeoUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 18:55 2018/8/27
 */
@RestController
@RequestMapping("/operate/index")
public class AdminIndexController extends BaseController {

    @Autowired
    private AdminIndexService service;

    /**
     * 首页数据概览接口
     *
     * @param request
     * @param response
     */
    @PostMapping("/dataOverview")
//	@RequiresPermissions("operate:index:dataOverview") // 权限管理
    public void dataOverview(HttpServletRequest request, HttpServletResponse response) {

        try {
            // 获取当前登录用户
            GeoUser user = getGeoUser();
            if (user == null) {
                this.sendError(request, response, "用户信息已过期，请重新登录！");
            }
            this.sendData(request, response, service.getDateOvew());
            this.sendOK(request, response);
        } catch (Exception e) {
            LogUtil.error("首页数据概览 ", "", getGeoUser().getName(), e);
            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 首页数据概览总数
     *
     * @param request
     * @param response
     */
    @PostMapping("/dataOverviewTotal")
//	@RequiresPermissions("operate:index:dataOverview") // 权限管理
    public void dataOverviewTotal(HttpServletRequest request, HttpServletResponse response) {

        try {
            // 获取当前登录用户
            GeoUser user = getGeoUser();
            if (user == null) {
                this.sendError(request, response, "用户信息已过期，请重新登录！");
            }

            this.sendData(request, response, service.dataOverviewTotal());
            this.sendOK(request, response);
        } catch (Exception e) {
            LogUtil.error("首页数据概览 ", "", getGeoUser().getName(), e);
            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 首页图表分析接口
     *
     * @param request
     * @param response
     */
    @PostMapping("/detailStatistics")
//	@RequiresPermissions("operate:index:detailStatistics") // 权限管理
    public void detailStatistics(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> parmMap) {

        try {
            // 获取当前登录用户
            GeoUser user = getGeoUser();
            if (user == null) {
                this.sendError(request, response, "用户信息已过期，请重新登录！");
            }
            String type = parmMap.get("type");
            String date = parmMap.get("date");

            if (StringUtils.isBlank(type) || StringUtils.isBlank(date) || (!"week".equalsIgnoreCase(date) && !"month".equalsIgnoreCase(date) && !"year".equalsIgnoreCase(date) && !"day".equalsIgnoreCase(date)) || (!"customer".equalsIgnoreCase(type) && !"model".equalsIgnoreCase(type) && !"event".equalsIgnoreCase(type))) {
                this.sendError(request, response, StatusCode.PARAMS_ERROR.getMessage());
            }
            this.sendData(request, response, service.detailStatistics(parmMap));
            this.sendOK(request, response);
        } catch (Exception e) {
            LogUtil.error("首页分析）", "", getGeoUser().getName(), e);
            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }

}
