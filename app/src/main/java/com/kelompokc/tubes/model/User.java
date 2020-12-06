package com.kelompokc.tubes.model;

import java.io.Serializable;

public class User implements Serializable
{
    private String nama, npm, jenisKelamin, fakultas, email, img_path;
    private int id;

    public User(int id, String nama, String npm, String jenisKelamin, String fakultas, String email, String img_path)
    {
        this.id = id;
        this.nama = nama;
        this.npm = npm;
        this.jenisKelamin = jenisKelamin;
        this.fakultas = fakultas;
        this.email = email;
        this.img_path = img_path;
    }

    public User(String nama, String npm, String jenisKelamin, String fakultas, String email, String img_path)
    {
        this.nama = nama;
        this.npm = npm;
        this.jenisKelamin = jenisKelamin;
        this.fakultas = fakultas;
        this.email = email;
        this.img_path = img_path;
    }

    public User(String nama, String fakultas, String jenisKelamin, int id)
    {
        this.id = id;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.fakultas = fakultas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
