package com.example.lele.protoui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.Map;

public class PieActivity extends AppCompatActivity {
    private PieChart pie_chart;
    public DemoData demodata = new DemoData();
    int sit, stand, upstairs, downstairs, walk, jog;
    TextView sit_time, stand_time, upstairs_time, downstairs_time, walk_time, jog_time;

    private void get_data(){

        OfflineFileRW raw = new OfflineFileRW();
        String date = HistoryMainActivity.c_month + "." + HistoryMainActivity.c_day;
        Map<String, String> result = raw.get(PieActivity.this, "history.txt", date);

        sit = Integer.valueOf(result.get("sit").toString()).intValue();
        stand = Integer.valueOf(result.get("stand").toString()).intValue();
        upstairs = Integer.valueOf(result.get("upstairs").toString()).intValue();
        downstairs = Integer.valueOf(result.get("downstairs").toString()).intValue();
        walk = Integer.valueOf(result.get("walk").toString()).intValue();
        jog = Integer.valueOf(result.get("jog").toString()).intValue();

        /*
        demodata.set_data();
        ArrayList<ArrayList> data = demodata.get_elem(HistoryMainActivity.c_day);
        ArrayList today = data.get(2);
        sit = Integer.valueOf(today.get(0).toString()).intValue();
        stand = Integer.valueOf(today.get(1).toString()).intValue();
        upstairs = Integer.valueOf(today.get(2).toString()).intValue();
        downstairs = Integer.valueOf(today.get(3).toString()).intValue();
        walk = Integer.valueOf(today.get(4).toString()).intValue();
        jog = Integer.valueOf(today.get(5).toString()).intValue();
        */
    }

    private PieData get_pie_data() {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        xValues.add("静坐");
        xValues.add("站立");
        xValues.add("上楼");
        xValues.add("下楼");
        xValues.add("步行");
        xValues.add("慢跑");

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        yValues.add(new Entry(sit, 0));
        yValues.add(new Entry(stand, 1));
        yValues.add(new Entry(upstairs, 2));
        yValues.add(new Entry(downstairs, 3));
        yValues.add(new Entry(walk, 4));
        yValues.add(new Entry(jog, 5));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(0, 245, 255));
        colors.add(Color.rgb(131, 111, 255));
        colors.add(Color.rgb(192, 255, 62));
        colors.add(Color.rgb(255, 160, 122));
        colors.add(Color.rgb(255, 187, 255));
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

    private void set_percent(){
        float sum_time = sit + stand + upstairs + downstairs + walk + jog;

        sit_time.setText(String.format("%.2f", sit/sum_time*100) + "%");
        stand_time.setText(String.format("%.2f", stand/sum_time*100) + "%");
        upstairs_time.setText(String.format("%.2f", upstairs/sum_time*100) + "%");
        downstairs_time.setText(String.format("%.2f", downstairs/sum_time*100) + "%");
        walk_time.setText(String.format("%.2f", walk/sum_time*100) + "%");
        jog_time.setText(String.format("%.2f", jog/sum_time*100) + "%");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        pie_chart = (PieChart) findViewById(R.id.pie_chart);
        sit_time = (TextView) findViewById(R.id.sit_time);
        stand_time = (TextView) findViewById(R.id.stand_time);
        upstairs_time = (TextView) findViewById(R.id.upstairs_time);
        downstairs_time = (TextView) findViewById(R.id.downstairs_time);
        walk_time = (TextView) findViewById(R.id.walk_time);
        jog_time = (TextView) findViewById(R.id.jog_time);

        get_data();
        PieData pie_data = get_pie_data();
        show_pie_chart(pie_chart, pie_data);
        set_percent();
    }
}
