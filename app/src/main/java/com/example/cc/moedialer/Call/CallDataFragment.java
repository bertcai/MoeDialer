package com.example.cc.moedialer.Call;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.cc.moedialer.Dialer.DialerActivity;
import com.example.cc.moedialer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cc on 18-1-9.
 */

public class CallDataFragment extends Fragment {
    private RecyclerView callListView;
    private List<CallItemModel> sourceDataList;
    private CallAdapter adapter;
    private View callView;
    private int lastOffset;
    private int lastPosition;
    private SharedPreferences sharedPreferences;
//    private FloatingActionButton dialerButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        callView = inflater.inflate(R.layout.call_list_fragment, container, false);
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},
                    1);
        } else {
            readCallData();
            init();
        }
        return callView;
    }

    private void init() {
        callListView = (RecyclerView) callView.findViewById(R.id.call_lv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        callListView.setLayoutManager(layoutManager);
        callListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() != null) {
                    getPositionAndOffset();
                }
            }
        });
        setAdapter();
//        initEvents();
//        setAdapter();

//        dialerButton = (FloatingActionButton) callView.findViewById(R.id.dialer_button);
//        dialerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(dialerButton.getVisibility()==View.VISIBLE){
//                    dialerButton.startAnimation(fabGoneAnimation);
//                    dialerButton.setVisibility(View.GONE);
//                }
//                Intent intent = new Intent(getContext(), DialerActivity.class);
//                getContext().startActivity(intent);
//            }
//        });
    }

    private void setAdapter() {
        adapter = new CallAdapter(this, this.getContext(), sourceDataList);
        callListView.setAdapter(adapter);
    }

//    private void initEvents() {
//        //click the item
//        callListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String number = ((CallItemModel) adapter.getItem(position)).getNumber();
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:" + number));
//                startActivity(intent);
//            }
//        });
//    }


    private void getPositionAndOffset() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) callListView.getLayoutManager();
        View topView = layoutManager.getChildAt(0);
        if (topView != null) {
            lastOffset = topView.getTop();
            lastPosition = layoutManager.getPosition(topView);

            sharedPreferences = this.getActivity()
                    .getSharedPreferences("key", this.getActivity().MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt("lastOffset", lastOffset);

            editor.putInt("lastPosition", lastPosition);

            editor.commit();
        }
    }

    private void readCallData() {
        sourceDataList = new ArrayList<>();
        Cursor cursor = null;
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},
                    1);
        }
        try {
            cursor = this.getContext()
                    .getContentResolver()
                    .query(CallLog.Calls.CONTENT_URI,
                            new String[]{
                                    CallLog.Calls.CACHED_NAME,
                                    CallLog.Calls.NUMBER,
                                    CallLog.Calls.TYPE,
                                    CallLog.Calls.DATE,
                                    CallLog.Calls.DURATION,
                                    CallLog.Calls._ID
                            }, null, null,
                            CallLog.Calls.DEFAULT_SORT_ORDER
                    );

            int i = 0;
            if (cursor != null && cursor.getCount() > 0) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date_today = simpleDateFormat.format(date);

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(), i++) {
                    CallItemModel callItem;
                    String callName = cursor.getString(0);
                    String callNumber = cursor.getString(1);

                    //Call Type
                    int callType = Integer.parseInt(cursor.getString(2));
                    String callTypeStr = "";
                    switch (callType) {
                        case CallLog.Calls.INCOMING_TYPE:
                            callTypeStr = "呼入";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            callTypeStr = "呼出";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            callTypeStr = "未接通";
                            break;
                        default:
                            i--;
                            continue;
                    }

                    //Call Date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat detailDateFormat =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date callDate = new Date(Long.parseLong(cursor.getString(3)));
                    String trueDate = detailDateFormat.format(callDate);
                    String callDateStr = sdf.format(callDate);
                    if (callDateStr.equals(date_today)) { //is today?
                        sdf = new SimpleDateFormat("HH:mm");
                        callDateStr = sdf.format(callDate);
                    } else if (date_today.contains(callDateStr.substring(0, 7))) { //is this month ?
                        sdf = new SimpleDateFormat("dd");
                        int callDay = Integer.valueOf(sdf.format(callDate));

                        int day = Integer.valueOf(sdf.format(date));
                        if (day - callDay == 1) {
                            callDateStr = "昨天";
                        } else {
                            sdf = new SimpleDateFormat("MM-dd");
                            callDateStr = sdf.format(callDate);
                        }
                    } else if (date_today.contains(callDateStr.substring(0, 4))) { //is this year ?
                        sdf = new SimpleDateFormat("MM-dd");
                        callDateStr = sdf.format(callDate);
                    }

                    //Call Duration
                    int callDuration = Integer.parseInt(cursor.getString(4));
                    int min = callDuration / 60;
                    int sec = callDuration % 60;
                    String callDurationStr = "";
                    if (sec > 0) {
                        if (min > 0) {
                            callDurationStr = min + "分" + sec + "秒";
                        } else {
                            callDurationStr = sec + "秒";
                        }
                    }

                    long callId = Integer.parseInt(cursor.getString(5));

                    callItem = new CallItemModel(callName, callTypeStr,
                            callDateStr, callNumber, callDurationStr, callId, trueDate);
                    sourceDataList.add(callItem);
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
                    readCallData();
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
    public void onPause() {
        super.onPause();
        getPositionAndOffset();
//        if(dialerButton.getVisibility()==View.VISIBLE){
//            dialerButton.startAnimation(fabGoneAnimation);
//            dialerButton.setVisibility(View.GONE);
//        }
    }

    //use resume to achieve live-update
    @Override
    public void onResume() {
        super.onResume();
        readCallData();
        init();
        scrollToPosition();
//        if(dialerButton.getVisibility()==View.GONE){
//            dialerButton.startAnimation(fabAppearAnimation);
//            dialerButton.setVisibility(View.VISIBLE);
//        }
    }

    private void scrollToPosition() {

        sharedPreferences = this.getActivity()
                .getSharedPreferences("key", this.getActivity().MODE_PRIVATE);

        lastOffset = sharedPreferences.getInt("lastOffset", 0);

        lastPosition = sharedPreferences.getInt("lastPosition", 0);

        if (callListView.getLayoutManager() != null && lastPosition >= 0) {

            ((LinearLayoutManager) callListView.getLayoutManager())
                    .scrollToPositionWithOffset(lastPosition, lastOffset);

        }

    }
}
