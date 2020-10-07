package com.kelompokc.tubes;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Peminjaman
{
    private String judul, genre, noSeri, imgURL;

    public Peminjaman(String judul, String genre, String noSeri, String imgURL)
    {
        this.judul = judul;
        this.genre = genre;
        this.noSeri = noSeri;
        this.imgURL = imgURL;
    }

    public String getJudul() {
        return judul;
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
