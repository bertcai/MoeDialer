package com.example.cc.moedialer.Call;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.cc.moedialer.R;

/**
 * Created by cc on 18-1-9.
 */

public class CallItemView extends LinearLayout {
    private Context context;
    private AttributeSet attrs;
    private ImageButton callDataBtn;

    public CallItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        LayoutInflater.from(context).inflate(R.layout.contact_item, this);
        callDataBtn = (ImageButton) findViewById(R.id.call_data_btn);
    }

    public ImageButton getCallDataBtn() {
        return callDataBtn;
    }
}
