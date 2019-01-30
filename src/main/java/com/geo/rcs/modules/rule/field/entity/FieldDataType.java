package com.geo.rcs.modules.rule.field.entity;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月03日 上午11:02
 */
public class FieldDataType {
    //类型编号
    private Integer id;
    //类型名称
    private String typeName;
    //父级编号
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
