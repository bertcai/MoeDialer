package com.example.cc.moedialer;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.support.v7.widget.AppCompatEditText;

/**
 * Created by cc on 18-1-3.
 */

public class EditTextWithDel extends AppCompatEditText {
    private final static String TAG = "EditTextWithDel";
    private Drawable imgAble;
    private Context mContext;
    private AttributeSet mAttrs;

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    private void init() {
        imgAble = mContext.getDrawable(R.drawable.ic_clear_black_24dp);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }


    //set clear icon
    private void setDrawable() {
        if (length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
