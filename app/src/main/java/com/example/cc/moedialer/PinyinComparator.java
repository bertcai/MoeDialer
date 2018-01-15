package com.example.cc.moedialer;


import com.example.cc.moedialer.Contacts.ContactItemModel;

import java.util.Comparator;

/**
 * Created by cc on 18-1-3.
 */

public class PinyinComparator implements Comparator<ContactItemModel> {

    public int compare(ContactItemModel o1, ContactItemModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}