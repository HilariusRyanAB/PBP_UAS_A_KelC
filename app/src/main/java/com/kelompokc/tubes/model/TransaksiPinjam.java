package com.kelompokc.tubes.model;

import java.io.Serializable;

public class TransaksiPinjam implements Serializable
{
    private int id, idUser, idBuku;
    private String tanggal;

    public TransaksiPinjam(int id, int idUser, int idBuku, String tanggal)
    {
        this.id = id;
        this.idUser = idUser;
        this.idBuku = idBuku;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public String getTanggal() {
        return tanggal;
    }
}
