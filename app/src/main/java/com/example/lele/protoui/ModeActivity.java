package com.example.lele.protoui;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ModeActivity extends AppCompatActivity {

    private ImageView phone_mode;
    private ImageView watch_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);

        phone_mode = findViewById(R.id.phone_mode);
        watch_mode = findViewById(R.id.watch_mode);

        phone_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_phone_mode();
            }
        });

        watch_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter_watch_mode();
            }
        });
    }

    private void alter_phone_mode() {
        String msg = new String();
        String now_mode = "当前模式为：" + "手机模式" + "\n";
        msg = now_mode + "是否切换？";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_phone_mode)
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }

    private void alter_watch_mode() {
        String msg = new String();
        String now_mode = "请确认蓝牙是否已打开！" + "\n";
        msg = now_mode + "开始匹配手环？";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_watch_mode)
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }
}
