package com.geo.rcs.modules.evestat.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.evestat.entity.EveStat;
import com.geo.rcs.modules.evestat.service.EveStatService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.evestat
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月09日 上午10:34
 */
@RestController
@RequestMapping("/evestat")
public class EveStatController extends BaseController{

    @Autowired
    private EveStatService eveStatService;

    /**
     * 客户事件统计列表 （模糊，分页）
     *
     * @param request
     * @param response
     * @param eveStat
     */
    @RequestMapping("/list")
    @RequiresPermissions("api:evestat:list")//权限管理
    public void getApprovalList(HttpServletRequest request, HttpServletResponse response, EveStat eveStat){
        try {
                //添加unique_code （客户唯一标识）
                eveStat.setUniqueCode(getUserId());

                this.sendData(request , response, new PageInfo<>(eveStatService.findByPage(eveStat)));

                this.sendOK(request, response);

        } catch (ServiceException e) {
        	LogUtil.error("客户事件统计列表 （模糊，分页）", eveStat.toString(), getGeoUser().getName(), e);
            this.sendError(request, response, "获取列表失败！");
        }
    }
}
