package com.example.lele.protoui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class StepActivity extends AppCompatActivity {

    Button step;

    private SensorManager mSensorManager;
    private Sensor mStepCount;
    private Sensor mStepDetector;
    private float mCount;//步行总数
    private float mDetector;//步行探测器
    private Timer timer = new Timer();
    private static final int sensorTypeD=Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC=Sensor.TYPE_STEP_COUNTER;

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
        String step_display = "步数为：" + mCount;
        step.setText(step_display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        step = (Button) findViewById(R.id.step);
        step.setText("步数");

        // 加速度数据监听
        final SensorEventListener step_listener = new SensorEventListener() {
            @Override
            // 传感器值改变时调用
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
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

        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount = 0;
                mDetector = 0;

                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

                mStepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

                mSensorManager.registerListener(step_listener, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
                mSensorManager.registerListener(step_listener, mStepDetector,SensorManager.SENSOR_DELAY_FASTEST);

                timer.schedule(display_task, 1000, 500);

            }
        });


    }
}
