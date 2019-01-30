package com.geo.rcs.modules.approval.service.impl;

import com.geo.rcs.modules.approval.dao.PatchDataMapper;
import com.geo.rcs.modules.approval.entity.PatchData;
import com.geo.rcs.modules.approval.service.PatchDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.approval.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月24日 下午4:32
 */
@Service
public class PatchDataServiceImpl implements PatchDataService{
    @Autowired
    private PatchDataMapper patchDataMapper;
    @Override
    public int insertSelective(PatchData record) {
        return patchDataMapper.insertSelective(record);
    }
}
