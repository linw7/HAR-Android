package com.example.lele.protoui;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class RankActivity extends ActivityGroup {
    private TabHost tabhost;
    private ImageView search;
    private ImageView title_bar_back_btn;

    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        search = (ImageView) findViewById(R.id.search);
        title_bar_back_btn = (ImageView)findViewById(R.id.title_bar_back_btn);

        tabhost = (TabHost) findViewById(android.R.id.tabhost);
        tabhost.setup();
        tabhost.setup(this.getLocalActivityManager());

        tabhost.addTab(tabhost.newTabSpec("tab0").setIndicator("").setContent(new Intent(this, RankiniActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.sit_u)).setContent(new Intent(this, RankSitActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.stand_u)).setContent(new Intent(this, RankStandActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.walk_u)).setContent(new Intent(this, RankWalkActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab4").setIndicator("", getResources().getDrawable(R.drawable.jog_u)).setContent(new Intent(this, RankJogActivity.class)));
        tabhost.addTab(tabhost.newTabSpec("tab5").setIndicator("").setContent(new Intent(this, RankiniActivity.class)));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RankActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        title_bar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RankActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
