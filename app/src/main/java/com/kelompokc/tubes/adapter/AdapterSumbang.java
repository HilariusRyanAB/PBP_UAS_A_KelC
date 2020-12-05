package com.kelompokc.tubes.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.SumbangAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.databinding.AdapterRecyclerViewBinding;
import com.kelompokc.tubes.databinding.AdapterSumbangBinding;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.model.TransaksiSumbang;
import com.kelompokc.tubes.ui.sumbang.TambahEditSumbang;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class AdapterSumbang extends RecyclerView.Adapter<AdapterSumbang.MyViewHolder>
{
    private Context context;
    private int check = 0;
    private boolean b;
    private AdapterSumbangBinding binding;
    private View view;
    private List<Buku> bukuList = new ArrayList<>();
    private List<TransaksiSumbang> listSumbang = new ArrayList<>();

    public AdapterSumbang(Context context, List<TransaksiSumbang> listSumbang)
    {
        this.context = context;
        this.listSumbang = listSumbang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_sumbang, parent, false);
        return new MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {
        Buku buku = listSumbang.get(position).getBuku();
        TransaksiSumbang sumbang = listSumbang.get(position);
        binding.setBukuS(buku);
        System.out.println(buku.getId());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TambahEditSumbang tambahEditSumbang = new TambahEditSumbang("edit", buku);
                loadFragment(tambahEditSumbang);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda yakin ingin menghapus buku?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        deleteBuku(Integer.toString(buku.getId()));
                        deleteTransaksiSumbang(Integer.toString(sumbang.getId()));
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSumbang.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final AdapterSumbangBinding binding;
        private TextView ivEdit, ivHapus;
        private MaterialCardView itemCard;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemCard = itemView.findViewById(R.id.item_buku);
            ivEdit = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus = (TextView) itemView.findViewById(R.id.ivHapus);
        }

        public void onClick(View view)
        {

        }
    }

    public void loadFragment(Fragment fragment)
    {
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment_sumbang, fragment)
                .commit();
    }

    public void deleteBuku(String id)
    {
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data buku");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(DELETE, BukuAPI.URL_DELETE + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        try
                        {

                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                        catch (JSONException e)
                        {
                            Log.i("DELBUKU", "catch onResponse: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                Log.i("DELBUKU", "onError: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void deleteTransaksiSumbang(String id)
    {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(DELETE, SumbangAPI.URL_DELETE + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {

                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                        catch (JSONException e)
                        {

                        }
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }
}