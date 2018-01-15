package com.example.cc.moedialer.Call;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cc.moedialer.R;

public class CallDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView onlyNumber;
    private TextView name;
    private TextView number;
    private ImageButton callButton;
    private ImageView callMade;
    private ImageView callReceived;
    private TextView callType;
    private TextView callDate;
    private TextView callDuration;
    private View copy;
    private View modifyCall;
    private View sendMessage;
    private View delete;
    private CallItemModel itemContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_detail);
        Intent intent = getIntent();
        CallItemModel mContent = (CallItemModel) intent.getSerializableExtra("item");
        itemContent = mContent;
        init(mContent);
    }

    private void init(CallItemModel mContent) {
        onlyNumber = (TextView) findViewById(R.id.only_number);
        name = (TextView) findViewById(R.id.call_name);
        number = (TextView) findViewById(R.id.call_number);
        callButton = (ImageButton) findViewById(R.id.call_log_call);
        callMade = (ImageView) findViewById(R.id.call_made);
        callReceived = (ImageView) findViewById(R.id.call_received);
        callType = (TextView) findViewById(R.id.call_type);
        callDate = (TextView) findViewById(R.id.call_date);
        callDuration = (TextView) findViewById(R.id.call_duration);
        copy = findViewById(R.id.copy_number);
        modifyCall = findViewById(R.id.modify_call);
        sendMessage = findViewById(R.id.send_message);
        delete = findViewById(R.id.delete_call_log); /*title set*/
        if (mContent.getName() == null) {
            onlyNumber.setText(mContent.getNumber());
            onlyNumber.setVisibility(View.VISIBLE);
            name.setVisibility(View.GONE);
            number.setVisibility(View.GONE);
        } else {
            onlyNumber.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            number.setVisibility(View.VISIBLE);
            name.setText(mContent.getName());
            number.setText(mContent.getNumber());
        } /*call button set*/
        callButton.setOnClickListener(this); /*call type set*/
        if (mContent.getType().equals("呼入")) {
            callMade.setVisibility(View.GONE);
            callReceived.setVisibility(View.VISIBLE);
            callType.setText("呼入");
            callDate.setText(mContent.getTrueDate());
            callDuration.setText(mContent.getCallTime());
        } else if (mContent.getType().equals("呼出")) {
            callMade.setVisibility(View.VISIBLE);
            callReceived.setVisibility(View.GONE);
            callDate.setText(mContent.getTrueDate());
            callDuration.setText(mContent.getCallTime());
            if (mContent.getCallTime().equals("")) callType.setText("未接通");
            else callType.setText("呼出");
        } else {
            callMade.setVisibility(View.GONE);
            callReceived.setVisibility(View.VISIBLE);
            callType.setText("未接通");
            callDate.setText(mContent.getTrueDate());
            callDuration.setText(mContent.getCallTime());
        }
        copy.setOnClickListener(this);
        modifyCall.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call_log_call:
                call(itemContent.getNumber());
                break;
            case R.id.copy_number:
                copy(itemContent.getNumber());
                break;
            case R.id.modify_call:
                modifyCallFunc(itemContent.getNumber());
                break;
            case R.id.send_message:
                sendMessage(itemContent.getNumber());
                break;
            case R.id.delete_call_log:
                delete(itemContent.getCall_id());
                break;
        }
    }

    private void call(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
        else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }

    private void copy(String number) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("label", number);
        cm.setPrimaryClip(mClipData);
        Toast.makeText(this, "文本已复制", Toast.LENGTH_LONG).show();
    } /*now it jmp to system dialer*/

    private void modifyCallFunc(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void sendMessage(String number) {
        Uri smsToUri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        startActivity(intent);
    }

    private void delete(long id) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALL_LOG}, 1);
        else {
            ContentResolver resolver = getContentResolver();
            resolver.delete(CallLog.Calls.CONTENT_URI, "_id=?", new String[]{id + ""});
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else Toast.makeText(this, "You denied the permission", Toast.LENGTH_LONG).show();
        }
    }
}
