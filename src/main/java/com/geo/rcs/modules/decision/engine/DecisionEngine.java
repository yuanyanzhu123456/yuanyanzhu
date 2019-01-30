package com.geo.rcs.modules.decision.engine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.log.LogFileName;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.api.annotation.LoginUser;
import com.geo.rcs.modules.api.modules.eventin.controller.ApiEventInController;
import com.geo.rcs.modules.decision.entity.*;
import com.geo.rcs.modules.decision.service.EngineDecisionLogService;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.sys.controller.SysLoginController;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.temporal.ValueRange;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.util
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年08月29日 下午4:12
 */
@Component("decisionEngine")
public class DecisionEngine extends BaseController {

    @Autowired
    private ApiEventInController apiEventInController;

    @Autowired
    private EngineDecisionLogService engineDecisionLogService;

    private static Logger logger = LoggerFactory.getLogger(DecisionEngine.class);

    public synchronized void decisionEngine(@LoginUser SysUser user, Map map, String decisionTree, ExcuteFlowForApi decision, StringBuffer decisionFlow, EngineDecisionLog engineDecisionLog, List<Long> executeFlow, List<ExcuteFlowForApi> decisions, HttpServletRequest httpServletRequest) {

        try {

            boolean result = true;

            if ((int) map.get("count") >= 100) {
                LogUtil.error("递归次数到达100次，决策引擎发生异常!",
                        "url:" + "" + "  json:" + " userId:" + user.getUniqueCode().toString(),
                        "", new Exception());
                return;
            }

            List<Decision> list = JSONUtil.jsonToBean(decisionTree, List.class);

            if (decision.getRulesId() == null) {
                throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
            }
            map.put("rulesId", decision.getRulesId());

            Geo geo = apiEventInController.eventEntryForDecision(map, user);
            //规则引擎返回结果为成功
            if (Integer.parseInt(geo.get("code").toString()) == 2000) {

                EventEntry entryEntity = (EventEntry) geo.get("entryEntity");
                Map<String, Object> dataMap = JSONUtil.beanToMap(geo.get("data"));

                EngineRules engineRules = JSONObject.parseObject(entryEntity.getResultMap(), EngineRules.class);
                if(engineRules.getMatchType() == 2){
                    engineDecisionLog.setScore(Integer.valueOf(engineRules.getRuleList().get(0).getConditionsList().get(0).getFieldList().get(0).getValue()));
                    decision.setScore(Integer.valueOf(engineRules.getRuleList().get(0).getConditionsList().get(0).getFieldList().get(0).getValue()));
                }
                else{
                    engineDecisionLog.setScore((int) dataMap.get("score"));
                    decision.setScore((int) dataMap.get("score"));
                }
                engineDecisionLog.setSysStatus((int) dataMap.get("status"));

                decision.setStatus((int) dataMap.get("status"));

                decision.setEventId(entryEntity.getId());

                decisions.add(decision);
                decisionFlow.append(dataMap);

                engineDecisionLog.setResultMap(decisionTree);

                List<FlowForApi> flow = decision.getFlow();

                for (FlowForApi decision1 : flow) {

                    if ((int) map.get("count") >= 100) {
                        break;
                    }

                    Object type = dataMap.get(decision1.getType());

                    if (!ValidateNull.isNull(decision1.getType())) {
                        switch (decision1.getType().toUpperCase()) {
                            case "STATUS":
                                result = IntOperator(decision1, type);
                                break;
                            case "SCORE":
                                result = IntOperator(decision1, type);
                                break;
                            default:
                                result = StringOperator(decision1, type);
                                break;
                        }
                        if (result == true) {
                            if (!ValidateNull.isNull(decision1.getTo())) {
                                int count = (int) map.get("count");
                                count = count + 1;
                                map.put("count", count);
                                //executeFlow.add(decision1.getRulesId());

                                for (int i = 0; i < list.size(); i++) {
                                    ExcuteFlowForApi decision2 = JSON.parseObject(JSON.toJSONString(list.get(i)), ExcuteFlowForApi.class);
                                    if (decision2.getIndex().toString().equals(decision1.getTo().toString())) {
                                        decision = decision2;
                                        break;
                                    }
                                }
                                decisionEngine(user, map, decisionTree, decision, decisionFlow, engineDecisionLog, executeFlow, decisions, httpServletRequest);
                            }
                        }
                    }

                }

            } else {
                logger.error("执行决策", geo.get("msg").toString(), "系统", "");
                throw new RcsException(geo.get("msg") == null ? "执行规则集失败":geo.get("msg").toString(),Integer.valueOf(geo.get("code").toString()));
            }
        } catch (Exception e) {
            logger.error("执行决策", "执行错误", "系统", e);
            e.printStackTrace();
            // TODO Auto-generated catch block
            throw new RcsException(e.getMessage());
        }
    }

    public synchronized DecisionForApi recursive(@LoginUser SysUser user, HttpServletRequest request, EngineDecision engineDecision, Map map) {

        try {

            List<ExcuteFlowForApi> decisions = new ArrayList<>();

            long start = System.currentTimeMillis();

            int count = 0;

            List<ExcuteFlowForApi> list = JSONUtil.jsonToBean(engineDecision.getDecisionFlow(), List.class);

            ExcuteFlowForApi decision = JSON.parseObject(JSON.toJSONString(list.get(0)), ExcuteFlowForApi.class);

            ArrayList<Long> executeFlow = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {

                ExcuteFlowForApi decision1 = JSON.parseObject(JSON.toJSONString(list.get(i)), ExcuteFlowForApi.class);
                if (decision1.getIndex().toString().equals(decision.getTo().toString())) {
                    decision = decision1;
                    break;
                }
            }
            if (decision.getRulesId() == null) {
                throw new RcsException(StatusCode.RULES_NOTFOUND_ERROR.getMessage(), StatusCode.RULES_NOTFOUND_ERROR.getCode());
            } else {
                executeFlow.add(decision.getRulesId());
                map.put("count", count);
                StringBuffer decisionFlow = new StringBuffer();
                EngineDecisionLog engineDecisionLog = new EngineDecisionLog();
                decisionEngine(user, map, engineDecision.getDecisionFlow(), decision, decisionFlow, engineDecisionLog, executeFlow, decisions, request);
                engineDecisionLog.setCreateTime(new Date());

                //决策引擎执行总时长
                long times = System.currentTimeMillis() - start;
                engineDecisionLog.setExpendTime((int) times);
                if (map.get("channel") == null || map.get("channel").toString().equals("")) {
                    engineDecisionLog.setChannel("api");
                } else {
                    engineDecisionLog.setChannel(map.get("channel").toString());
                }
                engineDecisionLog.setUserId(user.getUniqueCode());
                engineDecisionLog.setAddUser(user.getName());
                engineDecisionLog.setBusinessId(map.get("businessId") == null ? 0 : Integer.valueOf(map.get("businessId").toString()));
                engineDecisionLog.setDecisionId(map.get("decisionId") == null ? 0 : Integer.valueOf(map.get("decisionId").toString()));
                engineDecisionLog.setSceneId(map.get("senceId") == null ? 0 : Integer.valueOf(map.get("senceId").toString()));
                engineDecisionLog.setRealName(map.get("realName") == null ? null : map.get("realName").toString());
                engineDecisionLog.setMobile(map.get("cid") == null ? null : map.get("cid").toString());
                engineDecisionLog.setResultMap(engineDecision.getDecisionFlow());
                engineDecisionLog.setIdCard(map.get("idNumber") == null ? null : map.get("idNumber").toString());
                engineDecisionLog.setExcuteFlow(JSONObject.toJSONString(decisions));
                engineDecisionLog.setRemark(map.get("remark") == null ? null : map.get("remark").toString());
                DecisionForApi decisionForApi = parseForApi(engineDecisionLog, engineDecision, map);
                engineDecisionLog.setParameters(JSONUtil.beanToJson(decisionForApi.getParameters()));
                EngineDecisionLog engineDecisionLog1 = engineDecisionLogService.save(engineDecisionLog);
                UserDecision userDecision = new UserDecision();
                userDecision.setDecisionId(Integer.valueOf(map.get("decisionId").toString()));
                userDecision.setUserId(user.getUniqueCode());
                userDecision.setLogId(engineDecisionLog1.getId());
                engineDecisionLogService.saveToUserModel(userDecision);
                engineDecision.setDecisions(decisions);
                decisionForApi.setExcuteFlow(decisions);
                decisionForApi.setEventId(engineDecisionLog1.getId());
                decisionForApi.setSysStatus(engineDecisionLog.getSysStatus());
                return decisionForApi;
            }
        } catch (RcsException e) {
            e.printStackTrace();
            logger.error("执行决策", "执行错误", "系统", e);
            throw new RcsException(e.getMsg(), e.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("决策引擎", "执行决策", "系统", e);
            throw new RcsException("决策引擎异常");
        }
    }

    public static boolean IntOperator(FlowForApi decision, Object type) throws Exception {

        boolean result = true;
        switch (decision.getOperator()) {
            case "isnull":
                result = ValidateNull.isNull(decision.getValue());
                break;
            case "notnull":
                result = !ValidateNull.isNull(decision.getValue());
                break;
            case "==":
                result = decision.getValue() == (int) type;
                break;
            case "!=":
                result = decision.getValue() != (int) type;
                break;
            case ">":
                result = decision.getValue() > (int) type;
                break;
            case "<":
                result = decision.getValue() < (int) type;
                break;
            case ">=":
                result = decision.getValue() >= (int) type;
                break;
            case "<=":
                result = decision.getValue() <= (int) type;
                break;
        }

        return result;
    }

    public static boolean StringOperator(FlowForApi decision, Object type) throws Exception {

        boolean result = true;

        switch (decision.getOperator()) {
            case "isnull":
                result = ValidateNull.isNull(decision.getValue());
                break;
            case "notnull":
                result = !ValidateNull.isNull(decision.getValue());
                break;
            case "==":
                result = decision.getValue().equals(type.toString());
                break;
            case "!=":
                result = !decision.getValue().equals(type.toString());
                break;
        }

        return result;
    }

    public DecisionForApi parseForApi(EngineDecisionLog engineDecisionLog, EngineDecision engineDecision, Map<String, Object> map) throws Exception {

        DecisionForApi decisionForApi = new DecisionForApi();

        Map<String, Object> hashMap = new HashMap<>();
        Map<String, Object> parametersMap = JSONUtil.jsonToMap(engineDecision.getParameters());
        for (String s : map.keySet()) {
            if (parametersMap.containsKey(s)) {
                hashMap.put(s, map.get(s));
            }
        }

        decisionForApi.setParameters(JSONUtil.beanToJson(hashMap));
        decisionForApi.setId(engineDecision.getId());
        decisionForApi.setName(engineDecision.getName());
        decisionForApi.setChannel(engineDecisionLog.getChannel());
        decisionForApi.setBusinessId(engineDecision.getBusinessId());
        decisionForApi.setAppStatus(engineDecisionLog.getAppStatus());
        decisionForApi.setScore(engineDecisionLog.getScore());
        decisionForApi.setActiveStatus(engineDecision.getActiveStatus());
        decisionForApi.setSceneId(engineDecision.getSceneId());
        decisionForApi.setUsedRulesIds(engineDecision.getUsedRulesIds());
        decisionForApi.setCreater(engineDecision.getCreater());
        decisionForApi.setDecisionFlow(JSONObject.parse(engineDecision.getDecisionFlow()));
        decisionForApi.setCreateTime(engineDecision.getCreateTime());
        decisionForApi.setUpdateTime(engineDecision.getUpdateTime());

        return decisionForApi;
    }


}
