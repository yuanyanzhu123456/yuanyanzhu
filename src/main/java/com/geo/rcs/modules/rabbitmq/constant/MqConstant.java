package com.geo.rcs.modules.rabbitmq.constant;

import java.util.HashMap;

/**
 * @Author： qiaoShengLong
 * @email: qiaoshenglong@geotmt.com
 * @Description:
 * @Date： Created in 17:09 2018/6/18
 */
public abstract class MqConstant {

    public static final int FAIL_MAX_NUM = 3;
    public static final int HEART_TIME = 1000 * 20;
    public static final HashMap<String, Integer> MAIL_PRIORITY_MAP = new HashMap();
    public static final HashMap<String, Integer> API_QUEUE_PRIORITY_MAP = new HashMap();
    public static final HashMap<String, Integer> RULES_QUEUE_PRIORITY_MAP = new HashMap();
    public static final HashMap<String, Integer> ABEVENT_QUEUE_PRIORITY_MAP = new HashMap();

    static {
        MAIL_PRIORITY_MAP.put("Mail-super", 4);
        MAIL_PRIORITY_MAP.put("Mail-high", 3);
        MAIL_PRIORITY_MAP.put("Mail-mid", 2);
        MAIL_PRIORITY_MAP.put("Mail-low", 1);

        API_QUEUE_PRIORITY_MAP.put("Monitor-super", 4);
        API_QUEUE_PRIORITY_MAP.put("Monitor-high", 3);
        API_QUEUE_PRIORITY_MAP.put("Monitor-mid", 2);
        API_QUEUE_PRIORITY_MAP.put("Monitor-low", 1);

        RULES_QUEUE_PRIORITY_MAP.put("Rules-super", 4);

        ABEVENT_QUEUE_PRIORITY_MAP.put("AbEvent-super",4);
        ABEVENT_QUEUE_PRIORITY_MAP.put("AbEvent-high",3);
        ABEVENT_QUEUE_PRIORITY_MAP.put("AbEvent-mid",2);
        ABEVENT_QUEUE_PRIORITY_MAP.put("AbEvent-low",1);
    }

    public static HashMap getQueuePriorityMap(String type) {
        if (TaskType.MAIL_TASK.getMessage().equalsIgnoreCase(type)) {
            return MAIL_PRIORITY_MAP;
        } else if (TaskType.API_TASK.getMessage().equalsIgnoreCase(type)) {
            return API_QUEUE_PRIORITY_MAP;

        }else if (TaskType.RULES_TASK.getMessage().equalsIgnoreCase(type)){
            return RULES_QUEUE_PRIORITY_MAP;
        }else if (TaskType.AB_EVENT_TASK.getMessage().equalsIgnoreCase(type)){
            return ABEVENT_QUEUE_PRIORITY_MAP;
        }
        return null;
    }

    public enum WorkStatus {
        /**
         * worker内任务的状态:等待
         */
        WAIT(0, "WAIT"),
        /**
         * worker内任务的状态:繁忙
         */
        BUSY(1, "BUSY"),
        /**
         * worker状态:注销
         */
        WORKER_DEAD(0, "DEAD"),
        /**
         * worker状态:正常
         */
        WORK_SWITCH_OPEN(1, "ALIVE"),
        /**
         * worker状态:暂停
         */
        WORK_SWITCH_STOP(2, "STOP"),
        /**
         * 获取worker状态信息根据code获取相应注释
         */
        WORK_MESSAGE(120, "worker状态信息");

        WorkStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getMessage(int code) {
            switch (code) {
                case 0:
                    return "DEAD";
                case 1:
                    return "ALIVE";
                case 2:
                    return "STOP";
                default:

            }
            return "woker状态非法!";
        }
    }

    public enum TaskType {
        /**
         * 邮件类型任务
         */
        MAIL_TASK(0, "mail"),
        /**
         * 贷中监控任务
         */
        API_TASK(1, "api"),
        /**
         * api异步进件任务
         */
        RULES_TASK(2, "rules"),
        /**
         * ABTEST 任务
         */
        AB_EVENT_TASK(3, "abEvent");
        private int code;
        private String message;

        TaskType(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }

}
