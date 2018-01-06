package com.example.lele.protoui;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.suke.widget.SwitchButton;
import com.dd.CircularProgressButton;

import java.util.Calendar;


public class TestActivity extends AppCompatActivity {

    SwitchButton switchButton;
    CircularProgressButton circular_button;
    CircularProgressView progressView;
    FlowingDrawer mDrawer;
    TextView click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        switchButton = (SwitchButton) findViewById(R.id.switch_button);
        circular_button = (CircularProgressButton) findViewById(R.id.circular_button);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        click = (TextView) findViewById(R.id.click);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(TestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TestActivity.this.click.setText(hourOfDay +  "-" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });


        circular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circular_button.setIndeterminateProgressMode(true);
                circular_button.setProgress(20);
                circular_button.setProgress(40);
                circular_button.setProgress(60);
                circular_button.setProgress(80);
                circular_button.setProgress(100);

                progressView.startAnimation();
            }
        });

        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL); // 滑动模式
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }
            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }
}
