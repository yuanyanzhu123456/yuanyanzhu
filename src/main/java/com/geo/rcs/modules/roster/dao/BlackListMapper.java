package com.geo.rcs.modules.roster.dao;

import com.geo.rcs.modules.roster.entity.BlackList;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "blackMapper")
public interface BlackListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BlackList record);

    int insertSelective(BlackList record);

    BlackList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BlackList record);

    int updateByPrimaryKey(BlackList record);

    Page<BlackList> findByPage(BlackList blackList);

    void deleteBatch(Long[] ids);

    List<Map<String,Object>>  findAll();
}