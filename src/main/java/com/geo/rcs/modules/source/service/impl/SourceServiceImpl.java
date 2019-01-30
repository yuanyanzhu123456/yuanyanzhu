package com.geo.rcs.modules.source.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.constant.ConstantThreadPool;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.okhttp.okHttpClient;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.engine.entity.Parameters;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.source.handler.SourceFactory;
import com.geo.rcs.modules.source.handler.SourceMapper;
import com.geo.rcs.modules.source.handler.ValidateHandler;
import com.geo.rcs.modules.source.handler.WuXiParseResponse;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.source.service.SourceMapperService;
import com.geo.rcs.modules.source.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.source.service.impl
 * @Description : RuleEngineer
 * @Author yongmingz
 * @email yongmingz@geotmt.com
 * @Creation Date : 2018.1.11
 */

@Service
public class SourceServiceImpl implements SourceService {

	// 创建一个静态钥匙 -值是任意的
	static Object ThreadKey = "checkInterKey";

	//数据源类型
	public static final String GEO = "geo";

	public static final String TONG_DUN = "tongDun";

	/**
	 * 数据平台地址
	 */
	@Value("${dataSourceServer.rcsDataSource.url}")
	private String dataSourceServerUrl;

	@Autowired
	SourceMapperService sourceMapperService;

	@Autowired
	InterfaceService interfaceService;
	@Autowired
	WuXiParseResponse wuXiParseResponse;

	public final String getFieldRes() {
		return dataSourceServerUrl + "/dataSource/getFieldRes";
	}

	public final String getInterRes() {
		return dataSourceServerUrl + "/dataSource/getInterRes";
	}

	private Map<String, List<String>> systemValidInterMap = new HashMap<String, List<String>>();

	private static final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
			ConstantThreadPool.TASK_QUEUE_SIZE.getParm());

	ThreadPoolExecutor executor = new ThreadPoolExecutor(
			ConstantThreadPool.CORE_POOL_SIZE.getParm(),
			ConstantThreadPool.MAXIMUM_POOL_SIZE.getParm(),
			ConstantThreadPool.KEEP_ALIVE_TIME.getParm(),
			ConstantThreadPool.TIME_UNIT.getTimeUtil(),
			queue);

	private static final Integer MAX_TASK_NUM = ConstantThreadPool.MAX_TASK_NUM.getMaxTask();

	@Override
	public String getFieldRes(String rulesConfig, Long userId) throws Exception {

		long startTime = System.currentTimeMillis();
		System.out.println("[RCS-INFO]:数据源引擎启动运行！");

		Rules rules = JSONObject.parseObject(rulesConfig, Rules.class);
		rulesConfig = JSONObject.toJSONString(rules);

		rulesConfig = getRulesSourceData(rulesConfig, userId);

		long costTime = System.currentTimeMillis() - startTime;

		System.out.println("[RCS-INFO]:数据源引擎结束运行！costTime：" + costTime);

		return rulesConfig;
	}

	//	@Override
	//	public String getFieldRes(String rulesConfig, Long userId, Integer type) throws Exception {
	//
	//		long startTime = System.currentTimeMillis();
	//
	//		System.out.println("[RCS-INFO]:数据源引擎启动运行！");
	//
	//		Rules rules = JSON.parseObject(rulesConfig, Rules.class);
	//		rulesConfig = JSONObject.toJSONString(rules);
	//
	//		rulesConfig = getRulesSourceData(rulesConfig, userId, type);
	//
	//		long costTime = System.currentTimeMillis() - startTime;
	//
	//		System.out.println("[RCS-INFO]:数据源引擎结束运行！costTime："+ costTime);
	//
	//		return rulesConfig;
	//	}


	/**
	 * 调用三方数据源微服务模块，补充规则引擎输入数据
	 *
	 * @param
	 */

	@Override
	public String getFieldResByThird(String rulesConfig, Long userId) throws Exception {

		Map<String, Object> headersForm = new HashMap<String, Object>();
		Map<String, Object> postForm = new HashMap<String, Object>();
		postForm.put("rulesConfig", rulesConfig);
		postForm.put("userId", userId);

		if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
			throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
		}
		try {
			String resp = new okHttpClient().postByJSON(getFieldRes(),
					JSONObject.toJSONString(postForm),
					JSONObject.toJSONString(headersForm));
			return resp;
		} catch (ConnectException e) {
			throw new RcsException(StatusCode.DATASOURCE_CONN_ERROR.getMessage(), StatusCode.DATASOURCE_CONN_ERROR.getCode());
		} catch (SocketTimeoutException e) {
			throw new RcsException(StatusCode.DATASOURCE_CONN_TIMEOUT.getMessage(), StatusCode.DATASOURCE_CONN_TIMEOUT.getCode());
		} catch (Exception e) {
			throw new RcsException(StatusCode.DATASOURCE_ERROR.getMessage(), StatusCode.DATASOURCE_ERROR.getCode());
		}

	}

	/**
	 * 调用三方数据源微服务模块，请求原始接口数据
	 *
	 * @param
	 */

	@Override
	public String getInterResByThird(Long userId, List<String> interList, Map<String, String> parameters) throws Exception {

		Map<String, Object> headersForm = new HashMap<String, Object>();
		Map<String, Object> postForm = new HashMap<String, Object>();

		postForm.put("userId", userId.toString());
		postForm.put("interList", interList);
		postForm.put("params", parameters);

		if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
			throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
		}

		String resp = new okHttpClient().postByJSON(getFieldRes(),
				JSONObject.toJSONString(postForm),
				JSONObject.toJSONString(headersForm));

		return resp;
	}

	/**
	 * 调用三方数据源微服务模块，请求原始接口数据
	 *
	 * @param
	 */

	@Override
	public String getInterResByThird(Long userId, String innerIfType, Map<String, Object> parameters) {

		List<String> interList = new ArrayList<String>();
		interList.add(innerIfType);

		Map<String, Object> headersForm = new HashMap<String, Object>();
		Map<String, Object> postForm = new HashMap<String, Object>();

		postForm.put("interList", JSONObject.toJSONString(interList));
		postForm.put("params", JSONObject.toJSONString(parameters));
		postForm.put("userId", userId.toString());

		if (dataSourceServerUrl == null || dataSourceServerUrl.length() == 0) {
			throw new RcsException(StatusCode.DATASERVER_CONFIG_ERROR.getMessage(), StatusCode.DATASERVER_CONFIG_ERROR.getCode());
		}

		try {
			String resp = new okHttpClient().postByJSON(getFieldRes(),
					JSONObject.toJSONString(postForm),
					JSONObject.toJSONString(headersForm));
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return JSONObject.toJSONString(Geo.error(StatusCode.DATASOURCE_CONN_ERROR.getCode(), StatusCode.DATASOURCE_CONN_ERROR.getMessage()));
		}

	}

	/**
	 * 从数据源补充字段数据到RulesConfig
	 *
	 * @param rulesConfig
	 * @return
	 */
	private String getRulesSourceData(String rulesConfig, Long userId) throws Exception {

		SourceMapper sourceMapper = new SourceMapper();
		Long[] rulesFields = sourceMapperService.getFieldIds(rulesConfig);
		Rules rules = JSON.parseObject(rulesConfig, Rules.class);
		Parameters _parameters = rules.getParameters();
		String parameters = JSONObject.toJSONString(_parameters);

		/* 获取字段对应的接口集合 */
		List<EngineInter> rulesInter = sourceMapperService.getRulesInter(rulesFields);

		/* 创建数据字典 */
		Map<String, Map> rulesData = new HashMap<String, Map>();
		Map<String, Object> rulesRawData = new HashMap<String, Object>();

		/* 获取所有接口数据 */
		Map<String, Map> interData = new HashMap<>();
		String interParseData = "";

		List<String> tongDunInerList = new ArrayList<>();
		List<String> parameterCalcInerList = new ArrayList<>();
		List<String> intersList = new ArrayList<>();
		List<String> dataPlateFormInerList = new ArrayList<>();
		List<String> wXDataInerList= new ArrayList<>();
		List<String> wuXiWhiteList=new ArrayList<>();
		System.out.println(rulesInter.toString());
		for (EngineInter inter : rulesInter) {
			if (inter.getActive() == 0) {
				System.out.printf("接口已下线");
				throw new RcsException(StatusCode.INTER_NOTFOUND_ERROR.getMessage(), StatusCode.INTER_NOTFOUND_ERROR.getCode());
			}
			String interName = ValidateHandler.interNameValidate(inter.getName());
			intersList.add(interName);
			String dataSourceType = inter.getRequestType();

			System.out.println(interName + "==请求类型:" + dataSourceType);
			if (ValidateHandler.DATA_PLATFORM.equalsIgnoreCase(dataSourceType)){
				dataPlateFormInerList.add(interName);
				System.out.println("放入数据平台接口list"+dataPlateFormInerList);
				continue;
			}else if (ValidateHandler.PARAMETER_CALC.equalsIgnoreCase(dataSourceType)){
				parameterCalcInerList.add(interName);
				System.out.println("放入入参计算接口list"+parameterCalcInerList);
				continue;
			} else if (ValidateHandler.TONG_DUN.equals(dataSourceType)) {
				tongDunInerList.add(interName);
				System.out.println("放入同盾接口list" + tongDunInerList);
				continue;
			} else if (ValidateHandler.GEO.equals(dataSourceType)) {
				System.out.println("interName:" + interName + "请求geo");
				interData = interfaceService.getInterfaceDataMap(interName, parameters, userId);
				rulesData.putAll(interData);
			} else if(ValidateHandler.WU.equals(dataSourceType)){
				wXDataInerList.add(interName);
				System.out.println("无锡银行接口list"+wXDataInerList);
				continue;
			}else if (ValidateHandler.WU_XI_WHITE_LIST.equals(dataSourceType)){
				wuXiWhiteList.add(interName);
				System.out.println("无锡公积金白名单接口list"+wuXiWhiteList);
				continue;
			}
			else {
				System.out.println("接口参数错误：不识别的接口：" + interName + "  不在合法参数内：" + ValidateHandler.getValidInterSet());
				throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());
			}

		}
		// 获取同盾数据源接口数据
		if (tongDunInerList.size() > 0) {
			// 一次性请求同盾数据源
			Map<String, Map> mapTongDun = interfaceService
					.getInterfaceDataMapTongDun(tongDunInerList, parameters);
			rulesData.putAll(mapTongDun);
		}

		//获取入参计算平台接口数据
		if (parameterCalcInerList.size() > 0){
			Map<String,Map> mapParameterCalc = interfaceService.getParameterCalcData(parameterCalcInerList,parameters);
			rulesData.putAll(mapParameterCalc);
		}

		//获取数据平台接口数据
		if (dataPlateFormInerList.size() > 0){
			Map<String,Map> mapDataPlateForm = interfaceService.getDataPlateFormData(dataPlateFormInerList,parameters);
			rulesData.putAll(mapDataPlateForm);
		}
		//获取数据无锡银行接口数据
		if (wXDataInerList.size() > 0){
			Map<String,Map> mapDataPlateForm = wuXiParseResponse.parseData(wXDataInerList,parameters);
			rulesData.putAll(mapDataPlateForm);
		}
		if (wuXiWhiteList.size()>0){
			//TODO
			Map<String,Map> mapDataPlateForm = wuXiParseResponse.parseDataWhiteList(wuXiWhiteList,parameters);
			rulesData.putAll(mapDataPlateForm);
		}
		String rulesDataStr = JSON.toJSONString(rulesData);

		System.out.println("[RCS-INFO]:数据源调用外部返回结果：");
		System.out.println(rulesDataStr);

		/* 解析接口数据 */
		rulesConfig = SourceFactory.rulesDataPackager(rulesConfig, rulesData);

		System.out.println("[RCS-INFO]:数据源引擎返回结果：");
		System.out.println(rulesConfig);
		return rulesConfig;
	}


	/**
	 * 从数据源补充字段数据到RulesConfig
	 *
	 * @param rulesConfig
	 * @return
	 */
	private String getRulesSourceData(String rulesConfig, Long userId, Integer type) throws Exception {

		SourceMapper sourceMapper = new SourceMapper();
		Long[] rulesFields = sourceMapperService.getFieldIds(rulesConfig);
		Rules rules = JSON.parseObject(rulesConfig, Rules.class);
		Parameters _parameters = rules.getParameters();
		String parameters = JSONObject.toJSONString(_parameters);

		/* 获取字段对应的接口集合 */
		List<EngineInter> rulesInter = sourceMapperService.getRulesInter(rulesFields);


		/* 获取所有接口数据 */
		String interData = null;
		String interParseData = "";
		ArrayList<String> interResultList = new ArrayList<>();


		List<String> tongDunInerList = new ArrayList<>();
		System.out.println(rulesInter.toString());
		for (EngineInter inter : rulesInter) {
			String interName = ValidateHandler.interNameValidate(inter.getName());
			String dataSourceType = inter.getRequestType();;
			System.out.println(interName + "==请求类型:" + dataSourceType);
			if (ValidateHandler.TONG_DUN.equals(dataSourceType)) {
				tongDunInerList.add(interName);
				System.out.println("放入同盾接口list" + tongDunInerList);
				continue;
			} else if (ValidateHandler.GEO.equals(dataSourceType)) {
				System.out.println("interName:" + interName + "请求geo");
				if (type == 1) {
					interData = interfaceService.getInterfaceDataMap(interName, parameters, userId, type);
					interResultList.add(interData);
				}
			} else {
				System.out.println("接口参数错误：不识别的接口：" + interName + "  不在合法参数内：" + ValidateHandler.getValidInterSet());
				throw new RcsException(StatusCode.PARAMS_ERROR.getMessage(), StatusCode.PARAMS_ERROR.getCode());

			}

		}
		// 获取同盾数据源接口数据
		if (tongDunInerList.size() > 0) {
			// 一次性请求同盾数据源
			interData = interfaceService
					.getInterfaceDataMapTongDun(tongDunInerList, parameters, type);
			interResultList.add(interData);
		}

		System.out.println("[RCS-INFO]:数据源调用外部返回结果：");

		/* 解析接口数据 */
		rulesConfig = JSON.toJSONString(interResultList);
//		System.out.println("[RCS-INFO]:数据源引擎返回结果：");
		System.out.println(rulesConfig);
//		ArrayList arrayList = JSONUtil.jsonToBean(rulesConfig, ArrayList.class);
		return rulesConfig;
	}

	/**
	 * 获取所有合法接口
	 *
	 * @return
	 */
	public Map<String, List<String>> getInterMap() {

		List<EngineInter> interMap = interfaceService.getAllInter();
		for (EngineInter engineInter : interMap) {
			if (systemValidInterMap.get(engineInter.getRequestType()) == null) {
				List<String> list = new ArrayList<>();
				systemValidInterMap.put(engineInter.getRequestType(),
						list);
				systemValidInterMap.get(engineInter.getRequestType()).add(engineInter.getName());
			} else {
				systemValidInterMap.get(engineInter.getRequestType()).add(engineInter.getName());
			}
		}
		return systemValidInterMap;
	}
}
