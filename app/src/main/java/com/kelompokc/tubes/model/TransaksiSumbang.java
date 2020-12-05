package com.kelompokc.tubes.model;

import java.io.Serializable;

public class TransaksiSumbang implements Serializable
{
    private int id, idUser, idBuku;
    private Buku buku;

    public TransaksiSumbang(int id, int idUser, int idBuku, Buku buku)
    {
        this.id = id;
        this.idUser = idUser;
        this.idBuku = idBuku;
        this.buku = buku;
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

    public Buku getBuku() {
        return buku;
    }
}
