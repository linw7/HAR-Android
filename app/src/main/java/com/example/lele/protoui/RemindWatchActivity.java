package com.example.lele.protoui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

public class RemindWatchActivity extends AppCompatActivity {

    private TimePicker timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_watch);

        timepicker = (TimePicker) findViewById(R.id.timePicker);

    }
}
