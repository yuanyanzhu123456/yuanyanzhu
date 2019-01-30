package com.geo.rcs.modules.abtest.dao;

import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ab测试
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/10/25
 */
@Mapper
@Component(value = "abTestMapper")
public interface AbTestMapper {

    /**
     * 查询任务列表（支持多条件模糊，分页）
     * @param map
     * @return
     */
    Page<AbTest> findByPage(Map<String,Object> map);

    /**
     * 保存任务
     * @param abTest
     * @return
     */
    void saveAbJob(AbTest abTest);
    /**
     * 根据主键获取任务
     * @param jobId
     * @return
     */
    AbTest getAbTestJobByPrimaryKey(Long jobId);
    /**
     * 过滤任务
     * @param map
     * @return
     */
    List<AbScheduleTask> queryByFilter(HashMap<String, Object> map);
    /**
     * 结果统计
     * @param map
     * @return
     */
    Map<String,Object> getEventStat(Map<String, Object> map);
    /**
     * 获取子任务总量及完成数量
     * @param map
     * @return
     */
    Map<String,Object> getTaskCountAndCompletedCount(Map<String, Object> map);
    /**
     * 更改任务状态
     * @param map
     * @return
     */
    int updateJobStatus(Map<String, Object> map);
    /**
     * 更改任务
     * @param abTest
     * @return
     */
    void updateAbJob(AbTest abTest);
    /**
     * 更改任务已执行数量
     * @param jobId
     * @return
     */
    void updateAbJobCompletedCount(Long jobId);
    /**
     * 根据名称查询任务
     * @param abTest
     * @return
     */
    AbTest getAbTestJobByName(AbTest abTest);
}