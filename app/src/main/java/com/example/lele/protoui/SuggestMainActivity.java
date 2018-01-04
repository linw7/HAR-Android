package com.example.lele.protoui;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class SuggestMainActivity extends AppCompatActivity {
    RatingBar rating_bar;
    TextView suggest;
    EditText commit_text;
    Button commit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_main);

        rating_bar = (RatingBar)findViewById(R.id.rating_bar);
        suggest = (TextView)findViewById(R.id.suggest);
        commit_text = (EditText)findViewById(R.id.commit_text);
        commit_button = (Button)findViewById(R.id.commit_button);

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
                alter_submit();
            }
        });
    }
    private void alter_submit() {
        String msg = "请确认反馈建议";
        new AlertDialog.Builder(this)
                .setTitle(R.string.Alert_submit)
                .setMessage(msg)
                .setPositiveButton(R.string.AlertDialog_yes, null)
                .setNegativeButton(R.string.AlertDialog_no, null)
                .show();
    }
}
