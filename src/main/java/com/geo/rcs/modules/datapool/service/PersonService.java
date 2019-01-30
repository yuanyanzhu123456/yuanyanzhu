package com.geo.rcs.modules.datapool.service;

import com.geo.rcs.modules.datapool.entity.Person;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface PersonService {

    void addByRid(String rid, Map<String, Object> parameters);

    Person findByRid(@Param(value = "rid") String rid);

    void deleteByRid(@Param(value = "rid") String rid);

//    void updateByRid(@Param(value = "rid") String rid);

    Boolean isExists(@Param(value = "partTableName") String partTableName);

    void createPartTable(@Param(value = "partTableName") String partTableName);
}
