package com.geo.rcs.modules.admin.taskmanger.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.util.LogUtil;
import com.geo.rcs.modules.admin.taskmanger.service.TaskMangerService;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author qiaoShengLong
 * @email qiaoshenglong@geotmt.com
 * @time 2018年08月22日 上午10:14:41
 */
@RestController
@RequestMapping("/taskManger")
public class TaskMangerController extends BaseController {
    @Autowired
    private TaskMangerService taskService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Value("${spring.profiles.active}")
    private String environment;

    /**
     * 获取任务概览
     *
     * @param request
     * @param response
     */
    @RequestMapping("/overView")
//	@RequiresPermissions("task:overView") // 权限管理
    public void overView(@RequestBody Map<String,String> map, HttpServletRequest request, HttpServletResponse response) {
        try {
            String role = map.get("role");
            this.sendData(request, response, taskService.overView(role));
            this.sendOK(request, response);
        } catch (Exception e) {
            LogUtil.error("获取任务概览失败", "", getGeoUser().getName(), e);

            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }


    /**
     * 清除统计信息
     *
     * @param request
     * @param response
     */
    @RequestMapping("/delData")
//	@RequiresPermissions("task:overView") // 权限管理
    public void delData(HttpServletRequest request, HttpServletResponse response) {
        try {
            this.sendData(request, response, taskService.delData());
            this.sendOK(request, response);
        } catch (Exception e) {
            LogUtil.error("清除统计信息", "", getGeoUser().getName(), e);
            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 任务分发分析
     *
     * @param flagMap
     * @param request
     * @param response
     */
    @RequestMapping("/taskDetail")
//	@RequiresPermissions("task:taskDetail") // 权限管理
    @ResponseBody
    public void taskDetail(@RequestBody Map<String, String> flagMap, HttpServletRequest request, HttpServletResponse response) {
        try {
            String role = flagMap.get("role");
            String flagStr = flagMap.get("flag");
            String unit = "";
            int interval = 1;
            int size = 0;
//            if (!"pro".equalsIgnoreCase(environment)&&!"test".equalsIgnoreCase(environment)) {
            if (false) {

                if ("hour".equalsIgnoreCase(flagStr)) {
                    unit = "minute";
                    interval = 1;
                    size = 60;
                } else if ("day".equalsIgnoreCase(flagStr)) {
                    unit = "minute";
                    interval = 1;
                    size = 24;
                } else if ("week".equalsIgnoreCase(flagStr)) {
                    unit = "minute";
                    interval = 2;
                    size = 7;
                } else if ("month".equalsIgnoreCase(flagStr)) {
                    unit = "minute";
                    interval = 3;
                    size = 30;
                } else {
                    this.sendError(request, response, StatusCode.PARAMS_ERROR.getMessage());
                }
            } else {
                if ("hour".equalsIgnoreCase(flagStr)) {
                    unit = "minute";
                    interval = 1;
                    size = 60;
                } else if ("day".equalsIgnoreCase(flagStr)) {
                    unit = "hour";
                    interval = 1;
                    size = 24;
                } else if ("week".equalsIgnoreCase(flagStr)) {
                    unit = "day";
                    interval = 1;
                    size = 7;
                } else if ("month".equalsIgnoreCase(flagStr)) {
                    unit = "day";
                    interval = 1;
                    size = 30;
                } else {
                    this.sendError(request, response, StatusCode.PARAMS_ERROR.getMessage());
                }
            }
            this.sendData(request, response, JSON.toJSON(taskService.taskDetail(unit, interval, size,role)));
            this.sendOK(request, response);
        } catch (Exception e) {
            LogUtil.error("任务分发分析失败!", "" + "", getGeoUser().getName(), e);
            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 获取队列信息
     *
     * @param request
     * @param response
     */
    @RequestMapping("/quueDetail")
//	@RequiresPermissions("task:overView") // 权限管理
    public void quueDetail(@RequestBody Map<String,String> map,HttpServletRequest request, HttpServletResponse response) {
        try {
            String role = map.get("role");
            this.sendData(request, response, JSON.toJSON(taskService.quueDetail(role)));
            this.sendOK(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取队列信息", "", getGeoUser().getName(), e);

            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }

    /**
     * 获取注册者信息
     *
     * @param request
     * @param response
     */
    @RequestMapping("/getRegistrars")
//	@RequiresPermissions("task:overView") // 权限管理
    @ResponseBody
    public void getRegistrars(@RequestBody Map<String,String> map,HttpServletRequest request, HttpServletResponse response) {
        try {
            String role = map.get("role");
            this.sendData(request, response, JSON.toJSON(taskService.getRegistrars(role)));
            this.sendOK(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(" 获取注册者信息", "", getGeoUser().getName(), e);

            this.sendError(request, response, StatusCode.SERVER_ERROR.getMessage());
        }
    }


}
