package com.example.lele.protoui;


import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CollectPhoneActivity extends ActivityGroup {
    final int SAMPLE = 40;
    final int BUFFLINE = 5;

    private float acc_x;
    private float acc_y;
    private float acc_z;
    private float acc_x_array[] = new float[SAMPLE];
    private float acc_y_array[] = new float[SAMPLE];
    private float acc_z_array[] = new float[SAMPLE];

    private float features[] = new float[8];
    private String activity;

    private SwitchButton switch_online;
    private SwitchButton switch_offline;

    private TextView show_x;
    private TextView show_y;
    private TextView show_z;
    /*
    private ImageView image_hand;
    private ImageView image_read;
    private ImageView image_shirt;
    private ImageView image_trousers;
    */
    private TextView set_upload;
    private TextView set_analysis;

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

            // check_position();
        }
    }

    /*
    private void check_position() {
        // 新周期清空显示
        image_hand.setImageResource(R.drawable.white);
        image_read.setImageResource(R.drawable.white);
        image_shirt.setImageResource(R.drawable.white);
        image_trousers.setImageResource(R.drawable.white);

        if(features[3] > -10 && features[3] <= 5){
            image_hand.setImageResource(R.drawable.hand);
        }
        if(features[3] > 5 && features[3] <= 9) {
            image_read.setImageResource(R.drawable.read);
        }
        if(features[3] > 9 && features[3] <= 13) {
            image_shirt.setImageResource(R.drawable.shirt);
        }
        if(features[3] > 13){
            image_trousers.setImageResource(R.drawable.trousers);
        }
    }
    */

    private void record() {
        String record_line = new String();
        record_line = String.format("%.2f", this.acc_x) + "," + String.format("%.2f", this.acc_y)
                + "," + String.format("%.2f", this.acc_z) + ";" + array_record_line.size() + "\n";
        array_record_line.add(record_line);

        show_x.setText(String.format("%.2f", this.acc_x));
        show_y.setText(String.format("%.2f", this.acc_y));
        show_z.setText(String.format("%.2f", this.acc_z));

        if(array_record_line.size() == BUFFLINE) {
            OfflineFileRW raw_rw = new OfflineFileRW();
            raw_rw.save(CollectPhoneActivity.this, "record.txt", array_record_line);
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
        setContentView(R.layout.activity_collect_phone);

        switch_online = (SwitchButton) findViewById(R.id.switch_online);
        switch_offline = (SwitchButton) findViewById(R.id.switch_offline);

        show_x = findViewById(R.id.show_x);
        show_y = findViewById(R.id.show_y);
        show_z = findViewById(R.id.show_z);

        set_upload = findViewById(R.id.set_upload);
        set_analysis = findViewById(R.id.set_analysis);

        acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor_type = Sensor.TYPE_ACCELEROMETER;
        acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);

        set_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CollectPhoneActivity.this)
                        .setTitle("上传数据")
                        .setMessage("请确认是否上传数据")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        set_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CollectPhoneActivity.this)
                        .setTitle("分析数据")
                        .setMessage("请确认开始分析数据")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        switch_online.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) {
                    timer.schedule(hint_task, 1000, 1000);
                    timer.schedule(display_task, 5000, 50);
                } else {
                    timer.cancel();
                }
            }
        });

        switch_offline.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked) {
                    OfflineFileRW raw_rw = new OfflineFileRW();
                    raw_rw.clear(CollectPhoneActivity.this, "record.txt");

                    // timer.schedule(hint_task, 1000, 1000);
                    timer.schedule(record_task, 5000, 500);
                } else {
                    timer.cancel();
                }
            }
        });
    }
}
