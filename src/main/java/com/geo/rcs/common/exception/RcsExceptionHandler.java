package com.geo.rcs.common.exception;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.Geo;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/18 10:35
 */
@RestControllerAdvice
public class RcsExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RcsException.class)
	public Geo handleRcsException(RcsException e){
		Geo geo = new Geo();
		geo.put("code", e.getCode());
		geo.put("msg", e.getMessage());

		return geo;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Geo handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return Geo.error(StatusCode.RES_ERROR.getCode(),"数据库中已存在该记录");
	}

	@ExceptionHandler(IncorrectCredentialsException.class)
	public Geo handleIncorrectCredentialsException(IncorrectCredentialsException e){
		logger.error(e.getMessage(), e);
		return Geo.error(StatusCode.TOKEN_INVALID.getCode(), e.getMessage());
	}

	@ExceptionHandler(LockedAccountException.class)
	public Geo handleLockedAccountException(LockedAccountException e){
		logger.error(e.getMessage(), e);
		return Geo.error(StatusCode.SERVER_ERROR.getCode(), e.getMessage());
	}

	@ExceptionHandler(UnknownSessionException.class)
	public Geo handleUnknownSessionException(UnknownSessionException e){
		logger.error(e.getMessage(), e);
		return Geo.error(StatusCode.EXPIRED.getCode(),"登录session失效，请重新登录");
	}

	@ExceptionHandler(UnauthenticatedException.class)
	public Geo handleUnknownAccountException(UnauthenticatedException e){
		logger.error(e.getMessage(), e);
		return Geo.error(StatusCode.DENIED.getCode(),"无访问权限或账户未登录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public Geo handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return Geo.error(StatusCode.DENIED.getCode(),"没有权限，请联系管理员授权");
	}

	@ExceptionHandler(Exception.class)
	public Geo handleException(Exception e){
		logger.error(e.getMessage(), e);
		return Geo.error();
	}
}
