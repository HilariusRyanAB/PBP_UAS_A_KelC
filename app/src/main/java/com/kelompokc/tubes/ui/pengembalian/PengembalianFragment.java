package com.kelompokc.tubes.ui.pengembalian;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.adapter.RecyclerViewAdapter;
import com.kelompokc.tubes.ui.peminjaman.PeminjamanFragment;

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
    private RecyclerViewAdapter pModel;
    private List<Buku> tempKembali = new ArrayList<>();
    private FloatingActionButton remove;
    private int index;
    private Buku buku;
    private int idUser;
    private SharedPreferences sharedPreferences;
    private String message;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_pengembalian, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_pengembalian);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        remove = root.findViewById(R.id.kembali_button);
        pModel = new RecyclerViewAdapter(getContext(), tempKembali);

        recyclerView.setAdapter(pModel);

        tempKembali.clear();

        sharedPreferences  = getContext().getSharedPreferences("SharedPrefUser", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getInt("idUser", 0);

        getTransaksiPinjam(idUser);

        if(tempKembali.isEmpty())
        {
            Toast.makeText(getContext(), "Tidak Ada Buku yang Dipinjam", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                tempKembali = pModel.getDataBuku();
                if(tempKembali.isEmpty())
                {
                    Toast.makeText(getContext(),"Silahkan Pilih Buku Yang Akan Dikembalikan", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CharSequence [] title = getStringArray(tempKembali);
                    showDialog(title, tempKembali.size(), tempKembali);
                }
            }
        });

        return root;
    }

    private void showDialog(CharSequence[] a, int size, final List<Buku> tempBuku)
    {
        String temp = "";
        for(int i = 0; i<size;i++)
        {
            if(i<size-1)
            {
                temp =temp + a[i] + "\n\n";
            }
            else
            {
                temp =temp + a[i];
            }
        }
        new AlertDialog.Builder(getContext())
            .setTitle("Buku Yang Akan Dikembalikan")
            .setMessage(temp)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    for(int j = 0; j < size; j++)
                    {
                        editBuku(tempKembali.get(j));
                    }
                    Fragment fragment = new PengembalianFragment();
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
            })
            .create().show();
    }

    public static String[] getStringArray(List<Buku> input)
    {
        String[] strings = new String[input.size()];
        for (int j = 0; j < input.size(); j++)
        {
            strings[j] = "Judul Buku: " +  input.get(j).getJudul();
        }
        return strings;
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
                if(tempKembali.isEmpty())
                {
                    Toast.makeText(getContext(), "Tidak Ada Buku yang Dipinjam", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
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

    public void editBuku(Buku buku)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, BukuAPI.URL_UPDATE + buku.getId(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                System.out.println(error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("status", "Tersedia");

                return params;
            }
        };

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

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        int idUserDB  = Integer.parseInt(jsonObject.optString("idUser"));
                        if(idUserDB == idUser)
                        {
                            int idBukuDB  = Integer.parseInt(jsonObject.optString("idBuku"));
                            getBuku(idBukuDB);
                        }

                    }
                    pModel.notifyDataSetChanged();
                    message = response.getString("message");
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