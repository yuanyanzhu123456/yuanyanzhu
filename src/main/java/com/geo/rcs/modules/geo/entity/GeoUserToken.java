package com.geo.rcs.modules.geo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.geo.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年02月27日 下午12:25
 */
public class GeoUserToken implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;
    //用户ID
    private Long geoId;
    //token
    private String token;
    //过期时间
    private Date expireTime;
    //更新时间
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGeoId() {
        return geoId;
    }

    public void setGeoId(Long geoId) {
        this.geoId = geoId;
    }

    /**
     * 设置：token
     */
    public void setToken(String token) {
        this.token = token;
    }
    /**
     * 获取：token
     */
    public String getToken() {
        return token;
    }
    /**
     * 设置：过期时间
     */
    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
    /**
     * 获取：过期时间
     */
    public Date getExpireTime() {
        return expireTime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}
