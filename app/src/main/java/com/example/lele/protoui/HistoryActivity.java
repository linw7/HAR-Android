package com.example.lele.protoui;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CalendarView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private CalendarView calendar;
    private PieChart pie_chart;

    private LineChart line_chart;
    private XAxis xAxis;
    private YAxis yAxis;
    private YAxis yAxisR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        pie_chart = (PieChart) findViewById(R.id.pie_chart);

        line_chart = (LineChart)findViewById(R.id.line_chart);
        xAxis = line_chart.getXAxis();
        yAxis = line_chart.getAxisLeft();
        yAxisR = line_chart.getAxisRight();

        LineData line_data = get_line_data();
        show_chart(line_chart, line_data);

        PieData pie_data = get_pie_data();
        show_pie_chart(pie_chart, pie_data);
    }

    private void show_chart(LineChart line_chart, LineData line_data) {
        // 图表概览
        // 右下角图表描述、颜色、位置、字体、大小
        line_chart.setDescription("HAR");
        line_chart.setDescriptionColor(Color.RED);
        // line_chart.setDescriptionPosition(800f,1200f);
        // line_chart.setDescriptionTypeface();
        // line_chart.setDescriptionTextSize(8);
        // line_chart.setNoDataTextDescription("没有数据呢(⊙o⊙)");

        // 是否显示格子背景
        // line_chart.setDrawGridBackground(false);
        // 设置背景色
        line_chart.setBackgroundColor(Color.WHITE);

        // 边框
        // line_chart.setDrawBorders(true);
        // line_chart.setBorderColor(Color.rgb(57, 135, 200));   //上面的边框颜色
        // line_chart.setBorderWidth(2);       //上面边框的宽度，float类型，dp单位

        // 交互设置
        // 是否可触摸
        line_chart.setTouchEnabled(true);
        // 是否可拖拽
        line_chart.setDragEnabled(true);
        // 是否可缩放
        line_chart.setScaleEnabled(true);
        // 是否可同时缩放
        line_chart.setPinchZoom(true);
        // 是否双击方法图表
        line_chart.setDoubleTapToZoomEnabled(true);
        // 是否自动缩放
        line_chart.setAutoScaleMinMaxEnabled(true);

        // line_chart.setViewPortOffsets(10, 0, 10, 0);
        // Legend l = line_chart.getLegend();
        // l.setEnabled(true);

        yAxisR.setDrawGridLines(false);
        yAxisR.setEnabled(false);

        yAxis.resetAxisMaxValue();
        yAxis.resetAxisMinValue();
        yAxis.setSpaceTop(15);
        yAxis.setSpaceBottom(15);
        yAxis.setEnabled(true);
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        xAxis.resetAxisMinValue();
        xAxis.resetAxisMaxValue();
        xAxis.setDrawGridLines(false);
        // xAxis.setAxisLineColor(Color.rgb(244, 117, 117));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        line_chart.setData(line_data);
        line_chart.animateX(100);
        line_chart.invalidate();
    }

    private LineData get_line_data() {
        // 生成x轴数据
        ArrayList<String> xVal = new ArrayList<>();
        xVal.add("" + 0);
        xVal.add("" + 1);
        xVal.add("" + 2);
        xVal.add("" + 3);
        xVal.add("" + 4);
        xVal.add("" + 5);
        xVal.add("" + 6);
        xVal.add("" + 7);

        // 生成y轴数据
        ArrayList<Entry> yVal = new ArrayList<>();
        yVal.add(new Entry(3, 1));
        yVal.add(new Entry(7, 2));
        yVal.add(new Entry(2, 3));
        yVal.add(new Entry(5, 4));
        yVal.add(new Entry(4, 5));
        yVal.add(new Entry(6, 6));

        LineDataSet set1 = new LineDataSet(yVal, "Data");
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

        // 设置线宽和颜色
        set1.setLineWidth(1f);
        set1.setColor(Color.rgb(244, 117, 117));

        // 设置点数据半径和颜色
        set1.setCircleRadius(2f);
        set1.setCircleColor(Color.RED);

        // 焦点线
        set1.setHighLightColor(Color.RED);

        // 是否显示值
        set1.setDrawValues(true);
        LineData data = new LineData(xVal, set1);
        return data;
    }

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

}
