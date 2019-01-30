package com.geo.rcs.modules.rule.analysis;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.analysis
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年06月06日 下午3:09
 */
public class Analysis {

    //规则命中数
    private String ruleHitNum;
    //规则命中率
    private String ruleHitRate;
    //字段命中数
    private String fieldHitNum;
    //字段命中率
    private String fieldHitRate;

    public String getRuleHitNum() {
        return ruleHitNum;
    }

    public void setRuleHitNum(String ruleHitNum) {
        this.ruleHitNum = ruleHitNum;
    }

    public String getRuleHitRate() {
        return ruleHitRate;
    }

    public void setRuleHitRate(String ruleHitRate) {
        this.ruleHitRate = ruleHitRate;
    }

    public String getFieldHitNum() {
        return fieldHitNum;
    }

    public void setFieldHitNum(String fieldHitNum) {
        this.fieldHitNum = fieldHitNum;
    }

    public String getFieldHitRate() {
        return fieldHitRate;
    }

    public void setFieldHitRate(String fieldHitRate) {
        this.fieldHitRate = fieldHitRate;
    }
}
