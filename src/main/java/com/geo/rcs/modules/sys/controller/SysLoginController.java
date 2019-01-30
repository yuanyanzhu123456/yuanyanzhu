package com.geo.rcs.modules.sys.controller;


import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.log.LogFactory;
import com.geo.rcs.common.util.*;
import com.geo.rcs.common.util.http.HttpContextUtils;
import com.geo.rcs.modules.sys.entity.LoginLog;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.oauth2.OAuth2Token;
import com.geo.rcs.modules.sys.service.LoginLogService;
import com.geo.rcs.modules.sys.service.SysUserService;
import com.geo.rcs.modules.sys.service.SysUserTokenService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 登录相关
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017-12-22
 */
@RestController
@RequestMapping("/sys")
public class SysLoginController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LoginLogService loginLogService;

    private static Logger logger = LoggerFactory.getLogger(SysLoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Geo login(String username, String password, HttpServletRequest request) {


        String usertype = "user";
        //查询用户信息
        SysUser user = sysUserService.queryByUserName(username);


        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(MD5Util.encode(password))) {
            logger.error(LogUtil.operation("登录","",username, TimeUtil.dqsj(),"失败,账号或密码不正确"));
            return Geo.error("账号或密码不正确");
        }

        logger.info(LogUtil.operation("登录","",username,TimeUtil.dqsj(),"成功"));
        //账号锁定
        if (user.getStatus() == 0) {
            logger.error(LogUtil.operation("登录","",username,TimeUtil.dqsj(),"失败,账号已被锁定"));
            return Geo.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        Geo geo = sysUserTokenService.createToken(user.getId());

        //记录用户的登录ip和登录日期
        sysUserService.updateLoginInfo(user.getId(), IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()), new Date());

        //记录登录日志
        LoginLog loginLog = LogFactory.createLoginLog(Constant.LogType.LOGIN, user);
        loginLog.setUserId(user.getUniqueCode());
        loginLogService.save(loginLog);

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(new OAuth2Token(geo.get("token").toString(), usertype, username, password));

        //设置session
        request.getSession().setAttribute("sysUser", user);
        request.getSession().setMaxInactiveInterval(60 * 60);

        return geo;
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response, Long id) {
        if (id != null) {
            sysUserTokenService.deleteTokenById(id);
        }
        this.sendOK(request, response);
        logger.info(LogUtil.operation("注销",getUser().getUsername(),getUser().getName(),TimeUtil.dqsj(),"成功"));
    }


    /**
     * 记录退出登录日志
     */
    @RequestMapping(value = "/logoutLog", method = RequestMethod.GET)
    public Geo logoutLog() {
        //记录登出日志
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        LoginLog loginLog = LogFactory.createLoginLog(Constant.LogType.EXIT, user);
        loginLogService.save(loginLog);
        return Geo.ok();
    }

    /**
     * 登录日志
     */
    @RequestMapping(value = "/loginLog", method = RequestMethod.POST)
    @RequiresPermissions("sys:login:list")
    public void loginLog(@RequestBody LoginLog loginLog, HttpServletRequest request, HttpServletResponse response) {
        try {
            loginLog.setUserId(getUser().getUniqueCode());
            this.sendData(request, response, new PageInfo<>(loginLogService.findLoginLogByPage(loginLog)));
        } catch (ServiceException e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取登录日志","",getUser().getName(),e);
        }
    }
    /**
     * 登录日志
     */
    @RequestMapping(value = "/allLoginLog", method = RequestMethod.POST)
    @RequiresPermissions("sys:login:allLoginLog")
    public void allLoginLog(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, new PageInfo<>(loginLogService.queryList(map)));
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("获取登录日志","",getUser().getName(),e);
        }
    }




}
