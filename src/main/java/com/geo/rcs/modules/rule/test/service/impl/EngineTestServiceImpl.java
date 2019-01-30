package com.geo.rcs.modules.rule.test.service.impl;

import com.geo.rcs.modules.rule.test.dao.EngineTestMapper;
import com.geo.rcs.modules.rule.test.entity.EngineTest;
import com.geo.rcs.modules.rule.test.service.EngineTestService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.test.service.impl
 * @Description : TODD
 * @Author guoyujie
 * @email jinlin@geotmt.com
 * @Creation Date : 2018年01月04日 上午10:59
 */
@Service
public class EngineTestServiceImpl implements EngineTestService{

    @Autowired
    private EngineTestMapper engineTestMapper;

    @Override
    public Page<EngineTest> findByPage(EngineTest engineTest) {
        PageHelper.startPage(engineTest.getPageNo(), engineTest.getPageSize());
        return engineTestMapper.findByPage(engineTest);
    }

    @Override
    public void delete(Long id) {
        engineTestMapper.deleteByPrimaryKey(id);
    }
}
