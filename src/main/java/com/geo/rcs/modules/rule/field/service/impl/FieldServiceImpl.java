package com.geo.rcs.modules.rule.field.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.condition.dao.ConditionsMapper;
import com.geo.rcs.modules.rule.field.dao.EngineFieldMapper;
import com.geo.rcs.modules.rule.field.dao.EngineRawFieldMapper;
import com.geo.rcs.modules.rule.field.entity.EngineField;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.entity.FieldDataType;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.rule.ruleset.dao.EngineRulesMapper;
import com.geo.rcs.modules.rule.ruleset.entity.EngineRules;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:49
 */
@Service
public class FieldServiceImpl implements FieldService{
    @Autowired
    private EngineFieldMapper engineFieldMapper;
    @Autowired
    private EngineRawFieldMapper engineRawFieldMapper;
    @Autowired
    private ConditionsMapper conditionsMapper;
    @Autowired
    private EngineRulesMapper engineRulesMapper;
    @Autowired
    private EngineInterService engineInterService;

    @Override
    public Page<EngineRawField> findByPage(EngineRawField field) {
        PageHelper.startPage(field.getPageNo(), field.getPageSize());
        return engineRawFieldMapper.findByPage(field);
    }

    @Override
    public boolean usernameUnique(EngineRawField engineRawField) {
        if (BlankUtil.isBlank(engineRawField))
            return false;
        EngineRawField field = engineRawFieldMapper.queryByTypeAndName(engineRawField);
        return field == null;
    }

    @Override
    public void save(EngineRawField engineRawField) {
        engineRawField.setAddTime(new Date());
        engineRawField.setVerify(1);
        engineRawFieldMapper.insertToRawFields(engineRawField);
    }

    @Override
    public void saveField(EngineField engineField) throws ServiceException {
        engineFieldMapper.insertSelectiveEn(engineField);
    }

    @Override
    public List<EngineRawField> findInterParById(Long[] ids) {
        return engineRawFieldMapper.findInterParById(ids);
    }

    @Override
    public  List<EngineInter> findInterById(Long[] ids) {
        return engineFieldMapper.findInterById(ids);
    }

    @Override
    public void updateField(EngineRawField engineRawField) {
        engineRawFieldMapper.updateByPrimaryKeySelective(engineRawField);
    }

    @Override
    public void updateEngineField(EngineField engineField) throws ServiceException {
        engineFieldMapper.updateByPrimaryKeySelectiveEn(engineField);
    }

    @Override
    public EngineRawField getRawFieldById(Long id) {
        return engineRawFieldMapper.selectByPrimaryKey(id);
    }

    @Override
    public EngineField getFieldById(Long id) {
        return engineFieldMapper.getFieldById(id);
    }

    @Override
    public void delete(Long id) {
        engineRawFieldMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteFieldAdmin(Long id) throws ServiceException {
        engineRawFieldMapper.deleteFieldAdmin(id);
    }

    @Override
    public List<FieldType> getFieldType() {
        return engineFieldMapper.getFieldType();
    }

    @Override
    @Transactional
    public void updateFieldVerify(Approval approval) {
        EngineRawField engineRawField = new EngineRawField();
        engineRawField.setVerify(1);
        engineRawField.setId(approval.getOnlyId());
        engineRawFieldMapper.updateFieldVerify(engineRawField);
    }

    @Override
    public void updateFieldSelect(PatchData patchData) {
        engineFieldMapper.updateFieldSelect(patchData);
    }

    @Override
    public EngineRawField queryByName(EngineRawField engineRawField) {
        return engineRawFieldMapper.queryByTypeAndName(engineRawField);
    }

    @Override
    public List<EngineRawField> getFieldName() {
        return engineRawFieldMapper.getFieldName();
    }

    @Override
    public List<EngineInter> getInterName() {
        return engineFieldMapper.getInterName();
    }

    @Override
    @Transactional
    public List<EngineField>  addFieldBatch(List<EngineField> engineFields) {
        for (EngineField engineField : engineFields) {
            if(engineField.getId() != null){
                engineFieldMapper.updateByPrimaryKeySelectiveEn(engineField);
                EngineRules engineRules1 = conditionsMapper.queryEngineRulesActive(engineField.getConditionId());
                //更新规则集参数
                String params = engineInterService.getInterParams(engineRules1.getId());
                engineRulesMapper.updateParams(engineRules1.getId(), params);
            }
            else{
                engineFieldMapper.insertSelectiveEn(engineField);
                EngineRules engineRules1 = conditionsMapper.queryEngineRulesActive(engineField.getConditionId());
                //更新规则集参数
                String params = engineInterService.getInterParams(engineRules1.getId());
                engineRulesMapper.updateParams(engineRules1.getId(), params);
            }
        }

        //engineFieldMapper.addFieldBatch(engineRawFields);
        return engineFields;
    }

    @Override
    public List<EngineRawField> getFieldNameById(Integer fieldType) {
        return engineRawFieldMapper.getFieldNameById(fieldType);
    }

    @Override
    public Long[] selectByConditionId(Long id) {
        return engineFieldMapper.selectByConditionId(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        engineFieldMapper.deleteBatch(ids);
    }

    @Override
    public Page<EngineRawField> getGeoFieldList(EngineRawField field){
        PageHelper.startPage(field.getPageNo(), field.getPageSize());
        return engineRawFieldMapper.getGeoFieldList(field);
    }

    @Override
    @Transactional
    public List<EngineField> addFieldBatchNoUp(List<EngineField> engineFields) {
        for (EngineField engineField : engineFields) {
            engineFieldMapper.insertSelectiveEn(engineField);
        }
        return engineFields;
    }

    @Override
    public EngineRawField selectById(Long id){
        return engineRawFieldMapper.selectByPrimaryKey(id);
    }

    @Override
    public EngineInter selectInterByFieldId(Long id){
        return engineRawFieldMapper.selectInterByFieldId(id);
    }

    @Override
    public List<FieldDataType> getFieldDataType(){
        return engineRawFieldMapper.getFieldDataType();
    }

    @Override
    public void deleteByRulesId(Long id) {
        engineFieldMapper.deleteByRulesId(id);
    }

    @Override
    public List<EngineRawField> getAllEngineRawField() throws ServiceException {
        return engineFieldMapper.getAllEngineRawField();
    }

    @Override
    public List<EngineField> selectFieldForCon(Long id) {
        return engineFieldMapper.selectFieldForCon(id);
    }

    @Override
    public EngineRules findRulesByFieldId(Long id) {
        return engineFieldMapper.findRulesByFieldId(id);
    }

    @Override
    public void deleteByConId(Long id) {
        engineFieldMapper.deleteByConId(id);
    }

    @Override
    public void deleteByRuleId(Long id) {
        Long[] ids = engineFieldMapper.selectFieldByRuleId(id);
        if(ids.length > 0){
            engineFieldMapper.deleteByArray(ids);
        }
    }

    @Override
    public List<Long> selectRulesFieldUse() {
        return engineFieldMapper.selectRulesFieldUse();
    }

    @Override
    public String getFieldNameFromInnerName(String innerName) {
        return engineFieldMapper.getFieldNameFromInnerName(innerName);
}


}
