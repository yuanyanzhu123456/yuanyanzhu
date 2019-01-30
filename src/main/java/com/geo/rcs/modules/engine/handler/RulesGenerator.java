package com.geo.rcs.modules.engine.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.modules.engine.entity.*;
import com.geo.rcs.modules.engine.util.StrSubstitutor;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  yongmingz
 * Created on : 2018.1.8
 */

public class RulesGenerator {


    public static String generateRulesContent(String rulesConfig) throws Exception{

        Rules rulesEntity = JSON.parseObject(rulesConfig ,Rules.class);
        System.out.println("规则配置数据解析完成：");
        System.out.println("规则配置ID：" + rulesEntity.getId());
        System.out.println("规则配置名称：" + rulesEntity.getName());
        System.out.println("规则配置匹配规则：" + rulesEntity.getMatchType());
        System.out.println("规则配置最大阈值：" + rulesEntity.getThresholdMax());
        System.out.println("规则配置最小阈值：" + rulesEntity.getThresholdMin());
        System.out.println("规则配置原始数据：" + JSONObject.toJSONString(rulesEntity));

        String rulesHeader = generateRulesHeader(rulesEntity.getId());
        String rulesMain = generateRulesMain(rulesEntity);
        String rulesBody = generateRulesBody(rulesEntity);

        String rulesContent = String.join("\n", rulesHeader, rulesMain, rulesBody); //Todo

        return rulesContent;
    }

    /***
     * Generate Rules Header
     */
    public static String generateRulesHeader(long rulesId) {

        String rulesHeader = MessageFormat.format(RulesBuilderFactory.getRulesTemplateHeader(), String.valueOf(rulesId));
        if(rulesHeader != null && rulesHeader.length() !=0){
            return rulesHeader;
        }else{
            throw new RcsException(StatusCode.RULE_GENERATE_ERROR.getMessage(),StatusCode.RULE_GENERATE_ERROR.getCode());
        }
    }

    /***
     * Generate Rules Main
     */
    public static String generateRulesMain(Rules rulesEntity) throws Exception{

        String ruleMainTemplate = RulesBuilderFactory.getRulesTemplateRulesBlock();

        Long id = rulesEntity.getId();
        int thresholdMin = rulesEntity.getThresholdMin();
        int thresholdMax = rulesEntity.getThresholdMax();
        List<Rule> rules = rulesEntity.getRuleList();
        int _matchType = rulesEntity.getMatchType();

        String matchType = RulesBuilderFactory.getRulesTemplateAvgMatch(rules, thresholdMin, thresholdMax);

        // GET MatchType
        switch(_matchType){
            case 0:
                matchType = RulesBuilderFactory.getRulesTemplateAvgMatch(rules, thresholdMin, thresholdMax); break;
            case 1:
                matchType = RulesBuilderFactory.getRulesTemplateWorstMatch(rules); break;
            case 2:
                matchType = RulesBuilderFactory.getRulesTemplateWorstMatch(rules); break;
            default:
                throw new RcsException(StatusCode.RULE_GENERATE_ERROR.getMessage(),StatusCode.RULE_GENERATE_ERROR.getCode());
        }

        Map<String, String> rulesMap = new HashMap<String, String>();
        rulesMap.put("rulesId", String.valueOf(id));
        rulesMap.put("rulesMatchType", matchType);
        StrSubstitutor sub = new StrSubstitutor(rulesMap);

        String ruleMain = sub.replace(ruleMainTemplate);

        return ruleMain;
    }

    /***
     * Generate Rules RulesBody
     */
    public static String generateRulesBody(Rules rulesEntity) throws Exception{

        // RulesBuilderFactory Rbuilder = new RulesBuilderFactory();

        String rulesBody = "";
        Long id= rulesEntity.getId();
        List<Rule> ruleSet = rulesEntity.getRuleList();
        String ruleTemplate = RulesBuilderFactory.getRulesTemplateRuleBlock();
        String conTemplate  = RulesBuilderFactory.getRulesTemplateConditionBlock();
        String fieldTemplate = RulesBuilderFactory.getRulesTemplateFieldBlock();

        Map<String, String> ruleMap = new HashMap<String, String>();
        ruleMap.put("rulesId", String.valueOf(rulesEntity.getId()));

        for(int i=0; i<ruleSet.size(); i++){

            Rule rule = ruleSet.get(i);
            String _ruleBody = "";
            Long ruleId = rule.getId();
            List<Condition> conditionSet = rule.getConditionsList();
            String conditionRelationShip = RulesValidateHandler.relationShipValidate(rule.getConditionRelationShip());  //todo

            conditionRelationShip = RulesBuilderFactory.getRulesTemplateConditionRelationShip(conditionSet, i, conditionRelationShip);

            ruleMap.put("ruleId", String.valueOf(ruleId));
            ruleMap.put("ruleIndex", String.valueOf(i));
            ruleMap.put("conditionRelationShip", conditionRelationShip);
            StrSubstitutor sub = new StrSubstitutor(ruleMap);
            _ruleBody = sub.replace(ruleTemplate);
            rulesBody = String.join("", rulesBody, _ruleBody);

            for(int j=0; j<conditionSet.size(); j++){
                Condition con = conditionSet.get(j);
                String _conBody = "";
                Long conditionId = con.getId();
                List<Field> fieldSet = con.getFieldList();
                String fieldRelationShip = RulesValidateHandler.relationShipValidate(con.getFieldRelationShip());

                fieldRelationShip = RulesBuilderFactory.getRulesTemplateFieldRelationShip(i, j, fieldSet, fieldRelationShip);

                ruleMap.put("conditionId", String.valueOf(conditionId));
                ruleMap.put("conditionIndex", String.valueOf(j));
                ruleMap.put("fieldRelationShip", fieldRelationShip);
                StrSubstitutor ruleSub = new StrSubstitutor(ruleMap);
                _conBody = ruleSub.replace(conTemplate);
                rulesBody =String.join("", rulesBody, _conBody);

                for(int k=0; k<fieldSet.size(); k++){
                    Field field = fieldSet.get(k);
                    String _fieldBody = "";
                    Long fieldId = field.getId();
                    String type = field.getFieldType();
                    String operator = RulesValidateHandler.fieldOperateValidate(field.getOperator());
                    String fieldOperator = RulesBuilderFactory.getRulesTemplateFieldOperator(i, j, k, type, operator);

                    ruleMap.put("fieldId", String.valueOf(fieldId));
                    ruleMap.put("fieldIndex", String.valueOf(k));
                    ruleMap.put("fieldOperator", fieldOperator);
                    ruleMap.put("fieldRelationShip", fieldRelationShip);

                    StrSubstitutor fieldSub = new StrSubstitutor(ruleMap);
                    _fieldBody = fieldSub.replace(fieldTemplate);

                    rulesBody =String.join("", rulesBody, _fieldBody);
                }
            }
        }

        return rulesBody;

    }

}
