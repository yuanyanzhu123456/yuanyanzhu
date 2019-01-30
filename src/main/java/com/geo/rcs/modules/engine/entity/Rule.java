package com.geo.rcs.modules.engine.entity;

import java.util.List;

public class Rule {

    private Long id;

    private String  name;

    private int threshold;

    private int score;

    private int level;

    private Boolean result;

    private String conditionRelationShip;

    private List<Condition> conditionsList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getConditionRelationShip() {
        return conditionRelationShip;
    }

    public void setConditionRelationShip(String conditionRelationShip) {
        this.conditionRelationShip = conditionRelationShip;
    }

    public List<Condition> getConditionsList() {
        return conditionsList;
    }

    public void setConditionsList(List<Condition> conditionsList) {
        this.conditionsList = conditionsList;
    }
}
