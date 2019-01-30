package com.geo.rcs.modules.api.modules.user.controller;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.MD5Util;
import com.geo.rcs.common.validator.Assert;
import com.geo.rcs.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class ApiSysUserController {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 禁用/启用账户
     * @param userId
     * @param status
     * @return
     */
    @RequestMapping(value = "/disable")
    public Geo disableAccount(Long userId, Integer status){
        int count = sysUserService.disableAccount(userId, status);
        if(count == 0){
            return Geo.error("修改失败");
        }
        return Geo.ok("修改成功");
    }

    /**
     * 修改用户登录密码
     * @param password
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/password")
    public Geo password(Long userId, String password, String newPassword){
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = MD5Util.encode(password);

        //新密码
        newPassword = MD5Util.encode(newPassword);

        //更新密码
        int count = sysUserService.updatePassword(userId, password, newPassword);
        if(count == 0){
            return Geo.error("原密码不正确");
        }

        return Geo.ok("修改成功");
    }
}
