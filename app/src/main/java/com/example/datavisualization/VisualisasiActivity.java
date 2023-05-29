package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

public class VisualisasiActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisasi);

        Bundle bundle = getIntent().getExtras();
        String c = bundle.getString("Chart");

        switch(c){
            case "Bar"

                break;
            default:
                break;
        }

        frameLayout = findViewById(R.id.visualisasi_frame_layout);
        tabLayout = findViewById(R.id.visualisasi_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Chart"));
    }
}