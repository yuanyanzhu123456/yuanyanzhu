package com.geo.rcs.modules.api.modules.user.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.log.LogFactory;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.IPUtils;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.MD5Util;
import com.geo.rcs.common.util.http.HttpContextUtils;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.api.annotation.AuthIgnore;
import com.geo.rcs.modules.api.modules.user.service.ApiUserTokenService;
import com.geo.rcs.modules.source.client.Secret;
import com.geo.rcs.modules.sys.entity.LoginLog;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.entity.SysUserToken;
import com.geo.rcs.modules.sys.service.LoginLogService;
import com.geo.rcs.modules.sys.service.SysUserService;
import com.geo.rcs.modules.sys.service.SysUserTokenService;

/**
 * API登录授权
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/14 14:57
 */
@RestController
@RequestMapping("/api")
public class ApiLoginController extends BaseController{
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ApiUserTokenService apiUserTokenService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    /**
     * 登录
     */
    @AuthIgnore
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Geo login(String username, String password,String token) {

        if(this.isNullOrEmpty(token) == false){
            SysUserToken sysUserToken = sysUserTokenService.queryByToken(token);

            if(sysUserToken == null || sysUserToken.getExpireTime().getTime() < System.currentTimeMillis()){
                return Geo.error(StatusCode.TOKEN_INVALID.getCode(),"token失效，请重新登录");
            }
            else{
                Geo apiToken = apiUserTokenService.createToken(sysUserToken.getUserId());
                return apiToken;
            }
        }
        //查询用户信息
        SysUser user = sysUserService.queryByUserName(username);

        //账号不存在、密码错误
        if(user == null || !user.getPassword().equals(MD5Util.encode(password))) {
            return Geo.error("账号或密码不正确");
        }

        //账号锁定
        if(user.getStatus() == 0){
            return Geo.error("账号已被锁定,请联系管理员");
        }

        //生成token，并保存到数据库
        Geo geo = apiUserTokenService.createToken(user.getId());

        //返回用户信息
        geo.put("userId", user.getId());
        geo.put("username", user.getUsername());
        geo.put("name", user.getName());
        geo.put("mobile", user.getMobilephone());
        geo.put("email", user.getEmail());
        geo.put("status", user.getStatus());

        //记录用户的登录ip和登录日期
        sysUserService.updateLoginInfo(user.getId(), IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()), new Date());

        //记录登录日志
       /* LoginLog loginLog = LogFactory.createLoginLog(Constant.LogType.LOGIN, user);
        loginLogService.save(loginLog);*/

        return geo;
    }
    /**
	 * 登录
	 */
	@AuthIgnore
	@RequestMapping(value = "/loginClient", method = RequestMethod.POST)
	public String loginClient(String mapJson) {
		String username = null;
		String password = null;
		String result = null;
		boolean flag = false;
		if (mapJson != null) {
			if (!mapJson.startsWith("{")) {
				flag = true;
				try{
					mapJson = Secret.rcsDecrypt(mapJson);
				}catch (Exception e){
					flag = false;
				}

			}
		}
		Map<String, Object> parmMap = JSONUtil.jsonToMap(mapJson);
		username = (String) parmMap.get("userName");
		password = (String) parmMap.get("passWord");
		if (ValidateNull.isNull(username) || ValidateNull.isNull(password)) {
			result = JSONUtil.beanToJson(Geo.error("账号或密码不正确").toString());
			if (flag) {
				try{
					result = Secret.rcsDecrypt(result);
				}catch (Exception e){
					result = JSONUtil.beanToJson(Geo.error("账户登录验证失败").toString());
				}
			}
			return result;
		}

		SysUser user = sysUserService.queryByUserName(username);

		// 账号不存在、密码错误
		if (user == null || !user.getPassword().equals(MD5Util.encode(password))) {
			result = JSONUtil.beanToJson(Geo.error("账号或密码不正确").toString());
			if (flag) {
				result = Secret.rcsEncrypt(result);
				result = Secret.rcsEncrypt(result);
			}
			return result;
		}

		// 账号锁定
		if (user.getStatus() == 0) {
			result = JSONUtil.beanToJson(Geo.error("账号已被锁定,请联系管理员").toString());
			if (flag) {
				result = Secret.rcsEncrypt(result);
			}
			return result;
		}

		// 生成token，并保存到数据库
		Geo geo = apiUserTokenService.createToken(user.getId());

		// 返回用户信息
		geo.put("userId", user.getId());
		geo.put("username", user.getUsername());
		geo.put("name", user.getName());
		geo.put("mobile", user.getMobilephone());
		geo.put("email", user.getEmail());
		geo.put("status", user.getStatus());

		// 记录用户的登录ip和登录日期
		sysUserService.updateLoginInfo(user.getId(), IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()),
				new Date());

		// 记录登录日志
		LoginLog loginLog = LogFactory.createLoginLog(Constant.LogType.LOGIN, user);
		loginLogService.save(loginLog);
		result = JSONUtil.beanToJson(geo);
		if (flag) {
			result = Secret.rcsEncrypt(result);
		}
		return result;
	}


	/**
	 * 登录
	 */
	@AuthIgnore
	@RequestMapping(value = "/bankLogin", method = RequestMethod.POST)
	public Geo bankLogin(Integer ruleId, Integer msgId,Map<String,String> map) {

		List<LoginLog> loginLogs = loginLogService.getLogsByMacIddr(map.get("ip"));

		int count = 0;
		for (int i = 0;i<loginLogs.size();i=i+2){
			for (int j = 1;i<loginLogs.size();i=i+2){
				if(!loginLogs.get(i).getUserId().equals(loginLogs.get(j).getUserId())){
					count++;
					if(count>7){
						break;
					}
				}
			}
		}
		if(count > 7){
			return Geo.error("同一mac地址或同一手机 1小时内登录8个以上不同客户号，尝试登录密码。 ");
		}

		//查询用户信息
		SysUser user = sysUserService.queryByUserName(map.get("username"));

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(MD5Util.encode(map.get("password")))) {
			return Geo.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return Geo.error("账号已被锁定,请联系管理员");
		}

		Geo geo = null;
		//返回用户信息
		geo.put("userId", user.getId());
		geo.put("username", user.getUsername());
		geo.put("name", user.getName());
		geo.put("mobile", user.getMobilephone());
		geo.put("email", user.getEmail());
		geo.put("status", user.getStatus());

		//记录用户的登录ip和登录日期
		sysUserService.updateLoginInfo(user.getId(), IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()), new Date());

		//记录登录日志
        LoginLog loginLog = LogFactory.createLoginLog(Constant.LogType.LOGIN, user);
        loginLogService.save(loginLog);

		return geo;
	}

	public static void main(String[] args) {
		List<LoginLog> loginLogs = new ArrayList<>(10);
		for (int o=0;o<=10;o++) {
			LoginLog loginLog = new LoginLog();
			loginLog.setUserId(Long.valueOf(o));
			loginLogs.add(loginLog);
		}
		int count = 0;
		for (int i = 0;i<loginLogs.size();i=i+2){
			for (int j = 1;i<loginLogs.size();i=i+2){
				if(!loginLogs.get(i).getUserId().equals(loginLogs.get(j).getUserId())){
					count++;
					if(count>7){
						break;
					}
					System.out.printf(""+count);
				}
			}
		}
		if(count > 7){
			System.out.printf("同一mac地址或同一手机 1小时内登录8个以上不同客户号，尝试登录密码。 ");
		}
	}

}
