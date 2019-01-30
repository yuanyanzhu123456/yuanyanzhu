package com.geo.rcs.modules.engine.entity;

public class Message {

    public static final int HELLO   = 0;

    public static final int GOODBYE = 1;

    private String          message;

    private int             status;

    public static int getHELLO() {
        return HELLO;
    }

    public static int getGOODBYE() {
        return GOODBYE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
