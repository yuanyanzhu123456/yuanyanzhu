package com.geo.rcs.modules.source.service.impl;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.UpdateGroup;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.dao.EngineInterMapper;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.source.client.*;
import com.geo.rcs.modules.source.handler.ResponseParser;
import com.geo.rcs.modules.source.handler.ValidateHandler;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.sys.entity.Customer;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.geo.rcs.modules.sys.service.CustomerService;
import com.geo.rcs.modules.sys.service.SysUserService;
import freemarker.ext.servlet.HttpSessionHashModel;
import net.sf.json.util.JSONUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.*;

import static com.geo.rcs.common.StatusCode.RES_ERROR;

/**
 * 底层数据源接口请求-RawService
 * 
 * @author yongmingz
 * @created on 2017.12.29
 *
 */

@Service("interfaceService")
public class InterfaceServiceImpl implements InterfaceService {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private ClientTongDun clientTongDun;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private EngineInterMapper interMapper;

	@Autowired
	private FieldService fieldService;

	@Autowired
	private ClientDataPlatform clientDataPlatform;

    @Autowired
    private ClientDataPlatform2 clientDataPlatform2;

	@Autowired
	private EngineInterMapper engineInterMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** 验真平台接口请求数据名称常量 */
	private static final String REMOVE_KEY = "encrypted";
	private static final String PATH_URL = "/civp/getview/api/u/queryUnify";
	private static final String INTERFACE_TYPE = "innerIfType";
	private static final String AUTH_CODE = "authCode";
	private static final String CID = "cid";
	private static final String CIVP = "civp";

	/**
	 * 测试使用数据
	 */
	/** http://yz.geotmt.com、https://yz.geotmt.com */
	private static final String SERVER = "https://yz.geotmt.com";
	/** 是否加密传输 1是0否 */
	private static final int ENCRYPTED = 0;
	/**
	 * 加密类型和加密秘钥向GEO索取
	 * AES(秘钥长度不固定)、AES2(秘钥长度16)、DES(秘钥长度8)、DESede(秘钥长度24)、XOR(秘钥只能是数字)
	 */
	private static final String ENCRYPTION_TYPE = "AES2";
	/** 加密类型和加密秘钥向GEO索取(如果是获取数据的时候传的是RSA那么这里自己定义即可) */
	private static final String ENCRYPTION_KEY = "";
	/** 账户向GEO申请开通 */
	private static final String USER_NAME = "";
	/** GEO提供 */
	private static final String PASSWORD = "";
	/** GEO提供 */
	private static final String UNO = "";
	/** 只能填写""! */
	private static final String ETYPE = "";
	/** 是否进行数字签名 1是0否 */
	private static final int DSIGN = 0;

	/**
	 * 构造客户端(线程安全) 如果接入只是一个账号的话(一个账号对应一个对象)那么该类只需构造一次即可
	 */
	public static final Client client = new Client();

	/**
	 * 静态代码块，初始化测试使用数据
	 */
	static {
		client.setServer(SERVER);
		client.setEncrypted(ENCRYPTED);
		client.setEncryptionType(ENCRYPTION_TYPE);
		client.setEncryptionKey(ENCRYPTION_KEY);

		client.setUsername(USER_NAME);
		client.setPassword(PASSWORD);
		client.setUno(UNO);
		client.setEtype(ETYPE);
		client.setDsign(DSIGN);
	}

	@Override
	public Map getInterfaceDataMap(String innerIfType, String parameters, Long userId)
			throws Exception {

		String info = MessageFormat.format("{0},{1}", innerIfType, parameters);

		SysUser sysUser = sysUserService.selectByPrimaryKey(userId);

		logger.info(LogUtil.operation("数据源引擎", info, sysUser.getName(), TimeUtil.dqsj(), "启动引擎"));
		System.out.println(innerIfType + parameters);

		//geo合法参数集合
		innerIfType = ValidateHandler.interNameValidate(innerIfType);


		return getDataMap(innerIfType, parameters, userId);

	}
	@Override
	public String getInterfaceDataMap(String innerIfType, String parameters, Long userId,Integer type)
			throws Exception {

		String info = MessageFormat.format("{0},{1}", innerIfType, parameters);

		SysUser sysUser = sysUserService.selectByPrimaryKey(userId);

		logger.info(LogUtil.operation("数据源引擎", info, sysUser.getName(), TimeUtil.dqsj(), "启动引擎"));
		System.out.println(innerIfType + parameters);

		//geo合法参数集合
		List<String> interSet = ValidateHandler.getValidInterSet();
		innerIfType = ValidateHandler.interNameValidate(innerIfType);


		return getDataMap(innerIfType, parameters, userId,type);

	}

	/**
	 * 基本接口请求方法
	 * 
	 * @param innerIfType
	 * @param parameters,
	 *            接口参数
	 * @return Map
	 */
	@Override
	public Map<String, Map> getDataMap(String innerIfType, String parameters, Long userId) throws Exception {
		// 获取Customer对象
		Customer customer = customerService.findByUserId(userId);

		String info = MessageFormat.format("{0},{1}", innerIfType, parameters);

		SysUser sysUser = sysUserService.selectByPrimaryKey(userId);

		logger.info(LogUtil.operation("数据源引擎", info, sysUser.getName(), TimeUtil.dqsj(), "请求接口"));

		if (customer == null) {
			throw new RcsException(StatusCode.DATA_SOURCE_DENIED.getMessage(), StatusCode.DATA_SOURCE_DENIED.getCode());
		}
		ValidatorUtils.validateEntity(customer, UpdateGroup.class);

		// 获取HttpClient
		Client httpClient = getHttpClient(customer);

		// 请求url
		String path = httpClient.getServer() + PATH_URL;

		// 请求参数
		Map<String, String> params = JSONUtil.jsonToMap(parameters);

		if (innerIfType.equals("B18")) {
			params.put("longitude", params.get("workLongitude"));
		} else if (innerIfType.equals("B19")) {
			params.put("longitude", params.get("liveLongitude"));
		}
		params.remove(REMOVE_KEY);
		// 请求参数(client里面会自动加密,所以这里请使用明文)
		params.put(INTERFACE_TYPE, innerIfType);
		// 授权码得对应上客户合同编号
		params.put(AUTH_CODE, httpClient.rpad(UNO + ":" + params.get(CID), 32));

		// 请求数据接口返回json
		try{
			String data = httpClient.getData(path, params, CIVP);
			System.out.println(data);
			Map parseData = ResponseParser.ParserBasic(data, innerIfType);
			return parseData;
		}catch (RcsException e){
			throw new RcsException("接口:"+innerIfType+","+e.getMsg(),e.getCode());
		}
		catch (Exception e){
			throw new RcsException(StatusCode.DATA_SOURCE_OUTER_ERROR.getMessage()+"接口名:"+innerIfType,
					StatusCode.DATA_SOURCE_OUTER_ERROR.getCode());
		}
	}
	/**
	 * 基本接口请求方法
	 *
	 * @param innerIfType
	 * @param parameters,
	 *            接口参数
	 * @return Map
	 */
	@Override
	public String getDataMap(String innerIfType, String parameters, Long userId,Integer type) throws Exception {
		// 获取Customer对象
		Customer customer = customerService.findByUserId(userId);

		String info = MessageFormat.format("{0},{1}", innerIfType, parameters);

		SysUser sysUser = sysUserService.selectByPrimaryKey(userId);

		logger.info(LogUtil.operation("数据源引擎", info, sysUser.getName(), TimeUtil.dqsj(), "请求接口"));

		if (customer == null) {
			throw new RcsException(StatusCode.DATA_SOURCE_DENIED.getMessage(), StatusCode.DATA_SOURCE_DENIED.getCode());
		}
		ValidatorUtils.validateEntity(customer, UpdateGroup.class);

		// 获取HttpClient
		Client httpClient = getHttpClient(customer);

		// 请求url
		String path = httpClient.getServer() + PATH_URL;

		// 请求参数
		Map<String, String> params = JSONUtil.jsonToMap(parameters);

		if (innerIfType.equals("B18")) {
			params.put("longitude", params.get("workLongitude"));
		} else if (innerIfType.equals("B19")) {
			params.put("longitude", params.get("liveLongitude"));
		}
		params.remove(REMOVE_KEY);
		// 请求参数(client里面会自动加密,所以这里请使用明文)
		params.put(INTERFACE_TYPE, innerIfType);
		// 授权码得对应上客户合同编号
		params.put(AUTH_CODE, httpClient.rpad(UNO + ":" + params.get(CID), 32));

		// 请求数据接口返回json
		String data = httpClient.getData(path, params, CIVP);
		System.out.println(data);
//		Map parseData = ResponseParser.ParserBasic(data, innerIfType);

		return type==1?data:null;
	}

	/**
	 * 获取Client对象
	 * 
	 * @return
	 */
	public Client getHttpClient(Customer customer) {
		logger.info(LogUtil.operation("数据源引擎", customer.toString(), "", TimeUtil.dqsj(), "构造客户端"));
		Client httpClient = new Client();

		if (customer == null) {
			throw new RcsException(StatusCode.DATA_SOURCE_DENIED.getMessage(), StatusCode.DATA_SOURCE_DENIED.getCode());
		} else {

			try{
				httpClient.setServer(customer.getServer());
				httpClient.setEncrypted(customer.getEncrypted());
				httpClient.setEncryptionType(customer.getEncryptionType());
				httpClient.setEncryptionKey(customer.getEncryptionKey());

				httpClient.setUsername(customer.getUsername());
				httpClient.setPassword(customer.getPassword());
				httpClient.setUno(customer.getUno());
				httpClient.setEtype(customer.getEtype());
				httpClient.setDsign(customer.getDsign());
			}catch (NullPointerException e){
				throw new RcsException(StatusCode.DATA_SOURCE_DENIED.getMessage(), StatusCode.DATA_SOURCE_DENIED.getCode());
			}

		}

		return httpClient;
	}
	@Override
	public Map<String, Map> getInterfaceDataMapTongDun(List<String> tongDunInerList, String parameters) {
		// TODO Auto-generated method stub
		Map<String, Object> parmMap = JSONUtil.jsonToMap(parameters);
		if (parmMap.containsKey("idNumber")&&parmMap.containsKey("realName")&&parmMap.containsKey("cid")) {
			parmMap.put("id_number", parmMap.containsKey("idNumber"));
			parmMap.put("account_name", parmMap.containsKey("realName"));
			parmMap.put("account_mobile", parmMap.containsKey("cid"));
			parmMap.remove("idNumber");
			parmMap.remove("realName");
			parmMap.remove("cid");
			
			ClientTongDunResponse response = clientTongDun.invoke(parmMap);
			String result=response==null?"":response.toString();
			System.out.println("同盾数据源返回:"+result);
			Map<String, Map> parserBasicTongDun = ResponseParser.ParserBasicTongDun(response, tongDunInerList);
			return parserBasicTongDun;
		}else {
            throw new RcsException("同盾数据源参数缺失",RES_ERROR.getCode());

		}
	}

	@Override
	public String getInterfaceDataMapTongDun(List<String> tongDunInerList, String parameters,Integer type) {
		// TODO Auto-generated method stub
		Map<String, Object> parmMap = JSONUtil.jsonToMap(parameters);
		if (parmMap.containsKey("idNumber")&&parmMap.containsKey("realName")&&parmMap.containsKey("cid")) {
			parmMap.put("id_number", parmMap.containsKey("idNumber"));
			parmMap.put("account_name", parmMap.containsKey("realName"));
			parmMap.put("account_mobile", parmMap.containsKey("cid"));
			parmMap.remove("idNumber");
			parmMap.remove("realName");
			parmMap.remove("cid");

			ClientTongDunResponse response = clientTongDun.invoke(parmMap);
			String result=response==null?"":response.toString();
			System.out.println("同盾数据源返回:"+result);
//			Map<String, Map> parserBasicTongDun = ResponseParser.ParserBasicTongDun(response, tongDunInerList);
			return type==1?result:null;
		}else {
            throw new RcsException("同盾数据源参数缺失",RES_ERROR.getCode());

		}
	}

	@Override
	public List<EngineInter> getAllInter() {
		return interMapper.getAllInter();
	}

	@Override
	public Map<String, Map> getParameterCalcData(List<String> datasourceInnerList, String parameters) {

		Map<String,Map> result = new HashMap<>();
		ParameterCalcClient calcClient = new ParameterCalcClient();
		for (String inner : datasourceInnerList){
			  String data = "";
				if (inner.equals("INPUT_IDNUMBER")){
					data = calcClient.getData(inner,parameters);
				}else{
					String fieldName = fieldService.getFieldNameFromInnerName(inner);
					data = calcClient.getData(inner,parameters,fieldName);
				}

				result.putAll(ResponseParser.parserParameterCalcData(inner,data));
		}
		return result;
	}

	@Override
	public  Map<String,Map> getDataPlateFormData(List<String> dataPlateFormInnerList,String parameters){

		Map<String,Object> params = JSONUtil.jsonToMap(parameters);

		//传入大数据平台客户端的参数
		Map<String,Object> map = new HashedMap();
		//返回结果
		Map<String,Map> result = new HashedMap();

		List<EngineInter> interList = engineInterMapper.getIntersByNameList(dataPlateFormInnerList);

		//构建参数
		List<Map<String,Object>> paramsList = new ArrayList<>();
		for(EngineInter inter : interList){
			String interParam = inter.getParameters();
			Map<String,String> interParamMap = JSONUtil.jsonToMap(interParam);
			Map<String,Object> paramData = new HashedMap();
			paramData.put("indexName",inter.getName());
			for (Map.Entry<String,String> entry : interParamMap.entrySet()){
				paramData.put(entry.getKey(), params.get(entry.getKey()));
			}
			paramsList.add(paramData);
		}

		String paramsJson = JSONUtil.beanToJson(paramsList);
		map.put("paramsJson",paramsJson);
		try {
		    // 原请求方法
			// String dataJson = clientDataPlatform.invoke(map)
			String dataJson = clientDataPlatform2.post(map);
			result.putAll(ResponseParser.parserDataPlateFormData(dataJson));
		}catch(RcsException e){
			e.printStackTrace();
			throw e;
		} catch (ConnectException e) {
			e.printStackTrace();
			throw new RcsException("数据平台连接失败" + StatusCode.ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RcsException("访问数据平台失败"+StatusCode.ERROR);
		}

		return result;
	}
}
