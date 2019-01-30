package com.geo.rcs.modules.admin.customerstatistics.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.event.entity.EventEntry;

import ch.qos.logback.core.status.Status;

import com.geo.rcs.modules.admin.customerstatistics.service.CustomerStatisticsService;


/**
 * 客户统计
 * 
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @date 2018/5/7 16:43
 */
@RestController
@RequestMapping("/operate/CustomerStatistics")
public class CustomerStatistics extends BaseController{
	@Autowired
	private CustomerStatisticsService customerstatistics;
    /**
     * 后台查询客户管理列表（模糊，分页）
     *
     * @param request
     * @param response
     * @param entry
     */
    @RequestMapping("/allList")
    @RequiresPermissions("rule:list")//权限管理
    public void allList(HttpServletRequest request, HttpServletResponse response, EventEntry entry) throws ServiceException {
        try {
			this.sendData(request, response, new com.geo.rcs.modules.sys.entity.PageInfo<>(customerstatistics.findAllByPage(entry)));
			this.sendOK(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
			LogUtil.error("后台查询客户管理列表（模糊，分页）", entry.toString(),getGeoUser().getName(), e);
		}

    }
  
}
