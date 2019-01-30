package com.geo.rcs.common.schedule;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.schedule
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月12日 下午5:07
 */
public enum MailPriority {

    /** 最高优先级 */
    SUPER(1, "Mail-super"),
    /** 高优先级 */
    HIGH (2, "Mail-high"),
    /** 中优先级 */
    MID  (3, "Mail-mid"),
    /** 最低优先级 */
    LOW  (4, "Mail-low");

    private int code;

    private String message;

    MailPriority(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getPriority(Integer priority){

        switch (priority){
            case 1:return SUPER.getMessage();
            case 2:return HIGH.getMessage();
            case 3:return MID.getMessage();
            case 4:return LOW.getMessage();
        }
        return "暂无此优先级";
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
