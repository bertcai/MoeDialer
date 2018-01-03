package com.example.cc.moedialer.Contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.cc.moedialer.R;

/**
 * Created by cc on 18-1-3.
 */

public class ContactItemView extends LinearLayout {
    Context context;
    AttributeSet attrs;

    public ContactItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        LayoutInflater.from(context).inflate(R.layout.contact_item, this);
    }

}
