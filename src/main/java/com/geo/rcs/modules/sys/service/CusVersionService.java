package com.geo.rcs.modules.sys.service;

import com.geo.rcs.modules.sys.entity.CusVersion;

import java.util.List;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/11/9  10:13
 **/
public interface CusVersionService {
    void save(CusVersion cusVersion) throws  Exception;
    Long queryVersionId(Long customerId);
    void updateCusVersion(CusVersion cusVersion) throws Exception;
    Map<String,Object> getCusVersionInfo(Long customerId);

     void renewal(CusVersion cusVersion);
     CusVersion selectByCustomId(Long customId);

    CusVersion getCusInfoById(Long customerId);

     List<CusVersion> getAllCusVerInfo();

}
