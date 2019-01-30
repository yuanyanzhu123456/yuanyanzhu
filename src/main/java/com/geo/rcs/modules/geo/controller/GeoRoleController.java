package com.geo.rcs.modules.geo.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.geo.entity.GeoRoleMenu;
import com.geo.rcs.modules.geo.service.GeoRoleService;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysRoleMenu;
import com.geo.rcs.modules.sys.service.SysRoleService;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午10:58
 */
@RestController
@RequestMapping("/geo/role")
public class GeoRoleController extends BaseController {

	@Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private GeoRoleService geoRoleService;

    /**
     * 角色列表
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/list")
    //@RequiresPermissions("sys:role:list")//权限管理
    public void itemsPage(SysRole sysRole, HttpServletRequest request, HttpServletResponse response) {
        try {
            sysRole.setUserId(getGeoUser().getUniqueCode());
            this.sendData(request, response, new PageInfo<>(geoRoleService.findByPage(sysRole)));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
        }
    }

    /**
     * 获取角色列表
     */
    @RequestMapping("/roleList")
    public String getRoleList() {
        List<SysRole> sysRoleList = geoRoleService.getRoleList(getGeoUser().getUniqueCode());
        return JSON.toJSON(sysRoleList).toString();
    }

    /**
     * 获取权限列表
     */
    @RequestMapping("/permissionList")
    public void getPermissionList(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, geoRoleService.getPermissionList());//code:200表示成功
        } catch (Exception e) {
            this.sendError(request, response, "获取权限列表失败！");
        }
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public void save(SysRole sysRole, HttpServletRequest request, HttpServletResponse response) {
        ResultType resultType = ValidateNull.check(sysRole, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            sysRole.setUserId(getGeoUser().getUniqueCode());
            sysRole.setCreater(getGeoUser().getName());
            geoRoleService.save(sysRole);
            this.sendOK(request, response);//code:200表示成功
        } catch (ServiceException e) {
            this.sendError(request, response, "新增角色失败！");
        }
    }

    /**
     * 获取角色以及权限信息，用于回显权限配置数据
     */
    @RequestMapping("/toUpdate")
    public void toUpdate(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        try {
            this.sendData(request, response, geoRoleService.getPermissionById(getGeoUser().getUniqueCode(),id));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取角色及权限失败！");
        }
    }

    /**
     * 获取角色信息，用于回显数据
     */
    @RequestMapping("/getRoleInfo")
    public String getRoleInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        SysRole sysRole = geoRoleService.queryRoleById(id);
        Map hashMap = new HashMap();
        hashMap.put("message", sysRole);
        JSONObject jsonObject = JSONObject.fromObject(hashMap);
        return jsonObject.toString();
    }

    /**
     * 修改角色
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public void updateUser(@Validated SysRole sysRole, HttpServletRequest request, HttpServletResponse response) {

        ResultType resultType = ValidateNull.check(sysRole, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            if(sysRole.getId() == 1 || sysRole.getId().equals(getGeoUser().getRoleId())){
                this.sendError(request, response,"不能禁用当前角色");
            }
            else{
                geoRoleService.updateRole(sysRole);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            //logger.error("更新用户失败！", e);
            this.sendError(request, response, "更新角色失败！");
        }
    }

    /**
     * 修改角色权限
     */
    @PostMapping("/updatePermission")
    @RequiresPermissions("sys:role:updatePermission")
    public void updatePermission(@RequestBody GeoRoleMenu geoRoleMenu, HttpServletRequest request, HttpServletResponse response) {
        try {
            geoRoleMenu.setGeoId(getGeoUser().getUniqueCode());
            //保存角色权限
            geoRoleService.updatePermission(geoRoleMenu);
            this.sendOK(request, response);
        } catch (ServiceException e) {
            this.sendError(request, response, "修改权限失败！");
        }
    }

    /**
     * 获取用户角色及权限：验证权限使用
     */
    @RequestMapping("/getPermissionById")
    public void getRoleAndPermission(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, geoRoleService.getPermissionById(getGeoUser().getUniqueCode(),getGeoUser().getRoleId()));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取角色及权限失败！");
        }
    }
    /**
     * 客户端默认角色列表
     */
    @RequestMapping("/roleListClient")
    public String getRoleListClient(){
        List<SysRole> sysRoleList = sysRoleService.getRoleList(0L);
        return JSON.toJSON(sysRoleList).toString();
    }
    /**
     * 获取客户端默认角色权限列表
     */
    @RequestMapping("/permissionListClient")
    public void getPermissionListClient(HttpServletRequest request,HttpServletResponse response){
        try {
            this.sendData(request, response,sysRoleService.getPermissionList());//code:200表示成功
        } catch (Exception e) {
            this.sendError(request, response, "获取角色权限失败！");
        }
    }
    /**
     * 获取客户端默认角色以及权限信息，用于回显配置权限数据
     */
    @RequestMapping("/toUpdateClient")
    public String toUpdateClient(Long id, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        List<SysMenu> permissionById = sysRoleService.getPermissionById(0L, id);
        Map hashMap = new HashMap();
        hashMap.put("message",permissionById);
        JSONObject jsonObject = JSONObject.fromObject(hashMap);
        return jsonObject.toString();
    }
    /**
     * 修改客户端默认角色权限
     */
    @PostMapping("/updatePermissionClient")
    @RequiresPermissions("sys:role:updatePermission")
    public void updatePermissionClient(@RequestBody SysRoleMenu sysRoleMenu, HttpServletRequest request, HttpServletResponse response){
        try {
            	 sysRoleMenu.setUserId(0L);
                 //保存角色权限
                 sysRoleService.updatePermission(sysRoleMenu);
                 this.sendOK(request, response);
            	
        } catch (ServiceException e) {
            this.sendError(request, response, "修改权限失败！");
        }
    }
}
