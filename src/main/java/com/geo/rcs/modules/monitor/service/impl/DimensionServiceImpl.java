package com.geo.rcs.modules.monitor.service.impl;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.monitor.dao.DimensionMapper;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 15:10 2018/8/1
 */
@Service("dimensionService")
@Transactional
public class DimensionServiceImpl implements DimensionService {

    @Autowired
    private DimensionMapper dimensionMapper;

    @Override
    public Page<Dimension> findByPage(Dimension dimension,boolean allPage) throws ServiceException {
        if (!allPage&&dimension.getPageNo()!=null&&dimension.getPageSize()!=null){
            PageHelper.startPage(dimension.getPageNo(), dimension.getPageSize());
        }
        return dimensionMapper.findByPage(dimension);
    }

    @Override
    public void save(Dimension dimension) throws ServiceException {
        dimensionMapper.save(dimension);
    }

    @Override
    public void update(Dimension dimension) throws ServiceException {
        dimensionMapper.update(dimension);
    }

    @Override
    public void delete(Integer[] ids) throws ServiceException {
        dimensionMapper.delete(ids);
    }

    @Override
    public Dimension selectByPrimaryKey(Integer id) {
        return dimensionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Dimension> getDimensionByName(String dimensionDesc,Long uniqueCode) {
        return dimensionMapper.getDimensionByName(dimensionDesc,uniqueCode);
    }

    @Override
    public List<Dimension> queryListByJobIds(Long[] jobIds) {
        return dimensionMapper.queryListByJobIds(jobIds);
    }

}
