package com.example.ridho.mikan_penyewa.Model;

public class stan_model {
    private String nama;
    Boolean status;

    public stan_model(String nama, Boolean status) {
        this.nama = nama;
        this.status = status;
    }

    public stan_model() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
