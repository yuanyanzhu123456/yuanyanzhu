package com.geo.rcs.modules.rule.scene.service.impl;

import com.geo.rcs.common.util.BlankUtil;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.scene.dao.EngineSceneMapper;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.geo.rcs.modules.rule.scene.service.SceneService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.scene.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月02日 下午2:50
 */
@Service
public class SceneServiceImpl implements SceneService {
    @Autowired
    private EngineSceneMapper sceneMapper;

    @Override
    public Page<EngineScene> findByPage(EngineScene scene) {
        PageHelper.startPage(scene.getPageNo(), scene.getPageSize());
        return sceneMapper.findByPage(scene);
    }

    @Override
    public EngineScene getSceneById(Long id) {
        return sceneMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean usernameUnique(EngineScene engineScene) {
        if (BlankUtil.isBlank(engineScene))
            return false;
        EngineScene scene = sceneMapper.queryByTypeAndName(engineScene);
        return scene == null;
    }

    @Override
    public void save(EngineScene scene) {
        scene.setAddTime(new Date());
        sceneMapper.insertSelective(scene);
    }

    @Override
    public void updateScene(EngineScene scene) {
        sceneMapper.updateByPrimaryKeySelective(scene);
    }

    @Override
    public void delete(Long id) {
        sceneMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<BusinessType> getBusType(Long uniqueCode) {
        return sceneMapper.getBusType(uniqueCode);
    }

    @Override
    public List<EngineScene> getSceneType(Long uniqueCode) {
        return sceneMapper.getSceneType(uniqueCode);
    }

    @Override
    public void updateSceneSelect(PatchData patchData) {
        sceneMapper.updateSceneSelect(patchData);
    }

    @Override
    public EngineScene findById(Long sceneId) {
        return sceneMapper.selectByPrimaryKey(sceneId);
    }

    @Override
    public List<EngineScene> getGeoUserBusType(Long uniqueCode) {
        return sceneMapper.getSceneType(uniqueCode);
    }

    @Override
    public List<EngineScene> querySceneName(String name,Long uniqueCode,Integer businessId) {

        if (BlankUtil.isBlank(name) || BlankUtil.isBlank(uniqueCode) || BlankUtil.isBlank(businessId)){
            return null;
        }
        return sceneMapper.querySceneName(name,uniqueCode,businessId);
    }

}
