package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class VisualisasiActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    TabLayout tabLayout;
    ArrayList<Fragment> fragment;
    FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.chart_button_background)));

        setContentView(R.layout.activity_visualisasi);

        Bundle bundle = getIntent().getExtras();
        String c = bundle.getString("Chart");

        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                if(MainActivity.database.noConnection){
                    TextView tv = findViewById(R.id.visualisasi_loading);
                    tv.setText("Tidak ada jaringan internet");
                    tv.setTextColor(Color.RED);
                }
                else{
                    TextView tv = findViewById(R.id.visualisasi_loading);
                    tv.setText("Sedang Memperoleh Data. Mohon tunggu ...");
                    tv.setTextColor(Color.BLACK);
                }

                if(MainActivity.database.retrieved){
                    ((LinearLayout)findViewById(R.id.visualisasi_loading).getParent()).removeViewAt(0);
                }
                else {
                    handler.postDelayed(this, 1000);
                }
            }
        });
        MainActivity.thread.put("visualisasi_loading", thread);
        MainActivity.thread.get("visualisasi_loading").start();

        fragment = new ArrayList<>();
        switch(c){
            default:
                finish();
                break;
            case "Bar":
                fragment.add(new InputFragment());
                BarChartFragment bar = new BarChartFragment();
                fragment.add(bar);
                findViewById(R.id.visualisasi_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bar.save();
                    }
                });
                break;
            case "Line":
                fragment.add(new InputFragment());
                LineChartFragment line = new LineChartFragment();
                fragment.add(line);
                findViewById(R.id.visualisasi_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        line.save();
                    }
                });
                break;
            case "Pie":
                fragment.add(new SatuTahunInputFragment());
                PieChartFragment pie = new PieChartFragment();
                fragment.add(pie);
                findViewById(R.id.visualisasi_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pie.save();
                    }
                });
                break;
            case "Scatter":
                fragment.add(new InputFragment());
                ScatterChartFragment scatter = new ScatterChartFragment();
                fragment.add(scatter);
                findViewById(R.id.visualisasi_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scatter.save();
                    }
                });
                break;
            case "Radar":
                fragment.add(new InputFragment());
                RadarChartFragment radar = new RadarChartFragment();
                fragment.add(radar);
                findViewById(R.id.visualisasi_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radar.save();
                    }
                });
                break;
            case "Combined":
                fragment.add(new CombinedInputFragment());
                CombinedChartFragment combined = new CombinedChartFragment();
                fragment.add(combined);
                findViewById(R.id.visualisasi_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        combined.save();
                    }
                });
                break;
        }

        frameLayout = findViewById(R.id.visualisasi_frame_layout);
        saveButton = findViewById(R.id.visualisasi_save);
        saveButton.setVisibility(View.INVISIBLE);
        tabLayout = findViewById(R.id.visualisasi_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Chart"));
        tabLayout.setTabTextColors(Color.LTGRAY, Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        switchFragment(0);
        showGuide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.thread.get("visualisasi_loading").interrupt();
        MainActivity.thread.remove("visualisasi_loading");
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visualisasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showGuide();
        return true;
    }

    private void switchFragment(int position){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(position == 1){
            fragment.get(position).setArguments(fragment.get(0).getArguments());
            saveButton.setVisibility(View.VISIBLE);
        }
        else{
            saveButton.setVisibility(View.INVISIBLE);
        }
        ft.replace(R.id.visualisasi_frame_layout, fragment.get(position));
        ft.commit();
    }

    private void showGuide(){
        Dialog dialog = new Dialog(VisualisasiActivity.this);
        dialog.setContentView(R.layout.visualisasi_popup);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(-1, -1);
        dialog.show();
        Button popupClose = dialog.findViewById(R.id.visualisasi_popup_close);
        popupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}