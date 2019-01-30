package com.geo.rcs.modules.sys.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysVersion;
import com.geo.rcs.modules.sys.service.CusVersionService;
import com.geo.rcs.modules.sys.service.SysVersionService;
import com.github.pagehelper.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.sys.controller
 * @Description : TODD
 * @Author wuzuqi
 * @email wuzuqi@geotmt.com
 * @Creation Date : 2018年10月31日
 */
@RestController
@RequestMapping("/geo/version")
public class SysVersionController extends BaseController{

    @Autowired
    private SysVersionService sysVersionService;
    @Autowired
    private CusVersionService cusVersionService;




    /**
     * 版本列表信息（模糊，分页）
     *
     * @param request
     * @param response
     * @param
     */
    @PostMapping("/getVersionInfoByPage")
    @RequiresPermissions("sys:version:list")//权限管理
    public void getVersionInfoByPage(HttpServletRequest request, HttpServletResponse response, SysVersion sysVersion){
        try {
            Page<SysVersion> result = sysVersionService.getVersionInfoByPage(sysVersion);
            this.sendDataWhereDateToString(request , response, new PageInfo<>(result));
            this.sendOK(request, response);


        } catch (ServiceException e) {
            LogUtil.error("版本列表信息列表 （模糊，分页）", sysVersion.toString(), getGeoUser().getName(), e);
            this.sendError(request, response, "获取列表失败！");
        }
    }


    /**
     * 增加版本
     * @param request
     * @param response
     * @param sysVersion
     */
    @PostMapping("/save")
    @RequiresPermissions("sys:version:save")
    public void save(HttpServletRequest request,HttpServletResponse response,SysVersion sysVersion) {
        ResultType resultType = ValidateNull.check(sysVersion, NotNull.RequestType.NEW);
        sysVersion.setCreateTime(new Date());
        sysVersion.setUpdateTime(new Date());
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            if (sysVersionService.queryVersionName(sysVersion.getName())!=null) {
                this.sendError(request, response, "版本名已存在！");
                return;
            }
            // 添加版本信息
            sysVersion.setCreater(getGeoUser().getName());
            sysVersion.setUpdater(getGeoUser().getName());
            sysVersionService.save(sysVersion);
            this.sendOK(request, response);// code:200表示成功
        } catch (Exception e) {
            // logger.error("新增用户失败！", e);
            this.sendError(request, response, "添加版本失败！");
            LogUtil.error("添加版本", sysVersion.toString(),getGeoUser().getName(), e);

        }
    }


    /**
     * 修改版本状态
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:version:update")
    public void updateVersion(SysVersion sysVersion, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(sysVersion, NotNull.RequestType.UPDATE);
        sysVersion.setUpdater(getGeoUser().getName());
        sysVersion.setUpdateTime(new Date());
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            if(sysVersion.getId() != null ) {
                sysVersionService.updateVersion(sysVersion);
                this.sendOK(request, response);
            }
            this.sendError(request,response,"参数不能为空！");
        } catch (Exception e) {
            this.sendError(request, response, "修改版本失败！");
            LogUtil.error("修改版本", sysVersion.toString(),getGeoUser().getName(), e);
        }
    }
    /**
     * 删除版本
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys:version:update")
    public void deleteVersion(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (getGeoUser().getId() == null){
                this.sendError(request,response, "已过期，请重新登录！");
            }
            if(id != null) {
                sysVersionService.deleteVersion(id);
                this.sendOK(request, response);
            }
            this.sendError(request,response,"参数不能为空！");
        } catch (Exception e) {
            this.sendError(request, response, "删除版本失败！");
            LogUtil.error("删除版本", id.toString(),getGeoUser().getName(), e);
        }
    }


    /**
     * 用户版本数量统计
     */
    @PostMapping("/static")
    public void verUserSum(HttpServletRequest request,HttpServletResponse response){
        try {
            GeoUser geo = getGeoUser();
            if (geo == null) {
                this.sendError(request,response,"用户信息已过期，请重新登录");
            }

            this.sendData(request,response,sysVersionService.verUserSum());

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.error("用户版本数量统计","",getGeoUser().getName(),e);
            this.sendError(request,response,"用户版本数量统计"+StatusCode.ERROR.getMessage());
        }
        }



    /**
     * 版本到期提醒
     */
    @PostMapping("/warn")
    public void warn(Long id,HttpServletRequest request,HttpServletResponse response){
        try {

            if (id == null) {
                this.sendError(request,response,"参数错误!");
            }
           Map<String,Object> result = cusVersionService.getCusVersionInfo(id);
            if (result == null){
                this.sendError(request,response,"该用户尚未绑定版本信息"+StatusCode.ERROR.getMessage());
            }else{
                this.sendData(request,response,result);
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.error("到期提醒","",getUser().getName(),e);
            this.sendError(request,response,"版本到期提醒"+StatusCode.ERROR.getMessage());
        }
    }

    /**
     * 获取指定版本下的用户列表
     * @param request
     * @param response
     * @param sysVersion
     */
    @RequestMapping("/list")
    public void getVersionList(HttpServletRequest request,HttpServletResponse response,SysVersion sysVersion){
        if (sysVersion.getId() == null){
            this.sendError(request,response,"id不能为空");
            return;
        }

        try{
            Page<SysUser> result = sysVersionService.findCustomOfVersion(sysVersion);
            this.sendDataWhereDateToString(request,response,new PageInfo<>(result));
        }catch(Exception e){
            e.printStackTrace();
            if (e instanceof ServiceException){
                LogUtil.error("获取版本下客户列表失败","",getGeoUser().getName(),e);
                this.sendError(request,response,StatusCode.SERVER_ERROR.getMessage());
            }else{
                LogUtil.error("获取版本下客户列表失败","",getGeoUser().getName(),e);
                this.sendError(request,response,"获取版本下客户列表"+StatusCode.ERROR.getMessage());
            }
        }
    }





}





