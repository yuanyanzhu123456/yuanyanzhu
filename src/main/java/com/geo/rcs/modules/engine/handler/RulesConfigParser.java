package com.geo.rcs.modules.engine.handler;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.modules.engine.entity.RulesConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesConfigParser{

    public static String rulesConfig = RulesConfig.getRulesConfig_v2();

    public static void main(String[] args){

        System.out.println("Start RulesConfigParser ...");

        try{
            String  rulesId = JSONObject.parseObject(rulesConfig).getString("id");
            String rulesContent = RulesConfigParser.rulesParserToContent(rulesConfig);
            String rulesFileId = RulesConfigParser.rulesParserToFile(rulesId, rulesContent);
            System.out.println("rulesFileId:" + rulesFileId);
            System.out.println("rulesContent: \n"+ rulesContent);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("RulesConfigParser Error");
        }

        System.out.println("Stop RulesConfigParser ...");
    }


    /**
     * 规则集配置解析并生成备份文件
     * @param rulesId
     * @param rulesContent
     * @return 返回文件name
     * @throws Exception
     */
    public static String rulesParserToFile(String rulesId, String rulesContent) {

        System.out.println(rulesId);
        RulesFileReader.write(rulesId, rulesContent);

        // String readContent = RulesFileReader.read(rulesId);

        return String.valueOf(rulesId) + ".drl";
    }


    public static String rulesParserToContent(String rulesConfig) throws Exception{

        String rulesContent = RulesGenerator.generateRulesContent(rulesConfig);

        return rulesContent;
    }

    /**
     * 规则解析成MAP-暂未使用
     * @param rulesConfig
     * @return
     * @throws Exception
     */
    public static Map rulesParserToMap(String rulesConfig) {

        System.out.println(rulesConfig);

        String  rulesId = JSONObject.parseObject(rulesConfig).getString("id");
        String  rulesName = JSONObject.parseObject(rulesConfig).getString("name");
        String  rulesMatchType = JSONObject.parseObject(rulesConfig).getString("match_type");
        int  thresholdMin = JSONObject.parseObject(rulesConfig).getInteger("threshold_min");
        int  thresholdMax = JSONObject.parseObject(rulesConfig).getInteger("threshold_max");

        JSONArray rules = JSONObject.parseObject(rulesConfig).getJSONArray("rules");

        Map<String, Object> rulesMap = new HashMap<String, Object>();

        rulesMap.put("rulesId", rulesId);
        rulesMap.put("rulesName", rulesName);
        rulesMap.put("rulesMatchType", rulesMatchType);
        rulesMap.put("thresholdMin", thresholdMin);
        rulesMap.put("thresholdMax", thresholdMax);

        List<Object> _rules = new ArrayList<Object>();

        for(int i = 0; i < rules.size(); i++){
            Map<String, Object> _ruleMap = new HashMap<String, Object>();

            String rule = JSONObject.toJSONString(rules.get(i)); //{"name":"rule001", "id": "1"}
            String  ruleId = JSONObject.parseObject(rule).getString("id");
            String  ruleName = JSONObject.parseObject(rule).getString("name");

            _ruleMap.put("id",ruleId);
            _ruleMap.put("name",ruleName);
            _rules.add(_ruleMap);
        }
        rulesMap.put("rules", _rules);

        return rulesMap;
    }

}
