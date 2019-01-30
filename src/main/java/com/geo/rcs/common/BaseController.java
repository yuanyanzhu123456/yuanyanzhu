package com.geo.rcs.common;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.geo.entity.GeoUser;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author guoyujie
 * @date 2017-12-15
 * @version 1.0
 */
@Controller
public class BaseController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String CHAR_ENCODING = "UTF-8";
	private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	private static final String CONTENT_TYPE_TEXT = "text/html;charset=UTF-8";
	private static final String KEY_CODE = "code";
	private static final String KEY_MSG = "msg";
	private static final String KEY_ERRORMSG = "msg";
	private static final String KEY_DATAS = "datas";
	private static final String CONTEN_TTYPE_EXCEL = "application/x-execl";
	private static final String CONTENT_DISPOSITION_EXCEL = "Content-Disposition";
	private static final String ATTACHMENT_EXCEL = "attachment;filename=";

	//获取用户
	protected SysUser getUser() {
		return (SysUser) SecurityUtils.getSubject().getPrincipal();
	}

	protected GeoUser getGeoUser() {
		return (GeoUser) SecurityUtils.getSubject().getPrincipal();
	}
	//获取用户ID
	protected Long getUserId() {
		return getUser().getId();
	}

	protected Long getGeoId() {
		return getGeoUser().getId();
	}
	/**
	 * 以JSON方式响应请求
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 * @param json
	 */
	public void sendJson(HttpServletRequest request, HttpServletResponse response, String json) {

		try {
			request.setCharacterEncoding(CHAR_ENCODING);
			response.setContentType(CONTENT_TYPE_JSON);
			PrintWriter out = response.getWriter();
			out.println(json.trim());
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("sendJson error.", e);
		}
	}

	/**
	 * 以文本形式响应异步请求
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 * @param ajax
	 */
	public void sendAjax(HttpServletRequest request, HttpServletResponse response, String ajax) {

		try {
			request.setCharacterEncoding(CHAR_ENCODING);
			response.setContentType(CONTENT_TYPE_TEXT);
			PrintWriter out = response.getWriter();
			out.println(ajax);
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("sendAjax error.", e);
		}
	}

	/**
	 * 以JSON形式响应请求，返回异常状态
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 * @param msg
	 */
	public void sendError(HttpServletRequest request, HttpServletResponse response, String msg) {

		sendJson(
				request,
				response,
				JSONObject.fromObject(
						new JsonWrapper(4).put(KEY_CODE, ResponseStatus.ERROR.getCode())
								.put(KEY_ERRORMSG, msg).getWrap()).toString());
	}
	/**
	 * 以JSON形式响应请求，权限不足
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 * @param msg
	 */
	public void sendErrNp(HttpServletRequest request, HttpServletResponse response, String msg) {

		sendJson(
				request,
				response,
				JSONObject.fromObject(
						new JsonWrapper(4).put(KEY_CODE, ResponseStatus.ERRNp.getCode())
								.put(KEY_ERRORMSG, msg).getWrap()).toString());
	}
	public void sendErrAc(HttpServletRequest request, HttpServletResponse response, String msg) {

		sendJson(
				request,
				response,
				JSONObject.fromObject(
						new JsonWrapper(4).put(KEY_CODE, ResponseStatus.ERRAC.getCode())
								.put(KEY_ERRORMSG, msg).getWrap()).toString());
	}

	/**
	 * 以JSON形式响应请求，返回无权限状态
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @time 上午12:49:06
	 * @param request
	 * @param response
	 */
	public void sendDenied(HttpServletRequest request, HttpServletResponse response) {

		sendJson(
				request,
				response,
				JSONObject.fromObject(
						new JsonWrapper(4).put(KEY_CODE, ResponseStatus.DENIED.getCode())
								.put(KEY_ERRORMSG, ResponseStatus.DENIED.getDesc()).getWrap()).toString());
	}

	/**
	 * 以JSON形式响应请求，返回session过期信息
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 */
	public void sendExpired(HttpServletRequest request, HttpServletResponse response) {

		sendJson(
				request,
				response,
				JSONObject.fromObject(
						new JsonWrapper(4).put(KEY_CODE, ResponseStatus.EXPIRED.getCode())
								.put(KEY_ERRORMSG, ResponseStatus.EXPIRED.getDesc()).getWrap()).toString());
	}

	/**
	 * 退出系统
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @time 下午4:43:01
	 * @param request
	 * @param response
	 */
	public void sendLogoutSucess(HttpServletRequest request, HttpServletResponse response) {

		sendJson(
				request,
				response,
				JSONObject.fromObject(
						new JsonWrapper(4).put(KEY_CODE, ResponseStatus.SUCCESS.getCode()).put(KEY_MSG, "退出成功")
								.getWrap()).toString());
	}

	/**
	 * 以JSON形式响应请求，返回正常状态
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 */
	public void sendOK(HttpServletRequest request, HttpServletResponse response) {

		sendJson(request, response,
				JSONObject.fromObject(new JsonWrapper(2).put(KEY_CODE, ResponseStatus.SUCCESS.getCode()).getWrap())
						.toString());
	}

	/**
	 * 以JSON形式响应请求
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 * @param data
	 */
	public void sendData(HttpServletRequest request, HttpServletResponse response, Object data) {

		JsonWrapper jsonw = new JsonWrapper(8).put(KEY_CODE, ResponseStatus.SUCCESS.getCode()).put(KEY_DATAS, data);
		Page page = PageSupport.getPage();
		if (page != null) {
			jsonw = jsonw.put("pageNum", page.getPageNum()).put("pageSize", page.getPageSize());
			jsonw = page.getPages() == 0 ? jsonw.put("pages", 1) : jsonw.put("pages", page.getPages());
		}

		try {
			sendJson(request, response, JSONObject.fromObject(jsonw.getWrap()).toString());
		} catch (Exception e) {
			logger.error("sendData error.", e);
			this.sendError(request, response, "系统错误！");
		}
	}

	/**
	 * 以JSON形式响应请求(将其中时间戳字段改为字符串形式)
	 *
	 * @author 郑兴旺
	 * @date 2018-10-27
	 * @param request
	 * @param response
	 * @param data
	 */

	public void sendDataWhereDateToString(HttpServletRequest request, HttpServletResponse response, Object data) {

		JsonWrapper jsonw = new JsonWrapper(8).put(KEY_CODE, ResponseStatus.SUCCESS.getCode()).put(KEY_DATAS, data);
		Page page = PageSupport.getPage();
		if (page != null) {
			jsonw = jsonw.put("pageNum", page.getPageNum()).put("pageSize", page.getPageSize());
			jsonw = page.getPages() == 0 ? jsonw.put("pages", 1) : jsonw.put("pages", page.getPages());
		}

		try {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
			sendJson(request,response,JSONObject.fromObject(jsonw.getWrap(),jsonConfig).toString());
		} catch (Exception e) {
			logger.error("sendData error.", e);
			this.sendError(request, response, "系统错误！");
		}
	}

	/**
	 * 重定向
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param response
	 * @param path
	 */
	public void redirect(HttpServletResponse response, String path) {

		try {
			response.sendRedirect(path);
		} catch (IOException e) {
			logger.error("redirect error.", e);
		}
	}

	/**
	 * 转发
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param request
	 * @param response
	 * @param path
	 */
	public void forWard(HttpServletRequest request, HttpServletResponse response, String path) {

		try {
			request.getRequestDispatcher(path).forward(request, response);
		} catch (Exception e) {
			logger.error("forWard error.", e);
		}
	}

	/**
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param response
	 * @param request
	 * @param fileName
	 */
	public static void sendExcel(HttpServletResponse response, HttpServletRequest request, String fileName) {

		try {
			response.setContentType(CONTEN_TTYPE_EXCEL);
			response.setHeader(CONTENT_DISPOSITION_EXCEL, ATTACHMENT_EXCEL
					+ new String((fileName + ".xls").getBytes(), CHAR_ENCODING));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求参数为空判断
	 *
	 * @author guoyujie
	 * @date 2017-12-15
	 * @param parm
	 * @return
	 */
	public boolean isNullOrEmpty(String parm) {

		return parm == null || "".equals(parm.trim()) || "()".equals(parm) || "null".equalsIgnoreCase(parm)
				|| "(null)".equals(parm) || "undefined".equals(parm);
	}

	/**
	 * @author guoyujie
	 * @date 2017-12-15
	 */
	private class JsonWrapper {

		private final JsonWrapper jsonw = this;
		private final Map<String, Object> map;

		private JsonWrapper() {
			map = new HashMap<String, Object>();
		}

		/**
		 * the <code>JsonWrapper</code> constructor parameter should be a power
		 * of 2
		 * 
		 * @param capacity
		 */
		private JsonWrapper(int capacity) {
			map = new HashMap<String, Object>(capacity);
		}

		private JsonWrapper put(final String key, final Object value) {
			map.put(key, value);
			return jsonw;
		}

		private Map<String, Object> getWrap() {
			return map;
		}
	}

	/**
	 * @author: ZhengXingWang
	 * @Date:   2018-10-23
	 */

	public class JsonDateValueProcessor implements JsonValueProcessor {
		private String format = "yyyy-MM-dd HH:mm:ss";
		@Override
		public Object processArrayValue(Object value, JsonConfig config) {
			return process(value);
		}
		@Override
		public Object processObjectValue(String arg0, Object value, JsonConfig config) {
			return process(value);
		}
		private Object process(Object value){
			//遇到类型为日期的，就自动转换成“yyyy-MM-dd HH:mm:ss”格式的字符串
			if(value instanceof Date){
				SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.UK);
				return sdf.format(value);
			}
			return value == null ? "" : value.toString();
		}
	}


}
