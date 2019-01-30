package com.geo.rcs.modules.engine.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.engine.entity.Rules;
import com.geo.rcs.modules.engine.entity.RulesEngineCode;
import com.geo.rcs.modules.engine.handler.RulesConfigParser;
import com.geo.rcs.modules.engine.service.DroolsService;
import com.geo.rcs.modules.engine.service.EngineService;
import org.kie.api.runtime.rule.ConsequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.engine.service.impl
 * @Description : RuleEngineer
 * @Author yongmingz
 * @email yongmingz@geotmt.com
 * @Creation Date : 2018.1.11
 */

@Service
public class EngineServiceImpl implements EngineService{


    @Autowired
    private DroolsService droolsService;


    /**
     * Run and get RulesEngine result
     * @param rulesConfig
     * @return
     */

    @Override
    public String getRulesRes(String rulesConfig) throws Exception {
        System.out.println("[RCS-INFO]:规则引擎核心启动运行！");

        long point4 = System.currentTimeMillis();

        // 规则文件
        // String rulesConfig2 = RulesConfig.getRulesConfig();

        String Result = runner(rulesConfig);

        System.out.println("[RCS-INFO]:规则引擎返回运算结果：" + Result);

        System.out.println("[RCS-INFO]:规则引擎核心运行结束！" +(System.currentTimeMillis()-point4)) ;

        return Result;
    }



    @Override
    public String updateResStatusToInvalid(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);
        rules.setStatus(RulesEngineCode.INVALID.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String updateResStatusToHuman(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);
        rules.setStatus(RulesEngineCode.HUMAN.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String updateResStatusToPass(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);
        rules.setStatus(RulesEngineCode.PASS.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String updateResStatusToReject(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);
        rules.setStatus(RulesEngineCode.REJECT.getCode());
        rulesConfig = JSONObject.toJSONString(rules);

        return rulesConfig;
    }

    @Override
    public String runner(String rulesConfig) {

        Rules rules = JSON.parseObject(rulesConfig ,Rules.class);

        try{
            String  rulesId = JSONObject.parseObject(rulesConfig).getString("id");
            String rulesContent = RulesConfigParser.rulesParserToContent(rulesConfig);
            String rulesFileId = RulesConfigParser.rulesParserToFile(rulesId, rulesContent);
            String ruleFile = String.format("%s", rulesFileId);
            System.out.println("[RCS-INFO]:RULE FILE："  + ruleFile);

            // 静态加载规则-测试使用
//            droolsService.runStaticRules("rulesDemo.drl", rules);

            // 动态加载规则(自带KFS生成文件)
//            droolsService.runDynamicRules(rulesContent, rulesFileId, rules);

            // 缓存加载规则(自带KFS生成文件)
            droolsService.runDynamicCacheRules(rulesContent, rulesFileId, rules);

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
        }catch (ConsequenceException e) {
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getClass());
            rules.setStatus(RulesEngineCode.INVALID.getCode());
            rules.setReason(StatusCode.RULE_BUILD_ERROR.getMessage());
        }

        return JSONObject.toJSONString(rules);
    }

    private String checkRulesResult(String rules) {

        Rules ruleEntity = JSON.parseObject(rules, Rules.class);
        System.out.println("RULE：");
        if(ruleEntity.getStatus()==RulesEngineCode.INIT.getCode()){
            throw new RcsException(StatusCode.RULE_EXCUTE_ERROR.getMessage(), StatusCode.RULE_EXCUTE_ERROR.getCode());
        }else{
            String message = "[RCS-INFO]:RUN FINISH：";
            System.out.println(message);
        }

        return rules;
    }
}
