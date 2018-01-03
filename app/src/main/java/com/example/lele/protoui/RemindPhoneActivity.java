package com.example.lele.protoui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class RemindPhoneActivity extends AppCompatActivity {

    private TimePicker timepicker;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_phone);

        timepicker = (TimePicker) findViewById(R.id.timePicker);
        button = (Button) findViewById(R.id.buttone);

        timepicker.setIs24HourView(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timepicker.getCurrentHour();
                int minute = timepicker.getCurrentMinute();
                Log.i("hour", hour + "");
            }
        });

    }
}
