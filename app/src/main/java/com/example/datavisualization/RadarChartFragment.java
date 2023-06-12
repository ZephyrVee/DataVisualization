package com.example.datavisualization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RadarChartFragment extends Fragment {
    final int DEFAULT_KATEGORI = 0;

    DatasetKetenagakerjaanKabupaten data;

    RadarChart radarChart;
    TextView ubahWarnaTitle;
    GridLayout ubahWarnaField;

    ArrayList<IRadarDataSet> radarDataSetList;
    ArrayList<Integer> tahunList;
    Map<String, Integer> jenisKelaminMap;
    String[] kategoriArray, jenisKelaminArray, warnaNamaArray;
    Integer[] warnaArray;
    int kategori;

    public RadarChartFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;

        radarDataSetList = new ArrayList<>();
        tahunList = new ArrayList<>();
        jenisKelaminMap = new HashMap<>();

        jenisKelaminArray = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;

        warnaNamaArray = new String[]{"Biru", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};
        warnaArray = new Integer[]{
                Color.BLUE,
                Color.CYAN,
                Color.DKGRAY,
                Color.GRAY,
                Color.GREEN,
                Color.MAGENTA,
                Color.RED,
                Color.YELLOW
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radar_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radarChart = getView().findViewById(R.id.radarChart);
        ubahWarnaTitle = getView().findViewById(R.id.chart_radar_warna_title);
        ubahWarnaField = getView().findViewById(R.id.chart_radar_warna_field);
        getBundle();
        init();
        set();
        refreshData();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        kategoriArray = DatasetKetenagakerjaanKabupaten.getTableList(kategori);

        tahunList = getArguments().getIntegerArrayList("tahun");
    }
    private void init(){
        for(int i = 0; i < jenisKelaminArray.length; i++){
            if(i == 2){
                jenisKelaminMap.put(jenisKelaminArray[i], 1);
            }
            else {
                jenisKelaminMap.put(jenisKelaminArray[i], 0);
            }
            ((GridLayout)getView().findViewById(R.id.chart_radar_jenis_kelamin_field)).addView(addCheckBox(jenisKelaminArray[i]));
        }
        for(int i = 0; i < warnaArray.length; i++){
            ubahWarnaField.addView(addWarnaButton(i));
        }
        hideUbahWarna();

        radarChart.setBackgroundColor(Color.rgb(60, 65, 82));

        radarChart.getDescription().setEnabled(false);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);

        radarChart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(DatasetKetenagakerjaanKabupaten.getTableList(kategori)));
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);

        radarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showUbahWarna(h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                hideUbahWarna();
            }
        });
    }
    private CheckBox addCheckBox(String text){
        GridLayout.LayoutParams cbpr = new GridLayout.LayoutParams();
        cbpr.setMargins(8,8,8,8);

        CheckBox cb = new CheckBox(getContext());
        cb.setLayoutParams(cbpr);
        cb.setText(text);
        cb.setTextSize(10);
        cb.setTextColor(Color.BLACK);
        if(jenisKelaminMap.get(text) == 1){
            cb.setChecked(true);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    jenisKelaminMap.put(text, 1);
                }
                else{
                    jenisKelaminMap.put(text, 0);
                }
                set();
                refreshData();
            }
        });
        return cb;
    }
    private Button addWarnaButton(int index){
        GridLayout.LayoutParams bpr = new GridLayout.LayoutParams();
        bpr.setMargins(8,8,8,8);

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaNamaArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaArray[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }
    private void set(){
        radarDataSetList.clear();
        for(Integer t : tahunList){
            for(int i = 0; i < jenisKelaminArray.length; i++){
                if(jenisKelaminMap.get(jenisKelaminArray[i]) == 1){
                    ArrayList<RadarEntry> radarEntry = new ArrayList<>();
                    ArrayList<Integer> dataEntry;
                    if(i == 2){
                        dataEntry = data.get(t, kategori);
                    }
                    else {
                        dataEntry = data.get(t, i, kategori);
                    }
                    for(int k = 0; k < dataEntry.size(); k++){
                        radarEntry.add(new RadarEntry(dataEntry.get(k)));
                    }
                    RadarDataSet radarDataSet = new RadarDataSet(radarEntry, jenisKelaminArray[i] + " " + t);
                    radarDataSet.setColor(warnaArray[(i + t) % 8]);
                    radarDataSetList.add(radarDataSet);
                }
            }
        }
    }
    private void refreshData(){
        RadarData radarData = new RadarData(radarDataSetList);
        radarData.setValueTextSize(8f);
        radarData.setDrawValues(false);
        radarData.setValueTextColor(Color.WHITE);
        radarChart.setData(radarData);
        radarChart.invalidate();
    }
    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_radar_warna)).removeAllViews();
    }
    private void showUbahWarna(int dataSetIndex){
        LinearLayout ll = getView().findViewById(R.id.chart_radar_warna);
        if(ll.getChildCount() == 0) {
            ll.addView(ubahWarnaTitle);
            ll.addView(ubahWarnaField);
        }
        for(int i = 0; i < ubahWarnaField.getChildCount(); i++){
            final int index = i;
            ubahWarnaField.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RadarDataSet)radarDataSetList.get(dataSetIndex)).setColor(warnaArray[index]);
                    refreshData();
                    hideUbahWarna();
                }
            });
        }
    }
}