package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.dd.CircularProgressButton;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class RemindPhoneActivity extends ActivityGroup {

    private CircularProgressButton set_start;
    private TextView time;
    private TextView activity;
    private CircularProgressView progressView;

    private float acc_x;
    private float acc_y;
    private float acc_z;

    private Timer timer = new Timer();
    private SensorManager acc_sensor;
    private int sensor_type;

    private int minute = 0;
    private int hour = 0;
    private int second = 0;

    private int sample = 0;
    final int SAMPLE = 40;
    private float acc_x_array[] = new float[SAMPLE];
    private float acc_y_array[] = new float[SAMPLE];
    private float acc_z_array[] = new float[SAMPLE];
    private float features[] = new float[8];

    final String W = "Walking";
    final String U = "Upstairs";
    final String D = "Downstairs";
    final String J = "Jogging";
    final String SI = "Sitting";
    final String ST = "Standing";
    private String result;

    private boolean [] check = {false, false, false, false, false, false};
    private final String [] ACTIVITY = {"静坐", "站立", "上楼", "下楼", "步行", "慢跑"};

    private String set_activity(){
        String str = "监控行为:";
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                str = str + ACTIVITY[i] + " ";
            }
        }
        return str;
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

    // Handler分发器
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                hint();
            if (msg.what == 1)
                display();
            super.handleMessage(msg);
        }
    };

    // 提示任务执行
    private void hint(){
    }

    // 展示任务执行
    private void display() {
        if(sample < SAMPLE) {
            acc_x_array[sample]=this.acc_x;
            acc_y_array[sample]=this.acc_y;
            acc_z_array[sample]=this.acc_z;
            sample = sample + 1;
        }
        if(sample == SAMPLE) {
            sample = 0;
            GetFeatures gf = new GetFeatures();
            features = gf.get_features(acc_x_array, acc_y_array, acc_z_array);
            GetClassifify gc = new GetClassifify();
            result = gc.activity_online(features);
            check_activity(result);
        }
    }

    private void check_activity(String result) {
        if(result == SI) {
            second = second - 2;
            Log.i("sec", "" + second);
            if(second == 0){
                warning();
            }
        }
    }

    private void warning() {
        new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("您预设的行为：静坐已达最大设定时间！")
                .show();
    }

    private void alter() {
        int minute = second / 60;
        String set_time = "您设置的监控时长为：" + minute + "分钟";

        int count = 0;
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                count = count + 1;
            }
        }
        String checked_activity = "您当前共监控" + count + "种行为，包括：";
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                checked_activity = checked_activity + ACTIVITY[i];
                checked_activity = checked_activity + " ";
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_set)
                .setMessage(set_time + "\n" + checked_activity)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_start.setIndeterminateProgressMode(true);
                        set_start.setProgress(100);
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        set_start.setProgress(-1);
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_phone);

        set_start = (CircularProgressButton) findViewById(R.id.set_start);
        time = (TextView) findViewById(R.id.time);
        activity = (TextView) findViewById(R.id.activity);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor_type = Sensor.TYPE_ACCELEROMETER;
        acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < check.length; i++)
                    check[i] = false;

                AlertDialog dialog = new AlertDialog.Builder(RemindPhoneActivity.this).
                        setTitle("请选择监控行为").
                        setIcon(R.drawable.logo_activity)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = set_activity();
                                Log.i("activity", str);
                            }
                        })
                        .setMultiChoiceItems(ACTIVITY, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                check[which] = true;
                            }
                        }).create();
                dialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(RemindPhoneActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        hour = h;
                        minute = m;
                        second = h * 3600 + m * 60;
                        String time_length = "" + (h * 60 + minute);
                        Log.i("time", "监控时长:" + time_length + "分钟");
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_start.setIndeterminateProgressMode(true);
                for(int i = 1; i < 100; i++)
                    set_start.setProgress(i);
                set_start.setProgress(99);
                alter();
                progressView.startAnimation();

                timer.schedule(display_task, 2000, 50);
            }
        });
    }
}
