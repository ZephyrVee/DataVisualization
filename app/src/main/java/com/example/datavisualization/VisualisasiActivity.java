package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class VisualisasiActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    TabLayout tabLayout;
    ArrayList<Fragment> fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisasi);

        Bundle bundle = getIntent().getExtras();
        String c = bundle.getString("Chart");

        fragment = new ArrayList<>();
        fragment.add(new InputFragment(c));
        switch(c){
            case "Bar":
                fragment.add(new BarChartFragment());
                break;
            default:
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