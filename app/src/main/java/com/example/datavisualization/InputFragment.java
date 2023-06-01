package com.example.datavisualization;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class InputFragment extends Fragment {
    final String chart;

    DatasetKetenagakerjaanKabupaten data;

    Button pilihButton;
    ImageButton addButton;
    LinearLayout fields;
    int kategori;
    ArrayList<Integer> tahun, color, colorList, jenisKelamin;
    String[] colorName;

    public InputFragment(String c) {
        // Required empty public constructor
        chart = c;
        kategori = -1;
        data = MainActivity.database.data;
        tahun = new ArrayList<>();
        jenisKelamin = new ArrayList<>();
        color = new ArrayList<>();
        colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.CYAN);
        colorList.add(Color.DKGRAY);
        colorList.add(Color.GRAY);
        colorList.add(Color.GREEN);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.RED);
        colorList.add(Color.YELLOW);
        colorName = new String[]{"Biru", "Hitam", "Cyan", "Abu-abu gelap", "Abu-abu", "Hijau", "Magenta", "Merah", "Kuning"};
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fields = getView().findViewById(R.id.visualisasi_linear_layout);
        pilihButton = getView().findViewById(R.id.visualisasi_pilih_button);
        addButton = getView().findViewById(R.id.visualisasi_add_button);
        addButton.setVisibility(View.INVISIBLE);
        if(chart.equals("Bar")){
            barInit();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton.setVisibility(View.INVISIBLE);
                if(chart.equals("Bar")){
                    addBar(fields.getChildCount());
                }
            }
        });
    }

    @Override
    public void onDestroyView() { // Tab changed
        super.onDestroyView();
        pilihButton = null;
        addButton = null;
        setBundle();
    }

    private void barInit(){
        System.out.println("init");
        pilihButton.setOnClickListener(new View.OnClickListener() {
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
                                pilihButton.setText(selected);
                                addButton.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    public void addBar(int idx){
        tahun.add(-1);
        jenisKelamin.add(-1);
        color.add(-1);

        LinearLayout ll = new LinearLayout(getContext());
        LinearLayout.LayoutParams llpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(llpr);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.START);

        ll.addView(addPilihTahun(idx));
        ll.addView(addPilihJK(idx));
        ll.addView(addWarna());
        ll.addView(addPilihWarna(idx));
        fields.addView(ll);
    }
    private Button addPilihTahun(int idx){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        int m = intToDp(8);
        bpr.setMargins(0, m, m, m);
        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackgroundResource(R.drawable.grid);
        b.setText("Pilih Tahun");
        b.setTextSize(10);
        b.setPadding(0, 0, 0, 0);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                if(data.tahun.size() > 0){
                    for(int i = 0; i < data.tahun.size(); i++){
                        menu.getMenu().add(Integer.toString(data.tahun.get(i).value));
                    }
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        tahun.set(idx, Integer.parseInt(menuItem.getTitle().toString()));
                        b.setText(menuItem.getTitle().toString());
                        ((LinearLayout)b.getParent()).getChildAt(1).setVisibility(View.VISIBLE);
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }
    private Button addPilihJK(int idx){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        int m = intToDp(8);
        bpr.setMargins(m, m, m, m);
        Button b = new Button(getContext());
        b.setLayoutParams(bpr);
        b.setBackgroundResource(R.drawable.grid);
        b.setText("Jenis Kelamin");
        b.setTextSize(10);
        b.setPadding(0, 0, 0, 0);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                menu.getMenu().add(0, 0, 0, "Laki-laki");
                menu.getMenu().add(0, 1, 1, "Perempuan");
                menu.getMenu().add(0, 2, 2, "Laki-laki + Perempuan");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        jenisKelamin.set(idx, menuItem.getItemId());
                        b.setText(menuItem.getTitle().toString());
                        ((LinearLayout)b.getParent()).getChildAt(2).setVisibility(View.VISIBLE);
                        ((LinearLayout)b.getParent()).getChildAt(3).setVisibility(View.VISIBLE);
                        return true;
                    }
                });
                menu.show();
            }
        });
        return b;
    }
    private TextView addWarna(){
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams tvpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tvpr.setMargins(intToDp(16), 0, intToDp(8), 0);
        tv.setLayoutParams(tvpr);
        tv.setTextSize(12);
        tv.setGravity(Gravity.CENTER);
        tv.setText("Warna:");
        tv.setVisibility(View.INVISIBLE);
        return tv;
    }
    private ImageButton addPilihWarna(int idx){
        LinearLayout.LayoutParams bpr = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bpr.gravity = Gravity.CENTER;
        int m = intToDp(8);
        bpr.setMargins(m, m, 0, m);
        ImageButton c = new ImageButton(getContext());
        c.setLayoutParams(bpr);
        c.setBackgroundResource(R.drawable.grid);
        c.setPadding(0, 0, 0, 0);
        c.setBackgroundResource(R.drawable.warna);
        c.setVisibility(View.INVISIBLE);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getContext(), view);
                for(int i = 0; i < colorList.size(); i++){
                    menu.getMenu().add(0, i, i, menuIconWithText(getResources().getDrawable(R.drawable.warna), i, colorName[i]));
                }
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        color.set(idx, colorList.get(item.getItemId()));
                        c.getBackground().setColorFilter(new PorterDuffColorFilter(colorList.get(item.getItemId()), PorterDuff.Mode.MULTIPLY));
                        setBundle();
                        addButton.setVisibility(View.VISIBLE);
                        return true;
                    }
                });
            }
        });
        return c;
    }

    private void setBundle(){
        Bundle bundle = new Bundle();
        if(chart.equals("Bar")){
            bundle.putInt("kategori", kategori);
            bundle.putIntegerArrayList("tahun", tahun);
            bundle.putIntegerArrayList("jenis_kelamin", jenisKelamin);
            bundle.putIntegerArrayList("warna", color);
        }
        else if(chart.equals("Line")){

        }
        setArguments(bundle);
    }

    private int intToDp(int d) {
        return (int) (d * this.getResources().getDisplayMetrics().density + 0.5f);
    }
    private CharSequence menuIconWithText(Drawable r, int color, String title) {
        r.setColorFilter(colorList.get(color), PorterDuff.Mode.MULTIPLY);

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}