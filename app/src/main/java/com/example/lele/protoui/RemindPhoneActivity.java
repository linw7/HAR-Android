package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.dd.CircularProgressButton;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class RemindPhoneActivity extends ActivityGroup {

    private CircularProgressButton set_start;
    private TextView time;
    private TextView activity;
    private TextView time_start;
    private TextView time_end;
    private TextView energy_step;
    private TextView distance_step;
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
    private int click_times = 0;

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

    private int choice_time = 0;
    private int choice_activity = 0;

    private int tag = 0;
    private int retag = 0;
    private int full = 0;

    private boolean [] check = {false, false, false, false, false, false};
    private final String [] ACTIVITY = {"静坐", "站立", "上楼", "下楼", "步行", "慢跑"};
    private int set_time [] = {0, 0, 0, 0, 0, 0};
    private int dur_time [] = {0, 0, 0, 0, 0, 0};

    private int start_hour [] = {0, 0, 0, 0, 0, 0};
    private int start_minute [] = {0, 0, 0, 0, 0, 0};
    private int start_second [] = {0, 0, 0, 0, 0, 0};
    private int has_tag [] = {0, 0, 0, 0, 0, 0};

    private String set_activity(){
        String str = "监控行为:";
        for(int i = 0; i < check.length; i++){
            if(check[i] == true){
                str = str + ACTIVITY[i] + " ";
            }
        }
        return str;
    }

    private void get_end_time(){
        final Calendar mCalendar=Calendar.getInstance();
        int mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts=mCalendar.get(Calendar.MINUTE);
        int mSeconds=mCalendar.get(Calendar.SECOND);
        String end_time = new String();
        if(mMinuts < 10 && mSeconds < 10)
            end_time = mHour + ":0" + mMinuts + ":0" + mSeconds;
        if(mMinuts < 10 && mSeconds >=10)
            end_time = mHour + ":0" + mMinuts + ":" + mSeconds;
        if(mMinuts >= 10 && mSeconds < 10)
            end_time = mHour + ":" + mMinuts + ":0" + mSeconds;
        if(mMinuts >= 10 && mSeconds >= 10)
            end_time = mHour + ":" + mMinuts + ":" + mSeconds;
        time_end.setText(end_time);
    }

    private void get_start_time(int i){
        final Calendar mCalendar=Calendar.getInstance();
        int mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts=mCalendar.get(Calendar.MINUTE);
        int mSeconds=mCalendar.get(Calendar.SECOND);
        start_hour[i] = mHour;
        start_minute[i] = mMinuts;
        start_second[i] = mSeconds;
    }

    private void show_start_time(int i){
        String h = "" + start_hour[i];
        String m = new String();
        String s = new String();
        if(start_minute[i] >= 10) {
            m = "" + start_minute[i];
        }else {
            m = "0" + start_minute[i];
        }
        if(start_second[i] >= 10){
            s = "" + start_second[i];
        }
        else{
            s = "0" + start_second[i];
        }
        String start_time = h + ":" + m + ":" + s;
        time_start.setText(start_time);
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
    }

    private String act_e2c(String result){
        String act = new String();
        if(result == SI)
            act = "静坐";
        if(result == ST)
            act = "站立";
        if(result == U)
            act = "上楼";
        if(result == D)
            act = "下楼";
        if(result == W)
            act = "步行";
        if(result == J)
            act = "慢跑";
        return act;
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

            String act = act_e2c(result);
            int index = res_act(act);

            if(retag == 1) {
                energy_step.setText(act + "持续中！");
                distance_step.setText("累计:" + check_times(act) + "秒");
                check_activity(index);
            }
            else {
                energy_step.setText("持续行为");
                distance_step.setText("持续时间");
            }
        }
    }

    private int check_times(String act){
        int during = 0;
        if(full == 0) {
            if (act == "静坐") {
                if(has_tag[0] == 0) {
                    get_start_time(0);
                    has_tag[0] = 1;
                }
                if(has_tag[0] == 1)
                    show_start_time(0);
                dur_time[0] = dur_time[0] + 2;
                during = dur_time[0];
            }
            if (act == "站立") {
                if(has_tag[1] == 0) {
                    get_start_time(1);
                    has_tag[1] = 1;
                }
                if(has_tag[1] == 1)
                    show_start_time(1);
                dur_time[1] = dur_time[1] + 2;
                during = dur_time[1];
            }
            if (act == "上楼") {
                if(has_tag[2] == 0) {
                    get_start_time(2);
                    has_tag[2] = 1;
                }
                if(has_tag[2] == 1)
                    show_start_time(2);
                dur_time[2] = dur_time[2] + 2;
                during = dur_time[2];
            }
            if (act == "下楼") {
                if(has_tag[3] == 0) {
                    get_start_time(3);
                    has_tag[3] = 1;
                }
                if(has_tag[3] == 1)
                    show_start_time(3);
                dur_time[3] = dur_time[3] + 2;
                during = dur_time[3];
            }
            if (act == "步行") {
                if(has_tag[4] == 0) {
                    get_start_time(4);
                    has_tag[4] = 1;
                }
                if(has_tag[4] == 1)
                    show_start_time(4);
                dur_time[4] = dur_time[4] + 2;
                during = dur_time[4];
            }
            if (act == "慢跑") {
                if(has_tag[5] == 0) {
                    get_start_time(5);
                    has_tag[5] = 1;
                }
                if(has_tag[5] == 1)
                    show_start_time(5);
                dur_time[5] = dur_time[5] + 2;
                during = dur_time[5];
            }
        }
        return during;
    }

    private void button() {
        if(tag == 0){
            set_start.setProgress(0);
        }
        else{
            set_start.setProgress(100);
            get_end_time();
        }
    }

    private void check_activity(int i) {
        if ((i != 6) && (check[i] == true)) {
            set_time[i] = set_time[i] - 2;
            if (set_time[i] == 0)
                warning(i);
        } else {

        }
    }

    private void re_init(){
        tag = 0;
        retag = 0;
        full = 0;
        choice_activity = 0;
        choice_time = 0;
        for(int i = 0; i < 6; i++)
            check[i] = false;
        for(int i = 0; i < 6; i++) {
            set_time[i] = 0;
            dur_time[i] = 0;
            start_hour[i] = 0;
            start_minute[i] = 0;
            start_second[i] = 0;
            has_tag[i] = 0;
        }
        progressView.stopAnimation();
        time_start.setText("开始时间");
        time_end.setText("结束时间");
    }

    private int res_act(String result){
        if(result == "静坐")
            return 0;
        if(result == "步行")
            return 1;
        if(result == "上楼")
            return 2;
        if(result == "下楼")
            return 3;
        if(result == "步行")
            return 4;
        if(result == "慢跑")
            return 5;
        return 6;
    }

    private String msg_act(int i){
        String str = new String();
        if(i == 0)
            str = "静坐";
        if(i == 1)
            str = "步行";
        if(i == 2)
            str = "上楼";
        if(i == 3)
            str = "下楼";
        if(i == 4)
            str = "步行";
        if(i == 5)
            str = "慢跑";
        return str;
    }

    private void warning(int i) {
        long[] v_mode=new long[]{500,500,500,500,500,500,500,500};
        BeeAndVibrateManager bv = new BeeAndVibrateManager();
        bv.vibrate(this, v_mode, false);
        bv.playBee(this);


        full = 1;
        String msg = msg_act(i) + "已达最大设定时间！"+  "为了您的健康，请换一种运动方式！";

        new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        re_init();
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        re_init();
                    }
                })
                .show();
    }

    private boolean has_set(){
        for(int i = 0; i < 6; i++){
            if(check[i] == true)
                return true;
        }
        return false;
    }

    private void alter_ok() {
        click_times = click_times + 1;
        int real_minute = hour * 60 + minute;
        String set_time = "您设置的监控时长为：" + real_minute + "分钟";

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
                        if(click_times == 1) {
                            timer.schedule(display_task, 1000, 50);
                            timer.schedule(button_task, 0, 100);
                        }
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

    private void alter_no(int choice_time, int choice_activity) {
        String msg = new String();
        if((choice_time == 0) && (choice_activity == 1))
            msg = "您还未知预警时长，请重新设置！";
        if((choice_time == 1) && (choice_activity == 0))
            msg = "您还未知预警行为，请重新设置！";
        if((choice_time == 0) && (choice_activity == 0))
            msg = "您还未知预警时长和预警行为，请重新设置！";
        if(has_set() == false)
            msg = "请确认您已勾选待检测行为！";

        new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage(msg)
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

        energy_step = (TextView) findViewById(R.id.energy_step);
        distance_step = (TextView)findViewById(R.id.distance_step);

        time_start = (TextView)findViewById(R.id.time_start);
        time_end = (TextView)findViewById(R.id.time_end);

        acc_sensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor_type = Sensor.TYPE_ACCELEROMETER;
        acc_sensor.registerListener(acc_listener, acc_sensor.getDefaultSensor(sensor_type), SensorManager.SENSOR_DELAY_FASTEST);

        set_start.setIndeterminateProgressMode(true);

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
                                choice_activity = 1;
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
                        // 此处为test
                        second = 10;
                        // second = h * 3600 + m * 60;
                        String time_length = "" + (h * 60 + minute);
                        choice_time = 1;
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = 1;
                retag = 1;
                set_start.setProgress(60);
                progressView.startAnimation();
                if((choice_time == 1) && (choice_activity == 1) && has_set()) {
                    for(int i = 0; i < 6; i++){
                        if(check[i] == true){
                            set_time[i] = second;
                            Log.i("" + ACTIVITY[i],"" + set_time[i]);
                        }
                    }
                    alter_ok();
                } else
                    alter_no(choice_time, choice_activity);
            }
        });
    }
}
