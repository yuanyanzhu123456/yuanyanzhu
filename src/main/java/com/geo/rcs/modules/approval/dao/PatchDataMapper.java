package com.geo.rcs.modules.approval.dao;

import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.PatchData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component(value = "patchDataMapper")
public interface PatchDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PatchData record);

    int insertSelective(PatchData record);

    PatchData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PatchData record);

    int updateByPrimaryKey(PatchData record);

    PatchData selectByOnlyId(Approval approval);

    void OnlyUpdateVerify(PatchData patchData);
}