package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {
    BarDataSet barDataSet;
    BarData barData;
    BarChart barChart;

    ArrayList<BarEntry> visitors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> value = extras.getStringArrayList("table");
            //The key argument here must match that used in the other activity
            for(int i = 0; i < value.size(); i++){
                String[] str = value.get(i).split(",");
                visitors.add(new BarEntry(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
            }
        }
        else {
            visitors.add(new BarEntry(2014, 420));
            visitors.add(new BarEntry(2015, 475));
            visitors.add(new BarEntry(2016, 508));
            visitors.add(new BarEntry(2017, 550));
            visitors.add(new BarEntry(2018, 650));
            visitors.add(new BarEntry(2019, 470));
        }

        barDataSet = new BarDataSet(visitors, "Visitors");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barData = new BarData(barDataSet);

        barChart = findViewById(R.id.barChart);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Bar Chart");
        barChart.animateY(2000);

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}