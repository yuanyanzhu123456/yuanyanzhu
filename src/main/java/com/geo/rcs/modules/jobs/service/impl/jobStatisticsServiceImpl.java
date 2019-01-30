package com.geo.rcs.modules.jobs.service.impl;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.modules.jobs.service.RedisService;
import com.geo.rcs.modules.jobs.service.jobStatisticsService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
class jobStatisticsServiceImpl implements jobStatisticsService {

    @Autowired
    private RedisService redisService;

    @Value("${spring.profiles.active}")
    private String environment;
    @Value("${rabbitmq.taskroles}")
    private String[] taskroles;
    // 此处直接注入即可
    @Autowired
    private JedisPool jedisPool;
    Jedis jedis;
    static final String REDIS_MODELS_KEY = "RCS-JOBS-";
    static final String DATASOURCE_TABLE = "RCS-JobRegisterStatistics";
    public static final String CUSTOMSUCCESSTOTAL = REDIS_MODELS_KEY + "CUSTOMSUCCESSTOTAL";
    public static final String CUSTOMFAILTOTAL = REDIS_MODELS_KEY + "CUSTOMFAILTOTAL";
    public static final String CUSTOMTOTAL = REDIS_MODELS_KEY + "CUSTOMTOTAL";
    public static final String DISTRIBUTIONSUCCESS = REDIS_MODELS_KEY + "DISTRIBUTIONSUCCESS";
    public static final String DISTRIBUTIONFAIL = REDIS_MODELS_KEY + "DISTRIBUTIONFAIL";
    public static final String DISTRIBUTIONTOTAL = REDIS_MODELS_KEY + "DISTRIBUTIONTTOAL";
    public static final String CUSTOMSUCCESSTOTALNOW = "customSuccessTotalNow";
    public static final String CUSTOMFAILTOTALNOW = "customFailTotalNow";
    public static final String CUSTOMTOTALNOW = "customTotalNow";
    public static final String DISTRIBUTIONSUCCESSNOW = "distributionSuccessNow";
    public static final String DISTRIBUTIONFAILNOW = "distributionFailNow";
    public static final String DISTRIBUTIONTOTALNOW = "distributiontToalNow";
    public static final String TIME = "time";
    public static final String FORMATDATE = "formatDate";
    public static final String ALL = "_ALL";

    @Override
    public void setRegisterInfoByKey(String infoName, String infoContent) {
        redisService.hset(DATASOURCE_TABLE, infoName, infoContent);
    }

    @Override
    public void updateRegisterInfoByKey(String infoName, String infoContent) {
        redisService.hset(DATASOURCE_TABLE, infoName, infoContent);
    }

    @Override
    public void deleteRegisterInfoByKey(String infoName) {
        redisService.hdel(DATASOURCE_TABLE, infoName);
    }


    @Override
    public Boolean incrSuccess(String role, int num) {
        return incr(CUSTOMTOTAL + ALL, CUSTOMSUCCESSTOTAL + ALL, CUSTOMTOTAL + role, CUSTOMSUCCESSTOTAL + role, num);
    }

    private Boolean incr(String totalKey, String key, String roleTotalKey, String roleKey, int num) {
        try {
            getJedis();
            jedis.incrBy(totalKey, num + 0L);
            jedis.incrBy(key, num + 0L);
            jedis.incrBy(roleTotalKey, num + 0L);
            jedis.incrBy(roleKey, num + 0L);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getJedis() {
        if (jedis == null) {
            jedis = this.jedisPool.getResource();
        }
    }

    @Override
    public Boolean incrfail(String role, int num) {
        return incr(CUSTOMTOTAL + ALL, CUSTOMFAILTOTAL + ALL, CUSTOMTOTAL + role, CUSTOMFAILTOTAL + role, num);

    }

    @Override
    public Boolean delStatisticData() {
        String sumKey="_"+"ALL";
        Boolean delSumKey = delByKey(CUSTOMSUCCESSTOTAL + sumKey, CUSTOMFAILTOTAL + sumKey, DISTRIBUTIONSUCCESS + sumKey, DISTRIBUTIONFAIL + sumKey, CUSTOMTOTAL + sumKey, DISTRIBUTIONTOTAL + sumKey);

        for (int i = 0; i < taskroles.length; i++) {
            String role = "_" + taskroles[i].toUpperCase();
            Boolean deleteBoolean = delByKey(CUSTOMSUCCESSTOTAL + role, CUSTOMFAILTOTAL + role, DISTRIBUTIONSUCCESS + role, DISTRIBUTIONFAIL + role, CUSTOMTOTAL + role, DISTRIBUTIONTOTAL + role);
            Boolean deleteBoolean1 = delByKey("customTotal", "customSuccessTotal", "customFailTotal", "distributiontToal", "distributionFail", "distributionSuccess");
            Map allRegisterInfo1 = getAllRegisterInfo();
            for (Object str : allRegisterInfo1.keySet()) {
                deleteRegisterInfoByKey((String) str);
            }
        }
        return true;
    }

    private Boolean delByKey(final String... keys) {
        try {
            getJedis();
            jedis.del(keys);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean incrDistrbuteFail(String role,int num) {
        return incr(DISTRIBUTIONTOTAL+ALL, DISTRIBUTIONFAIL+ALL, DISTRIBUTIONTOTAL+role, DISTRIBUTIONFAIL+role,num);
    }

    @Override
    public Boolean incrDistrbuteSuccess(String role,int num) {
        return incr(DISTRIBUTIONTOTAL+ALL, DISTRIBUTIONSUCCESS+ALL,DISTRIBUTIONTOTAL+role, DISTRIBUTIONSUCCESS+role, num);
    }

    /**
     * hour
     *
     * @param unit
     * @param intervel
     * @param size
     * @param key
     * @return
     */
    private Boolean statistics(String role, Long unit, int intervel, int size, String key) {
        try {

            List<Map<Object, Object>> dayMapList = new ArrayList<>(size);
            //获取历史数据
            String historyJson = getRegisterInfoByKey(key);
            Date currentDate = new Date();
            //获取当前最新数据map
            HashMap<Object, Object> statisMap = null;
            if (StringUtils.isBlank(historyJson)) {
                //说明是第一次,直接记录
                statisMap = getDataMap(role,key);
                dayMapList.add(statisMap);
                setRegisterInfoByKey(key, JSON.toJSONString(dayMapList));
            } else {
                dayMapList = JSON.parseObject(historyJson, ArrayList.class);
                //获取最后一条数据
                Map<Object, Object> lastMap = dayMapList.get(dayMapList.size() - 1);
                //获取统计时间
                String time = (String) lastMap.get(FORMATDATE);
                //添加判断,如果是
                Date lastDate = null;
                if (key.contains("DAY")) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(time);
                } else if (key.contains("HOUR")) {
                    try {
                        lastDate = new SimpleDateFormat("yyyy-MM-dd HH:00:00").parse(time);
                    } catch (Exception e) {
                        lastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").parse(time);
                    }

                } else if (key.contains("MINUTE")) {
                    lastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:00").parse(time);
                }
                long time1 = lastDate.getTime();
                long currentTime = System.currentTimeMillis();
                if (time1 + unit * intervel <= currentTime) {
                    if (dayMapList.size() >= size) {
                        dayMapList.remove(0);
                    }
                    //说明时间到了该执行了
                    statisMap = getDataMap(role,key, lastMap);
                    dayMapList.add(statisMap);
                    setRegisterInfoByKey(key, JSON.toJSONString(dayMapList));

                } else {
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
        return false;
    }

    @Override
    public Boolean statisticsApi(String role, String unit, int intervel, int size) {
        role="_"+role.toUpperCase();
        Long secondUnit = 1000L;
        //统一转成大写
        unit = unit.toUpperCase();
        String key = "KEY_" + role
                .toUpperCase() + "_" + unit + "_" + intervel + "_" + size;
        if ("SECOND".equalsIgnoreCase(unit)) {
            return statistics(role, secondUnit, intervel, size, key);
        } else if ("MINUTE".equalsIgnoreCase(unit)) {
            return statistics(role,secondUnit * 60, intervel, size, key);
        } else if ("HOUR".equalsIgnoreCase(unit)) {
            return statistics(role,secondUnit * 60 * 60, intervel, size, key);
        } else if ("DAY".equalsIgnoreCase(unit)) {
            return statistics(role,secondUnit * 60 * 60 * 24, intervel, size, key);
        } else {
            System.out.println("暂不支持该时间单位");
            return false;
        }

    }

    @Override
    public List<Map<Object, Object>> getstatisticsApi(String unit, int intervel, int size,String role) {
        if (!StringUtils.isBlank(unit)) {
            unit = unit.toUpperCase();
        }
       // String key = "KEY_ALL_" + unit + "_" + intervel + "_" + size;
        String key = "KEY__" + role.toUpperCase() + "_" + unit + "_" + intervel + "_" + size;
        String historyJson = getRegisterInfoByKey(key);
        List<Map<Object, Object>> dayMapList = new ArrayList<>();
        if (StringUtils.isBlank(historyJson)) {
            return dayMapList;
        } else {
            dayMapList = JSON.parseObject(historyJson, ArrayList.class);
        }
        //为前台提取整合数据
        List<Map<Object,Object>> result = new ArrayList<>();
        for (Map<Object,Object> map : dayMapList){
            Map<Object,Object> resultMap = new HashedMap();
            resultMap.put("distributiontToalNow",map.get("distributiontToalNow_"+role.toUpperCase()));
            resultMap.put("distributionFailNow",map.get("distributionFailNow_"+role.toUpperCase()));
            resultMap.put("customTotalNow",map.get("customTotalNow_"+role.toUpperCase()));
            resultMap.put("time",map.get("time"));
            resultMap.put("formatDate",map.get("formatDate"));
            result.add(resultMap);
        }
        return result;
    }

    private int getCurrentHour() {
        String hourStr = DateUtils.format(new Date(), "HH");
        return Integer.valueOf(hourStr);
    }


    @Override
    public Map<String, Long> getDistrbiteExcuteThroughPut(String role) throws ParseException {
        String roleName = "_"+role.toUpperCase();
        getJedis();
        String customTotalStr = jedis.get(CUSTOMTOTAL+roleName) == null ? "0" : jedis.get(CUSTOMTOTAL+roleName);
        String distrbuteTotalStr = jedis.get(DISTRIBUTIONTOTAL+roleName) == null ? "0" : jedis.get(DISTRIBUTIONTOTAL+roleName);
        Long customTotal = Long.valueOf(customTotalStr);
        Long distrbuteTotal = Long.valueOf(distrbuteTotalStr);
        HashMap<String, Long> map = new HashMap<>();
        //实时分发执行总量
        map.put("distrbuteCount", distrbuteTotal);
        map.put("executeCount", customTotal);
        //计算吞吐量,默认获取为
        Long startTotal = 0L;
//        List<Map<Object, Object>> mapDataList = getstatisticsApi("hour", 1, 24);
        List<Map<Object, Object>> mapDataList = null;
//        if (!"pro".equalsIgnoreCase(environment) && !"test".equalsIgnoreCase(environment)) {
        if (false) {
            mapDataList = getstatisticsApi("minute", 1, 24,role);
        } else {
            mapDataList = getstatisticsApi("hour", 1, 24,role);
        }
        if (mapDataList.size() == 0) {
            map.put("throughPut", customTotal);
        } else {
            for (int i = 0, len = mapDataList.size(); i < len; i++) {
                Map<Object, Object> hourMap = mapDataList.get(i);
                startTotal = getaLong(hourMap.get(CUSTOMTOTAL+roleName));
                if (0 != startTotal) {
                    String startTimeStr = (String) hourMap.get(TIME);
                    long startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTimeStr).getTime();
                    long endTime = System.currentTimeMillis();
                    double throughPut = ((double) customTotal - (double) startTotal) / (((double) endTime - (double) startTime) * 1000 * 60 * 60);
                    map.put("throughPut", Math.round(throughPut));
                    getChange(map,role);
                    return map;
                }
            }
        }
        getChange(map,role);
        map.put("throughPut", customTotal);
        return map;
    }

    private void getChange(HashMap<String, Long> map,String role) {
        //统计任务分发量默认获取为, 今日和近七天的变化\
        List<Map<Object, Object>> mapDataListHis = null;
//        if (!"pro".equalsIgnoreCase(environment)&&!"test".equalsIgnoreCase(environment)) {
        if (false) {
            mapDataListHis = getstatisticsApi("minute", 1, 24,role);
        } else {
            mapDataListHis = getstatisticsApi("day", 1, 30,role);
        }
        if (mapDataListHis.size() == 0 || mapDataListHis.size() < 3) {
            map.put("taskExecuteDayChange", 0L);
            map.put("taskExecuteWeekChange", 0L);
            map.put("taskScheduDayChange", 0L);
            map.put("taskScheduWeekChange", 0L);
        } else {
            //可以统计较前日变化数据
            //以获取今天的总数
            Map<Object, Object> todyDataMap = mapDataListHis.get(mapDataListHis.size() - 1);
            Long customTotalTodyStr = getaLong(todyDataMap.get(CUSTOMTOTAL+"_ALL"));
            Long distrbuteTotalTodyStr = getaLong(todyDataMap.get(DISTRIBUTIONTOTAL+"_ALL"));
            //获取昨天的总数
            Map<Object, Object> yesDataMap = mapDataListHis.get(mapDataListHis.size() - 2);
            Long customTotalZTStr = getaLong(yesDataMap.get(CUSTOMTOTAL+"_ALL"));
            Long distrbuteTotalZTStr = getaLong(yesDataMap.get(DISTRIBUTIONTOTAL+"_ALL"));
            //获取前天的总数
            Map<Object, Object> beforeYesDataMap = mapDataListHis.get(mapDataListHis.size() - 3);
            Long cusbeforeYesStr = getaLong(beforeYesDataMap.get(CUSTOMTOTAL+"_ALL"));
            Long disbeforeYesStr = getaLong(beforeYesDataMap.get(DISTRIBUTIONTOTAL+"_ALL"));

            Long custoTotalTody = customTotalTodyStr - customTotalZTStr;
            Long custoTotalYesTerday = customTotalZTStr - cusbeforeYesStr;
            map.put("taskExecuteDayChange", custoTotalTody - custoTotalYesTerday);

            Long distrbuteTotalZT = distrbuteTotalTodyStr - distrbuteTotalZTStr;
            Long disbeforeYes = distrbuteTotalZTStr - disbeforeYesStr;
            map.put("taskScheduDayChange", distrbuteTotalZT - disbeforeYes);

            if (mapDataListHis.size() >= 15) {

                Map<Object, Object> todyDataMap7 = mapDataListHis.get(mapDataListHis.size() - 1);
                Long customTotalTodyStr7 = getaLong(todyDataMap7.get(CUSTOMTOTAL+"_ALL"));
                Long distrbuteTotalTodyStr7 = getaLong(todyDataMap7.get(DISTRIBUTIONTOTAL+"_ALL"));
                //获取倒数第八天的数据
                Map<Object, Object> lestEightDataMap = mapDataListHis.get(mapDataListHis.size() - 8);
                Long customTotalZTStr7 = getaLong(lestEightDataMap.get(CUSTOMTOTAL+"_ALL"));
                Long distrbuteTotalZT7Str7 = getaLong(lestEightDataMap.get(DISTRIBUTIONTOTAL+"_ALL"));
                //获取倒数第15天的数据
                Map<Object, Object> lestfifteentDataMap = mapDataListHis.get(mapDataListHis.size() - 15);
                Long cusbeforeYesStr7 = getaLong(lestfifteentDataMap.get(CUSTOMTOTAL+"_ALL"));
                Long disbeforeYesStr7 = getaLong(lestfifteentDataMap.get(DISTRIBUTIONTOTAL+"_ALL"));

                Long custoTotalTody7 = customTotalTodyStr7 - customTotalZTStr7;
                Long custoTotalYesTerday7 = customTotalZTStr7 - cusbeforeYesStr7;
                map.put("taskExecuteWeekChange", custoTotalTody7 - custoTotalYesTerday7);

                Long distrbuteTotalZT7 = distrbuteTotalTodyStr7 - distrbuteTotalZT7Str7;
                Long disbeforeYes7 = distrbuteTotalZT7Str7 - disbeforeYesStr7;
                map.put("taskScheduWeekChange", distrbuteTotalZT7 - disbeforeYes7);
            } else {
                map.put("taskExecuteWeekChange", 0L);
                map.put("taskScheduWeekChange", 0L);

            }
        }
    }

    private Long getaLong(Object o) {
        long result = 0L;
        if (o instanceof String) {
            result = Long.valueOf((String) o);
        } else if (o instanceof Integer) {
            result = Long.valueOf((Integer) o);
        } else if (o instanceof Long) {
            result = (Long) o;
        }
        return result;
    }


    /**
     * 获取redis最新数据
     *
     * @return
     */
    private HashMap<Object, Object> getDataMap(String role,String key) {
        getJedis();
        String successTotal = jedis.get(CUSTOMSUCCESSTOTAL+role) == null ? "0" : jedis.get(CUSTOMSUCCESSTOTAL+role);
        String failTotal = jedis.get( CUSTOMFAILTOTAL+role) == null ? "0" : jedis.get( CUSTOMFAILTOTAL+role);
        String distrbuteSuccessTotal = jedis.get( DISTRIBUTIONSUCCESS+role) == null ? "0" : jedis.get( DISTRIBUTIONSUCCESS+role);
        String distrbuteFailTotal = jedis.get( DISTRIBUTIONFAIL+role) == null ? "0" : jedis.get( DISTRIBUTIONFAIL+role);
        HashMap<Object, Object> statisMap = new HashMap<>();
        //全部数据
        statisMap.put(CUSTOMSUCCESSTOTAL+role, successTotal);
        statisMap.put( CUSTOMFAILTOTAL+role, failTotal);
        statisMap.put( DISTRIBUTIONSUCCESS+role, distrbuteSuccessTotal);
        statisMap.put( DISTRIBUTIONFAIL+role, distrbuteFailTotal);
        statisMap.put( CUSTOMTOTAL+role, Long.valueOf(successTotal) + Long.valueOf(failTotal) + "");
        statisMap.put( DISTRIBUTIONTOTAL+role, Long.valueOf(distrbuteFailTotal) + Long.valueOf(distrbuteSuccessTotal) + "");
        //增量数据
        statisMap.put( CUSTOMSUCCESSTOTALNOW+role, successTotal);
        statisMap.put( CUSTOMFAILTOTALNOW+role, failTotal);
        statisMap.put( DISTRIBUTIONSUCCESSNOW+role, distrbuteSuccessTotal);
        statisMap.put( DISTRIBUTIONFAILNOW+role, distrbuteFailTotal);
        statisMap.put( CUSTOMTOTALNOW+role, Long.valueOf(successTotal) + Long.valueOf(failTotal) + "");
        statisMap.put( DISTRIBUTIONTOTALNOW+role, Long.valueOf(distrbuteFailTotal) + Long.valueOf(distrbuteSuccessTotal) + "");
        Date currentDate = new Date();
        //根据unit判断formateDate的格式
        String date = "";
        String formatDate = "";
        if (key.contains("DAY")) {
            date = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss");
            formatDate = DateUtils.format(currentDate, "yyyy-MM-dd");
        } else if (key.contains("HOUR")) {
            date = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss");
            formatDate = DateUtils.format(currentDate, "yyyy-MM-dd HH:00:00");
        } else if (key.contains("MINUTE")) {
            date = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss");
            formatDate = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:00");
        }

        statisMap.put(TIME, date);
        statisMap.put(FORMATDATE, formatDate);
        return statisMap;
    }

    /**
     * 获取redis最新数据
     *
     * @return
     */
    private HashMap<Object, Object> getDataMap(String role,String key, Map<Object, Object> lastMap) {
        getJedis();
        String successTotal = jedis.get( CUSTOMSUCCESSTOTAL +role) == null ? "0" : jedis.get( CUSTOMSUCCESSTOTAL +role);
        String failTotal = jedis.get( CUSTOMFAILTOTAL +role) == null ? "0" : jedis.get( CUSTOMFAILTOTAL +role);
        String distrbuteSuccessTotal = jedis.get( DISTRIBUTIONSUCCESS +role) == null ? "0" : jedis.get( DISTRIBUTIONSUCCESS +role);
        String distrbuteFailTotal = jedis.get( DISTRIBUTIONFAIL +role) == null ? "0" : jedis.get( DISTRIBUTIONFAIL +role);
        HashMap<Object, Object> statisMap = new HashMap<>();
        //全量数据
        statisMap.put( CUSTOMSUCCESSTOTAL +role, successTotal);
        statisMap.put( CUSTOMFAILTOTAL +role, failTotal);
        statisMap.put( DISTRIBUTIONSUCCESS +role, distrbuteSuccessTotal);
        statisMap.put( DISTRIBUTIONFAIL +role, distrbuteFailTotal);
        statisMap.put(CUSTOMTOTAL+role, Long.valueOf(successTotal) + Long.valueOf(failTotal) + "");
        statisMap.put(DISTRIBUTIONTOTAL+role, Long.valueOf(distrbuteFailTotal) + Long.valueOf(distrbuteSuccessTotal) + "");
        //计算增量数据,1先求出上一次的全量数据2再用这次的全量数据减去昨天的全量数据,就是现在的增量数据

        Long yesterDayCusSuccTotal = getaLong(lastMap.get( CUSTOMSUCCESSTOTAL +role));
        Long yesterDayCusFailTotal = getaLong(lastMap.get( CUSTOMFAILTOTAL +role));
        Long yesterDayCusTotal = getaLong(lastMap.get(CUSTOMTOTAL+role));
        Long yesterDayDisSucce = getaLong(lastMap.get( DISTRIBUTIONSUCCESS +role));
        Long yesterDayDisFail = getaLong(lastMap.get( DISTRIBUTIONFAIL +role));
        Long yesterDayDisTotal = getaLong(lastMap.get(DISTRIBUTIONTOTAL+role));

        statisMap.put( CUSTOMSUCCESSTOTALNOW+role, Long.valueOf(successTotal) - yesterDayCusSuccTotal + "");
        statisMap.put( CUSTOMFAILTOTALNOW+role, Long.valueOf(failTotal) - yesterDayCusFailTotal + "");
        statisMap.put( CUSTOMTOTALNOW+role, Long.valueOf(successTotal) + Long.valueOf(failTotal) - yesterDayCusTotal + "");
        statisMap.put( DISTRIBUTIONSUCCESSNOW+role, Long.valueOf(distrbuteSuccessTotal) - yesterDayDisSucce + "");
        statisMap.put( DISTRIBUTIONFAILNOW+role, Long.valueOf(distrbuteFailTotal) - yesterDayDisFail + "");
        statisMap.put( DISTRIBUTIONTOTALNOW+role, Long.valueOf(distrbuteFailTotal) + Long.valueOf(distrbuteSuccessTotal) - yesterDayDisTotal + "");

        Date currentDate = new Date();
        //根据unit判断formateDate的格式
        String date = "";
        String formatDate = "";
        if (key.contains("DAY")) {
            date = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss");
            formatDate = DateUtils.format(currentDate, "yyyy-MM-dd");
        } else if (key.contains("HOUR")) {
            date = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss");
            formatDate = DateUtils.format(currentDate, "yyyy-MM-dd HH:00:00");
        } else if (key.contains("MINUTE")) {
            date = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss");
            formatDate = DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:00");
        }

        statisMap.put(TIME, date);
        statisMap.put(FORMATDATE, formatDate);
        return statisMap;
    }


    @Override
    public String getRegisterInfoByKey(String infoName) {
        String res = redisService.hget(DATASOURCE_TABLE, infoName);
        return res;
    }

    @Override
    public Map getAllRegisterInfo() {
        Map res = redisService.hgetAll(DATASOURCE_TABLE);
        return res;
    }

    /***
     * 测试主函数
     * @param args
     */
    public static void main(String[] args) {

//        RegisterService a = new jobStatisticsServiceImpl();
//        a.setRegisterInfoByKey("worker001", UUID.randomUUID().toString());
//        System.out.println(a.getRegisterInfoByKey( "worker001"));
//        a.updateRegisterInfoByKey("worker001", UUID.randomUUID().toString());
//        System.out.println(a.getRegisterInfoByKey("worker001"));
//        a.deleteRegisterInfoByKey("worker001");
//        System.out.println(a.getRegisterInfoByKey("worker001"));
//
//        /* 存储Worker注册信息测试 */
//        List list1 = new ArrayList();
//        JobWorker register = new JobWorker();
//        register.initWorkerInfo("worker002", "127.0.0.1", "amqp-dddzfhdsksls", 1, 0, list1,"MAIL—POSTER");
//        String info = register.getRegisterInfo(register);
//
//        a.setRegisterInfoByKey("worker001", "info");
//        a.deleteRegisterInfoByKey("worker002");
//        a.deleteRegisterInfoByKey("worker003");
//        a.deleteRegisterInfoByKey("worker004");
//
//        System.out.println(a.getAllRegisterInfo());
    }

}
