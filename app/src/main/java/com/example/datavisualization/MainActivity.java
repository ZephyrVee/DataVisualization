package com.example.datavisualization;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static Map<String, Thread> thread;
    public static Database database;

    String databaseName;
    String documentname;
    String akunCol;

    TextView textView, tv, idT, passT;
    Intent barChart;
    Intent pieChart;
    Intent lineChart;
    ArrayList<String> content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //hide status bar

        setContentView(R.layout.activity_main);

        thread = new HashMap<>();
        database = new Database();

        //database = new Database().database;

        textView = (TextView) findViewById(R.id.openedFile);
        barChart = new Intent(getApplicationContext(), BarChartActivity.class);
        pieChart = new Intent(getApplicationContext(), PieChartActivity.class);
        lineChart = new Intent(getApplicationContext(), LineChartActivity.class);

        tv = (TextView) findViewById(R.id.textTestButton);
        idT = (TextView) findViewById(R.id.idLogin);
        passT = (TextView) findViewById(R.id.passLogin);

        findViewById(R.id.buttonOpenFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(null);
            }
        });

        findViewById(R.id.buttonBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EditDataActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.buttonPie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content != null){
                    pieChart.putExtra("table", content);
                    startActivity(pieChart);
                }
            }
        });

        findViewById(R.id.buttonLine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(content != null){
                    lineChart.putExtra("table", content);
                    startActivity(lineChart);
                }
            }
        });

        findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*
                String id = Enkripsi.encrypt("A01");
                String username = Enkripsi.encrypt("Admin");
                String password = Enkripsi.encrypt("admin123", true);
                Map<String, String> field = new HashMap<>();
                field.put(Key.USERNAME.key(), username);
                field.put(Key.PASSWORD.key(), password);

                database.collection(Key.TABEL_AKUN.key()).document(id).set(field);
                 */

                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });

        findViewById(R.id.visualizeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VisualisasiActivity.class);
                startActivity(i);
            }
        });



        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED
        );
    }

    public void openFile(Uri pickerInitialUri){
        int PICK_PDF_FILE = 2;

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_PDF_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 2


                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                Intent in = new Intent(getApplicationContext(), LineChartActivity.class);

                try {
                    uri = resultData.getData();
                    // Perform operations on the document using its URI.
                    content = new ArrayList<>();
                    content = readTextFromUri(uri);

                    String str = uri.getPath();
                    int i = str.length() - 1;
                    while((str.charAt(i) != 92) && (str.charAt(i) != 58) && (str.charAt(i) != 47)){
                        //92 = \
                        //58 = :
                        //47 = /
                        i--;
                    }
                    String fileName = uri.getPath().substring(i + 1);
                    textView.setText(fileName);
                }
                catch(Exception e){

                }
            }
        }
    }

    private ArrayList<String> readTextFromUri(Uri uri) throws IOException {
        ArrayList<String> content = new ArrayList<>();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
        }
        return content;
    }
}