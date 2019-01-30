package com.geo.rcs.modules.rule.field.dao;

import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "engineFieldMapper")
public interface EngineFieldMapper {
    int deleteByPrimaryKey(Long id);

    int updateByPrimaryKey(EngineRawField record);

    List<EngineInter> findInterById(Long[] ids);

    List<FieldType> getFieldType();

    void updateFieldSelect(PatchData patchData);

    List<EngineInter> getInterName();

    Long[] selectByConditionId(Long id);

    void deleteBatch(Long[] ids);

    List<EngineField> selectFieldForCon(Long id);

    void updateByPrimaryKeySelectiveEn(EngineField engineField);

    void insertSelectiveEn(EngineField engineField);

    Page<EngineRawField> findByPage(EngineRawField field);

    EngineRawField selectByPrimaryKey(Long id);

    void deleteByRulesId(Long id);

    List<EngineRawField> getAllEngineRawField();

    EngineField getFieldById(Long id);

    EngineRules findRulesByFieldId(Long id);

    void deleteByConId(Long id);

    Long[] selectFieldByRuleId(Long id);

    void deleteByArray(Long[] ids);

    List<Long> selectRulesFieldUse();

    /**
     * 入参计算（一个接口对应一个字段）：根据接口名查字段名
     * @param innerName
     * @return
     */
    String getFieldNameFromInnerName(String innerName);
}