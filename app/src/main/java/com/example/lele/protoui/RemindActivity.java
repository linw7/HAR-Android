package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class RemindActivity extends ActivityGroup {

    private TabHost tabhost;
    private ImageView title_bar_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        title_bar_back_btn = (ImageView)findViewById(R.id.title_bar_back_btn);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();    //初始化TabHost组件

        tabhost.setup(this.getLocalActivityManager());

        //添加第一个标签页
        tabhost.addTab(tabhost.newTabSpec("tab").setIndicator("手机模式").setContent(new Intent(this, RemindPhoneActivity.class)));

        //添加第二个标签页
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("手环模式").setContent(new Intent(this, RemindWatchActivity.class)));

        title_bar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RemindActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
