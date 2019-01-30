package com.geo.rcs.modules.monitor.controller;

import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.common.validator.NotNull;
import com.geo.rcs.common.validator.ResultType;
import com.geo.rcs.common.validator.ValidateNull;
import com.geo.rcs.common.validator.ValidatorUtils;
import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.geo.rcs.modules.rule.field.entity.EngineRawField;
import com.geo.rcs.modules.rule.field.service.FieldService;
import com.geo.rcs.modules.rule.inter.entity.EngineInter;
import com.geo.rcs.modules.rule.inter.service.EngineInterService;
import com.geo.rcs.modules.sys.entity.PageInfo;
import com.github.pagehelper.Page;
import org.apache.commons.collections.FastHashMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 14:44 2018/8/1
 */
@RestController
@RequestMapping("/monitor/dimension")
public class DimensionController extends BaseController {

    @Autowired
    private DimensionService dimensionService;

    @Autowired
    private EngineInterService engineInterService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private ScheduleJobService scheduleJobService;


    /**
     * 策略列表
     *
     * @param dimension
     * @param request
     * @param response
     */
    @RequestMapping("/list")
    @RequiresPermissions("monitor:dimension:list")
    public void list(HttpServletRequest request, HttpServletResponse response, Dimension dimension) {
        try {
            // 添加unique_code （客户唯一标识）
            dimension.setUnicode(getUser().getUniqueCode());
            this.sendData(request, response, new PageInfo<>(dimensionService.findByPage(dimension, false)));
        } catch (Exception e) {
            this.sendError(request, response, "获取列表失败！");
            LogUtil.error("监控纬度列表（模糊，分页）", dimension.toString(), getUser().getName(), e);
        }
    }


    /**
     * 添加策略
     *
     * @param dimension
     * @param request
     * @param response
     */
    @SysLog("添加策略")
    @RequestMapping("/save")
    @RequiresPermissions("monitor:dimension:save")
    public void save(Dimension dimension, HttpServletRequest request, HttpServletResponse response) {
        ValidatorUtils.validateEntity(dimension, AddGroup.class);
        ResultType resultType = ValidateNull.check(dimension, NotNull.RequestType.NEW);
        if (ResultType.FAILD.equals(resultType)) {
            this.sendError(request, response, resultType.getMsg());
        }
        try {
            List<Dimension> dimensionList = dimensionService.getDimensionByName(dimension.getDimensionDesc(),getUser().getUniqueCode());
            if (dimensionList.size() > 0) {
                this.sendError(request, response, "该策略已存在:" + dimension.getDimensionDesc());
            } else {
                if(dimension.getPolicyType() == 3){
                    EngineRawField engineRawField = fieldService.selectById(dimension.getPolicyId());
                    EngineInter interById = engineInterService.getInterById(engineRawField.getInterId());
                    dimension.setParamJson(interById.getParameters());
                }
                // 添加unique_code （客户唯一标识）
                dimension.setUnicode(getUser().getUniqueCode());
                dimension.setUserName(getUser().getName());
                dimension.setCreateDate(new Date());
                dimensionService.save(dimension);
                this.sendOK(request, response);
            }
        } catch (Exception e) {
            this.sendError(request, response, "添加策略失败！");
            LogUtil.error("添加策略失败", dimension.toString(), getUser().getName(), e);
        }
    }


    /**
     *
     */
    @SysLog("修改策略")
    @RequestMapping("/update")
    @RequiresPermissions("monitor:dimension:update")
    public void update(Dimension dimension, HttpServletRequest request, HttpServletResponse response) {
        ValidatorUtils.validateEntity(dimension, UpdateGroup.class);
        try {
            List<Dimension> dimensionList = dimensionService.getDimensionByName(dimension.getDimensionDesc(),getUser().getUniqueCode());
            List<ScheduleJob> allDimension = scheduleJobService.getAllDimension();
            for (ScheduleJob scheduleJob : allDimension) {
                if(scheduleJob.getDimension().equals(dimension.getId())){
                    this.sendError(request, response, "该策略已在任务中使用，无法修改");
                    return;
                }
            }
            if (dimensionList.size() > 0 && !dimensionList.get(0).getId().equals(dimension.getId())) {
                this.sendError(request, response, "该策略已存在:" + dimension.getDimensionDesc());
            }
            else {
                if(dimension.getPolicyType() == 3){
                    EngineRawField engineRawField = fieldService.selectById(dimension.getPolicyId());
                    EngineInter interById = engineInterService.getInterById(engineRawField.getInterId());
                    dimension.setParamJson(interById.getParameters());
                }
                dimension.setUnicode(getUser().getUniqueCode());
                dimension.setUserName(getUser().getName());
                dimension.setUpdateDate(new Date());
                dimensionService.update(dimension);
                this.sendOK(request, response);
            }

        } catch (Exception e) {
            this.sendError(request, response, "更新策略失败！");
            LogUtil.error("更新策略失败", dimension.toString(), getUser().getName(), e);

        }
    }


    @SysLog("删除策略")
    @RequestMapping("/delete")
    @RequiresPermissions("monitor:dimension:delete")
    public void updateStatus(@RequestBody Integer[] ids, HttpServletRequest request, HttpServletResponse response) {


        try {
            dimensionService.delete(ids);
            this.sendOK(request, response);
        } catch (Exception e) {
            this.sendError(request, response, "删除策略失败！");
            LogUtil.error("删除策略失败", ids.toString(), getUser().getName(), e);

        }
    }

    /**
     * 根据编号获取策略
     *
     * @param id
     * @param request
     * @param response
     */
    @RequestMapping("/getDimensionById")
    public void getDimensionById(Integer id, HttpServletRequest request, HttpServletResponse response) {

        try {
            Dimension dimension = dimensionService.selectByPrimaryKey(id);
            this.sendData(request, response,dimension);
        } catch (Exception e) {
            this.sendError(request, response, "根据编号获取策略失败！");
            LogUtil.error("根据编号获取策略失败", id.toString(), getUser().getName(), e);

        }
    }

    /**
     * 接口类型
     * @param request
     * @param response
     * @param
     */
    @RequestMapping("/getInterList")
    public void getInterList(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response,engineInterService.getInterList());
        } catch (ServiceException e) {
            this.sendError(request, response, "获取接口失败！");
        }
    }


}
