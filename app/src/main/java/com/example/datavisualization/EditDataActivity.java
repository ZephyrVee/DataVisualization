package com.example.datavisualization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EditDataActivity extends AppCompatActivity {

    DatasetKetenagakerjaan data;

    ArrayList<GridLayout> arrayGridM, arrayGridF;
    String[] title;
    ArrayList<int[]> table;
    int tableM = 1;
    int tableF = 1;
    HorizontalScrollView gridM, gridF;

    float dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        dp = this.getResources().getDisplayMetrics().density; // this is a scale from dp to int (uses + 0.5f)
        data = new DatasetKetenagakerjaan();

        gridM = (HorizontalScrollView) findViewById(R.id.gridM);
        gridF = (HorizontalScrollView) findViewById(R.id.gridF);

        gridM.addView(newGrid(DatasetKetenagakerjaan.LAKI_LAKI, 1,2));
        //initTable();

        this.findViewById(R.id.tahunPopupM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(0,view);
            }
        });
    }

    private void showMenu(int gender, View view){
        PopupMenu menu = new PopupMenu(EditDataActivity.this, view);
        menu.inflate(R.menu.kategori_popup);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        menu.show();
    }

    private void changeTable(int gender){
        HorizontalScrollView grid;
        if(gender == DatasetKetenagakerjaan.LAKI_LAKI){
            grid = gridM;
        }
        else {
            grid = gridF;
        }
    }
    private void initGrid(){
        arrayGridM = new ArrayList<>();
        arrayGridF = new ArrayList<>();

        int[] from = new int[]{
                DatasetKetenagakerjaan.UMUR,
                DatasetKetenagakerjaan.KABUPATEN,
                DatasetKetenagakerjaan.PENDIDIKAN,
                DatasetKetenagakerjaan.JAM_KERJA,
                DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA,
                DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA,
                DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA
        };

        ArrayList<int[]> destination = new ArrayList<>();
        destination.add(new int[]{ //UMUR
                DatasetKetenagakerjaan.PENDIDIKAN, //0
                DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, //1
                DatasetKetenagakerjaan.JAM_KERJA, //2
                DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, //3
                DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, //4
                DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, //5
                DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN //6
        });
        destination.add(new int[]{ //KABUPATEN
                DatasetKetenagakerjaan.UMUR, //7
                DatasetKetenagakerjaan.PENDIDIKAN, //8
                DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, //9
                DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, //10
                DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, //11
                DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN //12
        });
        destination.add(new int[]{ //PENDIDIKAN
                DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, //13
                DatasetKetenagakerjaan.JAM_KERJA, //14
                DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, //15
                DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN //16
        });
        destination.add(new int[]{ //JAM KERJA
                DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, //17
                DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA //18
        });
        destination.add(new int[]{ //LAPANGAN PEKERJAAN UTAMA
                DatasetKetenagakerjaan.PENDIDIKAN, //19
                DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA //20
        });
        destination.add(new int[]{ //STATUS PEKERJAAN UTAMA
                DatasetKetenagakerjaan.PENDIDIKAN, //21
                DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, //22
                DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA //23
        });
        destination.add(new int[]{  //JENIS PEKERJAAN UTAMA
                DatasetKetenagakerjaan.PENDIDIKAN //24
        });

        for(int i = 0; i < from.length; i++){
            for(int d : destination.get(i)){
                arrayGridM.add(newGrid(DatasetKetenagakerjaan.LAKI_LAKI, from[i], d));
                arrayGridF.add(newGrid(DatasetKetenagakerjaan.LAKI_LAKI, from[i], d));
            }
        }
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
        table.add(new int[]{0, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{1, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{7, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{13, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{8, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.NONE});

        table.add(new int[]{9, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{0, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        table.add(new int[]{7, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        table.add(new int[]{8, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        table.add(new int[]{0, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{2, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{3, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{4, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{19, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{21, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{14, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{17, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{18, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{22, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{24, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{20, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{23, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{17, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PADA_PEKERJAAN_UTAMA});
        table.add(new int[]{18, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PADA_PEKERJAAN_UTAMA});
        table.add(new int[]{7, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.BEKERJA});

        table.add(new int[]{8, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{10, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{11, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        table.add(new int[]{0, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{8, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});

        table.add(new int[]{5, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{15, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{12, DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        table.add(new int[]{16, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN, DatasetKetenagakerjaan.NONE});
        table.add(new int[]{6, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN, DatasetKetenagakerjaan.NONE});
    }

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

    private GridLayout newGrid(int gender, int from, int destination){
        GridLayout grid = new GridLayout(this);

        //Add Header
        if (destination == DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN){
            grid.setColumnCount(DatasetKetenagakerjaan.SKK_LIST.length + 1);
            grid.addView(addTextViewToGrid(data.getNama(from), 3, 1));
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
            grid.setColumnCount(data.getSize(destination) + 1);
            grid.addView(addTextViewToGrid(data.getNama(from), 2, 1));
            grid.addView(addTextViewToGrid(data.getNama(destination), 1, data.getSize(destination)));
            for(String list : data.getList(destination)){
                grid.addView(addTextViewToGrid(list));
            }
        }
        //Add Header

        for(int i = 0; i < data.getList(from).length; i++){
            grid.addView(addTextViewToGrid(data.getList(from)[i]));
            for(int j = 0; j < data.getList(destination).length; j++){
                grid.addView(addEditTextToGrid());
            }
        }
        return grid;
    }
}