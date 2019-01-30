package com.geo.rcs.modules.rule.inter.entity;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.inter.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年12月08日 12:28
 */
public class EngineInterType {

    /**
     * 类型编号
     */
    private Integer id;
    /**
     * 类型名称
     */
    private String name;
    /**
     * 数据类型
     */
    private String dataType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
