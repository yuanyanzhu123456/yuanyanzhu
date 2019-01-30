package com.geo.rcs.modules.source.handler;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.modules.source.client.ClientTongDun;
import com.geo.rcs.modules.source.client.ClientTongDunResponse;
import org.apache.commons.collections.map.HashedMap;

import java.util.*;

import static com.geo.rcs.common.StatusCode.RES_ERROR;
import static com.geo.rcs.modules.source.entity.InterStatusCode.BASICDICT;
import static com.geo.rcs.modules.source.entity.InterStatusCode.ECLDICT;

public class ResponseParser {
	// 初始化常量
	private static final String CODE = "code";
	private static final String SUCCSEE_CODE = "200";
	private static final String DATA = "data";
	private static final String ISPNUM = "ISPNUM";
	private static final String RSL = "RSL";
	private static final String ECL = "ECL";
	private static final String IFT = "IFT";
	private static final String RS = "RS";
	private static final String DESC = "desc";
	private static final String INTER = "inter";
	private static final String FIELD = "field";
	private static final String VALUE = "value";
	private static final String VALUEDESC = "valueDesc";

	/**
	 * 主方法:解析集奥聚合返回数据
	 * 
	 * @param data
	 *            例：
	 *            "{\"code\":\"200\",\"data\":{\"ISPNUM\":{\"province\":\"湖北\",\"city\":\"宜昌\",\"isp\":\"联通\"
	 *            },
	 *            \"RSL\":[{\"RS\":{\"code\":\"3\",\"desc\":\"(24,+)\"},\"IFT\":\"A3\"},{\"RS\":{\"code\":\"002\",\"desc\":\"(40,80]\"
	 *            },
	 *            \"IFT\":\"B1\"},{\"RS\":{\"code\":\"0\",\"desc\":\"正常在用\"},\"IFT\":\"A4\"},{\"RS\":{\"code\":\"0\",\"desc\":\"三维验证一致\"
	 *            },
	 *            \"IFT\":\"B7\"},{\"RS\":{\"code\":\"1\",\"desc\":\"否\"},\"IFT\":\"B11\"},],\"ECL\":[{\"code\":\"10000002\",\"IFT\":\"B13\"
	 *            }]}, \"msg\":\"成功\"}"
	 * @param innerIfType
	 *            接口类型
	 * @return
	 */
	public static Map<String, Map> ParserBasic(String data, String innerIfType) {
		Map<String, Map> interData = new HashMap<>();

		if (data != null && data.length() != 0) {
			JSONObject response = JSONObject.parseObject(data);
			if (response.getString(CODE).equals(SUCCSEE_CODE)) {
				JSONObject basicInfo = response.getJSONObject(DATA).getJSONObject(ISPNUM);
				JSONArray interRslData = response.getJSONObject(DATA).getJSONArray(RSL);    
				JSONArray interEclData = response.getJSONObject(DATA).getJSONArray(ECL);

				/**
				 * 归属地信息字段数据
				 */
				if (basicInfo != null) {
					for (String key : basicInfo.keySet()) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, "");
						_interData.put(FIELD, key);
						_interData.put(VALUE, basicInfo.getString(key));
						_interData.put(VALUEDESC, "");
						interData.put(key, _interData);
					}
				}

				/**
				 * 正常调用结果字段数据
				 */
				interData.putAll(paserInterData(interRslData, innerIfType));

				/**
				 * 异常结果字段数据
				 */
				if (ValidateHandler.INTER2FIELD.keySet().contains(innerIfType)) {
					for (int i = 0; i < interEclData.size(); i++) {
						Map<String, Object> _interData = new HashMap<>();
						JSONObject object = (JSONObject) interEclData.get(i);
						String inter = object.getString(IFT);
						String code = object.getString(CODE);

						// todo: 单接口字典表
						String field = ValidateHandler.INTER2FIELD.get(inter);
						_interData.put(INTER, inter);
						_interData.put(FIELD, field);
						_interData.put(VALUE, code);
						_interData.put(VALUEDESC, ECLDICT.get(code));
						interData.put(field, _interData);
					}
				} else if (ValidateHandler.MULTIINTER2FIELD.keySet().contains(innerIfType)) {
					for (int i = 0; i < interEclData.size(); i++) {
						Map<String, Object> _interData = new HashMap<>();
						JSONObject object = (JSONObject) interEclData.get(i);
						String inter = object.getString(IFT);
						String code = object.getString(CODE);

						// todo: 多接口字典表
						for (String field : ValidateHandler.MULTIINTER2FIELD.get(innerIfType)) {
							_interData.put(INTER, inter);
							_interData.put(FIELD, field);
							_interData.put(VALUE, code);
							_interData.put(VALUEDESC, ECLDICT.get(code));
							interData.put(field, _interData);
						}
					}
				}

			} else {
				String errorCode = response.getString("code");
				throw new RcsException(BASICDICT.get(errorCode), RES_ERROR.getCode());
			}
		} else {
			throw new RcsException(RES_ERROR.getMessage(), RES_ERROR.getCode());
		}

		return interData;
	}

	/**
	 * 子方法: 解析集奥风控详细接口数据
	 * 
	 * @param interRslData
	 * @param innerIfType
	 * @return
	 */
	public static Map<String, Map> paserInterData(JSONArray interRslData, String innerIfType) {
		Map<String, Map> interData = new HashMap<>();
		Map<String, List<String>> interfaceDic = ValidateHandler.getInterfaceDic();

		for (int i = 0; i < interRslData.size(); i++) {
			JSONObject object = (JSONObject) interRslData.get(i);
			String inter = object.getString(IFT);
			String code = object.getJSONObject(RS).getString(CODE);
			String desc = object.getJSONObject(RS).getString(DESC);

			if (interfaceDic.get(ValidateHandler.SINGLEFIELD_1).contains(innerIfType)) {
				// todo: 单接口字段表,"code": "0","desc": "(0,6]"
				Map<String, Object> _interData = new HashMap<>();
				_interData.put(INTER, inter);
				_interData.put(FIELD, ValidateHandler.INTER2FIELD.get(inter));
				_interData.put(VALUE, code);
				_interData.put(VALUEDESC, desc);
				interData.put(ValidateHandler.INTER2FIELD.get(inter), _interData);

			} else if (interfaceDic.get(ValidateHandler.SINGLEFIELD_2).contains(innerIfType)) {
				// todo: 单接口特殊字段表,c6 "code": "-9999","desc": "ZTE-NX511J" 2.3.30
				// C6-手机号终端型号查询
				Map<String, Object> _interData = new HashMap<>();
				_interData.put(INTER, inter);
				_interData.put(FIELD, ValidateHandler.INTER2FIELD.get(inter));
				_interData.put(VALUE, desc);
				_interData.put(VALUEDESC, "");
				interData.put(ValidateHandler.INTER2FIELD.get(inter), _interData);

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_1).contains(innerIfType)) {
				// todo: 多接口字段表1 Y1 姓名身份证号验证"code": "-9999","desc":
				// "{\"birthday\":\"19880502\",\"gender\":\"男\",\"originalAdress\":\"黑龙江省齐齐哈尔市克东县\",\"identityResult\":\"0\",\"age\":\"28\"}"

				Map<String, Object> descMap = JSONUtil.jsonToMap(desc);
				if (descMap != null && !descMap.isEmpty()) {
					for (String key : descMap.keySet()) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, inter);
						_interData.put(FIELD, key);
						_interData.put(VALUE, descMap.get(key));
						_interData.put(VALUEDESC, "");
						interData.put(key, _interData);
						System.out.println(key);
					}
				}

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_5).contains(innerIfType)) {
				// todo: 多接口字段表5 Y1 评分模型  解析字段 = 接口_+结果字段
				// "{\"birthday\":\"19880502\",\"gender\":\"男\",\"originalAdress\":\"黑龙江省齐齐哈尔市克东县\",\"identityResult\":\"0\",\"age\":\"28\"}"

				Map<String, Object> descMap = JSONUtil.jsonToMap(desc);
				if (descMap != null && !descMap.isEmpty()) {
					for (String key : descMap.keySet()) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, inter);
						_interData.put(FIELD, inter+"_"+key);
						_interData.put(VALUE, descMap.get(key));
						_interData.put(VALUEDESC, "");
						interData.put(inter+"_"+key, _interData);
						System.out.println(inter+"_"+key);
					}
				}

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_2).contains(innerIfType)) {
				// todo: 多接口特殊字段表2 Z7 银行卡钱包位置查询 "code": "-9999","desc":
				// "{\"error_code\":0,\"data\":[{\"account_no\":\"6228483738174751273\",\"CSSP001\":\"3\",\"score\":null}]}"

				Map<String, Object> descMap = JSONUtil.jsonToMap(desc);
				List<Map<String, Object>> dataList = JSONUtil.jsonToBean(JSONUtil.beanToJson(descMap.get(DATA)),
						List.class);
				for (Map<String, Object> map : dataList) {
					if (map != null && !map.isEmpty()) {
						for (String key : map.keySet()) {
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, key);
							_interData.put(VALUE, map.get(key));
							_interData.put(VALUEDESC, "");
							interData.put(key, _interData);
							System.out.println(key);

						}
					}
				}

			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_3).contains(innerIfType)) {
				// todo: 多接口多维数据字段表3,
				Map<String, Object> descMap = JSONUtil.beanToMap(desc);
				for (String key : descMap.keySet()) {
					Object obj = descMap.get(key);
					if (obj instanceof List) {
						List<Map<String, Object>> innerList = (List<Map<String, Object>>) obj;
						for (int j = 0; j < innerList.size(); j++) {
							Map<String, Object> innerMap = innerList.get(j);
							for (String innerKey : innerMap.keySet()) {
								Map<String, Object> _interData = new HashMap<>();
								String field = key + "_" + j + "_" + innerKey;
								_interData.put(INTER, inter);
								_interData.put(FIELD, field);
								_interData.put(VALUE, innerMap.get(innerKey));
								_interData.put(VALUEDESC, "");
								interData.put(field, _interData);
								System.out.println(field);
							}
						}
					} else if (obj instanceof Map) {
						Map<String, Object> map = (Map<String, Object>) obj;
						for (String innerKey : map.keySet()) {
							Map<String, Object> _interData = new HashMap<>();
							String field = key + "_" + innerKey;
							_interData.put(INTER, inter);
							_interData.put(FIELD, field);
							_interData.put(VALUE, map.get(innerKey));
							_interData.put(VALUEDESC, "");
							interData.put(field, _interData);
							System.out.println(field);

						}
					} else if (obj instanceof String) {
						Map<String, Object> _interData = new HashMap<>();
						_interData.put(INTER, inter);
						_interData.put(FIELD, key);
						_interData.put(VALUE, obj);
						_interData.put(VALUEDESC, "");
						interData.put(key, _interData);
						System.out.println(key);

					}
				}
			} else if (interfaceDic.get(ValidateHandler.MULTIFIELD_4).contains(innerIfType)) {
				// todo: 多接口多维数据字段表4
				JSONArray descArray = JSONArray.parseArray(desc);
				for (int j = 0; j < descArray.size(); j++) {
					String expandKey = Integer.toString(j);
					Map<String, Object> descMap = JSONUtil.jsonToMap(JSONObject.toJSONString(descArray.get(j)));
					if (descMap != null && !descMap.isEmpty()) {
						String _expandKey = descMap.get("dataType").toString();
						expandKey = (_expandKey != null && _expandKey.length() != 0) ? _expandKey : expandKey;

						for (String key : descMap.keySet()) {
							Map<String, Object> _interData = new HashMap<>();
							_interData.put(INTER, inter);
							_interData.put(FIELD, expandKey + "_" + key);
							_interData.put(VALUE, descMap.get(key));
							_interData.put(VALUEDESC, "");
							interData.put(expandKey + "_" + key, _interData);
							System.out.println(expandKey + "_" + key);
						}
					}
				}
			}
		}

		if (ValidateHandler.MULTIINTER2FIELD.keySet().contains(innerIfType)) {
			List<String> keyList = new ArrayList<>(interData.keySet());
			for (int i = keyList.size() - 1; i >= 0; i--) {
				if (!ValidateHandler.MULTIINTER2FIELD.get(innerIfType).contains(keyList.get(i))) {
					interData.remove(keyList.get(i));
				}
			}
		}

		String json = JSONUtils.toJSONString(interData);
        return interData;
	}

	/**
	 * 主方法: 解析同盾接口返回数据
	 * 
	 * @param response
	 * @param tongDunInerList
	 * @return
	 */
	public static Map<String, Map> ParserBasicTongDun(ClientTongDunResponse response, List<String> tongDunInerList) {
		
		// 解析字段结果集合
		Hashtable<String, Map> interResultMap = new Hashtable<>();
		
		if (response == null) {
			throw new RcsException("同盾数据源服务异常", RES_ERROR.getCode());
		} else if (!response.getSuccess()) {
			throw new RcsException("同盾数据源" + response.getReason_desc(), RES_ERROR.getCode());
		} else {
			Map<String, List<String>> interfaceDicTongDun = ValidateHandler.getInterfaceDicTongDun();
			Map<String, Object> resultMap = JSONUtil.jsonToMap(response.getResult_desc());
			Set<String> fieldKeySet = ValidateHandler.getTongDunFieldMap().keySet();
			for (String inter : tongDunInerList) {
			
				if (ValidateHandler.MULTIINTER2FIELD.keySet().contains(inter)) {
					// 判断接口是否已注册为合法接口
					if (interfaceDicTongDun.get(ValidateHandler.MULTILFIELD1_RESULT_TONGDUN).contains(inter)) {
						for (String key : resultMap.keySet()) {
							Map<String, Object> subResultMap = (Map<String, Object>) resultMap.get(key);
							for (String subKey : subResultMap.keySet()) {
								String parentKey = "";
								if ("risk_items".equals(subKey)) {
									parentKey = (key).toLowerCase();
								} else {
									parentKey = (key + "_" + subKey).toLowerCase();
								}
								// 判断字段结果类型,如不是list和map可以直接作为可用字段
								if (!(subResultMap.get(subKey) instanceof List) && !(subResultMap.get(subKey) instanceof Map)) {
									HashMap<String, Object> hashMap = new HashMap<>();
									hashMap.put(FIELD, parentKey);
									hashMap.put(VALUE, subResultMap.get(subKey));
									hashMap.put(INTER, inter);
									hashMap.put(VALUEDESC, "");
									interResultMap.put(parentKey, hashMap);
								} else if (subResultMap.get(subKey) instanceof List) {
									List<Map<String, Object>> subValueList = (List<Map<String, Object>>) subResultMap.get(subKey);
									for (Map<String, Object> map : subValueList) {
										// 使用riskName映射自定义riskNameKey区分多个规则
										String riskName = (String) map.get("risk_name");
										if (fieldKeySet.contains(riskName)) {
											
											String fieldTempKey = parentKey + "_" + ValidateHandler.getTongDunFieldMap().get(riskName);
											for (String itemsKey: map.keySet()) {
												// itemsKey的值不为List或Map时,可选取为解析字段
												if(!(map.get(itemsKey) instanceof List) && !(map.get(itemsKey) instanceof Map)){
													HashMap<String, Object> hashMap = new HashMap<>();
													String fieldKey=fieldTempKey+"_"+itemsKey;
													hashMap.put(FIELD, fieldKey);
													hashMap.put(VALUE, map.get(itemsKey).toString());
													hashMap.put(INTER, inter);
													hashMap.put(VALUEDESC, "");
													interResultMap.put(fieldKey, hashMap);
													
												// itemsKey的值为List时,可自定义解析新字段
												}else if ((map.get(itemsKey) instanceof List)) {
													// 使用parserTongdunAntiRiskDetails方法, 自定义解析规则List类型的risk_detail
													List<Map<String, Object>> riskDetail = (List<Map<String, Object>>) map.get(itemsKey);
													interResultMap.putAll(parserTongdunAntiRiskDetails(inter, riskDetail, fieldTempKey));
												
												// itemsKey的值为Map时,可自定义解析新字段
												}else if ((map.get(itemsKey) instanceof Map)) {
													// 使用parserTongdunAntiRiskDetailsMap方法, 自定义解Map类型的risk_detail
													Map<String, Object> riskDetailMap = (Map<String, Object>) map.get(itemsKey);
													interResultMap.putAll(parserTongdunAntiRiskDetailsMap(inter, riskDetailMap, fieldTempKey));
												}
											}
										}else {
											//尚未注册的字段
//											String FindFieldJson = JSONUtil.beanToJson(map);
//											System.out.println("尚未注册字段:"+FindFieldJson);
											
											
										}
									}									
								} else if (subResultMap.get(subKey) instanceof Map) {
									Map<String, Object> map3 = (Map<String, Object>) subResultMap.get(subKey);
									for (String key3 : map3.keySet()) {
										String field3 = parentKey + "_" + key3;
										HashMap<String, Object> hashMap2 = new HashMap<>();
										hashMap2.put(FIELD, field3);
										hashMap2.put(VALUE, map3.get(key3));
										hashMap2.put(INTER, inter);
										hashMap2.put(VALUEDESC, "");
										interResultMap.put(field3, hashMap2);
									}
								}
							}
						} 
					}else {
						throw new RcsException("同盾数据源"+inter+"没有合法解析方式", RES_ERROR.getCode());
					}
				}
			}
		}

		// 所有解析的字段需要通过验证是否注册为合法字段,  防止接口新出现的字段解析数据造成污染
		interResultMap = validateFieldTongDun(interResultMap);
		System.out.println("=================");
		for (String key : interResultMap.keySet()) {
System.out.println(key);
		}
		System.out.println("=================");
		return interResultMap;

	}

	/**
	 * 子方法: 自定义解析同盾信贷保镖风险规则Map--详情
	 * @param inter
	 * @param detailMap
	 * @param riskNameKey
	 * @return
	 */
	private static Hashtable<String, Map> parserTongdunAntiRiskDetailsMap(String inter, Map<String, Object> detailMap, String riskNameKey) {
		Hashtable<String, Map> _interResultMap = new Hashtable<>();
		//解析:未来新规则详情,Map类型
		return _interResultMap;
	}

	/**
	 * 子方法: 自定义解析同盾信贷保镖风险规则列表--详情
	 * @param inter
	 * @param detailList
	 * @param riskNameKey
	 */
	private static Hashtable<String, Map> parserTongdunAntiRiskDetails(String inter, List<Map<String, Object>> detailList, String riskNameKey) {
		
		Hashtable<String, Map> _interResultMap = new Hashtable<>();
		//解析:6个月内申请人手机多平台申请规则
		if(riskNameKey.equals("antifraud_multiple_6m")){
			
			for (Map<String, Object> map2 : detailList) {
			Object object = map2.get("platform_detail_dimension");
			if (object instanceof List) {
				List<Map<String, Object>> dimensionList = (List<Map<String, Object>>) object;
				for (Map<String, Object> map3 : dimensionList) {
					if ("手机".equals(map3.get("dimension"))) {
						String field5key = riskNameKey + "_" + "byphone";
						HashMap<String, Object> hashMap4 = new HashMap<>();
						hashMap4.put(FIELD, field5key);
						hashMap4.put(VALUE, map3.get("count").toString());
						hashMap4.put(INTER, inter);
						hashMap4.put(VALUEDESC, map3.get("detail").toString());
						_interResultMap.put(field5key, hashMap4);
					} else if ("身份证号码".equals(map3.get("dimension"))) {
						String field5key = riskNameKey + "_" + "bycard";
						HashMap<String, Object> hashMap4 = new HashMap<>();
						hashMap4.put(FIELD, field5key);
						hashMap4.put(VALUE, map3.get("count").toString());
						hashMap4.put(INTER, inter);
						hashMap4.put(VALUEDESC, map3.get("detail").toString());
						_interResultMap.put(field5key, hashMap4);

					}
				}
				}
			}
		return _interResultMap;
			
		// 解析:其他字段定义,参考各字段父Key, 如antifraud_id_overdue_rule_id的父Key 是 antifraud_id_overdue
		}else{
			return _interResultMap;
		}

	}

	private static Hashtable<String, Map> validateFieldTongDun(Hashtable<String, Map> interResultMap) {
		// TODO Auto-generated method stub
		Set<String> keySet = interResultMap.keySet();
		List<String> keyList = new ArrayList<>(interResultMap.keySet());
		for (int i = keyList.size() - 1; i >= 0; i--) {
			if (!ValidateHandler.MULTIINTER2FIELD.get(ValidateHandler.TONG_DUN_ALLINTER_FIELDS).contains(keyList.get(i))) {
				interResultMap.remove(keyList.get(i));
			}
		}
		return interResultMap;
	}

	/**
	 * 解析入参计算返回的数据
	 * @param inter
	 * @param data
	 * @return
	 */
	public static Map<String,Map> parserParameterCalcData(String inter,String data){

		Map<String,Object> map = JSONUtil.jsonToMap(data);
		Map<String,Map> result = new HashedMap();
		for(Map.Entry<String,Object> entry : map.entrySet()){
			Map<String, Object> _interData = new HashMap<>();
			_interData.put(INTER, inter);
			_interData.put(FIELD, entry.getKey());
			_interData.put(VALUE, entry.getValue());
			_interData.put(VALUEDESC, "");
			result.put(entry.getKey(), _interData);
		}
		return result;
	}

	public static Map<String,Map> parserDataPlateFormData(String data ){

		List<Map<String,Object>> list = JSONUtil.jsonToBean(data,List.class);
		Map<String,Map> result = new HashedMap();

		for (Object obj : list){
			String objJson = JSON.toJSONString(obj);
			Map<String,Object> map =  JSONUtil.jsonToMap(objJson);
			Map<String, Object> _interData = new HashMap<>();
			_interData.put(INTER,map.get("indexName"));
			_interData.put(FIELD, map.get("indexName"));
			_interData.put(VALUE, map.get("num"));
			_interData.put(VALUEDESC, "");
			result.put((String)map.get("indexName"),_interData);
		}
		return result;
	}

	/**
	 * 主方法:测试对各类型的解析方法
	 * @param args
	 */
	public static void main1(String[] args) {
		// String json =
		// "{\"code\":\"200\",\"data\":{\"ISPNUM\":{\"province\":\"北京\",\"city\":\"北京\",\"isp\":\"联通\"},\"RSL\":[{\"RS\":{\"code\":\"-9999\",\"desc\":\"{\"result_xdpt\":[{\"REGISTERTIME\":\"2016/8/4
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_101694\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/2/7
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100046\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/1/10
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100852\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/9/6
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100630\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/8/12
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100040\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/1/10
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100168\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/9/8
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100505\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/12/3
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100593\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/3/6
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100060\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/3/24
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100079\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/7/28
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_101607\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/2/7
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_101725\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/4/7
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100824\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/2/9
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100047\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/2/6
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_102452\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/9/6
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_102209\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/9/13
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100476\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/8/6
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_102165\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/7/29
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100635\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/1/7
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_100525\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/1/10
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_102305\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2016/9/11
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_102301\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017/4/18
		// 0:00:00\",\"PLATFORMCODE\":\"EM21_101333
		// \",\"P_TYPE\":\"2\"}]}}}],\"ECL\":[]},\"msg\":\"成功\"}";
//		String json = "{\"code\":\"200\",\"data\":{\"ISPNUM\":{\"province\":\"山东\",\"city\":\"枣庄\",\"isp\":\"电信\"},\"RSL\":[{\"RS\":{\"code\":\"-9998\",\"desc\":\"{\\\"result_YQ_ZZSJ\\\":null,\\\"result_YQ_ZJSJ\\\":null,\\\"result_YQ_LJCS\\\":null,\\\"result_YQ_DQJE\\\":null,\\\"result_YQ_DQSC\\\":null,\\\"result_YQ_ZDJE\\\":null,\\\"result_YQ_ZDSC\\\":null,\\\"result_QZ_ZZSJ\\\":null,\\\"result_QZ_ZJSJ\\\":null,\\\"result_QZ_LJCS\\\":null,\\\"result_SX_ZZSJ\\\":null,\\\"result_SX_ZJSJ\\\":null,\\\"result_SX_LJCS\\\":null}\"},\"IFT\":\"T20103\"}],\"ECL\":[]},\"msg\":\"成功\"}";
//		String m41json = "{\"code\":\"200\",\"data\":{\"ISPNUM\":{\"province\":\"河北\",\"city\":\"石家庄\",\"isp\":\"联通\"},\"RSL\":[{\"RS\":{\"code\":\"-9999\",\"desc\":\"[{\\\"yiju\\\":\\\"，本院不予支持。依照《中华人民共和国民事诉讼法》第一百七十条第一款第一项、第一百七十一条\\\",\\\"pname\\\":\\\"赵玉柏\\\",\\\"sortTime\\\":1508083200000,\\\"dataType\\\":\\\"cpws\\\",\\\"judgeResult\\\":\\\"驳回上诉，维持原裁定。本裁定为终审裁定。\\\",\\\"matchRatio\\\":0.8,\\\"body\\\":\\\"被上诉人（原审原告）:赵玉柏;...\\\",\\\"court\\\":\\\"山东省枣庄市中级人民法院\\\",\\\"title\\\":\\\"赵玉祥、赵玉柏赠与合同纠纷二审民事裁定书\\\",\\\"caseNo\\\":\\\"（2017）鲁04民辖终115号\\\",\\\"caseType\\\":\\\"民事裁定书\\\",\\\"entryId\\\":\\\"c20173704minxiazhong115_t20171016\\\",\\\"courtRank\\\":\\\"3\\\",\\\"caseCause\\\":\\\"赠与合同纠纷\\\",\\\"sortTimeString\\\":\\\"2017年10月16日\\\",\\\"judge\\\":\\\"审判长:廖建新,审判员:张洪光,审判员:杜兆锋,书记员:李　晶\\\",\\\"partyId\\\":\\\"c20173704minxiazhong115_t20171016_pzhaoyubo_rt_111\\\",\\\"trialProcedure\\\":\\\"二审\\\"}]\"},\"IFT\":\"T20105\"}],\"ECL\":[]},\"msg\":\"成功\"}\n";
//		String m4json = "{\n" + "\t\"code\": \"200\",\n" + "\t\"data\": {\n" + "\t\t\"ISPNUM\": {\n"
//				+ "\t\t\t\"province\": \"河北\",\n" + "\t\t\t\"city\": \"石家庄\",\n" + "\t\t\t\"isp\": \"联通\"\n"
//				+ "\t\t},\n" + "\t\t\"RSL\": [{\n" + "\t\t\t\"RS\": {\n" + "\t\t\t\t\"code\": \"-9999\",\n"
//				+ "\t\t\t\t\"desc\": \"[{\\\"jtqx\\\":\\\"有履行能力而拒不履行生效法律文书确定义务的\\\",\\\"sortTime\\\":1495123200000,\\\"pname\\\":\\\"董庆彬\\\",\\\"sex\\\":\\\"男\\\",\\\"dataType\\\":\\\"shixin\\\",\\\"idcardNo\\\":\\\"2305231993****231X\\\",\\\"matchRatio\\\":0.99,\\\"body\\\":\\\"...9 宝清县人民法院 董庆彬...\\\",\\\"court\\\":\\\"宝清县人民法院\\\",\\\"caseNo\\\":\\\"（2017）黑0523执367号\\\",\\\"yjCode\\\":\\\"（2017）黑0523执367号\\\",\\\"entryId\\\":\\\"c2017230523zhi367_t20170519_pdongqingbin\\\",\\\"postTime\\\":1495641600000,\\\"sortTimeString\\\":\\\"2017年05月19日\\\",\\\"lxqk\\\":\\\"全部未履行\\\",\\\"province\\\":\\\"黑龙江\\\",\\\"yiwu\\\":\\\"一、撤销黑龙江省红兴隆农垦法院（2011）红刑初字第12号刑事判决的缓刑部分；二、被告人董庆彬犯盗窃罪，判处有期徒刑三年一个月，并处罚金5000元，原判刑罚一年一个月，并处罚金4000元，决定执行有期徒刑四年，并处罚金9000元。\\\",\\\"yjdw\\\":\\\"宝清县人民法院\\\",\\\"age\\\":0},{\\\"sortTime\\\":1495123200000,\\\"pname\\\":\\\"董庆彬\\\",\\\"dataType\\\":\\\"zxgg\\\",\\\"idcardNo\\\":\\\"23052319930****231X\\\",\\\"matchRatio\\\":0.99,\\\"body\\\":\\\"...0****231X 董庆彬...\\\",\\\"court\\\":\\\"宝清县人民法院\\\",\\\"title\\\":\\\"董庆彬\\\",\\\"execMoney\\\":9000.0,\\\"caseNo\\\":\\\"（2017）黑0523执367号\\\",\\\"entryId\\\":\\\"c2017230523zhi367_t20170519_pdongqingbin\\\",\\\"sortTimeString\\\":\\\"2017年05月19日\\\",\\\"caseState\\\":\\\"0\\\"}]\"\n"
//				+ "\t\t\t},\n" + "\t\t\t\"IFT\": \"T20105\"\n" + "\t\t}],\n" + "\t\t\"ECL\": []\n" + "\t},\n"
//				+ "\t\"msg\": \"成功\"\n" + "}";
//		Map map = new HashMap();
//		map.put("AA", "111");
//		map.put("BB", "222");
//		// JSONObject response =
//		// JSONUtil.jsonToBean(JSONUtil.beanToJson(json),JSONObject.class);
//		// JSONArray jsonArray = response.getJSONObject(DATA).getJSONArray(RSL);
//		Map map2 = ParserBasic(m41json, "T20105");
//		map.putAll(map2);
//		System.out.println(map);
		List<String> tongDunInerList= Arrays.asList("TONG_DUN_ANTIFRAUD");
		String parameters="{'cid':'13306328903','idNumber':'370404196212262212','realName':'赵玉柏'}";
		Map<String, Object> parmMap = JSONUtil.jsonToMap(parameters);
		if (parmMap.containsKey("idNumber")&&parmMap.containsKey("realName")&&parmMap.containsKey("cid")) {
			parmMap.put("id_number", parmMap.containsKey("idNumber"));
			parmMap.put("account_name", parmMap.containsKey("realName"));
			parmMap.put("account_mobile", parmMap.containsKey("cid"));
			parmMap.remove("idNumber");
			parmMap.remove("realName");
			parmMap.remove("cid");
			ClientTongDun clientTongDun=	new ClientTongDun();
			ClientTongDunResponse response = clientTongDun.invoke(parmMap);
			Map<String, Map> parserBasicTongDun = ResponseParser.ParserBasicTongDun(response, tongDunInerList);
		}else {
            throw new RcsException("同盾数据源参数缺失",RES_ERROR.getCode());

		}
	
	}


}
