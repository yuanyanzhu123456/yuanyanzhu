package com.geo.rcs.modules.event.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.redis.RedisKeys;
import com.geo.rcs.common.redis.RedisUtils;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.modules.engine.entity.*;
import com.geo.rcs.modules.event.dao.EventEntryMapper;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.entity.EventHistoryLog;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.event.vo.EventReport;
import com.geo.rcs.modules.event.vo.EventStatEntry;
import com.geo.rcs.modules.event.vo.HitRule;
import com.geo.rcs.modules.kafka.HadoopConsts;
import com.geo.rcs.modules.kafka.KafkaProducer;
import com.geo.rcs.modules.kafka.ObjToStrLine;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 事件进件
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/1/16 11:40
 */
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventEntryMapper entryMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Value("${spring.kafka.hdfsLogSwitch}")
    private boolean open;
    @Autowired
    private ObjToStrLine objToStrLine;
    @Override
    public EventEntry save(EventEntry entry) {
        entryMapper.insertSelective(entry);
        if (open) {
            String message = objToStrLine.getObjectLogRecord(entry);
            kafkaProducer.sendMessage(HadoopConsts.TOPIC_API_EVENT_ENTRY, message);
        }
        return entry;
    }

    @Override
    public int update(EventEntry entry) {
        return entryMapper.updateByPrimaryKeySelective(entry);
    }

    @Override
    public int delete(Long id) {
        return entryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public EventReport findById(Long id) {
        if (id == null) {
            return null;
        }
        Rules rules = null;
        EventEntry eventEntry = entryMapper.selectByPrimaryKey(id);
        EngineRules engineRules = null;
        Map<String,String> param = null;

        if(eventEntry.getRulesId() != null) {
            engineRules = ruleSetService.selectById(eventEntry.getRulesId());
            String parameters = engineRules.getParameters();
            param = JSONUtil.jsonToMap(parameters);
        }

        try {
            if (eventEntry.getResultMap() == null || eventEntry.getSysStatus() == 5) {
                rules = new Rules();
                rules.setId(eventEntry.getRulesId());
                if (engineRules != null){
                    rules.setName(engineRules.getName());
                    rules.setMatchType(engineRules.getMatchType());
                    rules.setThresholdMax(engineRules.getThresholdMax());
                    rules.setThresholdMin(engineRules.getThresholdMin());
                }

            } else {
                rules = JSON.parseObject(eventEntry.getResultMap(), Rules.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RcsException(StatusCode.API_PARSE_SOURCE_DATA_ERROR.getMessage(),
                    StatusCode.API_PARSE_SOURCE_DATA_ERROR.getCode());
        }


        //获取个人信息：key:中文 value:信息
        Map<String,String> paramInfo = null;
        paramInfo =  getPersonInfo(eventEntry,engineRules,rules);

        List<HitRule> hitRuleList = new ArrayList<>();
        List<Rule> ruleList = rules.getRuleList();
        List<Field> fieldList = new ArrayList<>();
        if (ruleList != null && !ruleList.isEmpty()) {
            for (Rule rule : ruleList) {
                if (rule.getScore() >= 0) {
                    HitRule hitRule = new HitRule();
                    hitRule.setScene(eventEntry.getSenceId().toString());
                    hitRule.setRuleSet(eventEntry.getRulesName());
                    hitRule.setRule(rule.getName());
                    hitRule.setCondition(rule.getConditionRelationShip());
                    String rulePattern = "";
                    if (rules.getMatchType() == 0){
                        rulePattern = "权重匹配";
                    }else if (rules.getMatchType() == 1){
                        rulePattern = "最坏匹配";
                    }else if (rules.getMatchType() == 2){
                        rulePattern = "模型匹配";
                    }
                    hitRule.setRulePattern(rulePattern);
                    hitRule.setRuleWeight(rule.getScore() + "");
                    hitRule.setRecordTime(DateUtils.format(eventEntry.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                    hitRuleList.add(hitRule);
                }
                List<Condition> conditionList = rule.getConditionsList();
                for (Condition condition : conditionList){
                    fieldList.addAll(condition.getFieldList());
                }
            }
        }

        EventReport eventReport = new EventReport();
        eventReport.setName(eventEntry.getUserName());
        eventReport.setGender(null);
        eventReport.setAge(null);
        eventReport.setOccupation(null);
        eventReport.setMobile(eventEntry.getMobile());
        eventReport.setIdCard(eventEntry.getIdCard());
        eventReport.setWorkUnit(null);
        eventReport.setCompany(null);
        eventReport.setHomeAddress(null);
        eventReport.setCompanyAddress(null);
        eventReport.setHitRuleList(hitRuleList);
        eventReport.setRealNameQuery(null);
        eventReport.setNameVerification(null);
        eventReport.setOnlineTime(null);
        eventReport.setNetworkState(null);
        eventReport.setEngineRules(rules);
        eventReport.setAddTime(DateUtils.format(eventEntry.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        eventReport.setParamInfo(paramInfo);
        eventReport.setFieldList(fieldList);

        //获取接口调用返回的所有规则字段
        List<Map<String, Object>> productFiledList = getProductList(rules);
        eventReport.setProductFieldList(productFiledList);

        //对于模型字段中的接口名字和字段中文描述
        if (fieldList != null && fieldList.size() > 0 ) {
            String fieldName = fieldList.get(0).getFieldName();
            List<Map<String, String>> fieldDesc = entryMapper.findInterNameAndFieldDesc(fieldName);
            eventReport.setFieldDesc(fieldDesc.get(0));
        }

        return eventReport;
    }

    @Override
    public Page<EventEntry> findByPage(EventEntry eventEntry) {
        PageHelper.startPage(eventEntry.getPageNo(), eventEntry.getPageSize());
        Page<EventEntry> eventEntries = entryMapper.findByPage(eventEntry);
        return eventEntries;
    }

    @Override
    public EventStatEntry todayEventStat(Map<String, Object> map) {
        List<EventStatEntry> eventStatEntries = entryMapper.findEventStatByParam(map);
        if (eventStatEntries == null || eventStatEntries.isEmpty()) {
            return new EventStatEntry();
        }
        return eventStatEntries.get(0);
    }

    @Override
    public EventStatEntry riskEventStat(Map<String, Object> map) {
        map.put("type", "time");
        List<EventStatEntry> eventStatEntryList = entryMapper.findEventStatByParam(map);

        return assembleData(eventStatEntryList);
    }

    @Override
    public EventStatEntry mapEventStat(Map<String, Object> map) {
        map.put("type", "map");
        List<EventStatEntry> eventStatEntryList = entryMapper.findEventStatByParam(map);

        return assembleData(eventStatEntryList);
    }

    @Override
    public Map<String, String> findAllFiledKV() {
        String key = RedisKeys.EVENT.productFieldKey();

        Map<String, String> productFiled = redisUtils.get(key, Map.class);
        if (productFiled == null || productFiled.isEmpty()) {
            productFiled = new HashMap<>();
            List<Map<String, String>> productFiledMap = entryMapper.findAllFiledKV();
            for (Map<String, String> map : productFiledMap) {
                productFiled.put(map.get("NAME"), map.get("describ"));
            }
            redisUtils.set(key, productFiled, 7 * 24 * 60 * 60);
        }

        return productFiled;
    }

    @Override
    public Map<String, String> findAllFieldKV2() {

        Map<String, String> productField = new HashMap<>();
        List<Map<String, String>> productFiledMap = entryMapper.findAllFiledKV();
        for (Map<String, String> map : productFiledMap) {
            productField.put(map.get("NAME"), map.get("describ"));
         }
        return productField;
    }

    @Override
    public EventStatEntry custEventStat(Map<String, Object> map) {
        List<EventStatEntry> eventStatEntryList = entryMapper.findCustEventStatByParam(map);

        return assembleData(eventStatEntryList);
    }

    @Override
    public List<EventStatEntry> yesterdayEventStat(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        return entryMapper.findYesterdayEventStat(map);
    }

    @Override
    public EventStatEntry thisRuleSetEventStat(Map<String, Object> map) {
        List<EventStatEntry> eventStatEntries = entryMapper.thisRuleSetEventStat(map);
        if (eventStatEntries == null || eventStatEntries.isEmpty()) {
            return new EventStatEntry();
        }
        return eventStatEntries.get(0);
    }

    @Override
    public List<EventEntry> thisRuleSetRecentStat(Map<String, Object> map) {
        List<EventEntry> eventEntries = entryMapper.thisRuleSetRecentStat(map);
        return eventEntries;

    }

    @Override
    public EventEntry getEntryDetail(Long entryId) {
        return  entryMapper.getEntryDetail(entryId);
    }

    @Override
    public List<Map<String, Object>> thisRuleSetEventTrend(Map<String, Object> map) {
        return entryMapper.thisRuleSetEventTrend(map);
    }

    @Override
    public List<String> thisRuleSetResultMap(Map<String, Object> map) {
        return entryMapper.thisRuleSetResultMap(map);
    }

    @Override
    public Page<Map<String, Object>> findByPageIds(Long[] ids) {
        return entryMapper.findByPageIds(ids);
    }

    @Override
    public Page<Map<String, Object>> findByPageAll(EventEntry eventEntry) {
        return entryMapper.findByPageAll(eventEntry);
    }

    @Override
    public void saveLog(Map<String, Object> map) {
        map.put("entryTime", TimeUtil.dqsj());
        entryMapper.saveLog(map);
    }

    @Override
    public Page<EventHistoryLog> findEventLogByPage(EventHistoryLog eventHistoryLog) {
        PageHelper.startPage(eventHistoryLog.getPageNo(), eventHistoryLog.getPageSize());
        return entryMapper.findEventLogByPage(eventHistoryLog);
    }

    @Override
    public EventHistoryLog getHistoryLog(Map<String, Object> map) {
        return entryMapper.getHistoryLog(map);
    }

    @Override
    public void updateLog(Map<String, Object> map) {
        entryMapper.updateLog(map);
    }

    @Override
    public EventHistoryLog getJobInfo(EventHistoryLog eventHistoryLog) {
        return entryMapper.getJobInfo(eventHistoryLog);
    }

    @Override
    public EventEntry saveForDecision(EventEntry entry) {
        entryMapper.saveForDecision(entry);
        if (open) {
            String message = objToStrLine.getObjectLogRecord(entry);
            kafkaProducer.sendMessage(HadoopConsts.TOPIC_API_EVENT_ENTRY, message);
        }
        return entry;
    }

    @Override
    public Map<String, Object> selectBySelective(Map<String, Object> map) {
        EventStatEntry entry = entryMapper.getCountTrend(map);
        map.put("data", entry);
        map.put("updateTime", new Date());
        return map;
    }

    @Override
    public Map<String, Object> getEventCountTrend(Map<String, Object> map) {
        List<Map<String, Object>> countTrend = new ArrayList<>();
        countTrend = entryMapper.getEventCountTrend(map);

        map.clear();
        map.put("countTrend", countTrend);
        return map;
    }

    @Override
    public Map<String, Object> getEventStatusTrend(Map<String, Object> map) {
        List<Map<String, Object>> eventStatusTrend = entryMapper.getEventStatusTrend(map);
        map.clear();
        map.put("eventStatusTrend", eventStatusTrend);
        return map;
    }

    @Override
    public List<Map<String, Object>> getEventCostTrend(Map<String, Object> map) {
        return entryMapper.getEventCostTrend(map);
    }

    @Override
    public List<Map<String, Object>> getEventScoreTrend(Map<String, Object> map) {
        return entryMapper.getEventScoreTrend(map);
    }

    /**
     * 拼装数据
     *
     * @param eventStatEntryList
     * @return
     */
    private EventStatEntry assembleData(List<EventStatEntry> eventStatEntryList) {
        EventStatEntry eventStatEntry = new EventStatEntry();
        int passEventCount = 0, refuseEventCount = 0, manualReviewCount = 0, invalidEventCount = 0, eventTotal = 0;

        if (eventStatEntryList != null || !eventStatEntryList.isEmpty()) {
            for (EventStatEntry entry : eventStatEntryList) {
                passEventCount += entry.getPassEventCount();
                refuseEventCount += entry.getRefuseEventCount();
                manualReviewCount += entry.getManualReviewCount();
                invalidEventCount += entry.getInvalidEventCount();
                eventTotal += entry.getEventTotal();
            }
        }
        eventStatEntry.setPassEventCount(passEventCount);
        eventStatEntry.setRefuseEventCount(refuseEventCount);
        eventStatEntry.setManualReviewCount(manualReviewCount);
        eventStatEntry.setInvalidEventCount(invalidEventCount);
        eventStatEntry.setEventTotal(eventTotal);
        eventStatEntry.setEventStatEntryList(eventStatEntryList);

        return eventStatEntry;
    }

    /**
     * 根据传入参数和规则所要求的输入参数匹配个人信息 （key:中文 value：值）
     * @param eventEntry
     * @param engineRules
     * @param rules
     * @return
     */
    public Map<String,String> getPersonInfo(EventEntry eventEntry,EngineRules engineRules,Rules rules){

        Map<String,String> param = null;
        if(eventEntry.getRulesId() != null) {
            engineRules = ruleSetService.selectById(eventEntry.getRulesId());
            String parameters = engineRules.getParameters();
            param = JSONUtil.jsonToMap(parameters);
        }

        Map<String,String> paramInfo = new HashMap<>();

        if (rules != null){
            Parameters p = rules.getParameters();
            if (p != null) {
                Map<String, String> pJson = JSONUtil.beanToMap(p);
                if (param != null) {
                    for (Map.Entry<String, String> paramEntry : param.entrySet()) {
                        for (Map.Entry<String, String> pEntry : pJson.entrySet()) {
                            if (paramEntry.getKey().equalsIgnoreCase(pEntry.getKey())) {
                                paramInfo.put(paramEntry.getValue(), pEntry.getValue());
                            }
                        }
                    }
                }
            }
        }

       /* //优先排姓名、省份证号、手机号
        Map<String,String> paramInfo_update = new LinkedHashMap<>();
        String priority_1 = "姓名";
        String priority_2 = "身份证号";
        String priority_3 = "手机号";
        if (paramInfo.containsKey(priority_1)){
            paramInfo_update.put(priority_1,paramInfo.get(priority_1));
            paramInfo.remove(priority_1);
        }
        if (paramInfo.containsKey(priority_2)){
            paramInfo_update.put(priority_2,paramInfo.get(priority_2));
            paramInfo.remove(priority_2);
        }
        if (paramInfo.containsKey(priority_3)){
            paramInfo_update.put(priority_3,paramInfo.get(priority_3));
            paramInfo.remove(priority_3);
        }
        paramInfo_update.putAll(paramInfo);*/
        return paramInfo;
    }

    /**
     * 获取数据源返回的所有规则字段
     * @param rules
     * @return
     */
    public  List<Map<String, Object>> getProductList(Rules rules) {

        Map<String, Object> sourceData = null;
        try {
            //兼容旧版本使用beanToMap存储的数据
            sourceData = JSONUtil.beanToMap(rules.getSourceData());
        } catch (Exception e) {
            try {
                sourceData = (Map<String, Object>) JSON.toJSON(rules.getSourceData());
            } catch (Exception ex) {
                throw new RcsException(StatusCode.API_PARSE_SOURCE_DATA_ERROR.getMessage(),
                        StatusCode.API_PARSE_SOURCE_DATA_ERROR.getCode());
            }
        }

        //原方法
        // Map<String, String> productFiledKV = findAllFiledKV();
         Map<String, String> productFiledKV = findAllFieldKV2();
        List<Map<String, Object>> productFiledList = new ArrayList<>();
        if (sourceData != null) {
            for (String key : sourceData.keySet()) {
                Map<String, Object> productFiled = new HashMap<>(2);
                Map<String, Object> paramMap = (Map<String, Object>) sourceData.get(key);
                productFiled.put("interfaceName", paramMap.get("inter"));
                productFiled.put("fieldName", key);
                if (productFiledKV.keySet().contains(key)) {
                    productFiled.put("fieldNameDesc", productFiledKV.get(key));
                    if (paramMap.get("value") != null && !"".equals(paramMap.get("value"))) {
                        productFiled.put("verificationResult", paramMap.get("value"));
                    } else {
                        productFiled.put("verificationResult", "空值");
                    }
                    if (paramMap.get("valueDesc") != null && !"".equals(paramMap.get("valueDesc"))) {
                        productFiled.put("verificationResultDesc", paramMap.get("valueDesc"));
                    } else {
                        productFiled.put("verificationResultDesc", "暂无");
                    }
                }else{
                    productFiled.put("fieldNameDesc", "暂无或已下线");
                    productFiled.put("verificationResult", "暂无");
                    productFiled.put("verificationResultDesc", "暂无");
                }
                productFiledList.add(productFiled);
            }
        }
        return productFiledList;
    }
}
