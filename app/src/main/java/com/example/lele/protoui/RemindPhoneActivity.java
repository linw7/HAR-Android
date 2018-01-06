package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TimePicker;

import com.dd.CircularProgressButton;

import java.util.HashMap;

public class RemindPhoneActivity extends ActivityGroup {

    private TabHost tabhost;
    private TimePicker timepicker;
    private CircularProgressButton setting;
    private CircularProgressButton set_start;
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
        setting = (CircularProgressButton) findViewById(R.id.set_time);
        set_start = (CircularProgressButton) findViewById(R.id.set_start);
        timepicker.setIs24HourView(true);
        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();    //初始化TabHost组件

        tabhost.setup(this.getLocalActivityManager());

        tabhost.addTab(tabhost.newTabSpec("tab0").setIndicator("", getResources().getDrawable(R.drawable.sit_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.stand_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.upstairs_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.downstairs_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.walk_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab5").setIndicator("", getResources().getDrawable(R.drawable.jog_r)).setContent(new Intent(this, BlankActivity.class)));

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    setting.setProgress(i);

                second = 0;
                int hour = timepicker.getCurrentHour();
                int minute = timepicker.getCurrentMinute();
                times.put("Hours", hour);
                times.put("Minutes", minute);
                second = hour * 3600 + minute * 60;

                String str = new String();

                for(int i = 0; i < check.length; i++)
                    check[i] = true;

                for(int i = 0; i < check.length; i++){
                    if(check[i] == true) {
                        Log.i("" + i, ACTIVITY[i]);
                    }
                }

                setting.setProgress(100);
                alter();
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_start.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    set_start.setProgress(i);

                set_start.setProgress(100);
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
                checked_activity = checked_activity + ACTIVITY_C[i];
                checked_activity = checked_activity + " ";
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_set)
                .setMessage(set_time + "\n" + checked_activity)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }
}
