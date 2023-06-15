package com.example.datavisualization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

public class SatuTahunInputFragment extends Fragment {
    final int DEFAULT_KATEGORI = 0;
    final int DEFAULT_TAHUN = 2022;

    DatasetKetenagakerjaanKabupaten data;
    int kategori, tahun;
    Button kategoriButton;
    RadioGroup radioGroup;

    public SatuTahunInputFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        kategori = DEFAULT_KATEGORI;
        tahun = DEFAULT_TAHUN;
        setBundle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_satu_tahun_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();

        kategoriButton = getView().findViewById(R.id.input_2_kategori);
        kategoriButton.setText("Kategori: " + DatasetKetenagakerjaanKabupaten.KATEGORI[kategori]);
        kategoriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                    menu.getMenu().add(DatasetKetenagakerjaanKabupaten.KATEGORI[i]);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String selected = menuItem.getTitle().toString();
                        for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length ; i++){
                            if(selected.equals(DatasetKetenagakerjaanKabupaten.KATEGORI[i])){
                                kategori = i;
                                kategoriButton.setText("Kategori: " + selected);
                                setBundle();
                                break;
                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

        radioGroup = getView().findViewById(R.id.input_2_tahun);
        if(data.tahun.size() > 0){
            for(int i = 0; i < data.tahun.size(); i++){
                String s = data.tahun.get(i).value.toString();
                int t = data.tahun.get(i).value;
                if(data.isComplete(t)) {
                    addRadioButton(t);
                }
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tahun = checkedId;
                setBundle();
            }
        });
    }

    private void addRadioButton(int t){
        RadioGroup.LayoutParams rbpr = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RadioButton rb = new RadioButton(getContext());
        rb.setText(Integer.toString(t));
        rb.setId(t);
        if(t == tahun){
            rb.setChecked(true);
        }

        int index = radioGroup.getChildCount();
        radioGroup.addView(rb, index, rbpr);
    }

    private void setBundle(){
        Bundle bundle = new Bundle();
        bundle.putInt("kategori", kategori);
        bundle.putInt("tahun", tahun);
        setArguments(bundle);
    }
    private void getBundle(){
        kategori = getArguments().getInt("kategori");
        tahun = getArguments().getInt("tahun");
    }
}