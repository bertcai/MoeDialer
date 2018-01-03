package com.example.cc.moedialer.Contacts;

/**
 * Created by cc on 18-1-3.
 */

public class ContactItemModel {
    private String name; //the contact 's name
    private String sortLetter; //the contact's name 's first PinYin

    public String getName() {
        return name;
    }

    public String getSortLetters() {
        return sortLetter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }
}
