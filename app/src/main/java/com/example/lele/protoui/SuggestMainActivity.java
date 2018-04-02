package com.example.lele.protoui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class SuggestMainActivity extends AppCompatActivity {
    RatingBar rating_bar;
    TextView suggest;
    EditText commit_text;
    Button commit_button;
    private CalendarView calendar;
    private int c_year, c_month, c_day;
    public DemoData demodata;

    private void alter_submit() {
        String msg = "请确认反馈建议";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_submit)
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }

    private void reset(){
        c_year = 0;
        c_month = 0;
        c_day = 0;
    }

    private void show_suggest(){
        suggest.setText("今日尚未填写医嘱！");
    }

    private void alter(){
        String date = c_year + "年" + c_month + "月" + c_day + "日";
        String msg = "您选中的时间为：" + "\n" + date;
        new AlertDialog.Builder(SuggestMainActivity.this)
                .setTitle("确认设置")
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        show_suggest();
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
        setContentView(R.layout.activity_suggest_main);

        calendar = (CalendarView) findViewById(R.id.calendar);
        rating_bar = (RatingBar)findViewById(R.id.rating_bar);
        suggest = (TextView)findViewById(R.id.suggest);
        commit_text = (EditText)findViewById(R.id.commit_text);
        commit_button = (Button)findViewById(R.id.commit_button);

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest.setText("今日活动量过大");
            }
        });

        commit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit_text.getText();
            }
        });

        commit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_submit();
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
