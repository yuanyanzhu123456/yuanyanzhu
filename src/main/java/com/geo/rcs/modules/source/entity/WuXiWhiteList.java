package com.geo.rcs.modules.source.entity;

/**
 * Created by 曾志 on 2019/1/21.
 */
public class WuXiWhiteList {
    //规则编号
    private Long id;
    //姓名
    private String name;
    //身份证号
    private String idNumber;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
