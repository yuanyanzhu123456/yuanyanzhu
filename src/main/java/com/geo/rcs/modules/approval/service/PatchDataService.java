package com.geo.rcs.modules.approval.service;

import com.geo.rcs.modules.approval.entity.PatchData;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.approval.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月24日 下午4:32
 */
public interface PatchDataService {
    int insertSelective(PatchData record);
}
