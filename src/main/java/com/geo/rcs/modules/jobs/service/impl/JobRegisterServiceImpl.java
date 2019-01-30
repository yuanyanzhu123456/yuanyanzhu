package com.geo.rcs.modules.jobs.service.impl;

import com.geo.rcs.modules.jobs.entity.JobRegister;
import com.geo.rcs.modules.jobs.service.JobRegisterService;
import com.geo.rcs.modules.jobs.service.RegisterService;
import com.geo.rcs.modules.jobs.service.jobStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobRegisterServiceImpl implements JobRegisterService{

    @Autowired
    private RegisterService redisService;
    @Autowired
    private jobStatisticsService statisticsServiceService;

    @Override
    public JobRegister register(JobRegister register) {

        String registerInfo = register.getRegisterInfo(register);

        if(redisService==null){
            RegisterService singleRedisService = new RegisterServiceImpl();
            singleRedisService.setRegisterInfoByKey(register.getId(), registerInfo);
        }else{
            redisService.setRegisterInfoByKey(register.getId(), registerInfo);
        }

        return register;
    }

    @Override
    public JobRegister keepAlive(JobRegister register) {

        String registerInfo = register.updateRegisterInfo(register);

        if(redisService==null){
            RegisterService singleRedisService = new RegisterServiceImpl();
            singleRedisService.setRegisterInfoByKey(register.getId(), registerInfo);
        }else{
            redisService.setRegisterInfoByKey(register.getId(), registerInfo);
        }
        return register;
    }

    @Override
    public Boolean changeCustomCount(String role, int type,int num) {

        //修改
        if (type==1){
        return statisticsServiceService.incrSuccess("_"+role.toUpperCase(),num);
        }else {
        return statisticsServiceService.incrfail("_"+role.toUpperCase(),num);

        }
    }
    @Override
    public Boolean changeDistrbuteCount(String role, int type,int num) {

        //修改
        if (type==1){
            return statisticsServiceService.incrDistrbuteSuccess("_"+role.toUpperCase(),num);

        }else {
            return statisticsServiceService.incrDistrbuteFail("_"+role.toUpperCase(),num);

        }
    }
}
