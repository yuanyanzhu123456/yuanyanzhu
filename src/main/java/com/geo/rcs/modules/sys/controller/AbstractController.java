package com.geo.rcs.modules.sys.controller;

import com.geo.rcs.modules.sys.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller公共组件
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/19 13:28
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUser getUser() {
		SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();
		return principal;
	}

	protected Long getUserId() {
		return getUser().getId();
	}

	protected String getUserType() {
		return getUser().getUserType();
	}

}
