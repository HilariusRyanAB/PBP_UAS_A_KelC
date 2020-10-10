package com.kelompokc.tubes.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

@Entity
public class Buku implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "judul")
    private String judul;

    @ColumnInfo(name = "genre")
    private String genre;

    @ColumnInfo(name = "no_seri")
    private String noSeri;

    @ColumnInfo(name = "img_url")
    private String imgURL;

    public Buku(String judul, String genre, String noSeri, String imgURL)
    {
        this.judul = judul;
        this.genre = genre;
        this.noSeri = noSeri;
        this.imgURL = imgURL;
    }

    public String getJudul() {
        return judul;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public String getNoSeri() {
        return noSeri;
    }

    public String getImgURL() {
        return imgURL;
    }

    @BindingAdapter({"profileImage"})
    public static void loadImgURL(ImageView view, String imgURL)
    {
        Glide.with(view.getContext()).load(imgURL).into(view);
    }
}
