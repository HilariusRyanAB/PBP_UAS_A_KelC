package com.kelompokc.tubes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kelompokc.tubes.model.Buku;

@Database(entities = {Buku.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract BukuDAO bukuDAO();
}
