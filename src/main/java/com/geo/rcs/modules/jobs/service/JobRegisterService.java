package com.geo.rcs.modules.jobs.service;


import com.geo.rcs.modules.jobs.entity.JobRegister;


/**
 * Job Register Center
 * @project: rcs
 * @package： com.geo.rcs.modules.job.service
 * @Description: Job Register Service for scheduler and worker
 * @Author: zhangyongming@geotmt.com
 * @Created on: 2018.7.5 16:11:00
 */

public interface JobRegisterService {

    JobRegister register(JobRegister register);

    JobRegister keepAlive(JobRegister register);

    /**
     * * 修改JobRegister消费的数量
     * @param jobRegisterFlag  woker标识
     * @param type 0失败 1成功
     * @return true 修改成功,false失败
     */
    Boolean changeCustomCount(String role,int type,int num);
    Boolean changeDistrbuteCount(String role,int type,int num);

}
