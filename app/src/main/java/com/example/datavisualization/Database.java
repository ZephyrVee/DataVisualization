package com.example.datavisualization;

import android.os.Handler;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private FirebaseFirestore db;
    public DocumentReference database;
    DatasetKetenagakerjaan dataset;

    private static final String databaseNamePlain = "col_database";
    private static final String documentNamePlain = "doc_Skripsi221810477";

    //private final String akunCollection = Enkripsi.encrypt("col_akun");
    private final String tahunListDocument = Enkripsi.encrypt("tahun");

    private static final String databaseName = Enkripsi.encrypt(databaseNamePlain);
    private static final String documentName = Enkripsi.encrypt(documentNamePlain);

    private static final String dataDB = Enkripsi.encrypt("Ketenagakerjaan");


    public Database(){
        this.db = FirebaseFirestore.getInstance();
        this.database = db.collection(databaseName).document(documentName);

        dataset = new DatasetKetenagakerjaan();
        load();
    }

    public DocumentReference docRef(DocumentReference from, ArrayList<String> query){
        CollectionReference cr = from.collection(query.get(0));
        query.remove(0);
        if(query.size() > 1){
            DocumentReference dr = cr.document(query.get(0));
            query.remove(0);
            return docRef(dr, query);
        }
        return cr.document(query.get(0));
    }

    public synchronized void save(ArrayList<ArrayList<ArrayList<Integer>>> dataM, ArrayList<ArrayList<ArrayList<Integer>>> dataF, int tahun){
        Map<String, String> map = new HashMap<>();
        String tahunField = Enkripsi.encrypt(Integer.toString(tahun));
        map.put(tahunField, tahunField);

        database.collection(dataDB).document(tahunListDocument).set(map, SetOptions.merge());

        save(dataM, tahun, DatasetKetenagakerjaan.LAKI_LAKI);
        save(dataF, tahun, DatasetKetenagakerjaan.PEREMPUAN);
        dataset.set(dataM, tahun, DatasetKetenagakerjaan.LAKI_LAKI);
        dataset.set(dataF, tahun, DatasetKetenagakerjaan.PEREMPUAN);
    }
    public synchronized void save(ArrayList<ArrayList<ArrayList<Integer>>> data, int tahun, int gender){
        for(int i = 0; i < data.size(); i++) {
            Map<String, String> map = new HashMap<>();
            int idx = 0;
            for (ArrayList<Integer> d : data.get(i)) {
                for (Integer in : d) {
                    map.put(Enkripsi.encrypt(Integer.toString(idx)), Enkripsi.encrypt(Integer.toString(in)));
                    idx++;
                }
            }
            String tahunDocument = Enkripsi.encrypt(Integer.toString(tahun));
            String genderCollection = Enkripsi.encrypt(Integer.toString(gender));
            String indexDocument = Enkripsi.encrypt(Integer.toString(i));
            database.collection(dataDB).document(tahunDocument).collection(genderCollection).document(indexDocument).set(map, SetOptions.merge());
            map.clear();
        }
    }
    public synchronized void load(){
        database.collection(dataDB).document(tahunListDocument).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData() != null) {
                    Object[] tahun = documentSnapshot.getData().keySet().toArray();
                    for (Object o : tahun) {
                        String t = o.toString();
                        int th = Integer.parseInt(Enkripsi.decrypt(t));
                        if(!dataset.isTahunExist(th)){
                            dataset.newTahun(th);
                        }
                        loadByTahun(th, DatasetKetenagakerjaan.LAKI_LAKI);
                        loadByTahun(th, DatasetKetenagakerjaan.PEREMPUAN);
                    }
                }
            }
        });
    }
    public synchronized void loadByTahun(int tahun, int gender){
        for(int i = 0; i < DatasetKetenagakerjaan.table.size(); i++){
            ArrayList<ArrayList<Integer>> data = new ArrayList<>();
            String tahunDocument = Enkripsi.encrypt(Integer.toString(tahun));
            String genderCollection = Enkripsi.encrypt(Integer.toString(gender));
            String indexDocument = Enkripsi.encrypt(Integer.toString(i));
            final int idx = i;
            database.collection(dataDB).document(tahunDocument).collection(genderCollection).document(indexDocument).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int colSize = dataset.getSize(DatasetKetenagakerjaan.table.get(idx)[0]);
                    int rowSize = dataset.getSize(DatasetKetenagakerjaan.table.get(idx)[1]);
                    ArrayList<Integer> row = new ArrayList<>();
                    for(int i = 0; i < rowSize * colSize; i++){
                        String s = Enkripsi.decrypt( documentSnapshot.getString( Enkripsi.encrypt(Integer.toString(i)) ) );
                        row.add( Integer.parseInt(s) );
                        if(row.size() == rowSize){
                            data.add(row);
                            row.clear();
                        }
                    }
                    dataset.set(data, idx, tahun, gender);
                }
            });
        }
    }
}
