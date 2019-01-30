package com.geo.rcs.modules.admin.adminIndex.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.admin.adminIndex.service.AdminViewService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author wuzuqi
 * @Email wuzuqi@geotmt.com
 * @Date 2018/11/12  16:00
 **/
@RestController
@RequestMapping("/index/admin")
public class AdminViewController  extends BaseController {
   @Autowired
   AdminViewService adminViewService;

    /**
     * 首页概览
     */
    @PostMapping("/statistic")
    @RequiresPermissions("index:list")
    public void statistic(Long id, HttpServletRequest request, HttpServletResponse response) {
        //参数传入
        if (id == null) {
            this.sendError(request,response,"参数为空！");
        }
        try {
            Map<String,Object> m = adminViewService.statistic(id);
            this.sendData(request,response,m);
        }catch (ServiceException e) {
            this.sendError(request, response, "获取首页概览信息失败！");
            LogUtil.error("获取首页概览信息", "", getUser().getName(), e);
        }

    }
}