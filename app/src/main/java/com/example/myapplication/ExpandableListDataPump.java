package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String> > getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> cricket = new ArrayList<String>();
        cricket.add("IMT (Indeks Massa Tubuh) atau dalam Bahasa inggris nya ialah BMI (Body Mass Index) merupakan " +
                "metrik yang digunakan untuk mendefinisikan karakteristik tinggi/berat badan pada orang dewasa " +
                "untuk mengklasifikasikan (kategori) ke dalam kelompok – kelompok");

        List<String> football = new ArrayList<String>();
        football.add("Body Mass Index atau Indeks Massa Tubuh adalah indeks sederhana yang dihitung berdasarkan " +
                "berat badan seseorang dalam kilogram dibagi dengan kuadrat tinggi badan dalam meter (kg/m^2). ");

        List<String> basketball = new ArrayList<String>();
        basketball.add("Terdapat 5 kategori, yakni:" +
                "\n\n <18.5, merupakan 'Kekurusan'" +
                "\n\n 18.5 s/d 22.9, merupakan 'Normal'" +
                "\n\n 23.0 s/d 24.9, merupakan 'Kegemukan'" +
                "\n\n 25.0 s/d 29.9, merupakan 'Obesitas I'" +
                "\n\n >30.0, merupakan Obesitas II");

        List<String> bmr = new ArrayList<String>();
        bmr.add("Basal Metabolic Rate (BMR) atau Angka Metabolisme Basal (AMB) adalah"+
                "kebutuhan minimal energi untuk melakukan proses kegiatan sehari-hari.");

        List<String> air = new ArrayList<String>();
        air.add("Keterangan air disini merupakan kadar mineral yang minimal dicukupi oleh pengguna," +
                " air mineral ini berguna untuk menopang kegiatan sehari-hari" +
                ". Apabila kekurangan cairan / mineral, nanti akan terjadi dehidrasi pada tubuh");

        List<String> lingkup = new ArrayList<String>();
        lingkup.add("Batasan masalah yang didapat adalah ditujukan kepada kalangan remaja dan orang dewasa maupun pria. " +
                "Takaran tolak ukur untuk klasifikasi pembagian hasil hanya mengikuti dengan tabel yang diperuntukkan orang asia");

        List<String> penting = new ArrayList<String>();
        penting.add("Pentingnya menjaga kesehatan, berikut:"+"\n\n"+
                "1. Dapat melakukan aktivitas lebih banyak karena stamina lebih tinggi"+
                "\n 2. Meminimalkan seseorang dari risiko terkena nyeri sendi dan nyeri otot"+
                "\n 3. Memiliki pola dan kualitas tidur yang lebih baik"+
                "\n 4. Kinerja jantung akan lebih ringan"+
                "\n 5. Peredaran darah dan metabolisme juga akan lebih baik");

        List<String> acuan = new ArrayList<String>();
        acuan.add("Pengukuran IMT akan mengkategorikan tubuh yang berada dalam golongan berat badan sehat atau tidak" +
                "sehat melalui ukuran tinggi dan berat badan. Namun penelitian menyebutkan bahwa" +
                "IMT tidak dapat dijadikan tolak ukur utama apakah tubuh dalam kondisi sehat atau sakit, karena " +
                "pengukuran IMT tidak dapat membedakan berat lemak dengan otot");

        List<String> pola_makan = new ArrayList<String>();
        pola_makan.add("Pentingnya menjaga pola makan tersebut agar anda bisa mengontrol sejauh mana tubuh anda" +
                "mampu menampung makanan yang sehat dan menghindari makanan-makanan yang akan menyebabkan" +
                "dampak negatif pada tubuh ");

        List<String> diabetes = new ArrayList<String>();
        diabetes.add("Diabetes adalah penyakit kronis yang ditandai dengan ciri-ciri berupa tingginya kadar gula (glukosa) darah. " +
                "Glukosa merupakan sumber energi utama bagi sel tubuh manusia ");

        expandableListDetail.put("Apa itu IMT ?", cricket);
        expandableListDetail.put("Bagaimana cara menghitung IMT ?", football);
        expandableListDetail.put("Sebutkan macam-macam kategori dalam IMT ?", basketball);
        expandableListDetail.put("Apa itu BMR ?", bmr);
        expandableListDetail.put("Pentingnya air ?", air);
        expandableListDetail.put("Ruang lingkup yang digunakan ?", lingkup);
        expandableListDetail.put("Pentingnya menjaga kesehatan ?", penting);
        expandableListDetail.put("Seberapa akurat pengukuran IMT untuk tubuh ?", acuan);
        expandableListDetail.put("Apakah penting menjaga pola makan ?", pola_makan);
        expandableListDetail.put("Apa itu diabetes ?", diabetes);
        return expandableListDetail;
    }
}
