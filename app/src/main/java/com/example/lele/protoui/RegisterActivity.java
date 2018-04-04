package com.example.lele.protoui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView barTitle;
    private ImageView backBtn;
    private TextView nextBtn;
    private TextView giveupBtn;
    private TextView user_name;
    private TextView pwd;
    private TextView pwd_confirm;
    private String uname;
    private String password;
    private String password_confirm;
    Map<String, String> registerData = new HashMap<String, String>();
    private SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //输入检查
            if (msg.what == 195) {
                Toast.makeText(RegisterActivity.this, R.string.register_msg_psd_unconfirm, Toast.LENGTH_LONG).show();
            }
            if (msg.what == 196) {
                Toast.makeText(RegisterActivity.this, R.string.register_msg_psd_confirm, Toast.LENGTH_LONG).show();
            }
            if (msg.what == 197) {
                Toast.makeText(RegisterActivity.this, getString(R.string.login_msg_nouser) + "," + getString(R.string.login_msg_nopwd), Toast.LENGTH_LONG).show();
            }
            if (msg.what == 198) {
                Toast.makeText(RegisterActivity.this, R.string.login_msg_nouser, Toast.LENGTH_LONG).show();
            }
            if (msg.what == 199) {
                Toast.makeText(RegisterActivity.this, R.string.login_msg_nopwd, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_name = (TextView) findViewById(R.id.register_user_name);
        pwd = (TextView) findViewById(R.id.register_user_pwd);
        pwd_confirm = (TextView) findViewById(R.id.register_user_pwd_confirm);
        barTitle = (TextView) findViewById(R.id.title_bar_name);
        barTitle.setText(R.string.reg_bar_title);
        backBtn = (ImageView) findViewById(R.id.title_bar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextBtn = (TextView) findViewById(R.id.reg_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击注册
                register();
            }
        });

        giveupBtn = (TextView) findViewById(R.id.reg_giveup_btn);
        giveupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(R.string.reg_alertdialog_title);
                builder.setMessage(R.string.reg_alertdialog_content);
                builder.setPositiveButton(R.string.AlertDialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.AlertDialog_no, null);
                builder.show();
            }
        });
    }

    private void register() {
        if (registerCheck()) {
            //上传用户名、密码至服务器步
            Intent i = new Intent(RegisterActivity.this, InitiateActivity.class);
            startActivity(i);
        }
    }

    private boolean registerCheck() {
        if (TextUtils.isEmpty(user_name.getText()) && TextUtils.isEmpty(pwd.getText())) {
            Message msg = Message.obtain();
            msg.what = 197;
            handler.sendMessage(msg);
        } else if (TextUtils.isEmpty(user_name.getText()) && TextUtils.isEmpty(pwd.getText()) != true) {
            Message msg = Message.obtain();
            msg.what = 198;
            handler.sendMessage(msg);
        } else if (TextUtils.isEmpty(user_name.getText()) != true && TextUtils.isEmpty(pwd.getText())) {
            Message msg = Message.obtain();
            msg.what = 199;
            handler.sendMessage(msg);
        } else if (!TextUtils.isEmpty(pwd.getText()) && TextUtils.isEmpty(pwd_confirm.getText())) {
            Message msg = Message.obtain();
            msg.what = 196;
            handler.sendMessage(msg);
        } else if (!TextUtils.isEmpty(pwd.getText()) && !TextUtils.isEmpty(pwd_confirm.getText())) {
            uname = user_name.getText().toString();
            password = pwd.getText().toString();
            password_confirm = pwd_confirm.getText().toString();
            if (!password_confirm.equals(password)) {
                Message msg = Message.obtain();
                msg.what = 195;
                handler.sendMessage(msg);
            } else {
                registerData.put("username", uname);
                registerData.put("password", password);
                sharedPrefsUtil.savePreferences_login(RegisterActivity.this, "DataBase", registerData);
                return true;
            }
        }
        return false;
    }
}
