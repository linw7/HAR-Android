package com.example.lele.protoui;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

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

    private void connect_phone_open(){
        try {
            sleep(500);
            new AlertDialog.Builder(this)
                    .setTitle("运行模式修改成功！")
                    .setMessage("您已切换到手机模式！")
                    .show();
        }catch (InterruptedException e) {
            ;
        }
    }

    private void alter_phone_mode() {
        String msg = new String();
        String now_mode = "当前模式为：" + "手机模式" + "\n";
        msg = now_mode + "是否切换？";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_phone_mode)
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connect_phone_open();
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }

    private void connect_watch_open(){
        try {
            sleep(1500);
            new AlertDialog.Builder(this)
                    .setTitle("运行模式修改成功！")
                    .setMessage("您已切换到手环模式！")
                    .show();
        }catch (InterruptedException e) {
            ;
        }
    }

    private void connect_watch_close(){
        try {
            sleep(500);
            new AlertDialog.Builder(this)
                    .setTitle("连接失败！")
                    .setMessage("您的蓝牙尚未开启！")
                    .show();
        }catch (InterruptedException e) {
            ;
        }
    }

    private void alter_watch_mode() {
        String msg = new String();
        String now_mode = "请确认蓝牙是否已打开！" + "\n";
        msg = now_mode + "开始匹配手环？";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_watch_mode)
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                        if (adapter.isEnabled()) {
                            Log.i("BT", "已打开！");
                            connect_watch_open();
                        } else {
                            Log.i("BT", "未打开！");
                            connect_watch_close();
                        }
                    }
                })
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }
}
