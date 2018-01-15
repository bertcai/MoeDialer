package com.example.cc.moedialer.Call;

import java.io.Serializable;

/**
 * Created by cc on 18-1-9.
 */
public class CallItemModel implements Serializable {
    private String name;
    private String type;
    private String date;
    private String trueDate;
    private String number;
    private String callTime;
    private long call_id;

    public CallItemModel(String name, String type, String date, String number, String callTime,
                         long id, String trueDate) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.number = number;
        this.callTime = callTime;
        this.call_id = id;
        this.trueDate = trueDate;
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

    public String getTrueDate() {
        return trueDate;
    }
}
