package com.example.datavisualization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatasetKetenagakerjaan {

    public static final int LAKI_LAKI = 0;
    public static final int PEREMPUAN = 1;

    public static final int UMUR = 1;
    public static final int PENDIDIKAN = 2;
    public static final int KABUPATEN = 3;
    public static final int STATUS_KEADAAN_KETENAGAKERJAAN = 4;
    public static final int JAM_KERJA = 5;
    public static final int LAPANGAN_PEKERJAAN_UTAMA = 6;
    public static final int STATUS_PEKERJAAN_UTAMA = 7;
    public static final int JENIS_PEKERJAAN_UTAMA = 8;
    public static final int KATEGORI_PENGANGGURAN = 9;
    public static final int KLASIFIKASI_PENGANGGURAN = 10;

    public static final int NONE = 0;
    public static final int ANGKATAN_KERJA = 1;
    public static final int BEKERJA = 2;
    public static final int PADA_PEKERJAAN_UTAMA = 3;
    public static final int PENGANGGURAN_TERBUKA = 4;

    public static final String[] UMUR_LIST = new String[] { "15 - 19", "20 - 24", "25 - 29", "30 - 34", "35 - 39", "40 - 44", "45 - 49", "50 - 54", "55 - 59", "60+"};
    public static final String[] PENDIDIKAN_LIST = new String[] {"Tidak/Belum Tamat SD", "SD", "SMP", "SMA", "SMK", "Diploma I/II/III", "Universitas"};
    public static final String[] KABUPATEN_LIST = new String[] {"Kep. Seribu", "Jakarta Selatan", "Jakarta Timur", "Jakarta Pusat", "Jakarta Barat", "Utara"};
    public static final String[] SKK_LIST = new String[] {"Bekerja", "Pernah Bekerja", "Tidak Pernah Bekerja", "Sekolah", "Mengurus Rumah Tangga", "Lainnya"};
    public static final String[] JAM_KERJA_LIST = new String[] {"0*", "1 - 4", "5 - 9", "10 - 14", "15 - 19", "20 - 24", "25 - 34", "35 - 44", "45 - 54", "55 - 59", "60 - 74", "75+"};
    public static final String[] LPU_LIST = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};
    public static final String[] SPU_LIST = new String[]{"1", "2", "3", "4", "5", "6", "7"};
    public static final String[] JPU_LIST = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static final String[] KATEGORI_PENGANGGURAN_LIST = new String[]{"Mencari Pekerjaan", "Mempersiapkan Usaha", "Merasa Tidak Mungkin Mendapatkan Pekerjaan", "Sudah Punya Pekerjaan Tapi Belum Mulai Bekerja"};
    public static final String[] KLASIFIKASI_PENGANGGURAN_LIST = new String[]{"Pengangguran Terbuka", "Sukarela", "Terpaksa"};

    final String[] tableTitle = new String[] {
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

    public static final List<int[]> table = Collections.unmodifiableList(new ArrayList<int[]>(){{
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.NONE});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.NONE});
        add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.NONE});

        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.STATUS_KEADAAN_KETENAGAKERJAAN, DatasetKetenagakerjaan.NONE});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.ANGKATAN_KERJA});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});

        add(new int[]{DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.JENIS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PADA_PEKERJAAN_UTAMA});
        add(new int[]{DatasetKetenagakerjaan.JAM_KERJA, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.PADA_PEKERJAAN_UTAMA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.BEKERJA});

        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.LAPANGAN_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.STATUS_PEKERJAAN_UTAMA, DatasetKetenagakerjaan.BEKERJA});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});

        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        add(new int[]{DatasetKetenagakerjaan.KABUPATEN, DatasetKetenagakerjaan.KATEGORI_PENGANGGURAN, DatasetKetenagakerjaan.PENGANGGURAN_TERBUKA});
        add(new int[]{DatasetKetenagakerjaan.PENDIDIKAN, DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN, DatasetKetenagakerjaan.NONE});
        add(new int[]{DatasetKetenagakerjaan.UMUR, DatasetKetenagakerjaan.KLASIFIKASI_PENGANGGURAN, DatasetKetenagakerjaan.NONE});
    }});

    class Tahun{
        int tahun;
        ArrayList<JenisKelamin> JK;

        public Tahun(int t){
            tahun = t;
            JK = new ArrayList<>();
            JK.add(new JenisKelamin());
            JK.add(new JenisKelamin());
        }
    }

    class JenisKelamin{
        ArrayList<Kategori> K;

        public JenisKelamin(){
            K = new ArrayList<>();
            K.add(new Kategori(UMUR, new int[] {
                    PENDIDIKAN,
                    STATUS_KEADAAN_KETENAGAKERJAAN,
                    JAM_KERJA,
                    LAPANGAN_PEKERJAAN_UTAMA,
                    STATUS_PEKERJAAN_UTAMA,
                    KATEGORI_PENGANGGURAN,
                    KLASIFIKASI_PENGANGGURAN
            }));
            K.add(new Kategori(KABUPATEN, new int[] {
                    UMUR,
                    PENDIDIKAN,
                    STATUS_KEADAAN_KETENAGAKERJAAN,
                    LAPANGAN_PEKERJAAN_UTAMA,
                    STATUS_PEKERJAAN_UTAMA,
                    KATEGORI_PENGANGGURAN
            }));
            K.add(new Kategori(PENDIDIKAN, new int[] {
                    STATUS_KEADAAN_KETENAGAKERJAAN,
                    JAM_KERJA,
                    KATEGORI_PENGANGGURAN,
                    KLASIFIKASI_PENGANGGURAN
            }));
            K.add(new Kategori(JAM_KERJA, new int[] {
                    LAPANGAN_PEKERJAAN_UTAMA,
                    STATUS_PEKERJAAN_UTAMA
            }));
            K.add(new Kategori(LAPANGAN_PEKERJAAN_UTAMA, new int[] {
                    PENDIDIKAN,
                    JENIS_PEKERJAAN_UTAMA
            }));
            K.add(new Kategori(STATUS_PEKERJAAN_UTAMA, new int[] {
                    PENDIDIKAN,
                    LAPANGAN_PEKERJAAN_UTAMA,
                    JENIS_PEKERJAAN_UTAMA
            }));
            K.add(new Kategori(JENIS_PEKERJAAN_UTAMA, new int[] {
                    PENDIDIKAN
            }));
        }

        public void setData(ArrayList<ArrayList<Integer>> data, int from, int destination, int classification){
            K.get(getIndex(K, from)).setData(data, destination, classification);
        }
        public ArrayList<ArrayList<Integer>> getData(int from, int destination, int classification){
            return K.get(getIndex(K, from)).getData(destination, classification);
        }

        public Boolean isComplete(int from, int destination, int classification){
            return K.get(getIndex(K, from)).isComplete(destination, classification);
        }
    }
    class Value{
        Integer value, akvalue, bvalue, puvalue, ptvalue;

        ArrayList<Kategori> K;

        //Constructor
        public Value(){
            value = -1;
            akvalue = -1;
            bvalue = -1;
            puvalue = -1;
            ptvalue = -1;
        }
        public Value(int[] kategori) {
            K = new ArrayList<>();
            for(int k : kategori){
                K.add(new Kategori(k));
            }
        }
        //Constructor

        public void setRow(ArrayList<Integer> row, int destination, int classification){
            K.get(getIndex(K, destination)).setRow(row, classification);
        }
        public ArrayList<Integer> getRow(int destination, int classification){
            return K.get(getIndex(K, destination)).getRow(classification);
        }
        public boolean isComplete(int destination, int classification){
            return K.get(getIndex(K, destination)).isComplete(classification);
        }
    }
    class Kategori{
        String nama;
        ArrayList<Value> V;

        public Kategori(int kategori){
            nama = getNama(kategori);
            V = new ArrayList<>();
            for(int i = 0; i < getSize(kategori); i++){
                V.add(new Value());
            }
        }
        public Kategori(int kategori, int[] direction){
            nama = getNama(kategori);
            V = new ArrayList<>();
            for(int i = 0; i < getSize(kategori); i++){
                V.add(new Value(direction));
            }
        }

        public boolean isComplete(int destination, int classification){
            for(int i = 0; i < V.size(); i++){
                if(!V.get(i).isComplete(destination, classification)){
                    return false;
                }
            }
            return true;
        }

        public boolean isComplete(int classification){
            for(Integer i : getRow(classification)){
                if(i == -1){
                    return false;
                }
            }
            return true;
        }


        public void setData(ArrayList<ArrayList<Integer>> data, int destination, int classification){
            for(int i = 0; i < data.size(); i++){
                V.get(i).setRow(data.get(i), destination, classification);
            }
        }
        public ArrayList<ArrayList<Integer>> getData(int destination, int classification){
            ArrayList<ArrayList<Integer>> data = new ArrayList<>();
            for(int i = 0; i < V.size(); i++){ data.add(V.get(i).getRow(destination, classification)); }
            return data;
        }

        public void setRow(ArrayList<Integer> row, int classification){
            switch (classification){
                case NONE:
                    for(int i = 0; i < row.size(); i++){ V.get(i).value = row.get(i); }
                    break;
                case ANGKATAN_KERJA:
                    for(int i = 0; i < row.size(); i++){ V.get(i).akvalue = row.get(i); }
                    break;
                case BEKERJA:
                    for(int i = 0; i < row.size(); i++){ V.get(i).bvalue = row.get(i); }
                    break;
                case PADA_PEKERJAAN_UTAMA:
                    for(int i = 0; i < row.size(); i++){ V.get(i).puvalue = row.get(i); }
                    break;
                case PENGANGGURAN_TERBUKA:
                    for(int i = 0; i < row.size(); i++){ V.get(i).ptvalue = row.get(i); }
                    break;
            }
        }
        public ArrayList<Integer> getRow(int classification){
            ArrayList<Integer> al = new ArrayList<>();
            switch (classification){
                case NONE:
                    for(int i = 0; i < V.size(); i++){ al.add(V.get(i).value); }
                    break;
                case ANGKATAN_KERJA:
                    for(int i = 0; i < V.size(); i++){ al.add(V.get(i).akvalue); }
                    break;
                case BEKERJA:
                    for(int i = 0; i < V.size(); i++){ al.add(V.get(i).bvalue); }
                    break;
                case PADA_PEKERJAAN_UTAMA:
                    for(int i = 0; i < V.size(); i++){ al.add(V.get(i).puvalue); }
                    break;
                case PENGANGGURAN_TERBUKA:
                    for(int i = 0; i < V.size(); i++){ al.add(V.get(i).ptvalue); }
                    break;
            }
            return al;
        }
    }

    Database database;
    ArrayList<Tahun> T;

    public DatasetKetenagakerjaan(){
        database = new Database();
        T = new ArrayList<>();

        for(Integer t : database.loadAllTahun()){
            newTahun(t);
            set(database.loadByTahun(t, LAKI_LAKI), database.loadByTahun(t, PEREMPUAN), t);
        }
    }

    public void newTahun(int tahun){
        T.add(new Tahun(tahun));
        sortTahun();
    }
    public void sortTahun(){
        if(T.size() > 0){
            Collections.sort(T, (t1, t2) -> {
                if(t1.tahun < t2.tahun){
                    return -1;
                }
                else{
                    return 1;
                }
            });
            /* replaced from
            Collections.sort(T, new Comparator<Tahun>() {
                @Override
                public int compare(Tahun t1, Tahun t2) {
                    if(t1.tahun < t2.tahun){
                        return -1;
                    }
                    else{
                        return 1;
                    }
                }
            });
             */
        }
    }
    public boolean isTahunExist(int tahun){
        for(Tahun t : T){
            if(t.tahun == tahun){
                return true;
            }
        }
        return false;
    }

    public String getNama(int kategori){
        String nama;
        switch (kategori){
            case UMUR:
                nama = "Golongan Umur";
                break;
            case PENDIDIKAN:
                nama = "Pendidikan Tertinggi Yang Ditamatkan";
                break;
            case KABUPATEN:
                nama = "Kabupaten/Kota";
                break;
            case STATUS_KEADAAN_KETENAGAKERJAAN:
                nama = "Jenis Kegiatan";
                break;
            case JAM_KERJA:
                nama = "Jam Kerja";
                break;
            case LAPANGAN_PEKERJAAN_UTAMA:
                nama = "Lapangan Pekerjaan Utama";
                break;
            case STATUS_PEKERJAAN_UTAMA:
                nama = "Status Pekerjaan Utama";
                break;
            case JENIS_PEKERJAAN_UTAMA:
                nama = "Jenis Pekerjaan Utama";
                break;
            case KATEGORI_PENGANGGURAN:
                nama = "Kategori Pengangguran";
                break;
            case KLASIFIKASI_PENGANGGURAN:
                nama = "Klasifikasi Pengannguran";
                break;
            default:
                nama = "";
                break;
        }
        return nama;
    }
    public int getSize(int kategori){
        int size;
        switch (kategori){
            case UMUR:
                size = UMUR_LIST.length;
                break;
            case PENDIDIKAN:
                size = PENDIDIKAN_LIST.length;
                break;
            case KABUPATEN:
                size = KABUPATEN_LIST.length;
                break;
            case STATUS_KEADAAN_KETENAGAKERJAAN:
                size = SKK_LIST.length;
                break;
            case JAM_KERJA:
                size = JAM_KERJA_LIST.length;
                break;
            case LAPANGAN_PEKERJAAN_UTAMA:
                size = LPU_LIST.length;
                break;
            case STATUS_PEKERJAAN_UTAMA:
                size = SPU_LIST.length;
                break;
            case JENIS_PEKERJAAN_UTAMA:
                size = JPU_LIST.length;
                break;
            case KATEGORI_PENGANGGURAN:
                size = KATEGORI_PENGANGGURAN_LIST.length;
                break;
            case KLASIFIKASI_PENGANGGURAN:
                size = KLASIFIKASI_PENGANGGURAN_LIST.length;
                break;
            default:
                size = 0;
                break;
        }
        return size;
    }
    public static String[] getList(int kategori){
        String[] list;
        switch (kategori){
            case UMUR:
                list = UMUR_LIST;
                break;
            case PENDIDIKAN:
                list = PENDIDIKAN_LIST;
                break;
            case KABUPATEN:
                list = KABUPATEN_LIST;
                break;
            case STATUS_KEADAAN_KETENAGAKERJAAN:
                list = SKK_LIST;
                break;
            case JAM_KERJA:
                list = JAM_KERJA_LIST;
                break;
            case LAPANGAN_PEKERJAAN_UTAMA:
                list = LPU_LIST;
                break;
            case STATUS_PEKERJAAN_UTAMA:
                list = SPU_LIST;
                break;
            case JENIS_PEKERJAAN_UTAMA:
                list = JPU_LIST;
                break;
            case KATEGORI_PENGANGGURAN:
                list = KATEGORI_PENGANGGURAN_LIST;
                break;
            case KLASIFIKASI_PENGANGGURAN:
                list = KLASIFIKASI_PENGANGGURAN_LIST;
                break;
            default:
                list = null;
        }
        return list;
    }

    public int getIndex(ArrayList<Kategori> k, int kategori){
        for(int i = 0; i < k.size(); i++){
            if(k.get(i).nama.equals(getNama(kategori))){
                return i;
            }
        }
        return -1;
    }
    public int getTahunIndex(int tahun){
        for(int i = 0; i < T.size(); i++){
            if(T.get(i).tahun == tahun) return i;
        }
        return -1;
    }


    public void set(ArrayList<ArrayList<ArrayList<Integer>>> dataM, ArrayList<ArrayList<ArrayList<Integer>>> dataF, int tahun){
        for(int i = 0; i < dataM.size(); i++){
            T.get(getTahunIndex(tahun)).JK.get(LAKI_LAKI).setData(dataM.get(i), table.get(i)[0], table.get(i)[1], table.get(i)[2]);
        }
        for(int i = 0; i < dataF.size(); i++){
            T.get(getTahunIndex(tahun)).JK.get(PEREMPUAN).setData(dataF.get(i), table.get(i)[0], table.get(i)[1], table.get(i)[2]);
        }
    }
    public ArrayList<ArrayList<ArrayList<Integer>>> get(int tahun, int gender){
        ArrayList<ArrayList<ArrayList<Integer>>> data = new ArrayList<>();
        for(int i = 0; i < table.size(); i++){
            data.add(T.get(getTahunIndex(tahun)).JK.get(gender).getData(table.get(i)[0], table.get(i)[1], table.get(i)[2]));
        }
        return data;
    }

    public void saveToDatabase(){
        for(Tahun t : T){
        //    if(isComplete(t.tahun)) {
                database.save(get(t.tahun, LAKI_LAKI), t.tahun, LAKI_LAKI);
                database.save(get(t.tahun, PEREMPUAN), t.tahun, PEREMPUAN);
        //    }
        }
    }

    public boolean isComplete(int tahun){
        for(int i = 0; i < table.size(); i++){
            if(!T.get(getTahunIndex(tahun)).JK.get(LAKI_LAKI).isComplete(table.get(i)[0], table.get(i)[1], table.get(i)[2])){
                return false;
            }
            else if(!T.get(getTahunIndex(tahun)).JK.get(PEREMPUAN).isComplete(table.get(i)[0], table.get(i)[1], table.get(i)[2])){
                return false;
            }
        }
        return true;
    }
    public ArrayList<Integer> whichNotComplete(int tahun, int gender){
        ArrayList<Integer> al = new ArrayList<>();
        for(int i = 0; i < table.size(); i++){
            if(!T.get(getTahunIndex(tahun)).JK.get(gender).isComplete(table.get(i)[0], table.get(i)[1], table.get(i)[2])){
                al.add(i);
            }
        }
        return al;
    }
}
