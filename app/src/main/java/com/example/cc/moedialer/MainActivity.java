package com.example.cc.moedialer;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cc.moedialer.Dialer.DialpadView;

public class MainActivity extends AppCompatActivity {


    private DialpadView dialpadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        dialpadView = (DialpadView) findViewById(R.id.dialer_pad);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dialpadView.callOnClick();
                } else {
                    Toast.makeText(this, "You denied the permission:(",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }
}
