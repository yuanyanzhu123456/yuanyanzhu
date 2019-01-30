package com.geo.rcs.modules.rule.scene.dao;

import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.rule.scene.entity.BusinessType;
import com.geo.rcs.modules.rule.scene.entity.EngineScene;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "engineSceneMapper")
public interface EngineSceneMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EngineScene record);

    int insertSelective(EngineScene record);

    EngineScene selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EngineScene record);

    int updateByPrimaryKey(EngineScene record);

    Page<EngineScene> findByPage(EngineScene scene);

    EngineScene queryByTypeAndName(EngineScene engineScene);

    List<BusinessType> getBusType(Long uniqueCode);

    List<EngineScene> getSceneType(Long uniqueCode);

    void updateSceneSelect(PatchData patchData);

    List<EngineScene> querySceneName(@Param("name") String name, @Param("uniqueCode") Long uniqueCode, @Param("businessId") Integer businessId);
}