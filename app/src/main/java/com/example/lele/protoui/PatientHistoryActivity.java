package com.example.lele.protoui;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Map;

public class PatientHistoryActivity extends AppCompatActivity {
    private CalendarView calendar;
    private ImageView more;
    private TextView detail_title;
    private ImageView btn;
    private ImageView left, right;
    private ProgressBar progress;
    private TextView progress_text;

    public static int c_year, c_month, c_day;
    public DemoData demodata = new DemoData();

    private String trans_time(int second){
        String show_time = new String();
        if(second < 60)
            show_time = second + "秒";
        else if(second == 60)
            show_time = "1分钟";
        else if(second > 60)
            show_time = (second/60) + "分" + (second/60) + "秒";
        return show_time;
    }

    private void show_data() {
        left.setImageResource(R.drawable.left);
        right.setImageResource(R.drawable.right);

        OfflineFileRW raw = new OfflineFileRW();
        String date = c_month + "." + c_day;
        Map<String, String> result = raw.get(PatientHistoryActivity.this, "history.txt", date);

        if((Integer.valueOf(result.get("sit")).intValue() == 0) && (Integer.valueOf(result.get("stand")).intValue() == 0) && (Integer.valueOf(result.get("upstairs")).intValue() == 0)
                && (Integer.valueOf(result.get("downstairs")).intValue() == 0) && (Integer.valueOf(result.get("walk")).intValue() == 0) && (Integer.valueOf(result.get("jog")).intValue() == 0)){

            reset();
        } else {
            more.setImageResource(R.drawable.more_blue);
            left.setImageResource(R.drawable.left_blue);
            right.setImageResource(R.drawable.right_blue);
        }
    }

    private void reset(){
        c_year = 0;
        c_month = 0;
        c_day = 0;
    }

    private void alter(){
        String date = c_year + "年" + c_month + "月" + c_day + "日";
        String msg = "您选中的时间为：" + date;
        new AlertDialog.Builder(PatientHistoryActivity.this)
                .setTitle("确认设置")
                .setIcon(R.drawable.date)
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
        left.setImageResource(R.drawable.left_w);
        right.setImageResource(R.drawable.right_w);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);

        calendar = (CalendarView) findViewById(R.id.calendarView);
        more = (ImageView) findViewById(R.id.more);
        detail_title = (TextView)findViewById(R.id.detial_title);
        left = (ImageView)findViewById(R.id.left);
        right = (ImageView)findViewById(R.id.right);
        btn = (ImageView)findViewById(R.id.btn);
        progress = (ProgressBar)findViewById(R.id.progressSelf);
        progress_text = (TextView)findViewById(R.id.tvProgress);

        c_year = c_month = c_day = 0;
        detail_title.setText(DoctorActivity.name);
        more.setImageResource(R.drawable.more_b);

        progress.setProgress(60);
        progress_text.setText(" " + 60 + "%");

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset_tip();

                if(c_day != 0) {
                    Intent i = new Intent(PatientHistoryActivity.this, MainChartActivity.class);
                    startActivity(i);
                }
                if(c_day == 0){
                    Toast.makeText(PatientHistoryActivity.this, "该日无运动数据！", Toast.LENGTH_LONG).show();
                    reset();
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PatientHistoryActivity.this, DoctorActivity.class);
                startActivity(i);
            }
        });

    }
}
