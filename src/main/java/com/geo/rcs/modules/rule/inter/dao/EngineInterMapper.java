package com.geo.rcs.modules.rule.inter.dao;

import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.inter.entity.EngineInterType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.inter.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午11:09
 */
@Mapper
@Component(value = "engineInterMapper")
public interface EngineInterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EngineInter record);

    int insertSelective(EngineInter record);

    EngineInter selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EngineInter record);

    int updateByPrimaryKey(EngineInter record);

    Page<EngineInter> findByPage(EngineInter engineInter);

    EngineInter queryByTypeAndName(EngineInter engineInter);

    List<FieldType> getFieldType();

    List<EngineInter> getFieldName();

    void deleteBatch(Long[] ids);

    List<EngineInter> getInterList();

    List<String> getInterParams(Long rulesId);

    List<EngineInter> getAllInter();

    /**
     * 获取接口类型
     * @return List<EngineInterType>
     */
    List<EngineInterType> getInterType();

    /**
     * 根据接口列表获取接口列表
     * @param interNameList
     * @return
     */
    List<EngineInter> getIntersByNameList(List<String> interNameList);
}
