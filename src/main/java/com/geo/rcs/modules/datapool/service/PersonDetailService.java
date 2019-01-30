package com.geo.rcs.modules.datapool.service;

import com.geo.rcs.modules.datapool.entity.PersonDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PersonDetailService {

    void addByRidDimension(@Param(value = "rid") String rid,  @Param(value = "dimension") String dimension,  @Param(value = "parameters") Map <String, Object> parameters);

    PersonDetail findByRidDimension(@Param(value = "rid") String rid, @Param(value = "dimension") String dimension);

    void updateById(@Param(value = "parameters") Map <String, String> parameters);
    // Dropped: void updateByRidDimension(@Param(value = "rid") String rid,  @Param(value = "dimension") String dimension,  @Param(value = "parameters") Map <String, String> parameters);

    void deleteByRidDimension(@Param(value = "rid") String rid,  @Param(value = "dimension") String dimension);

    Boolean isExists(@Param(value = "partTableName") String partTableName);

    void createPartTable(@Param(value = "partTableName") String partTableName);

}
