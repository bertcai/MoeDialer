package com.example.cc.moedialer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import com.example.cc.moedialer.Call.CallDataFragment;
import com.example.cc.moedialer.Contacts.ContactFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new
                    String[]{Manifest.permission.CALL_PHONE}, 1);
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
}
