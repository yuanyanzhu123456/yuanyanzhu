package com.geo.rcs.modules.source.controller;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.api.annotation.AuthIgnore;
import com.geo.rcs.modules.source.service.ApiMonitorService;
import com.geo.rcs.modules.source.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dataSource")
public class DataSourceController {

	@Autowired
	private SourceService sourceService;
	@Autowired
	private ApiMonitorService apiMonitorService;

	/**
	 * 使用JSON数据请求结果
	 * @param map
	 * @return
	 */
	@RequestMapping("/getFieldRes")
	public Geo getFieldRes(@RequestBody Map<String, String > map) {

		String userId = "";
		String rulesConfig = "";

		try {
			// @备注： Map不推荐使用<String, Object>
			// @备注： 使用map.get("userid").toString()时，userId为空会产生 NullPointerException
			userId = map.get("userId");
			rulesConfig = map.get("rulesConfig");

			LogUtil.info("API接口/dataSource/getFieldRes", "参数userId"+userId+" rulesConfig"+rulesConfig, "系统");

			return _getFieldRes(rulesConfig, userId, "/dataSource/getFieldResByJSON");

		} catch (Exception e) {
			LogUtil.error("/dataSource/getFieldRes", "参数userId"+userId+" rulesConfig"+rulesConfig, "系统", e);
			e.printStackTrace();
			if (e instanceof RcsException) {
				return Geo.error(((RcsException) e).getCode(), ((RcsException) e).getMsg());
			} else {
				return Geo.dataError();
			}
		}

	}

	/**
	 * 使用From表单数据请求结果，暂未使用
	 * @param rulesConfig
	 * @param userId
	 * @return
	 */

	@RequestMapping("/getFieldResByFrom")
	public Geo getFieldRes(String rulesConfig, String userId) {

		try {
			LogUtil.info("API接口/dataSource/getFieldRes", "参数userId"+userId+" rulesConfig"+rulesConfig, "系统");

			return _getFieldRes(rulesConfig, userId,  "/dataSource/getFieldRes");

		} catch (Exception e) {
			LogUtil.error("/dataSource/getFieldRes", "参数userId"+userId+" rulesConfig"+rulesConfig, "系统", e);
			e.printStackTrace();
			if (e instanceof RcsException) {
				return Geo.error(((RcsException) e).getCode(), ((RcsException) e).getMsg());
			} else {
				return Geo.dataError();
			}
		}

	}


	/**
	 * 使用JSON数据请求结果
	 * @param map
	 * @return
	 */
	@RequestMapping("/getInterRes")
	public Geo getFieldResMonitor(@RequestBody Map<String, String > map) {

		String userId = "";
		String _interList = "";
		String parms = "";
		try {
			userId = map.get("userId");
			_interList = map.get("interList");
			parms = map.get("params");

			LogUtil.info("API接口/dataSource/getInterRes",
					"参数userId"+userId+" interList"+ _interList+"  params" +parms,
					"系统");

			if( userId==null || userId.length()==0 || _interList==null || _interList.length()==0 ||
					parms==null || parms.length()==0){
				return Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage());
			}else{
				long start = System.currentTimeMillis();

				ArrayList<String> interList;
				try {
					Long id = Long.valueOf(userId);
					interList = JSONUtil.jsonToBean(_interList, ArrayList.class);
					List<String> fieldRes = apiMonitorService.getRulesSourceData(interList, parms , id, 1);
					LogUtil.info("API接口/dataSource/getInterResByJSON",
							"参数userId" + userId + " interList"+ _interList + "  params" + parms,
							"系统",
							"耗时"+(System.currentTimeMillis()-start)
					);
					return Geo.ok("成功", fieldRes);
				} catch (Exception e) {
					LogUtil.error("/dataSource/getInterRes",
							"参数userId" + userId + " interList"+ _interList +  "  params" + parms,
							"系统", e);
					e.printStackTrace();
					throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(),e);
				}
			}

		} catch (Exception e) {
			LogUtil.error("/dataSource/getInterRes",
					"参数userId" + userId + " interList"+ _interList +  "  params" + parms,
					"系统", e);
			if (e instanceof RcsException) {
				return Geo.error(((RcsException) e).getCode(), ((RcsException) e).getMsg());
			} else {
				e.printStackTrace();
				return Geo.dataError();
			}
		}
	}

	/**
	 * 使用FROM数据请求结果,暂未使用
	 * @param interList
	 * @param params
	 * @param userId
	 * @return
	 */
	@RequestMapping("/getInterResByFrom")
	public Geo getFieldResMonitor(String interList, String userId, String params) {

		try {
			LogUtil.info("API接口/dataSource/getInterRes",
					"参数userId"+userId+" interList"+ interList+"  params" +params,
					"系统");

			if( userId==null || userId.length()==0 || interList==null || interList.length()==0 ||
					params==null || params.length()==0){
				return Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage());
			}else{
				long start = System.currentTimeMillis();
				ArrayList<String> _interList;
				try {
					Long id = Long.valueOf(userId);
					_interList = JSONUtil.jsonToBean(interList, ArrayList.class);
					List<String>  fieldRes = apiMonitorService.getRulesSourceData(_interList, params , id, 1);
					LogUtil.info("API接口/dataSource/getInterRes",
							"参数userId" + userId + " interList"+ interList + "  params" + params,
							"系统",
							"耗时"+(System.currentTimeMillis()-start)
					);
					return Geo.ok("成功",fieldRes);
				} catch (Exception e) {
					LogUtil.error("/dataSource/getInterRes",
							"参数userId" + userId + " interList"+ interList +  "  params" + params,
							"系统", e);
					throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(),e);
				}
			}

		} catch (Exception e) {
			LogUtil.error("/dataSource/getInterRes",
					"参数userId" + userId + " interList"+ interList +  "  params" + params,
					"系统", e);
			if (e instanceof RcsException) {
				return Geo.error(((RcsException) e).getCode(), ((RcsException) e).getMsg());
			} else {
				e.printStackTrace();
				return Geo.dataError();
			}
		}
	}


	/**
	 * 子方法请求数据
	 * @param rulesConfig
	 * @param userId
	 * @return
	 */
	public Geo _getFieldRes(String rulesConfig, String userId, String api) throws Exception{
		if(userId==null|| userId.equals("") || rulesConfig==null || rulesConfig.equals("")){
			return Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage());
		}else{
			long start = System.currentTimeMillis();
			Long id = Long.valueOf(userId);
			String fieldRes = sourceService.getFieldRes(rulesConfig, id);
			LogUtil.info("API接口/dataSource/"+api,
					"参数userId"+userId+" rulesConfig"+rulesConfig,
					"系统",
					"耗时"+(System.currentTimeMillis()-start)
			);
			return Geo.ok("成功", fieldRes);
		}
	}

}
