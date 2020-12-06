package com.kelompokc.tubes.ui.peminjaman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.kelompokc.tubes.API.PinjamAPI;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.QRBarcodeActivity;
import com.kelompokc.tubes.adapter.AdapterPinjam;
import com.kelompokc.tubes.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class PeminjamanFragment extends Fragment
{
    private RecyclerView recyclerView;
    private AdapterPinjam pModel;
    private FloatingActionButton add;
    private ImageView scan;
    private List<Buku> tempPinjam = new ArrayList<>();
    private int idUser;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Calendar currentTime = Calendar.getInstance();
    private Date tanggal;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String tglKembali;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_peminjaman, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_peminjaman);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        scan = root.findViewById(R.id.button_qr);
        currentTime.add(Calendar.DATE, 7);
        tanggal = currentTime.getTime();
        tglKembali = sdf.format(tanggal);
        add = root.findViewById(R.id.button_pinjam);
        pModel = new AdapterPinjam(getContext(), tempPinjam);

        sharedPreferences  = getContext().getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt("idUser", 0);

        recyclerView.setAdapter(pModel);

        tempPinjam.clear();

        getBuku();

        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tempPinjam.clear();
                tempPinjam = pModel.getDataBuku();
                if (tempPinjam.isEmpty())
                {
                    FancyToast.makeText(getContext(), "Silahkan Pilih Buku", FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR, false).show();
                }
                else
                {
                    CharSequence[] title = getStringArray(tempPinjam);
                    getDialog(title, tempPinjam.size());
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getContext(), QRBarcodeActivity.class));
            }
        });

        swipeRefreshLayout = root.findViewById(R.id.swipePinjam);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                Fragment fragment = new PeminjamanFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return root;
    }

    private void getDialog(CharSequence[] a, int size)
    {
        String temp = "";

        for (int i = 0; i < size; i++)
        {
            if (i < size - 1)
            {
                temp = temp + a[i] + "\n\n";
            }
            else
            {
                temp = temp + a[i];
            }
        }
        new AlertDialog.Builder(getContext()).setTitle("Buku Yang Akan Dipinjam").setMessage(temp)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    for(int j = 0; j < tempPinjam.size(); j++)
                    {
                        editBuku(tempPinjam.get(j).getId());
                    }
                    Fragment fragment = new PeminjamanFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {

                }
            }).create().show();
    }

    public static String[] getStringArray(List<Buku> input)
    {
        String[] strings = new String[input.size()];

        for (int j = 0; j < input.size(); j++)
        {
            strings[j] = "Judul Buku: " + input.get(j).getJudul();
        }

        return strings;
    }

    public void tambahBuku(final String judul, final String genre, final String noSeri, final String gambar)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, BukuAPI.URL_ADD, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONObject obj = new JSONObject(response);

                    FancyToast.makeText(getContext(), obj.getString("message"), FancyToast.LENGTH_SHORT,
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
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("judul", judul);
                params.put("genre", genre);
                params.put("noSeri", noSeri);
                params.put("status", "Tersedia");
                params.put("img", gambar);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getBuku()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BukuAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!tempPinjam.isEmpty())
                        tempPinjam.clear();

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        int id                  = Integer.parseInt(jsonObject.optString("id"));
                        String judul            = jsonObject.optString("judul");
                        String genre            = jsonObject.optString("genre");
                        String noSeri           = jsonObject.optString("noSeri");
                        String gambar           = jsonObject.optString("img");
                        String status           = jsonObject.optString("status");

                        gambar.replace("\\", "");

                        if(status.equalsIgnoreCase("Tersedia"))
                        {
                            Buku buku = new Buku(id, judul, genre, noSeri, gambar, status);
                            tempPinjam.add(buku);
                            pModel.notifyDataSetChanged();
                        }
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                FancyToast.makeText(getContext(), response.optString("message"), FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS, true).show();
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

    public void editBuku(int idBuku)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest  stringRequest = new StringRequest(PUT, BukuAPI.URL_UPDATEST + idBuku,
                new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(getContext(), obj.getString("message"), FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS, true).show();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                tambahTransaksiPinjam(idUser, idBuku);
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
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("status", "Dipinjam");

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void tambahTransaksiPinjam(final int idUser, final int idBuku)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, PinjamAPI.URL_ADD, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                try
                {
                    JSONObject obj = new JSONObject(response);
                    FancyToast.makeText(getContext(), obj.getString("message"), FancyToast.LENGTH_SHORT,
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
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("idUser", String.valueOf(idUser));
                params.put("idBuku", String.valueOf(idBuku));
                params.put("tgl_kembali", tglKembali);

                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(stringRequest);
    }
}