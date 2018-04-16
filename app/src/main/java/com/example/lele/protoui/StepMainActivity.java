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
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class StepMainActivity extends AppCompatActivity {

    private boolean runable = false;

    private final float PERCENT = 100;
    private float step_pencent = 0;
    private float energy_percent = 0;

    private TextView energy_step;
    private TextView distance_step;
    private CircularProgressButton circular_button;
    private ProgressBar progress_step;
    private ProgressBar progress_energy;
    private TextView progress_step_text;
    private TextView progress_energy_text;
    private CircularProgressView progressView;
    private TextView target_step;
    private TextView target_energy;
    private TextView text_step;
    private TextView text_energy;

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

    private int step_finished = 0;
    private int energy_finished = 0;

    private int set_step = 0;
    private int set_energy = 0;

    int mutex = 1;

    private final String [] STEP = {"50步", "4000步", "6000步", "8000步", "10000步"};
    private final String [] ENERGY = {"3卡路里", "600卡路里", "900卡路里", "1200卡路里", "1500卡路里"};

    private void re_init(){
        progressView.stopAnimation();

        runable = false;
        set_energy = 0;
        set_step = 0;
        step_finished = 0;
        energy_finished = 0;

        range_step = 0;
        energy = 0;
        distance = 0;

        cur_step = (int)mCount;

        progress_step.setProgress(0);
        progress_step_text.setText("");

        progress_energy.setProgress(0);
        progress_energy_text.setText("");
    }

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

    private boolean is_zero(float num){
        if((num >= -0.000001) && (num <= 0.000001))
            return true;
        else
            return false;
    }

    private void display() {
        if(runable){
            range_step = (int)(mCount - cur_step);
            distance = (float)((range_step * 0.75) / 1000);
            energy = (float) (68 * distance * 1.036);
        }else{
            range_step = 0;
            distance = 0;
            energy = 0;
        }

        if((Float.compare(energy,0.0f) == 0) && (Float.compare(distance,0.0f) == 0)){
            energy_step.setText("卡路里");
            distance_step.setText("距离");
        }
        energy_step.setText("卡路里：" + String .format("%.2f", energy));
        distance_step.setText("距离：" + String .format("%.2f", distance));
    }

    // 弹窗、提示、恢复
    private void recover(){
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setIcon(R.drawable.finish_b)
                .setMessage("您今日的步数和消耗卡路里数已达到预设目标！请继续保持！")
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
        long[] v_mode=new long[]{500,500,500,500,500,500,500,500};
        BeeAndVibrateManager bv = new BeeAndVibrateManager();
        bv.vibrate(this, v_mode, false);
        bv.playBee(this);
    }

    private void step_progress(){
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    step_pencent = (float) ((float)range_step / set_step) * 100 ;
                    if((int)step_pencent > 100)
                        step_pencent = 100;
                    progress_step.setProgress((int)(step_pencent));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_step_text.setText((int)(step_pencent) + "%");
                        }
                    });
                    if(step_pencent >= PERCENT)
                        break;
                }
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress_step_text.setText("100%");
                    step_finished = 1;
                    if ((energy_finished == 1) && (step_finished == 1)) {
                        if(mutex == 1) {
                            --mutex;
                            recover();
                            break;
                        }
                        if(mutex == 0)
                            break;
                    }
                }
            }
        }.start();
    }

    private void energy_progress() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    energy_percent = (float) ((float)energy / set_energy) * 100;
                    if((int)energy_percent > 100)
                        energy_percent = 100;
                    progress_energy.setProgress((int) (energy_percent));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_energy_text.setText((int)(energy_percent) + "%");
                        }
                    });
                    if((int)energy_percent >= PERCENT)
                        break;;
                }
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress_energy_text.setText("100%");
                    energy_finished = 1;
                    if ((energy_finished == 1) && (step_finished == 1)) {
                        if(mutex == 1) {
                            --mutex;
                            recover();
                            break;
                        }
                        if(mutex == 0)
                            break;
                    }
                }
            }
        }.start();
    }

    private void reset(){
        new AlertDialog.Builder(StepMainActivity.this)
                .setTitle("请确认")
                .setMessage("您正在处于步数监测状态，是否退出？")
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        circular_button.setProgress(0);
                        re_init();
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }

    private void run(){
        runable = true;
        circular_button.setProgress(60);

        if(set_step == 0 || set_energy == 0){
            new AlertDialog.Builder(StepMainActivity.this)
                    .setTitle("请完成设置")
                    .setMessage("您还未设置目标，请在上方进行设置！")
                    .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runable = false;
                            circular_button.setProgress(0);
                        }
                    })
                    .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            runable = false;
                            circular_button.setProgress(0);
                        }
                    })
                    .show();
        }
        else {
            energy_progress();
            step_progress();
            circular_button.setProgress(100);
            progressView.startAnimation();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_main);

        energy_step = (TextView) findViewById(R.id.energy_step);
        distance_step = (TextView) findViewById(R.id.distance_step);
        circular_button = (CircularProgressButton) findViewById(R.id.circular_button);
        progress_step = (ProgressBar) findViewById(R.id.progress_step);
        progress_energy = (ProgressBar) findViewById(R.id.progress_energy);
        progress_step_text = (TextView) findViewById(R.id.progress_step_text);
        progress_energy_text = (TextView) findViewById(R.id.progress_energy_text);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        target_step = (TextView) findViewById(R.id.target_step);
        target_energy = (TextView) findViewById(R.id.target_energy);
        text_step = (TextView) findViewById(R.id.text_step);
        text_energy = (TextView) findViewById(R.id.text_energy);

        runable = false;
        circular_button.setIndeterminateProgressMode(true);


        // 设置目标
        target_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(StepMainActivity.this).setTitle("请设置步数目标")
                        .setIcon(R.drawable.logo_activity)
                        .setSingleChoiceItems(STEP, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(StepMainActivity.this, STEP[which], Toast.LENGTH_LONG).show();
                                if(which == 0)
                                    set_step = 50;
                                else if(which == 1)
                                    set_step = 4000;
                                else if(which == 2)
                                    set_step = 6000;
                                else if(which == 3)
                                    set_step = 8000;
                                else if(which == 4)
                                    set_step = 10000;
                                Log.i("step", "设置步数:" + set_step);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        // 设置能量目标
        target_energy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(StepMainActivity.this).setTitle("请设置卡路里目标")
                        .setIcon(R.drawable.logo_activity)
                        .setSingleChoiceItems(ENERGY, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(StepMainActivity.this, ENERGY[which], Toast.LENGTH_LONG).show();

                                if(which == 0)
                                    set_energy = 3;
                                else if(which == 1)
                                    set_energy = 600;
                                else if(which == 2)
                                    set_energy = 900;
                                else if(which == 3)
                                    set_energy = 1200;
                                else if(which == 4)
                                    set_energy = 1500;
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        // 传感器
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
        timer.schedule(display_task, 0, 100);

        circular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!runable) {
                    run();
                }
                else {
                    reset();
                }
            }
        });

    }
}
