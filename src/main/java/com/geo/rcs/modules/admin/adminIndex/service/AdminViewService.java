package com.geo.rcs.modules.admin.adminIndex.service;

import com.geo.rcs.common.exception.ServiceException;

import java.util.Map;

/**
 * Created by geo on 2018/11/12.
 */
public interface AdminViewService {
    Map<String,Object> statistic(Long id) throws ServiceException;
}
