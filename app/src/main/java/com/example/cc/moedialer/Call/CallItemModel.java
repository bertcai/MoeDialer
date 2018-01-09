package com.example.cc.moedialer.Call;

/**
 * Created by cc on 18-1-9.
 */

public class CallItemModel {
    private String name;
    private String type;
    private String date;
    private String number;
    private String callTime;
    private long call_id;

    public CallItemModel(String name, String type, String date,
                         String number, String callTime, long id) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.number = number;
        this.callTime = callTime;
        this.call_id = id;
    }

    public String getNumber() {
        return number;
    }

    public String getCallTime() {
        return callTime;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getCall_id() {
        return call_id;
    }
}
