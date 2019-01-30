package com.geo.rcs.common.constant;

import java.util.concurrent.TimeUnit;

/**
     * 业务是否成功的日志记录
     */
    public enum ConstantThreadPool {

    	TASK_QUEUE_SIZE(300),
    	CORE_POOL_SIZE(8),
    	MAXIMUM_POOL_SIZE(15),
    	KEEP_ALIVE_TIME(15),
    	TIME_UNIT(TimeUnit.MICROSECONDS),
    	MAX_TASK_NUM(0);
        Integer parm;
        TimeUnit util;
        ConstantThreadPool(int parm) {
            this.parm = parm;
        }
        ConstantThreadPool(TimeUnit util) {
        	this.util = util;
        }

        public Integer getParm() {
            return parm;
        }

        public void setParm(Integer parm) {
            this.parm = parm;
        }
        public TimeUnit getTimeUtil() {
        	return util;
        }
        
        public void setTimeUtil(TimeUnit util) {
        	this.util = util;
        }
        public Integer getMaxTask() {
            return MAXIMUM_POOL_SIZE.getParm()+TASK_QUEUE_SIZE.getParm();
        }
    }