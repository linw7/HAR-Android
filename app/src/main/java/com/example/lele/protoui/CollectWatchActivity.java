package com.example.lele.protoui;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CollectWatchActivity extends AppCompatActivity {

    final int SAMPLE = 40;
    final int BUFFLINE = 5000;

    private float acc_x;
    private float acc_y;
    private float acc_z;
    private float acc_x_array[] = new float[SAMPLE];
    private float acc_y_array[] = new float[SAMPLE];
    private float acc_z_array[] = new float[SAMPLE];

    private float features[] = new float[8];
    private String activity;

    private Switch switch_online;
    private Switch switch_offline;
    private ImageView result_online;
    private ImageView analysis_offline;
    private Button show_x;
    private Button show_y;
    private Button show_z;

    private ImageView unconnect;
    private ImageView connect;

    private Timer timer = new Timer();
    private SensorManager acc_sensor;
    private int sensor_type;

    private int sample = 0;
    private int ready = 3;
    private String path;

    final String W = "Walking";
    final String U = "Upstairs";
    final String D = "Downstairs";
    final String J = "Jogging";
    final String SI = "Sitting";
    final String ST = "Standing";

    private ArrayList<String> array_record_line = new ArrayList<String>();

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

    TimerTask record_task = new TimerTask() {
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
            if(msg.what == 2)
                record();
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
            acc_x_array[sample]=this.acc_x;
            acc_y_array[sample]=this.acc_y;
            acc_z_array[sample]=this.acc_z;

            if(sample % 10 == 0) {
                show_x.setText(String.format("%.2f", this.acc_x));
                show_y.setText(String.format("%.2f", this.acc_y));
                show_z.setText(String.format("%.2f", this.acc_z));
            }

            sample = sample + 1;
        }
        if(sample == SAMPLE) {
            sample = 0;

            GetFeatures gf = new GetFeatures();
            features = gf.get_features(acc_x_array, acc_y_array, acc_z_array);
            GetClassifify gc = new GetClassifify();
            activity = gc.activity_online(features);
            check_activity(activity);

            check_position();
        }
    }

    private void check_activity(String activity) {
        result_online.setImageResource(R.drawable.white);

        if(activity == W) {
            result_online.setImageResource(R.drawable.walk_r);
        }
        if(activity == J) {
            result_online.setImageResource(R.drawable.jog_r);
        }
        if(activity == U) {
            result_online.setImageResource(R.drawable.walk_r);
        }
        if(activity == D){
            result_online.setImageResource(R.drawable.walk_r);
        }
        if(activity == ST) {
            result_online.setImageResource(R.drawable.stand_r);
        }
        if(activity == SI) {
            result_online.setImageResource(R.drawable.sit_r);
        }
    }

    private void check_position() {
        unconnect.setImageResource(R.drawable.unconnect);
        connect.setImageResource(R.drawable.connect);
    }

    private void record() {
        String record_line = new String();
        record_line = String.format("%.2f", this.acc_x) + "," + String.format("%.2f", this.acc_y)
                + "," + String.format("%.2f", this.acc_z) + ";" + array_record_line.size() + "\n";
        array_record_line.add(record_line);

        show_x.setText(String.format("%.2f", this.acc_x));
        show_y.setText(String.format("%.2f", this.acc_y));
        show_z.setText(String.format("%.2f", this.acc_z));

        if(array_record_line.size() == BUFFLINE) {
            show_y.setText("Fresh");
            OfflineFileRW raw_rw = new OfflineFileRW();
            this.path = raw_rw.write_raw_data(array_record_line);
            array_record_line.clear();
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_watch);

        switch_online = (Switch) findViewById(R.id.switch_online);
        switch_offline = (Switch) findViewById(R.id.switch_offline);
        result_online = (ImageView) findViewById(R.id.result_online);
        analysis_offline = (ImageView) findViewById(R.id.analysis_offline);
        show_x = findViewById(R.id.show_x);
        show_y = findViewById(R.id.show_y);
        show_z = findViewById(R.id.show_z);
        connect = findViewById(R.id.connect);
        unconnect = findViewById(R.id.unconnect);

        switch_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                    sensor_type = Sensor.TYPE_ACCELEROMETER;
                    acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                    // task为任务，1000为延迟1000ms，每隔50ms收集一次
                    timer.schedule(hint_task, 1000, 1000);
                    timer.schedule(display_task, 5000, 50);
                } else {
                    timer.cancel();
                }
            }
        });

        switch_offline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                    sensor_type = Sensor.TYPE_ACCELEROMETER;
                    acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                    // task为任务，1000为延迟1000ms，每隔50ms收集一次
                    timer.schedule(hint_task, 1000, 1000);
                    timer.schedule(record_task, 5000, 500);
                } else {
                    timer.cancel();
                }
            }
        });

        result_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollectWatchActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });

        analysis_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CollectWatchActivity.this, SuggestActivity.class);
                startActivity(i);
            }
        });

    }
}
