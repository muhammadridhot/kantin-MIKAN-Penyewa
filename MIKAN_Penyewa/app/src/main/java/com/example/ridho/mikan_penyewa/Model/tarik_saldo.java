package com.example.ridho.mikan_penyewa.Model;

public class tarik_saldo {
    String idPenarikan, email, noRek, atm,tglPenarikan, status;
    int nominal;

    public tarik_saldo(String idPenarikan, String email, String noRek, String atm,String tglPenarikan, String status, int nominal) {
        this.idPenarikan = idPenarikan;
        this.email = email;
        this.noRek = noRek;
        this.atm = atm;
        this.tglPenarikan = tglPenarikan;
        this.status = status;
        this.nominal = nominal;
    }

    public tarik_saldo() {
    }

    public String getIdPenarikan() {
        return idPenarikan;
    }

    public void setIdPenarikan(String idPenarikan) {
        this.idPenarikan = idPenarikan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }

    public String getAtm() {
        return atm;
    }

    public void setAtm(String atm) {
        this.atm = atm;
    }

    public String getTglPenarikan() {
        return tglPenarikan;
    }

    public void setTglPenarikan(String tglPenarikan) {
        this.tglPenarikan = tglPenarikan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }
}
