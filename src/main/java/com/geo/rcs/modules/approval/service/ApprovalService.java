package com.geo.rcs.modules.approval.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.approval.entity.ActionType;
import com.geo.rcs.modules.approval.entity.Approval;
import com.geo.rcs.modules.approval.entity.ObjectType;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.approval.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2017年12月29日 上午11:56
 */
public interface ApprovalService {

    Page<Approval> findByPage(Approval approval) throws ServiceException;

    Approval getApprovalById(Long id) throws ServiceException;

    void addToApproval(Approval approval) throws ServiceException;

    void updateAppStatus(Approval approval) throws ServiceException;

    void updateAppStatusForDecision(Approval approval) throws ServiceException;

    Approval selectById(Long id);

    List<ObjectType> getObjList();

    List<ActionType> getActionList();

    Page<Approval> findUnByPage(Approval approval) throws ServiceException;

    void deleteToApproval(Approval approval) throws ServiceException;
}
