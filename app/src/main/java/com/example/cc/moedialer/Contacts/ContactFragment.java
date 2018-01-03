package com.example.cc.moedialer.Contacts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cc.moedialer.EditTextWithDel;
import com.example.cc.moedialer.PinyinComparator;
import com.example.cc.moedialer.PinyinUtils;
import com.example.cc.moedialer.R;

import java.util.ArrayList;
import java.util.Collection;
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
    private List<ContactItemModel> sourceDateList;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contact_list_fragment,container,false);
        init();
        return view;
    }

    private void init(){
        sortListView = (ListView) view.findViewById(R.id.contact_lv);
        sideBar = (SideBar) view.findViewById(R.id.sidebar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        mEtSearchName = (EditTextWithDel) view.findViewById(R.id.edit_search);
        initData();
        initEvents();
        setAdapter();
    }

    private void setAdapter(){
        sourceDateList = filledData(getResources().getStringArray(R.array.data));
        Collections.sort(sourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this.getContext(), sourceDateList);
        sortListView.setAdapter(adapter);
    }

    private void initEvents(){
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }
            }
        });

        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        ((ContactItemModel) adapter.getItem(position)).getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

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
    }

    private void initData(){
        sideBar.setTextView(dialog);
    }

    private void filterData(String filterStr){
        List<ContactItemModel> mSortList = new ArrayList<>();
        if(TextUtils.isEmpty(filterStr)){
            mSortList = sourceDateList;
        } else {
            mSortList.clear();
            for(ContactItemModel item: sourceDateList){
                String name = item.getName();
                if(name.toUpperCase().indexOf(filterStr.toString().toUpperCase())
                        != -1|| PinyinUtils.getAlpha(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())){
                    mSortList.add(item);
                }
            }
        }
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    private List<ContactItemModel> filledData(String[] date) {
        List<ContactItemModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            ContactItemModel sortModel = new ContactItemModel();
            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getAlpha(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetter(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }else {
                sortModel.setSortLetter("#");
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }
}
