package com.example.ridho.mikan_penyewa.Model;

public class Penyewa {
    private String userid,email,pass,nama,kantin,no_stan,no_rekening,image,status,masa_sewa,tgl_mulai_sewa,tgl_jatuhTempo_sewa;
    private Integer saldo;

    public Penyewa(String userid,String email, String pass, String nama, String kantin, String no_stan, String image) {
        this.userid = userid;
        this.email = email;
        this.pass = pass;
        this.nama = nama;
        this.kantin = kantin;
        this.no_stan = no_stan;
        this.no_rekening = "";
        this.image = image;
        this.status = "Tidak Aktif";
        this.masa_sewa = "";
        this.saldo = 0;
        this.tgl_mulai_sewa = "";
        this.tgl_jatuhTempo_sewa = "";
    }

    public Penyewa() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKantin() {
        return kantin;
    }

    public void setKantin(String kantin) {
        this.kantin = kantin;
    }

    public String getNo_stan() {
        return no_stan;
    }

    public void setNo_stan(String no_stan) {
        this.no_stan = no_stan;
    }

    public String getNo_rekening() {
        return no_rekening;
    }

    public void setNo_rekening(String no_rekening) {
        this.no_rekening = no_rekening;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMasa_sewa() {
        return masa_sewa;
    }

    public void setMasa_sewa(String masa_sewa) {
        this.masa_sewa = masa_sewa;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getTgl_mulai_sewa() {
        return tgl_mulai_sewa;
    }

    public void setTgl_mulai_sewa(String tgl_mulai_sewa) {
        this.tgl_mulai_sewa = tgl_mulai_sewa;
    }

    public String getTgl_jatuhTempo_sewa() {
        return tgl_jatuhTempo_sewa;
    }

    public void setTgl_jatuhTempo_sewa(String tgl_jatuhTempo_sewa) {
        this.tgl_jatuhTempo_sewa = tgl_jatuhTempo_sewa;
    }
}
