package com.geo.rcs.modules.monitor.task;

import com.geo.rcs.common.util.DateUtils;
import com.geo.rcs.common.util.mail.MailService;
import com.geo.rcs.modules.jobs.service.RedisService;
import com.geo.rcs.modules.monitor.entity.ScheduleJob;
import com.geo.rcs.modules.monitor.service.DimensionService;
import com.geo.rcs.modules.monitor.service.ScheduleJobService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @ Author     ：wp.
 * @ Date       ：Created in 11:38 2019/1/22
 */
@Component
@EnableScheduling
public class MonitorEmailTask implements SchedulingConfigurer {
    @Autowired
    private MailService mailService;
    @Autowired
    private ScheduleJobService scheduleJobService;
    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private RedisService redisService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadFactory emailThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("email-pool-%d").build();
    private ExecutorService fixedThreadPool = new ThreadPoolExecutor(0, 10,
            30, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),emailThreadFactory);;

    @Value("${monitor.email.task.time}")
    private String monitorEmailTaskTime;
    private static String MONITOR_EMAIL_JOB_ID_SET = "RCS2:MONITOR:EMAIL:JOB";
    private static String MONITOR_EMAIL_JOB_ID_SET_LOCK = "RCS2:MONITOR:EMAIL:JOB_LOCK";
    private static String EMAIL_FILE_PATH = "src/main/resources/static/mail/excel/";

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final Long RELEASE_SUCCESS = 1L;

    private void sendMail(Long jobId, Long userId) {
        String subject;
        String[] content = new String[11];
        //解决附件文件名称过长乱码问题
        System.setProperty("mail.mime.splitlongmapeters", "false");
        Map<String, Object> map = new HashMap<>(3);
        map.put("jobId", jobId);
        map.put("userId", userId);
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        c.setTime(now);
        c.add(Calendar.DATE,-1);
        Date startTime = c.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = df.format(startTime);
        map.put("yesterday",yesterday);
        ScheduleJob scheduleJob = scheduleJobService.queryAlarm(map);

        try {
            //发送主题
            subject = "【风险监控】" + scheduleJob.getJobName() + DateUtils.format(new Date(), "yyyyMMdd") + "-" + (1);
            //报警数据
            Map<String, Object> countMap = scheduleJobService.getCountMap(map);
            logger.info("【风险监控】定时发送邮件 scheduleJob:"+scheduleJob.toString());
            List<Map<String,Object>> alarmData = scheduleJobService.getAlarmData(map);
            int exceptionCount = Integer.valueOf(countMap.get("exceptionCount").toString());
            int normalCount = Integer.valueOf(countMap.get("normalCount").toString());
            int failCount = Integer.valueOf(countMap.get("failCount").toString());
            long effectiveCount = exceptionCount + normalCount + failCount;
            //总数为0就不发
            if(effectiveCount == 0L) {
                return;
            }
            //报告编号
            content[0] = DateUtils.format(new Date(), "yyyyMMdd") + "-" + (1);
            //监控任务名
            content[1] = scheduleJob.getJobName();
            //监控策略名
            content[2] = dimensionService.selectByPrimaryKey(scheduleJob.getDimension()).getDimensionDesc();
            //监控时间
            content[3] = yesterday;
            //处理时长
            Long handleTime = scheduleJobService.queryHandleTime(map);
            if (handleTime == null) {
                content[4] = "0S";
            } else {
                content[4] = handleTime.toString()+"S";
            }
            //监控总数
            content[5] = String.valueOf(effectiveCount);
            //报警条数
            content[7] = String.valueOf(exceptionCount);
            //正常条数
            content[8] = String.valueOf(normalCount);
            //错误条数
            content[9] = String.valueOf(failCount);
            //停止条数
            content[10] = (scheduleJob.getHitPolicy() == 0)?String.valueOf(failCount+exceptionCount):String.valueOf(failCount);
            //有效监控数
            content[6] = Long.toString(normalCount+exceptionCount);
            String fileName = subject + ".xlsx";
            String filePath = EMAIL_FILE_PATH+fileName;
            export(content, alarmData,fileName,scheduleJob.getHitPolicy());
            String[] emailArray;
            if(scheduleJob.getAlarmEmail()!=null && scheduleJob.getAlarmEmail().contains(";")){
                Set<String> emailSet = new HashSet<>(Arrays.asList(scheduleJob.getAlarmEmail().split(";")));
                emailArray = new String[emailSet.size()];
                int i = 0;
                for(String email:emailSet){
                    emailArray[i] = email;
                    i++;
                }
            } else {
                emailArray = new String[]{scheduleJob.getAlarmEmail()};
            }
            mailService.sendAlarmMail(emailArray, null, null, subject,content,filePath,fileName);
            logger.info("【风险监控】定时邮件发送成功：jobId:"+scheduleJob.getJobId()+"收件人："+ Arrays.toString(emailArray));
            delete(filePath);
        } catch (Exception e) {
            logger.error("【风险监控】定时发送邮件异常",e);
        }
    }

    public void export(String[] content, List<Map<String, Object>> alarmData, String fileName,int hit_policy) {
        String[] titleCol = new String[]{"报告编号：", "监控任务名：", "监控策略名：", "监控时间", "处理时长：", "监控总数：", "有效监控数：", "报警条数：", "正常条数：", "错误条数：", "停止条数："};
        String[] titleRow = new String[]{"姓名", "身份证号", "手机号", "监控状态", "报警时间", "上次监控状态", "监控进度", "命中策略", "报警方式"};
        //创建workbook
        Workbook workbook = new XSSFWorkbook();
        Row rows;
        Cell cells;
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet1 = workbook.createSheet("监控报告");
        Sheet sheet2 = workbook.createSheet("报告详情");

        // sheet1创建第0行 也就是标题
        rows = sheet1.createRow(0);
        // 设备标题的高度
        rows.setHeightInPoints(18);
        // 创建标题第一列
        cells = rows.createCell(0);
        // 合并列
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        // 设置值标题
        cells.setCellValue("报告汇总信息");
        // 第三步创建标题的单元格样式style2以及字体样式headerFont1
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        // 创建字体样式
        Font headerFont1 = workbook.createFont();
        // 字体加粗
        headerFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体类型
        headerFont1.setFontName("黑体");
        // 设置字体大小
        headerFont1.setFontHeightInPoints((short) 13);
        // 为标题样式设置字体样式
        style.setFont(headerFont1);
        // 设置标题样式
        cells.setCellStyle(style);

        //sheet2创建第0行 也就是标题
        rows = sheet2.createRow(0);
        // 设备标题的高度
        rows.setHeightInPoints(18);
        sheet1.setColumnWidth((short) 0, (short) 3500);
        sheet2.setColumnWidth((short) 0, (short) 3500);
        sheet2.setColumnWidth((short) 1, (short) 7000);
        sheet2.setColumnWidth((short) 2, (short) 5000);
        sheet2.setColumnWidth((short) 4, (short) 5000);
        sheet2.setColumnWidth((short) 7, (short) 5000);

        //创建sheet1
        for (int i=0;i<titleCol.length;i++){
            rows = sheet1.createRow(i+1);
            rows.createCell(0).setCellValue(titleCol[i]);
            rows.createCell(1).setCellValue(content[i]);
        }

        //创建sheet2
        if (alarmData.size()>0) {
            for (int i = 0; i < alarmData.size(); i++) {
                rows = sheet2.createRow(0);
                for (int j = 0; j < titleRow.length; j++) {
                    cells = rows.createCell(j);
                    cells.setCellValue(titleRow[j]);
                }

            }
            for (int i = 0; i < alarmData.size(); i++) {
                rows = sheet2.createRow(i+1);
                cells = rows.createCell(0);
                cells.setCellValue(String.valueOf(alarmData.get(i).get("name")));
                cells = rows.createCell(1);
                cells.setCellValue(String.valueOf(alarmData.get(i).get("id_number")));
                cells = rows.createCell(2);
                cells.setCellValue(String.valueOf(alarmData.get(i).get("cid")));
                cells = rows.createCell(3);
                switch (alarmData.get(i).get("task_status").toString()) {
                    case "0":
                        cells.setCellValue("未监控");
                        break;
                    case "1":
                        cells.setCellValue("监控中");
                        break;
                    case "2":
                        cells.setCellValue("监控完成");
                        break;
                    case "3":
                        cells.setCellValue("停止监控");
                        break;
                    default:
                        cells.setCellValue("");
                }
                cells = rows.createCell(4);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cells.setCellValue(sdf.format(alarmData.get(i).get("update_time")));
                cells = rows.createCell(5);
                switch (alarmData.get(i).get("execute_status").toString()) {
                    case "0":
                        cells.setCellValue("错误");
                        break;
                    case "1":
                        cells.setCellValue("正常");
                        break;
                    case "2":
                        cells.setCellValue("执行中");
                        break;
                    case "3":
                        cells.setCellValue("报警");
                        break;
                    case "4":
                        cells.setCellValue("未执行");
                        break;
                    default:
                        cells.setCellValue("");
                }
                cells = rows.createCell(6);
                cells.setCellValue(String.valueOf(alarmData.get(i).get("distribute_num")+"/"+String.valueOf(alarmData.get(i).get("cycle_num"))));
                cells = rows.createCell(7);
                switch (hit_policy){
                    case 0:
                        cells.setCellValue("停止监控");
                        break;
                    case 1:
                        cells.setCellValue("继续监控");
                        break;
                    default:
                        cells.setCellValue("");
                }
                cells = rows.createCell(8);
                cells.setCellValue("邮件");
            }

        }
        // 设置表头高度
        rows.setHeightInPoints(20);
        // 创建字体样式
        Font headerFont = workbook.createFont();
        // 字体加粗
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体类型
        headerFont.setFontName("黑体");
        // 设置字体大小
        headerFont.setFontHeightInPoints((short) 14);
        // 为标题样式设置字体样式
        style.setFont(headerFont);


        //为数据内容设置特点新单元格样式1 自动换行 上下居中
        CellStyle autoLine = workbook.createCellStyle();
        // 设置自动换行
        autoLine.setWrapText(true);
        //设置边框
        autoLine.setBottomBorderColor(HSSFColor.BLACK.index);
        //为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
        CellStyle autoLine2 = workbook.createCellStyle();
        // 设置自动换行
        autoLine2.setWrapText(true);

        //设置边框
        autoLine2.setBottomBorderColor(HSSFColor.BLACK.index);
        try {
            File file = new File(EMAIL_FILE_PATH);
            while(!file.exists()){
                if(file.mkdirs()){
                    break;
                }
            }
            FileOutputStream fout = new FileOutputStream(EMAIL_FILE_PATH+fileName);
            workbook.write(fout);
            fout.flush();
            fout.close();
            logger.info("【风险监控】定时邮件-导出" + fileName + "成功");
        } catch (Exception e) {
            logger.error("【风险监控】定时邮件-导出" + fileName + "失败",e);
        }
    }

    /**
     * 删除excel文件
     */
    public static void delete(String filePath){
        File file = new File(filePath);
        while (file.exists() && file.isFile()) {
            if(file.delete()){
                break;
            }
        }
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        Runnable task = () -> {

            List<ScheduleJob> jobs = scheduleJobService.selectAllUsedJobs();
            Jedis jedis = null;
            try {
                jedis = redisService.getJedis();
                for (ScheduleJob job : jobs) {
                    if (verifyJob(job.getJobId().toString(),jedis)) {
                        fixedThreadPool.execute(() -> sendMail(job.getJobId(), job.getUserId()));
                    }
                }
                //设置key的过期时间为10分钟
                redisService.getJedis().expire(MONITOR_EMAIL_JOB_ID_SET,120);
            } catch (Exception e){
                logger.error("【风险监控】定时邮件-异常",e);
            } finally {
                if(jedis != null){
                    jedis.close();
                }
            }

        };

        Trigger trigger = triggerContext -> {
            CronTrigger trigger1 = new CronTrigger(monitorEmailTaskTime);
            return trigger1.nextExecutionTime(triggerContext);
        };
        scheduledTaskRegistrar.addTriggerTask(task, trigger);
    }

    private boolean verifyJob(String jobId,Jedis jedis){
        String requestId = UUID.randomUUID().toString();
        try {
            while (true){
                if(tryGetDistributedLock(jedis,requestId)){
                    break;
                }
            }
            logger.info("【风险监控】定时邮件-加锁：requestId="+requestId);
            if(jedis.sismember(MONITOR_EMAIL_JOB_ID_SET,jobId)){
                return false;
            } else {
                jedis.sadd(MONITOR_EMAIL_JOB_ID_SET,jobId);
                return true;
            }
        }catch (Exception e){
            logger.error("【风险监控】定时邮件-异常：jobId="+jobId,e);
        }
        finally {
            boolean result = releaseDistributedLock(jedis,requestId);
            if(!result){
                logger.error("【风险监控】定时邮件-释放锁异常：requestId="+requestId);
            }
        }
        return false;
    }

    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param requestId 请求标识
     * @return 是否获取成功
     */
    private static boolean tryGetDistributedLock(Jedis jedis, String requestId) {
        //过期时间为半分钟
        String result = jedis.set(MONITOR_EMAIL_JOB_ID_SET_LOCK, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 30);
        return LOCK_SUCCESS.equals(result);

    }

    /**
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    private static boolean releaseDistributedLock(Jedis jedis, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(MONITOR_EMAIL_JOB_ID_SET_LOCK), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(result);

    }
}
