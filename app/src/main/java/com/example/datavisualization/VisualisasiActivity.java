package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VisualisasiActivity extends AppCompatActivity {
    Intent barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisasi);

        barChart = new Intent(this, BarChartActivity.class);

        findViewById(R.id.visualisasi_bar_chart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(barChart);
            }
        });
    }
}