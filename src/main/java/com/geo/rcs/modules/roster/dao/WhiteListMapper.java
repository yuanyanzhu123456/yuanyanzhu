package com.geo.rcs.modules.roster.dao;

import com.geo.rcs.modules.roster.entity.WhiteList;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "whiteMapper")
public interface WhiteListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WhiteList record);

    int insertSelective(WhiteList record);

    WhiteList selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WhiteList record);

    int updateByPrimaryKey(WhiteList record);

    Page<WhiteList> findByPage(WhiteList whiteList);

    void deleteBatch(Long[] ids);
}