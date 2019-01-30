package com.geo.rcs.modules.rule.ruleset.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.event.entity.EventEntry;
import com.geo.rcs.modules.event.service.EventService;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.geo.rcs.modules.rule.ruleset.service.RuleSetService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.geo.rcs.modules.sys.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.ruleset.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年06月26日 下午3:06
 */
@RestController
@RequestMapping("/ruleSetAnalysis")
public class RuleSetAnalysisController extends BaseController{

    @Autowired
    private RuleSetService ruleSetService;
    @Autowired
    private EventService eventService;

    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";


    /**
     * 近期审核状态
     * @param map
     * @return
     */
    @RequestMapping("/auditTrend")
    @RequiresPermissions("rule:ruleset:analysis")
    public Geo auditTrend(@RequestBody Map<String, Object> map){

        map.put("userId", getUser().getUniqueCode());
        return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",eventService.thisRuleSetEventTrend(map));
    }
    /**
     * 近期分数统计
     * @param map
     * @return
     */
    @RequestMapping("/scoreTrend")
    @RequiresPermissions("rule:ruleset:analysis")
    public Geo scoreTrend(@RequestBody Map<String, Object> map){

        Map scoreMap = new HashMap<>();

        Integer thresholdMin = 0;

        Integer thresholdMax = 0;

        map.put("userId", getUser().getUniqueCode());

        ArrayList<Integer> scores = new ArrayList<>();

        List<String> resultMap =  eventService.thisRuleSetResultMap(map);

        if(resultMap == null){
            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",resultMap);
        }

        for (int i =0;i<resultMap.size();i++) {

            EngineRules engineRules = JSON.parseObject(resultMap.get(i) ,EngineRules.class);
            if(engineRules != null){

                scores.add(engineRules.getScore());

                thresholdMin = engineRules.getThresholdMin();

                thresholdMax = engineRules.getThresholdMax();
            }
        }
        scoreMap.put("scores",scores);
        scoreMap.put("thresholdMin",thresholdMin);
        scoreMap.put("thresholdMax",thresholdMax);
        return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",scoreMap);
    }
    /**
     * 近期规则命中统计
     * @param map
     * @return
     */
    @RequestMapping("/ruleTrend")
    @RequiresPermissions("rule:ruleset:analysis")
    public Geo ruleTrend(@RequestBody Map<String, Object> map){

        Map ruleMap = new HashMap<>();

        Integer num = 0;

        map.put("userId", getUser().getUniqueCode());

        ArrayList<Integer> scores = new ArrayList<>();

        List<String> resultMap =  eventService.thisRuleSetResultMap(map);

        if(resultMap.size() <= 0){
            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",resultMap);
        }

        for (int i =0;i<resultMap.size();i++) {

            EngineRules engineRules = JSON.parseObject(resultMap.get(i) ,EngineRules.class);
            if(engineRules != null){
                    for (int j = 0;j<engineRules.getRuleList().size();j++) {
                        if(engineRules.getRuleList().get(j).getScore() != 0){
                            if(ruleMap.containsKey(engineRules.getRuleList().get(j).getName())){
                                num  = (int)ruleMap.get(engineRules.getRuleList().get(j).getName())+1;

                                ruleMap.put(engineRules.getRuleList().get(j).getName(),num);
                            }
                            else{
                                num = num + 1;
                                ruleMap.put(engineRules.getRuleList().get(j).getName(),num);
                            }
                        }else{
                            ruleMap.put(engineRules.getRuleList().get(j).getName(),0);
                        }
                    }
            }
        }
        return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",ruleMap);
    }

    /**
     * 单条规则集近期事件统计
     * @param eventEntry
     * @return
     */
    @PostMapping("/thisRuleSetRecentStat")
    @RequiresPermissions("rule:ruleset:analysis")
    public Geo thisRuleSetRecentStat(@RequestBody EventEntry eventEntry){
        //获取当前登录用户
        try {
            SysUser user = getUser();
            if(user == null) {
                return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
            }

            String startTime = eventEntry.getStartTime();
            String endTime = eventEntry.getEndTime();
            if(StringUtils.stripToNull(startTime) == null || StringUtils.stripToNull(endTime) == null){
                return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "日期不能为空");
            }
            if(!DateUtils.compareTo(startTime, endTime)) {
                return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "结束时间不能大于开始时间");
            }

            eventEntry.setUserId(getUser().getUniqueCode());
            PageInfo<EventEntry> eventEntryPageInfo = new PageInfo<>(eventService.findByPage(eventEntry));

            if(eventEntryPageInfo.getList().size() == 0){
                return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",eventEntryPageInfo);
            }
            List<EventEntry> list = eventEntryPageInfo.getList();
            for (EventEntry eventEntry1 : list) {
                if(eventEntry1.getResultMap() != null && eventEntry1.getSysStatus() != 5){
                    Map<String, Object> stringObjectMap = JSONUtil.jsonToMap(eventEntry1.getResultMap());
                    eventEntry1.setScore(Integer.parseInt(stringObjectMap.get("score").toString()));
                }
            }
            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data",eventEntryPageInfo);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error("单条规则集近期事件统计", eventEntry.toString(), getUser().getName(), e);
            return Geo.error("暂无此规则集分析情况");
        }
    }
}
