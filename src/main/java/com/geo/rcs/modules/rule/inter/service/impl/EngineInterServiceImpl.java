package com.geo.rcs.modules.rule.inter.service.impl;

import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.common.util.JSONUtil;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.modules.rule.field.dao.EngineFieldMapper;
import com.geo.rcs.modules.rule.field.dao.EngineRawFieldMapper;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.inter.dao.EngineInterMapper;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.inter.entity.EngineInterType;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.source.handler.ValidateHandler;
import com.geo.rcs.modules.source.service.InterfaceService;
import com.geo.rcs.modules.source.service.SourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.hadoop.hdfs.web.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.inter.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午11:09
 */
@Service
public class EngineInterServiceImpl implements EngineInterService {

    @Autowired
    private EngineFieldMapper engineFieldMapper;
    @Autowired
    private EngineRawFieldMapper engineRawFieldMapper;
    @Autowired
    private EngineInterMapper engineInterMapper;
    @Autowired
    private InterfaceService interfaceService;

    @Override
    public Page<EngineInter> findByPage(EngineInter engineInter) {
        PageHelper.startPage(engineInter.getPageNo(), engineInter.getPageSize());
        return engineInterMapper.findByPage(engineInter);
    }

    @Override
    public boolean usernameUnique(EngineInter engineInter) {
        if (BlankUtil.isBlank(engineInter))
            return false;
        EngineInter field = engineInterMapper.queryByTypeAndName(engineInter);
        return field == null;
    }

    @Override
    public String save(EngineInter engineInter) throws Exception {
        if(engineInter.getType() == 1){
            String interfaceDataMap = interfaceService.getInterfaceDataMap(engineInter.getName(), engineInter.getTestParameters(), engineInter.getUserId(), engineInter.getType());
            if(interfaceDataMap != null){
                return interfaceDataMap;
            }
            else{
                throw new RcsException(StatusCode.RES_ERROR.getMessage(),StatusCode.RES_ERROR.getCode());
            }
        }
        else {
            return null;
        }

    }

    @Override
    public EngineInter insertSelective(EngineInter engineInter) {
        engineInter.setAddTime(new Date());
        engineInter.setVerify(1);
        engineInter.setActive(1);
        Map<String, Object> stringObjectMap = JSONUtil.jsonToMap(engineInter.getParameters());
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : stringObjectMap.keySet()) {
            stringBuffer.append(s);
        }
        engineInter.setParametersDesc(stringBuffer.toString());
        engineInterMapper.insertSelective(engineInter);
        return engineInter;
    }

    @Override
    public  List<EngineInter> findInterById(Long[] ids) {
        return engineFieldMapper.findInterById(ids);
    }

    @Override
    public void updateInter(EngineInter engineInter) {
        engineInterMapper.updateByPrimaryKeySelective(engineInter);
    }

    @Override
    public List<EngineInter> getInterList() throws ServiceException {
        return engineInterMapper.getInterList();
    }

    @Override
    public String getInterParams(Long rulesId) {
        List<String> interParams = engineInterMapper.getInterParams(rulesId);
        List<Map<String,Object>> mapList = JSONUtil.jsonToBean(JSONUtil.beanToJson(interParams),List.class);
        Map<String,Object> map = new HashMap<>();
        for (Map<String,Object> map1:mapList) {
            if(map1 != null){
                map.putAll(map1);
            }
        }
        return JSONUtil.beanToJson(map);
    }

    @Override
    public Map<String,Object> getInterParamsByArray(Long[] rulesIds) {
        Map<String,Object> map = new HashMap<>();
        for (Long rulesId : rulesIds) {
            List<String> interParams = engineInterMapper.getInterParams(rulesId);
            List<Map<String,Object>> mapList = JSONUtil.jsonToBean(JSONUtil.beanToJson(interParams),List.class);
            for (Map<String,Object> map1:mapList) {
                if(map1 != null){
                    map.putAll(map1);
                }
            }
        }
        return map;
    }

    @Override
    public List<EngineInterType> getInterType() {
        return engineInterMapper.getInterType();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInterAndFields(EngineInter engineInter, List<Map<String,String>> fields) {
        EngineInter engineInter1 = insertSelective(engineInter);
        EngineRawField engineRawField = new EngineRawField();
        if(!ValidateNull.isNull(fields)){
            for (Map<String, String> field : fields) {
                engineRawField.setName(field.get("field"));
                engineRawField.setDescribtion(field.get("valueDesc") ==null ?"暂无返回值描述":field.get("valueDesc"));
                engineRawField.setInterId(engineInter1.getId());
                engineRawField.setAddTime(new Date());
                engineRawField.setAddUser(engineInter1.getAddUser());
                engineRawField.setActive(1);
                engineRawField.setVerify(1);
                engineRawFieldMapper.insertToRawFields(engineRawField);
            }
        }
    }

    @Override
    public EngineInter getFieldById(Long id) {
        return engineInterMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long id) {
        engineInterMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<FieldType> getFieldType() {
        return engineFieldMapper.getFieldType();
    }

    @Override
    public EngineInter queryByName(EngineInter engineInter) {
        return engineInterMapper.queryByTypeAndName(engineInter);
    }

    @Override
    public List<EngineInter> getFieldName() {
        return engineInterMapper.getFieldName();
    }

    @Override
    public void deleteBatch(Long[] ids) {
        engineFieldMapper.deleteBatch(ids);
    }

    @Override
    public EngineInter getInterById(Long id) {
        return engineInterMapper.selectByPrimaryKey(id);
    }

}
