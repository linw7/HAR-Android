package com.example.lele.protoui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

public class RemindPhoneActivity extends AppCompatActivity {

    private TimePicker timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_phone);

        timepicker = (TimePicker) findViewById(R.id.timePicker);

    }
}
