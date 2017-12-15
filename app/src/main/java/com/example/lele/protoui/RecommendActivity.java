package com.example.lele.protoui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecommendActivity extends AppCompatActivity {

    private TextView barTitle;
    private ImageView backBtn;
    private ImageView upLoadfromCamera;
    private ImageView upLoadfromAlbum;
    private ImageView tryagain;
    private ImageView confirm;
    private TextView recmd_txt1;
    private TextView recmd_txt2;
    private TextView recmd_txt_tryagain;
    private TextView recmd_txt_confirm;

    //设置浏览本地相册用的变量
    private ImageView mImageView;
    //设置拍照用的变量
    private String mPhotoPath;
    private String recommend_pic_name;
    private String user_name;
    private String[] attr_value;
    private File mPhotoFile;
    public final static int CAMERA_RESULT = 1;
    private Camera camera;
    private Camera.Parameters parameters = null;
    private SurfaceView surfaceView;/* 显示拍摄界面 */
    Bundle bundle = null; /* 声明一个Bundle对象，用来存储数据 */
    //上传图片
    private static String requestURL_getpic = "http://192.168.1.66:8080/fashion_server/getpic";
    private static String RECOMMEND_INFOPATH = "http://192.168.1.66:8080/fashion_server/re_returnClothInfo";
    private static URL url;
    String pic_uri;/* 图片uri */

    Bundle bundle_toUploadPicInfo = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        barTitle = findViewById(R.id.title_bar_name);
        barTitle.setText(R.string.barTitle_recmd);
        recmd_txt1 = findViewById(R.id.recmd_txt1);
        recmd_txt2 = findViewById(R.id.recmd_txt2);
        recmd_txt_tryagain = findViewById(R.id.recmd_txt_tryagain);
        recmd_txt_confirm = findViewById(R.id.recmd_txt_confirm);
        tryagain = findViewById(R.id.recmd_tryagain);
        confirm = findViewById(R.id.recmd_confirm);

        //返回--------------------------------------------------------------------------------------
        backBtn = findViewById(R.id.title_bar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upLoadfromCamera = findViewById(R.id.recmd_upload1_btn);
        upLoadfromAlbum = findViewById(R.id.recmd_upload2_btn);
        tryagain = findViewById(R.id.recmd_tryagain);
        mImageView = findViewById(R.id.mImageView);
        setCameraListener();
        setAlbumListener();

        //拍摄界面----------------------------------------------------------------------------------
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(800, 600); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数

        Bundle bundle_fromMain = this.getIntent().getExtras();
        user_name = bundle_fromMain.getString("userName");
    }

    protected void setCameraListener() {
        upLoadfromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    camera.takePicture(null, null, new MyPictureCallback());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void setAlbumListener() {
        upLoadfromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //开启pictures画面type设定为image
                intent.setType("image/*");
                //使用Intent.ACTION_GET_CONTENT这个Action
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //取得相片后返回本画面
                startActivityForResult(intent, 1);
//                //开启线程上传图片
//                new Thread((Runnable) () -> {
//                    try {
//                        File file1 = new File(Environment.getExternalStorageDirectory(), pic_uri);
//                        if (file1 != null) {
//                            String request = UploadUtil.uploadFile(file1, "UploadPicServlet");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
            }
        });
    }

    private final class MyPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                //获得图片
                bundle = new Bundle();
                bundle.putByteArray("bytes", data);

                //将图片顺时针旋转90度
                Bitmap bitmap_cc = BitmapFactory.decodeByteArray(data,0,data.length);
                Matrix mx = new Matrix();
                mx.setRotate(90,(float)bitmap_cc.getWidth()/2,(float)bitmap_cc.getHeight()/2);
                final Bitmap bm = Bitmap.createBitmap(bitmap_cc,0,0,bitmap_cc.getWidth(),bitmap_cc.getHeight(),mx,true);

                // 保存图片到sd卡中
                File fileFolder = new File(getSDPath() + "/ProtoUI/");
                boolean isDirectoryCreated = fileFolder.exists();
                if (!isDirectoryCreated) {
                    isDirectoryCreated = fileFolder.mkdir();
                }
                String photo_name = getPhotoFileName();
                mPhotoFile = new File(fileFolder, photo_name);
                mPhotoPath = getSDPath() + "/ProtoUI/" + photo_name;

                try{
                    FileOutputStream outputStream = new FileOutputStream(mPhotoFile);
                    //outputStream.write(data);
                    bm.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), R.string.msg_takephotot, Toast.LENGTH_SHORT).show();

                //开启线程上传图片
                new upload_pic().start();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //手机画面定格
                camera.stopPreview();
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(mPhotoFile);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                Recommend(data);
            }
        }
    }

    private class upload_pic extends Thread {
        public void run() {
            try {
                if (mPhotoFile != null) {
                    final Map<String, File> files = new HashMap<String, File>();
                    files.put("uploadfile", mPhotoFile);
                    //返回值是根据上传图像推荐的相似时尚服装图像
                    recommend_pic_name = UploadUtil.uploadpic(requestURL_getpic, files);
                    bundle_toUploadPicInfo.putString("recommend_pic_name",recommend_pic_name);
                    //获取推荐的到的对应服装信息
                    downloadclothinfo_recommend();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadclothinfo_recommend() {
        //开启线程，网络获取对应服装信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                //post方法向服务器传入用户名与索引
                map.put("recommend_pic_name", recommend_pic_name);

                try{
                    url = new URL(RECOMMEND_INFOPATH);
                } catch (MalformedURLException e) {
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
                        // 取回响应的结果，推荐服装图像的属性特征数据
                        String s = changeInputStream(urlConnection.getInputStream(), "UTF-8");
                        attr_value = s.split(",");

                        bundle_toUploadPicInfo.putStringArray("clothingInfo",attr_value);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    private String getPhotoFileName() {
        //Date date = new Date(System.currentTimeMillis());
        //SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        //return dateFormat.format(date) + ".jpg";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        return sdf.format(date) + ".jpg";
    }

    private void Recommend(byte[] data) {
        upLoadfromAlbum.setVisibility(View.GONE);
        upLoadfromCamera.setVisibility(View.GONE);
        recmd_txt1.setVisibility(View.GONE);
        recmd_txt2.setVisibility(View.GONE);
        recmd_txt_tryagain.setVisibility(View.VISIBLE);
        recmd_txt_confirm.setVisibility(View.VISIBLE);
        tryagain.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
        Bitmap bitmap = toBitmap(mPhotoPath);
        //mImageView.setImageBitmap(bitmap);
        //设置再来一次的监听
        setTryagainListener();
        //设置确认上传的监听
        setConfirmListener();
    }

    protected void setTryagainListener() {
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //转回拍摄界面
                    upLoadfromAlbum.setVisibility(View.VISIBLE);
                    upLoadfromCamera.setVisibility(View.VISIBLE);
                    recmd_txt1.setVisibility(View.VISIBLE);
                    recmd_txt2.setVisibility(View.VISIBLE);
                    recmd_txt_tryagain.setVisibility(View.GONE);
                    tryagain.setVisibility(View.GONE);
                    mPhotoPath = null;
                    camera.startPreview(); // 拍完照后，重新开始预览
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void setConfirmListener() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //转到UploadPicInfoActivity页面
                    Intent i = new Intent(RecommendActivity.this, UploadPicInfoActivity.class);
                    bundle_toUploadPicInfo.putString("camera_pic",mPhotoPath);
                    bundle_toUploadPicInfo.putString("userName",user_name);
                    i.putExtras(bundle_toUploadPicInfo);
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //设置预览图
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            pic_uri = uri.toString();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //mImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final class SurfaceCallback implements Callback {
        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(100); // 设置照片质量
            if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
            }
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                //camera.setDisplayOrientation(getPreviewDegree(RecommendActivity.this));
                camera.setDisplayOrientation(90);
                camera.cancelAutoFocus();// 打开自动对焦
                camera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    private Bitmap toBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
