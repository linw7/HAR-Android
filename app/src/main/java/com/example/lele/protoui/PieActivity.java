package com.example.lele.protoui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class PieActivity extends AppCompatActivity {
    private PieChart pie_chart;

    private PieData get_pie_data() {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        xValues.add("Jog");  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        xValues.add("Walk");
        xValues.add("Sit");
        xValues.add("Stand");

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        yValues.add(new Entry(14, 0));
        yValues.add(new Entry(14, 1));
        yValues.add(new Entry(34, 2));
        yValues.add(new Entry(38, 3));

        PieDataSet pieDataSet = new PieDataSet(yValues, "s");
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    private void show_pie_chart(PieChart pie_chart, PieData pie_data) {
        pie_chart.setHoleRadius(60f);  //半径
        pie_chart.setTransparentCircleRadius(64f); // 半透明圈
        pie_chart.setDescription("");
        pie_chart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setRotationAngle(90); // 初始旋转角度
        pie_chart.setRotationEnabled(true); // 可以手动旋转
        pie_chart.setUsePercentValues(true);  //显示成百分比
        pie_chart.setCenterText("Activity Recognition");  //饼状图中间的文字
        pie_chart.setData(pie_data); //设置数据
        Legend mLegend = pie_chart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);  //最右边显示
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        pie_chart.animateXY(1000, 1000);  //设置动画
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        pie_chart = (PieChart) findViewById(R.id.pie_chart);
        PieData pie_data = get_pie_data();
        show_pie_chart(pie_chart, pie_data);

    }
}
