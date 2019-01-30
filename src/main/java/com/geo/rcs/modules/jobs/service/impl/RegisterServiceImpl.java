package com.geo.rcs.modules.jobs.service.impl;

import com.geo.rcs.modules.jobs.entity.JobWorker;
import com.geo.rcs.modules.jobs.service.RedisService;
import com.geo.rcs.modules.jobs.service.RegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RedisService redisService;

    static final String  DATASOURCE_TABLE = "RCS-JobRegister";

    @Override
    public void setRegisterInfoByKey( String infoName, String infoContent) {
        redisService.hset(DATASOURCE_TABLE, infoName, infoContent);
    }

    @Override
    public void updateRegisterInfoByKey( String infoName, String infoContent) {
        redisService.hset(DATASOURCE_TABLE, infoName, infoContent);
    }

    @Override
    public void deleteRegisterInfoByKey( String infoName) {
        redisService.hdel(DATASOURCE_TABLE, infoName);
    }

    @Override
    public String  getRegisterInfoByKey( String infoName) {
        String res = redisService.hget(DATASOURCE_TABLE, infoName);
        return res;
    }

    @Override
    public Map getAllRegisterInfo() {
        Map res = redisService.hgetAll(DATASOURCE_TABLE);
        return res;
    }

    /***
     * 测试主函数
     * @param args
     */
    public static void main(String[] args){
        RegisterService a = new RegisterServiceImpl();
        a.setRegisterInfoByKey("worker001", UUID.randomUUID().toString());
        System.out.println(a.getRegisterInfoByKey( "worker001"));
        a.updateRegisterInfoByKey("worker001", UUID.randomUUID().toString());
        System.out.println(a.getRegisterInfoByKey("worker001"));
        a.deleteRegisterInfoByKey("worker001");
        System.out.println(a.getRegisterInfoByKey("worker001"));

        /* 存储Worker注册信息测试 */
        List list1 = new ArrayList();
        JobWorker register = new JobWorker();
        register.initWorkerInfo("worker002", "127.0.0.1", "amqp-dddzfhdsksls", 1, 0, list1,"MAIL—POSTER");
        String info = register.getRegisterInfo(register);

        a.setRegisterInfoByKey("worker001", "info");
        a.deleteRegisterInfoByKey("worker002");
        a.deleteRegisterInfoByKey("worker003");
        a.deleteRegisterInfoByKey("worker004");

        System.out.println(a.getAllRegisterInfo());
    }

}
