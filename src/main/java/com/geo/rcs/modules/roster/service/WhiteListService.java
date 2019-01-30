package com.geo.rcs.modules.roster.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.roster.entity.WhiteList;
import com.github.pagehelper.Page;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.roster.service
 * @Description : TODD
 * @Author guoyujie
 * @email jinlin@geotmt.com
 * @Creation Date : 2017年12月28日 上午11:37
 */
public interface WhiteListService {

    Page<WhiteList> findByPage(WhiteList whiteList) throws ServiceException;

    void deleteBatch(Long[] ids) throws ServiceException;
}
