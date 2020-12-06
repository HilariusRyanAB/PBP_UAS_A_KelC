package com.kelompokc.tubes.ui.pengembalian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.PinjamAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.adapter.AdapterKembali;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.model.TransaksiPinjam;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class PengembalianFragment extends Fragment
{
    private RecyclerView recyclerView;
    private AdapterKembali pModel;
    private List<Buku> tempKembali = new ArrayList<>();
    private List<TransaksiPinjam> listPinjam = new ArrayList<>();
    private Buku buku;
    private int idUser;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pModel = new AdapterKembali(getContext(), listPinjam);

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
                Fragment fragment = new PengembalianFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return root;
    }

    public void getBuku(int idBuku, int idTransaksi, String tanggal)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BukuAPI.URL_GET_SELECTED + idBuku
                , null, new Response.Listener<JSONObject>()
        {
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
                    listPinjam.add(new TransaksiPinjam(idTransaksi, idUser, buku.getId(), tanggal, buku));

                    pModel.notifyDataSetChanged();
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
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null && networkResponse.data != null)
                {
                    String jsonError = new String(networkResponse.data);
                    FancyToast.makeText(getContext(), jsonError, FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
                }
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
                , null, new Response.Listener<JSONObject>()
        {
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
                                getBuku(idBukuDB, id, tanggal);
                            }
                        }
                        pModel.notifyDataSetChanged();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    FancyToast.makeText(getContext(), response.optString("message"), FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS, true).show();
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
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null && networkResponse.data != null)
                {
                    String jsonError = new String(networkResponse.data);
                    FancyToast.makeText(getContext(), jsonError, FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
                }
            }
        });

        queue.add(stringRequest);
    }
}