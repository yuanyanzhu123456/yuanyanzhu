package com.geo.rcs.modules.monitor.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.jedis.RedisPool;
import com.geo.rcs.common.page.PageUtils;
import com.geo.rcs.common.page.Query;
import com.geo.rcs.common.util.Geo;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
@Component("scheduleJobController")
@RestController
    @RequestMapping("/sys/schedule")
public class ScheduleJobController extends BaseController{

    @Autowired
    private ScheduleJobService scheduleJobService;

    @Autowired
    private RedisPool redisPool;

    @Autowired
    private DimensionService dimensionService;

    /**
     * 定时任务列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:schedule:list")
    public Geo list(@RequestBody Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        query.put("userId", getUser().getUniqueCode());
        List<ScheduleJob> jobList = scheduleJobService.queryListByUserId(query);
        int total = scheduleJobService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());

        return Geo.ok().put("page", pageUtil);
    }

    /**
     * 已删除定时任务列表
     */
    @RequestMapping("/overList")
    @RequiresPermissions("sys:schedule:overList")
    public Geo overList(@RequestBody Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        query.put("status",params.get("status"));
        query.put("userId", getUser().getUniqueCode());
        List<ScheduleJob> jobList = scheduleJobService.queryOverList(query);

        int total = scheduleJobService.queryOverTotal(query);

        PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());

        return Geo.ok().put("page", pageUtil);
    }
    /**
     * 定时任务信息
     */
    @RequestMapping("/info/{jobId}")
    @RequiresPermissions("sys:schedule:info")
    public Geo info(@PathVariable("jobId") Long jobId){
        Long userId = getUser().getUniqueCode();
        ScheduleJob schedule = scheduleJobService.queryObject(jobId,userId);

        return Geo.ok().put("schedule", schedule);
    }

    /**
     * 保存定时任务
     */
    @SysLog("保存定时任务")
    @RequestMapping("/save")
    @RequiresPermissions("sys:schedule:save")
    public Geo save(@RequestBody ScheduleJob scheduleJob){
        ValidatorUtils.validateEntity(scheduleJob);
        scheduleJob.setUserId(getUser().getUniqueCode());
        Map<String, Object> params = new HashMap<>(2);
        params.put("page",1);
        params.put("limit",Integer.MAX_VALUE);
        Query query = new Query(params);
        query.put("userId", getUser().getUniqueCode());
        List<ScheduleJob> jobList = scheduleJobService.queryListByUserId(query);
        if(jobList!=null&&!jobList.isEmpty()){
            for(ScheduleJob job:jobList){
                if(job.getJobName().equals(scheduleJob.getJobName())){
                    return Geo.error("该任务已存在:" + scheduleJob.getJobName());
                }
            }
        }
        ScheduleJob scheduleJob1 = scheduleJobService.save(scheduleJob);
        return Geo.ok().put("scheduleJob",scheduleJob1);
    }
    /**
     * 修改定时任务
     */
    @SysLog("修改定时任务")
    @RequestMapping("/update")
    @RequiresPermissions("sys:schedule:update")
    public Geo update(@RequestBody ScheduleJob scheduleJob){
        ValidatorUtils.validateEntity(scheduleJob);
        scheduleJob.setUserId(getUser().getUniqueCode());
        scheduleJobService.update(scheduleJob);

        return Geo.ok();
    }

    /**
     * 删除定时任务
     */
    @SysLog("删除定时任务")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:schedule:delete")
    public Geo delete(Long[] jobIds){

        scheduleJobService.deleteBatch(jobIds);

        return Geo.ok();
    }

    /**
     * 删除定时任务（可恢复）
     */
    @SysLog("删除可恢复")
    @RequestMapping("/updateStatus")
    @RequiresPermissions("sys:schedule:update")
    public Geo updateStatus(@RequestBody ScheduleJob scheduleJob){
        scheduleJob.setUserId(getUser().getUniqueCode());
        scheduleJobService.updateForDelete(scheduleJob);

        return Geo.ok();
    }

    /**
     * 删除任务预查询（是否有未执行完的自任务）
     */
    @SysLog("删除任务预查询")
    @RequestMapping("/beforeDelete")
    //@RequiresPermissions("sys:schedule:delete")
    public Geo beforeDelete(@RequestBody Map<String, Object> map){
        map.put("userId",getUser().getUniqueCode());
        List<ScheduleTask> scheduleTasks = scheduleJobService.beforeDelete(map);
        if(scheduleTasks.size() > 0){
            return Geo.ok().put("msg",true);
        }
        return Geo.ok().put("msg",false);
    }
    /**
     * 立即执行任务
     */
    @SysLog("立即执行任务")
    @RequestMapping("/run")
    //@RequiresPermissions("sys:schedule:run")
    public Geo run(Long[] jobIds){
        Long userId = getUser().getUniqueCode();
        scheduleJobService.run(jobIds,userId);

        return Geo.ok();
    }

    /**
     * 暂停定时任务
     */
    @SysLog("暂停定时任务")
    @RequestMapping("/pause")
    @RequiresPermissions("sys:schedule:pause")
    public Geo pause(Long[] jobIds){
        scheduleJobService.pause(jobIds);

        return Geo.ok();
    }

    /**
     * 恢复定时任务
     */
    @SysLog("恢复定时任务")
    @RequestMapping("/resume")
    @RequiresPermissions("sys:schedule:resume")
    public Geo resume(Long[] jobIds){
        List<Dimension> dimensions = dimensionService.queryListByJobIds(jobIds);
        if(dimensions!=null&&!dimensions.isEmpty()){
            for(Dimension dimension:dimensions){
                if(dimension.getStatus() == 1){
                    return Geo.error("监控策略已禁用，无法恢复任务");
                }
            }
        } else {
            return Geo.error("监控策略已删除，无法恢复任务");
        }
        scheduleJobService.resume(jobIds);
        return Geo.ok();
    }

    /**
     * 各状态任务数量
     */
    @SysLog("各状态任务数量")
    @RequestMapping("/jobStatus")
    //@RequiresPermissions("sys:schedule:update")
    public Geo jobStatus(){
        Long userId = getUser().getUniqueCode();
        ScheduleJob scheduleJob = scheduleJobService.queryJobStatusNum(userId);
        return Geo.ok().put("data",scheduleJob);
    }


}
