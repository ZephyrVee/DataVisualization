package com.example.datavisualization.database;

import com.example.datavisualization.Enkripsi;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatabaseKabupaten {
    private FirebaseFirestore ff;
    DocumentReference db;
    DatasetKetenagakerjaanKabupaten data;

    private static final String collectionName = Enkripsi.encrypt("col_database");
    private static final String documentName = Enkripsi.encrypt("doc_Skripsi221810477");
    private static final String databaseName = Enkripsi.encrypt("Ketenagakerjaan");
    private static final String tahunDocument = Enkripsi.encrypt("Tahun Jakarta Utara");

    public DatabaseKabupaten(){
        ff = FirebaseFirestore.getInstance();
        db = ff.collection(collectionName).document(documentName);
        data = new DatasetKetenagakerjaanKabupaten();
    }

    public void save(){
        for(DatasetKetenagakerjaanKabupaten.Tahun t : data.tahun){
            for(DatasetKetenagakerjaanKabupaten.Kabupaten kab : t.kabupaten) {
                for (DatasetKetenagakerjaanKabupaten.JenisKelamin jk : kab.jenisKelamin) {
                    for (DatasetKetenagakerjaanKabupaten.Kategori k : jk.kategori) {
                        Map<String, String> map = new HashMap<>();
                        for (int i = 0; i < k.value.size(); i++) {
                            map.put(Integer.toString(i), Enkripsi.encrypt(Integer.toString(k.value.get(i))));
                        }
                        //db.collection(databaseName).document()
                    }
                }
            }
        }
    }

    public void load(){

    }
}
