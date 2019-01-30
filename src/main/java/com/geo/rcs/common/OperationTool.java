//package com.geo.rcs.common;
//
//import com.geo.rcs.modules.engine.entity.Condition;
//import com.geo.rcs.modules.engine.entity.Field;
//import com.geo.rcs.modules.engine.entity.Rule;
//import com.geo.rcs.modules.engine.entity.Rules;
//
///**
// * @ Author     ：wp.
// * @ Date       ：Created in 10:36 2019/1/21
// */
//public class OperationTool {
//
//    public static Rules runner(Rules rules){
//        if(rules == null || rules.getRuleList() == null || rules.getRuleList().isEmpty()){
//            return null;
//        }
//        for(Rule rule : rules.getRuleList()){
//            if(rule.getConditionsList()==null || rule.getConditionsList().isEmpty()){
//                continue;
//            }
//            String conditionRelationShip = rule.getConditionRelationShip();
//            for (Condition conditions : rule.getConditionsList()){
//                if(conditions.getFieldList()==null || conditions.getFieldList().isEmpty()){
//                    continue;
//                }
//                String fieldRelationShip = conditions.getFieldRelationShip();
//                for(Field field : conditions.getFieldList()){
//                    switch (field.getOperator()){
//                        case "==":
//                            field.setResult(field.getValue().equals(field.getParameter()));
//                            break;
//                        case ">=":
//                            field.setResult(Long.valueOf(field.getValue())>=Long.valueOf(field.getParameter()));
//                            break;
//                        case "<=":
//                            field.setResult(Long.valueOf(field.getValue())<=Long.valueOf(field.getParameter()));
//                            break;
//                        case "!=":
//                            field.setResult(!field.getValue().equals(field.getParameter()));
//                            break;
//                        case "isnull":
//                            field.setResult(field.getValue() == null || field.getValue().equals(""));
//                            break;
//                        case "notnull":
//                            field.setResult(field.getValue() != null && !field.getValue().equals(""));
//                            break;
//                    }
//                    fieldRelationShip = fieldRelationShip.replace(field.getFieldId().toString(),field.getResult().equals("true")?"t":"f");
//                }
//                conditions.setResult(substringRelationship(conditions.getFieldRelationShip()));
//                conditionRelationShip.replace(conditions.getId().toString(),conditions.getResult().equals("true")?"t":"f")
//            }
//            rule.setResult(substringRelationship(rule.getConditionRelationShip()));
//        }
//        return rules;
//    }
//
//    private static boolean substringRelationship(String relationship){
//        while(relationship.lastIndexOf("(")>0){
//            String bracketRelationship = relationship.substring(relationship.lastIndexOf("("));
//            bracketRelationship = bracketRelationship.substring(0,bracketRelationship.indexOf(")")+1);
//            relationship.replace(bracketRelationship,onRelationship(bracketRelationship)?"t":"f");
//        }
//        return onRelationship(relationship);
//    }
//
//    private static boolean onRelationship(String relationship){
//        boolean begin = true;
//        boolean beginFlag = true;
//        char[] chars = relationship.toCharArray();
//        boolean flag = false;
//        char parameter = 0;
//        for(char c:chars){
//            if(c == '(') continue;
//            if(c == '!'){
//                flag = true;
//                continue;
//            }
//            switch (c){
//                case 't':
//                    boolean t = true;
//                    if(flag){
//                        flag = false;
//                        t = false;
//                    }
//                    if(beginFlag){
//                        beginFlag = false;
//                        begin = t;
//                    } else {
//                        begin = operation(begin,t,parameter);
//                    }
//                    break;
//                case 'f':
//                    boolean f = false;
//                    if(flag){
//                        flag = false;
//                        f = true;
//                    }
//                    if(beginFlag){
//                        beginFlag = false;
//                        begin = f;
//                    } else {
//                        begin = operation(begin,f,parameter);
//                    }
//                    break;
//                case '&':
//                    parameter = '&';
//                    break;
//                case '|':
//                    parameter = '|';
//                    break;
//            }
//        }
//        return begin;
//    }
//
//    private static boolean operation(boolean begin, boolean to, char parameter){
//        switch (parameter){
//            case '&':
//                return begin && to;
//            case '|':
//                return begin || to;
//        }
//        return false;
//    }
//}
