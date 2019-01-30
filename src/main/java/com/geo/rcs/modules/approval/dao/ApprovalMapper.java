package com.geo.rcs.modules.approval.dao;

import com.geo.rcs.modules.approval.entity.ActionType;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.ObjectType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "approvalMapper")
public interface ApprovalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Approval record);

    int insertSelective(Approval record);

    Approval selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Approval record);

    int updateByPrimaryKey(Approval record);

    Page<Approval> findByPage(Approval approval);

    void updateAppStatus(Approval approval);

    List<ObjectType> getObjList();

    List<ActionType> getActionList();

    Page<Approval> findUnByPage(Approval approval);

    void updateRemarksById(Approval approval);
}