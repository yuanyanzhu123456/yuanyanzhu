package com.geo.rcs.modules.source.handler;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.modules.engine.entity.*;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceMapper {


    public static void  main(String[] args){

        System.out.println("字段解析成接口");

        String rulesConfig2 = RulesConfig.getRulesConfig();
        SourceMapper mapper = new SourceMapper();

    }

    /**
     * Get rules field ids
     * @param rulesConfig
     * @return
     */
    public static Long[] getFieldIds(String rulesConfig){

        Rules rules = JSON.parseObject(rulesConfig, Rules.class);
        List<Rule> ruleSet = rules.getRuleList();
        List<Long> fieldSet = new ArrayList();

        for (Rule rule:ruleSet) {
            List<Condition> conSet = rule.getConditionsList();
            for (Condition con: conSet) {
                List<Field> _fieldSet = con.getFieldList();
                for (Field field:_fieldSet) {
                    fieldSet.add(field.getId());
                }
            }
        }

        System.out.println(fieldSet);
        Long[] ids = fieldSet.toArray(new Long[fieldSet.size()]);

        return ids;
    }

    /**
     * Get interList by fieldIds
     * @param rulesFieldIds
     * @return
     */
    public List<EngineInter> getRulesInter(Long[] rulesFieldIds){

        System.out.println(Arrays.toString(rulesFieldIds));

//        List interById = fieldService.findInterById(rulesFieldIds);

//        Long[] ids = {10001L,10002L};
////        return fieldService.findInterById(ids);
//        FieldController fieldController = new FieldController();
//        return fieldController.test();

        /* 模拟接口数据 */
        List<EngineInter> interById = new ArrayList<>();

        EngineInter e1 = new EngineInter();
        e1.setId(10001L);
        e1.setName("A3");
        interById.add(e1);

        EngineInter e2 = new EngineInter();
        e2.setId(10002L);
        e2.setName("A4");
        interById.add(e2);

        EngineInter e3 = new EngineInter();
        e3.setId(10002L);
        e3.setName("B13");
        interById.add(e3);

        System.out.println(interById);
        return interById;

    }

}
