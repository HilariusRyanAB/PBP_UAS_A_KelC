package com.kelompokc.tubes.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kelompokc.tubes.model.Buku;

import java.util.List;

@Dao
public interface BukuPinjamDAO
{
    @Query("SELECT * FROM Buku")
    List<Buku> getAll();

    @Insert
    void insert(Buku buku);

    @Update
    void update(Buku buku);

    @Delete
    void delete(Buku buku);
}
