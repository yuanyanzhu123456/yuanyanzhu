package com.geo.rcs.modules.jobs.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Redis 注册任务 Worker
 */

public class JobWorker  extends   JobRegister{

	private String workerName;

	private String type;

	private String id;

	@Override
	public String getType() {
		return type;
	}

	@Override
    public void setType(String type) {
		this.type = type;
	}

	/**
	 * 任务状态
	 * 0:等待
	 * 1:繁忙

	 */
	private int taskStatus;

	/**
	 * worker自身状态
	 * 0:注销
	 * 1:开启
	 * 2:暂停
	 */
	private int workStatus;

	private List queneNameList;

	private long updateTime;

	private long registTime;

	private long createTime;

	private String updateTimeString;

	private String registTimeString;

	private String createTimeString;

	private String taskRole;

	private String ip;

	private String mqId;

	public JobWorker (){
		this.type="WORKER";

	}
	@Override
	public String getWorkerName() {
		return workerName;
	}
	@Override
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}



	@Override
	public List getQueneNameList() {
		return queneNameList;
	}
	@Override
	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}
	@Override
	public int getWorkStatus() {
		return workStatus;
	}
	@Override
	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}



	public void setQueneNameList(List queneNameList) {
		this.queneNameList = queneNameList;
	}

	@Override
	public String getTaskRole() {
		return taskRole;
	}

	public void setTaskRole(String taskRole) {
		this.taskRole = taskRole;
	}



	public String getMqId() {
		return mqId;
	}

	public void setMqId(String mqId) {
		this.mqId = mqId;
	}


	@Override
	public String toString() {
		return "JobWorker{" +
				"workerName='" + workerName + '\'' +
				", type='" + type + '\'' +
				", id='" + id + '\'' +
				", taskStatus=" + taskStatus +
				", workStatus=" + workStatus +
				", queneNameList=" + queneNameList +
				", updateTime=" + updateTime +
				", registTime=" + registTime +
				", createTime=" + createTime +
				", updateTimeString='" + updateTimeString + '\'' +
				", registTimeString='" + registTimeString + '\'' +
				", createTimeString='" + createTimeString + '\'' +
				", taskRole='" + taskRole + '\'' +
				", ip='" + ip + '\'' +
				", mqId='" + mqId + '\'' +
				'}';
	}

	/**
	 * 初始化Worker信息
	 * 
	 * @param workerName
	 * @param ip
	 * @param mqid
	 * @param queneNameList
	 * @param taskRole
	 */
	public void initWorkerInfo(String workerName, String ip, String mqid, int workStatus, int taskStatus,
			List queneNameList, String taskRole) {

		if (this.getId() == null) {
			this.setId(UUID.randomUUID().toString());
			this.setWorkerName(workerName);
			this.setIp(ip);
			this.setMqId(mqid);
			this.setTaskStatus(taskStatus);
			this.setWorkStatus(workStatus);
			this.setTaskRole(taskRole);
			this.setQueneNameList(queneNameList);
			this.setCreateTime(System.currentTimeMillis());
			this.updateCreateTimeString();
		}

	}

	@Override
	public String setType() {

		return this.type;
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

			return JSON.toJSONString(register);
		} else {
			return null;
		}

	}



	/**
	 * 主函数测试方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		List list1 = new ArrayList();
		JobWorker worker = new JobWorker();
		System.out.println(JSONObject.toJSONString(worker));

		System.out.println("初始化之后");
		worker.initWorkerInfo("workername", "127.0.0.1", "amqp", 1, 1, list1, "MAIL");
		System.out.println(JSONObject.toJSONString(worker));

		JobWorker worker2 = new JobWorker();
		System.out.println("注册之后");
		System.out.println(worker.getRegisterInfo(worker));
		System.out.println(worker.getRegisterInfo(worker2));

		for (int i = 0; i <= 10; i++) {
			Thread.sleep(3000);
			String s = worker.updateRegisterInfo(worker);
			System.out.println(s);
		}

	}
}
