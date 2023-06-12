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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class PieChartFragment extends Fragment {
    final int DEFAULT_KATEGORI = 0;
    final int DEFAULT_JENIS_KELAMIN = 2;

    PieChart pieChart;
    PieDataSet pieDataSet;
    TextView ubahWarnaTitle;
    GridLayout ubahWarnaField;
    RadioGroup jenisKelaminField;


    DatasetKetenagakerjaanKabupaten data;
    int kategori, tahun, jenisKelamin;

    String[] kategoriArray, warnaNameArray, jenisKelaminArray;
    Integer[] warnaArray;

    ArrayList<Integer> warnaList;


    public PieChartFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;
        jenisKelamin = DEFAULT_JENIS_KELAMIN;
        jenisKelaminArray = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
        warnaNameArray = new String[]{"Biru", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};
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
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pieChart = getView().findViewById(R.id.pieChart);
        ubahWarnaField = getView().findViewById(R.id.chart_pie_warna_field);
        ubahWarnaTitle = getView().findViewById(R.id.chart_pie_warna_title);
        jenisKelaminField = getView().findViewById(R.id.chart_pie_jenis_kelamin_field);

        getBundle();
        init();

        initChart();
        set();
        refreshData();
    }

    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahun = getArguments().getInt("tahun");

        kategoriArray = DatasetKetenagakerjaanKabupaten.getTableList(kategori);
    }
    private void init(){
        for (int i = 0; i < jenisKelaminArray.length; i++){
            jenisKelaminField.addView(addRadioButton(jenisKelaminArray[i], i), i, new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        jenisKelaminField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                jenisKelamin = checkedId;
                set();
                refreshData();
                pieChart.animateY(1500);
            }
        });
        for(int i = 0; i < warnaNameArray.length; i++){
            ubahWarnaField.addView(addWarnaButton(i));
        }
        hideUbahWarna();
    }
    private void initChart(){
        Legend l = pieChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);

        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int dataIndex = (int) h.getX();
                showUbahWarna(dataIndex);
            }

            @Override
            public void onNothingSelected() {
                hideUbahWarna();
            }
        });
    }
    private RadioButton addRadioButton(String text, int index){
        RadioButton rb = new RadioButton(getContext());
        rb.setText(text);
        rb.setId(index);
        if(index == jenisKelamin){
            rb.setChecked(true);
        }
        return rb;
    }
    private Button addWarnaButton(int index){
        GridLayout.LayoutParams bpr = new GridLayout.LayoutParams();
        bpr.setMargins(8,8,8,8);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bpr.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        }

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.image_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaNameArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaArray[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }

    private void set(){
        ArrayList<PieEntry> pieEntry = new ArrayList<>();
        ArrayList<Integer> dataEntry;

        warnaList = new ArrayList<>();

        if(jenisKelamin == 2){
            dataEntry = data.get(tahun, kategori);
        }
        else {
            dataEntry = data.get(tahun, jenisKelamin, kategori);
        }
        for(int i = 0; i < dataEntry.size(); i++){
            pieEntry.add(new PieEntry(dataEntry.get(i), kategoriArray[i]));
            warnaList.add(warnaArray[i % 8]);
        }
        pieDataSet = new PieDataSet(pieEntry, kategoriArray[kategori] + " " + tahun);
        pieDataSet.setColors(warnaList);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(2f);
    }
    private void refreshData(){
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_pie_warna)).removeAllViews();
    }
    private void showUbahWarna(int dataIndex){
        LinearLayout ll = (LinearLayout)getView().findViewById(R.id.chart_pie_warna);
        if(ll.getChildCount() == 0) {
            ll.addView(ubahWarnaTitle);
            ll.addView(ubahWarnaField);
        }
        for(int i = 0; i < ubahWarnaField.getChildCount(); i++){
            final int index = i;
            ubahWarnaField.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    warnaList.set(dataIndex, warnaArray[index]);
                    pieDataSet.setColors(warnaList);
                    refreshData();
                    hideUbahWarna();
                }
            });
        }
    }
}