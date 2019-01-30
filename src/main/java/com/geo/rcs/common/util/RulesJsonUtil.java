package com.geo.rcs.common.util;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import org.apache.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.util
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月30日 下午2:07
 */
public class RulesJsonUtil extends BaseController{

    public boolean rulesJsonCheck(EngineRules engineRules, HttpServletRequest request, HttpServletResponse response){

        if(engineRules == null){
            this.sendError(request,response,"当前规则集为空，不能导入！");
            return false;
        }
        else if(engineRules.getRuleList() == null){
            this.sendError(request,response,"当前规则集规则为空，不能导入！");
            return false;
        }
        List<EngineRule> ruleList = engineRules.getRuleList();
        for (EngineRule engineRule : ruleList) {
            if(engineRule.getConditionRelationship() == null || engineRule.getConditionRelationship() == ""){
                this.sendError(request,response,"当前规则集条件关联关系为空，不能导入！");
                return false;
            }
            List<Conditions> conditionsList = engineRule.getConditionsList();
            if(conditionsList == null){
                this.sendError(request,response,"当前规则集条件为空，不能导入！");
                return false;
            }
            for (Conditions conditions : conditionsList) {
                if(conditions.getFieldRelationship() == null || conditions.getFieldRelationship() ==""){
                    this.sendError(request,response,"当前规则集字段关联关系为空，不能导入！");
                    return false;
                }
                if(conditions.getFieldList() == null){
                    this.sendError(request,response,"当前规则集字段为空，不能导入！");
                    return false;
                }
            }
        }
        return true;
    }
}
