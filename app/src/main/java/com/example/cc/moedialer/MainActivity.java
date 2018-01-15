package com.example.cc.moedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import com.example.cc.moedialer.Call.CallDataFragment;
import com.example.cc.moedialer.Contacts.ContactFragment;
import com.example.cc.moedialer.Dialer.DialerActivity;
import com.example.cc.moedialer.Dialer.DialpadView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private DialpadView dialpadView;
    private List<Fragment> fragmentList;
    private AdapterFragment adapterFragment;
    private TabLayout tabLayout;
    private ArrayList<String> titleList;
    private ViewPager viewPager;
    private FloatingActionButton floatButton;
    private Animation fabAppearAnimation;
    private Animation fabGoneAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new
                    String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS}, 1);
        }
        setContentView(R.layout.activity_main);
        initView();


//        dialpadView = (DialpadView) findViewById(R.id.dialer_pad);

    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        fabAppearAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_visible);
        fabGoneAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_gone);

        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        CallDataFragment callDataFragment = new CallDataFragment();
        fragmentList.add(callDataFragment);
        ContactFragment contactFragment = new ContactFragment();
        fragmentList.add(contactFragment);
        titleList.add("通话");
        titleList.add("联系人");
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapterFragment = new AdapterFragment(fragmentManager, fragmentList, titleList);
        viewPager = (ViewPager) findViewById(R.id.view_page);
        viewPager.setAdapter(adapterFragment);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        floatButton = (FloatingActionButton) findViewById(R.id.float_button);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatButton.getVisibility() == View.VISIBLE) {
                    floatButton.startAnimation(fabGoneAnimation);
                    floatButton.setVisibility(View.GONE);
                }
                Intent intent = new Intent(MainActivity.this, DialerActivity.class);
                startActivity(intent);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                floatButton = (FloatingActionButton) findViewById(R.id.float_button);
                if (position == 0) {
                    floatButton.setImageResource(R.drawable.ic_dialpad_white_24dp);
                    floatButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (floatButton.getVisibility() == View.VISIBLE) {
                                floatButton.startAnimation(fabGoneAnimation);
                                floatButton.setVisibility(View.GONE);
                            }
                            Intent intent = new Intent(MainActivity.this, DialerActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                if (position == 1) {
                    floatButton.setImageResource(R.drawable.ic_person_add_white_24dp);
                    floatButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (floatButton.getVisibility() == View.VISIBLE) {
                                floatButton.startAnimation(fabGoneAnimation);
                                floatButton.setVisibility(View.GONE);
                            }
                            Intent intent = new Intent(Intent.ACTION_INSERT,
                                    Uri.parse("content://com.android.contacts/contacts"));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "You denied the permission:(",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (floatButton.getVisibility() == View.GONE) {
            floatButton.startAnimation(fabAppearAnimation);
            floatButton.setVisibility(View.VISIBLE);
        }
    }
}
