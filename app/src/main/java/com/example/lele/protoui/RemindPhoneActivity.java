package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.dd.CircularProgressButton;
import java.util.Calendar;


public class RemindPhoneActivity extends ActivityGroup {

    private CircularProgressButton set_start;
    private TextView time;
    private TextView activity;
    private CircularProgressView progressView;

    private int minute = 0;
    private int hour = 0;
    private int second = 0;

    private boolean [] check = {false, false, false, false, false, false};
    private final String [] ACTIVITY = {"静坐", "站立", "上楼", "下楼", "步行", "慢跑"};

    private String set_activity(){
        String str = "监控行为:";
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                str = str + ACTIVITY[i] + " ";
            }
        }
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_phone);

        set_start = (CircularProgressButton) findViewById(R.id.set_start);
        time = (TextView) findViewById(R.id.time);
        activity = (TextView) findViewById(R.id.activity);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < check.length; i++)
                    check[i] = false;

                AlertDialog dialog = new AlertDialog.Builder(RemindPhoneActivity.this).
                        setTitle("请选择监控行为").
                        setIcon(R.drawable.logo_activity)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = set_activity();
                                Log.i("activity", str);
                            }
                        })
                        .setMultiChoiceItems(ACTIVITY, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                check[which] = true;
                            }
                        }).create();
                dialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(RemindPhoneActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        hour = h;
                        minute = m;
                        second = h * 3600 + m * 60;
                        String time_length = "" + (h * 60 + minute);
                        Log.i("time", "监控时长:" + time_length + "分钟");
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_start.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    set_start.setProgress(i);
                set_start.setProgress(99);
                alter();

                // set_start.setProgress(100);
                progressView.startAnimation();
            }
        });
    }

    private void alter() {
        int minute = second / 60;
        String set_time = "您设置的监控时长为：" + minute + "分钟";

        int count = 0;
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                count = count + 1;
            }
        }
        String checked_activity = "您当前共监控" + count + "种行为，包括：";
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                checked_activity = checked_activity + ACTIVITY[i];
                checked_activity = checked_activity + " ";
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_set)
                .setMessage(set_time + "\n" + checked_activity)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_start.setIndeterminateProgressMode(true);
                        set_start.setProgress(100);
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_start.setProgress(-1);
                    }
                })
                .show();
    }
}
