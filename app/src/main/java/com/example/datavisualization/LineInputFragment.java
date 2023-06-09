package com.example.datavisualization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

public class LineInputFragment extends Fragment {
    DatasetKetenagakerjaanKabupaten data;

    final int DEFAULT_KATEGORI = 0;
    Map<String, Integer> tahun, kategori;
    int k;

    public LineInputFragment() {
        // Required empty public constructor
        data = MainActivity.database.data;
        tahun = new HashMap<>();
        k = DEFAULT_KATEGORI;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineInit();
    }

    private void lineInit(){
        if(data.tahun.size() > 0){
            for(int i = 0; i < data.tahun.size(); i++){
                if(data.isComplete(i)){
                    //add checkbox
                }
            }
        }
    }

    private void addTahun(){

    }
}