package com.geo.rcs.modules.sys.entity;

import org.hibernate.validator.constraints.NotBlank;

import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;

public class Customer {
    private Long id;

    @NotBlank(message="数据源配置错误，server不能为空", groups = { UpdateGroup.class})
    private String server;

    private Integer encrypted;
    @NotBlank(message="数据源配置错误，加密类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String encryptionType;

    private String encryptionKey;

    @NotBlank(message="数据源配置错误，验真用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String username;
    @NotBlank(message="数据源配置错误，验真密码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String password;
    @NotBlank(message="数据源配置错误，合同号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String uno;

    private String etype;

    private Integer dsign;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server == null ? null : server.trim();
    }

    public Integer getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Integer encrypted) {
        this.encrypted = encrypted;
    }

    public String getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType == null ? null : encryptionType.trim();
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey == null ? null : encryptionKey.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno == null ? null : uno.trim();
    }

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype == null ? null : etype.trim();
    }

    public Integer getDsign() {
        return dsign;
    }

    public void setDsign(Integer dsign) {
        this.dsign = dsign;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", server='" + server + '\'' +
                ", encrypted=" + encrypted +
                ", encryptionType='" + encryptionType + '\'' +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uno='" + uno + '\'' +
                ", etype='" + etype + '\'' +
                ", dsign=" + dsign +
                ", userId=" + userId +
                '}';
    }
}
