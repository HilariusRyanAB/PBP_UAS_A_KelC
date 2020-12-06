package com.kelompokc.tubes.ui.sumbang;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.SumbangAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.adapter.AdapterSumbang;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.model.TransaksiSumbang;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class SumbangFragment extends Fragment
{
    private RecyclerView recyclerView;
    private AdapterSumbang pModel;
    private FloatingActionButton addSumbang;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private List<TransaksiSumbang> listSumbang = new ArrayList<>();
    private int idUser, idBuku;
    private Buku buku;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_sumbang, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_sumbang);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        pModel = new AdapterSumbang(getContext(), listSumbang);

        sharedPreferences  = getContext().getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt("idUser", 0);

        sharedPreferences  = getContext().getSharedPreferences("SharedPrefIdBuku", Context.MODE_PRIVATE);

        idBuku = sharedPreferences.getInt("idBuku", 0);

        if(idBuku != 0)
        {
            createSumbang(idBuku, idUser);
            saveIdBuku();
        }

        listSumbang.clear();

        recyclerView.setAdapter(pModel);

        getTransaksiSumbang(idUser);

        addSumbang = root.findViewById(R.id.button_sumbang);
        addSumbang.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loadFragment(new TambahEditSumbang("tambah"));
            }
        });

        swipeRefreshLayout = root.findViewById(R.id.swipeSumbang);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                Fragment fragment = new SumbangFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return root;
    }

    private void loadFragment(Fragment fragment)
    {
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.frame_fragment_sumbang,fragment)
                .commit();
    }

    public void getBuku(final int idBuku, final int idTransaksi)
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
                    listSumbang.add(new TransaksiSumbang(idTransaksi, idUser, buku.getId(), buku));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                pModel.notifyDataSetChanged();
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

    public void getTransaksiSumbang(int idUser)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data buku yang disumbangkan");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, SumbangAPI.URL_GET
                , null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONArray jsonArray = response.getJSONArray("data");
                    listSumbang.clear();
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        int id = jsonObject.optInt("id");
                        int idUserDB  = Integer.parseInt(jsonObject.optString("idUser"));
                        if(idUserDB == idUser)
                        {
                            int idBukuDB  = Integer.parseInt(jsonObject.optString("idBuku"));
                            getBuku(idBukuDB, id);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                FancyToast.makeText(getContext(), response.optString("message"), FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS, true).show();
                pModel.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
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

    public void createSumbang(final int idBuku, final int idUser)
    {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(POST, SumbangAPI.URL_ADD, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject obj = new JSONObject(response);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                pModel.notifyDataSetChanged();
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
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("idUser", String.valueOf(idUser));
                params.put("idBuku", String.valueOf(idBuku));

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void saveIdBuku()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("SharedPrefIdBuku", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idBuku", 0);
        editor.commit();
    }
}