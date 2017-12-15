package com.example.lele.protoui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //右上弹出框
    private PopMenu topPopWindow;
    //主页加载与刷新
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private SimpleAdapter mSimpleAdapter;
    private ArrayList<HashMap<String, Object>> listItem;
    //public TextView maintext;
    private int mBackKeyPressedTimes = 0;
    //侧栏头像操作
    private ImageView main_avator;
    private SharedPrefsUtil sharedPrefsUtil = new SharedPrefsUtil();
    private static final String IMAGE_FILE_CUT_NAME = "avatarImage_cut.jpg";

    private static String IMAGEPATH = "http://192.168.1.66:8080/fashion_server/recommendPic";
    private static String INFOPATH = "http://192.168.1.66:8080/fashion_server/returnClothInfo";
    private static URL url;

    private int dynamicFlag = 0;
    private int picIndex = 0;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_menu);

        //maintext = findViewById(R.id.textView2);
        ImageView menuImg = findViewById(R.id.title_bar_menu_btn);
        final ImageView tagImg = findViewById(R.id.title_bar_tag_btn);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mListView = findViewById(R.id.listview);
        main_avator = findViewById(R.id.main_avator);
        NavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.setItemTextColor(null);
        nav_view.setItemIconTintList(null);

        //获取上一页面传递的用户名信息
        Bundle bundle_fromLogin = this.getIntent().getExtras();
        userName = bundle_fromLogin.getString("userName");

        //侧栏监听
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        //右上弹框监听
        tagImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showTopRightPopMenu(tagImg);
            }
        });

        //NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        nav_view.setNavigationItemSelectedListener(this);

        //主页内容加载------------------------------------------------------------------------------
        listItem = new ArrayList<HashMap<String, Object>>();
        //扔图片
        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.mainpage_test);//加入图片
            listItem.add(map);
        }
        mSimpleAdapter = new SimpleAdapter(this, listItem, R.layout.list_item, new String[]{"ItemImage"},
                new int[]{R.id.ItemImage});
        //list_item为自定义布局文件，为Listview中每一行内的布局，接下来两个参数，即将数据集内指定key对应的value，映射到行内指定的控件上。
        mListView.setAdapter(mSimpleAdapter);

        //下载图片并显示在listview中
        downloadphoto();
        //重写函数，使listview可显示Bitmap
        mSimpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder(){
            public boolean setViewValue(View view, Object data, String textRepresentation){
                if(view instanceof ImageView  && data instanceof Bitmap){
                    ImageView iv = (ImageView) view;
                    Bitmap bitmap = (Bitmap) data;
                    //Bitmap b = Bitmap.createScaledBitmap(bitmap, imageView.getDrawable().getIntrinsicWidth(), imageView.getDrawable().getIntrinsicHeight(), true);//创建一个可以缩放的位图对象
                    iv.setImageBitmap(bitmap);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        //测试list的点击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String msg;
                msg = arg2 + "selected";
                //maintext.setText(msg);
                //if (arg2 == 1) {
                //   Intent i = new Intent(MainActivity.this, ClothInfoActivity.class);
                //   startActivity(i);
                //}
                picIndex = arg2;
                downloadclothinfo(arg2);
            }
        });
        //刷新功能----------------------------------------------------------------------------------
        mSwipeLayout = findViewById(R.id.swipe_container);
        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                //发送一个延时1秒的handler信息
                handler.sendEmptyMessageDelayed(199, 1000);
            }
        });
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //动态加载----------------------------------------------------------------------------------
        //给listview设置一个滑动的监听
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当滑动状态发生改变的时候执行
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当不滚动的时候
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //判断是否是最底部
                        //if (view.getLastVisiblePosition() == (view.getCount()) - 1) {
                        //    maintext.setText(R.string.msg3);
                        //    HashMap<String, Object> map = new HashMap<String, Object>();
                        //    map.put("ItemImage", R.drawable.mainpage_test3);
                        //    listItem.add(map);
                        //    mSimpleAdapter.notifyDataSetChanged();
                        //}
                        break;
                }
            }

            //正在滑动的时候执行
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        //太乱了，需要精简
        if (sharedPrefsUtil.readPreferences(MainActivity.this, "DataBase")) {
            setPicToView();
        }
    }

    private void downloadclothinfo(int index) {
        //开启线程，网络获取对应服装信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                //post方法向服务器传入用户名与索引
                map.put("username", userName);
                map.put("index",String.valueOf(index));

                try{
                    url = new URL(INFOPATH);
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
                        // 取回响应的结果
                        Message msg = Message.obtain();
                        msg.what = 203;
                        msg.obj = changeInputStream(urlConnection.getInputStream(), "UTF-8");
                        handler.sendMessage(msg);
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

    private void downloadphoto() {
        //开启线程，网络下载图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> map = new HashMap<String, String>();
                //post方法向服务器传入用户名与索引，暂时设定取10张图片
                map.put("username", userName);
                map.put("index","0");
                Message msg = Message.obtain();
                Bitmap[] bm=new Bitmap[10];
                for(int i=0;i<10;i++){
                    map.put("index",String.valueOf(i));

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
                            Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                            bm[i]=bitmap;
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                msg.what=202;
                msg.obj = bm;
                handler.sendMessage(msg);
            }
        }

        ).start();
    }

    //主页刷新--------------------------------------------------------------------------------------
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                listItem.clear();
                //测试刷新功能
                for (int i = 0; i < 5; i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("ItemImage", R.drawable.mainpage_test);
                    listItem.add(map);
                }

                //下载图片并显示在listview中
                downloadphoto();

                //通知适配器数据变更
                mSimpleAdapter.notifyDataSetChanged();
                //设置组件的刷洗状态；false代表关闭
                mSwipeLayout.setRefreshing(false);
            }
            //将下载的图片显示在listview中
            if(msg.what == 202){
                Bitmap[] bm=(Bitmap[])msg.obj;
                listItem.clear();

                dynamicFlag++;
                dynamicFlag = dynamicFlag%2;

                if(dynamicFlag == 1) {
                    for(int i=0;i<5;i++){
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("ItemImage",bm[i]);
                        listItem.add(map);
                    }
                } else {
                    for(int i=5;i<10;i++){
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("ItemImage",bm[i]);
                        listItem.add(map);
                    }
                }
                mSimpleAdapter.notifyDataSetChanged();
            }
            //获取对应图像的服装信息
            if(msg.what == 203) {
                //s包含了所有返回的服装属性文本信息
                String s = (String) msg.obj;
                String[] attr_value = s.split(",");

                Intent i = new Intent(MainActivity.this, ClothInfoActivity.class);
                Bundle bundle_toClothInfo = new Bundle();

                if(dynamicFlag != 1) {
                    picIndex = picIndex + 5;
                }

                bundle_toClothInfo.putString("picIndex",String.valueOf(picIndex));
                bundle_toClothInfo.putString("userName",userName);
                bundle_toClothInfo.putStringArray("clothingInfo",attr_value);

                i.putExtras(bundle_toClothInfo);
                startActivity(i);
            }
        }
    };

    //侧栏------------------------------------------------------------------------------------------
    //重写实体返回键监听
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //按两次退出登录
            if (mBackKeyPressedTimes == 0) {
                Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
                mBackKeyPressedTimes = 1;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            mBackKeyPressedTimes = 0;
                        }
                    }
                }.start();
                return;
            } else {
                this.finish();
            }
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // 拍一拍
            Bundle bundle_toRecommend = new Bundle();
            bundle_toRecommend.putString("userName",userName);
            Intent i = new Intent(MainActivity.this, RecommendActivity.class);
            i.putExtras(bundle_toRecommend);
            startActivity(i);
        } else if (id == R.id.nav_setting) {
            // 偏好设置
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_level) {

        } else if (id == R.id.nav_help) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.AlertDialog_title)
                    .setMessage(R.string.AlertDialog_content)
                    .setPositiveButton(R.string.AlertDialog_yes, null)
                    .setNegativeButton(R.string.AlertDialog_no, null)
                    .show();
        } else if (id == R.id.nav_exit) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //显示右上角popup菜单---------------------------------------------------------------------------
    private void showTopRightPopMenu(ImageView imgView) {
        if (topPopWindow == null) {
            //(activity,onclicklistener,width,height)
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth = display.getWidth();
            int screenHeight = display.getHeight();
            int width, height;
            width = (new Double(screenWidth * 0.65)).intValue();
            height = (new Double(screenHeight * 0.16)).intValue();
            topPopWindow = new PopMenu(MainActivity.this, width, height);
        }
        //设置默认获取焦点
        topPopWindow.setFocusable(true);
        //以某个控件的x和y的偏移量位置开始显示窗口
        topPopWindow.showAsDropDown(imgView, 0, 0);
        int[] arr = new int[2];
        arr = topPopWindow.getTagInfo();
        //maintext.setText("Year:" + arr[0] + "Season" + arr[1]);
    }

    private void setPicToView() {
        Bitmap avator;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory().toString() + "/ProtoUI/");
                File mPhotoFile = new File(dir, IMAGE_FILE_CUT_NAME);//保存拍摄的图片
                String path = dir + IMAGE_FILE_CUT_NAME;
                if (mPhotoFile.exists()) {
                    avator = BitmapFactory.decodeFile(path);
                    Drawable drawable = new BitmapDrawable(null, avator);
                    main_avator.setImageDrawable(drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
