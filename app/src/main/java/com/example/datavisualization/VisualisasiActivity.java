package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class VisualisasiActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    TabLayout tabLayout;
    ArrayList<Fragment> fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //hide status bar

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
        fragment.add(new InputFragment());
        switch(c){
            default:
                fragment.add(new BarChartFragment());
                break;
            case "Line":
                fragment.add(new LineChartFragment());
                break;
        }

        frameLayout = findViewById(R.id.visualisasi_frame_layout);
        tabLayout = findViewById(R.id.visualisasi_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Chart"));
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

    private void switchFragment(int position){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(position == 1){
            fragment.get(position).setArguments(fragment.get(0).getArguments());
        }
        ft.replace(R.id.visualisasi_frame_layout, fragment.get(position));
        ft.commit();
    }
}