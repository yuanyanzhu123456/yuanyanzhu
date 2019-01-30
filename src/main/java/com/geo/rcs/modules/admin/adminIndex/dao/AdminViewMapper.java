package com.geo.rcs.modules.admin.adminIndex.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by geo on 2018/11/12.
 */
@Mapper
@Component(value = "adminViewMapper")
public interface AdminViewMapper {
    Long getActivity(Long id);

    List<Map<Object,Object>> getUserInfo(Long id);

    Long queryUniqueCode(Long id);

    Long getAllCount(Long uniqueCode);

    Long getMyCount(String submitter);

    Long getPrepareCount(Long id);

    Long getUserNum(Long uniqueCode);

    String getSubmitter(Long id);

    Long getRoleId(Long id);
}
