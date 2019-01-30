package com.geo.rcs.modules.source.entity;

import java.io.Serializable;

/**
 * Created by 曾志 on 2019/1/18.
 */
public class WuXi implements Serializable {

    //规则编号
    private Long id;
    //模拟返回参数
    private String xml;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
