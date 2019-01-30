package com.geo.rcs.modules.rabbitmq.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Message  implements Serializable{
    private static final long serialVersionUID = 123456557890L;

    private Long id;

	private String taskClassName;

	private String taskMethodName;

	private ConcurrentHashMap<String, Object> taskMethodParmMap;

	private int failCount;

	private int status;

	public Message(Long id, String taskClassName, String taskMethodName, ConcurrentHashMap<String, Object> taskMethodParmMap) {
		super();
		this.id = id;
		this.taskClassName = taskClassName;
		this.taskMethodName = taskMethodName;
		this.taskMethodParmMap = taskMethodParmMap;
	}
	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskClassName() {
		return taskClassName;
	}
	public void setTaskClassName(String taskClassName) {
		this.taskClassName = taskClassName;
	}

	public ConcurrentHashMap<String, Object> getTaskMethodParmMap() {
		return taskMethodParmMap;
	}
	public void setTaskMethodParmMap(ConcurrentHashMap<String, Object> taskMethodParmMap) {
		this.taskMethodParmMap = taskMethodParmMap;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTaskMethodName() {
		return taskMethodName;
	}
	public void setTaskMethodName(String taskMethodName) {
		this.taskMethodName = taskMethodName;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", taskClassName=" + taskClassName + ", taskMethodName=" + taskMethodName
				+ ", taskMethodParmMap=" + taskMethodParmMap + ", failCount=" + failCount + ", status=" + status + "]";
	}


}
