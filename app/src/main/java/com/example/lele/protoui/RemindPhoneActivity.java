package com.example.lele.protoui;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.dd.CircularProgressButton;

import java.util.HashMap;

public class RemindPhoneActivity extends AppCompatActivity {

    private TimePicker timepicker;
    private CheckBox check_sit;
    private CheckBox check_stand;
    private CheckBox check_upstair;
    private CheckBox check_downstair;
    private CheckBox check_walk;
    private CheckBox check_jog;
    CircularProgressButton set_start;
    CircularProgressButton set_end;
    CircularProgressButton set_time;
    CircularProgressButton set_activity;

    private boolean [] check = {false, false, false, false, false, false};
    private HashMap times = new HashMap();
    private final String [] ACTIVITY = {"Sitting", "Standing", "Upstairs", "Downstairs", "Walking", "Jogging"};
    private final String [] ACTIVITY_C = {"静坐", "站立", "上楼", "下楼", "步行", "慢跑"};
    private int second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_phone);

        timepicker = (TimePicker) findViewById(R.id.timePicker);
        set_activity = (CircularProgressButton) findViewById(R.id.set_activity);
        set_time = (CircularProgressButton) findViewById(R.id.set_time);
        set_start = (CircularProgressButton) findViewById(R.id.set_start);
        set_end = (CircularProgressButton) findViewById(R.id.set_end);
        check_sit = (CheckBox) findViewById(R.id.check_sit);
        check_stand = (CheckBox) findViewById(R.id.check_stand);
        check_upstair = (CheckBox) findViewById(R.id.check_upstair);
        check_downstair = (CheckBox) findViewById(R.id.check_downstair);
        check_walk = (CheckBox) findViewById(R.id.check_walk);
        check_jog = (CheckBox) findViewById(R.id.check_jog);
        timepicker.setIs24HourView(true);

        set_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = new String();

                for(int i = 0; i < check.length; i++)
                    check[i] = false;

                if(check_sit.isChecked()){
                    check[0] = true;
                }
                if(check_stand.isChecked()){
                    check[1] = true;
                }
                if(check_upstair.isChecked()){
                    check[2] = true;
                }
                if(check_downstair.isChecked()){
                    check[3] = true;
                }
                if(check_walk.isChecked()){
                    check[4] =true;
                }
                if(check_jog.isChecked()){
                    check[5] = true;
                }
                for(int i = 0; i < check.length; i++){
                    if(check[i] == true) {
                        Log.i("" + i, ACTIVITY[i]);
                    }
                }
                set_activity.setText("已锁定");
                alert_activity();
            }
        });

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second = 0;
                int hour = timepicker.getCurrentHour();
                int minute = timepicker.getCurrentMinute();
                times.put("Hours", hour);
                times.put("Minutes", minute);
                second = hour * 3600 + minute * 60;
                set_time.setText("已锁定");
                alter_time();
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_start.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    set_start.setProgress(i);

                // Start !

                set_start.setProgress(100);

            }
        });

        set_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_start.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    set_start.setProgress(i);

                // Start !

                set_start.setProgress(100);
            }
        });
    }

    private void alert_activity() {
        int count = 0;
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                count = count + 1;
            }
        }
        String checked_activity = "您当前共监控" + count + "种行为，包括：";
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                checked_activity = checked_activity + ACTIVITY_C[i];
                checked_activity = checked_activity + " ";
            }
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_activity)
                .setMessage(checked_activity)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }

    private void alter_time() {
        int minute = second / 60;
        String set_time = "您设置的监控时长为：" + minute + "分钟";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_time)
                .setMessage(set_time)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }
}
