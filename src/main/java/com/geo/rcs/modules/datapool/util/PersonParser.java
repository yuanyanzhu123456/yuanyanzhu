package com.geo.rcs.modules.datapool.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import springfox.documentation.spring.web.json.Json;

import java.util.*;

import static com.geo.rcs.common.StatusCode.RES_ERROR;
import static com.geo.rcs.modules.engine.util.DatetimeFormattor.compareDateTimeString;
import static com.geo.rcs.modules.engine.util.EngineCalculator.compareDatetime;
import static com.geo.rcs.modules.source.entity.InterStatusCode.BASICDICT;
import static com.geo.rcs.modules.source.entity.InterStatusCode.ECLDICT;

public class PersonParser {

    // 初始化常量
    private static final String CODE = "code";
    private static final String SUCCSEE_CODE = "200";
    private static final String RCS_SUCCSEE_CODE = "2000";
    private static final String MSG = "msg";
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
     * 解析各维度接口数据
     * @param dimension
     * @param resp
     * @return
     */
    public static  String parse(String dimension, String resp){

        if(resp!=null&&resp.length()>0){
            JSONObject respData = JSONObject.parseObject(resp);
            String code = respData.get(CODE).toString();

            if(code.equals(RCS_SUCCSEE_CODE)){
                JSONArray data = JSONArray.parseArray(respData.get(DATA).toString());
                System.out.println(data);
                if(data.size()>0){
                    String singleData = data.getString(0);

                    if (singleData != null && singleData.length() != 0) {
                        JSONObject response = JSONObject.parseObject(singleData);
                        if (response.getString(CODE).equals(SUCCSEE_CODE)) {
                            JSONObject basicInfo = response.getJSONObject(DATA).getJSONObject("ISPNUM");
                            JSONArray interRslData = response.getJSONObject(DATA).getJSONArray("RSL");
                            JSONArray interEclData = response.getJSONObject(DATA).getJSONArray("ECL");
                            System.out.println("interRslData:"+interRslData);
                            return parserDetail(dimension, interRslData,interEclData);
                        }else{
                            // todo: 处理源数据返回值异常的情况
                            String errorCode = response.getString(CODE);
                            throw new RcsException(BASICDICT.get(errorCode),RES_ERROR.getCode());
                        }
                    }else{
                        // todo: 处理数据源模块返回值
                        throw new RcsException(respData.get(MSG).toString(), StatusCode.RES_ERROR.getCode());
                    }
                }else{
                    // 返回值为空列表情况
                    throw new RcsException(StatusCode.DATASOURCE_RESP_ERROR);
                }

            }else{
                System.out.println(respData.get("code").toString() + respData.get(MSG).toString());
                // todo: 处理第三方数据源返回值非2000的情况
                throw new RcsException(respData.get(MSG).toString(), Integer.valueOf(respData.get(CODE).toString()));
            }
        }else{
            System.out.println("返回结果为空值");
            // todo: 处理第三方数据源异常的情况
            throw new RcsException(StatusCode.DATASOURCE_RESP_ERROR);
        }
    }


    /**
     * 聚合维度数据
     * @param dimension
     * @param interRslData
     * @param interEclData
     * @return
     */
    public static String parserDetail(String dimension, JSONArray interRslData, JSONArray interEclData) {

        //todo： 解析接口详细字段

        if(interEclData.size()==0) {
            if (dimension.toUpperCase().equals(DimensionInterMapper.MULTIPLATE.getName())) {
                JSONObject detail = (JSONObject) interRslData.get(0);
                String desc = detail.getJSONObject(RS).getString(DESC);

                // 解析返回的数据数据
                JSONObject descDetail = JSONObject.parseObject(desc) ;

                JSONArray result_xdpt = descDetail.getJSONArray("result_xdpt");
                JSONArray result_dksq = descDetail.getJSONArray("result_dksq");
                JSONArray result_dkfk = descDetail.getJSONArray("result_dkfk");
                JSONArray result_dkbh = descDetail.getJSONArray("result_dkbh");
                JSONArray result_yqpt = descDetail.getJSONArray("result_yqpt");

                // 返回解析完成的数据
                JSONObject respDetail = new JSONObject();

                respDetail.put("result_xdpt", result_xdpt!=null?result_xdpt:new JSONArray());
                respDetail.put("result_dksq", result_dksq!=null?result_dksq:new JSONArray());
                respDetail.put("result_dkfk", result_dkfk!=null?result_dkfk:new JSONArray());
                respDetail.put("result_dkbh", result_dkbh!=null?result_dkbh:new JSONArray());
                respDetail.put("result_yqpt", result_yqpt!=null?result_yqpt:new JSONArray());

                return respDetail.toJSONString();

            } else if (dimension.toUpperCase().equals(DimensionInterMapper.NETSTATUS.getName())) {
                //todo: parse A4

                List<JSONObject> interList = interRslData.toJavaList(JSONObject.class);
                JSONObject jsonObject = (JSONObject) interList.get(0).get("RS");
                String onLineCode = (String)jsonObject.get("code");
                String onLineCodeDesc = (String)jsonObject.get("desc");


                JSONObject respDetail = new JSONObject();
                respDetail.put("onlineStatus",onLineCode);
                respDetail.put("desc",onLineCodeDesc);
                return respDetail.toJSONString();

            } else{
                throw new RcsException(StatusCode.DATA_POOL_DIMENSION_ERROR);
            }
        }else{
            JSONObject object = (JSONObject) interEclData.get(0);
            String code = object.getString(CODE);
            throw new RcsException(ECLDICT.get(code), StatusCode.RES_ERROR.getCode());
        }

    }


    /**
     * 聚合维度数据
     * @param dimension
     * @param resp
     * @return
     */
    public static String group(String dimension, String cacheData, String resp,  Map<String, Object> parameters){

        if(resp!=null&&resp.length()>0&&cacheData!=null&&cacheData.length()>0){
            if(dimension.toUpperCase().equals(DimensionInterMapper.MULTIPLATE.getName())){
                // 数据组合模板
                JSONObject groupData = new JSONObject();

                HashSet result_xdpt_set = new HashSet();
                HashSet result_dksq_set = new HashSet();
                HashSet result_dkfk_set = new HashSet();
                HashSet result_dkbh_set = new HashSet();
                HashSet result_yqpt_set = new HashSet();

                JSONObject _cacheData = JSONObject.parseObject(cacheData);
                JSONObject _resp = JSONObject.parseObject(resp);

                JSONArray result_xdpt = _cacheData.getJSONArray("result_xdpt");
                JSONArray result_dksq = _cacheData.getJSONArray("result_dksq");
                JSONArray result_dkfk = _cacheData.getJSONArray("result_dkfk");
                JSONArray result_dkbh = _cacheData.getJSONArray("result_dkbh");
                JSONArray result_yqpt = _cacheData.getJSONArray("result_yqpt");

                // CacheData空值处理
                result_xdpt=result_xdpt!=null?result_xdpt:new JSONArray();
                result_dksq=result_dksq!=null?result_dksq:new JSONArray();
                result_dkfk=result_dkfk!=null?result_dkfk:new JSONArray();
                result_dkbh=result_dkbh!=null?result_dkbh:new JSONArray();
                result_yqpt=result_yqpt!=null?result_yqpt:new JSONArray();

                JSONArray resp_result_xdpt = _resp.getJSONArray("result_xdpt");
                JSONArray resp_result_dksq = _resp.getJSONArray("result_dksq");
                JSONArray resp_result_dkfk = _resp.getJSONArray("result_dkfk");
                JSONArray resp_result_dkbh = _resp.getJSONArray("result_dkbh");
                JSONArray resp_result_yqpt = _resp.getJSONArray("result_yqpt");

                // resp空值处理
                resp_result_xdpt=resp_result_xdpt!=null?resp_result_xdpt:new JSONArray();
                resp_result_dksq=resp_result_dksq!=null?resp_result_dksq:new JSONArray();
                resp_result_dkfk=resp_result_dkfk!=null?resp_result_dkfk:new JSONArray();
                resp_result_dkbh=resp_result_dkbh!=null?resp_result_dkbh:new JSONArray();
                resp_result_yqpt=resp_result_yqpt!=null?resp_result_yqpt:new JSONArray();

                // 组合数据
                result_xdpt.addAll(resp_result_xdpt);
                result_dksq.addAll(resp_result_dksq);
                result_dkfk.addAll(resp_result_dkfk);
                result_dkbh.addAll(resp_result_dkbh);
                result_yqpt.addAll(resp_result_yqpt);

                result_xdpt_set.addAll(result_xdpt);
                result_dksq_set.addAll(result_dksq);
                result_dkfk_set.addAll(result_dkfk);
                result_dkbh_set.addAll(result_dkbh);
                result_yqpt_set.addAll(result_yqpt);

                groupData.put("result_xdpt", result_xdpt_set);
                groupData.put("result_dksq", result_dksq_set);
                groupData.put("result_dkfk", result_dkfk_set);
                groupData.put("result_dkbh", result_dkbh_set);
                groupData.put("result_yqpt", result_yqpt_set);

                return groupData.toJSONString();

            }else if(dimension.toUpperCase().equals(DimensionInterMapper.NETSTATUS.getName())){
                // 不需要聚合数据,直接更新为最新结果
                return  resp;
            }
        }

        throw new RcsException(StatusCode.PARAMS_ERROR);
    }

    /**
     * 聚合维度历史数据
     * @param dimension
     * @param groupData
     * @return
     */
    public static String analysis(String dimension, String groupData,  Map<String, Object> parameters){

        if(groupData!=null&&groupData.length()>0&&groupData!=null&&groupData.length()>0){
            if(DimensionInterMapper.validDimension(dimension)){
                if(validParamerters(parameters)){
                    return analysisDetail(dimension, groupData, parameters);
                }else{
                    // 参数错误
                    throw new RcsException(StatusCode.PARAMS_ERROR);
                }
            }else{
                // 维度错误
                throw new RcsException(StatusCode.DATA_POOL_DIMENSION_ERROR);
            }
        }else{
            // 聚合数据错误
            throw new RcsException(StatusCode.DATA_POOL_GROUP_DATA_ERROR);
        }
    }

    /**
     * 聚合数据分析Detail
     * @param dimension
     * @param groupData
     * @param parameters
     * @return
     */
    public static String  analysisDetail(String dimension, String groupData,  Map<String, Object> parameters){

        JSONObject analysisData = new JSONObject();
        String taskCycle = String.valueOf(parameters.get("taskCycle"));
        if(Integer.parseInt(taskCycle)==1){
            // 消除第一次监控updateTime数据错误
            parameters.put("updateTime", "2000-01-01 00:00:00");
        }

        if(dimension.toUpperCase().equals(DimensionInterMapper.MULTIPLATE.toString())) {

            int regtimes = 0;
            int apptimes = 0;
            int loantimes = 0;
            int rejtimes = 0;
            String appmoney = "";
            String loanmoney = "";
            String updateTime = String.valueOf(parameters.get("updateTime"));
            JSONObject _groupData = JSONObject.parseObject(groupData);

            JSONArray result_xdpt = _groupData.getJSONArray("result_xdpt");
            JSONArray result_dksq = _groupData.getJSONArray("result_dksq");
            JSONArray result_dkfk = _groupData.getJSONArray("result_dkfk");
            JSONArray result_dkbh = _groupData.getJSONArray("result_dkbh");

            regtimes = getTimesByTime(result_xdpt, "REGISTERTIME", updateTime);
            apptimes = getTimesByTime(result_dksq, "APPLICATIONTIME", updateTime);
            loantimes = getTimesByTime(result_dkfk, "LOANLENDERSTIME", updateTime);
            rejtimes = getTimesByTime(result_dkbh, "REJECTIONTIME", updateTime);
            appmoney = getMaxMoneyByTime(result_dksq, "APPLICATIONAMOUNT","APPLICATIONTIME", updateTime);
            loanmoney = getMaxMoneyByTime(result_dkfk, "LOANLENDERSAMOUNT","LOANLENDERSTIME", updateTime);
            // 整合数据
            boolean alert = Integer.parseInt(taskCycle)>1&& (
                    regtimes>0||rejtimes>0||apptimes>0||loantimes>0 ||
                            (appmoney!=null&&appmoney.length()>0) ||
                            (loanmoney!=null&&loanmoney.length()>0)
            );

            ArrayList<Object> detailList = new ArrayList<>();
            HashMap<String, Object> fieldMap = new HashMap<>();
            fieldMap.put("field","regtimes");
            fieldMap.put("fieldName","注册次数");
            fieldMap.put("value",regtimes);
            fieldMap.put("valueDesc","");
//            analysisData.put("regtimes", regtimes);
            detailList.add(fieldMap);

            HashMap<String, Object> fieldMap2 = new HashMap<>();

            fieldMap2.put("field","apptimes");
            fieldMap2.put("fieldName","申请次数");
            fieldMap2.put("value",apptimes);
            fieldMap2.put("valueDesc","");
            detailList.add(fieldMap2);

//            analysisData.put("apptimes", apptimes);
            HashMap<String, Object> fieldMap3 = new HashMap<>();

            fieldMap3.put("field","loantimes");
            fieldMap3.put("fieldName","放款次数");
            fieldMap3.put("value",loantimes);
            fieldMap3.put("valueDesc","");
            detailList.add(fieldMap3);
//            analysisData.put("loantimes", loantimes);

            HashMap<String, Object> fieldMap4 = new HashMap<>();

            fieldMap4.put("field","appmoney");
            fieldMap4.put("fieldName","最大申请金额");
            fieldMap4.put("value",appmoney);
            fieldMap4.put("valueDesc","");
            detailList.add(fieldMap4);
//            analysisData.put("appmoney", appmoney);

            HashMap<String, Object> fieldMap5 = new HashMap<>();
            fieldMap5.put("field","loanmoney");
            fieldMap5.put("fieldName","最大放款金额");
            fieldMap5.put("value",loanmoney);
            fieldMap5.put("valueDesc","");
            detailList.add(fieldMap5);

//            analysisData.put("loanmoney", loanmoney);
            HashMap<String, Object> fieldMap6 = new HashMap<>();

            fieldMap6.put("field","rejtimes");
            fieldMap6.put("fieldName","驳回次数");
            fieldMap6.put("value",rejtimes);
            fieldMap6.put("valueDesc","");
            detailList.add(fieldMap6);
//            analysisData.put("rejtimes", rejtimes);

            analysisData.put("detail", detailList);
            analysisData.put("alert", alert);
            analysisData.put("taskCycle", taskCycle);
        }else if(dimension.toUpperCase().equals(DimensionInterMapper.NETSTATUS.toString())){
            // todo: 状态构建
            Boolean alert=false;
            JSONObject jsonObject = JSONObject.parseObject(groupData);
            String onlineCode = (String) jsonObject.get("onlineStatus");
            String desc = (String) jsonObject.get("desc");

            ArrayList<Object> detailList = new ArrayList<>();

            HashMap<String,String > fieldMap = new HashMap<>();
            fieldMap.put("field","onlineStatus");
            fieldMap.put("fieldName","在网状态");
            fieldMap.put("value",onlineCode);
            fieldMap.put("valueDesc",desc);

            detailList.add(fieldMap);
            analysisData.put("detail",detailList);
            analysisData.put("alert", alert);
            analysisData.put("taskCycle", taskCycle);

        }else{
            throw  new RcsException(StatusCode.DATA_POOL_DIMENSION_ERROR);
        }
        


        //todo： 聚合数据
        return analysisData.toJSONString();
        
    }

    /**
     * 计算注册次数
     * @param arrayData
     * @param updaTime
     * @return
     */
    public static int getTimesByTime(JSONArray arrayData, String key, String updaTime) {
        int count = 0;
        if(arrayData.size()>0){
            for (int i = 0; i < arrayData.size(); i++) {
                JSONObject item = arrayData.getJSONObject(i);
                String itemTime = item.getString(key);
                try{
                    int status = compareDateTimeString(itemTime, updaTime);
                    count += status>0?status:0;
                }catch (Exception e){
                    e.printStackTrace();
                    throw new RcsException(StatusCode.DATA_POOL_GROUP_DATA_ERROR);
                }
            }
        }
        return count;
    }


    /**
     * 计算最大金额
     * @param arrayData
     * @param updaTime
     * @return
     */
    public static String getMaxMoneyByTime(JSONArray arrayData, String key, String keyTime, String updaTime) {
        String maxMoney = "";
        if(arrayData.size()>0){
            for (int i = 0; i < arrayData.size(); i++) {
                JSONObject item = arrayData.getJSONObject(i);
                String itemMoney = item.getString(key);
                String itemTime = item.getString(keyTime);
                try{
                    int status = compareDateTimeString(itemTime, updaTime);
                    // 压点的处理
                    if(status>=0){
                        maxMoney = getMaxMoney(maxMoney, itemMoney);
                    }
                }catch (Exception e){
                    throw new RcsException(StatusCode.DATA_POOL_GROUP_DATA_ERROR);
                }
            }
        }
        return maxMoney;

    }

    /**
     *
     * @param maxMoney
     * @param nextMoney
     * @return
     */
    public static String getMaxMoney(String maxMoney, String nextMoney){

        List<String> maxSortList =  Arrays.asList("", "0W～0.2W","0.2W～0.5W", "0.5W～1W", "1W～3W", "3W～5W","5W～10W", "10W以上");

        if(maxSortList.contains(maxMoney)&&maxSortList.contains(nextMoney)){
            return maxSortList.indexOf(maxMoney)>maxSortList.indexOf(nextMoney)?maxMoney:nextMoney;
        }else{
            // todo: 处理非法字符错误，因为存在字符的不确定性，直接返回原maxMoney数据，不直接抛出异常
            return maxMoney;
        }
    }

    /**
     * 参数检查
     * @param parameters
     * @return
     */
    public static Boolean validParamerters(Map<String, Object> parameters){

        String taskCycle = String.valueOf(parameters.get("taskCycle"));
        String interval = String.valueOf(parameters.get("interval"));
        String unit = String.valueOf(parameters.get("unit"));
        String updateTime = String.valueOf(parameters.get("updateTime"));

        if(taskCycle!=null&&taskCycle.length()>0&&interval!=null&&interval.length()>0&& unit!=null&&unit.length()>0&&
            updateTime!=null&&updateTime.length()>0){
                //todo: 检查数据格式
            return true;
        }else{
            return false;
        }

    }




    public static void main(String[] args) {
//        String cacheData = "{\"result_yqpt\":[],\"result_dksq\":[],\"result_dkbh\":[],\"result_dkfk\":[],\"result_xdpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\",\"PLATFORMCODE\":\"GEO_0000182105\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017-04-12 13:57:19\",\"PLATFORMCODE\":\"GEO_0000001624\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017-02-20 16:01:51\",\"PLATFORMCODE\":\"GEO_0000000131\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017-10-30 09:35:30\",\"PLATFORMCODE\":\"GEO_0000004397\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2017-09-29 11:18:44\",\"PLATFORMCODE\":\"GEO_0000000143\",\"P_TYPE\":\"2\"},{\"REGISTERTIME\":\"2018-06-04 16:46:34\",\"PLATFORMCODE\":\"GEO_0000184957\",\"P_TYPE\":\"2\"}]}";

        String cacheData = "";
        String resp = "";

        /* 重复情况一 */
        cacheData = "{\"result_yqpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\"}],\"result_xdpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\",\"PLATFORMCODE\":\"GEO_0000182105\",\"P_TYPE\":\"2\"}]}";
        resp = "{\"result_yqpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\"}],\"result_xdpt\":[{\"REGISTERTIME\":\"2018-06-04 16:46:34\",\"PLATFORMCODE\":\"GEO_0000184957\",\"P_TYPE\":\"2\"}, {\"REGISTERTIME\":\"2016-09-01 15:07:58\",\"PLATFORMCODE\":\"GEO_0000182105\",\"P_TYPE\":\"2\"}]}";
        System.out.println( group("T40301", cacheData, resp, new HashMap<>()));


        /* 重复情况二 */
        cacheData = "{\"result_yqpt\":[],\"result_xdpt\":[]}";
        resp = "{\"result_yqpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\"}],\"result_xdpt\":[{\"REGISTERTIME\":\"2018-06-04 16:46:34\",\"PLATFORMCODE\":\"GEO_0000184957\",\"P_TYPE\":\"2\"}, {\"REGISTERTIME\":\"2016-09-01 15:07:58\",\"PLATFORMCODE\":\"GEO_0000182105\",\"P_TYPE\":\"2\"}]}";
        System.out.println( group("T40301", cacheData, resp, new HashMap<>()));

        /* 重复情况三 */
        resp = "{\"result_yqpt\":[],\"result_xdpt\":[]}";
        cacheData = "{\"result_yqpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\"}],\"result_xdpt\":[{\"REGISTERTIME\":\"2018-06-04 16:46:34\",\"PLATFORMCODE\":\"GEO_0000184957\",\"P_TYPE\":\"2\"}, {\"REGISTERTIME\":\"2016-09-01 15:07:58\",\"PLATFORMCODE\":\"GEO_0000182105\",\"P_TYPE\":\"2\"}]}";
        System.out.println( group("T40301", cacheData, resp, new HashMap<>()));


                /* 重复情况三 */
        resp = "{\"result_yqpt\":[{\"REGISTERTIME\":\"2016-09-01 15:07:58\"}],\"result_xdpt\":[]}";
        cacheData = "{\"result_yqpt\":[],\"result_xdpt\":[{\"REGISTERTIME\":\"2018-06-04 16:46:34\",\"PLATFORMCODE\":\"GEO_0000184957\",\"P_TYPE\":\"2\"}, {\"REGISTERTIME\":\"2016-09-01 15:07:58\",\"PLATFORMCODE\":\"GEO_0000182105\",\"P_TYPE\":\"2\"}]}";
        System.out.println( group("T40301", cacheData, resp, new HashMap<>()));

    }

}


