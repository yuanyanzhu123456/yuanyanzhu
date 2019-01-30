package com.geo.rcs.common.schedule;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.common.schedule
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年07月12日 下午5:07
 */
public enum MonitorPriority {

    DAY(1, "DAY"),

    MONTH(2, "MONTH"),

    NETSTATUS(1, "NETSTATUS"),

    MULTIPLATE(2, "MULTIPLATE"),

    /** 最高优先级 */
    SUPER(1, "Monitor-super"),
    /** 高优先级 */
    HIGH (2, "Monitor-high"),
    /** 中优先级 */
    MID  (3, "Monitor-mid"),
    /** 最低优先级 */
    LOW  (4, "Monitor-low"),
    /** AbTestEvent */
    ABTEST  (5, "AbEvent-super");

    private int code;

    private String message;

    MonitorPriority(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getPriority(Integer priority){

        switch (priority){
            case 1:return SUPER.getMessage();
            case 2:return HIGH.getMessage();
            case 3:return MID.getMessage();
            case 4:return LOW.getMessage();
            case 5:return ABTEST.getMessage();
        }
        return null;
    }

    public static String getUnit(Integer unit){

        switch (unit){
            case 1:return DAY.getMessage();
            case 2:return MONTH.getMessage();
        }
        return null;
    }

    public static String getDimension(Integer dimension){

        switch (dimension){
            case 1:return NETSTATUS.getMessage();
            case 2:return MULTIPLATE.getMessage();
        }
        return null;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
