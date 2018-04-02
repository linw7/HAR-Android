package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class MainChartActivity extends ActivityGroup {

    private TabHost tabhost;
    private ImageView title_bar_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chart);

        title_bar_back_btn = (ImageView)findViewById(R.id.title_bar_back_btn);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();    //初始化TabHost组件
        tabhost.setup(this.getLocalActivityManager());

        //添加第一个标签页
        tabhost.addTab(tabhost.newTabSpec("tab").setIndicator("饼状统计").setContent(new Intent(this, PieActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("柱状统计").setContent(new Intent(this, BarActivity.class)));

        title_bar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*
                Intent i = new Intent(MainChartActivity.this, HistoryMainActivity.class);
                startActivity(i);
                */
            }
        });
    }
}
