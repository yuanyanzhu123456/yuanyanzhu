package com.geo.rcs.modules.roster.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.roster.dao.WhiteListMapper;
import com.geo.rcs.modules.roster.entity.WhiteList;
import com.geo.rcs.modules.roster.service.WhiteListService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.roster.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email jinlin@geotmt.com
 * @Creation Date : 2017年12月28日 上午11:38
 */
@Service
public class WhiteListServiceImpl implements WhiteListService {
    @Autowired
    private WhiteListMapper whiteListMapper;

    /**
     * 查看白名单列表（模糊，分页）
     * @param whiteList
     * @return
     * @throws ServiceException
     */
    @Override
    public Page<WhiteList> findByPage(WhiteList whiteList) {
        PageHelper.startPage(whiteList.getPageNo(),whiteList.getPageSize());
        return whiteListMapper.findByPage(whiteList);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        whiteListMapper.deleteBatch(ids);
    }
}
