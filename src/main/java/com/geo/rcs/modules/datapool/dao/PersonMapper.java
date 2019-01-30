package com.geo.rcs.modules.datapool.dao;

import com.geo.rcs.modules.datapool.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 数据池Mapper
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2017/12/28 15:51
 */
@Mapper
@Component(value = "personMapper")
public interface PersonMapper {

    void addByRid(Map<String, Object> parameters);

    Person findByRid(@Param(value = "partTableName") String partTableName, @Param(value = "rid") String rid);

    void deleteByRid(@Param(value = "partTableName") String partTableName, @Param(value = "rid") String rid);

    String isExists(@Param(value = "partTableName") String partTableName);

    void createPartTable(@Param(value = "partTableName") String partTableName);
	
}


