package com.example.cc.moedialer.Call;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.app.Fragment;
import android.provider.CallLog;
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
    private Context mContext;
    private Fragment parent;


    public CallAdapter(Fragment parent, Context mContext, List<CallItemModel> list) {
        this.list = list;
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

    private void onClickButton(long callId) {
        if (ContextCompat.checkSelfPermission(parent.getContext(), Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            parent.requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},
                    1);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = ContentUris.withAppendedId(CallLog.Calls.CONTENT_URI, callId);
            intent.setData(uri);
            parent.getContext().startActivity(intent);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
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
                onClickButton(mContent.getCall_id());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CallItemModel mContent = list.get(position);
        String name = mContent.getName();
        String number = mContent.getNumber();
        String type = mContent.getType();
        String duration = mContent.getCallTime();
        String date = mContent.getDate();

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
}
