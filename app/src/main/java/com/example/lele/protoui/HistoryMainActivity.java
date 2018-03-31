package com.example.lele.protoui;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import java.util.ArrayList;

public class HistoryMainActivity extends AppCompatActivity {
    private CalendarView calendar;
    private CircularProgressButton button;
    private TextView sit_time;
    private TextView stand_time;
    private TextView upstairs_time;
    private TextView downstairs_time;
    private TextView walk_time;
    private TextView jog_time;
    private TextView detail_title;

    private ImageView left, right;
    private ImageView sit, stand, upstairs, downstairs, walk, jog;

    private int c_year, c_month, c_day;

    private void show_detail(){
        left.setImageResource(R.drawable.left);
        right.setImageResource(R.drawable.right);
        String str = c_year + "." + c_month + "." + c_day ;
        detail_title.setText(str);
    }

    private void show_data() {
        sit.setImageResource(R.drawable.sit_r);
        stand.setImageResource(R.drawable.stand_r);
        upstairs.setImageResource(R.drawable.upstairs_r);
        downstairs.setImageResource(R.drawable.downstairs_r);
        walk.setImageResource(R.drawable.walk_r);
        jog.setImageResource(R.drawable.jog_r);

        sit_time.setText("5分钟");
        stand_time.setText("1分钟");
        upstairs_time.setText("6分钟");
        downstairs_time.setText("3分钟");
        walk_time.setText("5分钟");
        jog_time.setText("3分钟");
    }

    private void reset(){
        c_year = 0;
        c_month = 0;
        c_day = 0;
    }

    private void alter(){
        String date = c_year + "年" + c_month + "月" + c_day + "日";
        String msg = "您选中的时间为：" + "\n" + date;
        new AlertDialog.Builder(HistoryMainActivity.this)
                .setTitle("确认设置")
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        show_detail();
                        show_data();
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        button = (CircularProgressButton)findViewById(R.id.button);
        sit_time = (TextView)findViewById(R.id.sit_time);
        stand_time = (TextView)findViewById(R.id.stand_time);
        upstairs_time = (TextView)findViewById(R.id.upstairs_time);
        downstairs_time = (TextView)findViewById(R.id.downstairs_time);
        walk_time = (TextView)findViewById(R.id.walk_time);
        jog_time = (TextView)findViewById(R.id.jog_time);
        detail_title = (TextView)findViewById(R.id.detial_title);
        left = (ImageView)findViewById(R.id.left);
        right = (ImageView)findViewById(R.id.right);
        sit = (ImageView)findViewById(R.id.sit);
        stand = (ImageView)findViewById(R.id.stand);
        upstairs = (ImageView)findViewById(R.id.upstairs);
        downstairs = (ImageView)findViewById(R.id.downstairs);
        walk = (ImageView)findViewById(R.id.walk);
        jog = (ImageView)findViewById(R.id.jog);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistoryMainActivity.this, MainChartActivity.class);
                startActivity(i);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                c_year = year;
                c_month = month;
                c_day = dayOfMonth;
                alter();
            }
        });

    }
}
