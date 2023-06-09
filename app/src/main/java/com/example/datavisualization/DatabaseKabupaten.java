package com.example.datavisualization;

import android.content.Context;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseKabupaten {
    private FirebaseFirestore ff;
    DocumentReference db;
    CollectionReference akun;

    DatasetKetenagakerjaanKabupaten data;

    private static final String akunCollection = Enkripsi.encrypt("col_akun");

    private static final String collectionName = Enkripsi.encrypt("col_database");
    private static final String documentName = Enkripsi.encrypt("doc_Skripsi221810477");
    private static final String databaseName = Enkripsi.encrypt("Ketenagakerjaan");
    private static final String tahunDocument = Enkripsi.encrypt("Tahun");

    boolean retrieved = false;
    boolean noConnection = false;
    int tahunLength = 0;
    int tahunLoaded = 0;

    public DatabaseKabupaten(){
        init();
    }

    public synchronized void init(){
        ff = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder(ff.getFirestoreSettings()).setPersistenceEnabled(false).build();
        ff.setFirestoreSettings(settings);

        db = ff.collection(collectionName).document(documentName);
        akun = db.collection(akunCollection);
        data = new DatasetKetenagakerjaanKabupaten();
        load();
    }

    public synchronized void save(){
        Map<String, String> m = new HashMap<>();
        for(DatasetKetenagakerjaanKabupaten.Tahun t : data.tahun){
            m.put(Enkripsi.encrypt(Integer.toString(t.value)), "0");
            for(DatasetKetenagakerjaanKabupaten.Kabupaten kab : t.kabupaten) {
                for (DatasetKetenagakerjaanKabupaten.JenisKelamin jk : kab.jenisKelamin) {
                    for (DatasetKetenagakerjaanKabupaten.Kategori k : jk.kategori) {
                        Map<String, String> map = new HashMap<>();
                        for (int i = 0; i < k.value.size(); i++) {
                            map.put(Integer.toString(i), Enkripsi.encrypt(Integer.toString(k.value.get(i))));
                        }
                        db.collection(Enkripsi.encrypt(Integer.toString(t.value))).document(Integer.toString(kab.index)).collection(Integer.toString(jk.index)).document(Integer.toString(k.index)).set(map, SetOptions.merge());
                    }
                }
            }
            db.collection(databaseName).document(tahunDocument).set(m, SetOptions.merge());
        }
    }
    public synchronized void load(){
        noConnection = false;
        db.collection(databaseName).document(tahunDocument).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Object[] object = documentSnapshot.getData().keySet().toArray();
                    tahunLength = object.length;
                    for(Object o : object){
                        String s = o.toString();
                        int t = Integer.parseInt(Enkripsi.decrypt(s));
                        data.newTahun(t);
                        load(t);
                        if(noConnection){
                            break;
                        }
                    }
                    if(tahunLoaded == tahunLength){
                        retrieved = true;
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                noConnection = true;
            }
        });
    }
    public synchronized void load(int t){
        for(int i = 0; i < data.tahun.get(data.getTahunIndex(t)).kabupaten.size(); i++){
            final int fi = i;
            for(int j = 0; j < data.tahun.get(data.getTahunIndex(t)).kabupaten.get(i).jenisKelamin.size(); j++){
                final int fj = j;
                for(int k = 0; k < data.tahun.get(data.getTahunIndex(t)).kabupaten.get(i).jenisKelamin.get(j).kategori.size(); k++){
                    final int fk = k;
                    db.collection(Enkripsi.encrypt(Integer.toString(data.tahun.get(data.getTahunIndex(t)).value))).document(Integer.toString(i)).collection(Integer.toString(j)).document(Integer.toString(k)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, Object> map = documentSnapshot.getData();
                            ArrayList<Integer> al = new ArrayList<>();
                            if(!map.isEmpty()){
                                for(int p = 0; p < map.size(); p++){
                                    String s = (String) map.get(Integer.toString(p));
                                    int n = Integer.parseInt(Enkripsi.decrypt(s));
                                    al.add(n);
                                }
                                data.set(al, t, fj, fk);
                            }
                            noConnection = false;
                            tahunLoaded++;
                            if(tahunLoaded == tahunLength){
                                retrieved = true;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            noConnection = true;
                        }
                    });
                    if(noConnection){
                        return;
                    }
                }
            }
        }
    }
    public void delete(int tahun){
        Map<String, Object> del = new HashMap<>();
        del.put(Enkripsi.encrypt(Integer.toString(tahun)), FieldValue.delete());
        db.collection(databaseName).document(tahunDocument).update(del);
    }
}
