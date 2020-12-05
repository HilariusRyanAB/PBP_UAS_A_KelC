package com.kelompokc.tubes.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.kelompokc.tubes.API.BukuAPI;
import com.kelompokc.tubes.API.PinjamAPI;
import com.kelompokc.tubes.API.SumbangAPI;
import com.kelompokc.tubes.R;
import com.kelompokc.tubes.databinding.AdapterKembaliBinding;
import com.kelompokc.tubes.model.Buku;
import com.kelompokc.tubes.model.TransaksiPinjam;
import com.kelompokc.tubes.ui.pengembalian.DialogKembali;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.PUT;

public class AdapterKembali extends RecyclerView.Adapter<AdapterKembali.MyViewHolder>
{
    private Context context;
    private AdapterKembaliBinding binding;
    private List<TransaksiPinjam> listPinjam = new ArrayList<>();

    public AdapterKembali(Context context, List<TransaksiPinjam> listPinjam)
    {
        this.context = context;
        this.listPinjam = listPinjam;
    }

    @NonNull
    @Override
    public AdapterKembali.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_kembali, parent, false);
        return new AdapterKembali.MyViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Buku buku = listPinjam.get(position).getBuku();
        TransaksiPinjam pinjam = listPinjam.get(position);
        binding.setBukuS(buku);
        holder.tgl.setText(pinjam.getTanggal());

        holder.edt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(pinjam.getIdBuku() == buku.getId())
                {
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    DialogKembali dialog = new DialogKembali();
                    dialog.show(manager, "dialog");

                    Bundle bundle = new Bundle();
                    bundle.putInt("idTransaksi", pinjam.getId());
                    bundle.putString("judul", buku.getJudul());
                    bundle.putString("genre", buku.getGenre());
                    bundle.putString("seri", buku.getNoSeri());
                    bundle.putString("tanggal", pinjam.getTanggal());
                    bundle.putString("gambar", buku.getImgURL());

                    dialog.setArguments(bundle);
                }
                else
                {
                    Toast.makeText(context, "Data Tidak Sinkron", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.hps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(pinjam.getIdBuku() == buku.getId())
                {
                    deleteTransaksiPinjam(pinjam.getId());
                    editBuku(buku.getId());
                }
                else
                {
                    Toast.makeText(context, "Data Tidak Sinkron", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listPinjam.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final AdapterKembaliBinding binding;
        private MaterialCardView itemCard;
        private MaterialTextView edt, tgl, hps;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemCard = itemView.findViewById(R.id.item_buku);
            edt = itemView.findViewById(R.id.ivEdit);
            hps = itemView.findViewById(R.id.ivHapus);
            tgl = itemView.findViewById(R.id.tanggal_kembali);
        }
        public void onClick(View view)
        {

        }
    }

    public void deleteTransaksiPinjam(int id)
    {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(DELETE, PinjamAPI.URL_DELETE + id,
                new Response.Listener<String>()
        {
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

    public void editBuku(int id)
    {
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data buku");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(PUT, BukuAPI.URL_UPDATEST + id,
                new Response.Listener<String>()
                {
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
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
