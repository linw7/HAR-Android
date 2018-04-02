package com.example.lele.protoui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //调试---------------
    private int flag = 1;

    //-------------------
    private TextView loginbtn;
    private TextView registerbtn;
    private TextView visitbtn;
    private EditText editTextName;
    private EditText editTextPwd;
    private String uname;
    private String password;
    Map<String, String> loginData = new HashMap<String, String>();
    private static String PATH = "http://192.168.1.66:8080/fashion_server/login";
    private static URL url;
    private ProgressDialog dialog;//登陆处理
    private SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //输入检查
            if (msg.what == 197) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_msg_nouser) + "," + getString(R.string.login_msg_nopwd), Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 198) {
                Toast.makeText(LoginActivity.this, R.string.login_msg_nouser, Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 199) {
                Toast.makeText(LoginActivity.this, R.string.login_msg_nopwd, Toast.LENGTH_SHORT).show();
            }
            //超时
            if (msg.what == 200) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(LoginActivity.this, R.string.login_msg_timeout, Toast.LENGTH_LONG).show();
            }
            //完成服务器响应
            if (msg.what == 201) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                String s = (String) msg.obj;
                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                if (s.contains("log in")) {
                    Bundle bundle_toMain = new Bundle();
                    bundle_toMain.putString("userName",uname);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtras(bundle_toMain);
                    startActivity(i);
                } else {
                    //密码错误
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void no_name_or_pwd(){
        new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请确保已填写账号和密码！")
                .show();
    }

    private void wrong_name_or_pwd(){
        new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("您的账号或密码不正确！")
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        // 隐藏状态栏
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);
        editTextName = findViewById(R.id.login_user_name);
        editTextPwd = findViewById(R.id.login_user_pwd);
        visitbtn = findViewById(R.id.visitbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTextName.getText()) || TextUtils.isEmpty(editTextPwd.getText())){
                    no_name_or_pwd();
                } else{
                    if((editTextName.getText().toString().equals("16214237")) && (editTextPwd.getText().toString().equals("Lwnetid4237"))) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else {
                        wrong_name_or_pwd();
                    }
                    // if (loginCheck())//登陆字串检查
                    //{
                    //    dialog = ProgressDialog.show(LoginActivity.this, getString(R.string.login_msg_dialog_title), getString(R.string.login_msg_dialog_content)); //弹出ProgressDialog
                    //    new login().start();//开始后台登陆
                    //}
                }
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        visitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, TestActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean loginCheck() {
        if (TextUtils.isEmpty(editTextName.getText()) && TextUtils.isEmpty(editTextPwd.getText())) {
            // 如果用户名或密码为空，发送消息197
            Message msg = Message.obtain();
            msg.what = 197;
            handler.sendMessage(msg);
        } else if (TextUtils.isEmpty(editTextName.getText()) && ! TextUtils.isEmpty(editTextPwd.getText())) {
            // 如果用户名为空，密码不为空，发送消息198
            Message msg = Message.obtain();
            msg.what = 198;
            handler.sendMessage(msg);
        } else if (!TextUtils.isEmpty(editTextName.getText()) && TextUtils.isEmpty(editTextPwd.getText())) {
            // 如果用户名不为空，密码为空，发送消息199
            Message msg = Message.obtain();
            msg.what = 199;
            handler.sendMessage(msg);
        } else {
            // 都不为空，从组件中获取用户名及密码字符串
            uname = editTextName.getText().toString();
            password = editTextPwd.getText().toString();
            loginData.put("username", uname);
            loginData.put("password", password);
            // 将logindata当做参数传给savePreferences_login
            sharedPrefsUtil.savePreferences_login(LoginActivity.this, "DataBase", loginData);
            return true;
        }
        return false;
    }

    private class login extends Thread {
        // 创建登录线程
        public void run() {
            try {
                // 服务器端登录url设置为：192.168.1.66:8080/fashion_server/login
                url = new URL(PATH);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // 处理待上传服务器数据stringBuilder
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : loginData.entrySet()) {
                try {
                    stringBuilder
                            .append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            try {
                // 对url建立HTTP连接，得到urlConnection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // 设置连接时长及数据提交方式
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                // 把待提交的数据转化为字节数组
                byte[] myData = stringBuilder.toString().getBytes();
                // 设置请求体的类型是文本类型,表示当前提交的是文本数据
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length",
                        String.valueOf(myData.length));
                // 获得输出流，向服务器写入内容，字节数组
                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(myData, 0, myData.length);
                outputStream.close();

                // 获得服务器响应结果和状态码
                int responseCode = urlConnection.getResponseCode();
                // 正常返回
                if (responseCode == 200) {
                    // 取回响应的结果，通过message对象获得返回的数据
                    Message msg = Message.obtain();
                    msg.what = 201;
                    // 通过changeInputStream获得"UTF-8"编码类型的服务器输出
                    msg.obj = changeInputStream(urlConnection.getInputStream(), "UTF-8");
                    // 发送消息
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                //跳线
                if (flag == 1) {
                    Message msg = Message.obtain();
                    msg.what = 201;
                    msg.obj = "log in";
                    handler.sendMessage(msg);
                }
                //超时处理
                Message msg = Message.obtain();
                msg.what = 200;
                handler.sendMessage(msg);
            }
        }
    }

    // 从服务器获取流数据
    private static String changeInputStream(InputStream inputStream, String encode) {
        // 内存流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = null;
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);
                }
                result = new String(byteArrayOutputStream.toByteArray(), encode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
