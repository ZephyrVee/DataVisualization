package com.example.datavisualization;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
    public static Database database1;
    public static DatabaseKabupaten database;

    Intent barChart;
    Intent pieChart;
    Intent lineChart, kelolaDataActivity, loginActivity;
    ArrayList<String> content = null;
    ActivityResultLauncher<Intent> login = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Button kelolaDataButton = new Button(MainActivity.this);
                        kelolaDataButton.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        kelolaDataButton.setText("Kelola data");
                        kelolaDataButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(kelolaDataActivity);
                            }
                        });
                        RelativeLayout rl = findViewById(R.id.main_kelola_data_button_field);
                        rl.addView(kelolaDataButton);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        thread = new HashMap<>();
        database = new DatabaseKabupaten();
        barChart = new Intent(getApplicationContext(), BarChartActivity.class);
        pieChart = new Intent(getApplicationContext(), PieChartActivity.class);
        lineChart = new Intent(getApplicationContext(), LineChartActivity.class);
        kelolaDataActivity = new Intent(getApplicationContext(), KelolaDataActivity.class);
        loginActivity = new Intent(getApplicationContext(), LoginActivity.class);



        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED
        );
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showMenu();
        return true;
    }

    private void showMenu(){
        PopupMenu menu = new PopupMenu(MainActivity.this, MainActivity.this.findViewById(R.id.account));
        menu.getMenu().add("Login");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("Login")){
                    login.launch(loginActivity);
                }
                return true;
            }
        });
        menu.show();
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