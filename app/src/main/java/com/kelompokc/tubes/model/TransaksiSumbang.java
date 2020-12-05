package com.kelompokc.tubes.model;

import java.io.Serializable;

public class TransaksiSumbang implements Serializable
{
    private int id, idUser, idBuku;

    public TransaksiSumbang(int id, int idUser, int idBuku)
    {
        this.id = id;
        this.idUser = idUser;
        this.idBuku = idBuku;
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
}
