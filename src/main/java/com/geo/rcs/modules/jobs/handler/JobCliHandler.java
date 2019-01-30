package com.geo.rcs.modules.jobs.handler;

import com.alibaba.fastjson.JSON;
import com.geo.rcs.common.exception.ServiceException;
import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.HistogramConsole;
import com.geo.rcs.modules.admin.taskmanger.entity.QueueDetail;
import com.geo.rcs.modules.admin.taskmanger.entity.TaskManger;
import com.geo.rcs.modules.admin.taskmanger.service.TaskMangerService;
import com.geo.rcs.modules.jobs.entity.JobRegister;
import com.geo.rcs.modules.jobs.entity.JobWorker;
import com.geo.rcs.modules.jobs.service.RegisterService;
import com.geo.rcs.modules.jobs.service.jobStatisticsService;
import com.geo.rcs.modules.monitor.entity.ScheduleRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JobCliHandler {


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    @Autowired
    private TaskMangerService taskService;
    @Value("${spring.profiles.active}")
    private String environment;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private jobStatisticsService jobStatisticsService;
    @Value("${rabbitmq.taskroles}")
    private String[] taskroles;
    private static  final  String all="ALL";
    /**
     * 显示队列，默认5s间隔
     */
    public void displayWorkers() {

        while (true) {
            System.out.println(ANSI_GREEN + "========= JobCli running at " +
                    time2String(System.currentTimeMillis()) + "=========" + ANSI_RESET);
            _displayWorker();
            //定时统计
            timeStatis();
            try {

                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示队列, 定制时间间隔
     */
    public void timeStatis() {

        //测试开启,生产关闭
//        if (!"pro".equalsIgnoreCase(environment)&&!"test".equalsIgnoreCase(environment)) {
        if (false) {
            test();
        } else {

            for (int i = 0; i <taskroles.length ; i++) {
                //        //最近一天,每小时统计一次,保存24小时数据
                Boolean minuteResult = jobStatisticsService.statisticsApi(taskroles[i],"minute", 1, 60);
                Boolean result = jobStatisticsService.statisticsApi(taskroles[i],"hour", 1, 24);
//        //最近一周,每天统计一次,保存7天数据
                Boolean dayResult = jobStatisticsService.statisticsApi(taskroles[i],"day", 1, 7);
//        //最近一个月,每天统计一次,保存30天
                Boolean monthResult = jobStatisticsService.statisticsApi(taskroles[i],"day", 1, 30);
            }
            //        //最近一天,每小时统计一次,保存24小时数据
            Boolean minuteResult = jobStatisticsService.statisticsApi(all,"minute", 1, 60);
            Boolean result = jobStatisticsService.statisticsApi(all,"hour", 1, 24);
//        //最近一周,每天统计一次,保存7天数据
            Boolean dayResult = jobStatisticsService.statisticsApi(all,"day", 1, 7);
//        //最近一个月,每天统计一次,保存30天
            Boolean monthResult = jobStatisticsService.statisticsApi(all,"day", 1, 30);
        }


    }

    private void test() {
        for (int i = 0; i <taskroles.length ; i++) {
            //        //最近一天,每小时统计一次,保存24小时数据
            Boolean minuteResult = jobStatisticsService.statisticsApi(taskroles[i],"minute", 1, 60);
            Boolean result = jobStatisticsService.statisticsApi(taskroles[i],"hour", 1, 24);
//        //最近一周,每天统计一次,保存7天数据
            Boolean dayResult = jobStatisticsService.statisticsApi(taskroles[i],"day", 1, 7);
//        //最近一个月,每天统计一次,保存30天
            Boolean monthResult = jobStatisticsService.statisticsApi(taskroles[i],"day", 1, 30);
        }
    }

    /**
     * 显示队列, 定制时间间隔
     *
     * @param interval
     */

    public void displayWorkers(int interval) {

        while (true) {
            System.out.println(ANSI_GREEN + "========= JobCli running at " +
                    time2String(System.currentTimeMillis()) + "=========" + ANSI_RESET);
            _displayWorker();

            try {
                Thread.sleep(interval * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 子方法：显示任务队列和状态
     */
    public void _displayWorker() {

        Long now = System.currentTimeMillis();
        Long maxHeartTime = 120 * 1000L;


        HashMap<String, List> allworkers = getAllworkers();

        List<JobWorker> workers = allworkers.get("WORKER");
        List<ScheduleRegister> schedulers = allworkers.get("SCHEDULE");
        ArrayList<JobWorker> jobWorkers = new ArrayList<>();
        if (workers.size() == 0 && schedulers.size() == 0) {
            System.out.println("\n " + " No Worker Connected. ");
        } else {
            //输出 worker
            if (workers.size() > 0) {
                for (JobWorker worker : workers) {
                    printRegister(now, worker, maxHeartTime, 1);
                }
            }
            //输出 scheduler
            if (schedulers.size() > 0) {
                if (workers.size() > 0) {
                    System.out.println();
                }
                for (ScheduleRegister schedu : schedulers) {
                    printRegister(now, schedu, maxHeartTime, 2);
                }
            }
            //输出统计信息
            try {
                String role = "ALL";
                printStatisticsInfo(role);
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        System.out.println();
        System.out.println();
        System.out.println();
    }

    //输出统计信息
    private void printStatisticsInfo(String role) throws ServiceException, ParseException {

        System.out.println();
        System.out.println();
        TaskManger taskManger = taskService.overView(role);
        HashMap<String, Object> overViewMap = new HashMap<>();
        overViewMap.put("任务队列数量", taskManger.getQueueCount());
        overViewMap.put("消费者数量", taskManger.getCustomerCount());
        overViewMap.put("调度者数量", taskManger.getScheduerCount());
        overViewMap.put("任务分发数量", taskManger.getTaskScheduCount());
        overViewMap.put("任务执行数量", taskManger.getTaskExcuteCount());
        overViewMap.put("任务吞吐量", taskManger.getTaskThroughPut());
        overViewMap.put("运行状态", taskManger.isStatus() == true ? "正常" : "异常");
        overViewMap.put("任务执行较前日", taskManger.getTaskExecuteDayChange());
        overViewMap.put("任务执行近七天", taskManger.getTaskExecuteWeekChange());
        overViewMap.put("任务分发较前日", taskManger.getTaskScheduDayChange());
        overViewMap.put("任务分发近七天", taskManger.getTaskScheduWeekChange());
        System.out.println("==================数据概览==================");
        System.out.println(overViewMap.toString());
        List<Map<Object, Object>> hour = taskService.taskDetail("minute", 1, 60,role);
        List<Map<Object, Object>> day = taskService.taskDetail("hour", 1, 24,role);
        List<Map<Object, Object>> week = taskService.taskDetail("day", 1, 7,role);
        List<Map<Object, Object>> month = taskService.taskDetail("day", 1, 30,role);
        System.out.println();
        System.out.println("=================任务分发分析===============");
        System.out.println("最近一小时: " + hour.toString());
        System.out.println("最近一天:   " + day.toString());
        System.out.println("最近一周:   " + week.toString());
        System.out.println("最近一月:   " + month.toString());


        //
        Long[] customArr = new Long[hour.size()];
        Long[] distrbuteArr = new Long[hour.size()];
        String[] dateArr = new String[hour.size()];
        ArrayList<String> dateList= new ArrayList<>(hour.size());
        Date date=null;
        for (int i=0,len=hour.size();i<len;i++){
            Map<Object, Object> map = hour.get(i);
            Object customSuccessTotal = map.get("customSuccessTotal");
            Object distributionSuccess = map.get("distributionSuccess");
           if (customSuccessTotal instanceof  String ){
               customArr[i]=Long.valueOf((String) map.get("customSuccessTotal"));
               distrbuteArr[i]=Long.valueOf((String)map.get("distributionSuccess"));
           }else {
               customArr[i]=(Long) map.get("customSuccessTotal");
               distrbuteArr[i]=(Long)map.get("distributionSuccess");
           }

            String time = (String) map.get("time");
            date= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            String mm = DateUtils.format(date, "mm");
            dateArr[i]=mm;
        }
        try {
            System.out.println("=================分发成功分析===============");
            if (dateArr!=null&&dateArr.length>0){
            HistogramConsole.echo(dateArr,distrbuteArr);
            }else {
                System.out.println("暂无数据!");
            }
            System.out.println("=================执行成功分析===============");
            if (dateArr!=null&&dateArr.length>0){
                HistogramConsole.echo(dateArr,customArr);
            }else {
                System.out.println("暂无数据!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println();
            QueueDetail queueDetail = taskService.quueDetail(role);
            System.out.println("==================队列信息==================");
            HashMap<String, Object> queueMap = new HashMap<>();
            queueMap.put("队列数量", queueDetail.getQueueNum());
            queueMap.put("单次任务分发限制", queueDetail.getEachTimeMax());
            queueMap.put("队列容量上限", queueDetail.getQueueCapacity());
            List<Map<Object, Object>> queueMapList = queueDetail.getQueueMapList();
            System.out.println(queueMap.toString());
            for (int i = 0, len = queueMapList.size(); i < len; i++) {
                System.out.println(queueMapList.get(i).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printRegister(Long now, JobRegister worker, Long maxHeartTime, int type) {
        String _msg = null;
        String taskRole = null;
        String taskStatus = null;
        if (type == 1) {
            _msg = "[WORKER]\t\t{0}\t\t{1}\t\t{2}\t\t{3}\t\t{4}\t\t{5}\t\t{6}\t\t{7}\t\t{8}";

        } else if (type == 2) {
            _msg = "[SCHEDULE]\t\t{0}\t\t{1}\t\t{2}\t\t{3}\t\t{4}\t\t{5}\t\t{6}\t\t{7}\t\t{8}";
        }
        taskRole = worker.getTaskRole();
        taskStatus = worker.getTaskStatus() == 0 ? "Wait" : "Busy";
        if (now - maxHeartTime > worker.getUpdateTime()) {
            worker.setWorkStatus(0);
            registerService.deleteRegisterInfoByKey(worker.getId());
        }
        String workStatus = worker.getWorkStatus() == 0 ? "Dead" : "Live";
        workStatus = worker.getWorkStatus() == 1 ? "Live" : workStatus;
        workStatus = worker.getWorkStatus() == 2 ? "STOP" : workStatus;

        String msg = MessageFormat.format(_msg,
                worker.getWorkerName() == null ? "----------" : worker.getWorkerName(),
                worker.getId(),
                workStatus,
                taskStatus,
                taskRole,
                worker.getIp(),
                worker.getQueneNameList() == null ? "--------------------------------" : worker.getQueneNameList(),
                worker.getRegistTimeString(),
                worker.getUpdateTimeString()
        );

        if (worker.getWorkStatus() == 0) {
            System.out.println(ANSI_BLUE + msg + ANSI_RESET);
        } else if (worker.getTaskStatus() == 0) {
            System.out.println(ANSI_GREEN + msg + ANSI_RESET);
        } else if (worker.getTaskStatus() == 1) {
            System.out.println(ANSI_RED + msg + ANSI_RESET);
        }
    }

    /**
     * 子方法: 获取所有
     *
     * @return
     */
    private HashMap<String, List> getAllworkers() {
        Map<String, String> _workers = registerService.getAllRegisterInfo();

        List<HashMap<String, String>> workers = new ArrayList<HashMap<String, String>>();

        HashMap<String, List> map = new HashMap<>();
        ArrayList<JobWorker> jobWorkerList = new ArrayList<>();
        ArrayList<ScheduleRegister> jobSchedulerList = new ArrayList<ScheduleRegister>();

        String typeStr = null;
        for (Map.Entry<String, String> entry : _workers.entrySet()) {

            Object type = JSON.parseObject(entry.getValue(), HashMap.class).get("type");
            if (type instanceof String) {
                typeStr = (String) type;
                if ("WORKER".equalsIgnoreCase(typeStr)) {
                    jobWorkerList.add(JSON.parseObject(entry.getValue(), JobWorker.class));
                } else if ("SCHEDULE".equalsIgnoreCase(typeStr)) {
                    jobSchedulerList.add(JSON.parseObject(entry.getValue(), ScheduleRegister.class));
                } else {
                    System.out.println("不识别!");
                }
            } else {

            }
        }
        map.put("WORKER", jobWorkerList);
        map.put("SCHEDULE", jobSchedulerList);
        return map;
    }

    /**
     * 子方法: 获取所有
     *
     * @return
     */
    private List<JobWorker> getAllworkers2() {
        Map<String, String> _workers = registerService.getAllRegisterInfo();

        List<JobWorker> workers = new ArrayList<JobWorker>();

        for (Map.Entry<String, String> entry : _workers.entrySet()) {
            workers.add(JSON.parseObject(entry.getValue(), JobWorker.class));
        }
        return workers;
    }


    /**
     * 工具方法: time String formattor
     *
     * @param rawtime
     * @return timeString
     */
    public static String time2String(long rawtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String timeString = sdf.format(rawtime);
        return timeString;
    }

}
