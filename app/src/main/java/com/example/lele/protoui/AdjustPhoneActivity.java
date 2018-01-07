package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Message;
import com.dd.CircularProgressButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;


public class AdjustPhoneActivity extends ActivityGroup {
    private float acc_x;
    private float acc_y;
    private float acc_z;

    private ImageView collect_sit;
    private ImageView collect_stand;
    private ImageView collect_upstairs;
    private ImageView collect_downstairs;
    private ImageView collect_walk;
    private ImageView collect_jog;
    private TextView text_process;
    private TextView start_adjust;
    private TextView start_upload;
    private TextView progress_text;
    private ProgressBar progress_bar;
    private TextView text_acc;
    private TextView show_x;
    private TextView show_y;
    private TextView show_z;

    private CircularProgressButton circular_button;

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

    TimerTask button_task = new TimerTask() {
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
                button();
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
            show_x.setText("x:" + String.format("%.2f", this.acc_x));
            show_y.setText("y:" + String.format("%.2f", this.acc_y));
            show_z.setText("z:" + String.format("%.2f", this.acc_z));
            sample = sample + 1;
        } else {
            Log.v("acc","finish");
        }
    }

    int i = 0;
    private void button() {
        if(i < 100){
            i = i + 1;
            circular_button.setProgress(i);
        }
        else {
            circular_button.setProgress(100);
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
        collect_sit = (ImageView) findViewById(R.id.collect_sit);
        collect_stand = (ImageView) findViewById(R.id.collect_stand);
        collect_upstairs = (ImageView) findViewById(R.id.collect_upstairs);
        collect_downstairs = (ImageView) findViewById(R.id.collect_downstairs);
        collect_walk = (ImageView) findViewById(R.id.collect_walk);
        collect_jog = (ImageView) findViewById(R.id.collect_jog);
        text_process = (TextView) findViewById(R.id.text_process);
        text_acc = (TextView) findViewById(R.id.text_acc);
        start_adjust = findViewById(R.id.start_adjust);
        start_upload = findViewById(R.id.start_upload);
        progress_text = findViewById(R.id.tvProgress);
        progress_bar = findViewById(R.id.progressSelf);
        circular_button = (CircularProgressButton) findViewById(R.id.circular_button);

        show_x = findViewById(R.id.show_x);
        show_y = findViewById(R.id.show_y);
        show_z = findViewById(R.id.show_z);

        circular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circular_button.setIndeterminateProgressMode(true);

                acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                sensor_type = Sensor.TYPE_ACCELEROMETER;
                acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);
                // task为任务，1000为延迟1000ms，每隔50ms收集一次
                collect_progress();
                timer.schedule(hint_task, 1000, 1000);
                timer.schedule(display_task, 5000, 500);
                timer.schedule(button_task, 0, 40);

                text_process.setText("采样进度");
                text_acc.setText("实时加速度");
            }
        });

        collect_sit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始采集行为")
                        .setMessage("您已选择静坐行为，确认后一分钟内请保持自然静坐状态，我们需要对该行为数据进行采集！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_sit.setImageResource(R.drawable.sit_r);
                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        collect_stand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始采集行为")
                        .setMessage("您已选择站立行为，确认后一分钟内请保持自然站立状态，我们需要对该行为数据进行采集！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_stand.setImageResource(R.drawable.stand_r);
                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        collect_upstairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始采集行为")
                        .setMessage("您已选择上楼行为，确认后一分钟内请开始上楼行为，我们需要对该行为数据进行采集！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_upstairs.setImageResource(R.drawable.upstairs_r);
                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        collect_downstairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始采集行为")
                        .setMessage("您已选择下楼行为，确认后一分钟内请开始下楼行为，我们需要对该行为数据进行采集！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_downstairs.setImageResource(R.drawable.downstairs_r);
                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        collect_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始采集行为")
                        .setMessage("您已选择步行行为，确认后一分钟内请开始日常行走，我们需要对该行为数据进行采集！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_walk.setImageResource(R.drawable.walk_r);
                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        collect_jog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始采集行为")
                        .setMessage("您已选择慢跑行为，确认后一分钟内请到开阔位置慢跑一分钟，我们需要对该行为数据进行采集！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collect_jog.setImageResource(R.drawable.jog_r);
                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        start_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("上传个人数据")
                        .setMessage("确认后您将上传刚刚采集到的您的个人行为数据，这些数据将有助于为您提供更精确的行为识别服务！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });

        start_adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AdjustPhoneActivity.this)
                        .setTitle("开始训练模型")
                        .setMessage("点击确认后，我们将在服务端开始计算服务，计算可能需要一些时间，请耐心等待！")
                        .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.AlertDialog_no, null)
                        .show();
            }
        });
    }
}
