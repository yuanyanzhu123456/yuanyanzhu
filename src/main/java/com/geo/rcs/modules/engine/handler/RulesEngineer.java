package com.geo.rcs.modules.engine.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.engine.drools.DroolsRunner;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.engine.entity.RulesConfig;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.engine.util.DatetimeFormattor;

import org.kie.api.runtime.rule.ConsequenceException;

public class RulesEngineer {

    public static void main(String[] args) {

        // 规则文件
        String rulesConfig2 = RulesConfig.getRulesConfig();

        String start = DatetimeFormattor.now();
        System.out.println("[RCS-INFO]:START" + start);

        for(int i=0; i<1; i++) {
            try {
                String Result = runner(rulesConfig2);
                System.out.println("[RCS-INFO]:RESULT" + Result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String end = DatetimeFormattor.now();
        System.out.println(String.format("[RCS-INFO]:END COST：%s -  %s",  start, end));
    }

    public static String runner(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);

        try{
            String  rulesId = JSONObject.parseObject(rulesConfig).getString("id");
            String rulesContent = RulesConfigParser.rulesParserToContent(rulesConfig);
            String rulesFileId = RulesConfigParser.rulesParserToFile(rulesId, rulesContent);
            String ruleFile = String.format("%s", rulesFileId);
            System.out.println("[RCS-INFO]:RULE FILE："  + ruleFile);

            // 静态加载规则-测试使用
            // DroolsRunner.runStaticRules("rulesDemo.drl", rules);

            // 动态加载规则(自带KFS生成文件)
            DroolsRunner.runDynamicRules(rulesContent, rulesFileId, rules);

            return checkRulesResult(JSONObject.toJSONString(rules));

        }catch (RcsException e) {
            e.printStackTrace();
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(e.getMsg());
        }catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
            // throw new RcsException(StatusCode.RULE_ERROR.getMessage(), StatusCode.RULE_ERROR.getCode());
        }catch (ConsequenceException e) {
            e.printStackTrace();
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
            e.printStackTrace();
            // throw new RcsException(StatusCode.RULE_ERROR.getMessage(), StatusCode.RULE_ERROR.getCode());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
        }

        return JSONObject.toJSONString(rules);
    }

    public static String run(String rulesConfig) throws Exception{

        String  rulesId = JSONObject.parseObject(rulesConfig).getString("id");
        String rulesContent = RulesConfigParser.rulesParserToContent(rulesConfig);
        String rulesFileId = RulesConfigParser.rulesParserToFile(rulesId, rulesContent);
        String ruleFile = String.format("%s", rulesFileId);

        System.out.println("引擎生成规则文件2："  + ruleFile);

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);

        // 动态加载规则(自带KFS生成文件)
        DroolsRunner.runDynamicRules(rulesContent, rulesFileId, rules);

        String rulesResult = checkRulesResult(JSONObject.toJSONString(rules));

        return JSONObject.toJSONString(rules);
    }


    private static String checkRulesResult(String rules) {

        Rules ruleEntity = JSON.parseObject(rules, Rules.class);
        System.out.println("RULE：");
        if(ruleEntity.getStatus()==RulesEngineCode.INIT.getCode()){
            String message = "规则运行状态异常，规则未执行";
            throw new RcsException(StatusCode.RULE_EXCUTE_ERROR.getMessage(), StatusCode.RULE_EXCUTE_ERROR.getCode());
        }else{
            String message = "[RCS-INFO]:RUN FINISH：";
            System.out.println(message);
        }

        return rules;
    }



}
