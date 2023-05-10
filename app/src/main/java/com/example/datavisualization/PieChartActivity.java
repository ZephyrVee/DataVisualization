package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartActivity extends AppCompatActivity {
    PieChart pieChart;
    PieDataSet pieDataSet;
    PieData pieData;

    ArrayList<PieEntry> pieEntry = new ArrayList<>();
    ArrayList<PieEntry> visitors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> value = extras.getStringArrayList("table");
            //The key argument here must match that used in the other activity
            for(int i = 0; i < value.size(); i++){
                String[] str = value.get(i).split(",");
                pieEntry.add(new PieEntry(Integer.parseInt(str[1]), str[0]));
            }
        }
        else {
            pieEntry.add(new PieEntry(508, "2016"));
            pieEntry.add(new PieEntry(600, "2017"));
            pieEntry.add(new PieEntry(750, "2018"));
            pieEntry.add(new PieEntry(600, "2019"));
            pieEntry.add(new PieEntry(670, "2020"));
        }

        pieDataSet = new PieDataSet(pieEntry, "Visitors");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        pieData = new PieData(pieDataSet);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Pie Chart");
        pieChart.animate();

        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}