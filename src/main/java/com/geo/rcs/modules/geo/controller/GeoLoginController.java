package com.geo.rcs.modules.geo.controller;


import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.log.LogFactory;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.IPUtils;
import com.geo.rcs.common.util.MD5Util;
import com.geo.rcs.common.util.http.HttpContextUtils;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.geo.service.GeoUserService;
import com.geo.rcs.modules.sys.entity.LoginLog;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.oauth2.OAuth2Token;
import com.geo.rcs.modules.sys.service.LoginLogService;
import com.geo.rcs.modules.sys.service.SysUserTokenService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/geo")
public class GeoLoginController extends BaseController {
    @Autowired
    private GeoUserService geoUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private LoginLogService loginLogService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Geo login(String username, String password,HttpServletRequest request) {

        String usertype = "admin";
        //查询用户信息
        GeoUser geoUser = geoUserService.queryByName(username);

        //账号不存在、密码错误
		if(geoUser == null || !geoUser.getPassword().equals(MD5Util.encode(password))) {
            return Geo.error("账号或密码不正确");
        }

        //账号锁定
        if(geoUser.getStatus() == 0){
            return Geo.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        Geo geo = geoUserService.createToken(geoUser.getId());

        //记录用户的登录ip和登录日期
        geoUserService.updateLoginInfo(geoUser.getId(), IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()), new Date());

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(new OAuth2Token(geo.get("token").toString(),usertype,username,password));

        //设置session
        request.getSession().setAttribute("geoUser",geoUser);
        request.getSession().setMaxInactiveInterval(60*60);

        return geo;
    }
    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/geoInfo")
    public Geo geoInfo() {
        return Geo.ok().put("geoUser", getGeoUser());
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, Object> map) {
     Long id = Long.valueOf((String) map.get("geoId"));
        if(id != null) {
            geoUserService.deleteTokenById(id);
        }
        this.sendOK(request,response);
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

}
