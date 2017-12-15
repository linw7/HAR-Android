package com.example.lele.protoui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class UploadPicInfoActivity extends AppCompatActivity {

    private ImageView backBtn;
    private ImageView feel_love;
    private ImageView feel_like;
    private ImageView feel_soso;
    private ImageView feel_dislike;
    private ImageView stamp;
    private ImageView clothImage;
    private TextView text;
    private TextView barTitle;
    private TextView displayinfo;
    private int score;
    private int flag;

    private String user_name;
    private String[] attr_value;
    private String upload_pic_name;
    private String recommend_pic_name;

    private static String IMAGEPATH = "http://192.168.1.66:8080/fashion_server/getRecommendPic";
    private static String FEEDBACKPATH = "http://192.168.1.66:8080/fashion_server/getFeedbackLevel2";
    private static URL url;

    Map<String, String> FeedbackData = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pic_info);
        barTitle = findViewById(R.id.title_bar_name);
        barTitle.setText(R.string.barTitle_cloth);
        //返回--------------------------------------------------------------------------------------
        backBtn = findViewById(R.id.title_bar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取上一页面传递的信息
        Bundle bundle_fromRecommend = this.getIntent().getExtras();
        upload_pic_name = bundle_fromRecommend.getString("camera_pic");
        recommend_pic_name = bundle_fromRecommend.getString("recommend_pic_name");
        user_name = bundle_fromRecommend.getString("userName");
        attr_value = bundle_fromRecommend.getStringArray("clothingInfo");
        if(attr_value == null) {
            Intent i = new Intent(UploadPicInfoActivity.this, LoginActivity.class);
            startActivity(i);
        }

        //分析服装属性信息
        String display_str = "";
        HashMap<String, String> listClothInfo = new HashMap<String, String>();
        listClothInfo.put("length_short", attr_value[0]);
        listClothInfo.put("length_mid", attr_value[1]);
        listClothInfo.put("length_long", attr_value[2]);
        listClothInfo.put("sleeve_length_sleeveless", attr_value[3]);
        listClothInfo.put("sleeve_length_short", attr_value[4]);
        listClothInfo.put("sleeve_length_long", attr_value[5]);
        listClothInfo.put("collar_shape_stand", attr_value[6]);
        listClothInfo.put("collar_shape_V", attr_value[7]);
        listClothInfo.put("collar_shape_bateau", attr_value[8]);
        listClothInfo.put("collar_shape_round", attr_value[9]);
        listClothInfo.put("collar_shape_lapel", attr_value[10]);
        listClothInfo.put("collar_shape_high", attr_value[11]);
        listClothInfo.put("collar_shape_hoodie", attr_value[12]);
        listClothInfo.put("model_tight", attr_value[13]);
        listClothInfo.put("model_straight", attr_value[14]);
        listClothInfo.put("model_loose", attr_value[15]);
        listClothInfo.put("pattern_pure", attr_value[16]);
        listClothInfo.put("pattern_grid", attr_value[17]);
        listClothInfo.put("pattern_dot", attr_value[18]);
        listClothInfo.put("pattern_floral", attr_value[19]);
        listClothInfo.put("pattern_cross-stripe", attr_value[20]);
        listClothInfo.put("pattern_vertical-stripe", attr_value[21]);
        listClothInfo.put("pattern_number&letter", attr_value[22]);
        listClothInfo.put("pattern_repeat", attr_value[23]);
        listClothInfo.put("pants_length_short", attr_value[24]);
        listClothInfo.put("pants_length_mid", attr_value[25]);
        listClothInfo.put("pants_length_long", attr_value[26]);
        listClothInfo.put("pants_pattern_pure", attr_value[27]);
        listClothInfo.put("pants_pattern_grid", attr_value[28]);
        listClothInfo.put("pants_pattern_dot", attr_value[29]);
        listClothInfo.put("pants_pattern_floral", attr_value[30]);
        listClothInfo.put("pants_pattern_cross-stripe", attr_value[31]);
        listClothInfo.put("pants_pattern_vertical-stripe", attr_value[32]);
        listClothInfo.put("pants_pattern_number&letter", attr_value[33]);
        listClothInfo.put("pants_pattern_repeat", attr_value[34]);
        listClothInfo.put("pants_model_straight", attr_value[35]);
        listClothInfo.put("pants_model_tight", attr_value[36]);
        listClothInfo.put("pants_model_loose", attr_value[37]);
        listClothInfo.put("skirt_length_short", attr_value[38]);
        listClothInfo.put("skirt_length_mid", attr_value[39]);
        listClothInfo.put("skirt_length_long", attr_value[40]);
        listClothInfo.put("skirt_model_package-hip", attr_value[41]);
        listClothInfo.put("skirt_model_A-sharp", attr_value[42]);
        listClothInfo.put("skirt_pattern_pure", attr_value[43]);
        listClothInfo.put("skirt_pattern_grid", attr_value[44]);
        listClothInfo.put("skirt_pattern_dot", attr_value[45]);
        listClothInfo.put("skirt_pattern_floral", attr_value[46]);
        listClothInfo.put("skirt_pattern_cross-stripe", attr_value[47]);
        listClothInfo.put("skirt_pattern_vertical-stripe", attr_value[48]);
        listClothInfo.put("skirt_pattern_number&letter", attr_value[49]);
        listClothInfo.put("skirt_pattern_repeat", attr_value[50]);
        listClothInfo.put("season", attr_value[51]);
        listClothInfo.put("brand", attr_value[52]);

        display_str = display_str + "品牌：" + listClothInfo.get("brand") + "  ";
        display_str = display_str + "时装发布时间："+ listClothInfo.get("season") + "  ";

        if(listClothInfo.get("length_long").equals("1")) {
            display_str = display_str + "服装长度：较长  ";
        } else if(listClothInfo.get("length_short").equals("1")) {
            display_str = display_str + "服装长度：较短  ";
        } else {
            display_str = display_str + "服装长度：中等  ";
        }
        if(listClothInfo.get("sleeve_length_sleeveless").equals("1")) {
            display_str = display_str + "袖长：无袖  ";
        } else if(listClothInfo.get("sleeve_length_short").equals("1")) {
            display_str = display_str + "袖长：较短  ";
        } else {
            display_str = display_str + "袖长：较长  ";
        }
        if(listClothInfo.get("collar_shape_high").equals("1")) {
            display_str = display_str + "领型：高领  ";
        } else if(listClothInfo.get("collar_shape_lapel").equals("1")) {
            display_str = display_str + "领型：翻领  ";
        } else if(listClothInfo.get("collar_shape_round").equals("1")) {
            display_str = display_str + "领型：圆领  ";
        } else if(listClothInfo.get("collar_shape_bateau").equals("1")) {
            display_str = display_str + "领型：宽领  ";
        } else if(listClothInfo.get("collar_shape_hoodie").equals("1")) {
            display_str = display_str + "领型：连帽衫&无领  ";
        } else if(listClothInfo.get("collar_shape_V").equals("1")) {
            display_str = display_str + "领型：V领  ";
        } else {
            display_str = display_str + "领型：立领  ";
        }
        if(listClothInfo.get("model_loose").equals("1")) {
            display_str = display_str + "版型：宽松  ";
        } else if(listClothInfo.get("model_tight").equals("1")) {
            display_str = display_str + "版型：修身  ";
        } else {
            display_str = display_str + "版型：合身  ";
        }

        display_str = display_str + "印花：";
        if(listClothInfo.get("pattern_pure").equals("1")) {
            display_str = display_str + "纯色 ";
        }
        if(listClothInfo.get("pattern_grid").equals("1")) {
            display_str = display_str + "网格 ";
        }
        if(listClothInfo.get("pattern_dot").equals("1")) {
            display_str = display_str + "圆点 ";
        }
        if(listClothInfo.get("pattern_floral").equals("1")) {
            display_str = display_str + "花饰 ";
        }
        if(listClothInfo.get("pattern_cross-stripe").equals("1")) {
            display_str = display_str + "横条纹 ";
        }
        if(listClothInfo.get("pattern_vertical-stripe").equals("1")) {
            display_str = display_str + "竖条纹 ";
        }
        if(listClothInfo.get("pattern_number&letter").equals("1")) {
            display_str = display_str + "数字&字母 ";
        }
        if(listClothInfo.get("pattern_repeat").equals("1")) {
            display_str = display_str + "重复样式 ";
        }

        displayinfo = findViewById(R.id.textView8);
        displayinfo.setText(display_str);

        //显示对应服装图像
        clothImage = findViewById(R.id.clothImage);
        downloadphoto();

        //用户评分----------------------------------------------------------------------------------
        score = UserScore();
    }

    private void downloadphoto() {
        //开启线程，网络下载图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                //post方法向服务器传入用户名与索引，取对应的图像
                map.put("recommend_pic_name", recommend_pic_name);
                Message msg = Message.obtain();
                Bitmap[] bm = new Bitmap[1];

                try {
                    url = new URL(IMAGEPATH);
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                }

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
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(3000);
                    urlConnection.setRequestMethod("POST"); // 以post请求方式提交
                    urlConnection.setDoInput(true); // 读取数据
                    urlConnection.setDoOutput(true); // 向服务器写数据
                    // 获取上传信息的大小和长度
                    byte[] myData = stringBuilder.toString().getBytes();
                    // 设置请求体的类型是文本类型,表示当前提交的是文本数据
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestProperty("Content-Length", String.valueOf(myData.length));
                    // 获得输出流，向服务器输出内容
                    OutputStream outputStream = urlConnection.getOutputStream();
                    // 写入数据
                    outputStream.write(myData, 0, myData.length);
                    outputStream.close();
                    // 获得服务器响应结果和状态码
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        // 取回响应的结果
                        Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                        bm[0]=bitmap;
                        //clothImage.setImageBitmap(bitmap);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

                msg.what=202;
                msg.obj = bm;
                handler.sendMessage(msg);
            }
        }

        ).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //将下载的图片显示
            if(msg.what == 202){
                Bitmap[] bm=(Bitmap[])msg.obj;

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage",bm[0]);
                clothImage.setImageBitmap(bm[0]);

                //mSimpleAdapter.notifyDataSetChanged();
            }
            if (msg.what == 204) {
                String s = (String) msg.obj;
                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                if (s.contains("server: update database successfully")) {
                    Toast.makeText(UploadPicInfoActivity.this, s, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UploadPicInfoActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private int UserScore() {
        text = findViewById(R.id.textView4);
        feel_love = findViewById(R.id.feel_love);
        feel_like = findViewById(R.id.feel_like);
        feel_soso = findViewById(R.id.feel_soso);
        feel_dislike = findViewById(R.id.feel_dislike);
        stamp = findViewById(R.id.stamp);
        score = 0;
        //监听
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackLevel = "level3";
                switch (v.getId()) {
                    case R.id.feel_love:
                        score = 0;
                        text.setText("Love");
                        stamp.setVisibility(View.VISIBLE);
                        feel_love.setBackgroundResource(R.drawable.heart_2a2a2a);
                        if (flag == 1) {
                            score = -1;
                            text.setText("Unset");
                            feel_love.setBackgroundResource(R.drawable.heart2_2a2a2a);
                            stamp.setVisibility(View.INVISIBLE);
                            flag = 0;
                        } else flag = 1;
                        feel_like.setBackgroundResource(R.drawable.smile_2a2a2a);
                        feel_soso.setBackgroundResource(R.drawable.meh_2a2a2a);
                        feel_dislike.setBackgroundResource(R.drawable.frown_2a2a2a);
                        stamp.setBackgroundResource(R.drawable.stamp_love);
                        feedbackLevel = "level1";
                        break;
                    case R.id.feel_like:
                        score = 1;
                        text.setText("Like");
                        stamp.setVisibility(View.VISIBLE);
                        feel_like.setBackgroundResource(R.drawable.smile2_2a2a2a);
                        if (flag == 2) {
                            score = -1;
                            text.setText("Unset");
                            feel_like.setBackgroundResource(R.drawable.smile_2a2a2a);
                            stamp.setVisibility(View.INVISIBLE);
                            flag = 0;
                        } else flag = 2;
                        feel_love.setBackgroundResource(R.drawable.heart2_2a2a2a);
                        feel_soso.setBackgroundResource(R.drawable.meh_2a2a2a);
                        feel_dislike.setBackgroundResource(R.drawable.frown_2a2a2a);
                        stamp.setBackgroundResource(R.drawable.stamp_like);
                        feedbackLevel = "level2";
                        break;
                    case R.id.feel_soso:
                        score = 2;
                        text.setText("Soso");
                        stamp.setVisibility(View.VISIBLE);
                        feel_soso.setBackgroundResource(R.drawable.meh2_2a2a2a);
                        if (flag == 3) {
                            score = -1;
                            text.setText("Unset");
                            feel_soso.setBackgroundResource(R.drawable.meh_2a2a2a);
                            stamp.setVisibility(View.INVISIBLE);
                            flag = 0;
                        } else flag = 3;
                        feel_love.setBackgroundResource(R.drawable.heart2_2a2a2a);
                        feel_like.setBackgroundResource(R.drawable.smile_2a2a2a);
                        feel_dislike.setBackgroundResource(R.drawable.frown_2a2a2a);
                        stamp.setBackgroundResource(R.drawable.stamp_soso);
                        feedbackLevel = "level3";
                        break;
                    case R.id.feel_dislike:
                        score = 3;
                        text.setText("Dislike");
                        feel_dislike.setBackgroundResource(R.drawable.frown2_2a2a2a);
                        stamp.setVisibility(View.VISIBLE);
                        if (flag == 4) {
                            score = -1;
                            text.setText("Unset");
                            feel_dislike.setBackgroundResource(R.drawable.frown_2a2a2a);
                            stamp.setVisibility(View.INVISIBLE);
                            flag = 0;
                        } else flag = 4;
                        feel_love.setBackgroundResource(R.drawable.heart2_2a2a2a);
                        feel_like.setBackgroundResource(R.drawable.smile_2a2a2a);
                        feel_soso.setBackgroundResource(R.drawable.meh_2a2a2a);
                        stamp.setBackgroundResource(R.drawable.stamp_dislike);
                        feedbackLevel = "level4";
                        break;
                }
                //上传反馈信息
                FeedbackData.put("username", user_name);
                FeedbackData.put("picname",recommend_pic_name);
                FeedbackData.put("feedbackLevel", feedbackLevel);
                new feedback2().start();
                //finish();
            }
        };
        feel_love.setOnClickListener(listener);
        feel_like.setOnClickListener(listener);
        feel_soso.setOnClickListener(listener);
        feel_dislike.setOnClickListener(listener);
        //返回分数
        return score;
    }

    private class feedback2 extends Thread {
        public void run() {
            try {
                url = new URL(FEEDBACKPATH);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : FeedbackData.entrySet()) {
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
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("POST"); // 以post请求方式提交
                urlConnection.setDoInput(true); // 读取数据
                urlConnection.setDoOutput(true); // 向服务器写数据
                // 获取上传信息的大小和长度
                byte[] myData = stringBuilder.toString().getBytes();
                // 设置请求体的类型是文本类型,表示当前提交的是文本数据
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length",
                        String.valueOf(myData.length));
                // 获得输出流，向服务器输出内容
                OutputStream outputStream = urlConnection.getOutputStream();
                // 写入数据
                outputStream.write(myData, 0, myData.length);
                outputStream.close();
                // 获得服务器响应结果和状态码
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    // 取回响应的结果
                    Message msg = Message.obtain();
                    msg.what = 204;
                    msg.obj = changeInputStream(urlConnection.getInputStream(), "UTF-8");
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
