package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class AdjustPhoneActivity extends ActivityGroup {
    private float acc_x;
    private float acc_y;
    private float acc_z;

    private Button collect_sit;
    private Button collect_stand;
    private Button collect_upstairs;
    private Button collect_downstairs;
    private Button collect_walk;
    private Button collect_jog;
    private Button start_adjust;
    private Button start_upload;
    private TextView progress_text;
    private ProgressBar progress_bar;
    private Button show_x;
    private Button show_y;
    private Button show_z;
    private ImageView phone;
    private TabHost tabhost;

    private Timer timer = new Timer();
    private SensorManager acc_sensor;
    private int sensor_type;

    private int sample = 0;
    private int ready = 3;
    final int SAMPLE = 200;
    final int PERCENT = 100;

    // 开始提示定时任务
    TimerTask hint_task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    };

    // 展示数据定时任务
    TimerTask display_task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    TimerTask phone_task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    };

    // Handler分发器
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                hint();
            if (msg.what == 1)
                display();
            if (msg.what == 2)
                phone();
            super.handleMessage(msg);
        }
    };

    // 提示任务执行
    private void hint(){
        switch (ready--){
            case 3:
                show_y.setText("3 !");
                break;
            case 2:
                show_y.setText("2 !");
                break;
            case 1:
                show_y.setText("1 !");
                break;
            case 0:
                show_y.setText("开始！");
                break;
        }
    }

    // 展示任务执行
    private void display() {
        if(sample < SAMPLE) {
            // acc_x_array[sample]=this.acc_x;
            // acc_y_array[sample]=this.acc_y;
            // acc_z_array[sample]=this.acc_z;
            show_x.setText(String.format("%.2f", this.acc_x));
            show_y.setText(String.format("%.2f", this.acc_y));
            show_z.setText(String.format("%.2f", this.acc_z));
            sample = sample + 1;
        } else {
            Log.v("acc","finish");
        }
    }

    private void phone() {
        phone.setImageResource(R.drawable.phone);
    }

    // 加速度数据监听
    final SensorEventListener acc_listener = new SensorEventListener() {
        @Override
        // 传感器值改变时调用
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                acc_x = sensorEvent.values[0];
                acc_y = sensorEvent.values[1];
                acc_z = sensorEvent.values[2];
            }
        }
        @Override
        // 传感器精度改变时调用
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    // 当前进度显示
    private void collect_progress(){
        new Thread(){
            @Override
            public void run() {
                int percent = 0;
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(percent++ < PERCENT) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int percent_final = percent;
                    progress_bar.setProgress(percent_final);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_text.setText(" " + percent_final + "%");
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_phone);

        // 各个组件
        collect_sit = findViewById(R.id.collect_sit);
        collect_stand = findViewById(R.id.collect_stand);
        collect_upstairs = findViewById(R.id.collect_upstairs);
        collect_downstairs = findViewById(R.id.collect_downstairs);
        collect_walk = findViewById(R.id.collect_walk);
        collect_jog = findViewById(R.id.collect_jog);
        start_adjust = findViewById(R.id.start_adjust);
        start_upload = findViewById(R.id.start_upload);
        progress_text = findViewById(R.id.tvProgress);
        progress_bar = findViewById(R.id.progressSelf);
        show_x = findViewById(R.id.show_x);
        show_y = findViewById(R.id.show_y);
        show_z = findViewById(R.id.show_z);
        phone = findViewById(R.id.phone);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();    //初始化TabHost组件
        tabhost.setup(this.getLocalActivityManager());
        tabhost.addTab(tabhost.newTabSpec("tab0").setIndicator("", getResources().getDrawable(R.drawable.sit_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.stand_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.upstairs_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.downstairs_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.walk_r)).setContent(new Intent(this, BlankActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab5").setIndicator("", getResources().getDrawable(R.drawable.jog_r)).setContent(new Intent(this, BlankActivity.class)));

        collect_sit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                timer.schedule(hint_task, 1000, 1000);
                collect_progress();
                timer.schedule(phone_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        collect_stand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                timer.schedule(hint_task, 1000, 1000);
                collect_progress();
                timer.schedule(phone_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        collect_upstairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                timer.schedule(hint_task, 1000, 1000);
                collect_progress();
                timer.schedule(phone_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        collect_downstairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                timer.schedule(hint_task, 1000, 1000);
                collect_progress();
                timer.schedule(phone_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        collect_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                timer.schedule(hint_task, 1000, 1000);
                collect_progress();
                timer.schedule(phone_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        collect_jog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                timer.schedule(hint_task, 1000, 1000);
                collect_progress();
                timer.schedule(phone_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        start_adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect_progress();
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });

        start_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect_progress();
                // Intent i = new Intent(AdjustPhoneActivity.this, MainActivity.class);
                // startActivity(i);
            }
        });
    }
}
