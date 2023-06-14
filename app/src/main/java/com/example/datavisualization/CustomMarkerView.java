package com.example.datavisualization;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class CustomMarkerView extends MarkerView {

    private final TextView tv;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tv = findViewById(R.id.marker_text);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if(e instanceof BarEntry){
            tv.setText(Integer.toString((int)e.getY()));
        }
        else {
            tv.setText(Integer.toString((int)e.getY()));
        }


        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
    }
}
