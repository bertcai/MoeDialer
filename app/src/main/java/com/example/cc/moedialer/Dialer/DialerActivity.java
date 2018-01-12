package com.example.cc.moedialer.Dialer;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.cc.moedialer.R;

public class DialerActivity extends AppCompatActivity {

    private DialpadView dialpadView;
    private Animation animation;
    private Animation outAnimation;
    private LayoutAnimationController lac;
    private boolean isEnd = false;
    private Animation dialBtnAppearAnimation;
    private FloatingActionButton call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        ActionBar actionBar = getSupportActionBar();

        dialpadView = (DialpadView) findViewById(R.id.dialer);
        call = (FloatingActionButton) dialpadView.findViewById(R.id.fab_call);

        animation = AnimationUtils.loadAnimation(this, R.anim.dialer_open);
        dialBtnAppearAnimation = AnimationUtils.loadAnimation(this,
                R.anim.fab_visible);

        //make the call button appear after the dialer view appeared
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (call.getVisibility() == View.INVISIBLE) {
                    call.startAnimation(dialBtnAppearAnimation);
                    call.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.dialer_close);
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnd = true;
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
    }

    @Override
    public void finish() {
        super.finish();

        this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
    }

}
