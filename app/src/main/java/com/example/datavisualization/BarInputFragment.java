package com.example.datavisualization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BarInputFragment extends Fragment {

    public BarInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bar_input, container, false);
    }

    public LinearLayout addDataset(){
        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(llpr);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.START);


        return ll;
    }
}