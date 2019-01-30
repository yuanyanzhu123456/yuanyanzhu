package com.geo.rcs.modules.datapool.dao;

import com.geo.rcs.modules.datapool.entity.Person;
import com.geo.rcs.modules.datapool.entity.PersonDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 数据池Mapper
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/28 15:51
 */
@Mapper
@Component(value = "personDetailMapper")
public interface PersonDetailMapper {

    void addByRidDimension(Map<String, Object> parameters);

    PersonDetail findByRidDimension(@Param(value = "partTableName") String partTableName, @Param(value = "rid") String rid, @Param(value = "dimension") String dimension);

    void updateById(Map<String, String> parameters);

    void deleteByRidDimension(@Param(value = "partTableName") String partTableName, @Param(value = "rid") String rid, @Param(value = "dimension") String dimension);

    String isExists(@Param(value = "partTableName") String partTableName);

    void createPartTable(@Param(value = "partTableName") String partTableName);
	
}


