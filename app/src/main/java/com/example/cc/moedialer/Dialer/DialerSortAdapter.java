package com.example.cc.moedialer.Dialer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.cc.moedialer.Contacts.ContactItemModel;
import com.example.cc.moedialer.Contacts.SortAdapter;
import com.example.cc.moedialer.R;

import java.util.List;

/**
 * Created by cc on 18-1-15.
 */

public class DialerSortAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactItemModel> list = null;
    private Context mContext;

    public DialerSortAdapter(Context mContext, List<ContactItemModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<ContactItemModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        DialerSortAdapter.ViewHolder viewHolder = null;
        final ContactItemModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.contact_sort_item, null);
            viewHolder.tvName = (TextView) view.findViewById(R.id.dialer_contact_name);
            view.setTag(viewHolder);
            viewHolder.tvNumber = (TextView) view.findViewById(R.id.dialer_contact_number);
        } else {
            viewHolder = (DialerSortAdapter.ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        viewHolder.tvName.setText(this.list.get(position).getName());
        viewHolder.tvNumber.setText(this.list.get(position).getNumber());

        return view;

    }


    final static class ViewHolder {
        TextView tvName;
        TextView tvNumber;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
