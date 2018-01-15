package com.example.cc.moedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cc.moedialer.Contacts.ContactItemModel;
import com.example.cc.moedialer.Dialer.DialerActivity;
import com.example.cc.moedialer.Dialer.DialerSortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortListActivity extends AppCompatActivity {
    private ListView sortList;
    private EditTextWithDel sortEdit;
    private DialerSortAdapter adapter;
    private List<ContactItemModel> sourceModelList;
    private List<ContactItemModel> sourceDataList;

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
        setContentView(R.layout.activity_sort_list);
        Intent intent = getIntent();
        String tempData = intent.getStringExtra("sortStr");
        this.overridePendingTransition(R.anim.sort_open, R.anim.activity_close);
        sortList = (ListView) findViewById(R.id.sort_list);
        sortEdit = (EditTextWithDel) findViewById(R.id.sort_edit_search);
        sortEdit.setText(tempData);
        sortEdit.setSelection(sortEdit.length());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initEvents();
        setAdapter();
        filterData(sortEdit.getText().toString());
    }

    private void initEvents() {
        sortEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                sortEdit.setSelection(sortEdit.length());
                sortList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(),
//                        ((ContactItemModel) adapter.getItem(position)).getName(),
//                        Toast.LENGTH_SHORT).show();
                        String number = ((ContactItemModel) adapter.getItem(position)).getNumber();
                        if (ContextCompat.checkSelfPermission(SortListActivity.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SortListActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + number));
                            startActivity(intent);
                        }
                    }
                });
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
            mSortList.clear();

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
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.sort_back, R.anim.sort_close);
    }
}
