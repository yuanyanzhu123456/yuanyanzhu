package com.geo.rcs.common.constant;

/**
 * 常量
 *
 * @author jinlin
 * @email jinlin@geotmt.com
 * @date 2017/10/18 09:47
 */
public class Constant {
	/** 超级管理员Type */
	public static final long SUPER_ADMIN = 1;

	/**
     * 菜单类型
     *
	 * @author jinlin
	 * @email jinlin@geotmt.com
	 * @date 2017/10/18 09:49
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author jinlin
     * @email jinlin@geotmt.com
     * @date 2017/10/18 09:51
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1),
        /**
         * 结束
         */
        OVER(3),

        /**
         * 上次状态
         */
        EXECUTE(4);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    /**
     * 分发状态
     *
     * @author guoyujie
     * @email guoyujie@geotmt.com
     * @date 2017/10/18 09:51
     */
    public enum DistributeStatus {
        /**
         * 未分发
         */
        INIT(0),
        /**
         * 暂停
         */
        RUNNING(1),
        /**
         * 正常
         */
        OVER(2);

        private int value;

        DistributeStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * 日志类型
     */
    public enum LogType {

        LOGIN("登录日志"),
        LOGIN_FAIL("登录失败日志"),
        EXIT("退出日志"),
        EXCEPTION("异常日志"),
        BUSSINESS("业务日志");

        String message;

        LogType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * 业务是否成功的日志记录
     */
    public enum LogSucceed {

        SUCCESS("成功"),
        FAIL("失败");

        String message;

        LogSucceed(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
