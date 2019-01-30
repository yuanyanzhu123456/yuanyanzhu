package com.geo.rcs.modules.rule.scene.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.scene.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:47
 */
public interface SceneService {

    Page<EngineScene> findByPage(EngineScene scene) throws ServiceException;

    EngineScene getSceneById(Long id);

    boolean usernameUnique(EngineScene engineScene);

    void save(EngineScene scene) throws ServiceException;

    void updateScene(EngineScene scene) throws ServiceException;

    void delete(Long id) throws ServiceException;

    List<BusinessType> getBusType(Long uniqueCode) throws ServiceException;

    List<EngineScene> getSceneType(Long uniqueCode);

    void updateSceneSelect(PatchData patchData);

    EngineScene findById(Long sceneId);

    List<EngineScene> getGeoUserBusType(Long uniqueCode);

    List<EngineScene> querySceneName(String name,Long uniqueCode,Integer businessId);
}
