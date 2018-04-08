package com.example.lele.protoui;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import static android.os.SystemClock.sleep;


public class DoctorActivity extends AppCompatActivity {
    private ImageView more_1, more_2, more_3;
    private ImageView rank_1, rank_2, rank_3;
    private TextView name_1, name_2, name_3;
    private TextView remind_text;
    private ImageView search, btn;
    private EditText text;

    private boolean moreable[] = {false, false, false};

    private void alter(int i){
        new AlertDialog.Builder(DoctorActivity.this)
                .setTitle("查看详情")
                .setIcon(R.drawable.more_info)
                .setMessage("请确认查看该患者运动详情")
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(DoctorActivity.this, PatientHistoryActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(i == 1)
                            more_1.setImageResource(R.drawable.more_b);
                        else if(i == 2)
                            more_2.setImageResource(R.drawable.more_b);
                        else if(i == 3)
                            more_3.setImageResource(R.drawable.more_b);
                    }
                })
                .show();
    }

    private void do_search(String input){
        new AlertDialog.Builder(DoctorActivity.this)
                .setTitle("快搜")
                .setIcon(R.drawable.more_info)
                .setMessage("请确认搜索以下用户：" + "\n" + input)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remind_text.setText("快搜结果");

                        rank_1.setImageResource(R.drawable.ch_1);
                        rank_2.setImageResource(R.drawable.ch_2);
                        rank_3.setImageResource(R.drawable.ch_3);
                        more_1.setImageResource(R.drawable.more_b);
                        more_2.setImageResource(R.drawable.more_b);
                        more_3.setImageResource(R.drawable.more_b);
                        name_1.setText("关羽");
                        name_2.setText("刘备");
                        name_3.setText("张飞");

                        moreable[0] = true;
                        moreable[1] = true;
                        moreable[2] = true;

                        search.setImageResource(R.drawable.search_b);
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        search.setImageResource(R.drawable.search_b);
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        more_1 = (ImageView) findViewById(R.id.more_1);
        more_2 = (ImageView) findViewById(R.id.more_2);
        more_3 = (ImageView) findViewById(R.id.more_3);
        rank_1 = (ImageView) findViewById(R.id.rank_1);
        rank_2 = (ImageView) findViewById(R.id.rank_2);
        rank_3 = (ImageView) findViewById(R.id.rank_3);
        name_1 = (TextView) findViewById(R.id.name_1);
        name_2 = (TextView) findViewById(R.id.name_2);
        name_3 = (TextView) findViewById(R.id.name_3);
        search = (ImageView) findViewById(R.id.search);
        text = (EditText) findViewById(R.id.id_search);
        btn = (ImageView) findViewById(R.id.btn);
        remind_text = (TextView) findViewById(R.id.text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setImageResource(R.drawable.search_blue);
                String input = new String();
                input = text.getText().toString();
                do_search(input);
            }
        });

        more_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moreable[0] == true){
                    more_1.setImageResource(R.drawable.more_blue);
                    alter(1);
                }
            }
        });

        more_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moreable[1] == true) {
                    more_2.setImageResource(R.drawable.more_blue);
                    alter(2);
                }
            }
        });

        more_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moreable[2] == true) {
                    more_3.setImageResource(R.drawable.more_blue);
                    alter(3);
                }
            }
        });

    }
}
