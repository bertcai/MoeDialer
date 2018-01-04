package com.example.cc.moedialer.Contacts;

/**
 * Created by cc on 18-1-3.
 */

public class ContactItemModel {
    private String name; //the contact 's name
    private String sortLetters; //the contact's name 's first PinYin
    private long id;
    private String number;

    public String getName() {
        return name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetters = sortLetter;
    }
}
