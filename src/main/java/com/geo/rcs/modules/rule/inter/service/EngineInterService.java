package com.geo.rcs.modules.rule.inter.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.field.entity.FieldType;
import com.geo.rcs.modules.rule.inter.entity.EngineInterType;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.inter.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年03月01日 上午11:09
 */
public interface EngineInterService {

    Page<EngineInter> findByPage(EngineInter field) throws ServiceException;

    boolean usernameUnique(EngineInter engineInter) throws ServiceException;

    String save(EngineInter engineInter) throws Exception;

    EngineInter insertSelective(EngineInter engineInter);

    List<EngineInter> findInterById(Long[] ids);

    EngineInter getFieldById(Long id);

    void delete(Long id) throws ServiceException;

    List<FieldType> getFieldType() throws ServiceException;

    EngineInter queryByName(EngineInter engineInter);

    List<EngineInter> getFieldName();

    void deleteBatch(Long[] ids);

    EngineInter getInterById(Long id);

    void updateInter(EngineInter engineInter) throws ServiceException;

    List<EngineInter> getInterList()  throws ServiceException;

    String getInterParams(Long rulesId);

    Map<String,Object> getInterParamsByArray(Long[] rulesIds);
    /**
     * 获取接口类型
     * @return List<EngineInterType>
     */
    List<EngineInterType> getInterType();

    void saveInterAndFields(EngineInter engineInter, List<Map<String,String>>  fields);
}
