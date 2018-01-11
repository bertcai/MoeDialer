package com.example.cc.moedialer.Dialer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.cc.moedialer.R;

public class DialerActivity extends AppCompatActivity {

    private DialpadView dialpadView;
    Animation animation;
    Animation outAnimation;
    LayoutAnimationController lac;
    private boolean isEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        ActionBar actionBar = getSupportActionBar();
        animation = AnimationUtils.loadAnimation(this, R.anim.dialer_open);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.dialer_close);
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dialpadView = (DialpadView) findViewById(R.id.dialer);
        if (dialpadView.getVisibility() == View.GONE) {
            dialpadView.startAnimation(animation);
            dialpadView.setVisibility(View.VISIBLE);
        }
        lac = new LayoutAnimationController(animation);
        this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
        if (actionBar != null) {
            actionBar.hide();
        }
    }


    @Override
    public void onBackPressed() {
        if (dialpadView.getVisibility() == View.VISIBLE) {
            dialpadView.startAnimation(outAnimation);
            dialpadView.setVisibility(View.GONE);
        }
        if(isEnd){
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();

        this.overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
    }

}
