package com.kelompokc.tubes.ui.peminjaman;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.QRBarcodeActivity;
import com.kelompokc.tubes.adapter.RecyclerViewAdapter;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.ui.pengembalian.ListKembali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class PeminjamanFragment extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerViewAdapter pModel;
    private FloatingActionButton add;
    private ImageView scan;
    private List<Buku> tempPinjam = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_peminjaman, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_peminjaman);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        scan = root.findViewById(R.id.button_qr);
        add = root.findViewById(R.id.button_pinjam);
        pModel = new RecyclerViewAdapter(getContext(), tempPinjam);

        recyclerView.setAdapter(pModel);

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
                    Toast.makeText(getContext(), "Silahkan Pilih Buku Yang Akan Dipinjam", Toast.LENGTH_SHORT).show();
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
                    for(int j = 0; j < size; j++)
                    {
                        editBuku(tempPinjam.get(j));
                    }
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
                progressDialog.dismiss();
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
                        }
                    }
                    pModel.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), response.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
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

        StringRequest  stringRequest = new StringRequest(POST, BukuAPI.URL_UPDATE + buku.getId(),
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
                params.put("status", "Dipinjam");

                return params;
            }
        };

        queue.add(stringRequest);
    }
}