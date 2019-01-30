package com.geo.rcs.modules.evestat.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.evestat.entity.EveStat;
import com.github.pagehelper.Page;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.evestat.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月09日 上午10:35
 */
public interface EveStatService {

    Page<EveStat> findByPage(EveStat eveStat) throws ServiceException;
}
