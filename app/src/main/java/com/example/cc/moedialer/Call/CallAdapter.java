package com.example.cc.moedialer.Call;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cc.moedialer.R;

import java.util.List;

/**
 * Created by cc on 18-1-9.
 */

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {
    private List<CallItemModel> list = null;
    private final static int ITEM_TYPE_FOOTER = 1;
    private Context mContext;
    private Fragment parent;


    public CallAdapter(Fragment parent, Context mContext, List<CallItemModel> list) {
//        CallItemModel last = new CallItemModel(null, null, null,
//                null, null, -1, null);
        this.list = list;
//        list.add(last);
        this.parent = parent;
    }

    public int getCount() {
        return list.size();
    }

    private void onClickItem(String number) {
        if (ContextCompat.checkSelfPermission(parent.getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            parent.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    1);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            parent.getContext().startActivity(intent);
        }
    }

    private void onClickButton(CallItemModel mContent) {
        Intent intent = new Intent(parent.getContext(), CallDetailActivity.class);
        intent.putExtra("item", mContent);
        parent.startActivity(intent);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            FooterHolder holder = new FooterHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.call_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.callItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    CallItemModel mContent = list.get(position);
                    onClickItem(mContent.getNumber());
                }
            });
            holder.callDataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(parent.getContext(), "You click the button", Toast.LENGTH_LONG)
//                        .show();
                    int position = holder.getAdapterPosition();
                    CallItemModel mContent = list.get(position);
                    onClickButton(mContent);
                }
            });
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof FooterHolder) {

        } else {
            final CallItemModel mContent = list.get(position);
            String name = mContent.getName();
            String number = mContent.getNumber();
            String type = mContent.getType();
            String duration = mContent.getCallTime();
            String date = mContent.getDate();
            holder.tvIsCallMade.setText("");
            if (name != null) {
                holder.tvTitle.setText(name);
            } else {
                holder.tvTitle.setText(number);
            }

            if (type.equals("呼出")) {
                holder.tvIsCallMade.setText(" ↗");
            }

            if (type.equals("未接通")) {
                holder.tvContent.setText(date + " 未接通");
            } else if (duration.equals("")) {
                holder.tvContent.setText(date + " 未接通");
            } else {
                holder.tvContent.setText(date + " " + type + duration);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE_FOOTER;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvContent;
        TextView tvIsCallMade;
        ImageButton callDataButton;
        View callItemView;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.call_title);
            tvContent = (TextView) view.findViewById(R.id.call_content);
            tvIsCallMade = (TextView) view.findViewById(R.id.is_call_made);
            callDataButton = (ImageButton) view.findViewById(R.id.call_data_btn);
            callItemView = view;
        }
    }

    class FooterHolder extends ViewHolder {

        public FooterHolder(View view) {
            super(view);
        }
    }
}
