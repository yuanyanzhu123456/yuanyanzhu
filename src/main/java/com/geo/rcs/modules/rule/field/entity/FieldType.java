package com.geo.rcs.modules.rule.field.entity;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月23日 下午3:37
 */
public class FieldType {
    //字段类型编号
    private Integer id;
    //类型名称
    private String typeName;
    //类型标识符
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
