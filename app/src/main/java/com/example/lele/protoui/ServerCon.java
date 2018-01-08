package com.example.lele.protoui;

import android.os.Handler;
import android.os.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by TeeKee on 2018/1/8.
 */

public class ServerCon {
    private String PUSH = "http://192.168.1.66:8080/HAR/push_file";
    private String PULL = "http://192.168.1.66:8080/HAR/pull_result";
    private String ADJUST = "http://192.168.1.66:8080/HAR/adjust";
    private String ANALYSIS = "http://192.168.1.66:8080/HAR/analysis";
    private String LOGIN = "http://192.168.1.66:8080/HAR/user_login";

    public void user_login() {

    }

    public void push_file(Map<String, String> map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = get_url(PUSH);
                byte[] myData = trans_data(map);
                try {
                    HttpURLConnection urlConnection = create_connect(myData, url);
                    send_msg(urlConnection, myData);
                    if (urlConnection.getResponseCode() == 200) {
                        get_msg(urlConnection);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void analysis() {

    }

    public void adjust() {

    }

    public void pull_result(Map<String, String> map){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = get_url(PULL);
                byte[] myData = trans_data(map);
                try {
                    HttpURLConnection urlConnection = create_connect(myData, url);
                    send_msg(urlConnection, myData);
                    if (urlConnection.getResponseCode() == 200) {
                        get_msg(urlConnection);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private URL get_url(String PATH) {
        try {
            URL url = new URL(PATH);
            return url;
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] trans_data(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
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
        return  stringBuilder.toString().getBytes();
    }

    private HttpURLConnection create_connect(byte[] myData, URL url) {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(myData.length));
            return urlConnection;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void send_msg(HttpURLConnection urlConnection, byte[] myData) {
        try {
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(myData, 0, myData.length);
            outputStream.close();}catch (IOException e){
            e.printStackTrace();
        }
    }

    private void get_msg(HttpURLConnection urlConnection) {
        try {
            Message msg = Message.obtain();
            msg.what = 202;
            msg.obj = changeInputStream(urlConnection.getInputStream(), "UTF-8");
            handler.sendMessage(msg);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 202){
                String response = (String) msg.obj;
                // Do it !
            }
        }
    };

    private static String changeInputStream(InputStream inputStream, String encode) {
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
