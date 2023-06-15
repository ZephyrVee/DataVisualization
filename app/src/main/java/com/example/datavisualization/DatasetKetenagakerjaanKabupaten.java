package com.example.datavisualization;

import java.util.ArrayList;
import java.util.Collections;

public class DatasetKetenagakerjaanKabupaten {
    public static final int UMUR = 0;
    public static final int PENDIDIKAN = 1;
    public static final int JENIS_KEGIATAN = 2;
    public static final int LAPANGAN_PEKERJAAN_UTAMA = 3;
    public static final int STATUS_PEKERJAAN_UTAMA = 4;
    public static final int PENGANGGURAN = 5;

    public static final int LAKI_LAKI = 0;
    public static final int PEREMPUAN = 1;

    public static final int JAKARTA_UTARA = 0;

    public static final String[] KATEGORI = new String[]{"Golongan Umur", "Pendidikan Tertinggi Yang Ditamatkan", "Jenis Kegiatan Selama Seminggu Lalu", "Lapangan Pekerjaan Utama", "Status Pekerjaan Utama", "Pengangguran Terbuka"};
    public static final String[] JENIS_KELAMIN = new String[]{"Laki-laki", "Perempuan", "Laki-laki + Perempuan"};

    class Tahun{
        final Integer value;
        ArrayList<Kabupaten> kabupaten;
        public Tahun(int t){
            value = t;

            kabupaten = new ArrayList<>();
            kabupaten.add(new Kabupaten("Jakarta Utara", JAKARTA_UTARA));
        }

        public void setAll(ArrayList<ArrayList<Integer>> dataL, ArrayList<ArrayList<Integer>> dataP){
            kabupaten.get(0).setAll(dataL, dataP);
        }
        public void setAll(ArrayList<ArrayList<Integer>> data, int jk){
            kabupaten.get(0).setAll(data, jk);
        }
        public ArrayList<Integer> get(int k){
            return kabupaten.get(0).get(k);
        }
        public ArrayList<Integer> get(int jk, int k){
            return kabupaten.get(0).get(jk, k);
        }
        public ArrayList<ArrayList<Integer>> getAll(){
            return kabupaten.get(0).getAll();
        }
        public ArrayList<ArrayList<Integer>> getAll(int jk){
            return kabupaten.get(0).getAll(jk);
        }
    }

    class Kabupaten{
        final String nama;
        final int index;

        ArrayList<JenisKelamin> jenisKelamin;

        public Kabupaten(String n, int i){
            nama = n;
            index = i;

            jenisKelamin = new ArrayList<>();
            jenisKelamin.add(new JenisKelamin("Laki_laki", LAKI_LAKI));
            jenisKelamin.add(new JenisKelamin("Perempuan", PEREMPUAN));
        }

        public void setAll(ArrayList<ArrayList<Integer>> dataL, ArrayList<ArrayList<Integer>> dataP){
            setAll(dataL, LAKI_LAKI);
            setAll(dataP, PEREMPUAN);
        }
        public void setAll(ArrayList<ArrayList<Integer>> data, int jk){
            jenisKelamin.get(jk).setAll(data);
        }
        public ArrayList<Integer> get(int k){
            ArrayList<Integer> al = new ArrayList<>();
            for(int i = 0; i < getSize(k); i++){
                al.add( get(LAKI_LAKI, k).get(i) + get(PEREMPUAN, k).get(i) );
            }
            return al;
        }
        public ArrayList<Integer> get(int jk, int k){
            return jenisKelamin.get(jk).get(k);
        }
        public ArrayList<ArrayList<Integer>> getAll(){
            ArrayList<ArrayList<Integer>> al = new ArrayList<>();
            for(int i = 0; i < DatasetKetenagakerjaanKabupaten.KATEGORI.length; i++){
                al.add(get(i));
            }
            return al;
        }
        public ArrayList<ArrayList<Integer>> getAll(int jk){
            return jenisKelamin.get(jk).getAll();
        }
    }

    class JenisKelamin{
        final String nama;
        final int index;

        ArrayList<Kategori> kategori;
        public JenisKelamin(String n, int idx){
            nama = n;
            index = idx;

            kategori = new ArrayList<>();
            for(int i = 0; i < KATEGORI.length; i++){
                kategori.add(new Kategori(KATEGORI[i], i));
            }
        }

        public void set(ArrayList<Integer> data, int k){
            kategori.get(k).value = data;
        }
        public void setAll(ArrayList<ArrayList<Integer>> data){
            for(int i = 0; i < kategori.size(); i++){
                set(data.get(i), i);
            }
        }
        public ArrayList<Integer> get(int k){
            return kategori.get(k).value;
        }
        public ArrayList<ArrayList<Integer>> getAll(){
            ArrayList<ArrayList<Integer>> al = new ArrayList<>();
            for(int i = 0; i < kategori.size(); i++){
                al.add(get(i));
            }
            return al;
        }
    }

    class Kategori {
        final String nama;
        final int index;
        final int size;

        ArrayList<Integer> value;

        public Kategori(String n, int idx) {
            nama = n;
            index = idx;
            size = getSize(idx);
            value = new ArrayList<>();
            for(int i = 0; i < size; i++){
                value.add(-1);
            }
        }
    }

    ArrayList<Tahun> tahun;

    public DatasetKetenagakerjaanKabupaten(){
        tahun = new ArrayList<>();
    }

    public void newTahun(int t){
        tahun.add(new Tahun(t));
        sortTahun();
    }
    public void sortTahun(){
        if(tahun.size() > 1){
            Collections.sort(tahun, (t1, t2) -> t1.value.compareTo(t2.value));
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
    public int getTahunIndex(int t){
        int idx = 0;
        for(int i = 0; i < tahun.size(); i++){
            if(tahun.get(i).value == t){
                idx = i;
            }
        }
        return idx;
    }
    public boolean isTahunExist(int t){
        for(DatasetKetenagakerjaanKabupaten.Tahun thn : tahun){
            if(thn.value == t){
                return true;
            }
        }
        return false;
    }

    public static String[] getList(int kategori){
        switch (kategori){
            case UMUR:
                return new String[] { "15 - 19", "20 - 24", "25 - 29", "30 - 34", "35 - 39", "40 - 44", "45 - 49", "50 - 54", "55 - 59", "60+"};
            case PENDIDIKAN:
                return new String[]{"Tidak/Belum Tamat SD", "SD", "SMP", "SMA", "SMK", "Diploma I/II/III", "Universitas"};
            case JENIS_KEGIATAN:
                return new String[] {"Bekerja", "Pernah Bekerja", "Tidak Pernah Bekerja", "Sekolah", "Mengurus Rumah Tangga", "Lainnya"};
            case LAPANGAN_PEKERJAAN_UTAMA:
                return new String[]{
                        "Pertanian, Perkebunan, Kehutanan, Perburuan Dan Perikanan",
                        "Pertambangan Dan Penggalian",
                        "Industri Pengolahan",
                        "Listrik Dan Gas",
                        "Air, Sampah, Limbah, Daur Ulang",

                        "Konstruksi",
                        "Perdagangan",
                        "Transportasi Dan Pergudangan",
                        "Akomodasi & Makan Minum",
                        "Informasi & Komunikasi ",

                        "Jasa Keuangan Dan Asuransi",
                        "Real Estate",
                        "Jasa Perusahaan",
                        "Administrasi Pemerintah",
                        "Jasa Pendidikan",

                        "Jasa Kesehatan",
                        "Jasa Lainnya"
                };
            case STATUS_PEKERJAAN_UTAMA:
                return new String[]{
                        "Berusaha Sendiri",
                        "Berusaha Dibantu Buruh Tidak Tetap/Buruh Tak Dibayar",
                        "Berusaha Dibantu Buruh Tetap/Buruh Dibayar",
                        "Buruh/Karyawan/Pegawai",
                        "Pekerja Bebas Di Pertanian",

                        "Pekerja Bebas Di Non Pertanian",
                        "Pekerja Keluarga/Tak Dibayar"
                };
            case PENGANGGURAN:
                return new String[]{"Mencari Pekerjaan", "Mempersiapkan Usaha", "Merasa Tidak Mungkin Mendapatkan Pekerjaan", "Sudah Punya Pekerjaan Tapi Belum Mulai Bekerja"};
        }
        return null;
    }
    public static String[] getTableList(int kategori){
        switch (kategori){
            case LAPANGAN_PEKERJAAN_UTAMA:
                return new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M-N", "O", "P", "Q", "R-U"};
            case STATUS_PEKERJAAN_UTAMA:
                return new String[]{"1", "2", "3", "4", "5", "6", "7"};
            case PENGANGGURAN :
                return new String[]{"1", "2", "3", "4"};
            case PENDIDIKAN:
                return new String[]{"Blm Tamat SD", "SD", "SMP", "SMA", "SMK", "D1 - D3", "Universitas"};
            case JENIS_KEGIATAN:
                return new String[]{"1", "2", "3", "4", "5", "6"};
            default:
                return getList(kategori);
        }
    }
    public static int getSize(int kategori){
        return getList(kategori).length;
    }

    public boolean isComplete(int t){
        for(ArrayList<Integer> al : getAll(t)){
            for(Integer i : al){
                if(i < 0){
                    return false;
                }
            }
        }
        return true;
    }

    public synchronized void setAll(ArrayList<ArrayList<Integer>> dataL, ArrayList<ArrayList<Integer>> dataP, int t){
        tahun.get(getTahunIndex(t)).setAll(dataL, dataP);
    }
    public synchronized void setAll(ArrayList<ArrayList<Integer>> data, int t, int jk){
        tahun.get(getTahunIndex(t)).setAll(data, jk);
    }
    public synchronized  void set(ArrayList<Integer> data, int t, int jk, int k){
        tahun.get(getTahunIndex(t)).kabupaten.get(0).jenisKelamin.get(jk).kategori.get(k).value = data;
    }
    public ArrayList<Integer> get(int t, int k){
        return tahun.get(getTahunIndex(t)).get(k);
    }
    public ArrayList<Integer> get(int t, int jk, int k){
        return tahun.get(getTahunIndex(t)).get(jk, k);
    }
    public ArrayList<ArrayList<Integer>> getAll(int t){
        return tahun.get(getTahunIndex(t)).getAll();
    }
    public ArrayList<ArrayList<Integer>> getAll(int t, int jk){
        return tahun.get(getTahunIndex(t)).getAll(jk);
    }
}
