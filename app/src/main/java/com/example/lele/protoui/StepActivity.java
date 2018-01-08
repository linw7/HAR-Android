package com.example.lele.protoui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.TextView;

import com.dd.CircularProgressButton;

public class StepActivity extends AppCompatActivity {

    private boolean runable = false;

    private TextView total_step;
    private TextView current_step;
    private TextView energy_step;
    private TextView distance_step;
    private CircularProgressButton circular_button;

    private float energy;
    private float distance;
    private int cur_step;
    private int range_step;
    private boolean new_start = false;
    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    private float mCount;//步行总数
    private float mDetector;//步行探测器
    private Timer timer = new Timer();

    TimerTask display_task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1)
                display();
            super.handleMessage(msg);
        }
    };

    private void display() {
        range_step = (int)(mCount - cur_step);
        distance = (float)((mCount * 0.75) / 1000);
        energy = (float) (68 * distance * 1.036);

        total_step.setText("总计：" + (int)(mCount));
        current_step.setText("当前：" + range_step);
        energy_step.setText("卡路里：" + String .format("%.2f", energy));
        distance_step.setText("距离：" + String .format("%.2f", distance));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        total_step = (TextView) findViewById(R.id.total_step);
        current_step = (TextView) findViewById(R.id.current_step);
        energy_step = (TextView) findViewById(R.id.energy_step);
        distance_step = (TextView) findViewById(R.id.distance_step);
        circular_button = (CircularProgressButton) findViewById(R.id.circular_button);

        final SensorEventListener step_listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                    if(new_start == false) {
                        new_start = true;
                        cur_step = (int)(sensorEvent.values[0]);
                    }
                    mCount = sensorEvent.values[0];
                }
                if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                    if(sensorEvent.values[0] == 1.0) {
                        mDetector = mDetector + 1;
                    }
                }
            }
            @Override
            // 传感器精度改变时调用
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mCount = 0;
        mDetector = 0;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mStepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(step_listener, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(step_listener, mStepDetector,SensorManager.SENSOR_DELAY_FASTEST);

        circular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!runable) {
                    runable = true;
                    circular_button.setIndeterminateProgressMode(true);
                    for (int i = 1; i < 100; i++)
                        circular_button.setProgress(i);
                    timer.schedule(display_task, 0, 100);
                    circular_button.setProgress(100);
                }
                else{
                    new AlertDialog.Builder(StepActivity.this)
                            .setTitle("请确认")
                            .setMessage("您正在处于步数监测状态，是否退出？")
                            .setPositiveButton(R.string.AlertDialog_yes, null)
                            .setNegativeButton(R.string.AlertDialog_no, null)
                            .show();
                }
            }
        });

    }
}
