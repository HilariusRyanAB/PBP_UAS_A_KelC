package com.kelompokc.tubes.ui.pengembalian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.PinjamAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.adapter.AdapterKembali;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.model.TransaksiPinjam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class PengembalianFragment extends Fragment
{
    private RecyclerView recyclerView;
    private AdapterKembali pModel;
    private List<Buku> tempKembali = new ArrayList<>();
    private List<TransaksiPinjam> listPinjam = new ArrayList<>();
    private FloatingActionButton remove;
    private int idTransaksi;
    private Buku buku;
    private int idUser;
    private SharedPreferences sharedPreferences;
    private String tanggal;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pModel = new AdapterKembali(getContext(), tempKembali, listPinjam);

        recyclerView.setAdapter(pModel);

        tempKembali.clear();

        listPinjam.clear();

        sharedPreferences  = getContext().getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt("idUser", 0);

        getTransaksiPinjam(idUser);

        swipeRefreshLayout = root.findViewById(R.id.swipeKembali);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                listPinjam.clear();
                tempKembali.clear();
                getTransaksiPinjam(idUser);
            }
        });

        return root;
    }

    public void getBuku(int idBuku)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BukuAPI.URL_GET_SELECTED + idBuku
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject jsonObject = response.getJSONObject("data");

                    int id                  = Integer.parseInt(jsonObject.optString("id"));
                    String judul            = jsonObject.optString("judul");
                    String genre            = jsonObject.optString("genre");
                    String noSeri           = jsonObject.optString("noSeri");
                    String gambar           = jsonObject.optString("img");
                    String status           = jsonObject.optString("status");

                    gambar.replace("\\", "");
                    buku = new Buku(id, judul, genre, noSeri, gambar, status);
                    tempKembali.add(buku);

                    pModel.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    public void getTransaksiPinjam(int idUser)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data buku yang dipinjam");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PinjamAPI.URL_GET
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONArray jsonArray = response.getJSONArray("data");
                    if(jsonArray.length()!=0)
                    {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            int id = jsonObject.getInt("id");
                            int idUserDB  = Integer.parseInt(jsonObject.optString("idUser"));
                            if(idUserDB == idUser)
                            {
                                int idBukuDB = Integer.parseInt(jsonObject.optString("idBuku"));
                                String tanggal = jsonObject.optString("tgl_kembali");
                                listPinjam.add(new TransaksiPinjam(id, idUserDB, idBukuDB, tanggal));
                                getBuku(idBukuDB);
                            }
                        }
                        pModel.notifyDataSetChanged();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }
}