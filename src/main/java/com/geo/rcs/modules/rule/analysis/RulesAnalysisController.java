package com.geo.rcs.modules.rule.analysis;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.modules.engine.entity.Condition;
import com.geo.rcs.modules.rule.condition.entity.Conditions;
import com.geo.rcs.modules.rule.entity.EngineRule;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.analysis
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年06月05日 下午3:04
 */
@RestController
@RequestMapping("/rule")
public class RulesAnalysisController {

    @RequestMapping("/analysis")
    public Geo analysis(HttpServletRequest request, HttpServletResponse response, List<String> resultMap){

        Analysis analysis = new Analysis();

        for (int i =0;i<resultMap.size();i++) {

            EngineRules engineRules = JSON.parseObject(resultMap.get(i) ,EngineRules.class);

            //遍历规则
            for (EngineRule engineRule : engineRules.getRuleList()) {
                //计算规则命中数
                if(engineRule.getResult() == "true"){
                    analysis.setRuleHitNum(analysis.getRuleHitNum()+1);

                    //遍历条件
                    for (Conditions conditions : engineRule.getConditionsList()) {
                    //遍历字段
                    for (EngineField engineField : conditions.getFieldList()){

                        if(engineField.getResult() == "true"){
                            analysis.setFieldHitNum(analysis.getFieldHitNum()+1);
                        }
                    }
                }

                }

            }
        }
        return Geo.ok();
    }
}
