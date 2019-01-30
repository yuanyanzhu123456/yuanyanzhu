package com.geo.rcs.modules.api.modules.user.controller;

import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.util.PropertiesFactory;
import com.geo.rcs.modules.api.annotation.AuthIgnore;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 初始化sql文件
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2018/3/2 17:12
 */
@RestController
@RequestMapping("/api")
public class InitSqlController {

    /**
     * 初始化sql
     * @return
     */
    @GetMapping("/initSql")
    @AuthIgnore
    public Geo init(){
        /** 数据库连接条件 */
        String url = PropertiesFactory.getPropertiesBean().getUrl();
        String driver = PropertiesFactory.getPropertiesBean().getDriver();
        String username = PropertiesFactory.getPropertiesBean().getUsername();
        String password = PropertiesFactory.getPropertiesBean().getPassword();

        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url, username, password);

            ScriptRunner runner = new ScriptRunner(conn);
            //设置字符集,不然中文乱码插入错误
            Resources.setCharset(Charset.forName("UTF-8"));
            //设置是否输出日志
            runner.setLogWriter(null);
            runner.runScript(Resources.getResourceAsReader("static/sql/rcs.sql"));
            runner.closeConnection();

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Geo.ok();
    }

}
