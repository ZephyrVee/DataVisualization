package com.example.datavisualization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
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
    String[] kategoriArray, jenisKelaminArray, warnaNamaArray, jenisKelaminNamaArray;
    int[] warnaArray;
    int kategori;

    public RadarChartFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;

        radarDataSetList = new ArrayList<>();
        tahunList = new ArrayList<>();
        jenisKelaminMap = new HashMap<>();

        jenisKelaminArray = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
        jenisKelaminNamaArray = new String[]{"(L)", "(P)", "(L+P)"};
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

        warnaNamaArray = getContext().getResources().getStringArray(R.array.chart_warna_nama_array);
        warnaArray = getContext().getResources().getIntArray(R.array.chart_warna_array);

        getBundle();
        init();
        set();
        refreshData();

        ((TextView)getView().findViewById(R.id.chart_radar_title)).setText(getContext().getResources().getStringArray(R.array.chart_title)[kategori]);

        ImageButton ib = getView().findViewById(R.id.chart_keterangan_button);
        if(kategori == DatasetKetenagakerjaanKabupaten.UMUR || kategori == DatasetKetenagakerjaanKabupaten.PENDIDIKAN){
            LinearLayout ll = getView().findViewById(R.id.chart_keterangan_field);
            ll.removeViewAt(2);
            ll.removeViewAt(1);
            ll.removeViewAt(0);
        }
        else{
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll = getView().findViewById(R.id.chart_adjustment);
                    TextView tv = getView().findViewById(R.id.chart_keterangan);
                    if(ll.getVisibility() == View.VISIBLE){
                        ll.setVisibility(View.INVISIBLE);
                        tv.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                        String s = "";
                        String[] s1 = DatasetKetenagakerjaanKabupaten.getList(kategori);
                        String[] s2 = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
                        for(int i = 0; i < s1.length; i++){
                            s += s2[i] +": " +s1[i] + System.getProperty("line.separator");
                        }
                        tv.setText(s);
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.icon_remove));
                    }
                    else {
                        ll.setVisibility(View.VISIBLE);
                        tv.setText("");
                        tv.getLayoutParams().height = 0;
                        ib.setImageDrawable(getResources().getDrawable(R.drawable.icon_add));
                    }
                }
            });
        }

        ((SwitchCompat)getView().findViewById(R.id.chart_radar_switch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for(IRadarDataSet i : radarDataSetList){
                        ((RadarDataSet)i).setDrawFilled(true);
                    }
                    refreshData();
                }
                else {
                    for(IRadarDataSet i : radarDataSetList){
                        ((RadarDataSet)i).setDrawFilled(false);
                    }
                    refreshData();
                }
            }
        });
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

        radarChart.getDescription().setEnabled(false);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.BLACK);
        radarChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(radarChart); // For bounds control
        radarChart.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(8f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(DatasetKetenagakerjaanKabupaten.getTableList(kategori)));
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setDrawLabels(false);

        Legend l = radarChart.getLegend();
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);

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
        b.setBackground(getResources().getDrawable(R.drawable.background_white_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaNamaArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.icon_warna);
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
                    int jumlah = 0;
                    for(int k = 0; k < dataEntry.size(); k++){
                        jumlah += dataEntry.get(k);
                    }
                    for(int k = 0; k < dataEntry.size(); k++){
                        radarEntry.add(new RadarEntry((float)dataEntry.get(k) / (float)jumlah * 100));
                    }
                    RadarDataSet radarDataSet = new RadarDataSet(radarEntry, t + "" + jenisKelaminNamaArray[i]);
                    radarDataSet.setColor(warnaArray[(i + t) % warnaNamaArray.length]);
                    if(((SwitchCompat)getView().findViewById(R.id.chart_radar_switch)).isChecked()){
                        radarDataSet.setDrawFilled(true);
                    }
                    else {
                        radarDataSet.setDrawFilled(false);
                    }
                    radarDataSet.setFillColor(warnaArray[(i + t) % warnaNamaArray.length]);
                    radarDataSet.setFillAlpha(100);
                    radarDataSet.setLineWidth(2f);
                    radarDataSet.setDrawHighlightCircleEnabled(true);
                    radarDataSet.setDrawHighlightIndicators(false);
                    radarDataSetList.add(radarDataSet);
                }
            }
        }
    }
    private void refreshData(){
        if(!radarDataSetList.isEmpty()) {
            RadarData radarData = new RadarData(radarDataSetList);
            radarData.setDrawValues(false);
            radarChart.setData(radarData);
            radarChart.invalidate();
            radarChart.animateXY(1000, 1000, Easing.EaseInOutQuad);
        }
        else {
            radarChart.clear();
        }
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
                    ((RadarDataSet)radarDataSetList.get(dataSetIndex)).setFillColor(warnaArray[index]);
                    refreshData();
                    hideUbahWarna();
                }
            });
        }
    }
    public void save(){
        if(radarChart.saveToGallery("radar_chart_" + System.currentTimeMillis(), 70)){
            Toast.makeText(getContext(), "Saved at DCIM folder", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Failed to icon_save Chart", Toast.LENGTH_SHORT).show();
        }
    }
}