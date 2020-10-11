package com.kelompokc.tubes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.kelompokc.tubes.model.Buku;

import java.util.List;

@Dao
public interface BukuPinjamDAO
{
    @Query("SELECT * FROM Buku")
    List<Buku> getAll();

    @Insert
    void insert(Buku buku);

    @Query("DELETE FROM Buku WHERE no_seri = :noSeri")
    abstract void delete(String noSeri);
}
