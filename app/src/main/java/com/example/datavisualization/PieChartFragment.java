package com.example.datavisualization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
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

    String[] kategoriArray, jenisKelaminArray, warnaNameArray;
    int[] warnaArray;

    ArrayList<Integer> warnaList;


    public PieChartFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;
        jenisKelamin = DEFAULT_JENIS_KELAMIN;
        jenisKelaminArray = DatasetKetenagakerjaanKabupaten.JENIS_KELAMIN;
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

        warnaNameArray = getContext().getResources().getStringArray(R.array.chart_warna_nama_array);
        warnaArray = getContext().getResources().getIntArray(R.array.chart_warna_array);

        getBundle();
        init();

        initChart();
        set();
        refreshData();

        ((TextView)getView().findViewById(R.id.chart_pie_title)).setText(getContext().getResources().getStringArray(R.array.chart_title)[kategori]);

        ImageButton ib = getView().findViewById(R.id.chart_keterangan_button);
        if(kategori == DatasetKetenagakerjaanKabupaten.UMUR || kategori == DatasetKetenagakerjaanKabupaten.PENDIDIKAN){
            LinearLayout ll = getView().findViewById(R.id.chart_keterangan_field);
            ll.removeAllViews();
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
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        //l.setXOffset(10f);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(45);
        pieChart.setCenterTextSize(14);
        pieChart.setDrawEntryLabels(false);
        pieChart.setExtraOffsets(25f,0,25f,0);
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

        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackground(getResources().getDrawable(R.drawable.background_white_button_selector));
        b.setTextSize(12);
        b.setTextColor(Color.BLACK);
        b.setText(warnaNameArray[index]);
        Drawable d = getResources().getDrawable(R.drawable.icon_warna);
        d.mutate();
        d.setColorFilter(new PorterDuffColorFilter(warnaArray[index], PorterDuff.Mode.SRC_IN));
        b.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
        return b;
    }

    private void set(){
        pieChart.highlightValues(null);
        hideUbahWarna();
        ArrayList<PieEntry> pieEntry = new ArrayList<>();
        ArrayList<Integer> dataEntry;

        warnaList = new ArrayList<>();

        if(jenisKelamin == 2){
            dataEntry = data.get(tahun, kategori);
        }
        else {
            dataEntry = data.get(tahun, jenisKelamin, kategori);
        }
        int jumlah = 0;
        for(int i = 0; i < dataEntry.size(); i++){
            jumlah += dataEntry.get(i);
        }
        for(int i = 0; i < dataEntry.size(); i++){
            pieEntry.add(new PieEntry(dataEntry.get(i), kategoriArray[i]));
            warnaList.add(warnaArray[i % warnaNameArray.length]);
        }
        pieChart.setCenterText("Jumlah Penduduk: " + jumlah);

        pieDataSet = new PieDataSet(pieEntry, "");
        pieDataSet.setColors(warnaList);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
    }
    private void refreshData(){
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
    }

    private void hideUbahWarna(){
        ((LinearLayout)getView().findViewById(R.id.chart_pie_warna)).removeAllViews();
    }
    private void showUbahWarna(int dataIndex){
        LinearLayout ll = getView().findViewById(R.id.chart_pie_warna);
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
    public void save(){
        if(pieChart.saveToGallery("pie_chart_" + System.currentTimeMillis(), 70)){
            Toast.makeText(getContext(), "Saved at DCIM folder", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Failed to icon_save Chart", Toast.LENGTH_SHORT).show();
        }
    }
}