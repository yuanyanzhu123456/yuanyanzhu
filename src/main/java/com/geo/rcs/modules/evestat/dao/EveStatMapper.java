package com.geo.rcs.modules.evestat.dao;

import com.geo.rcs.modules.evestat.entity.EveStat;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.api.evestat.dao
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月09日 上午10:36
 */
@Mapper
@Component(value = "eveStatMapper")
public interface EveStatMapper {

    Page<EveStat> findByPage(EveStat eveStat);
}
