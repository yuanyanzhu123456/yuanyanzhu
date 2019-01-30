package com.geo.rcs.modules.roster.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.roster.entity.BlackList;
import com.geo.rcs.modules.roster.service.BlackListService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.roster.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月28日 上午11:40
 */
@RestController
@RequestMapping("/ros/black")
public class BlackListController extends BaseController{

    @Autowired
    private BlackListService blackListService;

    @RequestMapping("/list")
    @RequiresPermissions("ros:bow:list")//权限管理
    public void getBlackList(BlackList blackList, HttpServletRequest request, HttpServletResponse response){
        try {
                blackList.setUniqueCode(getUser().getUniqueCode());
                this.sendData(request, response, new PageInfo<>(blackListService.findByPage(blackList)));
                this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }
    /**
     * 单条或批量删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ros:bow:delete")
    public void deleteRoster(@RequestBody Long[] ids,HttpServletRequest request, HttpServletResponse response){
        if(ids.length == 0) {
            this.sendError(request, response, "Id不能为空！");
        }
        try {
            blackListService.deleteBatch(ids);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "删除失败！");
        }

    }
}
