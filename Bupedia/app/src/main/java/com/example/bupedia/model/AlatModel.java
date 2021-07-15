package com.example.bupedia.model;

import java.sql.Blob;

public class AlatModel {
    private String id;
    private String namaAlat;
    private byte[] fotoAlat;
    private String jenis;
    private String deskripsiAlat;
    public AlatModel(){

    }
    public AlatModel(
             String id,
             String namaAlat,
             byte[] fotoAlat,
             String jenis,
             String deskripsiAlat
    ){
        this.id= id;
        this.namaAlat= namaAlat;
        this.fotoAlat= fotoAlat;
        this.jenis= jenis;
        this.deskripsiAlat= deskripsiAlat;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public byte[] getFotoAlat() {
        return fotoAlat;
    }

    public void setFotoAlat(byte[] fotoAlat) {
        this.fotoAlat = fotoAlat;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getDeskripsiAlat() {
        return deskripsiAlat;
    }

    public void setDeskripsiAlat(String deskripsiAlat) {
        this.deskripsiAlat = deskripsiAlat;
    }
}
