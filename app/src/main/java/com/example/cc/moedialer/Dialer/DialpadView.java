package com.example.cc.moedialer.Dialer;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.cc.moedialer.*;

import java.lang.reflect.Method;

/**
 * Created by cc on 18-1-2.
 */

public class DialpadView extends LinearLayout implements View.OnClickListener {
    private EditText inputPhoneNum;
    Context context;
    AttributeSet attrs;

    private ToneGenerator mToneGenerator;
    private Object mToneGeneratorLock = new Object();
    private Boolean mDTMFToneEnabled;
    private static final int TONE_LENGTH_MS = 150;
    private AudioManager mAudioManager;
    private boolean isEmpty = true;
    private int selectStartSave;
    private int oldLength;
    private int newLength;

    public DialpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        LayoutInflater.from(context).inflate(R.layout.dialpad, this);
        FloatingActionButton call = (FloatingActionButton) findViewById(R.id.fab_call);

        inputPhoneNum = (DialerEditText) findViewById(R.id.input_phone_number);

        inputPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                selectStartSave = inputPhoneNum.getSelectionStart();
                oldLength = inputPhoneNum.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEmpty()) {
                    isEmpty = true;
                    inputPhoneNum.setCursorVisible(false);
                } else {
                    isEmpty = false;
                    newLength = inputPhoneNum

                }
            }
        });

        inputPhoneNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty) {
                    inputPhoneNum.setCursorVisible(true);
                }
            }
        });


        ImageButton delPhoneNum = (ImageButton) findViewById(R.id.delete_phone_number);

        Button[] buttons = new Button[12];
        buttons[0] = (Button) findViewById(R.id.zero);
        buttons[1] = (Button) findViewById(R.id.one);
        buttons[2] = (Button) findViewById(R.id.two);
        buttons[3] = (Button) findViewById(R.id.three);
        buttons[4] = (Button) findViewById(R.id.four);
        buttons[5] = (Button) findViewById(R.id.five);
        buttons[6] = (Button) findViewById(R.id.six);
        buttons[7] = (Button) findViewById(R.id.seven);
        buttons[8] = (Button) findViewById(R.id.eight);
        buttons[9] = (Button) findViewById(R.id.nine);
        buttons[10] = (Button) findViewById(R.id.star_key); //* key
        buttons[11] = (Button) findViewById(R.id.pound_key); //# key

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(this); //bind click
        }

        delPhoneNum.setOnClickListener(this);
        delPhoneNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inputPhoneNum.setText("");
                return true;
            }
        });
        call.setOnClickListener(this);

        mAudioManager = (AudioManager) context.getSystemService(
                Context.AUDIO_SERVICE);
        mDTMFToneEnabled = Settings.System.getInt(
                context.getContentResolver(),
                Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                try {
                    mToneGenerator = new ToneGenerator(
                            AudioManager.STREAM_MUSIC, 80);
                } catch (Exception e) {
                    e.printStackTrace();
                    mToneGenerator = null;
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        String temp;
        switch (v.getId()) {
            case R.id.one:
                inputAdd('1');
                playTone(1);
                break;
            case R.id.two:
                inputAdd('2');
                playTone(2);
                break;
            case R.id.three:
                inputAdd('3');
                playTone(3);
                break;
            case R.id.four:
                inputAdd('4');
                playTone(4);
                break;
            case R.id.five:
                inputAdd('5');
                playTone(5);
                break;
            case R.id.six:
                inputAdd('6');
                playTone(6);
                break;
            case R.id.seven:
                inputAdd('7');
                playTone(7);
                break;
            case R.id.eight:
                inputAdd('8');
                playTone(8);
                break;
            case R.id.nine:
                inputAdd('9');
                playTone(9);
                break;
            case R.id.zero:
                inputAdd('0');
                playTone(0);
                break;
            case R.id.star_key:
                inputAdd('*');
                playTone(ToneGenerator.TONE_DTMF_S);
                break;
            case R.id.pound_key:
                inputAdd('#');
                playTone(ToneGenerator.TONE_DTMF_P);
                break;
            case R.id.delete_phone_number:
                temp = inputPhoneNum.getText().toString();
                if (!temp.isEmpty()) {
                    deleteNum();
                }
                break;
            case R.id.fab_call:
                temp = inputPhoneNum.getText().toString();
                call(temp);
                break;

        }
    }

    private void playTone(int tone) {
        if (!mDTMFToneEnabled) {
            return;
        }

        int ringerMode = mAudioManager.getRingerMode();
        if ((ringerMode == mAudioManager.RINGER_MODE_SILENT
                || ringerMode == mAudioManager.RINGER_MODE_VIBRATE)) {
            return;
        }

        synchronized (mToneGeneratorLock) {
            if (mToneGenerator == null) {
                return;
            }
            mToneGenerator.startTone(tone, TONE_LENGTH_MS);
        }
    }

    private void inputAdd(char c) {
        String temp = inputPhoneNum.getText().toString();
        if (temp.length() == 3) {
            temp = temp + " " + c;
        } else if ((temp.length() - 3) % 5 == 0) {
            temp = temp + " " + c;
        } else
            temp = temp + c;
        inputPhoneNum.setText(temp);
    }

    private void deleteNum() {
        String temp = inputPhoneNum.getText().toString();
        int select = inputPhoneNum.length();
        int selectEnd;
        select = inputPhoneNum.getSelectionStart();
        selectEnd = inputPhoneNum.getSelectionEnd();
        String str1;
        String str2;
        if (select == selectEnd) {
            if (select == 5) {
                str1 = temp.substring(0, select - 2);
                str2 = temp.substring(select, temp.length());
                inputPhoneNum.setText(str1 + str2);
            } else if ((select - 4) % 6 == 0) {
                str1 = temp.substring(0, select - 2);
                str2 = temp.substring(select, temp.length());
                inputPhoneNum.setText(str1 + str2);
            } else {
                str1 = temp.substring(0, select - 1);
                str2 = temp.substring(select, temp.length());
                inputPhoneNum.setText(str1 + str2);
            }
        } else {
            str1 = temp.substring(0, select);
            str2 = temp.substring(selectEnd, temp.length());
            inputPhoneNum.setText(str1 + str2);
        }
    }

    private void call(String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty() {
        return inputPhoneNum.length() == 0;
    }

    public String getText() {
        return inputPhoneNum.getText().toString();
    }
}
