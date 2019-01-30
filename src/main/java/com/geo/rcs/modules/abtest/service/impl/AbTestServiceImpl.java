package com.geo.rcs.modules.abtest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.schedule.AbScheduleUtils;
import com.geo.rcs.common.util.TimeUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.modules.abtest.dao.AbTestMapper;
import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.abtest.service.AbTestService;
import com.geo.rcs.modules.api.modules.decision.DecisionEngineApi;
import com.geo.rcs.modules.api.modules.eventin.controller.ApiEventInController;
import com.geo.rcs.modules.monitor.dao.ScheduleTaskMapper;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年10月25日 下午4:47
 */
@Service("abTestService")
public class AbTestServiceImpl implements AbTestService {
    @Resource
    private Scheduler scheduler;
    @Autowired
    private AbTestMapper abTestMapper;
    @Autowired
    private ScheduleTaskMapper scheduleTaskMapper;
    @Autowired
    private DecisionEngineApi decisionEngineApi;
    @Autowired
    private ApiEventInController apiEventInController;

    @Override
    public Page<AbTest> findByPage(Map<String,Object> map) {
        PageHelper.startPage(Integer.valueOf(map.get("pageNo").toString()), Integer.valueOf(map.get("pageSize").toString()));
        Page<AbTest> byPage = abTestMapper.findByPage(map);
        return byPage;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AbTest saveAbJob(AbTest abTest) {
        if(abTest.getEntryType() == 0 && abTest.getRuleType() == 0){
            /*abTestMapper.saveAbJob(abTest);
            ArrayList<AbScheduleTask> scheduleTasks = new ArrayList<>();
            AbScheduleTask abScheduleTask = new AbScheduleTask();
            abScheduleTask.setGoalA(abTest.getGoalA() == null ? 0 : abTest.getGoalA());
            abScheduleTask.setGoalB(abTest.getGoalB() == null ? 0 : abTest.getGoalB());
            abScheduleTask.setUserId(sysUser.getUniqueCode());
            abScheduleTask.setName(JSONUtil.jsonToMap(abTest.getParameters()).get("realName").toString());
            abScheduleTask.setIdNumber(JSONUtil.jsonToMap(abTest.getParameters()).get("idNumber").toString());
            abScheduleTask.setCid(JSONUtil.jsonToMap(abTest.getParameters()).get("cid").toString());
            saveTaskBatch(scheduleTasks);
            Geo geo = apiEventInController.eventEntry(JSONUtil.beanToMap(abTest), sysUser, request);*/
        }else if(abTest.getEntryType() == 0 && abTest.getRuleType() == 1){
            /*abTestMapper.saveAbJob(abTest);
            ArrayList<AbScheduleTask> scheduleTasks = new ArrayList<>();
            AbScheduleTask abScheduleTask = new AbScheduleTask();
            abScheduleTask.setGoalA(abTest.getGoalA() == null ? 0 : abTest.getGoalA());
            abScheduleTask.setGoalB(abTest.getGoalB() == null ? 0 : abTest.getGoalB());
            abScheduleTask.setUserId(sysUser.getUniqueCode());
            abScheduleTask.setName(JSONUtil.jsonToMap(abTest.getParameters()).get("realName").toString());
            abScheduleTask.setIdNumber(JSONUtil.jsonToMap(abTest.getParameters()).get("idNumber").toString());
            abScheduleTask.setCid(JSONUtil.jsonToMap(abTest.getParameters()).get("cid").toString());
            saveTaskBatch(scheduleTasks);
            Geo geo = decisionEngineApi.eventIn(JSONUtil.beanToMap(abTest), request, response, sysUser);
            DecisionForApi recursive = (DecisionForApi )geo.get("recursive");*/
        }else {
            abTest.setCreateTime(TimeUtil.dqsj());
            abTest.setBeanName("scanningAbTestTask");
            abTest.setMethodName("scanNing");
            abTest.setCronExpression("*/5 * * * * ?");
            abTest.setJobStatus(Constant.ScheduleStatus.PAUSE.getValue());
            abTestMapper.saveAbJob(abTest);
            AbScheduleUtils.createAbTestJob(scheduler, abTest);
            AbScheduleUtils.resumeJob(scheduler, abTest.getJobId());
        }
        return abTest;
    }

    @Override
    public void saveTaskBatch(List<AbScheduleTask> scheduleTasks) {
        String tableName = null;
        AbTest abTest = new AbTest();
        for (AbScheduleTask scheduleTask : scheduleTasks) {
            Long userId = scheduleTask.getUserId();
            tableName = "ab_task_"+userId/10+"_part";
            scheduleTask.setCreateTime(new Date());
            abTest.setJobId(scheduleTask.getJobId());
        }
        queryTableIFExists(tableName);
        scheduleTaskMapper.saveAbTaskBatch(scheduleTasks,tableName);
        abTest.setTaskCount(scheduleTasks.size());
        abTestMapper.updateAbJob(abTest);
    }
    @Override
    public void queryTableIFExists(String tableName) {
        String s = scheduleTaskMapper.queryTableIFExists(tableName);
        if(ValidateNull.isNull(s)){
            scheduleTaskMapper.createAbTaskTable(tableName);
        }
    }

    @Override
    public AbTest getAbTestJobByPrimaryKey(Long jobId) {
        return abTestMapper.getAbTestJobByPrimaryKey(jobId);
    }

    @Override
    public List<AbScheduleTask> queryByFilter(HashMap<String, Object> map) {
        String tableName = "ab_task_"+(Long)map.get("uniqueCode")/10+"_part";
        queryTableIFExists(tableName);
        map.put("tableName",tableName);
        return abTestMapper.queryByFilter(map);
    }

    @Override
    public void updateDistributeStatusToRunning(List<AbScheduleTask> scheduleTasks, Long uniqueCode) {
        ArrayList<Object> ids = new ArrayList<>();
        for (AbScheduleTask scheduleTask : scheduleTasks) {
            ids.add(scheduleTask.getId());
        }
        String tableName = "ab_task_"+uniqueCode/10+"_part";
        queryTableIFExists(tableName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("ids",ids);
        map.put("tableName",tableName);
        map.put("taskStatus",Constant.DistributeStatus.RUNNING.getValue());
        map.put("distributeStatus", Constant.DistributeStatus.RUNNING.getValue());
        scheduleTaskMapper.updateBatch(map);
    }

    @Override
    public void updateDistributeStatusToInit(List<AbScheduleTask> excessScheduleTask, Long uniqueCode) {
        ArrayList<Object> ids = new ArrayList<>();
        for (AbScheduleTask scheduleTask : excessScheduleTask) {
            ids.add(scheduleTask.getId());
        }
        String tableName = "ab_task_"+uniqueCode/10+"_part";
        queryTableIFExists(tableName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("ids",ids);
        map.put("tableName",tableName);
        map.put("taskStatus",Constant.DistributeStatus.RUNNING.getValue());
        map.put("distributeStatus", Constant.DistributeStatus.INIT.getValue());
        scheduleTaskMapper.updateBatch(map);
    }

    @Override
    public void deleteBatch(Long jobId) {
        AbTest abTest = new AbTest();
        abTest.setJobStatus(2);
        abTest.setJobId(jobId);
        abTestMapper.updateAbJob(abTest);
        AbScheduleUtils.deleteScheduleJob(scheduler, jobId);
    }

    @Override
    public Page<AbScheduleTask> getTaskResultList(Map<String, Object> map) {
        String tableName = "ab_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
        queryTableIFExists(tableName);
        PageHelper.startPage((int)map.get("pageNo"), (int)map.get("pageSize"));
        map.put("tableName",tableName);
        Page<AbScheduleTask> scheduleTasks = scheduleTaskMapper.findAbTaskByPage(map);
        return scheduleTasks;
    }

    @Override
    public List<Map<String,Object>> getEventStat(Map<String, Object> map) {
        String tableName = "ab_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
        queryTableIFExists(tableName);
        map.put("tableName",tableName);
        AbTest abTest = abTestMapper.getAbTestJobByPrimaryKey(Long.valueOf(map.get("jobId").toString()));
        List<Map<String,Object>> arrayList = new ArrayList();
        if(abTest.getGoalA() != null){
            map.put("goalId",abTest.getGoalA());
            Map<String, Object> aEventStat = abTestMapper.getEventStat(map);
            arrayList.add(aEventStat);
        }
        if(abTest.getGoalB() != null){
            map.put("goalId",abTest.getGoalB());
            Map<String, Object> bEventStat = abTestMapper.getEventStat(map);
            arrayList.add(bEventStat);
        }
        return arrayList;
    }

    @Override
    public Map<String, Object> getTaskCountAndCompletedCount(Map<String, Object> map) {
        String tableName = "ab_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
        queryTableIFExists(tableName);
        map.put("tableName",tableName);
        return abTestMapper.getTaskCountAndCompletedCount(map);
    }


    @Override
    @Transactional
    public Boolean msgCallbBack(ScheduleTask task) {
        String tableName = "ab_task_" + task.getUserId() / 10 + "_part";
        queryTableIFExists(tableName);
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> ids = new ArrayList<>();
        ids.add(task.getId());
        map.put("ids", ids);
        map.put("tableName", tableName);
        map.put("resultMap", task.getResultMap());
        map.put("updateTime", new Date());
        map.put("executeStatus", task.getExecuteStatus());
        map.put("id", task.getJobId());
        int rows = scheduleTaskMapper.updateBatch(map);
        if (rows == 1) {
            abTestMapper.updateAbJobCompletedCount(Long.valueOf(task.getJobId()));
        } else {
            return false;
        }
        return true;
    }

    @Override
    public AbScheduleTask getTaskDetail(Map<String, Object> map) {
        String tableName = "ab_task_"+Integer.parseInt(map.get("userId").toString())/10+"_part";
        queryTableIFExists(tableName);
        map.put("tableName",tableName);
        return scheduleTaskMapper.getAbTaskDetail(map);
    }

    @Override
    public List<AbScheduleTask> getTaskByJobId(Map<String, Object> map) {
        return scheduleTaskMapper.getTaskByJobId(map);
    }

    @Override
    public AbTest getAbTestJobByName(AbTest abTest) {
        return abTestMapper.getAbTestJobByName(abTest);
    }

    @Override
    public Map getTaskResultListAll(Map<String, Object> map) {
        String tableName = "ab_task_" + Integer.parseInt(map.get("userId").toString()) / 10 + "_part";
        queryTableIFExists(tableName);
        map.put("tableName", tableName);
        map.put("stopStatus", 3);
        Page<AbScheduleTask> scheduleTasks = scheduleTaskMapper.findAbTaskByPageAll(map);
        Map<String, Integer> mapADecision = new TreeMap<String, Integer>();
        Map<String, Integer> mapBDecision = new TreeMap<String, Integer>();
        for (AbScheduleTask task : scheduleTasks) {
            String resultMapStr = task.getResultMap();
            HashMap hashMap = null;
            try {
                hashMap = JSON.parseObject(resultMapStr, HashMap.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(tableName+"表通过或拒绝状态的结果字段记录,有异常信息!会导致统计结果有误!");
                continue;
            }
            if (hashMap == null) {
                continue;
            }
            JSONArray jsonArray = (JSONArray) hashMap.get("excuteFlow");
            for (int i=0,j=jsonArray.size();i<j;i++) {
                JSONObject executeMap = (JSONObject) jsonArray.get(i);
                Integer status = (Integer) executeMap.get("status");
                Integer rulesId = (Integer) executeMap.get("rulesId");
                String rulesName = "";
                if (2 == status || 3 == status) {
                    JSONArray decisionFlow = (JSONArray) hashMap.get("decisionFlow");
                    for (Object rules : decisionFlow) {
                        JSONObject rulesMap = (JSONObject) rules;
                        Integer rulesId2 = (Integer) rulesMap.get("rulesId");
                        if (rulesId.equals(rulesId2)) {
                            rulesName = (String) rulesMap.get("name");
                            break;
                        }
                    }

                    if (task.getRole().equalsIgnoreCase("a")) {
                        Integer count = mapADecision.get(rulesName) == null ? 1 : mapADecision.get(rulesName) + 1;
                        mapADecision.put(rulesName, count);
                    } else {
                        Integer count = mapBDecision.get(rulesName) == null ? 1 : mapBDecision.get(rulesName) + 1;
                        mapBDecision.put(rulesName, count);
                    }
                }
            }
        }
        HashMap<Object, Object> overViewMap = new HashMap<>();
        ArrayList<HashMap> listA = new ArrayList<>();
        ArrayList<HashMap> listB = new ArrayList<>();

        if (mapADecision.size() > 0) {
            mapADecision = sortMapByValue(mapADecision);
            for (String key : mapADecision.keySet()) {
                HashMap<String, Object> itemMap = new HashMap<>();
                itemMap.put("name", key);
                itemMap.put("value", mapADecision.get(key));
                listA.add(itemMap);

            }
        }
        if (mapBDecision.size() > 0) {
            mapBDecision = sortMapByValue(mapBDecision);
            for (String key : mapBDecision.keySet()) {
                HashMap<String, Object> itemMap = new HashMap<>();
                itemMap.put("name", key);
                itemMap.put("value", mapBDecision.get(key));
                listB.add(itemMap);
            }
        }
        overViewMap.put("stopA", listA);
        overViewMap.put("stopB", listB);
        return overViewMap;
    }

    static class MapValueComparator implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {

            return me2.getValue().compareTo(me1.getValue());
        }
    }

    public static void main(String[] args) {


        String json = "{\"activeStatus\":1,\"businessId\":10003,\"channel\":\"api\",\"createTime\":\"2018-10-22 10:01:52\",\"creater\":\"北京集奥集合\",\"decisionFlow\":[{\"name\":\"开始\",\"index\":\"0\",\"to\":\"1540173873055\",\"position\":{\"top\":110,\"left\":300},\"type\":\"status\",\"flow\":[{\"to\":\"1540173873055\",\"type\":\"status\",\"value\":1,\"operator\":\"==\"}]},{\"name\":\"手机行为及运营商数据查验\",\"index\":1540173873055,\"position\":{\"top\":210,\"left\":250},\"rulesId\":3,\"flow\":[{\"to\":\"1540173877172\",\"type\":\"status\",\"value\":1,\"operator\":\"==\"}]},{\"name\":\"多平台借贷行为甄别\",\"index\":1540173877172,\"position\":{\"top\":328,\"left\":209},\"rulesId\":25,\"flow\":[{\"to\":\"1540173881244\",\"type\":\"status\",\"value\":1,\"operator\":\"==\"}]},{\"name\":\"测试规则引擎模版\",\"index\":1540173881244,\"position\":{\"top\":410,\"left\":410},\"rulesId\":91,\"flow\":[{\"to\":\"1540173885532\",\"type\":\"status\",\"value\":1,\"operator\":\"==\"}]},{\"name\":\"test003\",\"index\":1540173885532,\"position\":{\"top\":550,\"left\":300},\"rulesId\":93,\"flow\":[]}],\"eventId\":5924,\"excuteFlow\":[{\"eventId\":217648,\"flow\":[{\"operator\":\"==\",\"to\":1540173877172,\"type\":\"status\",\"value\":1}],\"index\":1540173873055,\"position\":\"{\\\"top\\\":210,\\\"left\\\":250}\",\"rulesId\":3,\"score\":100,\"status\":2}],\"id\":17,\"name\":\"test\",\"parameters\":\"{\\\"realName\\\":\\\"赵玉柏\\\",\\\"liveLongitude\\\":\\\"北京\\\",\\\"idNumber\\\":\\\"370404196212262212\\\",\\\"cycle\\\":\\\"720\\\",\\\"cid\\\":\\\"13306328903\\\"}\",\"sceneId\":10026,\"score\":100,\"sysStatus\":2,\"updateTime\":\"2018-10-30 12:15:14\",\"usedRulesIds\":\"[3,25,91,93]\"}";
        HashMap hashMap = JSON.parseObject(json, HashMap.class);
        JSONArray jsonArray = (JSONArray) hashMap.get("excuteFlow");
        JSONObject executeMap = (JSONObject) jsonArray.get(0);
        Integer status = (Integer) executeMap.get("status");
        Integer rulesId = (Integer) executeMap.get("rulesId");
        String rulesName = "";
        if (2 == status || 3 == status) {
            JSONArray decisionFlow = (JSONArray) hashMap.get("decisionFlow");
            for (Object rules : decisionFlow) {
                JSONObject rulesMap = (JSONObject) rules;
                Integer rulesId2 = (Integer) rulesMap.get("rulesId");
                if (rulesId.equals(rulesId2)) {
                    rulesName = (String) rulesMap.get("name");
                }
            }
        }


        Map<String, Integer> map = new TreeMap<String, Integer>();

        map.put(rulesName, 1);
        map.put("KFC", 5);
        map.put("WNBA", 100);
        map.put("NBA", 9);
        map.put("CBA", 4);

//        Map<String, String> resultMap = sortMapByKey(map);    //按Key进行排序
        Map<String, Integer> resultMap = sortMapByValue(map); //按Value进行排序
        System.out.println(resultMap.toString());
        for (Map.Entry<String, Integer> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    /**
     * 使用 Map按value进行排序
     *
     * @param
     * @return
     */
    public static Map<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry<String, Integer>> iter = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

}
