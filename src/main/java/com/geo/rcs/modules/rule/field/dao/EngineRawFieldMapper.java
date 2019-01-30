package com.geo.rcs.modules.rule.field.dao;

import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldDataType;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年02月05日 下午2:59
 */
@Mapper
@Component(value = "engineRawFieldMapper")
public interface EngineRawFieldMapper {

    int deleteByPrimaryKey(Long id);
    int deleteFieldAdmin(Long id);

    int insert(EngineRawField record);

    int insertSelective(EngineRawField record);

    int insertToRawFields(EngineRawField record);

    EngineRawField selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EngineRawField record);

    int updateByPrimaryKey(EngineRawField record);

    Page<EngineRawField> findByPage(EngineRawField field);

    EngineRawField queryByTypeAndName(EngineRawField engineRawField);

    List<EngineRawField> findInterParById(Long[] ids);

    List<FieldType> getFieldType();

    void updateFieldVerify(EngineRawField engineRawField);

    List<EngineRawField> getFieldName();

    List<EngineRawField> getFieldNameById(Integer fieldType);

    void deleteBatch(Long[] ids);

    Page<EngineRawField> getGeoFieldList(EngineRawField field);

    EngineInter selectInterByFieldId(Long id);

    List<FieldDataType> getFieldDataType();

    Long queryUniquecodeByAdduser(String addUser);
}
