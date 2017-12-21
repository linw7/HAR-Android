package com.example.lele.protoui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;


public class EquipmentActivity extends AppCompatActivity {

    private TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();    //初始化TabHost组件

        //添加第一个标签页
        tabhost.addTab(tabhost.newTabSpec("tab").setIndicator("手机模式").setContent(new Intent(this, CollectActivity.class)));

        //添加第二个标签页
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("手环模式").setContent(new Intent(this, AdjustActivity.class)));
    }
}
