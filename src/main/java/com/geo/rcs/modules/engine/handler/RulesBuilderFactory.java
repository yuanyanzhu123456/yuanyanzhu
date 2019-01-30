package com.geo.rcs.modules.engine.handler;

import com.geo.rcs.modules.engine.entity.Condition;
import com.geo.rcs.modules.engine.entity.Field;
import com.geo.rcs.modules.engine.entity.Rule;

import java.util.List;

public class RulesBuilderFactory {

    /***
     * Drools Rules Template Header
     */
    private static String RulesTemplateHeader =
            "package rules{0};\n" +
            "\n" +
            "import com.geo.rcs.modules.engine.entity.Rules;\n" +
            "import com.geo.rcs.modules.engine.util.EngineCalculator;\n" +
            "import java.text.SimpleDateFormat;\n" +
            "import static java.lang.Math.*;\n" +
            "import java.util.Collections;\n" +
            "import java.util.List\n" +
            "import java.util.Date;\n" +
            "dialect \"mvel\"\n" +
            "\n" +
            "//declare any global variables here";

    /***
     * Drools Rules Template Methods
     * @Todo: Dropped from V2.1.0
     * @Change to : Use com.geo.rcs.modules.engine.util.EngineCalculator
     */
    private static String RuleTemplateMethods =
            "function int Not(int res) {\n" +
            "    if(res==1){\n" +
            "        res = 0;\n" +
            "    }else{\n" +
            "        res = 1;\n" +
            "    }\n" +
            "    return res;\n" +
            "}\n" +
            "\n" +
            "function int compareDate( String startDate, String endDate ){\n" +
            "\n" +
            "    SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd\");\n" +
            "\n" +
            "    Date start = sdf.parse(startDate);\n" +
            "    Date end = sdf.parse(endDate);\n" +
            "\n" +
            "    return start.compareTo(end);\n" +
            "}\n" +
            "\n" +
            "function int compareDatetime( String startDate, String endDate ){\n" +
            "\n" +
            "    SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n" +
            "\n" +
            "    Date start = sdf.parse(startDate);\n" +
            "    Date end = sdf.parse(endDate);\n" +
            "\n" +
            "    return start.compareTo(end);\n" +
            "}\n" +
            "\n" +
            "function int diffToday( String recentDate ){\n" +
            "\n" +
            "    SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd\");\n" +
            "\n" +
            "    Date today = new Date();\n" +
            "    Date end = sdf.parse(recentDate);\n" +
            "\n" +
            "    long diff = today.getTime() - end.getTime();\n" +
            "    return (int)(diff/(1000*60*60*24));\n" +
            "}\n" +
            "\n" +
            "function int diffTodayTime( String recentDate ){\n" +
            "\n" +
            "    SimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n" +
            "\n" +
            "    Date today = new Date();\n" +
            "    Date end = sdf.parse(recentDate);\n" +
            "\n" +
            "    long diff = today.getTime() - end.getTime();\n" +
            "    return (int)(diff/(1000*60*60*24));\n" +
            "}\n" +
            "\n";

    /***
     * Drools Rules Template Rules Block
     * ${rulesId}: Rules id
     * ${rulesMatchType}: Rules MatchType
     */
    private static String RulesTemplateRulesBlock = "// [ruleSetId]:${rulesId}\n" +
            "rule \"rs${rulesId}\"\n" +
            "//no-loop\n" +
            " salience 1\n" +
            " when\n" +
            "  $rs:Rules($rsid:id, $s:status==0);\n" +
            " then\n" +
            "  System.out.println(\"[ruleSetId]：\" + $rsid);\n" +
            "  System.out.println(\"[ruleSet${rulesId} Score]：\" + $rs.score);\n" +
            "  System.out.println(\"[ruleSet${rulesId} Status]：\" + $rs.status);\n" +
            "  ${rulesMatchType}" +
            "  $rs.status = $rs.status==0?1:$rs.status; \n" +
            "  System.out.println(\"[ruleSet${rulesId} Status]：\" + $rs.status);\n" +
            "  System.out.println(\"[ruleSet${rulesId} Score]：\" + $rs.score);\n" +
            "//  update($rs);\n" +
            "end\n" +
            "\n";

    /***
     * Drools Rules Template Rule Block
     * ${ruleId}: Rules id
     * ${ruleIndex}: Rules List Index
     * ${conditionRelationShip}: Conditions Relation Ship
     */
    private static String RulesTemplateRuleBlock = "// RuleIndex:${ruleIndex}, RuleId:${ruleId}\n" +
            "rule \"rs${rulesId}_r${ruleId}\"\n" +
            "//no-loop\n" +
            " salience 100\n" +
            " when\n" +
            "  $r:Rules(status==0);\n" +
            " then\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId} threshold]：\" + $r.ruleList[${ruleIndex}].threshold);\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId} level]：\" + $r.ruleList[${ruleIndex}].level);\n" +
            "  ${conditionRelationShip}" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId} end result]：\" + $r.ruleList[${ruleIndex}].result);\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId} end score]：\" + $r.ruleList[${ruleIndex}].score);\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId} end level]：\" + $r.ruleList[${ruleIndex}].level);\n" +
            "//  update($r);\n" +
            "end\n" +
            "\n" ;

    /***
     * Drools Rules Template Condition Block
     * ${conditionId}: Condition id
     * ${conditionIndex}: Conditions List Index
     * ${fieldRelationShip}: field Relation Ship
     */
    private static String RulesTemplateConditionBlock = "// conditionIndex：${conditionIndex}, conditionId: ${conditionId},\n" +
            "rule \"rs${rulesId}_c${conditionId}\"\n" +
            "// no-loop\n" +
            " salience 1000\n" +
            " when\n" +
            "  $r:Rules(status==0);\n" +
            " then\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId}-condition${conditionId}-Start]：\" + $r.ruleList[${ruleIndex}].conditionsList[${conditionIndex}].result);\n" +
            "  ${fieldRelationShip} " +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId}-condition${conditionId}-Result]：\" + $r.ruleList[${ruleIndex}].conditionsList[${conditionIndex}].result);\n" +
            "//  update($r);\n" +
            "end\n" +
            "\n";

    /***
     * Drools Rules Template Field Block
     * ${fieldId}: Condition id
     * ${fieldIndex}: Conditions List Index
     * ${fieldOperator}: field Operator
     */
    private static String RulesTemplateFieldBlock = "// fieldIndex：${fieldIndex}, fieldId: ${fieldId} \n" +
            "rule \"rs${rulesId}_field${fieldId}\"\n" +
            "// no-loop\n" +
            " salience 10000\n" +
            " when\n" +
            "  $r:Rules(status==0);\n" +
            " then\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId}-condition${conditionId}-field${fieldId}-Value]：\" + $r.ruleList[${ruleIndex}].conditionsList[${conditionIndex}].fieldList[${fieldIndex}].value);\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId}-condition${conditionId}-field${fieldId}-Operator]：\" + $r.ruleList[${ruleIndex}].conditionsList[${conditionIndex}].fieldList[${fieldIndex}].operator);\n" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId}-condition${conditionId}-field${fieldId}-Parameter]：\" + $r.ruleList[${ruleIndex}].conditionsList[${conditionIndex}].fieldList[${fieldIndex}].parameter);\n" +
            "  ${fieldOperator}" +
            "  System.out.println(\"[ruleSet${rulesId}-rule${ruleId}-condition${conditionId}-field${fieldId}-Result]：\" + $r.ruleList[${ruleIndex}].conditionsList[${conditionIndex}].fieldList[${fieldIndex}].result);\n" +
            "//  update($r);\n" +
            "end\n" +
            "\n";

    private static String RulesTemplateAvgMatch = "";

    private static String RulesTemplateWorstMatch = "";

    private static String RulesTemplateConditionRelationShip = ""; //"$r.ruleList[0].score = $r.ruleList[0].threshold * ( $r.ruleList[0].conditionsList[0].result | $r.ruleList[0].conditionsList[1].result) ;  //条件之间做或逻辑\n";

    private static String RulesTemplateFieldRelationShip = "";// $r.ruleList[0].conditionsList[0].result= ( $r.ruleList[0].conditionsList[0].fieldList[0].result | $r.ruleList[0].conditionsList[0].fieldList[1].result); // 字段之间关系逻辑\n";

    private static String RulesTemplateFieldOperator = ""; //$r.ruleList[0].conditionsList[0].fieldList[0].result=($r.ruleList[0].conditionsList[0].fieldList[0].value <= $r.ruleList[0].conditionsList[0].fieldList[0].parameter ); //字段运算逻辑 \n";

    public static String getRulesTemplateHeader() {
        return RulesTemplateHeader;
    }


    public static String getRulesTemplateRulesBlock() {
        return RulesTemplateRulesBlock;
    }

    public static String getRulesTemplateRuleBlock() {
        return RulesTemplateRuleBlock;
    }

    public static String getRulesTemplateConditionBlock() {
        return RulesTemplateConditionBlock;
    }

    public static String getRulesTemplateFieldBlock() {
        return RulesTemplateFieldBlock;
    }

    public static String getRulesTemplateAvgMatch(List<Rule> rules, int thresholdMin, int thresholdMax) {

        String matchLogic = "";

        for(int i=0; i< rules.size(); i++){
            matchLogic = String.join("", matchLogic, i==0?String.format("$rs.ruleList[%d].score", i):
                    String.format(" + $rs.ruleList[%d].score", i));
        }
        String statusLogic = String.join("","$rs.status = $rs.score < $rs.thresholdMin ? 1:$rs.status;\n",
                "  $rs.status = $rs.score >= $rs.thresholdMin && $rs.score <= $rs.thresholdMax ? 2:$rs.status;\n",
                "  $rs.status = $rs.score > $rs.thresholdMax ? 3:$rs.status");

        String matchBody = String.format(" $rs.score = %s; \n  %s;\n", matchLogic, statusLogic);

        return matchBody;
    }

    public static String getRulesTemplateWorstMatch(List<Rule> rules) {

        String matchLogic="";
        String statusLogic="";

        for(int i=0; i< rules.size(); i++){
            matchLogic = String.join("", matchLogic, i==0 ? String.format("$rs.ruleList[%d].score", i):
                    String.format(" + $rs.ruleList[%d].score", i));
            /* 待优化，使用构建的min(list)替代代码
             * 此处的$rs.status结果状态计算依赖于规则的Score
             */
            statusLogic = String.join("", statusLogic, String.format(
                    "  $rs.status = ($rs.ruleList[%d].result ? $rs.ruleList[%d].level:0) > $rs.status?($rs.ruleList[%d].result ? " +
                            "$rs.ruleList[%d].level:0):$rs.status;\n",
                    i, i, i, i));
        }
        /* +0 防止单一规则出现语法错误：(rule) */
        String matchBody = String.format("$rs.score = %s; \n%s\n", matchLogic, statusLogic);

        return matchBody;
    }

    public static String getRulesTemplateConditionRelationShip(List<Condition> conditionSet, int ruleIndex,  String conditionRelationShip)  throws Exception{

        conditionRelationShip = parserConditionRelation(ruleIndex, conditionSet, conditionRelationShip);
        String relationShip = String.format(
                "// Notice：ruleScore = threshold * result（true/false?1:0 ==> 1/0 ） \n  " +
                "$r.ruleList[%d].result = %s  ; \n  " +
                        "$r.ruleList[%d].score = $r.ruleList[%d].threshold * ($r.ruleList[%d].result?1:0); \n",
                ruleIndex, conditionRelationShip, ruleIndex, ruleIndex, ruleIndex);

        return RulesValidateHandler.relationShipContentValidate(relationShip);
    }

    public static String getRulesTemplateFieldRelationShip( int ruleIndex, int conIndex, List<Field> fieldSet,String fieldRelationShip) throws Exception {

        fieldRelationShip = parserFieldRelation(ruleIndex, conIndex, fieldSet, fieldRelationShip);
        String relationShip = String.format("$r.ruleList[%d].conditionsList[%d].result = %s; \n ", ruleIndex, conIndex, fieldRelationShip);

        return RulesValidateHandler.relationShipContentValidate(relationShip);
    }

    public static String getRulesTemplateFieldOperator(int ruleIndex, int conIndex, int fieldIndex, String type, String operator)  throws Exception{

        String operatorBody = "";

        switch (type.toUpperCase()){
            case "INT":
                operatorBody = IntOperator(ruleIndex, conIndex, fieldIndex, type, operator);
                break;
            case "STRING":
                operatorBody = StringOperator(ruleIndex, conIndex, fieldIndex, type, operator);
                break;
            case "DATE":
                operatorBody = DateOperator(ruleIndex, conIndex, fieldIndex, type, operator);
                break;
            case "DATETIME":
                operatorBody = DatetimeOperator(ruleIndex, conIndex, fieldIndex, type, operator);
                break;
        }

        return RulesValidateHandler.fieldOperateContentValidate(operatorBody);
    }


    public static String parserConditionRelation(int ruleIndex, List<Condition> conditionSet, String conditionRelationShip)  throws Exception{

        for(int i=0; i<conditionSet.size(); i++){
            Long conId = conditionSet.get(i).getId();
            int conIndex = i;
            conditionRelationShip = conditionRelationShip.replace(String.valueOf(conId), String.format("$r.ruleList[%d].conditionsList[%d].result", ruleIndex, conIndex));
        }

        return RulesValidateHandler.relationShipContentValidate(conditionRelationShip);

    }

    public static String parserFieldRelation(int ruleIndex, int conIndex, List<Field> fieldSet, String fieldRelationShip) throws Exception{

        for(int i=0; i<fieldSet.size(); i++){
            Long fieldId = fieldSet.get(i).getId();
            int fieldIndex = i;
            fieldRelationShip = fieldRelationShip.replace(String.valueOf(fieldId), String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result", ruleIndex, conIndex, fieldIndex));
        }

        return RulesValidateHandler.relationShipContentValidate(fieldRelationShip);

    }

    public static String IntOperator(int ruleIndex, int conIndex, int fieldIndex, String type, String operator) throws Exception{

        String operatorBody = "";
        switch (operator){
            case "isnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0 ));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            case "notnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value != null && $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length != 0 ));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            default:
                for(String str: RulesValidateHandler.intBaseOperator) {
                    if(str.trim().contains(operator))
                        operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                        "$r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0? "+
                                        "false:(Double.parseDouble($r.ruleList[%d].conditionsList[%d].fieldList[%d].value) %s Double.parseDouble($r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter) );\n",
                                ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, operator, ruleIndex, conIndex, fieldIndex);
                }
                break;
        }

        return RulesValidateHandler.fieldOperateContentValidate(operatorBody);
    }

    public static String StringOperator(int ruleIndex, int conIndex, int fieldIndex, String type, String operator)  throws Exception{

        String operatorBody = "";
        switch (operator){
            case "isnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0 ));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            case "notnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value != null && $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length != 0 ));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            case "==":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "($r.ruleList[%d].conditionsList[%d].fieldList[%d].value.equals($r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            case "!=":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "!($r.ruleList[%d].conditionsList[%d].fieldList[%d].value.equals($r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
        }

        return RulesValidateHandler.fieldOperateContentValidate(operatorBody);
    }

    public static String DateOperator(int ruleIndex, int conIndex, int fieldIndex, String type, String operator)  throws Exception{

        String operatorBody = "";
        switch (operator){
            case "isnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0 ));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            case "notnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value != null && $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length != 0 ));\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;

            case "->":
            case "->=":
            case "-<":
            case "-<=":
            case "-==":
            case "-!=":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "$r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0? "+
                                "false:(EngineCalculator.diffToday($r.ruleList[%d].conditionsList[%d].fieldList[%d].value) %s $r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter);\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex, operator.substring(1), ruleIndex, conIndex, fieldIndex);
                break;
            default:
                for(String str: RulesValidateHandler.DateBaseOperator) {
                    if(str.trim().contains(operator))
                        operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                        "$r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0? "+
                                        "false:EngineCalculator.compareDate($r.ruleList[%d].conditionsList[%d].fieldList[%d].value, $r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter) %s 0;\n",
                                ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, operator);
                }
                break;
        }

        return RulesValidateHandler.fieldOperateContentValidate(operatorBody);
    }

    public static String DatetimeOperator(int ruleIndex, int conIndex, int fieldIndex, String type, String operator)  throws Exception{

        String operatorBody = "";
        switch (operator){
            case "isnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0 ) );\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;
            case "notnull":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "(($r.ruleList[%d].conditionsList[%d].fieldList[%d].value != null && $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length != 0 ) );\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex);
                break;

            case "->":
            case "->=":
            case "-<":
            case "-<=":
            case "-==":
            case "-!=":
                operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                "$r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0? "+
                                "false:(EngineCalculator.diffTodayTime($r.ruleList[%d].conditionsList[%d].fieldList[%d].value) %s $r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter);\n",
                        ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex, operator.substring(1), ruleIndex, conIndex, fieldIndex);
                break;

            default:
                for(String str: RulesValidateHandler.DateBaseOperator) {
                    if(str.trim().contains(operator))
                        operatorBody = String.format("$r.ruleList[%d].conditionsList[%d].fieldList[%d].result=" +
                                        "$r.ruleList[%d].conditionsList[%d].fieldList[%d].value == null || $r.ruleList[%d].conditionsList[%d].fieldList[%d].value.length == 0? "+
                                        "false:EngineCalculator.compareDatetime($r.ruleList[%d].conditionsList[%d].fieldList[%d].value, $r.ruleList[%d].conditionsList[%d].fieldList[%d].parameter) %s 0;\n",
                                ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex,  ruleIndex, conIndex, fieldIndex, ruleIndex, conIndex, fieldIndex, operator);
                }
                break;
        }

        return RulesValidateHandler.fieldOperateContentValidate(operatorBody);
    }

}
