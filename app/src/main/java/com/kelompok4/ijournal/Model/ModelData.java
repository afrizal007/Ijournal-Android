package com.kelompok4.ijournal.Model;

/**
 * Created by ERIK on 9/18/2017.
 */

public class ModelData {
    String nama_sekolah;
    String alamat_sekolah;
    String min;
    String id;
    String no_sekolah;
    String email_sekolah;
    String website_sekolah;
    String kuota;
    String info_umum;
    String nama_siswa;
    String nilai_siswa;
    String no_data;

    public String getNo_data() {
        return no_data;
    }

    public void setNo_data(String no_data) {
        this.no_data = no_data;
    }

    public String getNo_urut() {
        return no_urut;
    }

    public void setNo_urut(String no_urut) {
        this.no_urut = no_urut;
    }

    String no_urut;

    public String getNo_daftar() {
        return no_daftar;
    }

    public void setNo_daftar(String no_daftar) {
        this.no_daftar = no_daftar;
    }

    String no_daftar;

    public String getNama_siswa() {
        return nama_siswa;
    }

    public void setNama_siswa(String nama_siswa) {
        this.nama_siswa = nama_siswa;
    }

    public String getNilai_siswa() {
        return nilai_siswa;
    }

    public void setNilai_siswa(String nilai_siswa) {
        this.nilai_siswa = nilai_siswa;
    }

    public String getDetail_siswa() {
        return detail_siswa;
    }

    public void setDetail_siswa(String detail_siswa) {
        this.detail_siswa = detail_siswa;
    }

    String detail_siswa;

    public ModelData() {
        this.nama_sekolah = nama_sekolah;
        this.alamat_sekolah = alamat_sekolah;
        this.min = min;
        this.id = id;
    }



    public String getEmail_sekolah() {
        return email_sekolah;
    }

    public void setEmail_sekolah(String email_sekolah) {
        this.email_sekolah = email_sekolah;
    }

    public String getWebsite_sekolah() {
        return website_sekolah;
    }

    public void setWebsite_sekolah(String website_sekolah) {
        this.website_sekolah = website_sekolah;
    }

    public String getKuota() {
        return kuota;
    }

    public void setKuota(String kuota) {
        this.kuota = kuota;
    }

    public String getInfo_umum() {
        return info_umum;
    }

    public void setInfo_umum(String info_umum) {
        this.info_umum = info_umum;
    }


    public String getNo_sekolah() {
        return no_sekolah;
    }

    public void setNo_sekolah(String no_sekolah) {
        this.no_sekolah = no_sekolah;
    }


    public String getNamaSekolah() {
        return nama_sekolah;
    }

    public void setNamaSekolah(String nama_sekolah) {
        this.nama_sekolah = nama_sekolah;
    }

    public String getAlamatSekkolah() {
        return alamat_sekolah;
    }

    public void setAlamatSekolah(String alamat_sekolah) {
        this.alamat_sekolah = alamat_sekolah;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
