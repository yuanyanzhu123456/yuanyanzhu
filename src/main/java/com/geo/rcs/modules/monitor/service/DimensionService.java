package com.geo.rcs.modules.monitor.service;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 15:10 2018/8/1
 */
public interface DimensionService {

    /**
     * 分页展示
     * @param dimension,allPage
     * @return
     * @throws ServiceException
     */
    Page<Dimension> findByPage(Dimension dimension,boolean allPage) throws ServiceException;

    /**
     * 添加
     * @param dimension
     * @throws ServiceException
     */
    void save(Dimension dimension) throws ServiceException;

    /**
     * 修改维度
     * @param dimension
     * @throws ServiceException
     */
    void update(Dimension dimension) throws ServiceException;
    /**
     * 删除维度
     * @param ids
     * @throws ServiceException
     */
    void delete(Integer[] ids) throws ServiceException;

    /**
     * 根据编号获取监控策略
     * @param id
     * @return Dimension
     */
    Dimension selectByPrimaryKey(Integer id);
    /**
     * 根据名称获取监控策略
     * @param dimensionDesc
     * @return Dimension
     */
    List<Dimension> getDimensionByName(String dimensionDesc,Long uniqueCode);

    /**
     * 根据任务编号获取监控策略列表
     * @param jobIds
     * @return
     */
    List<Dimension> queryListByJobIds(Long[] jobIds);

}
