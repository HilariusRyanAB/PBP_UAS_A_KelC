package com.kelompokc.tubes.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.kelompokc.tubes.model.Buku;

import java.util.List;

@Dao
public interface BukuDAO
{
    @Query("SELECT * FROM Buku WHERE status = :status")
    List<Buku> getAll(String status);

    @Insert
    void insert(Buku buku);

    @Query("UPDATE Buku SET status = :status WHERE no_seri =:noSeri")
    abstract void updateStatus(String status, String noSeri);

    @Query("DELETE FROM Buku WHERE no_seri = :noSeri")
    abstract void delete(String noSeri);
}
