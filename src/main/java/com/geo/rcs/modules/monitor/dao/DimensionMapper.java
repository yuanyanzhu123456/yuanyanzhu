package com.geo.rcs.modules.monitor.dao;

import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.sys.dao.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 15:27 2018/8/1
 */
@Mapper
@Component(value = "dimensionMapper")
public interface DimensionMapper extends BaseMapper {

    /**
     * 分页展示
     * @param dimension,allPage
     * @return
     * @throws ServiceException
     */
    Page<Dimension> findByPage(Dimension dimension);

    /**
     * 添加
     * @param dimension
     * @throws ServiceException
     */
    void save(Dimension dimension);

    /**
     * 修改维度
     * @param dimension
     * @throws ServiceException
     */
    void update(Dimension dimension);
    /**
     * 删除维度
     * @param ids
     * @throws ServiceException
     */
    void delete(Integer[] ids);

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
    List<Dimension> getDimensionByName(@Param("dimensionDesc")String dimensionDesc,@Param("uniqueCode") Long uniqueCode);

    /**
     * 根据任务编号获取监控策略列表
     * @param jobIds
     * @return
     */
    List<Dimension> queryListByJobIds(Long[] jobIds);

}
