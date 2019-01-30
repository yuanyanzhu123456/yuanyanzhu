package com.geo.rcs.modules.evestat.service.impl;

import com.geo.rcs.modules.evestat.dao.EveStatMapper;
import com.geo.rcs.modules.evestat.entity.EveStat;
import com.geo.rcs.modules.evestat.service.EveStatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.evestat.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月09日 上午10:35
 */
@Service
public class EveStatServiceImpl implements EveStatService{

    @Autowired
    private EveStatMapper eveStatMapper;

    @Override
    public Page<EveStat> findByPage(EveStat eveStat) {
        PageHelper.startPage(eveStat.getPageNo(), eveStat.getPageSize());
        return eveStatMapper.findByPage(eveStat);
    }
}
