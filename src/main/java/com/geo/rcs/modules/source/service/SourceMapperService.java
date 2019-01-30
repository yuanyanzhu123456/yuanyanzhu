package com.geo.rcs.modules.source.service;

import com.geo.rcs.modules.rule.inter.entity.EngineInter;

import java.util.List;

/**
 * Author:  yongmingz
 * Created on : 2018.1.11
 */
public interface SourceMapperService {

    Long[] getFieldIds(String rulesConfig) throws Exception;

    List<EngineInter> getRulesInter(Long[] rulesFieldIds) throws Exception;

}
