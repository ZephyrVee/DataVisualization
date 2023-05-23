package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class VisualisasiTestActivity extends AppCompatActivity {

    Fragment bar, line;
    TextView guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisasi);

        bar = new BarChartFragment();
        line = new LineChartFragment();

        guide = (TextView) findViewById(R.id.guideText);

        this.findViewById(R.id.barChartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(getFragment("Bar"));

                if(guide.getVisibility() == View.VISIBLE){
                    guide.setVisibility(View.GONE);
                }
            }
        });

        this.findViewById(R.id.lineChartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(getFragment("Line"));

                if(guide.getVisibility() == View.VISIBLE){
                    guide.setVisibility(View.GONE);
                }
            }
        });
    }

    private void switchFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.commit();
    }

    private Fragment getFragment(String str){
        switch(str){
            case "Bar":
                return bar;
            case "Line":
                return line;
            default:
                return bar;
        }
    }
}