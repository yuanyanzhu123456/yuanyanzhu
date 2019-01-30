package com.geo.rcs.modules.source.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.modules.engine.entity.Parameters;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.engine.entity.RulesConfig;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.source.service.impl.InterfaceServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SourceEngineer {

    public static void main(String[] args){

        String rulesConfig2 = RulesConfig.getRulesConfig2();
        rulesConfig2 = "{\n" +
                "    \"id\": 10001,\n" +
                "    \"name\": \"规则集一\",\n" +
                "    \"businessId\": 10001,\n" +
                "    \"senceId\": 10001,\n" +
                "    \"matchType\": false,\n" +
                "    \"thresholdMin\": 300,\n" +
                "    \"thresholdMax\": 600,\n" +
                "    \"describe\": \"用于登录场景下的规则集\",\n" +
                "    \"verify\": 0,\n" +
                "    \"active\": false,\n" +
                "    \"addUser\": \"user001\",\n" +
                "    \"addTime\": \"2018-01-01 00:00:00\",\n" +
                "    \"pageSize\": null,\n" +
                "    \"pageNo\": null,\n" +
                "    \"uniqueCode\": null,\n" +
                "    \"parameters\": {\n" +
                "     \"innerIfType\": \"B7\",\n" +
                "     \"cid\": \"13306328903\",\n" +
                "     \"idNumber\": \"370404196212262212\",\n" +
                "     \"realName\": \"赵玉柏\"\n" +
                "  },\n" +
                "    \"ruleList\": [\n" +
                "        {\n" +
                "            \"id\": 10001,\n" +
                "            \"name\": \"规则集一\",\n" +
                "            \"rulesId\": 10001,\n" +
                "            \"decision\": \"高风险\",\n" +
                "            \"level\": 3,\n" +
                "            \"threshold\": 90,\n" +
                "            \"conditionNumber\": 2,\n" +
                "            \"describ\": null,\n" +
                "            \"active\": false,\n" +
                "            \"verify\": 0,\n" +
                "            \"addUser\": \"user001\",\n" +
                "            \"addTime\": \"2018-01-01 00:00:00\",\n" +
                "            \"conditionRelationship\": \"10001或10002\",\n" +
                "            \"uniqueCode\": 1,\n" +
                "            \"pageSize\": null,\n" +
                "            \"pageNo\": null,\n" +
                "            \"conditionsList\": [\n" +
                "                {\n" +
                "                    \"id\": 10001,\n" +
                "                    \"name\": \"规则集一\",\n" +
                "                    \"ruleId\": 10001,\n" +
                "                    \"rulesId\": 10001,\n" +
                "                    \"fieldRelationship\": \"10001 或 10002\",\n" +
                "                    \"describe\": \"用于登录场景下的规则集\",\n" +
                "                    \"active\": false,\n" +
                "                    \"verify\": 0,\n" +
                "                    \"addUser\": \"user001\",\n" +
                "                    \"addTime\": \"2018-01-01 00:00:00\",\n" +
                "                    \"uniqueCode\": 1,\n" +
                "                    \"pageSize\": null,\n" +
                "                    \"pageNo\": null,\n" +
                "                    \"fieldList\": [\n" +
                "                        {\n" +
                "                            \"id\": 10001,\n" +
                "                            \"fieldId\": 10001,\n" +
                "                            \"fieldName\": \"在网时长\",\n" +
                "                            \"rulesId\": 10001,\n" +
                "                            \"conditionId\": 10001,\n" +
                "                            \"operator\": \"=\",\n" +
                "                            \"describ\": null,\n" +
                "                            \"active\": false,\n" +
                "                            \"verify\": 0,\n" +
                "                            \"addUser\": \"user001\",\n" +
                "                            \"addTime\": \"2018-01-01 00:00:00\",\n" +
                "                            \"value\": \"(24,+)\",\n" +
                "                            \"uniqueCode\": 1,\n" +
                "                            \"fieldType\": null,\n" +
                "                            \"pageSize\": null,\n" +
                "                            \"pageNo\": null\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";


        rulesConfig2 = "{\"id\":10001,\"name\":\"规则集一\",\"businessId\":10001,\"senceId\":10001,\"matchType\":false,\"thresholdMin\":300,\"thresholdMax\":600,\"describe\":\"用于登录场景下的规则集\",\"verify\":0,\"active\":false,\"addUser\":\"user001\",\"addTime\":1514736000000,\"pageSize\":null,\"pageNo\":null,\"uniqueCode\":null,\"parameters\":{\"realName\":\"\",\"idNumber\":\"\",\"cid\":\"\"},\"ruleList\":[{\"id\":10001,\"name\":\"规则集一\",\"rulesId\":10001,\"decision\":\"高风险\",\"threshold\":90,\"level\":3,\"conditionNumber\":2,\"describ\":null,\"active\":false,\"verify\":0,\"addUser\":\"user001\",\"addTime\":1514736000000,\"conditionRelationship\":\"10001或10002\",\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"conditionsList\":[{\"id\":10001,\"name\":\"规则集一\",\"ruleId\":10001,\"rulesId\":10001,\"fieldRelationship\":\"10001 或 10002\",\"describe\":\"用于登录场景下的规则集\",\"active\":false,\"verify\":0,\"addUser\":\"user001\",\"addTime\":1514736000000,\"uniqueCode\":1,\"pageSize\":null,\"pageNo\":null,\"fieldList\":[{\"id\":10001,\"fieldId\":10001,\"fieldName\":\"在网时长\",\"rulesId\":10001,\"conditionId\":10001,\"operator\":\"=\",\"describ\":null,\"active\":false,\"verify\":0,\"addUser\":\"user001\",\"addTime\":1514736000000,\"value\":\"(24,+)\",\"uniqueCode\":1,\"fieldType\":null,\"pageSize\":null,\"pageNo\":null}]}]}]}";

        rulesConfig2 = "{\n" +
                "\t\"id\": 10001,\n" +
                "\t\"matchType\": 0,\n" +
                "\t\"name\": \"验证个人信息\",\n" +
                "\t\"parameters\": {\n" +
                "\t\t\"cid\": \"13306328903\",\n" +
                "\t\t\"encrypted\": 0,\n" +
                "\t\t\"idNumber\": \"370404196212262212\",\n" +
                "\t\t\"realName\": \"赵玉柏\"\n" +
                "\t},\n" +
                "\t\"ruleList\": [{\n" +
                "\t\t\"conditionRelationShip\": \"10001&&10002\",\n" +
                "\t\t\"conditionsList\": [{\n" +
                "\t\t\t\"fieldList\": [{\n" +
                "\t\t\t\t\"fieldId\": \"10004\",\n" +
                "\t\t\t\t\"fieldName\": \"onlineTime\",\n" +
                "\t\t\t\t\"fieldType\": \"string\",\n" +
                "\t\t\t\t\"id\": 10001,\n" +
                "\t\t\t\t\"operator\": \"==\",\n" +
                "\t\t\t\t\"parameter\": \"(24,+)\"\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"fieldId\": \"10005\",\n" +
                "\t\t\t\t\"fieldName\": \"onlineStatus\",\n" +
                "\t\t\t\t\"fieldType\": \"string\",\n" +
                "\t\t\t\t\"id\": 10002,\n" +
                "\t\t\t\t\"operator\": \"!=\",\n" +
                "\t\t\t\t\"parameter\": \"0\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"fieldRelationShip\": \"10001&&10002\",\n" +
                "\t\t\t\"id\": 10001,\n" +
                "\t\t\t\"name\": \"在网时长大于24个月\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"fieldList\": [{\n" +
                "\t\t\t\t\"fieldId\": \"10008\",\n" +
                "\t\t\t\t\"fieldName\": \"outOfServiceTimes\",\n" +
                "\t\t\t\t\"fieldType\": \"int\",\n" +
                "\t\t\t\t\"id\": 10003,\n" +
                "\t\t\t\t\"operator\": \"==\",\n" +
                "\t\t\t\t\"parameter\": \"0\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"fieldRelationShip\": \"10003\",\n" +
                "\t\t\t\"id\": 10002,\n" +
                "\t\t\t\"name\": \"三个月内手机停机次数等于0\"\n" +
                "\t\t}],\n" +
                "\t\t\"id\": 10001,\n" +
                "\t\t\"level\": 3,\n" +
                "\t\t\"name\": \"手机在网时长大于2个月\",\n" +
                "\t\t\"score\": 0,\n" +
                "\t\t\"threshold\": 90\n" +
                "\t}],\n" +
                "\t\"score\": 0,\n" +
                "\t\"status\": 0,\n" +
                "\t\"thresholdMax\": 600,\n" +
                "\t\"thresholdMin\": 300\n" +
                "}";
        // rulesConfig2 = RulesConfig.getRulesConfig2();
        Rules rules = JSON.parseObject(rulesConfig2 ,Rules.class);

        String rulesDataStr = JSON.toJSONString(rules);

        System.out.println("rulesConfig2:");
        System.out.println(rulesConfig2);
        System.out.println("rulesDataStr:");
        System.out.println(rulesDataStr);

        try{
            String result = getRulesSourceData(rulesConfig2);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从数据源补充字段数据到RulesConfig
     * @param rulesConfig
     * @return
     */
    public static String getRulesSourceData(String rulesConfig) throws Exception{

        //rulesConfig = RulesConfig.getErrorRulesConfig();

        SourceMapper sourceMapper = new SourceMapper();
        Long[] rulesFields = SourceMapper.getFieldIds(rulesConfig);
        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);
        Parameters _parameters = rules.getParameters();
        String parameters = JSONObject.toJSONString(_parameters);

        /* 获取字段对应的接口集合 */
        List<EngineInter> rulesInter = sourceMapper.getRulesInter(rulesFields);

        /* 创建数据字典 */
        Map<String, Map> rulesData= new HashMap<String, Map>();
        Map<String, Object> rulesRawData= new HashMap<String, Object>();

        /* 获取所有接口数据 */
        Map<String, Map> interData = new HashMap<>();
        String interParseData = "";

        for(EngineInter inter: rulesInter) {
        	String requestType = inter.getRequestType();
            String interName = inter.getName();
            InterfaceService interfaceService = new InterfaceServiceImpl();
            interData = interfaceService.getInterfaceDataMap(interName, parameters, 1L);
            // interParseData = ResponseParser.getParseData(interName, parameters, interData);
            // rulesRawData.put(interName, interData);
            rulesData.putAll(interData);
        }

        String rulesDataStr = JSON.toJSONString(rulesData);

        /* 解析接口数据 */
        rulesConfig = SourceFactory.rulesDataPackager(rulesConfig, rulesData);

        // todo: 临时使用
        System.out.println("返回结果：");
        System.out.println(rulesDataStr);
        System.out.println("rulesConfig：");
        System.out.println(rulesConfig);

        return  rulesConfig;

    }

}
