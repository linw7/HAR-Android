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

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dd.CircularProgressButton;

public class StepMainActivity extends AppCompatActivity {

    final float PERCENT = 100;
    final float DEFAULT_STEP = 1000;
    final float DEFAULT_ENERGY = 20;
    float step_pencent = 0;
    float energy_percent = 0;

    private TextView total_step;
    private TextView current_step;
    private TextView energy_step;
    private TextView distance_step;
    private CircularProgressButton circular_button;
    private ProgressBar progress_step;
    private ProgressBar progress_energy;
    private TextView progress_step_text;
    private TextView progress_energy_text;

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
        distance = (float)((range_step * 0.75) / 1000);
        energy = (float) (68 * distance * 1.036);

        total_step.setText("总计：" + (int)(mCount));
        current_step.setText("当前：" + range_step);
        energy_step.setText("卡路里：" + String .format("%.2f", energy));
        distance_step.setText("距离：" + String .format("%.2f", distance));
    }

    private void step_progress(){
        new Thread(){
            @Override
            public void run() {
                while(step_pencent <= PERCENT) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    step_pencent = (float) ((float)range_step / DEFAULT_STEP) * 100 ;
                    progress_step.setProgress((int)(step_pencent));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_step_text.setText(" " + (int)(step_pencent) + "%");
                        }
                    });
                }
            }
        }.start();
    }

    private void energy_progress() {
        new Thread() {
            @Override
            public void run() {
                while (energy_percent <= PERCENT) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    energy_percent = (float) ((float)energy / DEFAULT_ENERGY) * 100;
                    Log.i("s","" + energy_percent);
                    progress_energy.setProgress((int) (energy));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_energy_text.setText(" " + (int)energy + "%");
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_main);
        total_step = (TextView) findViewById(R.id.total_step);
        current_step = (TextView) findViewById(R.id.current_step);
        energy_step = (TextView) findViewById(R.id.energy_step);
        distance_step = (TextView) findViewById(R.id.distance_step);
        circular_button = (CircularProgressButton) findViewById(R.id.circular_button);
        progress_step = (ProgressBar) findViewById(R.id.progress_step);
        progress_energy = (ProgressBar) findViewById(R.id.progress_energy);
        progress_step_text = (TextView) findViewById(R.id.progress_step_text);
        progress_energy_text = (TextView) findViewById(R.id.progress_energy_text);

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

        circular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circular_button.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    circular_button.setProgress(i);

                mCount = 0;
                mDetector = 0;

                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                mStepCount = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
                mStepDetector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

                mSensorManager.registerListener(step_listener, mStepCount, SensorManager.SENSOR_DELAY_FASTEST);
                mSensorManager.registerListener(step_listener, mStepDetector,SensorManager.SENSOR_DELAY_FASTEST);
                timer.schedule(display_task, 0, 100);

                energy_progress();
                step_progress();

                circular_button.setProgress(100);
            }
        });

    }
}
