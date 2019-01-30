package com.geo.rcs.modules.rule.field.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldDataType;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:48
 */
public interface FieldService {
    Page<EngineRawField> findByPage(EngineRawField field) throws ServiceException;

    boolean usernameUnique(EngineRawField engineRawField) throws ServiceException;

    void save(EngineRawField engineRawField) throws ServiceException;

    void saveField(EngineField engineField) throws ServiceException;

    List<EngineRawField> findInterParById(Long[] ids);

    List<EngineInter> findInterById(Long[] ids);

    void updateField(EngineRawField engineRawField) throws ServiceException;

    void updateEngineField(EngineField engineField) throws ServiceException;

    EngineRawField getRawFieldById(Long id);

    EngineField getFieldById(Long id);

    void delete(Long id) throws ServiceException;
    void deleteFieldAdmin(Long id) throws ServiceException;

    List<FieldType> getFieldType() throws ServiceException;

    void updateFieldVerify(Approval approval);

    void updateFieldSelect(PatchData patchData);

    EngineRawField queryByName(EngineRawField engineRawField);

    List<EngineRawField> getFieldName();

    List<EngineInter> getInterName();

    List<EngineField> addFieldBatch(List<EngineField> en);

    List<EngineRawField> getFieldNameById(Integer fieldType) throws ServiceException;

    Long[] selectByConditionId(Long id) throws ServiceException;

    void deleteBatch(Long[] ids);

    Page<EngineRawField> getGeoFieldList(EngineRawField field)  throws ServiceException;

    List<EngineField> addFieldBatchNoUp(List<EngineField> fieldList);

    EngineRawField selectById(Long id) throws ServiceException;

    EngineInter selectInterByFieldId(Long id) throws ServiceException;

    List<FieldDataType> getFieldDataType() throws ServiceException;

    void deleteByRulesId(Long id);

    List<EngineRawField> getAllEngineRawField() throws ServiceException;

    List<EngineField> selectFieldForCon(Long id);

    EngineRules findRulesByFieldId(Long id);

    void deleteByConId(Long id);

    void deleteByRuleId(Long id);
    List<Long> selectRulesFieldUse();

    /**
     * 入参计算（一个接口对应一个字段）：根据接口名查字段名
     * @param innerName
     * @return
     */
    String getFieldNameFromInnerName(String innerName);
}
