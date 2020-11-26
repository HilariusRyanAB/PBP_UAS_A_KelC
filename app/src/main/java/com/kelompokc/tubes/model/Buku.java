package com.kelompokc.tubes.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class Buku implements Serializable
{
    private int id;
    private String judul, genre, noSeri, imgURL, status;

    public Buku(int id, String judul, String genre, String noSeri, String imgURL, String status)
    {
        this.id = id;
        this.judul = judul;
        this.genre = genre;
        this.noSeri = noSeri;
        this.imgURL = imgURL;
        this.status = status;
    }

    public Buku(String judul, String genre, String noSeri, String imgURL, String status)
    {
        this.judul = judul;
        this.genre = genre;
        this.noSeri = noSeri;
        this.imgURL = imgURL;
        this.status = status;
    }

    public String getJudul() {
        return judul;
    }

    public int getId()
    {
        return id;
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

    public String getStatus()
    {
        return status;
    }

    @BindingAdapter({"profileImage"})
    public static void loadImgURL(ImageView view, String imgURL)
    {
        Glide.with(view.getContext()).load(imgURL).into(view);
    }

    public void setId(int id) {
        this.id = id;
    }
}
