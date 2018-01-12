package com.example.cc.moedialer.Contacts;

import android.Manifest;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cc.moedialer.EditTextWithDel;
import com.example.cc.moedialer.MyApplication;
import com.example.cc.moedialer.PinyinComparator;
import com.example.cc.moedialer.PinyinUtils;
import com.example.cc.moedialer.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by cc on 18-1-3.
 */

public class ContactFragment extends Fragment {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private EditTextWithDel mEtSearchName;
    private List<ContactItemModel> sourceDataList;
    private View view;
//    private FloatingActionButton addContact;
    private Animation fabVisible;
    private Animation fabGone;
    List<ContactItemModel> sourceModelList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_list_fragment, container, false);
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
            init();
        }
        return view;
    }

    private void init() {
        sortListView = (ListView) view.findViewById(R.id.contact_lv);
        sideBar = (SideBar) view.findViewById(R.id.sidebar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        mEtSearchName = (EditTextWithDel) view.findViewById(R.id.edit_search);
//        addContact = (FloatingActionButton) view.findViewById(R.id.add_contact);
        fabVisible = AnimationUtils.loadAnimation(this.getContext(), R.anim.fab_visible);
        fabGone = AnimationUtils.loadAnimation(this.getContext(), R.anim.fab_gone);
        initData();
        initEvents();
        setAdapter();
        filterData(mEtSearchName.getText().toString());
    }

    private void setAdapter() {
        sourceDataList = filledData(sourceModelList);
        Collections.sort(sourceDataList, new PinyinComparator());
        adapter = new SortAdapter(this.getContext(), sourceDataList);
        sortListView.setAdapter(adapter);
    }

    //handle events
    private void initEvents() {

        //sideBar events
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        //click the item
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(parent.getContext(),
//                        ((ContactItemModel) adapter.getItem(position)).getName(),
//                        Toast.LENGTH_SHORT).show();
                long contactId = ((ContactItemModel) adapter.getItem(position)).getId();
                Uri peopleUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                        contactId);

                Intent intent = new Intent();
                intent.setAction(ContactsContract.QuickContact.ACTION_QUICK_CONTACT);
                intent.setData(peopleUri);
//                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                intent.setAction(Intent.ACTION_VIEW);

                startActivity(intent);
            }
        });


        //searchEdit event
        mEtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //shield enter
        mEtSearchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        //touch listener
//        mEtSearchName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (addContact.getVisibility() == View.VISIBLE) {
//                    addContact.startAnimation(fabGone);
//                    addContact.setVisibility(View.GONE);
//                } else {
//                    addContact.startAnimation(fabVisible);
//                    addContact.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        addContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (addContact.getVisibility() == View.VISIBLE) {
//                    addContact.startAnimation(fabGone);
//                    addContact.setVisibility(View.GONE);
//                }
//                Intent intent = new Intent(Intent.ACTION_INSERT,
//                        Uri.parse("content://com.android.contacts/contacts"));
//                startActivity(intent);
//            }
//        });
    }

    private void initData() {
        sideBar.setTextView(dialog);
    }

    private void filterData(String filterStr) {
        List<ContactItemModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = sourceDataList;
        } else {
            mSortList.clear();
            for (ContactItemModel item : sourceDataList) {
                String name = item.getName();
                String number = item.getNumber();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase())
                        != -1 || PinyinUtils.getAlpha(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())
                        || number.contains(filterStr.toString())) {
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
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    //read contacts
    private void readContacts() {
        sourceModelList = new ArrayList<>();
        int i = 0;
        Cursor cursor = null;
        try {
            cursor = this.getContext()
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
                    item.setName(displayName);
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
                    Toast.makeText(this.getContext(), "You denied the permission",
                            Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        readContacts();
        init();
//        if(addContact.getVisibility()==View.GONE){
//            addContact.startAnimation(fabVisible);
//            addContact.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(addContact.getVisibility()==View.VISIBLE){
//            addContact.startAnimation(fabGone);
//            addContact.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
