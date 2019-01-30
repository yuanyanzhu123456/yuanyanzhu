package com.geo.rcs.modules.monitor.controller;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.BaseController;
import com.geo.rcs.common.StatusCode;
import com.geo.rcs.common.annotation.SysLog;
import com.geo.rcs.common.constant.Constant;
import com.geo.rcs.common.exception.RcsException;
import com.geo.rcs.common.util.*;
import com.geo.rcs.common.util.excel.ExportExcel;
import com.geo.rcs.common.util.excel.ReadExcel;
import com.geo.rcs.modules.monitor.entity.Dimension;
import com.geo.rcs.modules.monitor.entity.ScheduleTask;
import com.geo.rcs.modules.monitor.service.ScheduleTaskService;
import com.geo.rcs.modules.monitor.service.TaskLogService;
import com.geo.rcs.modules.monitor.task.SendMessage;
import com.geo.rcs.modules.sys.entity.SysUser;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.monitor.controller
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月19日 下午2:16
 */
@Controller
@RequestMapping("/monitor/task")
@Component
public class TaskController extends BaseController {

    @Autowired
    private ScheduleTaskService taskService;
    @Autowired
    private TaskLogService taskLogService;

    private ArrayList list;

    /**
     * 预览数据
     *
     * @param file
     * @return
     */
    @RequestMapping("/preview")
    @ResponseBody
    public Geo preview(MultipartFile file) {
        try {
            if (!BlankUtil.isBlank(file)) {
                List<ScheduleTask> excelInfo = new ReadExcel().getExcelInfo(file, 1);

                if(excelInfo != null ){
                    return Geo.ok().put("data", excelInfo);
                }
                return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), "请上传正确格式的文件");
            }
            return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), "请上传正确格式的文件");
        } catch (Exception e) {
            return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), e.getMessage());
        }
    }

    /**
     * 上传名单
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadList")
    @ResponseBody
    @RequiresPermissions("monitor:task:save")
    public Geo uploadList(MultipartFile file,Integer jobId) {

        try {
            String paramJson = taskService.getParamByJobId(jobId);
            if (!BlankUtil.isBlank(file)) {
                List<ScheduleTask> excelInfo = new ReadExcel().getExcelInfo(file, 0);

                if (!BlankUtil.isBlank(excelInfo)) {
                    for (ScheduleTask scheduleTask : excelInfo) {
                        JSONUtil.jsonToMap(scheduleTask.getParmsJson());
                        scheduleTask.setJobId(jobId);
                        scheduleTask.setUserId(getUser().getUniqueCode());
                        checkParamMap(scheduleTask.getParmsJson(),paramJson);
                    }
                    taskService.saveBatch(excelInfo);
                }
                return Geo.ok();
            }
            return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), "请上传正确格式的文件");
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof RcsException){
                return Geo.error(((RcsException) e).getCode(),((RcsException) e).getMsg());
            }
            return Geo.error(StatusCode.FILE_UPLOAD_ERRO.getCode(), StatusCode.FILE_UPLOAD_ERRO.getMessage());
        }

    }

    /**
     * 获取维度
     *
     * @param
     * @return dimensionList
     */
    @RequestMapping("/getDimension")
    @ResponseBody
    public Geo getDimension() {

        try {
            Long unicode = getUser().getUniqueCode();
            List<Dimension> dimensions = taskService.getDimension(unicode);
            return Geo.ok().put("data", dimensions);
        } catch (Exception e) {
            return Geo.error();
        }

    }
    /**
     * 错误重试
     *
     * @param
     * @return dimensionList
     */
    @PostMapping("/retry")
    @ResponseBody
    public Geo Retry(@RequestBody ConcurrentHashMap hashMap) {
        try {
            //推送到队列等待消费
            hashMap.put("userId",getUser().getUniqueCode());
            ScheduleTask scheduleTask = taskService.queryObject(hashMap);
            hashMap.put("distributeNum",scheduleTask.getDistributeNum()-1);
            hashMap.put("executeStatus",Constant.DistributeStatus.RUNNING.getValue());
            hashMap.put("taskStatus",Constant.DistributeStatus.RUNNING.getValue());
            hashMap.put("readStatus",Constant.DistributeStatus.INIT.getValue());
            taskService.updateByPrimaryKey(hashMap);
            return Geo.ok();
        } catch (Exception e) {
            return Geo.error();
        }

    }

    /**
     * 监控清单 （模糊，分页）
     *
     * @param map
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    @RequiresPermissions("monitor:task:list")
    public Geo list(@RequestBody Map<String, Object> map) {
        try {
            SysUser user = getUser();
            if (user == null) {
                return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
            }

            if (map == null || map.isEmpty()) {
                return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "参数不能为空！");
            }
            //Query query = new Query(map);
            //query.put("userId", getUser().getUniqueCode());
            map.put("userId", getUser().getUniqueCode());
            PageInfo<ScheduleTask> pageInfo = new PageInfo<>(taskService.findByPage(map));

            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", pageInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error(" 名单列表 （模糊，分页）", map.toString(), getUser().getName(), e);
            return Geo.error();

        }
    }

    /**
     * 报警数据 （模糊，分页）
     *
     * @param map
     * @return
     */
    @PostMapping("/alertList")
    @ResponseBody
    @RequiresPermissions("monitor:task:alertList")
    public Geo alertList(@RequestBody Map<String, Object> map) {
        try {
            SysUser user = getUser();
            if (user == null) {
                return Geo.error(StatusCode.EXPIRED.getCode(), "用户信息已过期，请重新登录");
            }

            if (map == null || map.isEmpty()) {
                return Geo.error(StatusCode.PARAMS_ERROR.getCode(), "参数不能为空！");
            }
            //Query query = new Query(map);
            //query.put("userId", getUser().getUniqueCode());
            map.put("userId", getUser().getUniqueCode());
            PageInfo<ScheduleTask> pageInfo = new PageInfo<>(taskService.findByPage(map));

            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", pageInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error(" 名单列表 （模糊，分页）", map.toString(), getUser().getName(), e);
            return Geo.error();

        }
    }

    /**
     * 人工审核
     */
    @SysLog("人工报警")
    @PostMapping("/alert")
    @ResponseBody
    @RequiresPermissions("sys:task:alert")
    @Transactional
    public Geo artificialAudit(@RequestBody Map<String, Object> map){
        Long userId = getUser().getUniqueCode();
        map.put("userId",userId);
        map.put("operator", getUser().getName());
        try {
            taskService.updateByPrimaryKey(map);
            taskLogService.updateByPrimaryKey(map);
        }catch (Exception e){
            LogUtil.error("人工报警", "请求参数:" + map, userId.toString(),e);
            return Geo.error("任务报警失败");
        }
        LogUtil.info("人工报警", "请求参数:" + map, userId.toString(),"成功");
        return Geo.ok();
    }

    /**
     * 导出Excel
     * @return
     */
    @SysLog("下载名单模版")
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    @ResponseBody
    public Geo export(HttpServletResponse response, HttpServletRequest request){

        try {
            List<Map<String, Object>> mapList = new ArrayList<>();
            List<Integer> intList = new ArrayList<>();
            String fileName = "名单模版_"+ DateUtils.format(new Date(),"yyyyMMddHHmmss")+".xlsx";
            String[] headers = new String[]{"客户姓名","身份证号","手机号","开始时间"};
            String[] props1 = new String[]{"realName","idNumber","cid","startTime"};
            String[] props2 = new String[]{"productType","productName","productExplain","productQuota","cooperationPoint","productConsumeRation","cooperationStatus","remaker"};

            ExportExcel excel = new ExportExcel("名单模版", headers);
            excel.write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("下载名单模版", "", getUser().getName(), e);
            return Geo.error(9000,LogUtil.getStackTraceInfo(e));

        }
        return Geo.ok();
    }

    /**
     * 删除名单
     */
    @SysLog("删除名单")
    @RequestMapping("/delete")
    //@RequiresPermissions("sys:schedule:delete")
    public Geo delete(Long[] jobIds){
        try {
            taskService.deleteBatch(jobIds,getUser().getUniqueCode());
            return Geo.ok();
        }catch (Exception e){
            return Geo.error("删除失败");
        }

    }

    /**
     * 各状态名单数量
     */
    @SysLog("各状态名单数量")
    @RequestMapping("/taskStatus")
    @ResponseBody
    @RequiresPermissions("sys:task:taskStatus")
    public Geo jobStatus(Long jobId){
        Long userId = getUser().getUniqueCode();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId",userId);
        hashMap.put("jobId",jobId);
        ScheduleTask scheduleTask = taskService.queryTaskStatusNum(hashMap);
        return Geo.ok().put("data",scheduleTask);
    }

    /**
     * 报警数据
     */
    @RequestMapping("/alarmData")
    @ResponseBody
    public Geo alarmData(@RequestBody Map<String, Object> map){
        try {
            map.put("userId", getUser().getUniqueCode());
            PageInfo<ScheduleTask> pageInfo = new PageInfo<>(taskService.getAlarmData(map));
            return Geo.ok(StatusCode.SUCCESS.getMessage()).put("data", pageInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.error(" 报警数据 （模糊，分页）", map.toString(), getUser().getName(), e);
            return Geo.error();

        }
    }

    private void checkParamMap(String taskParameter, String parametersMap) {
        int paramCount = 0;
        Hashtable rulesParams = JSON.parseObject(parametersMap, Hashtable.class);
        Hashtable map = JSON.parseObject(taskParameter, Hashtable.class);
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if (rulesParams.keySet().contains(key)) {
                System.out.println("" + key);
                paramCount += 1;
            }
        }
        if (paramCount != rulesParams.size()) {
            throw new RcsException("参数不齐全！", StatusCode.PARAMS_ERROR.getCode());
        }
    }




}
