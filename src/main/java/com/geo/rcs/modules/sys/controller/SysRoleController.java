package com.geo.rcs.modules.sys.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.sys.entity.SysMenu;
import com.geo.rcs.modules.sys.entity.SysRole;
import com.geo.rcs.modules.sys.entity.SysRoleMenu;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.ShiroService;
import com.geo.rcs.modules.sys.service.SysRoleService;
import com.geo.rcs.modules.sys.service.SysUserService;
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
 * @Package Name : com.geo.rcs.modules.sys.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月15日 下午6:59
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ShiroService shiroService;

    /**
     * 角色列表
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public void itemsPage(SysRole sysRole, HttpServletRequest request, HttpServletResponse response) {
        try {
            SysUser sysUser = sysUserService.selectByPrimaryKey(getUserId());
            sysRole.setUserId(sysUser.getUniqueCode());
            this.sendData(request, response, new PageInfo<>(sysRoleService.findByPage(sysRole)));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取角色列表",sysRole.toString(),getUser().getName(),e);

        }
    }

    /**
     * 获取角色列表
     */
    @RequestMapping("/roleList")
    public void getRoleList(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<SysRole> sysRoleList = sysRoleService.getRoleList(getUser().getUniqueCode());
            this.sendData(request, response, sysRoleList);
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");

        }
    }

    /**
     * 获取权限列表
     */
    @RequestMapping("/permissionList")
    @RequiresPermissions("sys:role:permission")
    public void getPermissionList(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, sysRoleService.getPermissionList());
        } catch (Exception e) {
            this.sendError(request, response, "新增角色失败！");
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
            sysRole.setCreater(getUser().getName());
            sysRole.setUserId(getUser().getUniqueCode());
            List<SysRole> lists = sysRoleService.queryRoleName(sysRole.getRoleName());
            System.out.print(lists.size());
            if (lists.size()>0){
                this.sendError(request,response,"角色名已存在！");
                return;
            }
            sysRoleService.save(sysRole);
            this.sendOK(request, response);//code:200表示成功
        } catch (ServiceException e) {
            this.sendError(request, response, "新增角色失败！");
            LogUtil.error("新增角色",sysRole.toString(),getUser().getName(),e);
        }
    }

    /**
     * 获取角色以及权限信息，用于回显配置权限数据
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(Long id, HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        List<SysMenu> permissionById = sysRoleService.getPermissionById(getUser().getUniqueCode(), id);
        Map hashMap = new HashMap();
        hashMap.put("message", permissionById);
        JSONObject jsonObject = JSONObject.fromObject(hashMap);
        return jsonObject.toString();
    }

    /**
     * 获取角色信息，用于回显数据
     */
    @RequestMapping("/getRoleInfo")
    public String getRoleInfo(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            this.sendError(request, response, "id不能为空！");
        }
        SysRole sysRole = sysRoleService.queryRoleById(id);
        Map hashMap = new HashMap();
        hashMap.put("message", sysRole);
        JSONObject jsonObject = JSONObject.fromObject(hashMap);
        return jsonObject.toString();
    }
    /**
     * 修改角色 启用禁用
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions("sys:role:updateStatus")
    public void updateStatus(@Validated SysRole sysRole, HttpServletRequest request, HttpServletResponse response) {

        ResultType resultType = ValidateNull.check(sysRole, NotNull.RequestType.UPDATE);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
            return;
        }
        try {
            if (sysRole.getId() == 2 || sysRole.getId() == 3 || sysRole.getId() == 4) {
                this.sendErrNp(request, response, "权限不足！");
            } else {
                sysRoleService.updateRole(sysRole);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "更新角色失败！");
            LogUtil.error("更新角色",sysRole.toString(),getUser().getName(),e);
        }
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
            return;
        }
        try {
            if (sysRole.getId() == 2 || sysRole.getId() == 3 || sysRole.getId() == 4) {
                this.sendErrNp(request, response, "权限不足！");
            } else {
                List<SysRole> lists = sysRoleService.queryRoleName(sysRole.getRoleName());
                if (lists != null){
                    for (int i = 0; i<lists.size(); i++){
                        if (lists.get(i).getId()!=(long)sysRole.getId()){
                        this.sendError(request,response,"角色名已存在！");
                        return;
                        }
                    }
                }
                sysRoleService.updateRole(sysRole);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "更新角色失败！");
            LogUtil.error("更新角色",sysRole.toString(),getUser().getName(),e);
        }
    }

    /**
     * 修改角色权限
     */
    @PostMapping("/updatePermission")
    @RequiresPermissions("sys:role:updatePermission")
    public void updatePermission(@RequestBody SysRoleMenu sysRoleMenu, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (sysRoleMenu.getRoleId() == 2 || sysRoleMenu.getRoleId() == 3 || sysRoleMenu.getRoleId() == 4) {
                this.sendErrNp(request, response, "权限不足！");
            } else {
                sysRoleMenu.setUserId(getUser().getUniqueCode());
                //保存角色权限
                sysRoleService.updatePermission(sysRoleMenu);
                this.sendOK(request, response);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "修改权限失败！");
            LogUtil.error("修改角色权限",sysRoleMenu.toString(),getUser().getName(),e);
        }
    }

    /**
     * 获取用户角色及权限
     */
    @RequestMapping("/getPermissionById")
    public void getRoleAndPermission(HttpServletRequest request, HttpServletResponse response) {
        try {
            SysRole sysRole = sysRoleService.queryRoleById(getUser().getRoleId());
            if(sysRole.getStatus() == 0){
                this.sendError(request, response, "该角色已被禁用,请联系管理员！");
            }
            else{
                List<SysMenu> permissions = sysRoleService.getPermissionById(getUser().getUniqueCode(), getUser().getRoleId());
                this.sendData(request, response, permissions);
            }
        } catch (ServiceException e) {
            this.sendError(request, response, "获取角色及权限失败！");
            LogUtil.error("获取角色及权限",getUser().getId().toString(),getUser().getName(),e);
        }
    }
}
