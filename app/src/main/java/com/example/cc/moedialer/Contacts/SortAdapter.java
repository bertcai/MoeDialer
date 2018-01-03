package com.example.cc.moedialer.Contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.cc.moedialer.*;

import java.util.List;

/**
 * Created by cc on 18-1-3.
 */

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<ContactItemModel> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<ContactItemModel> list) {
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
        ViewHolder viewHolder = null;
        final ContactItemModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.contact_name);
            view.setTag(viewHolder);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.contact_catagory);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(this.list.get(position).getName());

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
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
