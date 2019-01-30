package com.geo.rcs.modules.rule.test.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.rule.test.entity.EngineTest;
import com.github.pagehelper.Page;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.test.service
 * @Description : TODD
 * @Author guoyujie
 * @email jinlin@geotmt.com
 * @Creation Date : 2018年01月04日 上午10:58
 */
public interface EngineTestService {

    Page<EngineTest> findByPage(EngineTest engineTest) throws ServiceException;

    void delete(Long id) throws ServiceException;
}
