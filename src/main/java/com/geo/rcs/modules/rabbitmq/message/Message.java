package com.geo.rcs.modules.rabbitmq.message;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
    private static final long serialVersionUID = 123456557890L;

    private Long id;
    private HashMap<String, Object> taskMethodParmMap;
    private int failCount;
    private int status;

    public Message(Long id, HashMap<String, Object> taskMethodParmMap) {
        super();
        this.id = id;
        this.taskMethodParmMap = taskMethodParmMap;
    }

    public Message() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", taskMethodParmMap=" + taskMethodParmMap +
                ", failCount=" + failCount +
                ", status=" + status +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HashMap<String, Object> getTaskMethodParmMap() {
        return taskMethodParmMap;
    }

    public void setTaskMethodParmMap(HashMap<String, Object> taskMethodParmMap) {
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
}
