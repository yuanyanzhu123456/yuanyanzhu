package com.geo.rcs.modules.engine.entity;

import java.util.List;

public class Condition {

    private Long id;

    private String  name;

    private Boolean  result;

    private String  fieldRelationShip;

    private List<Field> fieldList;

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

    public String getFieldRelationShip() {
        return fieldRelationShip;
    }

    public void setFieldRelationShip(String fieldRelationShip) {
        this.fieldRelationShip = fieldRelationShip;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
