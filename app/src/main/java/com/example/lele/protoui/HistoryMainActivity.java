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
    private ImageView more;
    private TextView sit_time;
    private TextView stand_time;
    private TextView upstairs_time;
    private TextView downstairs_time;
    private TextView walk_time;
    private TextView jog_time;
    private TextView detail_title;

    private ImageView left, right;
    private ImageView sit, stand, upstairs, downstairs, walk, jog;

    public static int c_year, c_month, c_day;
    public DemoData demodata = new DemoData();

    private void show_data() {
        left.setImageResource(R.drawable.left);
        right.setImageResource(R.drawable.right);

        demodata.set_data();
        ArrayList<ArrayList> data = demodata.get_elem(c_day);
        ArrayList today = data.get(2);

        if(c_month == 3){
            String str = c_year + "." + c_month + "." + c_day ;
            detail_title.setText(str);

            sit.setImageResource(R.drawable.sit_r);
            stand.setImageResource(R.drawable.stand_r);
            upstairs.setImageResource(R.drawable.upstairs_r);
            downstairs.setImageResource(R.drawable.downstairs_r);
            walk.setImageResource(R.drawable.walk_r);
            jog.setImageResource(R.drawable.jog_r);

            sit_time.setText(today.get(0) + "分钟");
            stand_time.setText(today.get(1) + "分钟");
            upstairs_time.setText(today.get(2) + "分钟");
            downstairs_time.setText(today.get(3) + "分钟");
            walk_time.setText(today.get(4) + "分钟");
            jog_time.setText(today.get(5) + "分钟");
            more.setImageResource(R.drawable.more_blue);
            left.setImageResource(R.drawable.left_blue);
            right.setImageResource(R.drawable.right_blue);
        }

        if((c_month == 4) && (c_day <= 2)){
            String str = c_year + "." + c_month + "." + c_day ;
            detail_title.setText(str);

            sit.setImageResource(R.drawable.sit_r);
            stand.setImageResource(R.drawable.stand_r);
            upstairs.setImageResource(R.drawable.upstairs_r);
            downstairs.setImageResource(R.drawable.downstairs_r);
            walk.setImageResource(R.drawable.walk_r);
            jog.setImageResource(R.drawable.jog_r);

            sit_time.setText(today.get(0) + "分钟");
            stand_time.setText(today.get(1) + "分钟");
            upstairs_time.setText(today.get(2) + "分钟");
            downstairs_time.setText(today.get(3) + "分钟");
            walk_time.setText(today.get(4) + "分钟");
            jog_time.setText(today.get(5) + "分钟");
            more.setImageResource(R.drawable.more_blue);
            left.setImageResource(R.drawable.left_blue);
            right.setImageResource(R.drawable.right_blue);
        }

        if((c_month == 4) && (c_day > 2)){
            detail_title.setText("暂无数据");
            sit_time.setText("-");
            stand_time.setText("-");
            upstairs_time.setText("-");
            downstairs_time.setText("-");
            walk_time.setText("-");
            jog_time.setText("-");
            more.setImageResource(R.drawable.more_r);
            sit.setImageResource(R.drawable.sit_u);
            stand.setImageResource(R.drawable.stand_u);
            upstairs.setImageResource(R.drawable.upstairs_u);
            downstairs.setImageResource(R.drawable.downstairs_u);
            walk.setImageResource(R.drawable.walk_u);
            jog.setImageResource(R.drawable.jog_u);
            left.setImageResource(R.drawable.left_r);
            right.setImageResource(R.drawable.right_r);
            reset();
        }

        if((c_month != 3) && (c_month != 4)){
            detail_title.setText("暂无数据");
            sit_time.setText("-");
            stand_time.setText("-");
            upstairs_time.setText("-");
            downstairs_time.setText("-");
            walk_time.setText("-");
            jog_time.setText("-");
            more.setImageResource(R.drawable.more_r);
            sit.setImageResource(R.drawable.sit_u);
            stand.setImageResource(R.drawable.stand_u);
            upstairs.setImageResource(R.drawable.upstairs_u);
            downstairs.setImageResource(R.drawable.downstairs_u);
            walk.setImageResource(R.drawable.walk_u);
            jog.setImageResource(R.drawable.jog_u);
            left.setImageResource(R.drawable.left_r);
            right.setImageResource(R.drawable.right_r);
            reset();
        }
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

    private void reset_tip(){
        more.setImageResource(R.drawable.more_b);
        detail_title.setText("");
        left.setImageResource(R.drawable.left_w);
        right.setImageResource(R.drawable.right_w);

        sit.setImageResource(R.drawable.sit_u);
        stand.setImageResource(R.drawable.stand_u);
        upstairs.setImageResource(R.drawable.upstairs_u);
        downstairs.setImageResource(R.drawable.downstairs_u);
        walk.setImageResource(R.drawable.walk_u);
        jog.setImageResource(R.drawable.jog_u);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        more = (ImageView) findViewById(R.id.more);
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

        c_year = c_month = c_day = 0;

        more.setImageResource(R.drawable.more_b);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset_tip();
                if(c_day != 0) {
                    if(c_month == 3) {
                        Intent i = new Intent(HistoryMainActivity.this, MainChartActivity.class);
                        startActivity(i);
                    }
                    if((c_month == 4) && (c_day <= 2)){
                        Intent i = new Intent(HistoryMainActivity.this, MainChartActivity.class);
                        startActivity(i);
                    }
                    if((c_month == 4) && (c_day > 3)){
                        new AlertDialog.Builder(HistoryMainActivity.this)
                                .setTitle("提示")
                                .setMessage("暂未更新数据！")
                                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reset();
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
                    if((c_month != 3) && (c_month != 4)){
                        new AlertDialog.Builder(HistoryMainActivity.this)
                                .setTitle("提示")
                                .setMessage("暂未更新数据！")
                                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reset();
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
                }
                if(c_day == 0){
                    new AlertDialog.Builder(HistoryMainActivity.this)
                            .setTitle("确认设置")
                            .setMessage("请选择有效日期！")
                            .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reset();
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
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                c_year = year;
                c_month = month + 1;
                c_day = dayOfMonth;
                alter();
            }
        });

    }
}
