package com.geo.rcs.modules.api.modules.eventin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.modules.api.annotation.LoginUser;
import com.geo.rcs.modules.api.modules.user.entity.ApiUserToken;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.engine.service.EngineService;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.kafka.ObjToStrLine;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.source.service.SourceService;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.modules.eventin.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年05月15日 上午11:59

 */

@RestController
@RequestMapping("/api/eventin")
public class ApiEventInController extends BaseController {

    // 数据源地址
    @Value("${dataSourceServer.rcsDataSource.url}")
    private String dataSourceServerUrl;

    @Autowired
    private EventService eventService;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private EngineService engineService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private ObjToStrLine objToStrLine;

    private final Logger infoLogger = LogUtil.logger(LogFileName.API_LOG);

    private final Logger errorLogger = LogUtil.logger(LogFileName.API_ERROR_LOG);

    private static Logger logger = LogUtil.logger(LogFileName.API_LOG);

    private static final String RULES_ID = "rulesId";

    private final String KEY_TYPE = "type";

    private final String KEY_CHANNEL = "channel";

    private final String KEY_REMARK = "remark";

    @SysLog("进件接口调用（规则校验）")
    @PostMapping("/entry")
    public Geo eventEntry(@RequestBody Map<String, Object> map, @LoginUser SysUser user) {

        try {
            // 进件初始化参数
            Geo geo = new Geo();
            int sourceTime = 0;
            int engineTime = 0;
            Long rulesId;
            long st = System.currentTimeMillis();
            EngineRules rules = new EngineRules();
            Map<String, Object> rulesMap = new Hashtable<>();

            logger.info(LogUtil.infoMessage("规则进件", map.toString(), user.getName(),TimeUtil.dqsj(),"开始进件"));

            if (map.isEmpty() || map.get(RULES_ID)==null || !ValidatorUtils.isNumeric(map.get(RULES_ID))) {
                logger.info(LogUtil.errorMessage("规则进件", map.toString(), user.getName(), TimeUtil.dqsj(), "进件失败", "参数错误"));
                return Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage());
            }

            try{
                // 验证进件类型
                rulesId = NumberUtils.toLong(map.get(RULES_ID).toString());
                System.out.println("调试使用=======检查点一 "+(System.currentTimeMillis()-st));

                //第一步 获取rules
                if (map.get(KEY_TYPE)!=null && Integer.valueOf(map.get(KEY_TYPE).toString()) == 1) {
                    rules = ruleSetService.findAllByIdForTest(rulesId);
                } else {
                    rules = ruleSetService.findAllById(rulesId,true);
                }

                System.out.println("调试使用=======检查点二 "+(System.currentTimeMillis()-st));
                checkRules(rules,user);

                // 检查进件参数
                checkParamMap(map, rules.getParameters());

                rules.setParameters(JSONUtil.beanToJson(map));
                String executeRulesConfig = JSONUtil.beanToJson(rules);

                // 调用数据源服务模块-内置服务
                System.out.println("调试使用=======检查点三 "+(System.currentTimeMillis()-st));
                long st2 = System.currentTimeMillis();
                String executeRulesConfigData = sourceService.getFieldRes(executeRulesConfig, user.getUniqueCode());

                sourceTime = ((int) (System.currentTimeMillis() - st2));

                // 请求数据源服务结束
                String rulesJson = engineService.getRulesRes(executeRulesConfigData);
                engineTime = (int) (System.currentTimeMillis() - st2 - sourceTime);
                rulesMap = JSONUtil.jsonToMap(rulesJson);

                // 成功日志
                EventEntry entry = getEventEntry(rules, user, map, rulesMap, geo);
                entry.setSourceTime(sourceTime);
                entry.setEngineTime(engineTime);
                entry.setExpendTime((int) (System.currentTimeMillis() - st));

                EventEntry eventEntry = eventService.save(entry);

                // 输出文件及第三方日志
                String message = objToStrLine.getObjectLogRecord(entry);
                infoLogger.info(message);

                rulesMap.put("eventId", eventEntry.getId());
                geo = Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", rulesMap);
                logger.info(LogUtil.infoMessage("规则进件", rulesMap.toString(), user.getName(),TimeUtil.dqsj(),"进件成功"));

            }catch (RcsException e) {
                e.printStackTrace();
                geo = Geo.error(e.getCode(), e.getMsg());
                // 失败日志
                EventEntry entry = getEventEntry(rules, user, map, rulesMap, geo);
                entry.setSourceTime(sourceTime);
                entry.setEngineTime(engineTime);
                entry.setExpendTime((int) (System.currentTimeMillis() - st));
                entry.setElseTime((int) (System.currentTimeMillis() - st)-sourceTime-engineTime);

                EventEntry save = eventService.save(entry);
                geo = geo.put("eventId",save.getId());
                logger.info(LogUtil.errorMessage("规则进件", rulesMap.toString(), user.getName(),TimeUtil.dqsj(),"进件失败", e.toString()));
            }
            return geo;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(LogUtil.errorMessage("规则进件", map.toString(), user.getName(),TimeUtil.dqsj(),"进件失败，未知异常", e.toString()));

            // 输出到文件及第三方日志
            LogUtil.error("规则进件", map.toString(), user.getName(),"进件失败，未知异常", e);
            return Geo.error();

        }
    }

    @SuppressWarnings("unchecked")
    private EventEntry getEventEntry(EngineRules rules, SysUser user, Map<String, Object> paramsMap, Map<String, Object> resultMap, Geo geo) {
        EventEntry entry = new EventEntry();

        //获取API访问渠道（系统）
        //String channel = DeviceUtils.getDeviceSystem(HttpContextUtils.getHttpServletRequest());

        //设置进件信息
        if (rules != null) {
            entry.setBusinessId(rules.getBusinessId() ==null ? null :rules.getBusinessId().longValue());
            entry.setSenceId(rules.getSenceId());
            entry.setRulesId(rules.getId());
            entry.setRulesName(rules.getName());
            entry.setUserId(user.getUniqueCode());
            entry.setNickname(user.getCompany());
        }
        if (paramsMap != null && !paramsMap.isEmpty()) {
            entry.setUserName((String) paramsMap.get("realName"));
            entry.setMobile((String) paramsMap.get("cid"));
            entry.setIdCard((String) paramsMap.get("idNumber"));
            if (paramsMap.get(KEY_REMARK) != null) {
                entry.setRemark(paramsMap.get(KEY_REMARK).toString());
            }
            if (paramsMap.get(KEY_CHANNEL) == null || "".equals(paramsMap.get(KEY_CHANNEL).toString())) {
                entry.setChannel("api");
            } else {
                entry.setChannel(paramsMap.get(KEY_CHANNEL).toString());
            }
        }
        if (resultMap != null && !resultMap.isEmpty()) {
            int status = (int) resultMap.get("status");
            int score = (int) resultMap.get("score");
            entry.setSysStatus(status);
            entry.setScore(score);
            if (status != RulesEngineCode.HUMAN.getCode()) {
                entry.setApproverName("系统");
            }
            Map<String, Object> sourceData;
            try {
                //兼容旧版本beanToMap存储的数据
                sourceData = JSONUtil.beanToMap(resultMap.get("sourceData"));
            } catch (Exception e) {
                assert rules != null;
                String info = MessageFormat.format("{0}:{1},{2}", rules.getId(), rules.getName(), rules.getParameters());
                LogUtil.error("进件源数据解析-方式一", info, user.getName(), e);
                infoLogger.info("进件源数据解析-使用方式二", info, user.getName(), TimeUtil.dqsj(), "启动解析");

                try {
                    sourceData = (Map<String, Object>) JSON.toJSON(resultMap.get("sourceData"));

                } catch (Exception ex) {
                    LogUtil.error("进件源数据解析-方式二", info, user.getName(), ex);
                    throw new RcsException(StatusCode.API_PARSE_SOURCE_DATA_ERROR.getMessage(),
                            StatusCode.API_PARSE_SOURCE_DATA_ERROR.getCode());
                }

            }

            Map<String, Object> province = JSONUtil.beanToMap(sourceData.get("province"));
            Map<String, Object> city = JSONUtil.beanToMap(sourceData.get("city"));
            Map<String, Object> isp = JSONUtil.beanToMap(sourceData.get("isp"));
            entry.setProvince(province == null ? "" : province.get("value").toString());
            entry.setCity(city == null ? "" : city.get("value").toString());
            entry.setIsp(isp == null ? "" : isp.get("value").toString());
            entry.setResultMap(JSONObject.toJSONString(resultMap));
        } else {
            entry.setApproverName("系统");
            entry.setSysStatus(RulesEngineCode.FAIL.getCode());
            entry.setResultMap(JSONObject.toJSONString(geo));
            entry.setUserId(user.getUniqueCode());
        }

        entry.setSysApprovalTime(new Date());
        entry.setCreateTime(new Date());

        return entry;
    }


    @SysLog("决策引擎专用进件接口（规则校验）")
    @PostMapping("/entryForDecision")
    public Geo eventEntryForDecision(@RequestBody Map<String, Object> map, @LoginUser SysUser user) {
        Geo geo = new Geo();
        int sourceTime = 0;
        int engineTime = 0;
        long st = System.currentTimeMillis();
        EngineRules rules = null;
        Map<String, Object> rulesMap = new HashMap<>();
        Long rulesId;
        logger.info(LogUtil.infoMessage("规则进件", map.toString(), user.getName(),TimeUtil.dqsj(),"开始进件"));

        if (map.isEmpty() || map.get(RULES_ID)==null || !ValidatorUtils.isNumeric(map.get(RULES_ID))) {
            logger.info(LogUtil.errorMessage("规则进件", map.toString(), user.getName(), TimeUtil.dqsj(), "进件失败", "参数错误"));
            return Geo.error(StatusCode.PARAMS_ERROR.getCode(), StatusCode.PARAMS_ERROR.getMessage());
        }
        try {
            rulesId = NumberUtils.toLong(map.get(RULES_ID).toString());

            if (map.get(KEY_TYPE)!=null && Integer.valueOf(map.get(KEY_TYPE).toString()) == 1) {
                rules = ruleSetService.findAllByIdForTest(rulesId);
            } else {
                rules = ruleSetService.findAllById(rulesId,true);
            }


            //检查进件参数
            checkParamMap(map, rules.getParameters());

            rules.setParameters(JSONUtil.beanToJson(map));
            String json = JSONUtil.beanToJson(rules);

            try {  // 调用数据源服务模块-内置服务
                String fieldJson = sourceService.getFieldRes(json, user.getUniqueCode());
                sourceTime = ((int) (System.currentTimeMillis() - st));

                // 请求数据源服务结束
                String rulesJson = engineService.getRulesRes(fieldJson);
                engineTime = (int) (System.currentTimeMillis() - st - sourceTime);
                rulesMap = JSONUtil.jsonToMap(rulesJson);
                geo =  Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", rulesMap);
            } catch (RcsException e) {
                // TODO Auto-generated catch block
                LogUtil.error("获取数据源服务数据失败!",
                        "url:" + dataSourceServerUrl + "  json:" + json + " userId:" + user.getUniqueCode().toString(),
                        "", e);
                e.printStackTrace();
                geo =  Geo.error(e.getCode(), e.getMsg());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                LogUtil.error("获取数据源服务数据失败!",
                        "url:" + dataSourceServerUrl + "  json:" + json + " userId:" + user.getUniqueCode().toString(),
                        "", e);
                e.printStackTrace();
                geo =  Geo.error("数据源服务返回数据异常!");
            }
        } catch (RcsException e) {
            geo = Geo.error(e.getCode(), e.getMsg());
            errorLogger.error("决策引擎调用规则API!",
                    "url:" + dataSourceServerUrl + "  json:" + "" + " userId:" + user.getUniqueCode().toString(),
                    "", e);
        } catch (Exception e) {
            e.printStackTrace();
            errorLogger.error("决策引擎调用规则API!",
                    "url:" + dataSourceServerUrl + "  json:" + "" + " userId:" + user.getUniqueCode().toString(),
                    "", e);
            geo = Geo.error("决策引擎调用规则API失败！");
        } finally {
            EventEntry entry = getEventEntry(rules, user, map, rulesMap, geo);
            entry.setSourceTime(sourceTime);
            entry.setEngineTime(engineTime);
            if (map.get(KEY_REMARK) != null) {
                entry.setRemark(map.get(KEY_REMARK).toString());
            }
            entry.setExpendTime((int) (System.currentTimeMillis() - st));
            if (map.get(KEY_CHANNEL) == null || "".equals(map.get(KEY_CHANNEL).toString())) {
                entry.setChannel("api");
            } else {
                entry.setChannel(map.get(KEY_CHANNEL).toString());
            }
            EventEntry save = eventService.save(entry);
            if (rules != null) {
                String message = objToStrLine.getObjectLogRecord(entry);
                infoLogger.info(message);
            } else {
                errorLogger.error(LogUtil.operation("进件", "不完整规则集", user.getName(), TimeUtil.dqsj(), "进件失败") + ":: [耗时]:" + (System.currentTimeMillis() - st));
            }
            if(geo!=null) geo.put("entryEntity", save);
        }
        return geo;

    }

    /**
     * 参数检查
     * @param map Map<String, Object>
     * @param apiUserToken ApiUserToken
     * @param sysUser SysUser
     * @param rulesId Long
     * */

    public void checkParameters(Map<String, Object> map, ApiUserToken apiUserToken, SysUser sysUser, Long rulesId) {
        if (map == null || map.isEmpty()) {
            throw new RcsException("进件参数不能为空", StatusCode.PARAMS_ERROR.getCode());
        } else if (map.get(KEY_TYPE) == null) {
            throw new RcsException("请选择进件类型", StatusCode.ERROR.getCode());
        } else if (apiUserToken == null || sysUser == null) {
            throw new RcsException("登录凭证错误", StatusCode.ERROR.getCode());
        } else if (rulesId == null) {
            throw new RcsException("规则集ID错误", StatusCode.PARAMS_ERROR.getCode());
        }
    }

    private void checkRules(EngineRules engineRules, SysUser sysUser) {
        if (engineRules == null || !engineRules.getUniqueCode().equals(sysUser.getUniqueCode())) {
            throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
        }
    }

    private void checkParamMap(Map<String, Object> map, String parametersMap) {
        int paramCount = 0;
        Hashtable rulesParams = JSON.parseObject(parametersMap, Hashtable.class);
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if (rulesParams.keySet().contains(key)) {
                System.out.println("" + key);
                paramCount += 1;
            }
        }
        if (paramCount != rulesParams.size()) {
            throw new RcsException("参数不齐全！", StatusCode.PARAMS_ERROR.getCode());
        }
    }

}
