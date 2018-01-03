package com.example.lele.protoui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;

import java.util.HashMap;

public class RemindWatchActivity extends AppCompatActivity {

    private TimePicker timepicker;
    private Button set_activity;
    private Button set_time;
    private Button set_start;
    private Button set_end;
    private CheckBox check_sit;
    private CheckBox check_stand;
    private CheckBox check_upstair;
    private CheckBox check_downstair;
    private CheckBox check_walk;
    private CheckBox check_jog;
    private boolean [] check = {false, false, false, false, false, false};
    private HashMap times = new HashMap();
    private final String [] ACTIVITY = {"Sitting", "Standing", "Upstairs", "Downstairs", "Walking", "Jogging"};
    private int second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_watch);

        timepicker = (TimePicker) findViewById(R.id.timePicker);
        set_activity = (Button) findViewById(R.id.set_activity);
        set_time = (Button) findViewById(R.id.set_time);
        set_start = (Button) findViewById(R.id.set_start);
        set_end = (Button) findViewById(R.id.set_end);
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
                set_activity.setText("已设置");
            }
        });

        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timepicker.getCurrentHour();
                int minute = timepicker.getCurrentMinute();
                times.put("Hours", hour);
                times.put("Minutes", minute);
                second = hour * 3600 + minute * 60;
                set_time.setText("已设置");
            }
        });


        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        set_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
