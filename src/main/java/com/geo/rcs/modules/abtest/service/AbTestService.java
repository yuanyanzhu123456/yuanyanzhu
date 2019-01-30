package com.geo.rcs.modules.abtest.service;

import com.geo.rcs.modules.abtest.entity.AbScheduleTask;
import com.geo.rcs.modules.abtest.entity.AbTest;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.emp.service
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年10月25日 下午4:47
 */
public interface AbTestService {
    /**
     * 查询任务列表（支持多条件模糊，分页）
     *
     * @param map
     * @return
     */
    Page<AbTest> findByPage(Map<String, Object> map);

    /**
     * 保存任务
     *
     * @param abTest
     * @return
     */
    AbTest saveAbJob(AbTest abTest);

    /**
     * 保存名单
     *
     * @param excelInfo
     * @return
     */
    void saveTaskBatch(List<AbScheduleTask> excelInfo);

    /**
     * 查看表是否存在
     *
     * @param tableName
     * @return
     */
    void queryTableIFExists(@Param("tableName") String tableName);

    /**
     * 根据编号获取任务
     *
     * @param jobId
     * @return
     */
    AbTest getAbTestJobByPrimaryKey(Long jobId);

    /**
     * 过滤查找子任务
     */
    List<AbScheduleTask> queryByFilter(HashMap<String, Object> map);

    /**
     * 批量修改任务状态（运行）
     *
     * @param scheduleTasks
     * @param uniqueCode
     * @return
     */
    void updateDistributeStatusToRunning(List<AbScheduleTask> scheduleTasks, Long uniqueCode);

    /**
     * 批量修改任务状态（初始化）
     *
     * @param excessScheduleTask
     * @param uniqueCode
     * @return
     */
    void updateDistributeStatusToInit(List<AbScheduleTask> excessScheduleTask, Long uniqueCode);

    /**
     * 批量删除定时任务
     *
     * @param jobId
     * @return
     */
    void deleteBatch(Long jobId);

    /**
     * 结果分析列表
     *
     * @param map
     * @return
     */
    Page<AbScheduleTask> getTaskResultList(Map<String, Object> map);
    /**
     * 结果分析列表
     *
     * @param map
     * @return
     */
    Map getTaskResultListAll(Map<String, Object> map);

    /**
     * 结果统计(图)
     *
     * @param map
     * @return
     */
    List<Map<String,Object>> getEventStat(Map<String, Object> map);

    /**
     * 获取子任务总量及完成数量
     *
     * @param map
     * @return
     */
    Map<String, Object> getTaskCountAndCompletedCount(Map<String, Object> map);
    /**
     * 消息回调
     *
     * @param task
     * @return
     */
    Boolean msgCallbBack(ScheduleTask task);
    /**
     * 获取子任务详情
     *
     * @param map
     * @return
     */
    AbScheduleTask getTaskDetail(Map<String, Object> map);
    /**
     * 根据任务编号筛选子任务
     *
     * @param map
     * @return
     */
    List<AbScheduleTask> getTaskByJobId(Map<String, Object> map);
    /**
     * 根据任务名称获取任务信息
     *
     * @param abTest
     * @return AbTest
     */
    AbTest getAbTestJobByName(AbTest abTest);
}
