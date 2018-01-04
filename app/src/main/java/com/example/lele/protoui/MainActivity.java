package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActivityGroup implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView nav_view;
    private DrawerLayout drawer;

    private TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_menu);

        ImageView menuImg = findViewById(R.id.title_bar_menu_btn);
        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setItemTextColor(null);
        nav_view.setItemIconTintList(null);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        nav_view.setNavigationItemSelectedListener(this);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();
        tabhost.setup(this.getLocalActivityManager());

        tabhost.addTab(tabhost.newTabSpec("tab").setIndicator("校准").setContent(new Intent(this, AdjustPhoneActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("实时").setContent(new Intent(this, CollectPhoneActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("提醒").setContent(new Intent(this, RemindPhoneActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("历史").setContent(new Intent(this, HistoryMainActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("建议").setContent(new Intent(this, SuggestMainActivity.class)));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_retarget) {
            Intent i = new Intent(MainActivity.this, AdjustActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_collect) {
            Intent i = new Intent(MainActivity.this, CollectActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_his) {
            Intent i = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(i);
        }
        if(id == R.id.nav_sugg){
            Intent i = new Intent(MainActivity.this, SuggestActivity.class);
            startActivity(i);
        }
        if(id == R.id.nav_pos){
            Intent i = new Intent(MainActivity.this, RemindActivity.class);
            startActivity(i);
        }
        if(id == R.id.nav_setting){
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        }
        if (id == R.id.nav_help) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.AlertDialog_title)
                    .setMessage(R.string.AlertDialog_content)
                    .setPositiveButton(R.string.AlertDialog_yes, null)
                    .setNegativeButton(R.string.AlertDialog_no, null)
                    .show();
        }
        if (id == R.id.nav_exit) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
