package com.geo.rcs.modules.jobs.entity;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 17:46 2018/8/10
 */
public class JobScheduler extends JobRegister {

    private String type;
    private static final String SCHEDULER = "SCHEDULER";

    private String id;

    private long updateTime;

    private long registTime;

    private long createTime;

    private String updateTimeString;

    private String registTimeString;

    private String createTimeString;
    private String ip;

    @Override
    public String toString() {
        return "JobScheduler{" +
                "id='" + id + '\'' +
                ", updateTime=" + updateTime +
                ", registTime=" + registTime +
                ", createTime=" + createTime +
                ", updateTimeString='" + updateTimeString + '\'' +
                ", registTimeString='" + registTimeString + '\'' +
                ", createTimeString='" + createTimeString + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public void setWorkerName(String workerName) {

    }

    @Override
    public String setType() {
        return this.type;
    }

    /**
     * 初始化scheduler信息
     *
     * @param ip
     */
    public void initSchedulerInfo(String ip) {
        this.type = "SCHEDULER";
        if (this.getId() == null) {
            this.setId(UUID.randomUUID().toString());
            this.setIp(ip);
            this.setCreateTime(System.currentTimeMillis());
            this.updateCreateTimeString();
        }

    }


    /**
     * 生成注册Worker数据
     *
     * @param register
     * @return
     */
    @Override
    public String getRegisterInfo(JobRegister register) {

        if (register.getId() != null) {
            register.setUpdateTime(System.currentTimeMillis());
            register.setRegistTime(System.currentTimeMillis());
            register.updateRegistTimeString();
            register.updateUpdateTimeString();
            return JSONObject.toJSONString(register);
        } else {
            return null;
        }

    }

    /**
     * 更新注册Worker数据
     *
     * @param register
     * @return
     */
    @Override
    public String updateRegisterInfo(JobRegister register) {

        if (register.getId() != null) {
            register.setUpdateTime(System.currentTimeMillis());
            register.updateUpdateTimeString();
            return JSONObject.toJSONString(register);
        } else {
            return null;
        }

    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getTaskStatus() {
        return 0;
    }

    @Override
    public int getWorkStatus() {
        return 0;
    }

    @Override
    public String getWorkerName() {
        return null;
    }

    @Override
    public String getTaskRole() {
        return null;
    }

    @Override
    public void setWorkStatus(int status) {

    }

    @Override
    public List getQueneNameList() {
        return null;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 主函数测试方法
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        List list1 = new ArrayList();
        JobScheduler worker = new JobScheduler();
        System.out.println(JSONObject.toJSONString(worker));

        System.out.println("初始化之后");
        worker.initSchedulerInfo("127.0.0.1");
        System.out.println(JSONObject.toJSONString(worker));

        JobScheduler worker2 = new JobScheduler();
        System.out.println("注册之后");
        System.out.println(worker.getRegisterInfo(worker));
        System.out.println(worker.getRegisterInfo(worker2));

        for (int i = 0; i <= 10; i++) {
            Thread.sleep(3000);
            System.out.println(worker.updateRegisterInfo(worker));
        }

    }
}
