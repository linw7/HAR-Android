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
        xValues.add("静坐");
        xValues.add("站立");
        xValues.add("上楼");
        xValues.add("下楼");
        xValues.add("步行");
        xValues.add("慢跑");

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        yValues.add(new Entry(18, 0));
        yValues.add(new Entry(14, 1));
        yValues.add(new Entry(34, 2));
        yValues.add(new Entry(38, 3));
        yValues.add(new Entry(41, 4));
        yValues.add(new Entry(25, 5));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(255, 21, 226));
        colors.add(Color.rgb(155, 241, 226));

        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(10);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px);

        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    private void show_pie_chart(PieChart pie_chart, PieData pie_data) {
        pie_chart.setHoleRadius(58f);  //半径
        pie_chart.setTransparentCircleRadius(65f); // 半透明圈
        pie_chart.setDescription("");
        pie_chart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setRotationAngle(90); // 初始旋转角度
        pie_chart.setRotationEnabled(true); // 可以手动旋转
        pie_chart.setUsePercentValues(true);  //显示成百分比
        pie_chart.setCenterText("行为时长占比");  //饼状图中间的文字
        pie_chart.setData(pie_data); //设置数据
        Legend mLegend = pie_chart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        pie_chart.animateXY(2000, 2000);  //设置动画
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
