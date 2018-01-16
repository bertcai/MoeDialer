package com.example.cc.moedialer.Dialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cc.moedialer.Contacts.ContactFragment;
import com.example.cc.moedialer.Contacts.ContactItemModel;
import com.example.cc.moedialer.PinyinComparator;
import com.example.cc.moedialer.PinyinUtils;
import com.example.cc.moedialer.R;
import com.example.cc.moedialer.SortListActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialerActivity extends AppCompatActivity {

    private DialpadView dialpadView;
    private Animation animation;
    private Animation outAnimation;
    private LayoutAnimationController lac;
    private boolean isEnd = false;
    private Animation dialBtnAppearAnimation;
    private FloatingActionButton call;
    private ListView sortList;
    private List<ContactItemModel> sourceDataList;
    private EditText inputPhoneNum;
    private DialerSortAdapter adapter;
    private List<ContactItemModel> sourceModelList;
    private LinearLayout topBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
            init();
        }
    }

    private void init() {
        setContentView(R.layout.activity_dialer);
        ActionBar actionBar = getSupportActionBar();

        dialpadView = (DialpadView) findViewById(R.id.dialer);
        call = (FloatingActionButton) dialpadView.findViewById(R.id.fab_call);


        animation = AnimationUtils.loadAnimation(this, R.anim.dialer_open);
        dialBtnAppearAnimation = AnimationUtils.loadAnimation(this,
                R.anim.fab_visible);
        inputPhoneNum = (EditText) findViewById(R.id.input_phone_number);
        sortList = (ListView) findViewById(R.id.dialer_sort_list);
        topBg = (LinearLayout) findViewById(R.id.top_bg);


        initEvents();
        setAdapter();
        filterData(inputPhoneNum.getText().toString());


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


    private void initEvents() {
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

        sortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(),
//                        ((ContactItemModel) adapter.getItem(position)).getName(),
//                        Toast.LENGTH_SHORT).show();
                String number = ((ContactItemModel) adapter.getItem(position)).getNumber();
                if (ContextCompat.checkSelfPermission(DialerActivity.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DialerActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            }
        });

//        sortList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY,
//                                       int oldScrollX, int oldScrollY) {
//                if (sortList.getVisibility() == View.VISIBLE) {
//                    Intent intent = new Intent(DialerActivity.this,
//                            SortListActivity.class);
//                    intent.putExtra("sortStr", inputPhoneNum.getText().toString());
//                    DialerActivity.this.startActivity(intent);
//                }
//            }
//        });

//        sortList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
//                    if (sortList.getVisibility() == View.VISIBLE) {
//                        if (dialpadView.getVisibility() == View.VISIBLE) {
//                            dialpadView.startAnimation(outAnimation);
//                            dialpadView.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
//        sortList.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (sortList.getVisibility() == View.VISIBLE) {
//                    if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                        Intent intent = new Intent(DialerActivity.this,
//                                SortListActivity.class);
//                        intent.putExtra("sortStr", inputPhoneNum.getText().toString());
//                        DialerActivity.this.startActivity(intent);
//                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        inputPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
                sortList.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputPhoneNum.length() == 0) {
                    sortList.setVisibility(View.GONE);
                    topBg.setClickable(true);
                } else {
                    topBg.setClickable(false);
                }
            }
        });

        topBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortList.getVisibility() == View.GONE) {
                    if (dialpadView.getVisibility() == View.VISIBLE) {
                        dialpadView.startAnimation(outAnimation);
                        dialpadView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void setAdapter() {
        sourceDataList = filledData(sourceModelList);
        Collections.sort(sourceDataList, new PinyinComparator());
        adapter = new DialerSortAdapter(this, sourceDataList);
        sortList.setAdapter(adapter);
    }

    private void filterData(String filterStr) {
        List<ContactItemModel> mSortList = new ArrayList<>();
        filterStr = filterStr.replaceAll("[\\s]", "");
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = sourceDataList;
        } else {
            mSortList.clear();
            for (ContactItemModel item : sourceDataList) {
                String name = item.getName();
                String number = item.getNumber();
                number = number.replaceAll("[^0-9]", "");
                if (name.toUpperCase().indexOf(filterStr.toUpperCase())
                        != -1 || PinyinUtils.getAlpha(name).toUpperCase()
                        .startsWith(filterStr.toUpperCase())
                        || number.contains(filterStr)) {
                    mSortList.add(item);
                }
            }
        }
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    private List<ContactItemModel> filledData(List<ContactItemModel> sourceList) {
        List<ContactItemModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < sourceList.size(); i++) {
//            ContactItemModel sortModel = new ContactItemModel();
//            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getAlpha(sourceList.get(i).getName());

            //check input
            if (pinyin.length() < 1) {
                continue;
            }

            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sourceList.get(i).setSortLetter(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sourceList.get(i).setSortLetter("#");
            }
            mSortList.add(sourceList.get(i));
        }
        Collections.sort(indexString);
        return mSortList;
    }

    //read contacts
    private void readContacts() {
        sourceModelList = new ArrayList<>();
        int i = 0;
        Cursor cursor = null;
        try {
            cursor = this
                    .getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String displayNumber = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    long contactsId = cursor.getInt(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    ContactItemModel item = new ContactItemModel();
                    if (displayName == null) {
                        item.setName("");
                    } else {
                        item.setName(displayName);
                    }
                    item.setId(contactsId);
                    item.setNumber(displayNumber);
                    sourceModelList.add(item);
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                    init();
                } else {
                    Toast.makeText(this, "You denied the permission",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (sortList.getVisibility() == View.VISIBLE) {
//                    Intent intent = new Intent(DialerActivity.this,
//                            SortListActivity.class);
//                    intent.putExtra("sortStr", inputPhoneNum.getText().toString());
//                    DialerActivity.this.startActivity(intent);
//                }
//                return true;
//            default:
//                return super.onTouchEvent(event);
//        }
//    }
}
