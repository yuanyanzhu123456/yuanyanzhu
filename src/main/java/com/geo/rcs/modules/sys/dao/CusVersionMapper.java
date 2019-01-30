package com.geo.rcs.modules.sys.dao;

import com.geo.rcs.modules.sys.entity.CusVersion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhengXingWang
 * @Email zhengxingwang@geotmt.com
 * @Date 2018/11/9  11:14
 **/
@Mapper
@Component(value = "cusVersionMapper")
public interface CusVersionMapper {
    void save(CusVersion cusVersion);
    Long queryVersionId(Long customerId);
    void update(CusVersion cusVersion);
    Map<String,Object> getCusVersionInfo(Long customerId);

    CusVersion getCusInfoById(Long customerId);

    CusVersion selectByCustomId(Long customId);


    void renewal(CusVersion cusVersion);

    List<CusVersion> getAllCusVerInfo();

    void updateById(CusVersion cusVersion);
    List<CusVersion> findAllByVersionId(Long VersionId);
    void delete(Long id);


    Date getExpireTime(Long customerId);
}
