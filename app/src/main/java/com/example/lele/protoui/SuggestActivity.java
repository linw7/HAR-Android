package com.example.lele.protoui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class SuggestActivity extends AppCompatActivity {
    RatingBar rating_bar;
    TextView suggest;
    EditText commit_text;
    Button commit_button;
    ImageView title_bar_back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        rating_bar = (RatingBar)findViewById(R.id.rating_bar);
        suggest = (TextView)findViewById(R.id.suggest);
        commit_text = (EditText)findViewById(R.id.commit_text);
        commit_button = (Button)findViewById(R.id.commit_button);
        title_bar_back_btn = (ImageView)findViewById(R.id.title_bar_back_btn);

        title_bar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuggestActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest.setText("今日活动量过大");
            }
        });

        commit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit_text.getText();
            }
        });

        commit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 上传服务器
                Intent i = new Intent(SuggestActivity.this, CollectActivity.class);
                startActivity(i);
            }
        });

    }
}
