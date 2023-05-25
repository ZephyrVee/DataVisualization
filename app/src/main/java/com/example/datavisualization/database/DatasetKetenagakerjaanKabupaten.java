package com.example.datavisualization.database;

import java.util.ArrayList;

public class DatasetKetenagakerjaanKabupaten {
    public static final int UMUR = 0;
    public static final int PENDIDIKAN = 1;
    public static final int JENIS_KEGIATAN = 2;
    public static final int JAM_KERJA = 3;
    public static final int LAPANGAN_PEKERJAAN_UTAMA = 4;
    public static final int STATUS_PEKERJAAN_UTAMA = 5;
    public static final int JENIS_PEKERJAAN_UTAMA = 6;
    public static final int PENGANGGURAN = 7;

    class Tahun{
        final Integer value;
        ArrayList<JenisKelamin> jenisKelamin;
        public Tahun(int t){
            value = t;
            jenisKelamin = new ArrayList<>();
            jenisKelamin.add(new JenisKelamin("Laki_laki", 0));
            jenisKelamin.add(new JenisKelamin("Perempuan", 1));
        }
    }

    class JenisKelamin{
        final String nama;
        final int index;

        ArrayList<Kategori> kategori;

        public JenisKelamin(String n, int i){
            nama = n;
            index = i;

            kategori = new ArrayList<>();
            kategori.add(new Kategori("Golongan Umur", 0));
            kategori.add(new Kategori("Pendidikan Tertinggi Yang Ditamatkan", 1));
            kategori.add(new Kategori("Jenis Kegiatan Selama Seminggu Lalu", 2));
            kategori.add(new Kategori("Jumlah Jam Kerja", 3));
            kategori.add(new Kategori("Lapangan Pekerjaan Utama", 4));
            kategori.add(new Kategori("Status Pekerjaan Utama", 5));
            kategori.add(new Kategori("Jenis Pekerjaan Utama", 6));
            kategori.add(new Kategori("Pengangguran", 7));
        }

        public void set(ArrayList<ArrayList<Integer>> data){
            for(int i = 0; i < kategori.size(); i++){
                kategori.get(i).value = data.get(i);
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
            size = getTableList(idx).length;
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
    }

    public static String[] getList(int kategori){
        switch (kategori){
            case UMUR:
                return new String[] { "15 - 19", "20 - 24", "25 - 29", "30 - 34", "35 - 39", "40 - 44", "45 - 49", "50 - 54", "55 - 59", "60+"};
            case PENDIDIKAN:
                return new String[]{"Tidak/Belum Tamat SD", "SD", "SMP", "SMA", "SMK", "Diploma I/II/III", "Universitas"};
            case JENIS_KEGIATAN:
                return new String[] {"Bekerja", "Pernah Bekerja", "Tidak Pernah Bekerja", "Sekolah", "Mengurus Rumah Tangga", "Lainnya"};
            case JAM_KERJA:
                return new String[] {"0*", "1 - 4", "5 - 9", "10 - 14", "15 - 19", "20 - 24", "25 - 34", "35 - 44", "45 - 54", "55 - 59", "60 - 74", "75+"};
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
            case JENIS_PEKERJAAN_UTAMA:
                return new String[]{
                        "Tni Dan Polri",
                        "Manajer",
                        "Profesional",
                        "Teknisi Dan Asisten Profesional",
                        "Tenaga Tata Usaha",

                        "Tenaga Usaha Jasa Dan Tenaga Penjualan",
                        "Pekerja Terampil Pertanian, Kehutanan, Dan Perikanan",
                        "Pekerja Pengolahan, Kerajinan, Dan YBDI",
                        "Operator Dan Perakitan Mesin",
                        "Pekerja Kasar"
                };
            case PENGANGGURAN:
                return new String[]{"Mencari Pekerjaan", "Mempersiapkan Usaha", "Merasa Tidak Mungkin Mendapatkan Pekerjaan", "Sudah Punya Pekerjaan Tapi Belum Mulai Bekerja", "Pengangguran Terbuka", "Sukarela", "Terpaksa"};
        }
        return null;
    }
    public static String[] getTableList(int kategori){
        switch (kategori){
            case UMUR:
            case PENDIDIKAN:
            case JENIS_KEGIATAN:
            case PENGANGGURAN:
                return getList(kategori);
            case LAPANGAN_PEKERJAAN_UTAMA:
                return new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M-N", "O", "P", "Q", "R-U"};
            case STATUS_PEKERJAAN_UTAMA:
                return new String[]{"1", "2", "3", "4", "5", "6", "7"};
            case JENIS_PEKERJAAN_UTAMA:
                return new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        }
        return null;
    }
}
