package com.example.lele.protoui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by LeLe on 2017/8/15.
 */
public class PopMenu extends PopupWindow {
    private View mView;
    private TextView btn2017, btn2016, btn2015, btnLater, btnSpr, btnSummr, btnAutn, btnWintr;
    public int year, season;

    public PopMenu(Activity paramActivity, int paramInt1, int paramInt2) {
        mView = LayoutInflater.from(paramActivity).inflate(R.layout.pop_menu, null);
        btn2017 = (TextView) mView.findViewById(R.id.btn2017);
        btn2016 = (TextView) mView.findViewById(R.id.btn2016);
        btn2015 = (TextView) mView.findViewById(R.id.btn2015);
        btnLater = (TextView) mView.findViewById(R.id.btnLater);
        btnSpr = (TextView) mView.findViewById(R.id.btnSpr);
        btnSummr = (TextView) mView.findViewById(R.id.btnSummr);
        btnAutn = (TextView) mView.findViewById(R.id.btnAutn);
        btnWintr = (TextView) mView.findViewById(R.id.btnWintr);

        setContentView(mView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));

        //监听
        //返回参数：年代、季节
        year=season=0;
        btn2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 2017) {
                    btn2017.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btn2017.getPaint();
                    paint.setFakeBoldText(true);
                    year = 2017;
                    //
                    btn2016.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2016.getPaint();
                    paint.setFakeBoldText(false);
                    btn2015.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2015.getPaint();
                    paint.setFakeBoldText(false);
                    btnLater.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnLater.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        btn2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 2016) {
                    btn2016.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btn2016.getPaint();
                    paint.setFakeBoldText(true);
                    year = 2016;
                    //
                    btn2017.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2017.getPaint();
                    paint.setFakeBoldText(false);
                    btn2015.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2015.getPaint();
                    paint.setFakeBoldText(false);
                    btnLater.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnLater.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        btn2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 2015) {
                    btn2015.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btn2015.getPaint();
                    paint.setFakeBoldText(true);
                    year = 2015;
                    //
                    btn2016.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2016.getPaint();
                    paint.setFakeBoldText(false);
                    btn2017.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2017.getPaint();
                    paint.setFakeBoldText(false);
                    btnLater.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnLater.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 2010) {
                    btnLater.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btnLater.getPaint();
                    paint.setFakeBoldText(true);
                    year = 2010;
                    //
                    btn2016.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2016.getPaint();
                    paint.setFakeBoldText(false);
                    btn2015.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2015.getPaint();
                    paint.setFakeBoldText(false);
                    btn2017.setTextColor(Color.rgb(170, 178, 189));
                    paint = btn2017.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        //季节--------------------------------------------------------------------------------------
        btnSpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 0) {
                    btnSpr.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btnSpr.getPaint();
                    paint.setFakeBoldText(true);
                    season = 0;
                    //
                    btnSummr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnSummr.getPaint();
                    paint.setFakeBoldText(false);
                    btnAutn.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnAutn.getPaint();
                    paint.setFakeBoldText(false);
                    btnWintr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnWintr.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        btnSummr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 1) {
                    btnSummr.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btnSummr.getPaint();
                    paint.setFakeBoldText(true);
                    season = 1;
                    //
                    btnSpr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnSpr.getPaint();
                    paint.setFakeBoldText(false);
                    btnAutn.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnAutn.getPaint();
                    paint.setFakeBoldText(false);
                    btnWintr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnWintr.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        btnAutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 2) {
                    btnAutn.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btnAutn.getPaint();
                    paint.setFakeBoldText(true);
                    season = 2;
                    //
                    btnSummr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnSummr.getPaint();
                    paint.setFakeBoldText(false);
                    btnSpr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnSpr.getPaint();
                    paint.setFakeBoldText(false);
                    btnWintr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnWintr.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
        btnWintr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (year != 3) {
                    btnWintr.setTextColor(Color.rgb(255, 0, 0));
                    TextPaint paint = btnWintr.getPaint();
                    paint.setFakeBoldText(true);
                    season = 3;
                    //
                    btnSummr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnSummr.getPaint();
                    paint.setFakeBoldText(false);
                    btnAutn.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnAutn.getPaint();
                    paint.setFakeBoldText(false);
                    btnSpr.setTextColor(Color.rgb(170, 178, 189));
                    paint = btnSpr.getPaint();
                    paint.setFakeBoldText(false);
                }
            }
        });
    }

    public int[] getTagInfo() {
        int[] arr = new int[2];
        arr[0] = year;
        arr[1] = season;
        return arr;
    }
}

