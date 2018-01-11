package com.example.cc.moedialer.Call;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.cc.moedialer.R;

/**
 * Created by cc on 18-1-11.
 */

public class FooterItemView extends LinearLayout {
    private Context context;
    private AttributeSet attrs;

    public FooterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        LayoutInflater.from(context).inflate(R.layout.footer_item, this);

    }

}
