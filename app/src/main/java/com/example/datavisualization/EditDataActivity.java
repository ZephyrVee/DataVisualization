package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EditDataActivity extends AppCompatActivity {

    DatasetKetenagakerjaan data;

    HorizontalScrollView gridM, gridF;
    ArrayList<GridLayout> arrayGridM, arrayGridF;
    ArrayList<ArrayList<ArrayList<EditText>>> cellM, cellF;
    String[] title;
    ArrayList<int[]> table;
    TextView titleM, titleF, editDataTableIndexM, editDataTableIndexF;
    Button tahunPopup;

    int tableM = 0;
    int tableF = 0;

    ProgressBar pb;
    Handler handler;

    float dp;

    ArrayList<ArrayList<Integer>> test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        handler = new Handler();

        test = new ArrayList<>();

        Thread thread = new Thread(() -> { //new Runnable can be replaced by lambda { () -> }
            initAll();
            runOnUiThread(this::initUI); //Lambda can be replaced by method reference { this::<MethodName> }
        });
        thread.start();
    }

    private void initAll(){
        findViewById(R.id.editView).setVisibility(View.INVISIBLE);

        dp = this.getResources().getDisplayMetrics().density; // this is a scale from dp to int (uses + 0.5f)

        data = new DatasetKetenagakerjaan();

        titleM = findViewById(R.id.titleM);
        titleF = findViewById(R.id.titleF);
        editDataTableIndexM = findViewById(R.id.edit_data_table_index_m);
        editDataTableIndexF = findViewById(R.id.edit_data_table_index_f);
        tahunPopup = findViewById(R.id.tahunPopup);

        pb = findViewById(R.id.pb);
        pb.setMax(70);
        pb.setProgress(0);

        gridM = findViewById(R.id.gridM);
        gridF = findViewById(R.id.gridF);

        initTable();
        initCell();
        initGrid();
    }
    //These Functions Only Called in Thread
    private void initUI(){
        changeTable(DatasetKetenagakerjaan.LAKI_LAKI, tableM);
        changeTable(DatasetKetenagakerjaan.PEREMPUAN, tableF);

        findViewById(R.id.tahunPopup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view);
            }
        });

        findViewById(R.id.leftM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableM -= 1;
                changeTable(DatasetKetenagakerjaan.LAKI_LAKI, tableM);
            }
        });

        findViewById(R.id.rightM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableM += 1;
                changeTable(DatasetKetenagakerjaan.LAKI_LAKI, tableM);
            }
        });

        findViewById(R.id.leftF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableF -= 1;
                changeTable(DatasetKetenagakerjaan.PEREMPUAN, tableF);
            }
        });

        findViewById(R.id.rightF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableF += 1;
                changeTable(DatasetKetenagakerjaan.PEREMPUAN, tableF);
            }
        });

        findViewById(R.id.edit_data_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                fillTable(DatasetKetenagakerjaan.PEREMPUAN);
            }
        });

        ((ConstraintLayout)findViewById(R.id.loadingView).getParent()).removeView(findViewById(R.id.loadingView));
        findViewById(R.id.editView).setVisibility(View.VISIBLE);
    }
    private void initTable(){
        title = new String[]{
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT GOLONGAN UMUR DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT GOLONGAN UMUR DAN JENIS KEGIATAN SELAMA SEMINGGU YANG LALU, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT KABUPATEN/KOTA DAN GOLONGAN UMUR DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT PENDIDIKAN TERTINGGI YANG DITAMATKAN DAN JENIS KEGIATAN SELAMA SEMINGGU YANG LALU, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT KABUPATEN/KOTA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",

                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT KABUPATEN/KOTA DAN JENIS KEGIATAN SELAMA SEMINGGU YANG LALU, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK ANGKATAN KERJA MENURUT GOLONGAN UMUR DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK ANGKATAN KERJA MENURUT KABUPATEN/KOTA DAN GOLONGAN UMUR, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK ANGKATAN KERJA MENURUT KABUPATEN/KOTA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT GOLONGAN UMUR DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",

                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT GOLONGAN UMUR DAN JUMLAH JAM KERJA SELURUHNYA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT GOLONGAN UMUR DAN LAPANGAN PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT GOLONGAN UMUR DAN STATUS PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT LAPANGAN PEKERJAAN UTAMA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA 2022",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT STATUS PEKERJAAN UTAMA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",

                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT PENDIDIKAN TERTINGGI YANG DITAMATKAN DAN JUMLAH JAM KERJA SELURUHNYA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT JUMLAH JAM KERJA SELURUHNYA DAN LAPANGAN PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT JUMLAH JAM KERJA SELURUHNYA DAN STATUS PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT STATUS PEKERJAAN UTAMA DAN LAPANGAN PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT JENIS PEKERJAAN UTAMA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",

                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT LAPANGAN PEKERJAAN UTAMA DAN JENIS PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT STATUS PEKERJAAN UTAMA DAN JENIS PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT JUMLAH JAM KERJA PADA PEKERJAAN UTAMA DAN LAPANGAN PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT JUMLAH JAM KERJA PADA PEKERJAAN UTAMA, DAN STATUS PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT KABUPATEN/KOTA DAN GOLONGAN UMUR, DKI JAKARTA",

                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT KABUPATEN/KOTA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT KABUPATEN/KOTA DAN LAPANGAN PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG BEKERJA MENURUT KABUPATEN/KOTA DAN STATUS PEKERJAAN UTAMA, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK PENGANGGURAN TERBUKA *) MENURUT GOLONGAN UMUR DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK PENGANGGURAN TERBUKA *) MENURUT KABUPATEN/KOTA DAN PENDIDIKAN TERTINGGI YANG DITAMATKAN, DKI JAKARTA",

                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK PENGANGGURAN TERBUKA MENURUT GOLONGAN UMUR DAN KATEGORI PENGANGGURAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK PENGANGGURAN TERBUKA MENURUT PENDIDIKAN TERTINGGI YANG DITAMATKAN DAN KATEGORI PENGANGGURAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS YANG TERMASUK PENGANGGURAN TERBUKA MENURUT KABUPATEN/KOTA DAN KATEGORI PENGANGGURAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT PENDIDIKAN TERTINGGI YANG DITAMATKAN DAN KLASIFIKASI PENGANGGURAN, DKI JAKARTA",
                "PENDUDUK BERUMUR 15 TAHUN KE ATAS MENURUT GOLONGAN UMUR DAN KLASIFIKASI PENGANGGURAN, DKI JAKARTA"
        };
        table = new ArrayList<>();
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.NONE});

        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PADA_PEKERJAAN_UTAMA});
        table.add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PADA_PEKERJAAN_UTAMA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});

        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN, DatasetKetenagakerjaan.NONE});
    }
    private void initCell(){
        cellM = new ArrayList<>();
        cellF = new ArrayList<>();
        for(int[] t : table){
            cellM.add(newEditTextCell(t));
            cellF.add(newEditTextCell(t));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pb.incrementProgressBy(1);
                }
            });
        }
    }
    private void initGrid(){
        arrayGridM = new ArrayList<>();
        arrayGridF = new ArrayList<>();

        for(int i = 0; i < table.size(); i++){
            arrayGridM.add(newGrid(table.get(i), cellM.get(i)));
            arrayGridF.add(newGrid(table.get(i), cellF.get(i)));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pb.incrementProgressBy(1);
                }
            });
        }
    }
    //These Functions Only Called in Thread

    private int dpToInt(int d){
        int dps = (int)(d*dp + 0.5f);
        return dps;
    }

    private TextView addTextViewToGrid(String str){
        return addTextViewToGrid(str, 1, 1);
    }
    private TextView addTextViewToGrid(String str, int rowSpan, int colSpan){
        TextView tv = new TextView(this);
        GridLayout.LayoutParams pr = new GridLayout.LayoutParams();
        pr.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, rowSpan);
        pr.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, colSpan);
        pr.setGravity(Gravity.FILL);

        tv.setLayoutParams(pr);
        tv.setGravity(Gravity.CENTER);

        int pd = dpToInt(8);
        tv.setPadding(pd, pd, pd, pd);
        tv.setBackground(ContextCompat.getDrawable(this, R.drawable.cell));

        tv.setText(str);
        return tv;
    }
    private EditText addEditTextToGrid(){
        EditText et = new EditText(this);
        GridLayout.LayoutParams pr = new GridLayout.LayoutParams();
        pr.setGravity(Gravity.FILL);

        et.setLayoutParams(pr);

        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        et.setGravity(Gravity.CENTER);

        int pd = dpToInt(8);
        et.setPadding(pd, pd, pd, pd);
        et.setBackground(ContextCompat.getDrawable(this, R.drawable.cell));

        return et;
    }
    private EditText addEditTextToGrid(String str){
        EditText et = addEditTextToGrid();
        et.setText(str);
        return et;
    }
    private ArrayList<ArrayList<EditText>> newEditTextCell(int[] table){
        ArrayList<ArrayList<EditText>> cell = new ArrayList<>();
        for(int i = 0; i < data.getSize(table[0]); i++){
            ArrayList<EditText> row = new ArrayList<>();
            for(int j = 0; j < data.getSize(table[1]); j++){
                row.add(addEditTextToGrid());
            }
            cell.add(row);
        }
        return cell;
    }
    private GridLayout newGrid(int[] table, ArrayList<ArrayList<EditText>> cell){
        GridLayout grid = new GridLayout(this);

        //Add Header
        if (table[1] == DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN){
            grid.setColumnCount(DatasetKetenagakerjaan.SKK_LIST.length + 1);
            grid.addView(addTextViewToGrid(data.getNama(table[0]), 3, 1));
            grid.addView(addTextViewToGrid("Angkatan Kerja", 1, 3));
            grid.addView(addTextViewToGrid("Bukan Angkatan Kerja", 1, 3));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[0], 2, 1));
            grid.addView(addTextViewToGrid("Pengangguran", 1, 2));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[3], 2, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[4], 2, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[5], 2, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[1], 1, 1));
            grid.addView(addTextViewToGrid(DatasetKetenagakerjaan.SKK_LIST[2], 1, 1));

        }
        else {
            grid.setColumnCount(data.getSize(table[1]) + 1);
            grid.addView(addTextViewToGrid(data.getNama(table[0]), 2, 1));
            grid.addView(addTextViewToGrid(data.getNama(table[1]), 1, data.getSize(table[1])));
            for(String list : data.getList(table[1])){
                grid.addView(addTextViewToGrid(list));
            }
        }
        //Add Header

        for(int i = 0; i < cell.size(); i++){
            grid.addView(addTextViewToGrid(data.getList(table[0])[i]));
            for(EditText et : cell.get(i)){
                grid.addView(et);
            }
        }
        return grid;
    }

    private void showMenu(View view){
        PopupMenu menu = new PopupMenu(EditDataActivity.this, view);
        if(data.T.size() > 0){
            for(int i = 0; i < data.T.size(); i++){
                menu.getMenu().add(Integer.toString(data.T.get(i).tahun));
            }
        }
        menu.getMenu().add("Tambah ...");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        menu.show();
    }
    private void changeTable(int gender, int g){
        if(gender == DatasetKetenagakerjaan.LAKI_LAKI){
            gridM.removeAllViews();
            gridM.addView(arrayGridM.get(g));
            if(tableM == 0){
                findViewById(R.id.leftM).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.leftM).setVisibility(View.VISIBLE);
            }

            if(tableM == 34){
                findViewById(R.id.rightM).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.rightM).setVisibility(View.VISIBLE);
            }

            titleM.setText(title[tableM]);
            String s = (tableM + 1) + "/35";
            editDataTableIndexM.setText(s);
        }
        else {
            gridF.removeAllViews();
            gridF.addView(arrayGridF.get(g));
            if(tableF == 0){
                findViewById(R.id.leftF).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.leftF).setVisibility(View.VISIBLE);
            }

            if(tableF == 34){
                findViewById(R.id.rightF).setVisibility(View.INVISIBLE);
            }
            else {
                findViewById(R.id.rightF).setVisibility(View.VISIBLE);
            }

            titleF.setText(title[tableF]);
            String s = (tableF + 1) + "/35";
            editDataTableIndexF.setText(s);
        }
    }
    private void fillTable(int gender){
        GridLayout grid;
        if(gender == DatasetKetenagakerjaan.LAKI_LAKI){
            grid = arrayGridM.get(tableM);
        }
        else {
            grid = arrayGridF.get(tableF);
        }
        int a = 0;
        int b = 0;
        for(int i = 0; i < grid.getChildCount(); i++){
            try{
                EditText et = (EditText) grid.getChildAt(i);
                et.setText(test.get(a).get(b).toString());
                b += 1;
                if(b == (test.get(a).size() - 1)){
                    a += 1;
                    b = 0;
                }
            }
            catch (Exception e){ continue; }
        }
    }
    private void saveData(){
        //for(int i = 0; i < table.size(); i++){
            int max = data.getSize(table.get(tableM)[2]);
            int c = 0;
            ArrayList<Integer> column = new ArrayList<>();
            for(int j = 0; j < arrayGridM.get(tableM).getChildCount(); j++){
                try{
                    String value = ((EditText) arrayGridM.get(tableM).getChildAt(j)).toString();

                    if(c == 0){ column = new ArrayList<>(); }
                    if(value.trim().length() == 0){
                        column.add(-1);
                    }
                    else{
                        column.add(Integer.parseInt(value));
                    }
                    if(c == (max - 1)){
                        test.add(column);
                        c = 0;
                    }
                    else {
                        c += 1;
                    }
                }
                catch (Exception e){ continue; }
            }
        //}
    }
}