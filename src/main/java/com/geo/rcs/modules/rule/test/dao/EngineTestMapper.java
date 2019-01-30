package com.geo.rcs.modules.rule.test.dao;

import com.geo.rcs.modules.rule.test.entity.EngineTest;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "engineTestMapper")
public interface EngineTestMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EngineTest record);

    int insertSelective(EngineTest record);

    EngineTest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EngineTest record);

    int updateByPrimaryKey(EngineTest record);

    Page<EngineTest> findByPage(EngineTest engineTest);
}