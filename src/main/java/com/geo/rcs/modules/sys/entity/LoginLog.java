package com.geo.rcs.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/21
 */
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    //日志id
    private Long id;
    //日志名称
    private String logname;
    //用户id
    private Long userId;
    //用户登录名
    private String username;
    //登陆时间
    private Date createtime;
    //成功信息
    private String succeed;
    //登录信息
    private String message;
    //登录ip
    private String ip;

    private Integer pageSize;

    private Integer pageNo;

    private String beginTime;

    private String endTime;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
